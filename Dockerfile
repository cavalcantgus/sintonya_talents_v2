# ====== STAGE 1: Build com Maven + JDK21 ======
FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Gera o JAR (skip dos testes pra ser mais rápido)
RUN mvn clean package -DskipTests

# ====== STAGE 2: Runtime ======
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copia o JAR do build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]