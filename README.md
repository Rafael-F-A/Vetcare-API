# VetCare API

API REST para gestão de clínicas veterinárias. Desenvolvida com Java 17 e Spring Boot.

## Tecnologias

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database (desenvolvimento)
- Maven
- Lombok (opcional)

## Como executar

1. Clone o repositório:
   git clone https://github.com/seu-usuario/vetcare-api.git

2. Entre na pasta do projeto:
   cd vetcare-api

3. Execute com Maven:
   ./mvnw spring-boot:run

4. Acesse a API em: http://localhost:8080

## Endpoints principais (exemplo)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /api/tutores | Lista todos os tutores |
| POST | /api/tutores | Cadastra um tutor |
| GET | /api/tutores/{id} | Busca tutor por ID |
| PUT | /api/tutores/{id} | Atualiza tutor |
| DELETE | /api/tutores/{id} | Remove tutor |

## Console H2

Acesse http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:vetcare
Usuário: sa
Senha: (vazio)

## Autores

- Alice Santos Silva
- Rafael Florêncio de Azevedo
- Gabriel Gomes Oliveira

## Licença

Projeto acadêmico – sem fins comerciais.
