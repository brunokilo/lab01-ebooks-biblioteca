package br.edu.pucminas.biblioteca.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Disciplina {
    private int periodo;
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;
    private String nome;
    private List<Ebook> eBooks;

    public Disciplina(int periodo, LocalDate inicioPeriodo, LocalDate fimPeriodo, String nome){
        this.periodo = periodo;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
        this.nome = nome;
        this.eBooks = new ArrayList<>();
    }

    public void indicarEBooK(Ebook eBook){
        // TODO: implementar na Sprint 3
    }
}
