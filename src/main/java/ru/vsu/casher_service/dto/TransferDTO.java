package ru.vsu.casher_service.dto;

public class TransferDTO {
    private Integer sum;


    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Long getOtherCardNumber() {
        return otherCardNumber;
    }

    public void setOtherCardNumber(Long otherCardNumber) {
        this.otherCardNumber = otherCardNumber;
    }

    private Long otherCardNumber;

}
