package com.restapi.todolist.service.todos.impl;

import com.restapi.todolist.models.todos.Todo;
import com.restapi.todolist.repository.todo.TodoRepository;
import com.restapi.todolist.repository.users.UserRepository;
import com.restapi.todolist.service.todos.TodoService;
import com.restapi.todolist.validation.todos.TodoAccessValidator;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    final TodoRepository todoRepository;

    final UserRepository userRepository;

    final TodoAccessValidator todoAccessValidator;

    public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository, TodoAccessValidator todoAccessValidator) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.todoAccessValidator = todoAccessValidator;
    }

    @Override
    @PostAuthorize("returnObject.user.username == authentication.name")
    public Todo saveTodo(Todo newTodo) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findUserByUsername(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("User not found with username " + authentication.getName()));
        newTodo.setUser(user);
        return todoRepository.save(newTodo);
    }


    @Override
    public List<Todo> getAllTodoByUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        return todoRepository.findAllByUserUsername(username);
    }

    @Override
    @PostAuthorize("returnObject.user.username == authentication.name")
    public Todo updateTodo(Todo todoUpdated, Long id) {
        var todo = todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Todo not found with id " + id));
        todo.setDescription(todoUpdated.getDescription());
        todo.setTargetDate(todoUpdated.getTargetDate());
        todo.setCompleted(todoUpdated.isCompleted());
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodoById(Long id) {
        var todoToDelete = todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Todo not found with id " + id));
        todoAccessValidator.validateTodoAccess(todoToDelete);
        todoRepository.delete(todoToDelete);
    }

    @Override
    public Todo getTodoById(Long id) {
        var todo = todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Todo not found with id " + id));
        todoAccessValidator.validateTodoAccess(todo);
        return todo;
    }
}
