package br.edu.pucminas.biblioteca.modelo;
public class Ebook {
    private String titulo;
    private String editora;
    private String formato;
    private Categoria categoria; 
    private boolean obrigatorio;
    private Licenca licenca;

    private void init(String titulo, String editora, String formato, Categoria categoria){
        if (titulo == null || titulo.isBlank()) 
            throw new IllegalArgumentException("Titulo do Ebook não pode ser vazio");
        
        if (editora == null || editora.isBlank()) 
            throw new IllegalArgumentException("Editora do Ebook não pode ser vazio");
        
        if (formato == null || formato.isBlank()) 
            throw new IllegalArgumentException("Formato do Ebook não pode ser vazio");
        
        this.titulo = titulo;
        this.editora = editora;
        this.formato = formato;
        this.categoria = categoria;
        this.obrigatorio = false;
        this.licenca = new Licenca();
    }
    
    public Ebook (String titulo, String editora, String formato, Categoria categoria){
        init(titulo, editora, formato, categoria);
    }

    public void editar(String titulo, String editora, String formato, Categoria categoria){
        init(titulo, editora, formato, categoria);
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

    public String getTitulo() {
        return titulo;
    }

    public String getEditora() {
        return editora;
    }

    public String getFormato() {
        return formato;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public String toString(){
        return "Titulo: " + getTitulo() + "| Obrigatoriedade: " + obrigatorio;
    }
}
