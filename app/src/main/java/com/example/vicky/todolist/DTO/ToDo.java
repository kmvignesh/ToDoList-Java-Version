package com.example.vicky.todolist.DTO;

import java.util.ArrayList;
import java.util.List;

public class ToDo {
    private long id = -1;
    private String name = "";
    private String createdAt = "";
    private List items = (List)(new ArrayList());

    public final long getId() {
        return this.id;
    }

    public final void setId(long var1) {
        this.id = var1;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName( String name) {
        this.name = name;
    }
    
    public final List getItems() {
        return this.items;
    }

    public final void setItems( List items) {
        this.items = items;
    }
}
