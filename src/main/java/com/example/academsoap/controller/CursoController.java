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
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public CursoController(CursoRepository cursoRepository, DisciplinaRepository disciplinaRepository) {
        this.cursoRepository = cursoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Curso buscarPorId(@PathVariable Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso nao encontrado: " + id));
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Curso criar(@RequestBody Curso curso) {
        curso.setId(null);
        normalizarIdsDisciplinas(curso.getDisciplinas());
        sincronizarDisciplinas(curso, curso.getDisciplinas());
        return cursoRepository.save(curso);
    }

    @PutMapping("/{id}")
    @Transactional
    public Curso atualizar(@PathVariable Long id, @RequestBody Curso cursoAtualizado) {
        Curso cursoExistente = buscarPorId(id);
        cursoExistente.setCodigo(cursoAtualizado.getCodigo());
        cursoExistente.setNome(cursoAtualizado.getNome());
        cursoExistente.setDuracao(cursoAtualizado.getDuracao());
        normalizarIdsDisciplinas(cursoAtualizado.getDisciplinas());
        sincronizarDisciplinas(cursoExistente, cursoAtualizado.getDisciplinas());
        return cursoRepository.save(cursoExistente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        Curso curso = buscarPorId(id);
        limparRelacoes(curso);
        cursoRepository.delete(curso);
    }

    private void sincronizarDisciplinas(Curso curso, List<Disciplina> novasDisciplinas) {
        List<Disciplina> copia = novasDisciplinas == null ? Collections.emptyList() : new ArrayList<>(novasDisciplinas);
        limparRelacoes(curso);
        for (Disciplina disciplina : copia) {
            disciplina.setId(normalizarId(disciplina.getId()));
            Disciplina associada = disciplina.getId() == null
                    ? disciplina
                    : disciplinaRepository.findById(disciplina.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Disciplina nao encontrada: " + disciplina.getId()));
            if (!associada.getCursos().contains(curso)) {
                associada.getCursos().add(curso);
            }
            if (!curso.getDisciplinas().contains(associada)) {
                curso.getDisciplinas().add(associada);
            }
        }
    }

    private void normalizarIdsDisciplinas(List<Disciplina> disciplinas) {
        if (disciplinas == null) {
            return;
        }
        for (Disciplina disciplina : disciplinas) {
            disciplina.setId(normalizarId(disciplina.getId()));
        }
    }

    private void limparRelacoes(Curso curso) {
        for (Disciplina disciplina : new ArrayList<>(curso.getDisciplinas())) {
            disciplina.getCursos().remove(curso);
        }
        curso.getDisciplinas().clear();
    }

    private Long normalizarId(Long id) {
        return id == null || id <= 0 ? null : id;
    }
}
