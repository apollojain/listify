package com.example.apollo.listify;

/**
 * Created by Apollo on 6/14/2015.
 */
public class ListItem {
    private String name;
    private String description;
    public ListItem(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }
}
