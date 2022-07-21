package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServiceVerificationCodeClient;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.responese.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {
    @Autowired
    ServiceVerificationCodeClient serviceVerificationCodeClient;

    private String verificationCodepPrefix = "passenger-verification-code-";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult generatirCode(String passengerPhone) {
        //获取验证码调用服务可以根据传入参数返回该参数位数的验证码 size
        ResponseResult<NumberCodeResponse> numberCodeReponse = serviceVerificationCodeClient.getNumberCode(6);
        int numberCode = numberCodeReponse.getData().getNumberCode();
        //TODO 写入redis
        String key = verificationCodepPrefix + passengerPhone;
        stringRedisTemplate.opsForValue().set(key, numberCode + "", 2, TimeUnit.MINUTES);
        //TODO 通过短信服务商，将对应的验证码发送到手机上，阿里短信服务，腾讯短信通，华信，容联
        return ResponseResult.success();
    }
}
