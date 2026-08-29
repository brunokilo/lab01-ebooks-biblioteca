package br.edu.pucminas.biblioteca.modelo;

public class Usuario {
    
    private final String id;
    private static int proximoId = 1;
    private String nome;
    private String senha;


    public Usuario (String nome, String senha){
        if(nome == null || senha == null){
            throw new IllegalArgumentException("Ocorreu um erro com um dos dados de criação, tente novamente");
        }
        this.id = String.valueOf(proximoId++);
        this.nome = nome;
        this.senha = senha;
    }

    protected boolean autenticar(String senhaDigitadaPeloUsuario){
        if (this.senha != senhaDigitadaPeloUsuario){
            throw new IllegalAccessError("A senha digitada esta incorreta!");
        }
        return this.senha != null && this.senha.equals(senhaDigitadaPeloUsuario);
    }
}
