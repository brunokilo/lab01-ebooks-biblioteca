package br.edu.pucminas.biblioteca.modelo;

public class EquipeBiblioteca extends Usuario {

    private String registroFuncional;

    public EquipeBiblioteca(String nome, String senha, String registroFuncional) {
        super(nome, senha);
        if (registroFuncional == null || registroFuncional.isBlank())
            throw new IllegalArgumentException("Registro Funcional não pode ser vazio");
        this.registroFuncional = registroFuncional;
    }

    public Aluno cadastrarAluno(String nome, String senha, String matricula) {
        return new Aluno(nome, senha, matricula);
    }

    public Bibliotecario cadastrarBibliotecario(String nome, String senha, String registroFuncional) {
        return new Bibliotecario(nome, senha, registroFuncional);
    }

    public Disciplina cadastrarDisciplina(Disciplina disciplina) {
        if (disciplina == null)
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        return disciplina;
    }

    public Categoria cadastrarCategoria(Categoria categoria) {
        if (categoria == null)
            throw new IllegalArgumentException("Categoria não pode ser nula");
        return categoria;
    }

    public String getRegistroFuncional() {
        return registroFuncional;
    }
}