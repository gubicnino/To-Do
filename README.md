# Todo CRUD Aplikacija

Celostna aplikacija za upravljanje opravil (todos) z Java Spring Boot backend-om in React frontend-om. Aplikacija omogoča uporabnikom registracijo, prijavo ter ustvarjanje, branje, posodabljanje in brisanje lastnih opravil.

---

## 📋 Kazalo

1. [Pregled projekta](#pregled-projekta)
2. [Tehnologije](#tehnologije)
3. [Projektna struktura](#projektna-struktura)
4. [Navodila za namestitev](#navodila-za-namestitev)
5. [Zagon aplikacije](#zagon-aplikacije)
6. [Dokumentacija za razvijalce](#dokumentacija-za-razvijalce)
7. [Standardi kodiranja](#standardi-kodiranja)
8. [API Endpoints](#api-endpoints)
9. [Navodila za prispevanje](#navodila-za-prispevanje)

---

## 🎯 Pregled projekta

Aplikacija omogoča uporabnikom:
- **Registracijo** novega računa z uporabniškim imenom, emailom in geslom
- **Prijavo** z uporabniškim imenom in geslom
- **Ustvarjanje** novih opravil (todo) z naslovom in opisom
- **Pregledovanje** lastnih opravil
- **Urejanje** obstoječih opravil
- **Brisanje** opravil
- **Odjavo** iz sistema

Vsak uporabnik vidi in upravlja **samo svoja opravila** (user-specific data).

---

## 🛠️ Tehnologije

### Backend
- **Java**: 25
- **Spring Boot**: 3.5.7
- **Spring Data JPA**: Za delo z bazo podatkov
- **MySQL**: 8.4.7
- **Maven**: Upravljanje odvisnosti
- **REST API**: Komunikacija med frontend-om in backend-om

### Frontend
- **React**: 19.2.0
- **Node.js**: v22.21.0
- **React Router DOM**: 7.9.4 - Routing
- **Axios**: 1.12.2 - HTTP klici
- **Material-UI (MUI)**: 7.3.4 - UI komponente
- **CSS3**: Stilizacija

### Baza podatkov
- **MySQL**: 8.4.7
- Dve glavni tabeli:
  - `users` (id, username, email, password)
  - `todos` (id, title, description, user_id)

---

## 📁 Projektna struktura

```
RIS/
├── backend/                          # Java Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/example/todo/
│   │       │       ├── controller/   # REST Controllers
│   │       │       │   ├── AuthController.java
│   │       │       │   ├── TodoController.java
│   │       │       │   └── UserController.java
│   │       │       ├── model/        # JPA Entities
│   │       │       │   ├── User.java
│   │       │       │   └── Todo.java
│   │       │       ├── repository/   # JPA Repositories
│   │       │       │   ├── UserRepository.java
│   │       │       │   └── TodoRepository.java
│   │       │       ├── service/      # Business Logic
│   │       │       │   ├── AuthService.java
│   │       │       │   ├── TodoService.java
│   │       │       │   └── UserService.java
│   │       │       └── TodoApplication.java
│   │       └── resources/
│   │           └── application.properties  # DB config
│   ├── pom.xml                       # Maven dependencies
│   └── target/                       # Build output
│
├── frontend/                         # React frontend
│   ├── public/
│   │   ├── index.html
│   │   └── manifest.json
│   ├── src/
│   │   ├── components/
│   │   │   ├── auth/                 # Authentication components
│   │   │   │   ├── LoginForm.js
│   │   │   │   ├── LoginForm.css
│   │   │   │   ├── RegisterForm.js
│   │   │   │   └── ProtectedRoute.js
│   │   │   ├── common/               # Reusable components
│   │   │   │   ├── Modal.js
│   │   │   │   └── Modal.css
│   │   │   ├── todos/                # Todo CRUD components
│   │   │   │   ├── TodoList.js
│   │   │   │   ├── TodoList.css
│   │   │   │   ├── TodoForm.js
│   │   │   │   └── TodoForm.css
│   │   │   ├── index/                # Landing page
│   │   │   │   ├── Index.js
│   │   │   │   └── Index.css
│   │   │   ├── routing/
│   │   │   │   └── Routing.js        # Route definitions
│   │   │   ├── Navigation.js         # Navbar
│   │   │   └── Navigation.css
│   │   ├── context/
│   │   │   └── UserContext.js        # Global user state
│   │   ├── services/
│   │   │   ├── api.js                # Axios instance
│   │   │   ├── auth.js               # Auth API calls
│   │   │   ├── todos.js              # Todo API calls
│   │   │   └── users.js              # User API calls
│   │   ├── App.js                    # Root component
│   │   ├── App.css
│   │   ├── index.js                  # Entry point
│   │   └── index.css
│   ├── package.json                  # NPM dependencies
│   ├── package-lock.json
│   └── .env                          # Environment variables
│
├── .gitignore
└── README.md                         # Ta dokument
```

---

## 🚀 Navodila za namestitev

### Predpogoji

Prepričajte se, da imate nameščeno:

1. **Node.js v22.21.0**
   - Preverite: `node --version`
   - Prenos: https://nodejs.org/

2. **Java 25**
   - Preverite: `java --version`
   - Prenos: https://www.oracle.com/java/technologies/downloads/

3. **Maven 3.9.11**
   - Preverite: `mvn --version`
   - Prenos: https://maven.apache.org/download.cgi

4. **MySQL 8.4.7**
   - Preverite: `mysql --version`
   - Prenos: https://dev.mysql.com/downloads/mysql/

---

### 1. Kloniranje repozitorija

```bash
git clone https://github.com/gubicnino/To-Do.git
cd To-Do
```

---

### 2. Nastavitev baze podatkov

#### Ustvarite MySQL bazo podatkov:

```sql
-- Povežite se v MySQL
mysql -u root -p

-- Ustvarite bazo
CREATE DATABASE todo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Ustvarite uporabnika (opcijsko)
CREATE USER 'todo_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON todo_db.* TO 'todo_user'@'localhost';
FLUSH PRIVILEGES;

-- Izhod
EXIT;
```

#### Tabele se ustvarijo avtomatsko ob prvem zagonu (JPA):

**Tabela `users`:**
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Tabela `todos`:**
```sql
CREATE TABLE todos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

---

### 3. Konfiguracija Backend-a

#### Uredite `backend/src/main/resources/application.properties`:

```properties
# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

```

#### Namestite backend odvisnosti:

```bash
cd backend
mvn clean install
```

---

### 4. Konfiguracija Frontend-a

#### Ustvarite `.env` datoteko v `frontend/` mapi:

```env
REACT_APP_BASE_URL=http://localhost:8080/api/v1
```

#### Namestite frontend odvisnosti:

```bash
cd frontend
npm install
```

---

## ▶️ Zagon aplikacije

### 1. Zaženite Backend (Terminal 1)

```bash
cd backend
mvn spring-boot:run
```

Backend bo dosegljiv na: **http://localhost:8080**

**Preverjanje:**
- Odprite: http://localhost:8080/api/v1/users
- Pričakovano: Prazen JSON array `[]` ali seznam uporabnikov

---

### 2. Zaženite Frontend (Terminal 2)

```bash
cd frontend
npm start
```

Frontend bo dosegljiv na: **http://localhost:3000**

**Preverjanje:**
- Odpre se brskalnik na http://localhost:3000
- Vidite landing page aplikacije

---

### 3. Uporaba aplikacije

1. **Registracija**: Kliknite "Login" → "Don't have an account? Register"
2. **Vnos podatkov**: Username, Email, Password
3. **Registracija**: Kliknite "Register"
4. **Avtomatska prijava**: Ste avtomatsko prijavljeni
5. **Ustvarjanje TODO**: Kliknite "New Todo" → Vnos Title in Description
6. **Seznam TODO**: Vidite vse svoje TODO-je
7. **Urejanje**: Kliknite "Edit" na TODO-ju
8. **Brisanje**: Kliknite "Delete" (potrdite)
9. **Odjava**: Kliknite "Logout" v navigaciji

---

## 👨‍💻 Dokumentacija za razvijalce

### Arhitektura

Aplikacija sledi **3-tier arhitekturi**:

```
Frontend (React)
      ↓ HTTP/REST
Backend (Spring Boot)
      ↓ JDBC
Database (MySQL)
```

---

### Frontend - React Component Hierarchy

```
App.js
├── UserContextProvider (Global state)
│   ├── Navigation
│   │   ├── Modal (Login)
│   │   │   └── LoginForm
│   │   └── Links
│   └── Routing
│       ├── Index (Landing page)
│       ├── ProtectedRoute
│       │   ├── TodoList
│       │   └── TodoForm
│       └── RegisterForm
```

---

### State Management

**Global State (Context API):**
- `UserContext.js` - trenutni uporabnik, prijava/odjava
- Dostopen iz katerekoli komponente z `useUser()` hook-om

**Local State (useState):**
- Vsaka komponenta upravlja svoj lokalni state (forme, loading, errors)

**Persistence:**
- `localStorage` - shrani trenutnega uporabnika (session persistence)

---

### API Komunikacija

**Axios Instance (`src/services/api.js`):**
```javascript
const api = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL,  // http://localhost:8080/api/v1
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
});
```

**Service Layer:**
- `auth.js` - Prijava, registracija, localStorage management
- `todos.js` - CRUD operacije za TODO-je
- `users.js` - CRUD operacije za uporabnike

---

### Backend - Spring Boot Layers

#### 1. **Controller Layer** (REST API)
- `AuthController.java` - `/api/v1/auth/*` endpoints
- `TodoController.java` - `/api/v1/todos/*` endpoints
- `UserController.java` - `/api/v1/users/*` endpoints

#### 2. **Service Layer** (Business Logic)
- Validacija podatkov
- Povezovanje med entitetami
- Error handling

#### 3. **Repository Layer** (Data Access)
- JPA Repositories za komunikacijo z bazo
- Custom query methods

#### 4. **Model Layer** (Entities)
- `User.java` - JPA entiteta za uporabnike
- `Todo.java` - JPA entiteta za TODO-je

---

### Podatkovni tok (Request Flow)

**Primer: Ustvarjanje TODO-ja**

```
1. User klikne "Save" v TodoForm
   ↓
2. handleSubmit() v TodoForm.js
   ↓
3. createTodo(userId, {title, description}) v todos.js
   ↓
4. axios.post('/todos?userId=1', payload)
   ↓
5. POST http://localhost:8080/api/v1/todos?userId=1
   ↓
6. TodoController.createTodo(@RequestParam userId, @RequestBody todo)
   ↓
7. todoService.createTodo(userId, todo)
   ↓
8. todoRepository.save(todo)
   ↓
9. MySQL INSERT INTO todos ...
   ↓
10. Response: Created Todo JSON
   ↓
11. Frontend: navigate('/todos')
```

---

## 📝 Standardi kodiranja

### Backend (Java)

#### Naming Conventions:
- **Classes**: PascalCase (`UserService`, `TodoController`)
- **Methods**: camelCase (`createTodo`, `findByUsername`)
- **Variables**: camelCase (`userId`, `todoList`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_LENGTH`, `DEFAULT_PAGE_SIZE`)

#### Package Structure:
```
com.example.todo
├── controller    # REST endpoints
├── service       # Business logic
├── repository    # Data access
├── model         # JPA entities
└── dto           # Data Transfer Objects (opcijsko)
```

#### Annotations:
```java
@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    @GetMapping
    public List<Todo> getTodos(@RequestParam Integer userId) {
        return todoService.getUserTodos(userId);
    }
}
```

#### Error Handling:
```java
try {
    // Business logic
} catch (EntityNotFoundException e) {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
}
```

---

### Frontend (React)

#### Naming Conventions:
- **Components**: PascalCase (`TodoList`, `LoginForm`)
- **Functions**: camelCase (`handleSubmit`, `loadTodos`)
- **Variables**: camelCase (`userId`, `isLoading`)
- **Constants**: UPPER_SNAKE_CASE (`API_BASE_URL`)
- **CSS Classes**: kebab-case (`todo-wrapper`, `btn-primary`)

#### Component Structure:
```javascript
// 1. Imports
import React, { useState } from 'react';
import './Component.css';

// 2. Component definition
export default function Component() {
  // 3. Hooks (useState, useEffect, custom hooks)
  const [state, setState] = useState('');
  
  // 4. Event handlers
  const handleEvent = () => {
    // Logic
  };
  
  // 5. Render
  return (
    <div className="component">
      {/* JSX */}
    </div>
  );
}
```

#### Hooks Rules:
- Vedno na vrhu komponente
- Nikoli v zanki ali if stavku
- Custom hooks začnejo z `use` (`useUser`, `useAuth`)

#### Props:
```javascript
// Destructure props
export default function Component({ title, onSubmit, isLoading }) {
  // ...
}
```

#### Conditional Rendering:
```javascript
{isLoading && <div>Loading...</div>}
{error && <div className="error">{error}</div>}
{todos.length === 0 ? <p>No todos</p> : <TodoList todos={todos} />}
```

---

### CSS

#### Class Naming (BEM-style):
```css
.component { }              /* Block */
.component__element { }     /* Element */
.component--modifier { }    /* Modifier */
```

#### Variables:
```css
:root {
  --primary-color: #4CAF50;
  --error-color: #c33;
  --border-radius: 8px;
}
```

#### Responsive:
```css
@media (max-width: 768px) {
  .component {
    /* Mobile styles */
  }
}
```

---

## 🔌 API Endpoints

### Authentication (`/api/v1/auth`)

#### POST `/auth/register`
**Opis**: Registracija novega uporabnika

**Request Body**:
```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "username": "john",
  "email": "john@example.com"
}
```

**Errors**:
- `400 Bad Request` - Username/Email already taken

---

#### POST `/auth/login`
**Opis**: Prijava uporabnika

**Request Body**:
```json
{
  "username": "john",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "username": "john",
  "email": "john@example.com"
}
```

**Errors**:
- `400 Bad Request` - Invalid username or password

---

### Users (`/api/v1/users`)

#### GET `/users`
**Opis**: Pridobi vse uporabnike

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "username": "john",
    "email": "john@example.com"
  }
]
```

#### GET `/users/{id}`
**Opis**: Pridobi specifičnega uporabnika

**Response** (200 OK):
```json
{
  "id": 1,
  "username": "john",
  "email": "john@example.com"
}
```

#### POST `/users`
**Opis**: Ustvari novega uporabnika

#### PUT `/users/{id}`
**Opis**: Posodobi uporabnika

#### DELETE `/users/{id}`
**Opis**: Izbriši uporabnika

---

### Todos (`/api/v1/todos`)

#### GET `/todos?userId={userId}`
**Opis**: Pridobi vse TODO-je za specifičnega uporabnika

**Query Parameters**:
- `userId` (required): ID uporabnika

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, bread, eggs",
    "userId": 1
  }
]
```

---

#### GET `/todos/{id}`
**Opis**: Pridobi specifični TODO

**Response** (200 OK):
```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, bread, eggs",
  "userId": 1
}
```

---

#### POST `/todos?userId={userId}`
**Opis**: Ustvari nov TODO za uporabnika

**Query Parameters**:
- `userId` (required): ID uporabnika

**Request Body**:
```json
{
  "title": "Buy groceries",
  "description": "Milk, bread, eggs"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, bread, eggs",
  "userId": 1
}
```

---

#### PUT `/todos/{id}`
**Opis**: Posodobi obstoječi TODO

**Request Body**:
```json
{
  "title": "Buy groceries (updated)",
  "description": "Milk, bread, eggs, cheese"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "title": "Buy groceries (updated)",
  "description": "Milk, bread, eggs, cheese",
  "userId": 1
}
```

---

#### DELETE `/todos/{id}`
**Opis**: Izbriši TODO

**Response** (204 No Content)

---

## 🤝 Navodila za prispevanje

### 1. Fork & Clone

```bash
# Fork projekta na GitHub
# Nato kloniraj svoj fork
git clone https://github.com/YOUR_USERNAME/To-Do.git
cd To-Do
```

---

### 2. Ustvarite novo vejo (branch)

```bash
git checkout -b feature/opis-spremembe
```

**Branch naming conventions**:
- `feature/ime-funkcionalnosti` - Nova funkcionalnost
- `bugfix/ime-napake` - Popravek napake
- `hotfix/kritična-napaka` - Nujna popravka
- `refactor/kaj-refaktoriraš` - Refaktorizacija kode

---

### 3. Razvojni cikel

#### Backend spremembe:
```bash
cd backend
# Naredi spremembe v Java kodi
mvn clean install    # Preveri, da se builda
mvn test            # Zaženi teste (če obstajajo)
```

#### Frontend spremembe:
```bash
cd frontend
# Naredi spremembe v React kodi
npm start           # Preveri v brskalniku
npm run build       # Preveri, da se builda
```

---

### 4. Commit sprememb

```bash
git add .
git commit -m "tip: kratek opis spremembe"
```

**Commit message conventions**:
- `feat: dodaj funkcionalnost X` - Nova funkcionalnost
- `fix: popravi napako v Y` - Popravek napake
- `refactor: refaktoriraj Z` - Refaktorizacija
- `style: popravi CSS v komponenti X` - Styling
- `docs: posodobi README` - Dokumentacija

---

### 5. Push & Pull Request

```bash
git push origin feature/opis-spremembe
```

Nato na GitHubu:
1. Odprite Pull Request
2. Opišite spremembe
3. Dodate screenshots (če je UI sprememba)
4. Počakate na code review

---

### Code Review Checklist

**Backend:**
- [ ] Koda se builda brez napak
- [ ] Vsi testi so zeleni
- [ ] API endpoints so dokumentirani
- [ ] Error handling je implementiran
- [ ] CORS je pravilno nastavljen

**Frontend:**
- [ ] Koda se builda brez napak
- [ ] Ni console.log() v produkcijski kodi
- [ ] Komponente so pravilno imenovane
- [ ] CSS je organiziran
- [ ] Responsive design (mobile friendly)
- [ ] Error handling (user-friendly sporočila)

**Skupno:**
- [ ] README je posodobljen (če je potrebno)
- [ ] Commit sporočila so jasna
- [ ] Branch ime sledi konvencijam

---

## 🐛 Debugging

### Backend Issues

**Problem**: Backend ne steče
```bash
# Preveri Java verzijo
java --version  # Mora biti 25

