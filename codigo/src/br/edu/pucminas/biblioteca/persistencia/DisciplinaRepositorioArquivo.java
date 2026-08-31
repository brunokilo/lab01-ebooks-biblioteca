package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Disciplina;
import br.edu.pucminas.biblioteca.modelo.Ebook;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DisciplinaRepositorioArquivo {

    private static final String ARQUIVO = "codigo/src/br/edu/pucminas/biblioteca/persistencia/disciplinas.csv";

    public void salvar(List<Disciplina> disciplinas) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Disciplina disciplina : disciplinas) {
                String titulosEbooks = String.join(",", titulosDosEbooks(disciplina));

                escritor.println(
                    disciplina.getPeriodo() + ";" +
                    disciplina.getNome() + ";" +
                    disciplina.getInicioPeriodo() + ";" +
                    disciplina.getFimPeriodo() + ";" +
                    titulosEbooks
                );
            }
        }
    }

    private List<String> titulosDosEbooks(Disciplina disciplina) {
        List<String> titulos = new ArrayList<>();
        for (Ebook ebook : disciplina.listar()) {
            titulos.add(ebook.getTitulo());
        }
        return titulos;
    }

    public List<Disciplina> carregar(List<Ebook> todosEbooksJaCarregados) throws IOException {
        List<Disciplina> disciplinas = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return disciplinas;
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) continue;

                String[] campos = linha.split(";", -1);
                int periodo = Integer.parseInt(campos[0]);
                String nome = campos[1];
                LocalDate inicioPeriodo = LocalDate.parse(campos[2]);
                LocalDate fimPeriodo = LocalDate.parse(campos[3]);
                String titulosEbooks = campos.length > 4 ? campos[4] : "";

                Disciplina disciplina = new Disciplina(periodo, inicioPeriodo, fimPeriodo, nome);
                religarEbooks(disciplina, titulosEbooks, todosEbooksJaCarregados);

                disciplinas.add(disciplina);
            }
        }
        return disciplinas;
    }

    private void religarEbooks(Disciplina disciplina, String titulosEbooks, List<Ebook> todosEbooks) {
        if (titulosEbooks.isBlank()) return;

        for (String titulo : titulosEbooks.split(",")) {
            buscarPorTitulo(todosEbooks, titulo).ifPresent(disciplina::indicarEBooK);
        }
    }

    private Optional<Ebook> buscarPorTitulo(List<Ebook> ebooks, String titulo) {
        return ebooks.stream()
            .filter(ebook -> ebook.getTitulo().equals(titulo))
            .findFirst();
    }
}