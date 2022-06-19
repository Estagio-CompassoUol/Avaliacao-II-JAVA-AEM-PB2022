# Avaliacao-II-JAVA-AEM-PB2022
### Avaliação da segunda Sprint
### Projeto Maven

## Foram apresentados 2 problemas para serem resolvidos codificando em java:



### Gerenciamento de Oferta

Esse programa apresenta 4 possibilidades de gerenciamento(INSERT, UPDATE, DELETE e busca por parãmetros) de Ofertas de um produto utilizando JDBC e MySql;

1. O programa solicita ao usuário que digite a opção desejada;"\n"
2. 1-Cadastrar  2-Atualizar Oferta  3-Deletar  4-Listar um produto Especifico  5-Sair;
3. Recebe os dados digitado no teclado;
4. Todos as opções são persistidas no BD realizadas atravéz de sql;
5. As exceptions são tratadas, ao apresentar uma exceptions por valores incorretos ele exibe uma msg informando e retorna para o ponto de nova tentativa.
6. O programa é finalizado ao digitar a opção "sair";




### Identificação de sentimento através de digitação de 2 tipos de emoticons  :-) e :-(

Esse app solicita ao usuário que digite qualquer frase e digite quantos emoticons quiserem, o app tem a capacidade de analizar todos os caracteres ignorando os espaços vazios e identificando os emoticons semelhantes mesmos em posiçoes contrarias; então ele conta quantos emoticons "sorriso" e "triste" e compara a quantidade, se tiver maior quantidade do sorriso então ele exibe "divertido", caso contrário "chateado" e se nao atender à nenhum dos critérios exibe "neutro" e então salva atualizando os valores existentes no BD-MySql utilizando JDBC e Sql; 

1. O programa solicita uma mensagem ao usuário;
2. Logo após, separa em caracteres;
3. Identifica o primeiro caracteres não em braco e compara com um caracter especifico;
4. Concatena os 2 caracteres seguintes e compara com os emoticons especificos;
5. Se digitado dois caracteres dos 3 caracteres que formam os emoticons ele pergunta ao usuário qual dos emoticons queria ter digitado e aguarda a responta;
6. Ele contabiliza a quantidade de cada emoticons da mensagem digitada pelo usuário e compara qual a quantidade é maior e exibe a mensagem correspondente; 
7. Se não houver emoticons ou o numero de emoticons feliz e chateado forem iguais ele contabiliza "neutro";
8. Então após identificar qual o sentimento do usuário ele persiste no banco de dados;
9. A cada nova mensagem ele busca a quantidade do banco de dados e atualiza com o novo numero;
10. Foram utilizados tratamento de erros no códico evitando parada do app por Exceptions.;
11. Então o programa é finalzado. 


