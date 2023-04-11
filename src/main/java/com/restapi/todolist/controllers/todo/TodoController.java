package com.restapi.todolist.controllers.todo;


import com.restapi.todolist.models.todos.Todo;
import com.restapi.todolist.payload.response.MessageResponse;
import com.restapi.todolist.repository.users.UserRepository;
import com.restapi.todolist.service.todos.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/todos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TodoController {

    final TodoService todoService;
    final UserRepository userRepository;

    public TodoController(TodoService todoService, UserRepository userRepository) {
        this.todoService = todoService;
        this.userRepository = userRepository;
    }


    //Get todos list of logged-in users
    @GetMapping()
    public ResponseEntity<?> getAllTodoByUser() {
        List<Todo> todos = todoService.getAllTodoByUser();
        if (todos.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("List is empty"));
        } else {
            return ResponseEntity.ok(todos);
        }
    }

    //Get todos by id of logged-in users
    @GetMapping("{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    //Create Todos of logged-in users
    @PostMapping()
    public Todo createTodo(@Valid @RequestBody Todo newTodo) {
        return todoService.saveTodo(newTodo);
    }

    //update todos of logged-in users
    @PutMapping("/{id}")
    public Todo updateTodo(@Valid @PathVariable Long id, @RequestBody Todo todoUpdate) {
        return todoService.updateTodo(todoUpdate, id);
    }

    //delete todos of logged-in users
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTodoById(@PathVariable Long id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.ok(new MessageResponse("Delete todo successfully"));
    }


}
