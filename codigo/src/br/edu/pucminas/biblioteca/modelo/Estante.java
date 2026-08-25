package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Estante {
    private final int maxQtdObrigatorio = 4;
    private final int maxQtdNaoObrigatorio = 2;
    private final List<Ebook> eBooks;

    public Estante(){
        this.eBooks = new LinkedList<>();
    }

    public void adicionar(Ebook ebook){
        if (!ebook.getLicenca().temVagaDisponivel(ebook)) {
            throw new IllegalStateException("Licença expirada ou sem vagas disponíveis");
        }

        boolean temEspacoNaEstante = ebook.isObrigatorio() 
            ? contarEBooksObrigatorios() < maxQtdObrigatorio 
            : contarEBooksNaoObrigatorio() < maxQtdNaoObrigatorio;

        if (temEspacoNaEstante) {
            eBooks.add(ebook);
            ebook.getLicenca().incrementarAcessosAtivos();
        }
    }
    
    public void remover(Ebook ebook){
        if (!eBooks.remove(ebook)) {
            throw new NoSuchElementException ("Ebook não existe na estante ou já foi removido");
        }
        ebook.getLicenca().decrementarAcessosAtivos();
    }
    
    public List<Ebook> listar(){
        return List.copyOf(eBooks);
    }

    public int contarEBooks(){
        return eBooks.size();
    }

    public int contarEBooksObrigatorios(){
        int cont = 0;
        for (Ebook ebook : eBooks) {
            if (ebook.isObrigatorio()) {
                cont ++;
            }
        }
        return cont;
    }

    public int contarEBooksNaoObrigatorio(){
        int cont = 0;
        for (Ebook ebook : eBooks) {
            if (!ebook.isObrigatorio()) {
                cont ++;
            }
        }
        return cont;
    }
}
