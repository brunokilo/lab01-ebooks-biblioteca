package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.*;
import br.edu.pucminas.biblioteca.persistencia.CategoriaRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.DisciplinaRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.EbookRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.EstanteRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.UsuarioRepositorioArquivo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Nota de transparencia sobre uso de IA:
 * Usei o claude para gerar a documentação deste código para facilitar a leitura 
 */

/**
 * Aplicacao de console para o sistema de biblioteca de eBooks da PUC Minas.
 * Permite que a equipe da biblioteca cadastre eBooks, disciplinas, alunos e
 * demais usuarios, e que alunos e bibliotecarios realizem consultas e
 * reservas sobre o acervo.
 */
public class App {

    // #region configuracao e estado global

    static Scanner leitor;
    private static final EquipeBiblioteca equipe = new EquipeBiblioteca("Equipe teste", "teste", "teste");
    private static final Bibliotecario bibliotecarioPadrao = new Bibliotecario("Bibliotecario teste", "teste", "teste");
    private static final EbookRepositorioArquivo ebookRepositorio = new EbookRepositorioArquivo();
    private static final UsuarioRepositorioArquivo usuarioRepositorio = new UsuarioRepositorioArquivo();
    private static final EstanteRepositorioArquivo estanteRepositorio = new EstanteRepositorioArquivo();
    private static final DisciplinaRepositorioArquivo disciplinaRepositorio = new DisciplinaRepositorioArquivo();
    private static final CategoriaRepositorioArquivo categoriaRepositorio = new CategoriaRepositorioArquivo();
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final List<Aluno> todosAlunos = new LinkedList<>();
    private static final List<Categoria> todasCategorias = new LinkedList<>();
    private static final List<Ebook> todosEbooks = new LinkedList<>();
    private static final List<Bibliotecario> todosBibliotecarios = new LinkedList<>();
    private static final List<EquipeBiblioteca> todasEquipes = new LinkedList<>();
    private static final List<Administrador> todosAdministradores = new LinkedList<>();
    private static final List<Disciplina> todasDisciplinas = new LinkedList<>();
    
    // #endregion

    // #region persistencia de dados
    
    /**
     * Carrega todos os dados persistidos em arquivo (eBooks, usuarios e
     * estantes) para as listas em memoria. Deve ser chamado uma unica vez,
     * no inicio da execucao.
     *
     * Nota de transparencia sobre uso de IA: Nao estava conseguindo fazer
     * com que esta funcao fizesse a leitura dos dados em .csv. Entao pedi
     * para que a ferramenta Claude da Anthropic fizesse a funcao da leitura
     * de alguns dados.
     */
    private static void carregarDados() {
        try {
            todasCategorias.addAll(categoriaRepositorio.carregar());
            todosEbooks.addAll(ebookRepositorio.carregar(todasCategorias));

            UsuarioRepositorioArquivo.UsuariosCarregados carregados = usuarioRepositorio.carregar();
            todosAlunos.addAll(carregados.alunos);
            todosBibliotecarios.addAll(carregados.bibliotecarios);
            todasEquipes.addAll(carregados.equipes);
            todosAdministradores.addAll(carregados.administradores);
            estanteRepositorio.carregar(todosAlunos, todosEbooks);
            todasDisciplinas.addAll(disciplinaRepositorio.carregar(todosEbooks));
            removerEbooksComLicencaExpirada();

            System.out.println("Dados carregados: " + todosEbooks.size() + " eBook(s), "
                + todosAlunos.size() + " aluno(s), " + todosBibliotecarios.size() + " bibliotecario(s), "
                + todasEquipes.size() + " equipe(s), " + todosAdministradores.size() + " administrador(es).");
        } catch (IOException e) {
            System.out.println("Nao foi possivel carregar os dados salvos: " + e.getMessage());
        }
    }

    /**
     * Persiste a lista atual de eBooks em arquivo.
     */
    private static void salvarEbooks() {
        try {
            ebookRepositorio.salvar(todosEbooks);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar os eBooks: " + e.getMessage());
        }
    }

    /**
     * Persiste as listas de alunos, bibliotecarios, equipes e
     * administradores em arquivo.
     */
    private static void salvarUsuarios() {
        try {
            usuarioRepositorio.salvar(todosAlunos, todosBibliotecarios, todasEquipes, todosAdministradores);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar os usuarios: " + e.getMessage());
        }
    }

