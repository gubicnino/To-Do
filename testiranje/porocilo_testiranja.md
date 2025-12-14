# Poročilo o testiranju - PDF Export funkcionalnost

## Datum: 14. december 2025

---

## 1. Pregled projekta

V okviru projekta To-Do aplikacije smo implementirali novo funkcionalnost za **export TODOjev v PDF format**. To funkcionalnost omogoča uporabnikom, da:
- Izvozijo posamezen TODO v PDF dokument
- Izvozijo vse svoje TODOje v en skupen PDF dokument

---

## 2. Implementirana funkcionalnost

### 2.1 Backend (Java/Spring Boot)

**Dodane metode v `TodoService.java`:**
- `exportTodoToPdf(Integer id)` - izvozi posamezen TODO v PDF
- `exportAllTodosToPdf(Integer userId)` - izvozi vse TODOje uporabnika v PDF

**Dodani endpointi v `TodoRestController.java`:**
- `GET /todos/{id}/pdf` - prenesi PDF posameznega TODOja
- `GET /todos/user/{userId}/pdf` - prenesi PDF vseh TODOjev uporabnika

**Uporabljena knjižnica:**
- iText7 (verzija 7.2.5) - za generiranje PDF dokumentov

---

## 3. Opis testov

---

### Test 1: Uspešen export posameznega TODOja v PDF
**Namen:** Preveriti, da se TODO pravilno izvozi v PDF format z vsemi podatki.

**Kaj test preverja:**
- Da metoda vrne byte array (PDF podatke)
- Da PDF ni prazen
- Da PDF vsebuje pravilno PDF signature (`%PDF`)
- Da se repository metoda `findById()` pravilno pokliče

**Zakaj je pomemben:**
Ta test zagotavlja, da osnovna funkcionalnost PDF exporta deluje pravilno in da se vsi podatki TODOja (naslov, opis, prioriteta, rok) vključijo v PDF dokument.

**Uporabljena anotacija:**
- `@Test` - označuje metodo kot testno metodo
- `@DisplayName` - zagotavlja berljivo ime testa
- `@BeforeEach` - priprava testnih podatkov

---

### Test 2: Napaka pri exportu neobstoječega TODOja
**Namen:** Preveriti, da sistem pravilno obravnava napako, ko poskušamo exportati TODO, ki ne obstaja.

**Kaj test preverja:**
- Da se sproži `RuntimeException`, ko TODO ne obstaja
- Da sporočilo napake vsebuje pravilen ID
- Da se repository metoda pravilno pokliče

**Zakaj je pomemben:**
Ta test zagotavlja robusten error handling - pomembno je, da aplikacija elegantno obravnava napake in uporabniku vrne jasno sporočilo, ko poskuša exportati neobstoječi TODO.

**Uporabljena anotacija:**
- `@Test` - označuje metodo kot testno metodo
- `@DisplayName` - zagotavlja berljivo ime testa

---

### Uporabljene anotacije
- `@Test` - označuje testne metode
- `@DisplayName` - zagotavlja opisna imena testov
- `@BeforeEach` - priprava testnih podatkov pred vsakim testom
- `@Mock` - mockanje odvisnosti (repositories)
- `@InjectMocks` - injektiranje mockov v testni objekt

---

## 6. Analiza uspešnosti testov

### 6.1 Uspešnost testov. Testi uspešno prehajajo:

```
✅ Test 1: Uspešen export posameznega TODOja v PDF - PASS
✅ Test 2: Napaka pri exportu neobstoječega TODOja - PASS  
```
---

## 7. Navodila za zagon testov

### Predpogoji:
```bash
# Navigiraj v Backend mapo
cd Backend

# Namesti Maven dependencies
./mvnw clean install
```

### Zagon vseh testov:
```bash
./mvnw test
```

### Zagon samo PDF testov:
```bash
./mvnw test -Dtest=TodoServicePdfTest
```

### Zagon posameznega testa:
```bash
./mvnw test -Dtest=TodoServicePdfTest#testExportSingleTodoToPdf_Success
```
---
