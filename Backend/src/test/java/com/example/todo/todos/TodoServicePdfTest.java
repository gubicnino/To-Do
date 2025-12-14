package com.example.todo.todos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;

/**

 * 1. Uspešen export posameznega TODOja v PDF (pozitiven scenarij)
 * 2. Napaka pri exportu neobstoječega TODOja (negativen scenarij)

 */
@DisplayName("TodoService PDF Export Tests")
public class TodoServicePdfTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo testTodo;
    private User testUser;

    /**
     * Priprava testnih podatkov pred vsakim testom.
     * Anotacija @BeforeEach za inicializacijo.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // napravimo test subject
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        
        // TODO TEST SUBJECT
        testTodo = new Todo();
        testTodo.setId(1);
        testTodo.setTitle("Testni TODO");
        testTodo.setDescription("To je opis testnega TODOja za PDF export");
        testTodo.setPriority(Todo.Priority.HIGH);
        testTodo.setCompleted(false);
        testTodo.setDueDate(LocalDateTime.of(2025, 12, 31, 23, 59));
        testTodo.setUser(testUser);
    }

    /**
     * TEST 1: Pozitiven scenarij - uspešen export posameznega TODOja v PDF
     * 
     * Namen: Preveriti, da se TODO pravilno izvozi v PDF format z vsemi podatki.
     * Uporablja anotaciji: @Test in @DisplayName
     * 
     * Koraki testa:
     * 1. Mock repository vrne obstoječi TODO
     * 2. Kliči metodo exportTodoToPdf()
     * 3. Preveri, da je rezultat byte array (PDF)
     * 4. Preveri, da PDF ni prazen
     * 5. Preveri, da PDF vsebuje PDF signature (%PDF)
     */
    @Test
    @DisplayName("Test 1: Uspešen export posameznega TODOja v PDF")
    void testExportSingleTodoToPdf_Success() {
        when(todoRepository.findById(1)).thenReturn(Optional.of(testTodo));
        
        byte[] pdfBytes = todoService.exportTodoToPdf(1);
        
        assertNotNull(pdfBytes, "PDF bytes ne smejo biti null");
        assertTrue(pdfBytes.length > 0, "PDF mora vsebovati podatke");
        
        String pdfHeader = new String(Arrays.copyOfRange(pdfBytes, 0, Math.min(4, pdfBytes.length)));
        assertEquals("%PDF", pdfHeader, "PDF mora začeti z %PDF signature");
        
        verify(todoRepository, times(1)).findById(1);
    }

    /**
     * TEST 2: Negativen scenarij - napaka pri exportu neobstoječega TODOja
     * 
     * Namen: Preveriti, da sistem pravilno obravnava napako, ko poskušamo exportati TODO, ki ne obstaja.
     * Uporablja anotaciji: @Test in @DisplayName
     * 
     * Koraki testa:
     * 1. Mock repository vrne prazen Optional (TODO ne obstaja)
     * 2. Kliči metodo exportTodoToPdf()
     * 3. Preveri, da se sproži RuntimeException
     * 4. Preveri sporočilo napake
     */
    @Test
    @DisplayName("Test 2: Napaka pri exportu neobstoječega TODOja")
    void testExportSingleTodoToPdf_TodoNotFound() {
        when(todoRepository.findById(999)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> todoService.exportTodoToPdf(999),
            "Pričakovana RuntimeException za neobstoječi TODO"
        );
        
        assertTrue(
            exception.getMessage().contains("Todo not found with id: 999"),
            "Sporočilo napake mora vsebovati ID neobstoječega TODOja"
        );
        
        verify(todoRepository, times(1)).findById(999);
    }

}
