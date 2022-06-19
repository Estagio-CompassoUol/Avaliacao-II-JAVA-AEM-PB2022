package Questao9;

import Questao9.ClasseDAO.ProdutoDAO;
import Questao9.Model.Produto;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class AppOferta {
    public static void main(String[] args) {

        Produto produto;
        String sair = "";

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
            System.out.println("====================================================================");
            try {
                opt = leitorNum.nextInt();

                if(opt==1) {
                    do {
                        try {
                            System.out.println("Digite o nome");
                            String nomeEntrada = leitorStr.nextLine();
                            String nome = "";
                            String descricao = "";
                            String[] dividido = nomeEntrada.split(" ");
                            for (String nomeS : dividido) {
                                if (!nomeS.equals("")) {
                                    nome = nome + " " + nomeS;
                                }
                            }
                            System.out.println("a descrição do Produto");
                            String descricaoEntrada = leitorStr.nextLine();
                            String[] divididoDesc = descricaoEntrada.split(" ");
                            for (String descricaoS : divididoDesc) {
                                if (!descricaoS.equals("")) {
                                    descricao = descricao + " " + descricaoS;
                                }
                            }
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
                                    System.out.println("====================================");
                                    System.out.println("Item cadastrado com sucesso");

                                } catch (SQLException e) {
                                    System.out.println("\nNão foi possível salvar a oferta");
                                }
                            }
                        }catch (InputMismatchException | ArrayIndexOutOfBoundsException ex) {
                            System.out.println("Dado inválido, tente novamente");
                            leitorNum.nextLine();
                            sair = "repete";
                            System.out.println("-------------------------------------------");
                        }
                    }while (sair == "repete");
                } else if (opt==2) {
                    do{
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
                        }catch (InputMismatchException  | ArrayIndexOutOfBoundsException ex) {
                            System.out.println("Dado inválido, tente novamente");
                            leitorNum.nextLine();
                            sair="repete";
                            System.out.println("-------------------------------------------");
                        }
                    }while (sair == "repete");
                } else if (opt==3) {
                    do{
                        try{
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            produtoDAO.buscarOfertasOpt();
                            System.lineSeparator();
                            System.out.println("============================================");
                            System.out.println("\nDigite o id do produto que deseja excluir");
                            int idAtu = leitorNum.nextInt();
                            boolean existe = produtoDAO.excluirOferta(idAtu);
                            if (!existe) sair="repete";
                        } catch (SQLException e) {
                            System.out.println("Erro ao excluir a oferta");
                        }
                        catch (InputMismatchException  | ArrayIndexOutOfBoundsException ex) {
                            System.out.println("Digite um id válido");
                            leitorNum.nextLine();
                            sair="repete";
                            System.out.println("-------------------------------------------");
                        }
                    }while (sair == "repete");
                } else if (opt==4) {
                    do {
                        try {
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            System.lineSeparator();
                            System.out.println("============================================");
                            System.out.println("\nDigite o que deseja Pesquisar");
                            String texto = leitorStr.nextLine();
                            produtoDAO.listarOfertas(texto);
                        } catch (SQLException e) {
                            System.out.println("Erro ao buscar oferta");
                        } catch (InputMismatchException | ArrayIndexOutOfBoundsException ex) {
                            System.out.println("Dado inválido, tente novamente");
                            leitorNum.nextLine();
                            sair="repete";
                            System.out.println("-------------------------------------------");
                        }
                    }while (sair == "repete");
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