    /**
     * Persiste o conteudo das estantes de todos os alunos em arquivo.
     */
    private static void salvarEstantes() {
        try {
            estanteRepositorio.salvar(todosAlunos);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar as estantes: " + e.getMessage());
        }
    }

    /**
     * Persiste a lista atual de disciplinas (e os eBooks indicados a cada
     * uma) em arquivo.
     */
    private static void salvarDisciplinas() {
        try {
            disciplinaRepositorio.salvar(todasDisciplinas);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar as disciplinas: " + e.getMessage());
        }
    }

    /**
     * Persiste a lista atual de categorias em arquivo.
     */
    private static void salvarCategorias() {
        try {
            categoriaRepositorio.salvar(todasCategorias);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar as categorias: " + e.getMessage());
        }
    }

    /**
     * Remove do sistema por completo os eBooks com licenca expirada: da
     * disciplina, das estantes de todos os alunos que os possuem, e da lista
     * geral de eBooks. Executado uma unica vez, na inicializacao do programa.
     */
    private static void removerEbooksComLicencaExpirada() {
        List<Ebook> ebooksRemovidos = new LinkedList<>();

        for (Disciplina disciplina : todasDisciplinas) {
            for (Ebook ebook : disciplina.listarEBooksComLicencaExpirada()) {
                disciplina.removerEBook(ebook);
                ebooksRemovidos.add(ebook);
            }
        }

        if (ebooksRemovidos.isEmpty()) return;

        for (Ebook ebook : ebooksRemovidos) {
            for (Aluno aluno : todosAlunos) {
                if (aluno.getEstante().listar().contains(ebook)) {
                    aluno.getEstante().remover(ebook);
                }
            }
            todosEbooks.remove(ebook);
        }
        
        System.out.println(ebooksRemovidos.size() + " eBook(s) removido(s) do sistema por licenca expirada.");
        salvarDisciplinas();
        salvarEbooks();
        salvarEstantes();
    }

    // #endregion

    // #region menu e execucao principal

