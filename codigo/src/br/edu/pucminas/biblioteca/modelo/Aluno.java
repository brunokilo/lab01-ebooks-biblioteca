package br.edu.pucminas.biblioteca.modelo;

public class Aluno extends Usuario {
    private String matricula;
    private Estante estante;

    private void init (String matricula){
        if (matricula == null || matricula.isBlank()) 
            throw new IllegalArgumentException("Matricula do aluno não pode ser vazio");
        this.matricula = matricula;
        this.estante = new Estante();
    }

    public Aluno(String nome, String senha, String matricula) {
        super(nome, senha);
        init(matricula);
    }

    public String getMatricula() {
        return matricula;
    }

    public void editar(String nome, String senha, String matricula){
        super.editar(nome, senha);
        init(matricula);
    }
    
    public Estante getEstante() {
        return estante;
    }
}
