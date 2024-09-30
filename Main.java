import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Tarefa> tarefas = new ArrayList<>(); // Lista de tarefas

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Menu:");
            System.out.println("1. Criar nova tarefa");
            System.out.println("2. Listar todas as tarefas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    criarTarefa(scanner); // Função para criar nova tarefa
                    break;
                case 2:
                    listarTarefas(); // Função para listar todas as tarefas
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    // Função para criar uma nova tarefa
    private static void criarTarefa(Scanner scanner) {
        System.out.print("Digite o título da tarefa: ");
        String titulo = scanner.nextLine();

        System.out.print("Digite a descrição da tarefa: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a data de vencimento (YYYY-MM-DD): ");
        String data = scanner.nextLine();
        LocalDate dataVencimento = LocalDate.parse(data);

        System.out.println("Selecione o status da tarefa:");
        System.out.println("1. Pendente");
        System.out.println("2. Em progresso");
        System.out.println("3. Concluída");
        int statusOpcao = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha
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
}
