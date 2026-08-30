package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class EquipeBiblioteca extends Usuario {
 
    private String registroFuncional;
    private static final List<Aluno> alunos = new LinkedList<>();
    private static final List<Bibliotecario> bibliotecarios = new LinkedList<>();
    private static final List<Disciplina> disciplinas = new LinkedList<>();
    private static final List<Categoria> categorias = new LinkedList<>();

    public EquipeBiblioteca(String nome, String senha, String registroFuncional) {
        super(nome, senha);
        if (registroFuncional == null || registroFuncional.isBlank())
            throw new IllegalArgumentException("Registro Funcional não pode ser vazio");
        this.registroFuncional = registroFuncional;
    }

    public Aluno cadastrarAluno(String nome, String senha, String matricula) {
        Aluno aluno = new Aluno(nome, senha, matricula);
        alunos.add(aluno);
        return aluno;
    }

    public Aluno editarAluno(Aluno aluno, String nome, String matricula) {
        if (!alunos.contains(aluno))
            throw new NoSuchElementException("Aluno não encontrado");
        aluno.editar(nome, matricula);
        return aluno;
    }

    public Aluno removerAluno(Aluno aluno) {
        if (!alunos.remove(aluno))
            throw new NoSuchElementException("Aluno não encontrado ou já removido");
        return aluno;
    }

    public Bibliotecario cadastrarBibliotecario(String nome, String senha, String registroFuncional) {
        Bibliotecario bibliotecario = new Bibliotecario(nome, senha, registroFuncional);
        bibliotecarios.add(bibliotecario);
        return bibliotecario;
    }

    public Bibliotecario editarBibliotecario(Bibliotecario bibliotecario, String nome, String senha, String registroFuncional) {
        if (!bibliotecarios.contains(bibliotecario))
            throw new NoSuchElementException("Bibliotecario não encontrado");
        bibliotecario.editar(nome, senha, registroFuncional);
        return bibliotecario;
    }

    public Bibliotecario removerBibliotecario(Bibliotecario bibliotecario) {
        if (!bibliotecarios.remove(bibliotecario))
            throw new NoSuchElementException("Bibliotecario não encontrado ou já removido");
        return bibliotecario;
    }

    public Disciplina cadastrarDisciplina(Disciplina disciplina) {
        if (disciplina == null)
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        disciplinas.add(disciplina);
        return disciplina;
    }

    public Disciplina editarDisciplina(Disciplina disciplina, int periodo, java.time.LocalDate inicioPeriodo,
                                        java.time.LocalDate fimPeriodo, String nome) {
        if (!disciplinas.contains(disciplina))
            throw new NoSuchElementException("Disciplina não encontrada");
        disciplina.editar(periodo, inicioPeriodo, fimPeriodo, nome);
        return disciplina;
    }

    public Disciplina removerDisciplina(Disciplina disciplina) {
        if (!disciplinas.remove(disciplina))
            throw new NoSuchElementException("Disciplina não encontrada ou já removida");
        return disciplina;
    }


    public Categoria cadastrarCategoria(Categoria categoria) {
        if (categoria == null)
            throw new IllegalArgumentException("Categoria não pode ser nula");
        categorias.add(categoria);
        return categoria;
    }

    public Categoria editarCategoria(Categoria categoria, String descricao) {
        if (!categorias.contains(categoria))
            throw new NoSuchElementException("Categoria não encontrada");
        categoria.editar(descricao);
        return categoria;
    }

    public Categoria removerCategoria(Categoria categoria) {
        if (!categorias.remove(categoria))
            throw new NoSuchElementException("Categoria não encontrada ou já removida");
        return categoria;
    }

    public String getRegistroFuncional() {
        return registroFuncional;
    }
}