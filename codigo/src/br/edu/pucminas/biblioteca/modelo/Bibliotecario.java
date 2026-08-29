package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;

public class Bibliotecario extends Usuario {
    String registroFuncional;

    private void init (String registroFuncional){
        if (registroFuncional == null || registroFuncional.isBlank()) 
            throw new IllegalArgumentException("Registro Funcional do bibliotecário não pode ser vazio");
        this.registroFuncional = registroFuncional;
    }

    public Bibliotecario(String nome, String senha, String registroFuncional) {
        super(nome, senha);
        init(registroFuncional);
    }

    public void editar(String nome, String senha, String registroFuncional){
        super.editar(nome, senha);
        init(registroFuncional);
    }

    public List<Aluno> consultarAlunosComEBook(List<Aluno> alunos, Ebook ebook){
        List<Aluno> alunosComEbbok = new LinkedList<>();
        for (Aluno aluno : alunos) {
            if (aluno.getEstante().temEbook(ebook)) {
                alunosComEbbok.add(aluno);
            }
        }
        return alunosComEbbok;
    }
    
}
