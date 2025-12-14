package com.example.todo.todos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
      
     * Uporabljene anotacije:
         - @Test: označuje metodo kot unit test
         - @DisplayName: določa opis testa
      
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
}
