package br.edu.pucminas.biblioteca.modelo;

import java.util.LinkedList;
import java.util.List;

public class Estante {
    private final int maxQtdObrigatorio = 4;
    private final int maxQtdNaoObrigatorio = 2;
    private final List<Ebook> eBooks;

    public Estante(){
        this.eBooks = new LinkedList<>();
    }

    public void adicionar(Ebook ebook){
       if (ebook.isObrigatorio() && contarEBooksObrigatorios() < maxQtdObrigatorio) {
            eBooks.add(ebook);
       } else if (!ebook.isObrigatorio() && contarEBooksNaoObrigatorio() < maxQtdNaoObrigatorio) {
            eBooks.add(ebook);
       }
        
    } 
    
    public void remover(Ebook ebook){
        eBooks.remove(ebook);
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