    /**
     * Exibe o menu principal com todas as opcoes disponiveis, agrupadas por
     * perfil de usuario (equipe da biblioteca, aluno, administrador e
     * bibliotecario).
     */
    public static void imprimirMenu() {
        System.out.println("\n--- Bem vindo a biblioteca: ---");

        System.out.println("\n--- Menu Equipe da biblioteca: ---");
        System.out.println(" 1. Cadastrar categoria");
        System.out.println(" 2. Cadastrar eBook");
        System.out.println(" 3. Cadastrar aluno");
        System.out.println(" 4. Cadastrar bibliotecario");
        System.out.println(" 5. Cadastrar equipe da biblioteca");
        System.out.println(" 6. Cadastrar administrador");
        System.out.println(" 7. Cadastrar disciplina");
        System.out.println(" 8. Indicar eBook a uma disciplina");

        System.out.println("\n--- Menu Aluno: ---");
        System.out.println(" 9. Reservar eBook para um aluno");
        System.out.println(" 10. Listar estante de um aluno (nome e livros)");

        System.out.println("\n--- Menu Administrador: ---");
        System.out.println(" 11. Administrador: redefinir senha de um usuario");

        System.out.println("\n--- Menu Bibliotecario: ---");
        System.out.println(" 12. Consultar aluno (nome e quantidade de livros)");
        System.out.println(" 13. Consultar alunos que possuem um eBook");

        System.out.println("\n 14. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    /**
     * Ponto de entrada da aplicacao. Carrega os dados persistidos, exibe o
     * menu em loop e direciona a opcao escolhida para o metodo
     * correspondente ate que o usuario opte por sair.
     *
     * @param args argumentos de linha de comando (nao utilizados)
     */
    public static void main(String[] args) {
        leitor = new Scanner(System.in);
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
                    case 3 -> cadastrarAluno();
                    case 4 -> cadastrarBibliotecario();
                    case 5 -> cadastrarEquipeBiblioteca();
                    case 6 -> cadastrarAdministrador();
                    case 7 -> cadastrarDisciplina();
                    case 8 -> indicarEbookADisciplina();
                    case 9 -> reservarEbookParaAluno();
                    case 10 -> listarEstante();
                    case 11 -> redefinirSenhaDeUsuario();
                    case 12 -> consultarAluno();
                    case 13 -> consultarAlunosComEbook();
                    case 14 -> continuar = false;
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

    // #endregion

    // #region cadastros (equipe da biblioteca)

    /**
     * Le os dados de uma nova categoria pelo teclado e a cadastra atraves da
     * equipe da biblioteca.
     */
    private static void cadastrarCategoria() {
        System.out.print("Descricao da categoria: ");
        String descricao = leitor.nextLine();

        Categoria categoria = equipe.cadastrarCategoria(new Categoria(descricao));
        todasCategorias.add(categoria);
        System.out.println("Categoria cadastrada: " + categoria.getDescricao());
        salvarCategorias();
    }

    /**
     * Cadastra um novo eBook. Exige que ao menos uma disciplina ja exista,
     * pois todo eBook e associado a uma disciplina no momento do cadastro.
     */
    private static void cadastrarEbook() {
        if (todasDisciplinas.isEmpty()) {
            System.out.println("Cadastre uma disciplina primeiro (opcao 7).");
            return;
        }

        if (todasCategorias.isEmpty()) {
            System.out.println("Cadastre uma categoria primeiro (opcao 1).");
            return;
        }

        Disciplina disciplina = escolherDisciplina();
        if (disciplina == null) return;

        Categoria categoria = escolherCategoria();
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
        disciplina.indicarEBooK(ebook);
        System.out.println("eBook cadastrado: " + ebook.getTitulo() + " (categoria: " + categoria.getDescricao() + ")");
        salvarEbooks();
        salvarDisciplinas();
    }

    /**
     * Le os dados de um novo aluno pelo teclado e o cadastra atraves da
     * equipe da biblioteca, persistindo em seguida.
     */
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

    /**
     * Le os dados de um novo bibliotecario pelo teclado e o cadastra
     * atraves da equipe da biblioteca.
     */
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

    /**
     * Le os dados de um novo integrante da equipe da biblioteca pelo
     * teclado e o cadastra.
     */
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

    /**
     * Le os dados de um novo administrador pelo teclado e o cadastra.
     */
    private static void cadastrarAdministrador() {
        System.out.print("Nome do administrador: ");
        String nome = leitor.nextLine();
        System.out.print("Senha: ");
        String senha = leitor.nextLine();

        Administrador administrador = new Administrador(nome, senha);
        todosAdministradores.add(administrador);
        System.out.println("Administrador cadastrado: " + administrador.getNome());
    }

    /**
     * Le os dados de uma nova disciplina (nome, periodo e datas de inicio e
     * fim) pelo teclado e a cadastra atraves da equipe da biblioteca.
     */
    private static void cadastrarDisciplina() {
        System.out.print("Nome da disciplina: ");
        String nome = leitor.nextLine();
        System.out.print("Periodo (numero, ex: 1): ");
        int periodo = Integer.parseInt(leitor.nextLine());
        System.out.print("Data de inicio (DD/MM/AAAA): ");
        LocalDate inicio = LocalDate.parse(leitor.nextLine(), FORMATO_DATA);
        System.out.print("Data de fim (DD//MM/AAAA): ");
        LocalDate fim = LocalDate.parse(leitor.nextLine(), FORMATO_DATA);

        Disciplina disciplina = equipe.cadastrarDisciplina(new Disciplina(periodo, inicio, fim, nome));
        todasDisciplinas.add(disciplina);
        System.out.println("Disciplina cadastrada: " + disciplina);
        salvarDisciplinas();
    }

    /**
     * Associa um eBook ja existente a uma disciplina adicional, alem
     * daquela informada no momento do seu cadastro.
     */
    private static void indicarEbookADisciplina() {
        if (todasDisciplinas.isEmpty()) {
            System.out.println("Cadastre uma disciplina primeiro (opcao 7).");
            return;
        }
        if (todosEbooks.isEmpty()) {
            System.out.println("Cadastre um eBook primeiro (opcao 2).");
            return;
        }

        Disciplina disciplina = escolherDisciplina();
        if (disciplina == null) return;

        Ebook ebook = escolherEbookDaLista(todosEbooks);
        if (ebook == null) return;

        disciplina.indicarEBooK(ebook);
        System.out.println("eBook \"" + ebook.getTitulo() + "\" indicado a disciplina \"" + disciplina + "\"");
        salvarDisciplinas();
    }

    // #endregion

    // #region acoes do aluno

    /**
     * Reserva um eBook para um aluno, apos autenticacao por senha. O aluno
     * escolhe primeiro a disciplina e, em seguida, um eBook dentre os
     * indicados para ela.
     */
    private static void reservarEbookParaAluno() {
        if (todosAlunos.isEmpty()) {
            System.out.println("Cadastre um aluno primeiro (opcao 3).");
            return;
        }
        if (todasDisciplinas.isEmpty()) {
            System.out.println("Cadastre uma disciplina primeiro (opcao 7).");
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

        Disciplina disciplina = escolherDisciplina();
        if (disciplina == null) return;

        List<Ebook> ebooksDaDisciplina = disciplina.listar();
        if (ebooksDaDisciplina.isEmpty()) {
            System.out.println("Esta disciplina nao possui eBooks indicados ainda.");
            return;
        }

        Ebook ebook = escolherEbookDaLista(ebooksDaDisciplina);
        if (ebook == null) return;

        aluno.getEstante().adicionar(ebook);
        System.out.println("eBook \"" + ebook.getTitulo() + "\" reservado para " + aluno.getNome()
            + " (acessos ativos na licenca: " + ebook.getLicenca().getAcessosAtivos() + "/60)");
        salvarEbooks();
        salvarEstantes();
    }

    /**
     * Lista os eBooks presentes na estante de um aluno escolhido.
     */
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

    // #endregion

    // #region acoes do administrador

    /**
     * Permite que um administrador redefina a senha de qualquer usuario
     * cadastrado no sistema (aluno, bibliotecario, equipe ou outro
     * administrador).
     */
    private static void redefinirSenhaDeUsuario() {
        if (todosAdministradores.isEmpty()) {
            System.out.println("Cadastre um administrador primeiro (opcao 6).");
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

    // #endregion

    // #region acoes do bibliotecario

    /**
     * Exibe a quantidade de eBooks presentes na estante de um aluno
     * escolhido.
     */
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

    /**
     * Lista todos os alunos que possuem um determinado eBook na estante.
     */
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

    // #endregion

    // #region metodos auxiliares de entrada e escolha

    /**
     * Interrompe a execucao ate que o usuario pressione Enter, dando tempo
     * de ler a saida anterior antes de exibir o menu novamente.
     */
    private static void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        leitor.nextLine();
    }

    /**
     * Pergunta repetidamente pelo formato do eBook ate que uma opcao valida
     * (PDF ou Epub) seja informada.
     *
     * @return "PDF" ou "Epub", conforme a escolha do usuario
     */
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

    /**
     * Pergunta repetidamente se o eBook pertence a uma disciplina
     * obrigatoria, ate que uma opcao valida seja informada.
     *
     * @return true se o eBook e obrigatorio, false caso contrario
     */
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

    /**
     * Exibe as disciplinas cadastradas para escolha pelo indice.
     *
     * @return a disciplina escolhida, ou null se o indice informado for
     *         invalido
     */
    private static Disciplina escolherDisciplina() {
        System.out.println("Disciplinas disponiveis:");
        for (int i = 0; i < todasDisciplinas.size(); i++) {
            System.out.println(i + " - " + todasDisciplinas.get(i));
        }
        System.out.print("Escolha o indice da disciplina: ");
        int indice = Integer.parseInt(leitor.nextLine());
        if (indice < 0 || indice >= todasDisciplinas.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return todasDisciplinas.get(indice);
    }

    /**
     * Exibe os alunos cadastrados para escolha pelo indice.
     *
     * @return o aluno escolhido, ou null se o indice informado for invalido
     */
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

    /**
     * Exibe os eBooks de uma lista informada para escolha pelo indice.
     *
     * @param lista lista de eBooks a partir da qual a escolha sera feita
     * @return o eBook escolhido, ou null se o indice informado for invalido
     */
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

    /**
     * Exibe os administradores cadastrados para escolha pelo indice. Se
     * houver apenas um administrador, ele e retornado diretamente, sem
     * exigir escolha.
     *
     * @return o administrador escolhido, ou null se o indice informado for
     *         invalido
     */
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

    /**
     * Exibe as categorias cadastradas para escolha pelo indice.
     *
     * @return a categoria escolhida, ou null se o indice informado for
     *         invalido
     */
    private static Categoria escolherCategoria() {
        System.out.println("Categorias disponiveis:");
        for (int i = 0; i < todasCategorias.size(); i++) {
            System.out.println(i + " - " + todasCategorias.get(i).getDescricao());
        }
        System.out.print("Escolha o indice da categoria: ");
        int indice = Integer.parseInt(leitor.nextLine());
        if (indice < 0 || indice >= todasCategorias.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return todasCategorias.get(indice);
    }

    // #endregion
}