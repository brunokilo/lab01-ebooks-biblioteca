package br.edu.pucminas.biblioteca.modelo;

public class Usuario {

    private final String id;
    private static int proximoId = 1;
    private String nome;
    private String senha;

    public Usuario(String nome, String senha) {
        if (nome == null || senha == null) {
            throw new IllegalArgumentException("Ocorreu um erro com um dos dados de criação, tente novamente");
        }
        this.id = String.valueOf(proximoId++);
        this.nome = nome;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    protected void editar(String nome, String senha) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio");
        this.nome = nome;
        this.senha = senha;
    }

    public boolean autenticar(String senhaDigitadaPeloUsuario) {
        return this.senha != null && this.senha.equals(senhaDigitadaPeloUsuario);
    }
}