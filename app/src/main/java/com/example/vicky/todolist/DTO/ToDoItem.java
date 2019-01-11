package com.example.vicky.todolist.DTO;

public class ToDoItem {

    private long id = -1L;
    private long toDoId = -1L;
    private String itemName = "";
    private boolean isCompleted;

    public final long getId() {
        return this.id;
    }

    public final void setId(long var1) {
        this.id = var1;
    }

    public final long getToDoId() {
        return this.toDoId;
    }

    public final void setToDoId(long var1) {
        this.toDoId = var1;
    }

    
    public final String getItemName() {
        return this.itemName;
    }

    public final void setItemName( String itemName) {
        this.itemName = itemName;
    }

    public final boolean isCompleted() {
        return this.isCompleted;
    }

    public final void setCompleted(boolean var1) {
        this.isCompleted = var1;
    }
}
