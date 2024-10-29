package com.example.proyecto_ago_dic_2024;
public class Pokemon {
    private String name;
    private String image;
    private int weight;
    private int height;

    public Pokemon(String name, String image, int weight, int height) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public int getHeight() {
        return height;
    }
    public String getImageUrl() {
        return image;
    }
}

