package ru.vsu.casher_service.dto;

import lombok.Data;

public @Data class TransferDTO {
    private Integer sum;
    private Long otherCardNumber;

}
