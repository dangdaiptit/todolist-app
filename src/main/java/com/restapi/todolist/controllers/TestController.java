package com.restapi.todolist.controllers;


import com.restapi.todolist.models.todos.Todo;
import com.restapi.todolist.repository.todo.TodoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test")
public class TestController {

    final TodoRepository todoRepository;

    public TestController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/todos/all/{id}")
    public ResponseEntity<List<Todo>> allTodoById(@PathVariable Long id) {

        List<Todo> todos = todoRepository.findTodoByUser_Id(id);

        if (todos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }


}
