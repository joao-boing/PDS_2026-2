# Loja de Hardware — MVC (DAO / Model / View) + JDBC

Mesma estrutura do material de aula (exemplo Estudante): `dao`, `modelo`,
`visao`, `main` — agora para uma loja de hardware, com uma tela de login
na frente da tela de produtos.

## 1. Banco de dados

Rode `sql/create_database.sql` no MySQL. Ele cria:

- o banco `hardware_store` e o usuario `store_user`;
- a tabela **`product`**: `id`, `product_name`, `brand`, `product_type`,
  `model`, `stock_quantity`;
- a tabela **`user`**: `id`, `username`, `password` (autenticacao), com
  um usuario padrao `admin` / `admin123` para voce conseguir logar de
  primeira.

## 2. Projeto no Eclipse

1. `File → New → Java Project`. Adicione o `mysql-connector-j.jar` ao
   Build Path (igual ao Passo 2 do PDF).
2. Crie quatro pacotes: `dao`, `modelo`, `visao`, `main`.
3. Copie os arquivos de `src/` para os pacotes correspondentes.

## 3. Mapa dos pacotes

| Pacote   | Arquivos                                  | Papel                                    |
|----------|---------------------------------------------|-------------------------------------------|
| `dao`    | `DBConnection`, `ProductDAO`, `UserDAO`     | Fala com o MySQL, aplica regras do conjunto |
| `modelo` | `Product`, `User`                           | Atributos, get/set, `validate()`          |
| `visao`  | `LoginWindow`, `ProductWindow`              | Telas Swing, chamam o DAO diretamente     |
| `main`   | `Main`                                      | Ponto de entrada — execute sempre este    |

Segue o mesmo padrao DAO do `EstudanteDAO`/`Estudante`/`JanelaEstudante`
do exemplo de aula (sem uma camada de Controller separada): a View chama
o DAO diretamente e exibe o resultado.

## 4. Como executar

Rode `main/Main.java` (nunca `visao/LoginWindow` ou `visao/ProductWindow`
diretamente, pelo mesmo motivo do PDF: `DBConnection.main` e
`LoginWindow.main` existem apenas como testes isolados).

Fluxo: `LoginWindow` → valida usuario/senha contra a tabela `user` via
`UserDAO.authenticate` → com sucesso, abre a `ProductWindow`, que faz o
CRUD de produtos (cadastrar, alterar, excluir, listar, buscar) contra a
tabela `product` via `ProductDAO`.

## 5. Onde cada campo mora (a regra dos "cinco lugares" do PDF)

| Campo                  | Coluna (`product`)  | Atributo em `Product` | Tipo em Java |
|--------------------------|---------------------|--------------------------|--------------|
| Id do produto            | `id`                | `id`                     | `int`        |
| Nome do produto           | `product_name`      | `productName`            | `String`     |
| Marca                     | `brand`             | `brand`                  | `String`     |
| Tipo                      | `product_type`      | `productType`            | `String`     |
| Modelo                    | `model`             | `model`                  | `String`     |
| Quantidade em estoque     | `stock_quantity`    | `stockQuantity`          | `int`        |

Se voce acrescentar um campo novo depois, siga os mesmos cinco lugares:
coluna → atributo → get/set → `?`/`set...` no DAO → rotulo + campo na
View — exatamente como explicado no Passo 10 do PDF.

## 6. Sobre a senha

Por simplicidade, `user.password` fica salva como texto puro, no mesmo
nivel didatico do restante do material. Num sistema real voce guardaria
um hash (ex.: BCrypt) em `UserDAO.insert`/`authenticate`, em vez da
senha crua.
