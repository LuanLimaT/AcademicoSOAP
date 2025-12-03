package com.example.academsoap.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Curso {

    private String codigo;
    private String nome;
    private int duracao;
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Curso() {
    }

    public Curso(String codigo, String nome, int duracao, List<Disciplina> disciplinas) {
        this.codigo = codigo;
        this.nome = nome;
        this.duracao = duracao;
        if (disciplinas != null) {
            this.disciplinas = disciplinas;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    @XmlElementWrapper(name = "disciplinas")
    @XmlElement(name = "disciplina")
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
