package Questao9.ClasseDAO;


import Factory.ConnectionFactory;
import Questao9.Intefaces.IProduto;
import Questao9.Model.Produto;
import Questao9.OfertasPreSelecionadas;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProdutoDAO implements IProduto {

    private Connection connection;
    private double precoComDesc;
    private double valorDesconto;

    public ProdutoDAO() throws SQLException {
        this.connection = new ConnectionFactory().getConexao();
    }

    public Connection getConnection() {return connection;}

    @Override
    public void salvarOferta(Produto produto, double valorDesc){
        String sql = "INSERT INTO produto (nome, descricao, desconto, valorDesconto, preco, dataInicio, precoComDesc) VALUES (?,?,?,?,?,?,?)";
       try{
           Statement stm = connection.createStatement();
           ResultSet result = stm.executeQuery("SELECT nome FROM produto");
           if(!result.next()) new OfertasPreSelecionadas().setarDadosIniciaisBD(sql);
           stm.close();
           result.close();
           PreparedStatement psm = connection.prepareStatement(sql);
           double precoComDescEntrada = produto.getPreco()-valorDesc;
           psm.setString(1,produto.getNome());
           psm.setString(2, produto.getDescricao());
           psm.setInt(3,produto.getDesconto());
           psm.setDouble(4, valorDesc);
           psm.setDouble(5, produto.getPreco());
           psm.setString(6,produto.getDataInicio());
           psm.setDouble(7,precoComDescEntrada);
           psm.execute();
           psm.close();
           connection.close();
       }catch(SQLException e) {
           throw new RuntimeException(e);
       }
    }


    @Override
    public void atualizarOferta(int id) {
        Scanner ler = new Scanner(System.in);
        String sair="";
        try {
            String sql="";
            int desc;
            double preco;
            PreparedStatement psmId = this.connection.prepareStatement("SELECT id, nome, descricao, desconto,valorDesconto,  preco, dataInicio, precoComDesc FROM produto WHERE id = ?");
            psmId.setInt(1,id);
            psmId.execute();
            ResultSet result = psmId.getResultSet();
            List<Produto> produtos = buscarOfertaEspecifica(result);
            if (psmId.execute()){
                    if (!produtos.isEmpty()) {
                        try {
                            System.out.println("Deseja alterar  1-Desconto  2-Preço  3-Desconto e Preço?");
                            int resposta = ler.nextInt();
                            if (resposta == 1) {
                                System.out.println("Digite o novo desconto");
                                desc = ler.nextInt();
                                double novoDesc = produtos.get(0).getPreco() * desc / 100;
                                double novoPrecoComDesc = produtos.get(0).getPreco() - novoDesc;
                                sql = "UPDATE produto SET desconto = ?, valorDesconto = ?, precoComDesc = ?  WHERE id = ?";
                                PreparedStatement psmUpdate = this.connection.prepareStatement(sql);
                                psmUpdate.setInt(1, desc);
                                psmUpdate.setDouble(2, novoDesc);
                                psmUpdate.setDouble(3, novoPrecoComDesc);
                                psmUpdate.setInt(4, id);
                                psmUpdate.execute();
                            } else if (resposta == 2) {
                                System.out.println("Digite o novo preço");
                                preco = ler.nextDouble();
                                double novoDesc = preco * produtos.get(0).getDesconto() / 100;
                                double novoPrecoComDesc = preco - novoDesc;
                                sql = "UPDATE produto SET preco = ?, valorDesconto = ?, precoComDesc = ? WHERE id = ?";
                                PreparedStatement psmUpdate = this.connection.prepareStatement(sql);
                                psmUpdate.setDouble(1, preco);
                                psmUpdate.setDouble(2, novoDesc);
                                psmUpdate.setDouble(3, novoPrecoComDesc);
                                psmUpdate.setInt(4, id);
                                psmUpdate.execute();
                            } else if (resposta == 3) {
                                System.out.println("Digite o novo desconto");
                                desc = ler.nextInt();
                                System.out.println("Digite o novo preço");
                                preco = ler.nextDouble();
                                double novoDesc = preco * desc / 100;
                                double novoPrecoComDesc = preco - novoDesc;
                                sql = "UPDATE produto SET desconto = ?, preco = ?, valorDesconto = ?, precoComDesc = ? WHERE id = ?";
                                PreparedStatement psmUpdate = this.connection.prepareStatement(sql);
                                psmUpdate.setDouble(1, desc);
                                psmUpdate.setDouble(2, preco);
                                psmUpdate.setDouble(3, novoDesc);
                                psmUpdate.setDouble(4, novoPrecoComDesc);
                                psmUpdate.setInt(5, id);
                                psmUpdate.execute();
                            } else {
                                System.out.println("Digite uma das opções acima");
                            }
                            System.out.println("Oferta Atualizada");
                        } catch (Exception ex) {
                            System.out.println("Digite uma das opções acima");
                        }
                    }else{
                        System.out.println("Id não cadastrado no Banco de Dados");
                    }
                }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Produtos");
        } catch (InputMismatchException | ArrayIndexOutOfBoundsException ex) {
            System.out.println("Digite um numero válido");
            ler.nextLine();
        }
     }

    @Override
    public boolean excluirOferta(int id) {
        try{
            if (verificaSeExiste(id)) {
              System.out.println("Produto não encontrado no Banco de Dados");
              PreparedStatement psmDel = this.connection.prepareStatement("DELETE FROM produto WHERE id = ?");
              psmDel.setInt(1, id);
              psmDel.execute();
              psmDel.close();
              System.out.println("--------------------------------------");
              System.out.println("Produto excluido com êxito");
              return true;
            }else{
              System.out.println("Produto não registrado");
              return false;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Produto> listarOfertas(String nome) {
        List<Produto> produtoEncontrados = new ArrayList<>();
        try{
            String[] nomePart = nome.split(" ");
            for (int i=0; i< nomePart.length;i++){
                String parametro = '%'+nomePart[i]+'%';
                PreparedStatement psm = this.connection.prepareStatement("SELECT id, nome, descricao, desconto,valorDesconto,  preco, dataInicio, precoComDesc FROM produto WHERE nome LIKE ? OR descricao LIKE ?");
                psm.setString(1,parametro);
                psm.setString(2,parametro);
                psm.execute();
                if(psm.execute()){
                    produtoEncontrados = buscarOfertaEspecifica(psm.getResultSet());
                }
            }
            if (!produtoEncontrados.isEmpty()){
                System.out.println("=========================================");
                System.out.println("========Itens encontrados=========================");
                System.out.println("-----------------------------------------");
                for (int i = 0; i < produtoEncontrados.size(); i++) {
                    System.out.println(String.format("%3s",produtoEncontrados.get(i).getId())  +" - "+
                            String.format("%5s",produtoEncontrados.get(i).getNome())+" - "+
                            String.format("%14s",produtoEncontrados.get(i).getDescricao())+" - "+
                            String.format("%2s", produtoEncontrados.get(i).getDesconto())+"% - "+
                            String.format("%5s", "R$"+this.valorDesconto)+" - "+
                            String.format("%5s", "R$"+produtoEncontrados.get(i).getPreco())+" - "+
                            String.format("%5s", "R$"+this.precoComDesc)+" - "+
                            String.format("%5s", produtoEncontrados.get(i).getDataInicio()));
                }
                System.out.println("==========================================");
            }else{
                System.out.println("Produto não encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (IndexOutOfBoundsException iob){
            System.out.println("Erro a0 imprimir a lista");
        }

        return null;
    }

    public List<Produto> buscarOfertasOpt() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try {
            Statement stmo = this.connection.createStatement();
            stmo.executeQuery("SELECT id, nome, descricao, desconto, preco, dataInicio FROM produto");
            ResultSet result = stmo.getResultSet();
            System.out.println("==============================================");
            while (result.next()){
                int id = result.getInt("ID");
                String nome = result.getString("NOME");
                String descricao = result.getString("DESCRICAO");
                int desconto = result.getInt("DESCONTO");
                double preco = result.getDouble("PRECO");

               produtos.add(new Produto(id,nome,descricao, desconto,preco,""));
                System.out.println(id+" - "+nome+" - "+descricao+" - "+desconto+" - R$ "+preco);
            }
            stmo.close();
            return produtos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Produtos");
        }
        return produtos;
    }

    public List<Produto> buscarOfertaEspecifica(ResultSet result) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        int i=0;
        try {
                while (result.next()) {
                    int idEncontrado = result.getInt("ID");
                    String nome = result.getString("NOME");
                    String descricao = result.getString("DESCRICAO");
                    int desconto = result.getInt("DESCONTO");
                    this.valorDesconto = result.getDouble("VALORDESCONTO");
                    double preco = result.getDouble("PRECO");
                    this.precoComDesc = result.getDouble("PRECOCOMDESC");
                    String data = result.getString("DATAINICIO");
                    produtos.add(new Produto(idEncontrado, nome, descricao, desconto, preco, data));
                }
            result.close();
            return produtos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Produtos");
        }
        return produtos;
    }

    public boolean verificaSeExiste(int id) throws SQLException {

        PreparedStatement psmEx = this.connection.prepareStatement("SELECT id FROM produto WHERE id = ?");
        psmEx.setInt(1,id);
        psmEx.execute();
        ResultSet resultSet = psmEx.getResultSet();
        if (resultSet.next()) return true;
        psmEx.close();
        return false;
    }
    public String dataAtual(){
         LocalDate dataAgora = LocalDate.now();
         String dia = String.valueOf(dataAgora.getDayOfMonth());
         String mes = String.valueOf(dataAgora.getMonthValue());
         String ano = String.valueOf(dataAgora.getYear());
         return dia+"/" + mes +"/" +ano;
     }


}
