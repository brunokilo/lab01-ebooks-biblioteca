package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

//Nota de Transparência sobre Uso de IA
// pedi auxilio do claude para sugestão de melhoria de escrita para o método adicionar(Ebook ebook) e listar()

public class Estante {
    private final int maxQtdObrigatorio = 4;
    private final int maxQtdNaoObrigatorio = 2;
    private final List<Ebook> eBooks;

    public Estante(){
        this.eBooks = new LinkedList<>();
    }

    public void adicionar(Ebook ebook){
        if (!ebook.getLicenca().temVagaDisponivel(ebook))
            throw new IllegalStateException("Licença expirada ou sem vagas disponíveis");
        if (temEbook(ebook))
            throw new IllegalStateException("EBook já existe na estante");

        boolean temEspacoNaEstante = ebook.isObrigatorio() 
            ? contarEBooksObrigatorios() < maxQtdObrigatorio 
            : contarEBooksNaoObrigatorio() < maxQtdNaoObrigatorio;

        if (temEspacoNaEstante) {
            eBooks.add(ebook);
            ebook.getLicenca().incrementarAcessosAtivos();
        }
    }
    
    public void remover(Ebook ebook){
        if (!eBooks.remove(ebook))
            throw new NoSuchElementException ("Ebook não existe na estante ou já foi removido");
        ebook.getLicenca().decrementarAcessosAtivos();
    }
    
    public List<Ebook> listar(){
        return List.copyOf(eBooks);
    }

    public int contarEBooks(){
        return eBooks.size();
    }

    private int contarEBooksObrigatorios(){
        int cont = 0;
        for (Ebook ebook : eBooks) {
            if (ebook.isObrigatorio()) {
                cont ++;
            }
        }
        return cont;
    }

    private int contarEBooksNaoObrigatorio(){
        int cont = 0;
        for (Ebook ebook : eBooks) {
            if (!ebook.isObrigatorio()) {
                cont ++;
            }
        }
        return cont;
    }

    public boolean temEbook(Ebook ebook){
        for (Ebook eBook : eBooks) {
            if (eBook.equals(ebook)) {
                return true;
            }            
        }
        return false;
    }

    /**
     * Adiciona um eBook diretamente a estante, sem validar limites de cota
     * ou disponibilidade de licenca, e sem incrementar o contador de acessos
     * ativos da licenca. Uso exclusivo para reconstrucao do estado a partir
     * de dados persistidos, onde essas validacoes ja ocorreram no momento do
     * cadastro original.
     */
    public void carregarSemValidacao(Ebook ebook) {
        eBooks.add(ebook);
    }
}
