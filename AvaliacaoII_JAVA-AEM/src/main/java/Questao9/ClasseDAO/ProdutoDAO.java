package Questao9.ClasseDAO;


import Factory.ConnectionFactory;
import Questao9.Intefaces.IProduto;
import Questao9.Model.Produto;
import Questao9.OfertasPreSelecionadas;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProdutoDAO implements IProduto {

    private Connection connection;
    private double[] valorDesconto;

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
        try {
            String sql="";
            int desc;
            double preco;
            PreparedStatement psm = this.connection.prepareStatement("SELECT id, desconto, preco FROM produto WHERE id = ?");
            psm.setInt(1,id);
            psm.execute();
            List<Produto> produtos = buscarOfertaEspecifica(psm.getResultSet());
            if (psm.execute()){
                try {
                    System.out.println("Deseja alterar  1-Desconto  2-Preço  3-Desconto e Preço?");
                    int resposta = ler.nextInt();
                    if (resposta == 1) {
                        System.out.println("Digite o novo desconto");
                        desc = ler.nextInt();
                        double novoDesc = produtos.get(0).getPreco() * desc/100;
                        sql = "UPDATE produto SET desconto = ?, valorDesconto = ?  WHERE id = ?";
                        PreparedStatement psmUpdate = this.connection.prepareStatement(sql);
                        psmUpdate.setInt(1,desc);
                        psmUpdate.setDouble(2,novoDesc);
                        psmUpdate.setInt(3,id);
                        psmUpdate.execute();
                    } else if (resposta == 2) {
                        System.out.println("Digite o novo preço");
                        preco = ler.nextDouble();
                        double novoDesc = preco * produtos.get(0).getDesconto()/100;
                        sql = "UPDATE produto SET preco = ?, valorDesconto = ? WHERE id = ?";
                        PreparedStatement psmUpdate = this.connection.prepareStatement(sql);
                        psmUpdate.setDouble(1,preco);
                        psmUpdate.setDouble(2,novoDesc);
                        psmUpdate.setInt(3,id);
                        psmUpdate.execute();
                    } else if (resposta == 3) {
                        System.out.println("Digite o novo desconto");
                        desc = ler.nextInt();
                        System.out.println("Digite o novo preço");
                        preco = ler.nextDouble();
                        double novoDesc = preco * desc/100;
                        sql = "UPDATE produto SET desconto = ?, preco = ?, valorDesconto = ? WHERE id = ?";
                        PreparedStatement psmUpdate = this.connection.prepareStatement(sql);
                        psmUpdate.setDouble(1,desc);
                        psmUpdate.setDouble(2,preco);
                        psmUpdate.setDouble(3,novoDesc);
                        psmUpdate.setInt(4,id);
                        psmUpdate.execute();
                    } else {
                        System.out.println("Digite uma das opções acima");
                    }
                    System.out.println("Oferta Atualizada");
                }catch (Exception ex){
                    System.out.println("Digite uma das opções acima");
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
    public void excluirOferta(int id) {
        try{
           if (verificaSeExiste(id)){
                PreparedStatement psmDel= this.connection.prepareStatement("DELETE FROM produto WHERE id = ?");
                psmDel.setInt(1,id);
                psmDel.execute();
           }else{
               System.out.println("Produto não encontrado");
           }

            System.out.println("Produto excluido com êxito");
        } catch (SQLException e) {
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
                for (int i = 0; i < 4; i++) {
                    System.out.println(produtoEncontrados.get(i).getId() +" - "+
                            produtoEncontrados.get(i).getNome()+" - "+
                            produtoEncontrados.get(i).getDescricao()+" - "+
                            produtoEncontrados.get(i).getDesconto()+" - "+
//                            this.valorDesconto[i] +" - "+
                            produtoEncontrados.get(i).getPreco()+" - "+
                            produtoEncontrados.get(i).getDataInicio());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Produto> buscarOfertasOpt() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try {
            Statement stmo = this.connection.createStatement();
            stmo.executeQuery("SELECT id, nome, descricao, desconto, preco, dataInicio FROM produto");
            ResultSet result = stmo.getResultSet();
            while (result.next()){
                int id = result.getInt("ID");
                String nome = result.getString("NOME");
                String descricao = result.getString("DESCRICAO");
                int desconto = result.getInt("DESCONTO");
                double preco = result.getDouble("PRECO");

               produtos.add(new Produto(id,nome,descricao, desconto,preco,""));
                System.out.println(id+" - "+nome+" - "+descricao+" - "+desconto+" - "+preco);
            }
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
//                this.valorDesconto[i] = result.getDouble("VALORDESCONTO");
                double preco = result.getDouble("PRECO");
                String data = result.getString("DATAINICIO");

                produtos.add(new Produto(idEncontrado, nome,descricao, desconto, preco, data));
            }
            return produtos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Produtos");
        }
        return produtos;
    }

    public boolean verificaSeExiste(int id) throws SQLException {

        PreparedStatement psm = this.connection.prepareStatement("SELECT id FROM produto WHERE id = ?");
        psm.setInt(1,id);
        boolean existe = psm.execute();
        psm.close();
        if (existe) return true;
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
