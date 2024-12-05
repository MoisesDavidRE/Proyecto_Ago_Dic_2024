package com.example.proyecto_ago_dic_2024;

public class RequestModel {
    private String createdAt;
    private int idRequest;
    private String message;
    private int petId;
    private int requesterId;
    private String status;
    private String pet_name;
    private String image1;

    public RequestModel(String createdAt, int idRequest, String message, int petId, int requesterId, String status, String pet_name, String image1) {
        this.createdAt = createdAt;
        this.idRequest = idRequest;
        this.message = message;
        this.petId = petId;
        this.requesterId = requesterId;
        this.status = status;
        this.pet_name = pet_name;
        this.image1 = image1;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public String getMessage() {
        return message;
    }

    public int getPetId() {
        return petId;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public String getStatus() {
        return status;
    }
    public String getPet_name() {
        return pet_name;
    }

    public String getImage1() {
        return image1;
    }
}
