import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Tarefa {
    private String titulo;
    private String descricao;
    private LocalDate dataVencimento;
    private Status status;

    // Construtor
    public Tarefa(String titulo, String descricao, LocalDate dataVencimento, Status status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public Status getStatus() {
        return status;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Exibir detalhes da tarefa
    public void exibirTarefa() {
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Data de Vencimento: " + dataVencimento);
        System.out.println("Status: " + status);
    }

    // Listar as tarefas filtradas
    public static void listarTarefasFiltradas(List<Tarefa> tarefas, Status filtro) {
        for (Tarefa tarefa : tarefas) {
            if (filtro == null || tarefa.getStatus() == filtro) {
                System.out.println("Título: " + tarefa.getTitulo());
                System.out.println("Descrição: " + tarefa.getDescricao());
                System.out.println("Data de Vencimento: " + tarefa.getDataVencimento());
                System.out.println("Status: " + tarefa.getStatus());
                System.out.println("---------------------------");
            }
        }
    }

    // Filtrar as tarefas por status
    public static void filtrarTarefasPorStatus(List<Tarefa> tarefas) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Filtrar por:");
        System.out.println("1. Tarefas Pendentes");
        System.out.println("2. Tarefas Concluídas");
        System.out.println("3. Todas as Tarefas");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
    
        switch (opcao) {
            case 1:
                listarTarefasFiltradas(tarefas, Status.PENDENTE);
                break;
            case 2:
                listarTarefasFiltradas(tarefas, Status.CONCLUIDA);
                break;
            case 3:
                listarTarefasFiltradas(tarefas, null);  // Ajuste aqui: usar a lista recebida
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }
}
