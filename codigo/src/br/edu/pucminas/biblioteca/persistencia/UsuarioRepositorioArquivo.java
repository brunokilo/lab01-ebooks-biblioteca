package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class UsuarioRepositorioArquivo {

    private static final String ARQUIVO = "br/edu/pucminas/biblioteca/persistencia/usuarios.csv";

    public void salvar(List<Aluno> alunos, List<Bibliotecario> bibliotecarios,
                        List<EquipeBiblioteca> equipes, List<Administrador> administradores) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Aluno aluno : alunos) {
                escritor.println("ALUNO;" + aluno.getNome() + ";" + aluno.getSenha() + ";" + aluno.getMatricula());
            }
            for (Bibliotecario bibliotecario : bibliotecarios) {
                escritor.println("BIBLIOTECARIO;" + bibliotecario.getNome() + ";" + bibliotecario.getSenha()
                    + ";" + bibliotecario.getRegistroFuncional());
            }
            for (EquipeBiblioteca equipe : equipes) {
                escritor.println("EQUIPE;" + equipe.getNome() + ";" + equipe.getSenha()
                    + ";" + equipe.getRegistroFuncional());
            }
            for (Administrador administrador : administradores) {
                escritor.println("ADMINISTRADOR;" + administrador.getNome() + ";" + administrador.getSenha());
            }
        }
    }

    public UsuariosCarregados carregar() throws IOException {
        UsuariosCarregados resultado = new UsuariosCarregados();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return resultado;
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) continue;

                String[] campos = linha.split(";");
                String tipo = campos[0];
                String nome = campos[1];
                String senha = campos[2];

                switch (tipo) {
                    case "ALUNO" -> resultado.alunos.add(new Aluno(nome, senha, campos[3]));
                    case "BIBLIOTECARIO" -> resultado.bibliotecarios.add(new Bibliotecario(nome, senha, campos[3]));
                    case "EQUIPE" -> resultado.equipes.add(new EquipeBiblioteca(nome, senha, campos[3]));
                    case "ADMINISTRADOR" -> resultado.administradores.add(new Administrador(nome, senha));
                    default -> {}
                }
            }
        }
        return resultado;
    }

    public static class UsuariosCarregados {
        public final List<Aluno> alunos = new LinkedList<>();
        public final List<Bibliotecario> bibliotecarios = new LinkedList<>();
        public final List<EquipeBiblioteca> equipes = new LinkedList<>();
        public final List<Administrador> administradores = new LinkedList<>();
    }
}