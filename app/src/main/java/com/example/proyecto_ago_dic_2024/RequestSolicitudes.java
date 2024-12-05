package com.example.proyecto_ago_dic_2024;

public class RequestSolicitudes {
    private String idRequest;
    private String city;
    private String description;
    private String lastName1;
    private String lastName2;
    private String message;
    private String name;
    private String socialNetwork;
    private String status;

    public RequestSolicitudes(String city, String description, String lastName1, String lastName2,
                              String message, String name, String socialNetwork, String status, String idRequest) {
        this.city = city;
        this.description = description;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.message = message;
        this.name = name;
        this.socialNetwork = socialNetwork;
        this.status = status;
        this.idRequest =  idRequest;
    }

    // Getters para acceder a los datos
    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getLastName1() {
        return lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public String getStatus() {
        return status;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

