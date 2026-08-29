package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.*;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void imprimirMenu() {
        System.out.println("\n--- Bem vindo a biblioteca: ---");
        System.out.println("1. Cadastrar aluno");
        System.out.println("2. Adicionar eBook a estante do aluno");
        System.out.println("3. Listar estante do aluno");
        System.out.println("4. Redefinir senha do usuário");
        System.out.println(". Sair");
        System.out.print("Escolha uma opcao: ");
    }

    public static void main(String[] args) {

        Scanner leitor = new Scanner(System.in);
        EquipeBiblioteca equipe = new EquipeBiblioteca("Equipe Central", "senha123", "REG-001");
        Aluno alunoAtual = null;

        boolean continuar = true;
        while (continuar) {
            imprimirMenu();

            int opcao;
            try {
                opcao = Integer.parseInt(leitor.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
                continue;
            }

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("Id do aluno: ");
                        String id = leitor.nextLine();
                        System.out.print("Nome do aluno: ");
                        String nome = leitor.nextLine();
                        System.out.print("Senha: ");
                        String senha = leitor.nextLine();
                        System.out.print("Matricula: ");
                        String matricula = leitor.nextLine();
                        alunoAtual = equipe.cadastrarAluno(id, nome, senha, matricula);
                        System.out.println("Aluno cadastrado: " + alunoAtual.getNome());
                        break;

                    case 2:
                        if (alunoAtual == null) {
                            System.out.println("Cadastre um aluno primeiro (opcao 1).");
                            break;
                        }
                        System.out.print("Titulo do eBook: ");
                        String titulo = leitor.nextLine();
                        System.out.print("Editora: ");
                        String editora = leitor.nextLine();
                        System.out.print("Formato: ");
                        String formato = leitor.nextLine();

                        Categoria categoria = new Categoria("Categoria Teste");
                        Ebook ebook = new Ebook(titulo, editora, formato, categoria);

                        alunoAtual.getEstante().adicionar(ebook);
                        System.out.println("eBook adicionado a estante de " + alunoAtual.getNome());
                        break;

                    case 3:
                        if (alunoAtual == null) {
                            System.out.println("Cadastre um aluno primeiro (opcao 1).");
                            break;
                        }
                        List<Ebook> ebooks = alunoAtual.getEstante().listar();
                        System.out.println("Estante de " + alunoAtual.getNome() + " (" + ebooks.size() + " eBooks):");
                        for (Ebook e : ebooks) {
                            System.out.println("- " + e.getTitulo() + " (" + e.getEditora() + ", " + e.getFormato() + ")");
                        }
                        break;

                    case 4:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opcao invalida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
        leitor.close();
    }
}