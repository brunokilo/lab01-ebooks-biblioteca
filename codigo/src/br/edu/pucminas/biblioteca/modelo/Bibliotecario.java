package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;

public class Bibliotecario extends Usuario {

    String registroFuncional;

    public Bibliotecario(String id, String nome, String senha, String registroFuncional) {
        super(id, nome, senha);
        this.registroFuncional = registroFuncional;
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
