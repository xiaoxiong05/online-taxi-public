package com.mashibing.apipassenger.request;

public class VerificationCodeDTO {

    private String passengerPhone;

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    @Override
    public String toString() {
        return "VerificationCodeDTO{" +
                "passengerPhone='" + passengerPhone + '\'' +
                '}';
    }
}
