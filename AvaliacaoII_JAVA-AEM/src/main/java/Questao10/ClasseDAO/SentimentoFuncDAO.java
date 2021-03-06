package Questao10.ClasseDAO;

import Questao10.Factory.ConnectionFactory;
import Questao10.Interface.ISentimentoFuncDAO;
import Questao10.Model.Sentimento;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SentimentoFuncDAO implements ISentimentoFuncDAO {
    private Connection connection;

    public SentimentoFuncDAO() throws SQLException {
        this.connection = new ConnectionFactory().getConexao();
    }

    @Override
    public void insert(String msg) {
        String retorno = identificaEmoticos(msg);
        String[] sentimentoRet = retorno.split(" ");
        int divertidoUsu = Integer.parseInt(sentimentoRet[0]), chateadoUsu = Integer.parseInt(sentimentoRet[1]), neutroUsu = Integer.parseInt(sentimentoRet[2]);
        int divertidoBus = 0, chateadoBusc = 0, neutroBusc = 0;

        try{
            String sql = "UPDATE sentimento SET quantidade = ? WHERE sentimento = ?";
            PreparedStatement psm = connection.prepareStatement(sql);
            List<Sentimento> buscaQuant = buscarQuantidadeBD();

            for (Sentimento sentimento:buscaQuant) {
                 divertidoBus= sentimento.getQuantidade();
                chateadoBusc = sentimento.getQuantidade();
                neutroBusc= sentimento.getQuantidade();
            }

            if(divertidoUsu>chateadoUsu){
                int divertidoTot=1+divertidoBus;
                System.out.println("Você esta divertido");
                psm.setInt(1,divertidoTot);
                psm.setString(2,"divertido");
                psm.execute();

            } else if (chateadoUsu>divertidoUsu) {
                System.out.println("Você esta chateado");
                int chateadoTot=1+chateadoBusc;
                psm.setInt(1,chateadoTot);
                psm.setString(2,"chateado");
                psm.execute();
            }else {
                int neutroTot=1+neutroBusc;
                System.out.println("neutro");
                psm.setInt(1,neutroTot);
                psm.setString(2,"neutro");
                psm.execute();
            }
            psm.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String identificaEmoticos(String msg){
        String[] msgFracionaria = msg.split("");
        String retorno="";
        int chateado = 0, divertido = 0, neutro = 0;
        for (int i = 0; i < msgFracionaria.length; i++) {
                if (msgFracionaria[i].equals(":")) {
                    if (msgFracionaria.length - i >= 3) {
                        retorno = verificaQuantidadeUsuario(i,msgFracionaria);
                        String[] retornoFrac = retorno.split(" ");
                        divertido += Integer.parseInt(retornoFrac[0]);
                        chateado += Integer.parseInt(retornoFrac[1]);
                        neutro += Integer.parseInt(retornoFrac[2]);
                        if (Integer.parseInt(retornoFrac[3]) ==3 ){
                            i=i+2;
                        } else if (Integer.parseInt(retornoFrac[3]) ==2) {
                            i=i+1;
                        }
                    }
                } else if (msgFracionaria[i].equals(")")) {
                    if (msgFracionaria.length - i >= 3) {
                        retorno = verificaQuantidadeUsuario(i,msgFracionaria);
                        String[] retornoFrac = retorno.split(" ");
                        divertido += Integer.parseInt(retornoFrac[0]);
                        chateado += Integer.parseInt(retornoFrac[1]);
                        neutro += Integer.parseInt(retornoFrac[2]);
                        if (Integer.parseInt(retornoFrac[3]) ==3 ){
                            i=i+2;
                        } else if (Integer.parseInt(retornoFrac[3]) ==2) {
                            i=i+1;
                        }
                    }
                } else if (msgFracionaria[i].equals("(")) {
                    if (msgFracionaria.length - i >= 3) {
                        retorno = verificaQuantidadeUsuario(i,msgFracionaria);
                        String[] retornoFrac = retorno.split(" ");
                        divertido += Integer.parseInt(retornoFrac[0]);
                        chateado += Integer.parseInt(retornoFrac[1]);
                        neutro += Integer.parseInt(retornoFrac[2]);
                        if (Integer.parseInt(retornoFrac[3]) ==3 ){
                            i=i+2;
                        } else if (Integer.parseInt(retornoFrac[3]) ==2) {
                            i=i+1;
                        }
                    }
                }else if (msgFracionaria[i].equals("-")) {
                    if (msgFracionaria.length - i >= 2) {
                        retorno = verificaQuantidadeUsuario(i, msgFracionaria);
                        String[] retornoFrac = retorno.split(" ");
                        divertido += Integer.parseInt(retornoFrac[0]);
                        chateado += Integer.parseInt(retornoFrac[1]);
                        neutro += Integer.parseInt(retornoFrac[2]);
                        i = i + 1;
                    }
                }
//            }
        } return String.valueOf(divertido)+" "+String.valueOf(chateado)+" "+String.valueOf(neutro);
    }

    public List<Sentimento> buscarQuantidadeBD() throws SQLException {
        List<Sentimento> listaQtn = new ArrayList<>();
        try {
            PreparedStatement psm = this.connection.prepareStatement("SELECT sentimento, quantidade FROM sentimento");
            psm.execute();

            ResultSet result = psm.getResultSet();
            if (!result.next()){
                psm.execute("INSERT INTO sentimento(sentimento, quantidade) VALUES ('divertido',0)");
                psm.execute("INSERT INTO sentimento(sentimento, quantidade) VALUES ('chateado',0)");
                psm.execute("INSERT INTO sentimento(sentimento, quantidade) VALUES ('neutro',0)");
            }
            while (result.next()){
                String sentimento = result.getString("SENTIMENTO");
                int quantidade = result.getInt("QUANTIDADE");

                listaQtn.add(new Sentimento(sentimento, quantidade));
            }
            return listaQtn;
        } catch (SQLException e) {
        }
        return listaQtn;
    }

    public String verificaQuantidadeUsuario(int i,String[] msgFracionaria) {
        int ind = i;
        int chateado = 0, divertido = 0, neutro = 0;
        String emoticons = msgFracionaria[ind] + msgFracionaria[++ind] + msgFracionaria[++ind];
        int qtn = verificarQualEmoticons(emoticons);
        if(qtn == 3) {
            if (emoticons.equals(":-)") || emoticons.equals("(-:")) {
                divertido++;
            } else if (emoticons.equals(":-(") || emoticons.equals(")-:")) {
                chateado++;
            }
        }else if (qtn == 2){
                System.out.println("----------------------------------------");
                System.out.println("Se você quis escrever alguns desses emoticons :-) ou :-( \nDigite: \n  1 -  :-)\n  2 -  :-(\n  3 -  Não  ");
                System.out.println("----------------------------------------");
                try {
                    int resp = new Scanner(System.in).nextInt();
                    if (resp == 1) {
                        divertido++;
                    } else if (resp == 2) {
                        chateado++;
                    }
                } catch (InputMismatchException ime) {
                    System.out.println("=======================================");
                    System.out.println("Digite uma opção válida");
                    System.out.println("=======================================");
                }
        }
        return String.valueOf(divertido)+" "+String.valueOf(chateado)+" "+String.valueOf(neutro)+" "+String.valueOf(qtn);
    }
    public int verificarQualEmoticons(String emoticons){
        String [] divEmot = emoticons.split(" ");
        String divEmotic = "";
        for (int j = 0; j < divEmot.length; j++) {
            divEmotic= divEmotic + divEmot[j];
        }
        String[] emoticonsDiv = divEmotic.split("");
        int qtn = emoticonsDiv.length;
        return qtn;
    }
}
