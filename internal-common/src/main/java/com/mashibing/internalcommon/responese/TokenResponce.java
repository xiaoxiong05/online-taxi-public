package com.mashibing.internalcommon.responese;

import lombok.Data;

@Data
public class TokenResponce {
    private String accessToken;
    private String refreshToken;
}
