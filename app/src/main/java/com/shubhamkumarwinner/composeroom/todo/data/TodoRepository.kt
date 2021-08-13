package com.shubhamkumarwinner.composeroom.todo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(private val dao: TodoDao) {
    suspend fun insert(todo: Todo) = withContext(Dispatchers.IO){
        dao.insert(todo)
    }

    fun getAllTodos(): Flow<List<Todo>> = dao.getAllTodo()

    suspend fun deleteTodo(todo: Todo) = dao.deleteTodo(todo)

    suspend fun updateTodo(todo: Todo) = dao.updateTodo(todo)
}