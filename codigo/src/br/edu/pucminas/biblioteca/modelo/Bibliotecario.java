package br.edu.pucminas.biblioteca.modelo;

import java.util.List;

public class Bibliotecario extends Usuario {

    String registroFuncional;

    public Bibliotecario(String id, String nome, String senha, String registroFuncional) {
        super(id, nome, senha);
        this.registroFuncional = registroFuncional;
    }

    public List consultarAlunosComEBook(Ebook ebook){
        //TODO: Implementar na Sprint 3.

        return consultarAlunosComEBook(ebook);
    }
    
}
