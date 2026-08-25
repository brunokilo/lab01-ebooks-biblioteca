package br.edu.pucminas.biblioteca.modelo;

public class Aluno extends Usuario {
    private String matricula;
    private Estante estante;

    
    public Aluno(String id, String nome, String senha, String matricula) {
        super(id, nome, senha);
        this.matricula = matricula;
    }
    
    public Estante getEstante() {
        return estante;
    }
}
