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


Test 3: Uspešno ustvarjanje TODOja

Namen: Preveriti, da se TODO pravilno ustvari, ko uporabnik obstaja.
Test preverja:
- da UserRepository vrne obstoječega uporabnika,
- da metoda createTodo() uspešno ustvari TODO,
- da se todoRepository.save() pokliče,
- da ima ustvarjeni TODO pravilno nastavljenega uporabnika.

Pomen: Test potrjuje osnovno funkcionalnost aplikacije – ustvarjanje novih opravil.

Uporabljena anotacija:
-@Test – označuje testno metodo
-@DisplayName("Test create todo - success") – berljivo ime testa

Test 4: Napaka pri ustvarjanju TODOja za neobstoječega uporabnika

Namen: Preveriti pravilno obravnavo napake, če userId ne obstaja.
Test preverja:
- da UserRepository vrne Optional.empty(),
- da se sproži RuntimeException,
- da se todoRepository.save() ne pokliče.
- Pomen: Zagotavlja robustnost sistema in preprečuje shranjevanje neveljavnih podatkov.

Uporabljena anotacija:
- @Test – označuje testno metodo
- @DisplayName("Test create todo - user not found")

Test 5: Nastavitev default priority na MEDIUM

Namen: Preveriti, da se priority avtomatsko nastavi na MEDIUM, če ni podana.
Test preverja:
- da se TODO ustvari brez nastavljene priority (null),
- da metoda createTodo() nastavi default vrednost MEDIUM,
- da so ostali podatki pravilno shranjeni.

Pomen: Zagotavlja, da sistem pravilno obvladuje manjkajoče podatke z default vrednostmi.

Uporabljena anotacija:
- @Test – označuje testno metodo
- @DisplayName – berljivo ime testa

Test 6: Napaka pri shranjevanju TODO-ja v bazo

Namen: Preveriti pravilno obravnavo napake pri shranjevanju v bazo podatkov.
Test preverja:
- da se vrže RuntimeException pri database error,
- da sporočilo vsebuje pravilen opis napake,
- da so bili narejeni pravilni klici repozitorijev.

Pomen: Zagotavlja robusten error handling pri database operacijah.

Uporabljena anotacija:
- @Test – označuje testno metodo
- @DisplayName – berljivo ime testa

Uporabljene anotacije:
-@Test – označuje testno metodo
-@DisplayName – bolj berljivo ime testa
-@Mock – nadomesti odvisnosti (repositories)
-@InjectMocks – injicira mocke v testni razred
-@BeforeEach – priprava testnih podatkov

---

## 4. Člani skupine in odgovornosti

- **Nino Gubič** - Test 5 in Test 6
- **Jure Matiš** - Test 1 in Test 2
- **Gabrijel Bratina** - Test 3 in Test 4

---

## 6. Analiza uspešnosti testov

### 6.1 Uspešnost testov. Testi uspešno prehajajo:

```
✅ Test 1: Uspešen export posameznega TODOja v PDF - PASS
✅ Test 2: Napaka pri exportu neobstoječega TODOja - PASS
✅ Test 3: Uspešno ustvarjanje novega TODOja – PASS
✅ Test 4: Napaka pri ustvarjanju TODOja za neobstoječega uporabnika – PASS
✅ Test 5: Nastavitev default priority na MEDIUM – PASS
✅ Test 6: Napaka pri shranjevanju TODO-ja v bazo – PASS
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
