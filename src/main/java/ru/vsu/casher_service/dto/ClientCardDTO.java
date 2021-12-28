package ru.vsu.casher_service.dto;

import lombok.Data;

public @Data class ClientCardDTO {
    private Long cardNumber;
    private int PIN;
    private int counterPIN = 0;
    private int Cash =0;
    private boolean ban = false;
    private int clientId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private int id;
    private String passport;


}
