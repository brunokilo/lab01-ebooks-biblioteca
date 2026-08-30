package br.edu.pucminas.biblioteca.modelo;

public class Categoria {
    
    private String descricao;

    public Categoria(String categoria) {
        if (categoria == null)
            throw new IllegalArgumentException("Descrição da categoria não pode ser nula");
        this.descricao = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void editar(String categoria){
        if (categoria == null || categoria.isBlank())
            throw new IllegalArgumentException("Descrição da categoria não pode ser vazia");
        this.descricao = categoria;
    }
}
