import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Criando uma nova tarefa
        Tarefa tarefa1 = new Tarefa(
            "Estudar Java",
            "Estudar as funcionalidades básicas da linguagem Java.",
            LocalDate.of(2024, 10, 5),
            Status.PENDENTE
        );

        // Exibindo os detalhes da tarefa
        tarefa1.exibirTarefa();
    }
}
