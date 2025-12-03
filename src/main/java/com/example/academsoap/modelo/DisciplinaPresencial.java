package com.example.academsoap.modelo;

public class DisciplinaPresencial extends Disciplina {

    private String numeroSala;
    private boolean possuiLaboratorio;

    public DisciplinaPresencial() {
    }

    public DisciplinaPresencial(String nome, int cargaHoraria, String numeroSala, boolean possuiLaboratorio) {
        super(nome, cargaHoraria);
        this.numeroSala = numeroSala;
        this.possuiLaboratorio = possuiLaboratorio;
    }

    public String getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(String numeroSala) {
        this.numeroSala = numeroSala;
    }

    public boolean isPossuiLaboratorio() {
        return possuiLaboratorio;
    }

    public void setPossuiLaboratorio(boolean possuiLaboratorio) {
        this.possuiLaboratorio = possuiLaboratorio;
    }
}
