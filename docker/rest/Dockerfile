FROM eclipse-temurin:24-jre
WORKDIR /app
RUN wget -O app.jar https://github.com/Andrzej-Swietek/Cloud-Computing-Project/releases/download/backend-v0.0.1/recommender-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
