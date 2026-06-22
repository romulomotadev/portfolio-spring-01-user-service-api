# Estágio 1: Compilação (Usa o Maven com JDK)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos de configuração para baixar as dependências
COPY pom.xml .
COPY src ./src

# Compila o projeto e gera o arquivo .jar (ignora os testes para agilizar)
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Usa apenas o JRE, muito mais leve)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria um usuário do sistema para não rodar como root (Segurança)
RUN addgroup -S dev && adduser -S usertest -G dev
USER usertest

# Copia o arquivo .jar gerado no primeiro estágio
# O nome do jar geralmente segue o padrão: artefato-versao.jar
COPY --from=build /app/target/*.jar app.jar

# Define a porta padrão do Spring Boot (geralmente 8080)
EXPOSE 8080

# Comando correto para executar um jar no Java
CMD ["java", "-jar", "app.jar"]