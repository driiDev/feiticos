# Biblioteca de Feiticos

API REST desenvolvida em Java e Spring Boot para gerenciamento e catalogo de feiticos magicos. O sistema permite cadastrar, consultar, atualizar parcialmente e remover feiticos, alem de contar com integracao ao Spring AI (Google GenAI) e persistencia em banco de dados relacional PostgreSQL.

## Tecnologias Utilizadas

* Java 21

* Spring Boot 4.x (Web MVC, Data JPA, Validation)

* Spring AI (Google GenAI Starter)

* PostgreSQL

* SpringDoc OpenAPI / Swagger UI

* Dotenv Java

* Lombok

* Maven

## Estrutura do Projeto

```
br.com.biblioteca.feiticos/
├── controller/          # Controladores REST da aplicacao
├── domain/              # Entidades JPA
├── dtos/
│   ├── request/         # Objetos de requisicao
│   └── response/        # Objetos de resposta 
├── enums/               # Enums de apoio ao dominio 
├── exception/           # Tratamento global de excecoes e erros
├── mapper/              # Conversores entre entidades e DTOs
├── repository/          # Interfaces de acesso a dados
├── service/             # Regras de negocio 
└── FeiticosApplication  # Classe principal de inicializacao da aplicacao

```

## Modelo de Dados

A entidade principal `Feitico` contem os seguintes atributos:

| Campo | Tipo | Descricao | 
 | ----- | ----- | ----- | 
| id | Long | Identificador unico auto-incremental (PK) | 
| nome | String | Nome identificador do feitico | 
| descricao | String (TEXT) | Descricao detalhada sobre o efeito da magia | 
| tipo | Tipo (Enum) | Categoria/tipo do feitico | 
| nivelDificuldade | Integer | Grau de complexidade ou dificuldade do feitiço | 

## Endpoints da API

Documentação interativa (Swagger UI) disponivel localmente em: `http://localhost:8080/swagger-ui.html`

### Rotas de Feiticos (`/feiticos`)

| Metodo | Endpoint | Descricao | Codigo de Sucesso | 
 | ----- | ----- | ----- | ----- | 
| POST | `/feiticos` | Cadastra um novo feitico | 201 Created | 
| GET | `/feiticos` | Retorna a lista de todos os feiticos | 200 OK | 
| GET | `/feiticos/{id}` | Busca os detalhes de um feitico por ID | 200 OK | 
| PATCH | `/feiticos/{id}` | Atualiza informacoes parciais do feitico | 200 OK | 
| DELETE | `/feiticos/{id}` | Remove um feitico da base de dados | 204 No Content | 

## Pré-requisitos

* Java 21

* Maven

* Instancia ativa do PostgreSQL

* Chave de API da Google (Gemini) configurada para o modulo Spring AI

## Configuração e Execução

### 1. Clonar o repositório

```
git clone https://github.com/driiDev/feiticos.git
cd feiticos
```

### 2. Variáveis de Ambiente (.env)

Crie um arquivo `.env` na raiz do projeto:

```
DB_URL=jdbc:postgresql://localhost:5432/feiticos_db
DB_USERNAME=seu_db
DB_PASSWORD=sua_senha
SPRING_AI_GOOGLE_GENAI_API_KEY=sua_chave_gemini_aqui
```

### 3. Build e Inicialização

Usando o Maven:

```
# Baixar dependencias e compilar
./mvnw clean compile

# Executar a aplicacao
./mvnw spring-boot:run

```

A aplicacao estará disponivel em: `http://localhost:8080`.