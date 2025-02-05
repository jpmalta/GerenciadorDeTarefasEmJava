import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa {
    private String titulo;
    private String descricao;
    private LocalDate dataVencimento;
    private Status status;

    // Construtor da classe Tarefa
    public Tarefa(String titulo, String descricao, LocalDate dataVencimento, Status status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }

    // Getters e setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Método para exibir os detalhes da tarefa
    public void exibirTarefa() {
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Data de Vencimento: " + dataVencimento);
        System.out.println("Status: " + status);
    }

    // Método para salvar a tarefa no formato CSV
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return titulo + "," + descricao + "," + dataVencimento.format(formatter) + "," + status;
    }

    // Método para carregar uma tarefa a partir de uma linha CSV
    public static Tarefa fromCSV(String linha) {
        String[] partes = linha.split(",");
        if (partes.length < 4) { // Verifica se tem pelo menos 4 partes esperadas
            System.out.println("Erro: Linha inválida encontrada no arquivo: " + linha);
            return null;
        }
    
        try {
            String titulo = partes[0].trim();
            String descricao = partes[1].trim();
            LocalDate dataVencimento = LocalDate.parse(partes[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Status status = Status.valueOf(partes[3].trim().toUpperCase());
    
            return new Tarefa(titulo, descricao, dataVencimento, status);
        } catch (Exception e) {
            System.out.println("Erro ao processar linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }
}