# Preveri Maven
mvn --version

# Preveri MySQL
mysql -u root -p
SHOW DATABASES;
```

**Problem**: Database connection error
- Preveri `application.properties`
- Preveri MySQL credentials
- Preveri, da MySQL server teče

---

### Frontend Issues

**Problem**: Frontend ne steče
```bash
# Preveri Node verzijo
node --version  # Mora biti v22.21.0

# Počisti cache
rm -rf node_modules package-lock.json
npm install
```

**Problem**: API calls fail (CORS error)
- Preveri `REACT_APP_BASE_URL` v `.env`
- Preveri, da backend CORS dovoljuje `http://localhost:3000`

---

### Common Errors

**Error**: "Cannot update component while rendering"
- **Vzrok**: Kličete state setter v render metodi
- **Fix**: Premaknite v event handler ali useEffect

**Error**: "401 Unauthorized"
- **Vzrok**: Uporabnik ni prijavljen
- **Fix**: Preverite, da je user v localStorage

**Error**: "400 Bad Request" pri registraciji
- **Vzrok**: Username/Email že obstaja
- **Fix**: Uporabite drugo ime/email

---

## 📚 Dodatni viri

### React
- [React Docs](https://react.dev/)
- [React Router](https://reactrouter.com/)
- [Axios Docs](https://axios-http.com/)

### Spring Boot
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### MySQL
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## 📄 Licenca

Ta projekt je narejen za učne namene kot del šolskega projekta.

---

## 👥 Avtorji

- **Vaše ime** - [gubicnino](https://github.com/gubicnino)

---

## 🎉 Zahvala

Hvala vsem, ki ste prispevali k razvoju tega projekta!

---

**Zadnja posodobitev**: November 2025
