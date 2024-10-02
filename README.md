# Bank System Application


## Como rodar o projeto

Existem duas possibilidades para rodar o projeto, a primeira delas é usando o docker para tudo (aplicação + demais serviços).
A outra opção é rodar localmente um arquivo secundário no docker-compose somente com os serviços e rodar a aplicação localmente.

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
