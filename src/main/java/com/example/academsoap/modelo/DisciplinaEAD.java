package com.example.academsoap.modelo;

public class DisciplinaEAD extends Disciplina {

    private String linkPlataforma;
    private String senhaAcesso;

    public DisciplinaEAD() {
    }

    public DisciplinaEAD(String nome, int cargaHoraria, String linkPlataforma, String senhaAcesso) {
        super(nome, cargaHoraria);
        this.linkPlataforma = linkPlataforma;
        this.senhaAcesso = senhaAcesso;
    }

    public String getLinkPlataforma() {
        return linkPlataforma;
    }

    public void setLinkPlataforma(String linkPlataforma) {
        this.linkPlataforma = linkPlataforma;
    }

    public String getSenhaAcesso() {
        return senhaAcesso;
    }

    public void setSenhaAcesso(String senhaAcesso) {
        this.senhaAcesso = senhaAcesso;
    }
}
