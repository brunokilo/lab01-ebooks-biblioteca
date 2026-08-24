package br.edu.pucminas.biblioteca.modelo;

public class Ebook {
    private String titulo;
    private String editora;
    private  String formato;
    private Categoria categoria; 
    private boolean obrigatorio;
    private Licenca licenca;
    private Disciplina disciplina;

    public Ebook (String titulo, String editora, String formato, Categoria categoria, Disciplina disciplina){
        this.titulo = titulo;
        this.editora = editora;
        this.formato = formato;
        this.categoria = categoria;
        this.obrigatorio = false;
        this.disciplina = disciplina;
    }

    public boolean verificaQtdMinimaEbook(){
        // TODO: implementar na Sprint 3
        return false;
    }

    public void removerPorLicencaExpirada(Ebook ebook ){
        // TODO: implementar na Sprint 3
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }
}
