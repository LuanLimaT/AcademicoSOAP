package com.example.academsoap.ws;

import com.example.academsoap.modelo.Curso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

@WebService(serviceName = "SistemaAcademicoService")
public class SistemaAcademicoWS {

    private static final List<Curso> CURSOS = Collections.synchronizedList(new ArrayList<>());

    @WebMethod
    public List<Curso> listarCursos() {
        return CURSOS;
    }

    @WebMethod
    public Curso buscarCursoPorCodigo(@WebParam(name = "codigo") String codigo) {
        if (codigo == null) {
            return null;
        }
        synchronized (CURSOS) {
            for (Curso curso : CURSOS) {
                if (codigo.equalsIgnoreCase(curso.getCodigo())) {
                    return curso;
                }
            }
        }
        return null;
    }

    @WebMethod
    public String adicionarCurso(
            @WebParam(name = "curso") Curso curso,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "senha") String senha) {

        validarCredenciais(usuario, senha);
        if (curso == null) {
            throw new WebServiceException("Curso inválido para inserção.");
        }
        CURSOS.add(curso);
        return "Curso adicionado com sucesso.";
    }

    private void validarCredenciais(String usuario, String senha) {
        if (!"admin".equals(usuario) || !"123".equals(senha)) {
            throw new WebServiceException("Erro de autenticação ao adicionar cursos.");
        }
    }
}
