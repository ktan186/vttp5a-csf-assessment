# Build Angular static files
FROM node:23 AS ngbuild

WORKDIR /client

RUN npm i -g @angular/cli

COPY client/package.json .
COPY client/package-lock.json .

RUN npm ci

COPY client/angular.json .
COPY client/tsconfig.json .
COPY client/tsconfig.app.json .
COPY client/src src

RUN ng build

# Package Spring project
FROM openjdk:23 AS javabuild

WORKDIR /server

COPY server/pom.xml .
COPY server/.mvn .mvn
COPY server/mvnw .
COPY server/src src

COPY --from=ngbuild /client/dist/client/browser src/main/resources/static

RUN chmod a+x mvnw
RUN ./mvnw package -Dmaven.test.skip=true

# Run container
FROM openjdk:23

WORKDIR /app

COPY --from=javabuild /server/target/server-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT [ "java", "-jar", "app.jar"]