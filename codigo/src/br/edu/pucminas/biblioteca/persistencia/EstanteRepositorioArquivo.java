package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.Ebook;
import java.io.*;
import java.util.List;

public class EstanteRepositorioArquivo {

    private static final String ARQUIVO = "codigo/src/br/edu/pucminas/biblioteca/persistencia/estantes.csv";

    public void salvar(List<Aluno> alunos) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Aluno aluno : alunos) {
                for (Ebook ebook : aluno.getEstante().listar()) {
                    escritor.println(
                        aluno.getMatricula() + ";" +
                        ebook.getTitulo() + ";" +
                        ebook.getEditora() + ";" +
                        ebook.getFormato()
                    );
                }
            }
        }
    }

    public void carregar(List<Aluno> alunos, List<Ebook> ebooks) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) continue;

                String[] campos = linha.split(";");
                String matricula = campos[0];
                String titulo = campos[1];
                String editora = campos[2];
                String formato = campos[3];

                Aluno aluno = buscarAlunoPorMatricula(alunos, matricula);
                Ebook ebook = buscarEbook(ebooks, titulo, editora, formato);

                if (aluno != null && ebook != null) {
                    aluno.getEstante().carregarSemValidacao(ebook);
                }
            }
        }
    }

    private Aluno buscarAlunoPorMatricula(List<Aluno> alunos, String matricula) {
        for (Aluno aluno : alunos) {
            if (aluno.getMatricula().equals(matricula)) {
                return aluno;
            }
        }
        return null;
    }

    private Ebook buscarEbook(List<Ebook> ebooks, String titulo, String editora, String formato) {
        for (Ebook ebook : ebooks) {
            if (ebook.getTitulo().equals(titulo)
                && ebook.getEditora().equals(editora)
                && ebook.getFormato().equals(formato)) {
                return ebook;
            }
        }
        return null;
    }
}