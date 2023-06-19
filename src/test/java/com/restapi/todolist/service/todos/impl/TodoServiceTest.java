package com.restapi.todolist.service.todos.impl;

import com.restapi.todolist.models.todos.Todo;
import com.restapi.todolist.models.users.User;
import com.restapi.todolist.repository.todo.TodoRepository;
import com.restapi.todolist.repository.users.UserRepository;
import com.restapi.todolist.validation.todos.TodoAccessValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TodoAccessValidator todoAccessValidator;

    @InjectMocks
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Giả lập thông tin người dùng đang được xác thực
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getAllTodoByUser() {

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("john@example.com");
        user.setPassword("password");
        // Tạo danh sách các Todos
        List<Todo> todos = new ArrayList<>();
        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setDescription("Todo 1");
        todo1.setTargetDate(LocalDate.now());
        todo1.setCompleted(false);
        todo1.setUser(user); // Thiết lập đối tượng User cho Todos 1
        todos.add(todo1);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setDescription("Todo 2");
        todo2.setTargetDate(LocalDate.now().plusDays(1));
        todo2.setCompleted(false);
        todo2.setUser(user); // Thiết lập đối tượng User cho Todos 2
        todos.add(todo2);

        // Giả lập phương thức findAllByUserUsername() của TodoRepository trả về danh sách các Todos
        when(todoRepository.findAllByUserUsername(user.getUsername())).thenReturn(todos);


        // Gọi phương thức getAllTodoByUser()
        List<Todo> result = todoService.getAllTodoByUser();

        // Kiểm tra kết quả trả về
        assertEquals(2, result.size());
        assertEquals(todo1, result.get(0));
        assertEquals(todo2, result.get(1));

    }

    @Test
    void getALlTodoByUserWithNoTodo() {
        User user = new User();
        user.setId(1L);
        user.setUsername("daivu");
        user.setEmail("daivu@gmail.com");
        user.setPassword("password");

        List<Todo> todos = new ArrayList<>();
        when(todoRepository.findAllByUserUsername("daivu")).thenReturn(todos);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        List<Todo> result = todoService.getAllTodoByUser();
        assertTrue(result.isEmpty());
    }
}