package ru.vsu.casher_service.models;

public class Card {
    private Long cardNumber;
    private int PIN;
    private int counterPIN = 0;
    private int Cash;
    private boolean ban = false;
    private String clientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }


    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }


    public int getCounterPIN() {
        return counterPIN;
    }

    public void setCounterPIN(int counterPIN) {
        this.counterPIN = counterPIN;
    }


    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

    public int getCash() {
        return Cash;
    }

    public void setCash(int cash) {
        Cash = cash;
    }
}
