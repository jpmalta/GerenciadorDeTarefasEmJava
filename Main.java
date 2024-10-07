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
        carregarTarefasDeArquivo(); // Carregar as tarefas salvas no arquivo

        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (true) {
            exibirMenu(); // Chamada da função que exibe o menu
            try {
                System.out.print("Escolha uma opção: ");
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        criarTarefa(scanner); // Função para criar nova tarefa
                        salvarTarefasNoArquivo(); // Salvar no arquivo após criar
                        break;
                    case 2:
                        listarTarefas(); // Função para listar todas as tarefas
                        break;
                    case 3:
                        filtrarTarefasPorStatus(scanner); // Função para filtrar tarefas por status
                        break;
                    case 4:
                        editarTarefa(scanner); // Função para editar tarefas
                        salvarTarefasNoArquivo(); // Salvar no arquivo após editar
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        salvarTarefasNoArquivo(); // Salvar no arquivo ao sair
                        return; // Sai do programa
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
            }
        }
    }

    // Função que exibe o menu principal
    private static void exibirMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Criar nova tarefa");
        System.out.println("2. Listar todas as tarefas");
        System.out.println("3. Filtrar tarefas por status");
        System.out.println("4. Editar tarefa");
        System.out.println("0. Sair");
    }

    // Função para criar uma nova tarefa
    private static void criarTarefa(Scanner scanner) {
        System.out.print("Digite o título da tarefa: ");
        String titulo = scanner.nextLine();

        System.out.print("Digite a descrição da tarefa: ");
        String descricao = scanner.nextLine();

        LocalDate dataVencimento = solicitarData(scanner);

        int statusOpcao = -1;
        while (statusOpcao < 1 || statusOpcao > 3) {
            try {
                System.out.println("Selecione o status da tarefa:");
                System.out.println("1. Pendente");
                System.out.println("2. Em progresso");
                System.out.println("3. Concluída");
                System.out.print("Escolha uma opção: ");
                statusOpcao = Integer.parseInt(scanner.nextLine());

                if (statusOpcao < 1 || statusOpcao > 3) {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
            }
        }

        Status status = Status.PENDENTE;
        if (statusOpcao == 2) {
            status = Status.EM_PROGRESSO;
        } else if (statusOpcao == 3) {
            status = Status.CONCLUIDA;
        }

        // Criar nova tarefa e adicionar à lista
        Tarefa novaTarefa = new Tarefa(titulo, descricao, dataVencimento, status);
        tarefas.add(novaTarefa);
        System.out.println("Tarefa criada com sucesso!");
    }

    // Função para solicitar e validar a data de vencimento
    private static LocalDate solicitarData(Scanner scanner) {
        LocalDate dataVencimento = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d"); // Aceita formato sem zeros

        while (dataVencimento == null) {
            System.out.print("Digite a data de vencimento (YYYY-MM-DD): ");
            String data = scanner.nextLine();
            try {
                // Faz o parsing aceitando entrada como "2024-10-2" e ajustando para "2024-10-02"
                dataVencimento = LocalDate.parse(data, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data incorreta, digite novamente no formato correto (YYYY-MM-DD).");
            }
        }
        return dataVencimento;
    }

    // Função para listar todas as tarefas
    private static void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            System.out.println("Lista de Tarefas:");
            for (int i = 0; i < tarefas.size(); i++) {
                System.out.println("Tarefa " + (i + 1) + ":");
                tarefas.get(i).exibirTarefa();
                System.out.println("-------------------------");
            }
        }
    }

    // Função para filtrar tarefas por status
    private static void filtrarTarefasPorStatus(Scanner scanner) {
        int statusOpcao = -1;

        while (statusOpcao < 1 || statusOpcao > 3) {
            try {
                System.out.println("Selecione o status para filtrar:");
                System.out.println("1. Pendente");
                System.out.println("2. Em progresso");
                System.out.println("3. Concluída");
                System.out.print("Escolha uma opção: ");
                statusOpcao = Integer.parseInt(scanner.nextLine());

                if (statusOpcao < 1 || statusOpcao > 3) {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
            }
        }

        Status status = Status.PENDENTE;
        if (statusOpcao == 2) {
            status = Status.EM_PROGRESSO;
        } else if (statusOpcao == 3) {
            status = Status.CONCLUIDA;
        }

        // Filtrar as tarefas pelo status selecionado
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getStatus() == status) {
                tarefasFiltradas.add(tarefa);
            }
        }

        if (tarefasFiltradas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com o status " + status + ".");
        } else {
            System.out.println("Tarefas com status " + status + ":");
            for (Tarefa tarefa : tarefasFiltradas) {
                tarefa.exibirTarefa();
                System.out.println("-------------------------");
            }
        }
    }

    // Função para editar uma tarefa existente
    private static void editarTarefa(Scanner scanner) {
        listarTarefas(); // Exibe todas as tarefas para escolha
        System.out.print("Digite o número da tarefa que deseja editar: ");
        int indice = Integer.parseInt(scanner.nextLine()) - 1;

        if (indice >= 0 && indice < tarefas.size()) {
            Tarefa tarefa = tarefas.get(indice);

            System.out.print("Digite o novo título (ou deixe em branco para manter o atual): ");
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.isEmpty()) {
                tarefa.setTitulo(novoTitulo);
            }

            System.out.print("Digite a nova descrição (ou deixe em branco para manter a atual): ");
            String novaDescricao = scanner.nextLine();
            if (!novaDescricao.isEmpty()) {
                tarefa.setDescricao(novaDescricao);
            }

            System.out.print("Deseja alterar a data de vencimento? (s/n): ");
            String alterarData = scanner.nextLine();
            if (alterarData.equalsIgnoreCase("s")) {
                tarefa.setDataVencimento(solicitarData(scanner));
            }

            int statusOpcao = -1;
            while (statusOpcao < 1 || statusOpcao > 3) {
                try {
                    System.out.println("Selecione o novo status da tarefa:");
                    System.out.println("1. Pendente");
                    System.out.println("2. Em progresso");
                    System.out.println("3. Concluída");
                    System.out.print("Escolha uma opção: ");
                    statusOpcao = Integer.parseInt(scanner.nextLine());

                    if (statusOpcao < 1 || statusOpcao > 3) {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Por favor, insira um número.");
                }
            }

            Status novoStatus = Status.PENDENTE;
            if (statusOpcao == 2) {
                novoStatus = Status.EM_PROGRESSO;
            } else if (statusOpcao == 3) {
                novoStatus = Status.CONCLUIDA;
            }
            tarefa.setStatus(novoStatus);

            System.out.println("Tarefa atualizada com sucesso!");
        } else {
            System.out.println("Número de tarefa inválido.");
        }
    }

    // Função para salvar tarefas no arquivo de texto
    private static void salvarTarefasNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_TAREFAS))) {
            for (Tarefa tarefa : tarefas) {
                writer.write(tarefa.toCSV());
                writer.newLine();
            }
            System.out.println("Tarefas salvas no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas no arquivo: " + e.getMessage());
        }
    }

    // Função para carregar tarefas do arquivo de texto
    private static void carregarTarefasDeArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_TAREFAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Tarefa tarefa = Tarefa.fromCSV(linha);
                tarefas.add(tarefa);
            }
            System.out.println("Tarefas carregadas do arquivo com sucesso!");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de tarefas não encontrado. Nenhuma tarefa carregada.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas do arquivo: " + e.getMessage());
        }
    }
}
