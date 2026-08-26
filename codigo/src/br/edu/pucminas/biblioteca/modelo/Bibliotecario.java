package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;

public class Bibliotecario extends Usuario {
    //TODO implementar o resto das exceções
    String registroFuncional;

    private void init (String registroFuncional){
        this.registroFuncional = registroFuncional;
    }

    public Bibliotecario(String id, String nome, String senha, String registroFuncional) {
        super(id, nome, senha);
        this.registroFuncional = registroFuncional;
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
