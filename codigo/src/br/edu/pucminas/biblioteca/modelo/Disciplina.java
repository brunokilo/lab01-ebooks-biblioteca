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

    /**
     * Identifica os eBooks desta disciplina cuja licenca esta expirada
     * (periodo da disciplina encerrado e acessos ativos abaixo do minimo
     * permitido), sem remove-los.
     *
     * @return lista de eBooks com licenca expirada nesta disciplina
     */
    public List<Ebook> listarEBooksComLicencaExpirada(){
        List<Ebook> expirados = new LinkedList<>();
        for (Ebook ebook : eBooks) {
            if (fimPeriodo.isBefore(LocalDate.now()) && ebook.getLicenca().acessosAtivosMenorQueMinimoPermitido()) {
                expirados.add(ebook);
            }
        }
        return expirados;
    }

    /**
     * Remove um eBook especifico da lista de eBooks indicados a esta
     * disciplina.
     */
    public void removerEBook(Ebook ebook){
        eBooks.remove(ebook);
    }
    
    public List<Ebook> listar(){
        return List.copyOf(eBooks);
    }
    
    public int getPeriodo() {
        return periodo;
    }

    public LocalDate getInicioPeriodo() {
        return inicioPeriodo;
    }

    public LocalDate getFimPeriodo() {
        return fimPeriodo;
    }
    
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString(){
        return "Disciplina: " + nome;
    }
}
