package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Categoria;
import br.edu.pucminas.biblioteca.modelo.Ebook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EbookRepositorioArquivo {

    private static final String ARQUIVO = "codigo/src/br/edu/pucminas/biblioteca/persistencia/ebooks.csv";

    public void salvar(List<Ebook> ebooks) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Ebook ebook : ebooks) {
                String categoriaDescricao = ebook.getCategoria() != null
                    ? ebook.getCategoria().getDescricao()
                    : "";

                escritor.println(
                    ebook.getTitulo() + ";" +
                    ebook.getEditora() + ";" +
                    ebook.getFormato() + ";" +
                    categoriaDescricao + ";" +
                    ebook.isObrigatorio() + ";" +
                    ebook.getLicenca().getAcessosAtivos()
                );
            }
        }
    }

public List<Ebook> carregar(List<Categoria> todasCategoriasJaCarregadas) throws IOException {
    List<Ebook> ebooks = new ArrayList<>();
    File arquivo = new File(ARQUIVO);
    if (!arquivo.exists()) {
        return ebooks;
    }

    try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
        String linha;
        while ((linha = leitor.readLine()) != null) {
            if (linha.isBlank()) continue;

            String[] campos = linha.split(";");
            String titulo = campos[0];
            String editora = campos[1];
            String formato = campos[2];
            String categoriaDescricao = campos[3];
            boolean obrigatorio = Boolean.parseBoolean(campos[4]);
            int acessosAtivos = Integer.parseInt(campos[5]);

            Categoria categoria = buscarPorDescricao(todasCategoriasJaCarregadas, categoriaDescricao)
                .orElseGet(() -> new Categoria(categoriaDescricao));

            Ebook ebook = new Ebook(titulo, editora, formato, categoria);
            ebook.setObrigatorio(obrigatorio);

            for (int i = 0; i < acessosAtivos; i++) {
                ebook.getLicenca().incrementarAcessosAtivos();
            }

            ebooks.add(ebook);
        }
    }
    return ebooks;
}

private Optional<Categoria> buscarPorDescricao(List<Categoria> categorias, String descricao) {
    return categorias.stream()
        .filter(categoria -> categoria.getDescricao().equals(descricao))
        .findFirst();
}
}