# AcademicoSOAP - REST API (Swagger)

Este projeto foi migrado de SOAP para REST com JSON, usando Spring Boot, JPA e H2.

## Comunicacao

- Protocolo: HTTP/REST
- Formato: JSON
- Persistencia: H2 (em memoria por padrao)
- Documentacao interativa: Swagger UI (springdoc-openapi)
- Relacionamento: N:N bidirecional entre Curso e Disciplina, com protecao contra loop no JSON

## Swagger UI

Acesse no navegador:

http://localhost:8080/swagger-ui/index.html

## Endpoints

Base URL: http://localhost:8080

### Cursos

- GET /api/cursos
  - Lista todos os cursos, incluindo disciplinas
- GET /api/cursos/{id}
  - Busca um curso por id
- POST /api/cursos
  - Cria um curso
- PUT /api/cursos/{id}
  - Atualiza um curso
- DELETE /api/cursos/{id}
  - Remove um curso

#### Exemplo: POST /api/cursos

```json
{
  "codigo": "ENG-2025",
  "nome": "Engenharia de Software",
  "duracao": 8,
  "disciplinas": [
    { "nome": "POO", "cargaHoraria": 60 },
    { "nome": "Banco de Dados", "cargaHoraria": 80 }
  ]
}
```

#### Exemplo: POST /api/cursos (vincular disciplinas existentes)

```json
{
  "codigo": "ENG-2025",
  "nome": "Engenharia de Software",
  "duracao": 8,
  "disciplinas": [
    { "id": 1 },
    { "id": 2 }
  ]
}
```

### Disciplinas

- GET /api/disciplinas
  - Lista todas as disciplinas, incluindo cursos
- GET /api/disciplinas/{id}
  - Busca uma disciplina por id
- POST /api/disciplinas
  - Cria uma disciplina
- PUT /api/disciplinas/{id}
  - Atualiza uma disciplina
- DELETE /api/disciplinas/{id}
  - Remove uma disciplina

#### Exemplo: POST /api/disciplinas

```json
{
  "nome": "Estruturas de Dados",
  "cargaHoraria": 60,
  "cursos": [
    { "id": 1 }
  ]
}
```

## H2 Console

Se precisar visualizar o banco:

- URL: http://localhost:8080/h2-console
- JDBC URL (em memoria): jdbc:h2:mem:academicosoap

Para persistir em arquivo, edite `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:h2:file:./data/test
```

## Observacoes

- JSON evita loop do relacionamento usando `@JsonIgnoreProperties`.
- Use ids existentes para vincular objetos sem recriar registros.
