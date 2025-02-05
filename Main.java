import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Tarefa> tarefas = new ArrayList<>(); // Lista de tarefas
    private static final String ARQUIVO_TAREFAS = "tarefas.txt"; // Nome do arquivo de persistência

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        carregarTarefasDeArquivo(); // Carrega tarefas ao iniciar

        while (true) {
            try {
                exibirMenu();
                int opcao = solicitarInteiro(scanner, "Escolha uma opção: ", 0, 5);
                switch (opcao) {
                    case 1 -> {
                        criarTarefa(scanner);
                        salvarTarefasNoArquivo(); // Salva após criar
                    }
                    case 2 -> listarTarefas();
                    case 3 -> filtrarTarefasPorStatus(scanner);
                    case 4 -> {
                        editarTarefa(scanner);
                        salvarTarefasNoArquivo(); // Salva após editar
                    }
                    case 5 -> {
                        removerTarefa(scanner);
                        salvarTarefasNoArquivo(); // Salva após remover
                    }
                    case 0 -> {
                        System.out.println("Saindo...");
                        scanner.close();
                        return; // Encerra o programa
                    }
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

    // Função para carregar as tarefas do arquivo
    private static void carregarTarefasDeArquivo() {
        File arquivo = new File(ARQUIVO_TAREFAS);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_TAREFAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Tarefa tarefa = Tarefa.fromCSV(linha);
                if (tarefa != null) {
                    tarefas.add(tarefa);
                } else {
                    System.out.println("Tarefa inválida encontrada no arquivo e ignorada.");
                }
            }
            System.out.println("Tarefas carregadas com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }

    // Função para salvar as tarefas no arquivo
    private static void salvarTarefasNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_TAREFAS))) {
            for (Tarefa tarefa : tarefas) {
                writer.write(tarefa.toCSV());
                writer.newLine();
            }
            System.out.println("Tarefas salvas com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    // Função que exibe o menu principal
    private static void exibirMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Criar nova tarefa");
        System.out.println("2. Listar todas as tarefas");
        System.out.println("3. Filtrar tarefas por status");
        System.out.println("4. Editar tarefa");
        System.out.println("5. Remover tarefa");
        System.out.println("0. Sair");
    }

    // Função para criar uma nova tarefa
    private static void criarTarefa(Scanner scanner) {
        System.out.print("Digite o título da tarefa: ");
        String titulo = scanner.nextLine();

        System.out.print("Digite a descrição da tarefa: ");
        String descricao = scanner.nextLine();

        LocalDate dataVencimento = solicitarData(scanner);

        System.out.println("Selecione o status da tarefa:");
        System.out.println("1. Pendente");
        System.out.println("2. Em progresso");
        System.out.println("3. Concluída");
        int statusOpcao = solicitarInteiro(scanner, "Escolha uma opção: ", 1, 3);

        Status status = Status.values()[statusOpcao - 1];
        Tarefa novaTarefa = new Tarefa(titulo, descricao, dataVencimento, status);
        tarefas.add(novaTarefa);
        System.out.println("Tarefa criada com sucesso!");
    }

    // Função para solicitar e validar a data de vencimento
    private static LocalDate solicitarData(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print("Digite a data de vencimento (YYYY-MM-DD): ");
            String entrada = scanner.nextLine();
            try {
                LocalDate data = LocalDate.parse(entrada, formatter);
                if (!data.isBefore(LocalDate.now())) {
                    return data;
                }
                System.out.println("A data de vencimento não pode ser no passado.");
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato YYYY-MM-DD.");
            }
        }
    }

    // Função para listar todas as tarefas
    private static void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            System.out.println("Lista de Tarefas:");
            for (int i = 0; i < tarefas.size(); i++) {
                System.out.println("/nTarefa " + (i + 1) + ":");
                tarefas.get(i).exibirTarefa();
                System.out.println("-------------------------");
            }
        }
    }

    // Função para filtrar tarefas por status
    private static void filtrarTarefasPorStatus(Scanner scanner) {
        System.out.println("Selecione o status para filtrar:");
        System.out.println("1. Pendente");
        System.out.println("2. Em progresso");
        System.out.println("3. Concluída");
        int statusOpcao = solicitarInteiro(scanner, "/nEscolha uma opção: ", 1, 3);

        Status status = Status.values()[statusOpcao - 1];
        List<Tarefa> tarefasFiltradas = tarefas.stream()
                .filter(tarefa -> tarefa.getStatus() == status)
                .toList();

        if (tarefasFiltradas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com o status " + status + ".");
        } else {
            System.out.println("Tarefas com status " + status + ":");
            tarefasFiltradas.forEach(tarefa -> {
                tarefa.exibirTarefa();
                System.out.println("-------------------------");
            });
        }
    }

    // Função para editar uma tarefa existente
    private static void editarTarefa(Scanner scanner) {
        listarTarefas();
        int indice = solicitarInteiro(scanner, "Digite o número da tarefa que deseja editar: ", 1, tarefas.size()) - 1;

        Tarefa tarefa = tarefas.get(indice);

        System.out.print("Digite o novo título (ou pressione Enter para manter): ");
        String novoTitulo = scanner.nextLine();
        if (!novoTitulo.isEmpty()) {
            tarefa.setTitulo(novoTitulo);
        }

        System.out.print("Digite a nova descrição (ou pressione Enter para manter): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.isEmpty()) {
            tarefa.setDescricao(novaDescricao);
        }

        System.out.print("Deseja alterar a data de vencimento? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            tarefa.setDataVencimento(solicitarData(scanner));
        }

        System.out.println("Selecione o novo status:");
        System.out.println("1. Pendente");
        System.out.println("2. Em progresso");
        System.out.println("3. Concluída");
        int statusOpcao = solicitarInteiro(scanner, "Escolha uma opção: ", 1, 3);

        tarefa.setStatus(Status.values()[statusOpcao - 1]);
        System.out.println("Tarefa editada com sucesso!");
    }

   // Função para remover uma tarefa com confirmação
    private static void removerTarefa(Scanner scanner) {
        // Listar as tarefas disponíveis
        listarTarefas();

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa disponível para remover.");
            return;
        }

        // Solicitar o número da tarefa a ser removida
        int indice = solicitarInteiro(scanner, 
            "Digite o número da tarefa que deseja remover: ", 
            1, tarefas.size()) - 1;

        // Exibir os detalhes da tarefa selecionada para confirmação
        Tarefa tarefaSelecionada = tarefas.get(indice);
        System.out.println("\nVocê selecionou a tarefa:");
        System.out.println("Título: " + tarefaSelecionada.getTitulo());
        System.out.println("Descrição: " + tarefaSelecionada.getDescricao());
        System.out.println("Data de vencimento: " + tarefaSelecionada.getDataVencimento());
        System.out.println("Tem certeza de que deseja remover esta tarefa? (s/n)");

        // Confirmar a remoção
        String confirmacao = scanner.nextLine().toLowerCase();
        if (confirmacao.equals("s")) {
            tarefas.remove(indice);
            System.out.println("Tarefa removida com sucesso!");
        } else {
            System.out.println("Remoção cancelada pelo usuário.");
        }
    }


    // Função genérica para solicitar um inteiro com limites
    private static int solicitarInteiro(Scanner scanner, String mensagem, int min, int max) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Entrada fora do intervalo permitido (" + min + " a " + max + ").");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
            }
        }
    }
}
