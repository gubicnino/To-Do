package com.example.todo.todos;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;

public class TodoServiceCreateTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    private User testUser;
    private Todo testTodo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1);

        testTodo = new Todo();
        testTodo.setTitle("Testni Todo");
        testTodo.setDescription("Opis");
        testTodo.setDueDate(LocalDateTime.now().plusDays(1));
        testTodo.setPriority(Todo.Priority.HIGH);
    }

   /**
     * TEST 1: Pozitiven scenarij - uspešno ustvarjanje novega TODO-ja
      
     * Namen:
         Preveriti, da metoda createTodo() pravilno ustvari nov TODO, ko uporabnik obstaja
         in so podatki veljavni. Test zagotavlja, da se TODO shrani, poveže z uporabnikom
         ter da so klici repozitorijev izvedeni pravilno.
      
     * Uporabljene anotacije:
         - @Test: označuje metodo kot unit test
         - @DisplayName: omogoča berljivo ime testa
      
     * Koraki testa:
         1. Mockiramo UserRepository, da vrne obstoječega uporabnika
         2. Mockiramo TodoRepository.save(), da simulira shranjevanje TODO-ja
         3. Pokličemo metodo createTodo(1, testTodo)
         4. Preverimo, da metoda vrne ne-null objekt
         5. Preverimo, da je TODO povezan s pravim uporabnikom
         6. Preverimo, da so podatki (npr. naslov) pravilno preneseni
         7. Preverimo, da sta findById() in save() poklicana natanko enkrat
     */
    @Test
    @DisplayName("Pozitiven test: Ustvarjanje TODO-ja za obstoječega uporabnika")
    void testCreateTodoSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(todoRepository.save(testTodo)).thenReturn(testTodo);

        Todo created = todoService.createTodo(1, testTodo);

        assertNotNull(created);
        assertEquals(testUser, created.getUser());
        assertEquals("Testni Todo", created.getTitle());

        verify(todoRepository, times(1)).save(testTodo);
        verify(userRepository, times(1)).findById(1);
    }

    /**
     * TEST 2: Negativen scenarij - napaka pri ustvarjanju TODO-ja zaradi neobstoječega uporabnika
      
     * Namen:
        Preveriti, da metoda createTodo() pravilno vrže izjemo, ko uporabnik z danim ID-jem
        ne obstaja. Test zagotavlja, da se v takem primeru TODO NE shrani v repozitorij
        ter da metoda uporabnika preveri na pravilen način.

     * Koraki testa:
         1. Mockiramo UserRepository, da vrne Optional.empty() (uporabnik ne obstaja)
         2. Pokličemo createTodo() z neveljavnim ID-jem
         3. Preverimo, da metoda vrže RuntimeException
         4. Preverimo, da sporočilo napake vsebuje ustrezen opis napake ("User not found")
         5. Preverimo, da save() NI bil poklican (TODO se ne sme shraniti)
     */
    @Test
    @DisplayName("Negativen test: Napaka pri ustvarjanju TODO-ja, ker uporabnik ne obstaja")
    void testCreateTodoUserNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> todoService.createTodo(999, testTodo)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(todoRepository, never()).save(any());
    }

    /**
     * TEST 3: Pozitiven scenarij - preverjanje default vrednosti za priority
      
     * Namen:
        Preveriti, da metoda createTodo() pravilno nastavi privzeto vrednost MEDIUM za priority,
        ko ta atribut ni eksplicitno nastavljen (je null). Test zagotavlja, da sistem pravilno
        obvladuje manjkajoče podatke in uporabi smiselne privzete vrednosti.
        
     * Koraki testa:
         1. Ustvarimo TODO brez nastavljene priority (ostane null)
         2. Mockiramo UserRepository, da vrne obstoječega uporabnika
         3. Mockiramo TodoRepository.save() z ArgumentCaptor za zajemanje objekta
         4. Pokličemo createTodo(1, todoWithoutPriority)
         5. Preverimo, da je priority avtomatsko nastavljena na MEDIUM
         6. Preverimo, da so ostali podatki še vedno pravilno shranjeni
         7. Preverimo, da je save() bil poklican z objektom, ki ima priority MEDIUM
     */
    @Test
    @DisplayName("Pozitiven test: Nastavitev default priority na MEDIUM, če ni podana")
    void testCreateTodoDefaultPriority() {
        Todo todoWithoutPriority = new Todo();
        todoWithoutPriority.setTitle("TODO brez prioritete");
        todoWithoutPriority.setDescription("Test default vrednosti");
        todoWithoutPriority.setPriority(null); // Eksplicitno nastavimo na null

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Todo created = todoService.createTodo(1, todoWithoutPriority);

        assertNotNull(created);
        assertEquals(Todo.Priority.MEDIUM, created.getPriority(), "Priority bi moral biti nastavljen na MEDIUM");
        assertEquals("TODO brez prioritete", created.getTitle());
        assertEquals(testUser, created.getUser());

        verify(todoRepository, times(1)).save(any(Todo.class));
        verify(userRepository, times(1)).findById(1);
    }

    /**
     * TEST 4: Negativen scenarij - napaka pri shranjevanju TODO-ja v bazo podatkov
      
     * Namen:
        Preveriti, da metoda createTodo() pravilno obravnava situacijo, ko pride do napake
        pri shranjevanju v bazo podatkov (npr. database connection error, constraint violation).
        Test zagotavlja, da aplikacija ustrezno odzove na nepričakovane težave z bazo in
        da uporabnik dobi jasno sporočilo o napaki namesto tihe napake.
      
     * Uporabljene anotacije:
         - @Test: označuje metodo kot unit test
         - @DisplayName: omogoča opisno ime testa, ki jasno nakaže namen
      
     * Koraki testa:
         1. Ustvarimo veljaven TODO objekt z vsemi potrebnimi podatki
         2. Mockiramo UserRepository, da vrne obstoječega uporabnika
         3. Mockiramo TodoRepository.save(), da simulira napako pri shranjevanju
            (npr. RuntimeException za simulacijo database error)
         4. Pokličemo createTodo(1, testTodo) - pričakujemo izjemo
         5. Preverimo, da je bila vržena RuntimeException
         6. Preverimo, da sporočilo napake vsebuje pravilen opis problema
         7. Preverimo, da je bil save() poklican (napaka se zgodi pri shranjevanju)
         8. Preverimo, da je bil findById() poklican (uporabnik je bil uspešno najden)
     */
    @Test
    @DisplayName("Negativen test: Napaka pri shranjevanju TODO-ja v bazo podatkov")
    void testCreateTodoSaveFailure() {
        // Pripravimo veljaven TODO
        testTodo.setTitle("Testni TODO");
        testTodo.setDescription("Opis TODO-ja");
        testTodo.setPriority(Todo.Priority.HIGH);

        // Mock: Uporabnik obstaja
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        
        // Mock: Simuliramo napako pri shranjevanju (npr. database constraint violation)
        when(todoRepository.save(any(Todo.class)))
            .thenThrow(new RuntimeException("Database error: Unable to save TODO"));

        // Preverimo, da se vrže RuntimeException
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> todoService.createTodo(1, testTodo),
            "Pričakovali smo RuntimeException pri napaki shranjevanja"
        );

        // Preverimo, da sporočilo napake vsebuje pravilen opis
        assertTrue(
            exception.getMessage().contains("Database error"),
            "Sporočilo napake mora vsebovati 'Database error'"
        );
        
        // Preverimo, da so bili narejeni pravilni klici
        verify(userRepository, times(1)).findById(1);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }
}
