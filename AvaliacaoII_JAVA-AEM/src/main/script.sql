CREATE DATABASE IF NOT EXISTS AvaliacaoII;

DROP TABLE IF EXISTS `produto`;
CREATE TABLE `produto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `desconto` int NOT NULL,
  `valorDesconto` double NOT NULL,
  `preco` double NOT NULL,
  `dataInicio` varchar(10) NOT NULL,
  `precoComDesc` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `sentimento`;
CREATE TABLE `sentimento` (
  `sentimento` varchar(15) NOT NULL,
  `quantidade` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


