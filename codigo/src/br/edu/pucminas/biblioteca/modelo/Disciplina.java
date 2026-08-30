package br.edu.pucminas.biblioteca.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

//Nota de Transparência sobre Uso de IA
// pedi auxilio do claude para sugestão de exceções. Também pedi uma sugestão de melhoria de escrita para o método removerEbookPorLicencaExpirada()

public class Disciplina {
    private int periodo;
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;
    private String nome;
    private final List<Ebook> eBooks;

    private void init(int periodo, LocalDate inicioPeriodo, LocalDate fimPeriodo, String nome){
        if (fimPeriodo.isBefore(inicioPeriodo))
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início");
        if (periodo <= 0) 
            throw new IllegalArgumentException("Período deve ser maior que zero");
        if (nome == null || nome.isBlank()) 
            throw new IllegalArgumentException("Nome da disciplina não pode ser vazio");
        if (inicioPeriodo == null || fimPeriodo == null)
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");

        
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
        if (eBook == null) 
            throw new IllegalArgumentException("Ebook não pode ser nulo");
        if (eBooks.contains(eBook)) 
            throw new IllegalArgumentException("Ebook já indicado nesta disciplina");
        eBooks.add(eBook);
    }

    public void removerEbookPorLicencaExpirada(){
        eBooks.removeIf(ebook ->
            fimPeriodo.isBefore(LocalDate.now()) && ebook.getLicenca().acessosAtivosMenorQueMinimoPermitido()
        );
    }

    @Override
    public String toString(){
        return "Disciplina: " + nome;
    }
}
