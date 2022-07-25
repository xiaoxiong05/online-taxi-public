package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServiceVerificationCodeClient;
import com.mashibing.internalcommon.constart.CommonStatusEnum;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.responese.NumberCodeResponse;
import com.mashibing.internalcommon.responese.TokenResponce;
import io.netty.util.internal.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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

    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        //查询Redis验证码
        String key = generalKeyByPhone(passengerPhone);
        String redisValue = stringRedisTemplate.opsForValue().get(key);
        //验证码校验
        if (StringUtils.isBlank(redisValue)) {
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        //用户传入验证码错误
        if (verificationCode.trim().equals(redisValue.trim())) {
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        //有无账号校验，没有注册
        System.out.println("判断是否有用户，并进行对应的处理");
        //TODO 判断是否有用户，没有创建

        //办法令牌
        System.out.println("办法令牌");
        TokenResponce tokenResponce = new TokenResponce();
        tokenResponce.setToken("token value");
        return ResponseResult.success(tokenResponce);
    }

    public String generalKeyByPhone(String passengerPhone) {
        return verificationCodepPrefix + passengerPhone;
    }
}
