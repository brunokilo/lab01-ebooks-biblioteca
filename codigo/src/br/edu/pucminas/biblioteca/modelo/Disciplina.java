package br.edu.pucminas.biblioteca.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Disciplina {
    //TODO implementar o resto das exceções
    private int periodo;
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;
    private String nome;
    private final List<Ebook> eBooks;

    private void init(int periodo, LocalDate inicioPeriodo, LocalDate fimPeriodo, String nome){
        this.periodo = periodo;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
        this.nome = nome;
    }
    
    public Disciplina(int periodo, LocalDate inicioPeriodo, LocalDate fimPeriodo, String nome){
        init(periodo, inicioPeriodo, fimPeriodo, nome);
        this.eBooks = new LinkedList<>();
    }

    public void editar(int periodo, LocalDate inicioPeriodo, LocalDate fimPeriodo, String nome){
        init(periodo, inicioPeriodo, fimPeriodo, nome);
    }

    public void indicarEBooK(Ebook eBook){
        eBooks.add(eBook);
    }

    public void removerEbookPorLicencaExpirada(){
        eBooks.removeIf(ebook ->
            fimPeriodo.isBefore(LocalDate.now()) && ebook.getLicenca().acessosAtivosMenorQueMinimoPermitido()
        );
    }
}
