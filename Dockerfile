# Fase 1: Construir o JAR da aplicação usando Maven
FROM maven:3.9.5-eclipse-temurin-17 as build

# Defina o diretório de trabalho no container
WORKDIR /app

# Copie o arquivo de configuração do Maven (pom.xml) e as dependências
COPY pom.xml .

# Baixe as dependências sem compilar (isso aproveita o cache em builds futuros)
RUN mvn dependency:go-offline

# Copie o código-fonte da aplicação
COPY src ./src

# Compile e faça o package da aplicação
RUN mvn clean package -DskipTests

# Fase 2: Executar o JAR em uma imagem mais leve
FROM eclipse-temurin:17-jre-jammy

# Defina o diretório de trabalho
WORKDIR /app

# Copie o JAR gerado na fase anterior para o diretório do container
COPY --from=build /app/target/*.jar /app/app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
