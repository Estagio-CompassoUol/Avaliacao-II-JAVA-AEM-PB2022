package Questao9;

import Questao9.ClasseDAO.ProdutoDAO;
import Questao9.Model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfertasPreSelecionadas {

    public List<Produto> valoresIniciais() throws SQLException {
            List<Produto> listaProd = new ArrayList<>();

            String dataAtual = new ProdutoDAO().dataAtual();

            listaProd.add(new Produto(1,"TV", "TV Samsung 42Pol HD", 10, 1899.99d, dataAtual));
            listaProd.add(new Produto(2,"Fogão", "Fogão 5 Bocas Consul", 5, 899.99d, dataAtual));
            listaProd.add(new Produto(3,"Geladeira", "Geladeira 354L Eletrolux Branca", 15, 1999.99d, dataAtual));

            return listaProd;

    }

    public void setarDadosIniciaisBD(String sql){

        try{
            Connection connection = new ProdutoDAO().getConnection();
            PreparedStatement psm = connection.prepareStatement(sql);

            int i =0;
            double[] valorDesconto = new double[3];

            valorDesconto[0]  = 1899.9/10;
            valorDesconto[1] = (899*5)/100;
            valorDesconto[2] = (15*1999)/100;

            for (Produto produtoOfInic : valoresIniciais()) {

                double precoComDesc = (produtoOfInic.getPreco()-valorDesconto[i]);

                psm.setString(1, produtoOfInic.getNome());
                psm.setString(2, produtoOfInic.getDescricao());
                psm.setInt(3, produtoOfInic.getDesconto());
                psm.setDouble(4, valorDesconto[i]);
                psm.setDouble(5, produtoOfInic.getPreco());
                psm.setString(6, produtoOfInic.getDataInicio());
                psm.setDouble(7,precoComDesc);
                psm. execute();
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
