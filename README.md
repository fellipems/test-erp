
# Documentação do Projeto

Este readme descreve o projeto de backend JAVA ERP Sênior desenvolvido em Java, utilizando diversas tecnologias e frameworks. O objetivo deste projeto é fornecer uma API RESTful para gerenciar itens, produtos e pedidos de compra e pôr conhecimento em prática.

## Tecnologias Utilizadas

- BD: PostgreSQL
- Linguagem: Java 8
- Gerenciador de Dependências: Maven
- Framework: Spring (Spring Boot)
- Framework de Persistência: JPA (Java Persistence API)
- Validação de Beans: Bean Validation
- Query Builder: QueryDSL
- Protocolo de Comunicação: REST com JSON

## Estrutura do Projeto

### Entidades

O projeto é composto por três entidades principais:

1. **Item**: Representa um item que foi comprado em uma ordem de compra.
2. **Product**: Representa um produto disponível para compra.
3. **PurchaseOrder**: Representa uma ordem de compra contendo uma lista de itens.

### Controllers

Os controllers fornecem os endpoints das entidades para criação, acesso, leitura, escrita e atualização (CRUD):

1. **ItemController**: Responsável por endpoints relacionados à entidade Item.
2. **ProductController**: Responsável por endpoints relacionados à entidade Product.
3. **PurchaseOrderController**: Responsável por endpoints relacionados à entidade PurchaseOrder.

## Configuração do Projeto

### Dependências Maven

As dependências do projeto são gerenciadas pelo Maven, e estão listadas no `pom.xml`. Algumas das principais dependências incluem:

- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- PostgreSQL JDBC Driver
- Lombok
- Hibernate Validator
- QueryDSL
- Junit5

### Configuração do Banco de Dados

As configurações do banco de dados PostgreSQL são especificadas no arquivo `application.properties`. Este arquivo contém detalhes como URL, nome de usuário e senha para conectar ao banco de dados.

### Execução do Projeto

O projeto é configurado para ser executado, por padrão, na  porta 8080 do localhost (`http://localhost:8080`).

Para iniciar a aplicação, execute o método `main` na classe principal do projeto ou  `mvn spring-boot:run`  .

## Endpoints da API

A API fornece os seguintes endpoints para cada entidade:

### Item

- **GET /items**: Retorna todos os itens.
```
curl --location 'localhost:8080/items'
```
- **GET /items/all**: Retorna uma página de itens com paginação e filtros opcionais.
 ```
curl --location --request GET 'localhost:8080/items/all?page=0&size=10&sortBy=id&direction=asc' \
--header 'Content-Type: application/json'
```
- **POST /items**: Cria um novo item.
 ```
curl --location 'localhost:8080/items' \
--header 'Content-Type: application/json' \
--data '{
    "quantity": 2,
    "product": {
        "id":"29809a60-1eab-4cba-b49f-08ac5f967ca8"
    }
}'
```
- **GET /items/{itemId}**: Retorna um item específico com base no ID.
- **DELETE /items/{itemId}**: Exclui um item específico com base no ID.
- **PUT /items/{itemId}**: Atualiza um item específico com base no ID.

### Product

- **GET /products**: Retorna todos os produtos.
```
curl --location 'localhost:8080/products'
```
- **GET /products/all**: Retorna uma página de produtos com paginação e filtros opcionais.
```
curl --location --request GET 'localhost:8080/products/all?page=0&size=10&sortBy=id&direction=asc' \
--header 'Content-Type: application/json' \
--data '{
    "active":false
}'
```
- **POST /products**: Cria um novo produto.
```
curl --location 'localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Mouse",
    "price": 99.900,
    "type": "SERVICE"
}'
```
- **GET /products/{productId}**: Retorna um produto específico com base no ID.
```
curl --location 'localhost:8080/products/e0465eba-9ef6-4e60-bf03-2442ada50929'
```
- **DELETE /products/{productId}**: Exclui um produto específico com base no ID.
```
curl --location --request DELETE 'localhost:8080/products/c3fd93da-2659-4a88-97ac-08f5f99c681d'
```
- **PUT /products/{productId}**: Atualiza um produto específico com base no ID.
```
curl --location --request PUT 'localhost:8080/products/df9827ce-a3be-413e-aba0-0af202e1a28f' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Mouse",
    "price": 99.90,
    "type": "SERVICE",
    "active": false
}'
```

### PurchaseOrder

- **GET /orders**: Retorna todas as ordens de compra.
- **GET /orders/all**: Retorna uma página de ordens de compra com paginação e filtros opcionais.
- **POST /orders**: Cria uma nova ordem de compra.
```
curl --location 'localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "items":[
        {
            "quantity": 1,
            "product": {
                "id":"86c06dc4-73c8-42d7-bcab-d726216525e0"
            }
        },
        {
            "quantity": 2,
            "product": {
                "id": "0a4a361f-ec33-4bca-b2ad-b11278d71823"
            }
        }
    ],
    "discount":15.1987
}'
```
- **GET /orders/{orderId}**: Retorna uma ordem de compra específica com base no ID.
- **DELETE /orders/{orderId}**: Exclui uma ordem de compra específica com base no ID.
- **PUT /orders/{orderId}**: Atualiza uma ordem de compra específica com base no ID.
 ```
curl --location --request PUT 'localhost:8080/orders/status/d52e97a6-02d4-44f7-8740-e76663676265' \
--header 'Content-Type: application/json' \
--data '{
    "orderStatus": "OPEN"
}'
```
- **DELETE /orders/item/{orderId}**: Remove itens de uma ordem de compra específica com base no ID da ordem e IDs dos itens.
- **PUT /orders/status/{orderId}**: Atualiza o status de uma ordem de compra específica com base no ID.

## Error Handler

O projeto possui um ErrorHandler para tratar exceções de validação. Por ora, tem somente um caso de, se ocorra uma violação de constraint, será retornada uma resposta com status HTTP 400 (Bad Request), contendo informações sobre o erro.

## Testes unitários
O projeto inclui testes unitários iniciais, mas completo da classe ProductService, para as classes de serviço utilizando JUnit4 e mocks com Mockito.

## Considerações Finais

Este documento fornece uma visão geral do projeto, incluindo suas tecnologias, estrutura, configuração e endpoints da API. Para mais detalhes sobre a implementação específica de cada componente, temos o código-fonte do projeto no repo do GitHub.

Interessante em pesquisar sobre QueryDSL([QueryDSL Documentação](http://querydsl.com/) - [Github QueryDSL](https://github.com/querydsl/querydsl) ), algo que nunca tinha visto, bem produtivo e fácil de entender. Com certeza me aprofundarei mais.
