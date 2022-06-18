package Questao10.Model;

public class Sentimento {
    private String sentimento;
    private int quantidade;

    public Sentimento(String sentimento, int quantidade) {
        sentimento = sentimento;
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getSentimento() {
        return sentimento;
    }
}
