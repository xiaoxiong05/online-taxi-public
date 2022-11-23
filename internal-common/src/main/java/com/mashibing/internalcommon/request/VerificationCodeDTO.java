package com.mashibing.internalcommon.request;

public class VerificationCodeDTO {

    private String passengerPhone;

    private String verificationCode;

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "VerificationCodeDTO{" +
                "passengerPhone='" + passengerPhone + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
