# PollCaster-Backend
PollCaster-Backend is a REST API built with Spring Boot. The application requires a local MySQL database connection to store user and poll entities.
This application runs locally at http://localhost:8080/.

## API Endpoints

### Auth

- `POST /login` — user login
- `POST /signup` — user creation
- `PATCH /update` — updates user's username and password
- `GET /authenticated` — check if current user is authenticated

### Poll

- `GET /` — get all polls in database
- `GET /user/:id` — get all polls by user id
- `GET /:id` — get specific poll
- `POST /` — create a poll
- `POST /:id/vote` — vote on poll 
- `DELETE /:id` — delete poll

### User

- `GET /current` — get current logged in user
- `GET /:ID` — get user by id

## Dependencies

The following are required dependencies as defined in the pom.xml file

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>

```


## MySQL Configuration

Include the following in your application.properties file

```
spring.datasource.url=<MYSQL_URI>
spring.datasource.username=<MYSQL_USERNAME>
spring.datasource.password=<MYSQL_PASSWORD>
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto = update
app.jwtSecret = <JWT_SECRET_KEY>
app.jwtExpirationInMs = <JWT_EXPIRATION_LENGTH>

```

## Todo

- Add user profile pictures 
