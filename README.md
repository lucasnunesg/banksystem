# Bank System Application

## Descrição do projeto

O projeto consiste num sistema bancário simplificado, onde é possível cadastrar novas contas e realizar transferência entre elas.
Existem dois tipos de conta: `PERSONAL` e `BUSINESS`. A diferença entre elas é que a conta business não pode transferir, apenas receber dinheiro.

A aplicação realiza consulta em um serviço de autorização externo, para definir se a transação ocorrerá ou não.
Adicionalmente há um serviço de notificação extera, que pode apresentar lentidão na resposta, e por isso foi configurado o sistema de mensageria com RabbitMQ para evitar a perda de mensagens.

### Endpoints
> OBS: Existe uma postman collection no repositório para testes. Por padrão o projeto roda no http://0.0.0.0/8080

Listar todas as contas cadastradas:
```http request
GET /accounts
```

Detalhes de uma conta por id (no parâmetro da requisição)
```http request
GET /accounts/{id}
```

Criar conta:
```http request
POST /accounts
Content-Type: application/json

{
    "fullName": "Michael Scott",
    "document": "012.345.678-90",
    "email": "michaelscott@dundermifflin.com",
    "password": "itsbritneyb*tch",
    "accountType": "PERSONAL" //BUSINESS
}
```

Listar todas as transferências realizadas:
```http request
GET /transfer
```

Detalhes de uma transferência por id (no parâmetro da requisição)
```http request
GET /transfer/{id}
```

Realizar transferências entre contas:
```http request
POST /transfer
Content-Type: application/json

{
"value": 100.0,
"payer": 4,
"payee": 15
}
```

## Tecnologias usadas
- Java Spring Boot para a aplicação;
- OpenFeign para consulta de API's externas;
- PostgreSQL para o banco de dados;
- RabbitMQ para mensageria;
- Mockito e JUnit para testes unitários.

## Como rodar o projeto

Existem duas possibilidades para rodar o projeto, a primeira delas é usando o docker para tudo (aplicação + demais serviços).
A outra opção é rodar localmente um arquivo secundário no docker-compose somente com os serviços e rodar a aplicação localmente.

Para ambas as abordagens é necessário definir as variáveis de ambiente:

### Variáveis de ambiente necessárias
Criar um arquivo `.env` na raiz do projeto contendo (ou copiar do arquivo `.env.sample`):
```env
POSTGRES_DB=banksystemdb
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root
```

### Utilizando apenas o docker

Para rodar tudo no docker basta utilizar o `docker-compose.yaml` da seguinte maneira:
```bash
docker-compose up
```

### Utilizando o docker apenas para as dependências PostgreSQL e RabbitMQ

Utilizar o arquivo `infra.yaml`:
```bash
docker-compose -f infra.yaml up
```
Após inicializar o container, rodar o projeto localmente (direto pela IDE, por exemplo)


## Testes unitários

Para rodar os testes unitários, basta, na raiz do projeto usar o comando:
```bash
./mvnw test
```

## Sugestões de melhoria
- Implementação de cache para as consultas no banco;
- Melhoria (refatoração) da lógica usada para definir quais contas podem realizar transferência;
- Otimização das configurações do RabbitMQ para melhorar a eficiência do serviço de mensageria;
- Aumentar a cobertura de testes unitários.
