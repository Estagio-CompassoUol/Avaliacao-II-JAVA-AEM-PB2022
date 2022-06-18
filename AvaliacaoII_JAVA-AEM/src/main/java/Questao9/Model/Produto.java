package Questao9.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private int desconto;
    private double preco;
    private String dataInicio;

    public Produto(int id, String nome, String descricao, int desconto, double preco, String data) {
        this.id=id;
        this.nome = nome;
        this.descricao = descricao;
        this.desconto = desconto;
        this.preco = preco;
        this.dataInicio = data;

    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDesconto() {
        return desconto;
    }

    public void setDesconto(int desconto) {
        this.desconto = desconto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

}
