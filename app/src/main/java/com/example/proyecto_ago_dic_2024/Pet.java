package com.example.proyecto_ago_dic_2024;
import java.sql.Timestamp;

public class Pet {
    private int idPet;
    private int idUser ;
    private String name;
    private String animal;
    private String breed;
    private Integer age;
    private Integer gender;
    private String size;
    private String description;
    private String image1;
    private String image2;
    private String image3;
    private Double lat, lon;

    public Pet(int idPet, int idUser , String name, String animal, String breed, Integer age, Integer gender, String size, String description, String image1, String image2, String image3, Double lat, Double lon) {
        this.idPet = idPet;
        this.idUser  = idUser ;
        this.name = name;
        this.animal = animal;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.size = size;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters y Setters
    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public int getIdUser () {
        return idUser ;
    }

    public void setIdUser (int idUser ) {
        this.idUser  = idUser ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "idPet=" + idPet +
                ", idUser =" + idUser  +
                ", name='" + name + '\'' +
                ", animal='" + animal + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", size='" + size + '\'' +
                ", description='" + description + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' + '}';
    }
}
