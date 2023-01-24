# pool-challenge
Solução feita em Java Spring Boot e React.

Documentação Swagger: http://52.23.164.7:8080/swagger-ui-custom.html

Deploy EC2 backend: http://52.23.164.7:8080

Deploy EC2 frontend: http://52.23.164.7:3000

Para rodar localmente, é necessário ter java 17 e gradle 7.2 instalados, assim como nodejs e npm.

O fluxo da aplicação consiste em:
- Criar um ou mais employee(s): POST /employeee (apenas via API)
- Criar uma poll: POST /poll (apenas via API)
- Realizar o login com o cpf de um employee: POST /login
- Votar na poll: PUT /poll
- Verificar os resultados: /poll/last

O fluxo pode ser testado tanto chamandos os endpoints manualmente assim como também de forma mais intuitiva pelo frontend (também localmente ou pelas instâncias AWS acima).

CPFs disponíveis para teste:
- 15464203559
- 04334836607
- 04764268710
