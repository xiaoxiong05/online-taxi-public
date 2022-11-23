package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServicePassengerUserClient;
import com.mashibing.apipassenger.remote.ServiceVerificationCodeClient;
import com.mashibing.internalcommon.constart.CommonStatusEnum;
import com.mashibing.internalcommon.constart.IdentityConstant;
import com.mashibing.internalcommon.constart.TokenConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.VerificationCodeDTO;
import com.mashibing.internalcommon.responese.NumberCodeResponse;
import com.mashibing.internalcommon.responese.TokenResponce;
import com.mashibing.internalcommon.until.JwtUtils;
import com.mashibing.internalcommon.until.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {
    @Autowired
    ServiceVerificationCodeClient serviceVerificationCodeClient;
    @Autowired
    ServicePassengerUserClient servicePassengerUserClient;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult generatirCode(String passengerPhone) {
        //获取验证码调用服务可以根据传入参数返回该参数位数的验证码 size
        ResponseResult<NumberCodeResponse> numberCodeReponse = serviceVerificationCodeClient.getNumberCode(6);
        int numberCode = numberCodeReponse.getData().getNumberCode();
//        String key = RedisPrefixUtils.verificationCodepPrefix + passengerPhone;
        String key = RedisPrefixUtils.generalKeyByPhone(passengerPhone);
        stringRedisTemplate.opsForValue().set(key, numberCode + "", 1, TimeUnit.DAYS);
        //TODO 通过短信服务商，将对应的验证码发送到手机上，阿里短信服务，腾讯短信通，华信，容联
        return ResponseResult.success();
    }

    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        //查询Redis验证码
        String key = RedisPrefixUtils.generalKeyByPhone(passengerPhone);
        String redisValue = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value：" + redisValue);
        //验证码校验
        if (StringUtils.isBlank(redisValue)) {
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        //用户传入验证码错误
        if (!verificationCode.trim().equals(redisValue.trim())) {
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        //有无账号校验，没有注册
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
//        verificationCodeDTO.setVerificationCode(verificationCode);
        servicePassengerUserClient.loginOrRegister(verificationCodeDTO);
        //办法令牌
        String accessToken = JwtUtils.getGeneratorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstant.ACCESSTOKEN_TYPE);
        String refreshToken = JwtUtils.getGeneratorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstant.REFERESHTOKEN_TYPE);

        //请求token并保存在redis中
        String accessTokenKey = RedisPrefixUtils.generalTokenKey(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstant.ACCESSTOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);

        String refreshTokenKey = RedisPrefixUtils.generalTokenKey(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstant.REFERESHTOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);

        TokenResponce tokenResponce = new TokenResponce();
        tokenResponce.setAccessToken(accessToken);
        tokenResponce.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponce);
    }


}
