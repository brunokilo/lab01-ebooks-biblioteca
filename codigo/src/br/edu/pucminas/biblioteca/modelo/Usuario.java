package br.edu.pucminas.biblioteca.modelo;

public class Usuario {
    
    private String id;
    private String nome;
    private String senha;


    public Usuario (String id, String nome, String senha){
        if(id == null || nome == null || senha == null){
            throw new IllegalArgumentException("Ocorreu um erro com um dos dados de criação, tente novamente");
        }
        
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    protected void autenticar(){
        //TODO: Implementar na Sprint 3
    }
}
