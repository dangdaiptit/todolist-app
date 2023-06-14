# Mytodos App API
Mytodos App API is a Spring Boot API project that allows users to create and manage tasks in their daily life. With this API, users can create tasks, set time, set labels, mark complete and delete tasks.

The project is built on Spring Boot platform, uses MySQL database and uses RESTful methods to transfer data.
## Java version

- Java 19

### IDE

- IntelliJ

## Database

- MySQL

## Environment
- Docker

### API tools

- Talend
- Postman

## API Reference
- API provides login, registration, logout, CURD functions for user, todo, admin.
- Spring Boot JWT Authentication and Authorization access token, refresh token, reset password, spring mail server.
- Specific details how to use the API, you can access swagger-ui:
  https://mytodobackend.herokuapp.com/swagger-ui/index.html

## Setup local

- Run 'docker-local.yml' file, create environment for mysql

```shell
# Use root/example as user/password credentials
version: '3.1'

services:

  db:
    image: mysql
    container_name: mysql-db-todolist
    cap_add:
      - SYS_NICE
    command: --default-authentication-plugin=mysql_native_password
    restart: always
    ports:
      - "3307:3306"
    environment:
      MYSQL_DATABASE: todos
      MYSQL_ROOT_PASSWORD: 123456
    volumes:
      - ./docker/db:/var/lib/mysql
volumes:
  db:
    driver: local
```
- Run the project, then Run the following SQL insert statements:

```shell
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

```

- Then connect the db according to the mysql configuration information in dokcer.



