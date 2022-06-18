package Questao10;

import Questao10.ClasseDAO.SentimentoFuncDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class AppSentimento {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("Digite uma mensagem");
        String lerMsn = ler.nextLine();
        try{
            SentimentoFuncDAO sentimento = new SentimentoFuncDAO();
            sentimento.insert(lerMsn);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao BD");
        }
    }
}
