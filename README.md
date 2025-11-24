# Todo CRUD Aplikacija

Celostna aplikacija za upravljanje opravil (todos) z Java Spring Boot backend-om in React frontend-om. Aplikacija omogoča uporabnikom registracijo, prijavo ter ustvarjanje, branje, posodabljanje in brisanje lastnih opravil.

---

## 📋 Kazalo

1. [Vizija projekta](#-vizija-projekta)
2. [Pregled projekta](#-pregled-projekta)
3. [Tehnologije](#️-tehnologije)
4. [Projektna struktura](#-projektna-struktura)
5. [Navodila za namestitev](#-navodila-za-namestitev)
6. [Zagon aplikacije](#️-zagon-aplikacije)
7. [Standardi kodiranja](#-standardi-kodiranja)
8. [Navodila za prispevanje](#-navodila-za-prispevanje)
9. [Besednjak](#-besednjak)

---

## 🌟 Vizija projekta

**Namen aplikacije:**
Todo CRUD aplikacija je zasnovana kot enostavna, a zmogljiva rešitev za osebno upravljanje opravil in nalog. Njen glavni namen je uporabnikom omogočiti učinkovito organizacijo vsakodnevnih obveznosti, projektnih nalog in dolgoročnih ciljev na enem mestu.

**Kaj želimo doseči:**
Naš cilj je ustvariti intuitivno in hitro aplikacijo, ki uporabnikom omogoča nemoteno sledenje svojim opravilom brez nepotrebne kompleksnosti. Želimo ponuditi osnovno, a robustno platformo za upravljanje nalog, ki jo je enostavno razširiti z dodatnimi funkcionalnostmi po potrebi.

**Komu je namenjena:**
Aplikacija je namenjena:
- **Posameznikom**, ki iščejo preprosto rešitev za dnevno organizacijo opravil
- **Študentom**, ki potrebujejo pregleden sistem za sledenje učnim nalogam in projektom
- **Profesionalcem**, ki želijo ločeno voditi osebna in delovna opravila
- **Razvijalcem**, ki iščejo osnovno CRUD aplikacijo kot izhodišče za lastne projekte

**Kako rešuje problem:**
Aplikacija naslavlja ključne izzive upravljanja opravil:
- **Personalizacija**: Vsak uporabnik upravlja svoja opravila v varnem, ločenem okolju
- **Enostavnost**: Minimalistični vmesnik omogoča hitro dodajanje in urejanje opravil brez učenja zapletenih funkcij
- **Dostopnost**: Spletna aplikacija je dostopna iz kateregakoli naprave z brskalnikom
- **Razširljivost**: Modularno zasnovan kodni sistem omogoča enostavno dodajanje novih funkcionalnosti (oznake, prioritete, roke, skupinska opravila, itd.)

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

## 🤝 Navodila za prispevanje

### 1. Fork & Clone

```bash
# Fork projekta na GitHub
# Nato kloniraj svoj fork
git clone https://github.com/YOUR_USERNAME/To-Do.git
cd To-Do
```

---

### 2. Delo na main

V tem projektu uporabljamo trunk-based development, zato spremembe delamo neposredno na main

---

### 3. Naredite spremembe

#### Backend spremembe:
```bash
cd backend
mvn clean install
```

#### Frontend spremembe:
```bash
cd frontend
npm start
npm run build
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
git push origin main
```

Nato na GitHubu:
1. Odprite Pull Request
2. Opišite spremembe
3. Dodate screenshots (če je UI sprememba)
4. Počakate na code review
---


## Use case diagram
Posodobljen UCD 

<img width="898" height="581" alt="Screenshot 2025-11-24 at 12 08 38" src="https://github.com/user-attachments/assets/73037bd4-bf1f-42b7-a347-3d81223ae19a" />


## Besednjak
Spodaj so razloženi ključni pojmi, ki se pojavljajo v projektu Todo CRUD aplikacije
---

📚 Aplikacijski pojmi
User (Uporabnik)

Oseba, ki se registrira, prijavi in upravlja s svojimi todos.

Todo (Opravilo)

Naloga, ki si jo uporabnik nastavi. Vsebuje:

  Naslov – kratek povzetek opravila
  
  Opis – podrobnejši opis naloge
  
  Datum ustvarjanja – kdaj je bilo opravilo dodano
  
  Datum zadnje posodobitve – kdaj je bilo nazadnje spremenjeno

  Rok (due date) – do kdaj naj bo naloga opravljena
  
  Status – npr. pending, in progress, completed
  
  Prioriteta – npr. low, medium, high
  
  Kategorija – npr. šola, služba, osebno
  
  Oznake (tags) – dodatno označevanje (npr. "fitness", "urgent")
  
  Ali je naloga pomembna (flag/starred) – označeno pomembno opravilo
  
  Opombe – dodatne informacije, ki jih uporabnik dopiše

  Ali je naloga zaključena (boolean) – hitri indikator stanja

Login / Logout

Postopek prijave in odjave iz aplikacije.

Todo List

Seznam vseh opravil, ki pripadajo določenemu uporabniku.

Filter / Sort

Možnost filtriranja todo-jev (npr. po statusu, prioritete) ali razvrščanja.

🧩 Splošni programski pojmi
API (Application Programming Interface)

Vmesnik, ki omogoča komunikacijo med dvema programskima komponentama (npr. frontend ↔ backend).

Arhitektura

Struktura in organizacija sistema, npr. razdelitev na frontend, backend in podatkovni sloj.

Avtentikacija (Authentication)

Postopek, kjer sistem preveri identiteto uporabnika (npr. prijava).

Avtorizacija (Authorization)

Določanje, ali ima uporabnik dovoljenje za določeno dejanje (npr. urejanje samo svojih todo-jev).

Bug (Napaka)

Nepravilno delovanje sistema zaradi napašne kode ali logike.

Endpoint

URL naslov na backendu, ki sprejme HTTP zahtevo (npr. GET /api/v1/todos).

Feature (Funkcionalnost)

Nova sposobnost ali izboljšava aplikacije.

Refaktoring (Refactoring)

Preoblikovanje kode za večjo berljivost ali učinkovitost brez spremembe funkcionalnosti.

🖥️ Frontend izrazi (React)
Component (Komponenta)

Ponovno uporabna gradbena enota UI-ja v Reactu (npr. TodoList, LoginForm).

User Session (Uporabniška seja)

Čas med prijavo in odjavo uporabnika; predstavlja trenutno stanje njegove prijave.

Props

Vrednosti, ki jih komponenta prejme od starševske komponente.

State (Stanje)

Podatki, ki jih komponenta hrani in jih lahko spreminja med delovanjem.

Hook

Posebna funkcija (npr. useState, useEffect), ki React komponentam omogoča uporabo logike.

Routing

Sistem navigacije med stranmi v aplikaciji (React Router DOM).

HTTP Request (zahteva)

Klic z brskalnika do backend-a (GET, POST, PUT, DELETE).

SPA (Single Page Application)

Spletna aplikacija, ki deluje na eni HTML strani in dinamično posodablja UI brez reload-a.

⚙️ Backend izrazi (Java / Spring Boot)
Controller

Razred, ki sprejema HTTP zahteve in vrača odgovore (REST API).

Service (Storitev)

Vrsta razreda, ki vsebuje poslovno logiko aplikacije.

Repository

Razred, ki skrbi za delo z bazo podatkov (JPA).

Entity

Razred, ki predstavlja tabelo v bazi podatkov (User, Todo).

DTO (Data Transfer Object)

Objekt za prenos podatkov med backend sloji ali do frontenda.

JPA (Java Persistence API)

Specifikacija za upravljanje entitet in interakcijo z bazo podatkov.

Hibernate

Implementacija JPA; skrbi za povezovanje Java objektov z bazo.

REST API

Arhitekturni slog za komunikacijo med sistemi preko HTTP metod.

Dependency Injection (DI)

Vzorec, ki omogoča avtomatsko vbrizgavanje odvisnosti (npr. @Autowired).

Exception Handling

Mehanizem, ki obravnava napake in vrača primerne HTTP statuse.

🗄️ Baza podatkov (MySQL)

Relacijska baza

Baza, ki podatke organizira v povezane tabele.

Primary Key (PK)

Unikatni identifikator v tabeli (id).

Foreign Key (FK)

Referenca na drug zapis v drugi tabeli (user_id v todos).

Migration

Postopek spremembe strukture baze (v tem projektu izvaja Hibernate).

DDL (Data Definition Language)

Ukazi za ustvarjanje ali spreminjanje tabel (CREATE TABLE, ALTER TABLE).


📦 Git terminologija
Repository (Repo)

Osrednje skladišče kode (GitHub projekt).

Commit

Shranjena sprememba v git zgodovini.

Branch (Veja)

Ločena linija razvoja; v tem projektu delamo večinoma na main.

Pull Request (PR)

Zahteva za združitev sprememb iz tvoje kode v glavno vejo.

Merge

Združevanje dveh vej.

Clone

Prenos repozitorija na lokalni računalnik.

Push

Pošiljanje lokalnih sprememb na GitHub.

Trunk-Based Development

Metodologija, kjer večina dela poteka neposredno na glavni veji (main).


## Podroben opis funkcionalnosti
ADMIN

Primer uporabe: Dodajanje uporabnikov                                                           ID: 1

Cilj: Admin doda v sistem dodati novega uporabnika

Akterji: Admin

Predpogoji: Admin mora biti prijavljen

Stanje sistema po PU: Sistem ima dodan nov uporabniški račun

Scenarij:
1. Admin izbere možnost Dodajanje uporabnikov.
2. Sistem prikaže obrazec za nov račun.
3. Admin vnese podatke (ime, e-pošta, geslo, vloga …).
4. Admin potrdi dodajanje.
5. Sistem ustvari novega uporabnika in ga shrani v bazo.
6. Sistem vrne potrditev uspešnega dodajanja.

Alternativni tokovi: -Admin vnese napačne ali nepopolne podatke ---> sistem sporoci napako in zahteva, da se popravi
		     -Admin prekine dodajanje

Izjeme: -Uporabnik z tem emailom že obstaja ---> izpiše se "Uporabnik z tem emailom že obstaja"

--------------------------------------------------------------------------------------------------------------------------------------------------------------------
ADMIN

Primer uporabe: Brisanje uporabnikov                                                           ID: 2

Cilj: Admin izbriše uporabnika iz sistema

Akterji: Admin

Predpogoji: -Admin mora biti prijavljen
	    -Uporabnik mora obstajati v bazi

Stanje sistema po PU: Izbran uporabnik je odstranjen iz baze

Scenarij:
1. Admin odpre seznam uporabnikov.
2. Admin izbere uporabnika, ki ga želi izbrisati.
3. Admin klikne Izbriši uporabnika.
4. Sistem zahteva potrditev brisanja.
5. Admin potrdi.
6. Sistem izbriše uporabnika in prikaže obvestilo o uspehu.

Alternativni tokovi: -Admin prekliče potrditev ---> uporabnik ostane v sistemu

Izjeme: -Uporabnik ne obstaja več ---> sistem prikaže napako

--------------------------------------------------------------------------------------------------------------------------------------------------------------------
ADMIN

Primer uporabe: Urejanje uporabnikov                                                        ID: 3

Cilj: Admin posodobi podatke uporabnika

Akterji: Admin

Predpogoji: -Admin mora biti prijavljen
	    -Uporabnik, ki ga admin ureja mora obstajati v bazi

Stanje sistema po PU: Posodobljeni podatki so shranjeni

Scenarij:
1. Admin odpre seznam uporabnikov (Pregled uporabnikov).
2. Admin izbere možnost Urejanje uporabnikov.
3. Sistem prikaže obrazec s trenutnimi podatki uporabnika.
4. Admin posodobi želene informacije.
5. Admin potrdi spremembe.
6. Sistem shrani posodobljene podatke.
7. Sistem posodobi pogled v Pregled uporabnikov.

Alternativni tokovi: -Admin ne vnese pravilnih podatkov ---> sistem zahteva popravek
		     -Admin prekine urejanje ---> spremebe se ne shranijo

Izjeme: -Uporabnik ne obstaja več ---> sistem prikaže napako
	-Spremeba povzroči nek konflikt, kot je npr. če ima nek uporabnik že ta email, potem vrne napako
  
  --------------------------------------------------------------------------------------------------------------------------------------------------------------------


**Zadnja posodobitev**: November 2025
  
--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN / UPORABNIK

Primer uporabe: Dodajanje todoja                                                           ID: 4

Cilj: Registriran uporabnik (ali admin) doda nov todo v svoj seznam

Akterji: Uporabnik, Admin

Predpogoji:
- Uporabnik mora biti prijavljen.

Stanje sistema po PU: Nov todo je shranjen v bazi in se prikaže v uporabnikovem seznamu.

Scenarij:
1. Uporabnik odpre stran za dodajanje todojev in klikne na gumb "Dodaj todo".
2. Sistem prikaže prazen obrazec z obveznimi polji (npr. naslov, opis, rok).
3. Uporabnik vnese podatke ter klikne "Dodaj".
4. Prikaže se potrdilo o uspehu.
5. Novi todo se pokaže 

Alternativni tokovi:
- Uporabnik ne vnese obveznih podatkov → sistem prikaže napako in zahteva popravek.
- Uporabnik prekine dodajanje → obrazec se zapre, spremembe se ne shranijo.

Izjeme:
- Backend vrne napako (npr. DB napaka) → frontend prikaže ustrezno sporočilo.
- Konflikt validacije (npr. prepovedani znaki ali predolgi podatki) → backend vrne napako s pojasnilom.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN / UPORABNIK

Primer uporabe: Brisanje todoja                                                           ID: 5

Cilj: Uporabnik izbriše obstoječ todo iz svojega seznama

Akterji: Uporabnik, Admin

Predpogoji:
- Uporabnik mora biti prijavljen.
- Todo, ki ga briše, mora obstajati in pripadati uporabniku (razen če je admin).

Stanje sistema po PU: Izbrani todo je odstranjen iz baze in ni več viden v seznamu.

Scenarij:
1. Uporabnik odpre seznam todojev in klikne gumb/ikono "Izbriši" pri želenem todoju.
2. Sistem prikaže potrdilo za potrditev.
3. Uporabnik potrdi brisanje.
4. Posodobbi se uporabnikov seznam z todoji.

Alternativni tokovi:
- Uporabnik prekliče potrditev → brisanje ni izvedeno.
- Uporabnik nima dovoljenja → sistem prikaže sporočilo o nedovoljenem ukrepu.

Izjeme:
- Todo ne obstaja (že izbrisan/odstranjen) → backend vrne napako, frontend prikaže obvestilo.
- Napaka pri brisanju (npr. DB napaka) → sistem ne izvede spremembe in obvesti uporabnika.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN / UPORABNIK

Primer uporabe: Potrditev brisanja todojev                ID: 6

Cilj: Uporabnik pridobi potrdilo da je uspesno izbrisal todo

Akterji: Uporabnik, Admin

Predpogoji:
- Uporabnik mora biti prijavljen.
- Izbrani todoji obstajajo in pripadajo uporabniku.

Stanje sistema po PU: Uporabnik prejme povratno informacijo.

Scenarij:
1. Po brisanju todoja uporabnik prejme obvestilo o uspešnosti
2. Uporabnik klikne gumb za zaprtje obvestila
3. Obvestilo se zapre

Alternativni tokovi:
- DUporabnik ne klikne gumba za zaprje -> obvestilo se samodejno zapre čez minuto

Izjeme:
- Pride do napake pri ustvrajanju obvestila

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN / UPORABNIK

Primer uporabe: Spreminjanje Todojev                ID: 7

Cilj: Uporabnik posodobi obstoječ todo (spremeni naslov, opis ali druge podatke)

Akterji: Uporabnik, Admin

Predpogoji:
- Uporabnik mora biti prijavljen.
- Todo, ki ga ureja, mora obstajati in pripadati uporabniku.

Stanje sistema po PU: Posodobljeni podatki todoja so shranjeni v bazi.

Scenarij:
1. Uporabnik odpre seznam todojev.
2. Uporabnik klikne na todo, ki ga želi urediti.
3. Sistem prikaže obrazec s trenutnimi podatki todoja.
4. Uporabnik spremeni želene informacije (naslov, opis, rok).
5. Uporabnik potrdi spremembe.
6. Sistem validira vnesene podatke.
7. Sistem shrani posodobljene podatke.
8. Sistem osveži prikaz in prikaže posodobljen todo.
9. Sistem prikaže obvestilo o uspešni posodobitvi.

Alternativni tokovi:
- Uporabnik ne vnese obveznih podatkov → sistem prikaže napako in zahteva popravek.
- Uporabnik prekine urejanje → spremembe se ne shranijo.

Izjeme:
- Todo ne obstaja več → sistem prikaže napako.
- Napaka pri shranjevanju → sistem ne posodobi todoja in prikaže sporočilo o napaki.
- Uporabnik nima dovoljenja za urejanje → sistem zavrne akcijo.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN / UPORABNIK

Primer uporabe: Pregled Todojev                ID: 8

Cilj: Uporabnik prikaže seznam vseh svojih todojev

Akterji: Uporabnik, Admin

Predpogoji:
- Uporabnik mora biti prijavljen.

Stanje sistema po PU: Stanje sistema se ne spremeni, prikazan je samo seznam todojev.

Scenarij:
1. Uporabnik odpre glavno stran aplikacije ali klikne na "Moji Todoji".
2. Sistem naloži vse todoje, ki pripadajo prijavljenemu uporabniku.
3. Sistem prikaže seznam todojev (naslov, opis, rok, status).
4. Uporabnik lahko pregleda vse svoje todoje.

Alternativni tokovi:
- Uporabnik nima nobenega todoja → sistem prikaže sporočilo "Nimate še nobenega todoja. Dodajte prvega!"

Izjeme:
- Napaka pri nalaganju podatkov → sistem prikaže sporočilo o napaki in omogoči osvežitev.
- Povezava z bazo ni na voljo → sistem prikaže obvestilo o težavi s povezavo.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN / UPORABNIK

Primer uporabe: Prenos Todojev v PDF                ID: 9

Cilj: Uporabnik izvozi svoje todoje v PDF dokument za tiskanje ali shranjevanje

Akterji: Uporabnik, Admin

Predpogoji:
- Uporabnik mora biti prijavljen.
- Uporabnik mora imeti vsaj en todo.

Stanje sistema po PU: Stanje sistema se ne spremeni, ustvarjen je PDF dokument.

Scenarij:
1. Uporabnik odpre seznam todojev.
2. Uporabnik klikne na gumb "Izvozi v PDF" ali "Natisni".
3. Sistem zbere vse uporabnikove todoje.
4. Sistem generira PDF dokument z oblikovanim seznamom todojev.
5. Sistem sproži prenos PDF datoteke.
6. Uporabnik shrani ali odpre PDF dokument.

Alternativni tokovi:
- Uporabnik nima todojev → sistem prikaže obvestilo "Nimate todojev za izvoz".
- Uporabnik prekliče prenos → PDF se ne ustvari.

Izjeme:
- Napaka pri generiranju PDF → sistem prikaže sporočilo o napaki.
- Brskalnik ne podpira prenosa → sistem ponudi alternativno možnost (npr. odpiranje v novem zavihku).

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN

Primer uporabe: Pregled uporabnikov                ID: 10

Cilj: Admin prikaže seznam vseh registriranih uporabnikov v sistemu

Akterji: Admin

Predpogoji:
- Admin mora biti prijavljen.

Stanje sistema po PU: Stanje sistema se ne spremeni, prikazan je samo seznam uporabnikov.

Scenarij:
1. Admin odpre stran za upravljanje uporabnikov.
2. Sistem naloži vse registrirane uporabnike.
3. Sistem prikaže seznam uporabnikov (ime, email, vloga, datum registracije).
4. Admin lahko pregleda vse uporabnike.

Alternativni tokovi:
- V sistemu ni nobenega uporabnika → sistem prikaže sporočilo "Ni registriranih uporabnikov".

Izjeme:
- Napaka pri nalaganju podatkov → sistem prikaže sporočilo o napaki in omogoči osvežitev.
- Admin nima ustreznih pravic → sistem zavrne dostop.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

ADMIN

Primer uporabe: Potrditev brisanja uporabnika                ID: 11

Cilj: Admin potrdi brisanje izbranega uporabnika za preprečitev neželenih brisanj

Akterji: Admin

Predpogoji:
- Admin mora biti prijavljen.
- Admin mora izbrati uporabnika za brisanje.

Stanje sistema po PU: Prikazan je potrditveni dialog, sistem še ni spremenil.

Scenarij:
1. Admin izbere uporabnika in klikne "Izbriši".
2. Sistem prikaže potrditveno okno z opozorilom.
3. Sistem prikaže podatke uporabnika (ime, email).
4. Admin prebere opozorilo in potrdi brisanje.
5. Sistem izvede brisanje uporabnika (vključi ID: 2).

Alternativni tokovi:
- Admin prekliče potrditev → uporabnik ni izbrisan, dialog se zapre.

Izjeme:
- Dialog se ne prikaže → sistem izvede brisanje brez potrditve (napaka).

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

