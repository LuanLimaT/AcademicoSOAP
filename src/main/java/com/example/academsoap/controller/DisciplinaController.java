package com.example.academsoap.controller;

import com.example.academsoap.modelo.Curso;
import com.example.academsoap.modelo.Disciplina;
import com.example.academsoap.repository.CursoRepository;
import com.example.academsoap.repository.DisciplinaRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Disciplina> listarDisciplinas() {
        return disciplinaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Disciplina buscarPorId(@PathVariable Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina nao encontrada: " + id));
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Disciplina criar(@RequestBody Disciplina disciplina) {
        disciplina.setId(null);
        normalizarIdsCursos(disciplina.getCursos());
        sincronizarCursos(disciplina, disciplina.getCursos());
        return disciplinaRepository.save(disciplina);
    }

    @PutMapping("/{id}")
    @Transactional
    public Disciplina atualizar(@PathVariable Long id, @RequestBody Disciplina dadosAtualizados) {
        Disciplina disciplinaExistente = buscarPorId(id);
        disciplinaExistente.setNome(dadosAtualizados.getNome());
        disciplinaExistente.setCargaHoraria(dadosAtualizados.getCargaHoraria());
        normalizarIdsCursos(dadosAtualizados.getCursos());
        sincronizarCursos(disciplinaExistente, dadosAtualizados.getCursos());
        return disciplinaRepository.save(disciplinaExistente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        Disciplina disciplina = buscarPorId(id);
        desvincularCursos(disciplina);
        disciplinaRepository.delete(disciplina);
    }

    private void sincronizarCursos(Disciplina disciplina, List<Curso> novosCursos) {
        List<Curso> copia = novosCursos == null ? Collections.emptyList() : new ArrayList<>(novosCursos);
        desvincularCursos(disciplina);
        for (Curso curso : copia) {
            curso.setId(normalizarId(curso.getId()));
            if (curso.getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Informe o id do curso para vincular a disciplina");
            }
            Curso cursoGerenciado = cursoRepository.findById(curso.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Curso nao encontrado: " + curso.getId()));
            if (!cursoGerenciado.getDisciplinas().contains(disciplina)) {
                cursoGerenciado.getDisciplinas().add(disciplina);
            }
            if (!disciplina.getCursos().contains(cursoGerenciado)) {
                disciplina.getCursos().add(cursoGerenciado);
            }
        }
    }

    private void normalizarIdsCursos(List<Curso> cursos) {
        if (cursos == null) {
            return;
        }
        for (Curso curso : cursos) {
            curso.setId(normalizarId(curso.getId()));
        }
    }

    private void desvincularCursos(Disciplina disciplina) {
        for (Curso curso : new ArrayList<>(disciplina.getCursos())) {
            curso.getDisciplinas().remove(disciplina);
        }
        disciplina.getCursos().clear();
    }

    private Long normalizarId(Long id) {
        return id == null || id <= 0 ? null : id;
    }
}
