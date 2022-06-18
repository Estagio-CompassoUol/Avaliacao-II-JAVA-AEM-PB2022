package Questao9;

import Questao9.ClasseDAO.ProdutoDAO;
import Questao9.Model.Produto;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class AppOferta {
    public static void main(String[] args) {

        Produto produto;

        Scanner leitorNum = new Scanner(System.in);
        Scanner leitorStr = new Scanner(System.in);
        int opt = 0;

        do {
            System.out.println("\n====================================================================");
            System.out.println("Digite\n 1 - Cadastrar Oferta\n" +
                    " 2 - Atualizar Oferta\n" +
                    " 3 - Excluir Oferta\n" +
                    " 4 - Listar produtos desejados\n" +
                    " 5 - Para sair do programa\n");

            try {
                opt = leitorNum.nextInt();

                if(opt==1) {
                    System.out.println("Digite o nome");
                    String nome = leitorStr.nextLine();
                    System.out.println("a descrição do Produto");
                    String descricao = leitorStr.nextLine();
                    System.out.println("o valor do desconto em %");
                    int descontoPorc = leitorNum.nextInt();
                    System.out.println("o preço");
                    double preco = leitorNum.nextDouble();

                    double desconto = (preco * descontoPorc / 100);
                    if (descontoPorc < 100) {
                        try {
                            String data = new ProdutoDAO().dataAtual();
                            ProdutoDAO produtoDao = new ProdutoDAO();
                            produtoDao.salvarOferta(new Produto(0, nome, descricao, descontoPorc, preco, data), desconto);

                        } catch (SQLException e) {
                            System.out.println("\nNão foi possível salvar a oferta");
                        }
                    }

                } else if (opt==2) {
                    try {
                        ProdutoDAO produtoDAO = new ProdutoDAO();
                        produtoDAO.buscarOfertasOpt();
                        System.lineSeparator();
                        System.out.println("============================================");
                        System.out.println("\nDigite o id do produto que deseja alterar");
                        int idAtu = leitorNum.nextInt();
                        produtoDAO.atualizarOferta(idAtu);
                    } catch (SQLException e) {
                        System.out.println("Erro ao atualizar a oferta");
                    }
                } else if (opt==3) {
                        try{
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            produtoDAO.buscarOfertasOpt();
                            System.lineSeparator();
                            System.out.println("============================================");
                            System.out.println("\nDigite o id do produto que deseja excluir");
                            int idAtu = leitorNum.nextInt();
                            produtoDAO.excluirOferta(idAtu);
                        } catch (SQLException e) {
                            System.out.println("Erro ao excluir a oferta");
                        }
                } else if (opt==4) {
                        try{
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            System.lineSeparator();
                            System.out.println("============================================");
                            System.out.println("\nDigite o que deseja Pesquisar");
                            String texto = leitorStr.nextLine();
                            produtoDAO.listarOfertas(texto);
                        } catch (SQLException e) {
                            System.out.println("Erro ao buscar oferta");
                        }
                }

            } catch (InputMismatchException  | ArrayIndexOutOfBoundsException ex) {
                System.out.println("Dado inválido, tente novamente");
                leitorNum.nextLine();

            }catch (NullPointerException npe){
                System.out.println("Não foi poosivel buscar as ofertas");
            }finally {
                try {
                    ProdutoDAO produtoDAO = new ProdutoDAO();
                    produtoDAO.getConnection().close();
                } catch (SQLException e) {}

            }

        }while (opt!=5);
        leitorNum.close();
        leitorStr.close();
        System.out.println("Até Logo");
    }
}


//                    private String nome;
//                    private String descricao;
//                    private int desconto;
//                    private double preco;
//                    private String dataInicio;
