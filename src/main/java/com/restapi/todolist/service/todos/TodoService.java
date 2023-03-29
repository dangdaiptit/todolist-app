package com.restapi.todolist.service.todos;


import com.restapi.todolist.models.todos.Todo;

import java.util.List;

public interface TodoService {

    Todo saveTodo(Todo todo);

    List<Todo> getAllTodoByUser();

    Todo updateTodo(Todo todo, Long id);

    void deleteTodoById(Long id);

    Todo getTodoById(Long id);


}
