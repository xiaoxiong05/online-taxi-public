package com.mashibing.servicepassengeruser.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassengerNameDto {

    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private String paggengerPhone;
    private String passengerName;
    private byte passengerGender;
    private byte state;

}
