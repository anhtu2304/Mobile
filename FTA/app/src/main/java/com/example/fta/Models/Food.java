package com.example.fta.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Food implements Serializable {
    private int id;
    private String title;
    private String picture;
    private String description;
    private int fee;
    private int star;
    private int time;
    private int calories;
    private int numberInCart;

    public Food() {
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public Food(int id, String title, String picture, String description, int fee, int star, int time, int calories) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.fee = fee;
        this.star = star;
        this.time = time;
        this.calories = calories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("fee", fee);
        return result;
    }
}
