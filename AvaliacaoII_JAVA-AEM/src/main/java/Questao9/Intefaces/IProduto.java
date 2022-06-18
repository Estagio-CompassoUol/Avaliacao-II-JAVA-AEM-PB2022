package Questao9.Intefaces;

import Questao9.Model.Produto;

import java.sql.SQLException;
import java.util.List;

public interface IProduto {

    void salvarOferta(Produto produto,double valorDesc) throws SQLException;

    void atualizarOferta(int id);

    void excluirOferta(int id);

    List<Produto> listarOfertas(String nome);



}
