package br.edu.pucminas.biblioteca.modelo;
public class Ebook {
    //TODO implementar o resto das exceções
    private String titulo;
    private String editora;
    private String formato;
    private Categoria categoria; 
    private boolean obrigatorio;
    private Licenca licenca;

    private void init(String titulo, String editora, String formato, Categoria categoria, Disciplina disciplina){
        this.titulo = titulo;
        this.editora = editora;
        this.formato = formato;
        this.categoria = categoria;
        this.obrigatorio = false;
    }
    
    public Ebook (String titulo, String editora, String formato, Categoria categoria, Disciplina disciplina){
        init(titulo, editora, formato, categoria, disciplina);
        this.licenca = new Licenca();
    }

    public void editar(String titulo, String editora, String formato, Categoria categoria, Disciplina disciplina){
        init(titulo, editora, formato, categoria, disciplina);
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public Licenca getLicenca() {
        return licenca;
    }
}
