# pool-challenge
Solução feita em Java Spring Boot e React.

Documentação Swagger: http://44.201.131.139:8080/swagger-ui-custom.html

Deploy backend: http://44.201.131.139:8080
Deploy frontend: http://44.201.131.139:3000

Para rodar localmente, é necessário ter java 17 e gradle 7.2 instalados, assim como nodejs e npm.

O fluxo da aplicação consiste em:
- Criar um ou mais employee(s): POST /employeee
- Criar uma poll: POST /poll
- Realizar o login com o cpf de um employee: POST /login
- Votar na poll: PUT /poll
- Verificar os resultados: /poll/opened e /poll/last
