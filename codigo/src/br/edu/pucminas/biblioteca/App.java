package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import br.edu.pucminas.biblioteca.persistencia.EbookRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.UsuarioRepositorioArquivo;

public class App {

    private static final Scanner leitor = new Scanner(System.in);
    private static final EquipeBiblioteca equipe = new EquipeBiblioteca("Equipe teste", "teste", "teste");
    private static final Bibliotecario bibliotecarioPadrao = new Bibliotecario("Bibliotecario teste", "teste", "teste");
    private static final EbookRepositorioArquivo ebookRepositorio = new EbookRepositorioArquivo();
    private static final UsuarioRepositorioArquivo usuarioRepositorio = new UsuarioRepositorioArquivo();

    private static final List<Aluno> todosAlunos = new LinkedList<>();
    private static final List<Categoria> todasCategorias = new LinkedList<>();
    private static final List<Ebook> todosEbooks = new LinkedList<>();
    private static final List<Bibliotecario> todosBibliotecarios = new LinkedList<>();
    private static final List<EquipeBiblioteca> todasEquipes = new LinkedList<>();
    private static final List<Administrador> todosAdministradores = new LinkedList<>();

    public static void imprimirMenu() {
        System.out.println("\n--- Bem vindo a biblioteca: ---");
        System.out.println(" 1. Cadastrar categoria");
        System.out.println(" 2. Cadastrar eBook");
        System.out.println(" 3. Reservar eBook para um aluno");
        System.out.println(" 4. Cadastrar aluno");
        System.out.println(" 5. Consultar aluno (nome e quantidade de livros)");
        System.out.println(" 6. Listar estante de um aluno (nome e livros)");
        System.out.println(" 7. Consultar alunos que possuem um eBook");
        System.out.println(" 8. Cadastrar bibliotecario");
        System.out.println(" 9. Cadastrar equipe da biblioteca");
        System.out.println("10. Cadastrar administrador");
        System.out.println("11. Administrador: redefinir senha de um usuario");
        System.out.println("12. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    /*
    Nota de transparência sobre uso de IA: Não estava conseguindo fazer com que a função abaixo carregarDados() fizesse a leitura 
    dos dados em .csv. Então pedi para que a ferramenta Claude da Anthropic fizesse a leitura de alguns dados.
    */
    private static void carregarDados() {
        try {
            todosEbooks.addAll(ebookRepositorio.carregar());

            UsuarioRepositorioArquivo.UsuariosCarregados carregados = usuarioRepositorio.carregar();
            todosAlunos.addAll(carregados.alunos);
            todosBibliotecarios.addAll(carregados.bibliotecarios);
            todasEquipes.addAll(carregados.equipes);
            todosAdministradores.addAll(carregados.administradores);

            System.out.println("Dados carregados: " + todosEbooks.size() + " eBook(s), "
                + todosAlunos.size() + " aluno(s), " + todosBibliotecarios.size() + " bibliotecario(s), "
                + todasEquipes.size() + " equipe(s), " + todosAdministradores.size() + " administrador(es).");
        } catch (IOException e) {
            System.out.println("Nao foi possivel carregar os dados salvos: " + e.getMessage());
        }
    }

    private static void salvarEbooks() {
        try {
            ebookRepositorio.salvar(todosEbooks);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar os eBooks: " + e.getMessage());
        }
    }

    private static void salvarUsuarios() {
        try {
            usuarioRepositorio.salvar(todosAlunos, todosBibliotecarios, todasEquipes, todosAdministradores);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar os usuarios: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        carregarDados();
        boolean continuar = true;
        while (continuar) {
            imprimirMenu();

            int opcao;
            try {
                opcao = Integer.parseInt(leitor.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
                pausar();
                continue;
            }

            try {
                switch (opcao) {
                    case 1 -> cadastrarCategoria();  
                    case 2 -> cadastrarEbook();
                    case 3 -> reservarEbookParaAluno();
                    case 4 -> cadastrarAluno();
                    case 5 -> consultarAluno();
                    case 6 -> listarEstante();
                    case 7 -> consultarAlunosComEbook();
                    case 8 -> cadastrarBibliotecario();
                    case 9 -> cadastrarEquipeBiblioteca();
                    case 10 -> cadastrarAdministrador();
                    case 11 -> redefinirSenhaDeUsuario();
                    case 12 -> continuar = false;
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }

            if (continuar) {
                pausar();
            }
        }
        leitor.close();
    }

    private static void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        leitor.nextLine();
    }

    private static void cadastrarCategoria() {
        System.out.print("Descricao da categoria: ");
        String descricao = leitor.nextLine();

        Categoria categoria = equipe.cadastrarCategoria(new Categoria(descricao));
        todasCategorias.add(categoria);
        System.out.println("Categoria cadastrada: " + categoria.getDescricao());
    }

    private static void cadastrarEbook() {
        Categoria categoria = escolherOuCriarCategoria();
        if (categoria == null) return;

        System.out.print("Titulo do eBook: ");
        String titulo = leitor.nextLine();
        System.out.print("Editora: ");
        String editora = leitor.nextLine();
        String formato = escolherFormato();
        boolean obrigatorio = perguntarSeObrigatorio();

        Ebook ebook = new Ebook(titulo, editora, formato, categoria);
        ebook.setObrigatorio(obrigatorio);
        todosEbooks.add(ebook);
        System.out.println("eBook cadastrado: " + ebook.getTitulo() + " (categoria: " + categoria.getDescricao() + ")");
        salvarEbooks();
    }

    private static String escolherFormato() {
        while (true) {
            System.out.println("Formato do eBook:");
            System.out.println("1 - PDF");
            System.out.println("2 - Epub");
            System.out.print("Escolha uma opcao: ");
            String opcao = leitor.nextLine();

            if (opcao.equals("1")) return "PDF";
            if (opcao.equals("2")) return "Epub";
            System.out.println("Opcao invalida, tente novamente.");
        }
    }

    private static boolean perguntarSeObrigatorio() {
        while (true) {
            System.out.println("Este eBook e de uma disciplina obrigatoria?");
            System.out.println("1 - Sim");
            System.out.println("2 - Nao");
            System.out.print("Escolha uma opcao: ");
            String opcao = leitor.nextLine();

            if (opcao.equals("1")) return true;
            if (opcao.equals("2")) return false;
            System.out.println("Opcao invalida, tente novamente.");
        }
    }

    private static void reservarEbookParaAluno() {
        if (todosAlunos.isEmpty()) {
            System.out.println("Cadastre um aluno primeiro (opcao 4).");
            return;
        }
        if (todosEbooks.isEmpty()) {
            System.out.println("Cadastre um eBook primeiro (opcao 2).");
            return;
        }

        Aluno aluno = escolherAluno();
        if (aluno == null) return;

        System.out.print("Senha do aluno " + aluno.getNome() + ": ");
        String senha = leitor.nextLine();
        if (!aluno.autenticar(senha)) {
            System.out.println("Senha incorreta. Reserva cancelada.");
            return;
        }

        Ebook ebook = escolherEbookDaLista(todosEbooks);
        if (ebook == null) return;

        aluno.getEstante().adicionar(ebook);
        System.out.println("eBook \"" + ebook.getTitulo() + "\" reservado para " + aluno.getNome()
            + " (acessos ativos na licenca: " + ebook.getLicenca().getAcessosAtivos() + "/60)");
        salvarEbooks();
    }

    private static void cadastrarAluno() {
        System.out.print("Nome do aluno: ");
        String nome = leitor.nextLine();
        System.out.print("Senha: ");
        String senha = leitor.nextLine();
        System.out.print("Matricula: ");
        String matricula = leitor.nextLine();

        Aluno aluno = equipe.cadastrarAluno(nome, senha, matricula);
        todosAlunos.add(aluno);
        System.out.println("Aluno cadastrado: " + aluno.getNome() + " (id gerado: " + aluno.getId() + ")");
        salvarUsuarios();
    }

    private static void consultarAluno() {
        if (todosAlunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado ainda.");
            return;
        }
        Aluno aluno = escolherAluno();
        if (aluno == null) return;

        int qtdLivros = aluno.getEstante().contarEBooks();
        System.out.println(aluno.getNome() + " possui " + qtdLivros + " eBook(s) na estante.");
    }

    private static void listarEstante() {
        if (todosAlunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado ainda.");
            return;
        }
        Aluno aluno = escolherAluno();
        if (aluno == null) return;

        List<Ebook> ebooks = aluno.getEstante().listar();
        System.out.println("Estante de " + aluno.getNome() + " (" + ebooks.size() + " eBooks):");
        for (Ebook ebook : ebooks) {
            System.out.println("- " + ebook.getTitulo() + " (" + ebook.getEditora() + ", " + ebook.getFormato() + ")");
        }
    }

    private static void consultarAlunosComEbook() {
        if (todosEbooks.isEmpty()) {
            System.out.println("Nenhum eBook cadastrado ainda.");
            return;
        }
        if (todosAlunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado ainda.");
            return;
        }

        Ebook ebook = escolherEbookDaLista(todosEbooks);
        if (ebook == null) return;

        List<Aluno> alunosComEbook = bibliotecarioPadrao.consultarAlunosComEBook(todosAlunos, ebook);
        System.out.println("Alunos com este eBook na estante (" + alunosComEbook.size() + "):");
        for (Aluno aluno : alunosComEbook) {
            System.out.println("- " + aluno.getNome());
        }
    }

    private static void cadastrarBibliotecario() {
        System.out.print("Nome do bibliotecario: ");
        String nome = leitor.nextLine();
        System.out.print("Senha: ");
        String senha = leitor.nextLine();
        System.out.print("Registro funcional: ");
        String registroFuncional = leitor.nextLine();

        Bibliotecario bibliotecario = equipe.cadastrarBibliotecario(nome, senha, registroFuncional);
        todosBibliotecarios.add(bibliotecario);
        System.out.println("Bibliotecario cadastrado: " + bibliotecario.getNome());
    }

    private static void cadastrarEquipeBiblioteca() {
        System.out.print("Nome do integrante da biblioteca ");
        String nome = leitor.nextLine();
        System.out.print("Senha: ");
        String senha = leitor.nextLine();
        System.out.print("Registro funcional: ");
        String registroFuncional = leitor.nextLine();

        EquipeBiblioteca novoIntegrante = new EquipeBiblioteca(nome, senha, registroFuncional);
        todasEquipes.add(novoIntegrante);
        System.out.println("Integrante da equipe cadastrado: " + novoIntegrante.getNome());
    }

    private static void cadastrarAdministrador() {
        System.out.print("Nome do administrador: ");
        String nome = leitor.nextLine();
        System.out.print("Senha: ");
        String senha = leitor.nextLine();

        Administrador administrador = new Administrador(nome, senha);
        todosAdministradores.add(administrador);
        System.out.println("Administrador cadastrado: " + administrador.getNome());
    }

    private static void redefinirSenhaDeUsuario() {
        if (todosAdministradores.isEmpty()) {
            System.out.println("Cadastre um administrador primeiro (opcao 10).");
            return;
        }

        List<Usuario> todosUsuarios = new LinkedList<>();
        todosUsuarios.addAll(todosAlunos);
        todosUsuarios.addAll(todosBibliotecarios);
        todosUsuarios.addAll(todasEquipes);
        todosUsuarios.addAll(todosAdministradores);

        if (todosUsuarios.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado ainda.");
            return;
        }

        Administrador administrador = escolherAdministrador();
        if (administrador == null) return;

        System.out.println("Usuarios cadastrados:");
        for (int i = 0; i < todosUsuarios.size(); i++) {
            Usuario u = todosUsuarios.get(i);
            System.out.println(i + " - " + u.getNome() + " (" + u.getClass().getSimpleName() + ")");
        }
        System.out.print("Escolha o indice do usuario: ");
        int indice = Integer.parseInt(leitor.nextLine());
        if (indice < 0 || indice >= todosUsuarios.size()) {
            System.out.println("Indice invalido.");
            return;
        }
        Usuario usuarioAlvo = todosUsuarios.get(indice);

        System.out.print("Nova senha para " + usuarioAlvo.getNome() + ": ");
        String novaSenha = leitor.nextLine();

        administrador.redefinirSenha(usuarioAlvo, novaSenha);
        System.out.println("Senha de " + usuarioAlvo.getNome() + " redefinida com sucesso.");
    }

    private static Categoria escolherOuCriarCategoria() {
        if (!todasCategorias.isEmpty()) {
            System.out.println("0 - Criar nova categoria");
            for (int i = 0; i < todasCategorias.size(); i++) {
                System.out.println((i + 1) + " - " + todasCategorias.get(i).getDescricao());
            }
            System.out.print("Escolha uma opcao: ");
            int indice = Integer.parseInt(leitor.nextLine());

            if (indice == 0) {
                return criarCategoria();
            }
            if (indice >= 1 && indice <= todasCategorias.size()) {
                return todasCategorias.get(indice - 1);
            }
            System.out.println("Indice invalido.");
            return null;
        }
        System.out.println("Nenhuma categoria cadastrada ainda, vamos criar uma nova.");
        return criarCategoria();
    }

    private static Categoria criarCategoria() {
        System.out.print("Descricao da nova categoria: ");
        String descricao = leitor.nextLine();
        Categoria categoria = equipe.cadastrarCategoria(new Categoria(descricao));
        todasCategorias.add(categoria);
        return categoria;
    }

    private static Aluno escolherAluno() {
        System.out.println("Alunos disponiveis:");
        for (int i = 0; i < todosAlunos.size(); i++) {
            System.out.println(i + " - " + todosAlunos.get(i).getNome());
        }
        System.out.print("Escolha o indice do aluno: ");
        int indice = Integer.parseInt(leitor.nextLine());
        if (indice < 0 || indice >= todosAlunos.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return todosAlunos.get(indice);
    }

    private static Ebook escolherEbookDaLista(List<Ebook> lista) {
        System.out.println("eBooks disponiveis:");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(i + " - " + lista.get(i));
        }
        System.out.print("Escolha o indice do eBook: ");
        int indice = Integer.parseInt(leitor.nextLine());
        if (indice < 0 || indice >= lista.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return lista.get(indice);
    }

    private static Administrador escolherAdministrador() {
        if (todosAdministradores.size() == 1) {
            return todosAdministradores.get(0);
        }
        System.out.println("Administradores disponiveis:");
        for (int i = 0; i < todosAdministradores.size(); i++) {
            System.out.println(i + " - " + todosAdministradores.get(i).getNome());
        }
        System.out.print("Escolha o indice do administrador: ");
        int indice = Integer.parseInt(leitor.nextLine());
        if (indice < 0 || indice >= todosAdministradores.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return todosAdministradores.get(indice);
    }
}