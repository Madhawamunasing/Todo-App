package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoServiceTest {
    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    private User user;
    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        todo = new Todo();
        todo.setId(1L);
        todo.setUser(user);
        todo.setTitle("test");
        todo.setDescription("this is a test.");
        todo.setCompleted(false);
    }

    @Test
    void TestCreateTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo createdTodo = todoService.CreateTodo(todo);

        assertNotNull(createdTodo);
        assertEquals("Test Todo", createdTodo.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void TestGetTodoById() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo foundTodo = todoService.GetTodoById(1L, user);

        assertNotNull(foundTodo);
        assertEquals("Test Todo", foundTodo.getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void TestGetTodoByIdNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            todoService.GetTodoById(1L, user);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Item not found.", exception.getReason());
    }

    @Test
    void TestUpdateComplete() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo updatedTodo = todoService.UpdateCompleteStatus(1L, true, user);

        assertTrue(updatedTodo.getCompleted());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void TestUpdateCompleteNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            todoService.UpdateCompleteStatus(1L, true, user);
        });

        assertEquals("Todo not found or not authorized to access", exception.getMessage());
    }

    @Test
    void TestDeleteSuccess() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.DeleteTodo(1L, user);

        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    void TestDeleteNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            todoService.DeleteTodo(1L, user);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Item not found.", exception.getReason());
    }
}
