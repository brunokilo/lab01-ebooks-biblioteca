package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Categoria;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorioArquivo {

    private static final String ARQUIVO = "codigo/src/br/edu/pucminas/biblioteca/persistencia/categorias.csv";

    public void salvar(List<Categoria> categorias) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Categoria categoria : categorias) {
                escritor.println(categoria.getDescricao());
            }
        }
    }

    public List<Categoria> carregar() throws IOException {
        List<Categoria> categorias = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return categorias;
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) continue;
                categorias.add(new Categoria(linha));
            }
        }
        return categorias;
    }
}