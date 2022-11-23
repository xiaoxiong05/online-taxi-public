package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.constart.CommonStatusEnum;
import com.mashibing.internalcommon.constart.TokenConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.TokenResult;
import com.mashibing.internalcommon.responese.TokenResponce;
import com.mashibing.internalcommon.until.JwtUtils;
import com.mashibing.internalcommon.until.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult tokenRefresh(String refreshTokensrc) {
        //提取token包含的信息
        TokenResult tokenResult = JwtUtils.checkToken(refreshTokensrc);
        //检查解析token的对象是否为空
        if (tokenResult == null) {
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(), CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();
        //生成token的key
        String refreshTokenKey = RedisPrefixUtils.generalTokenKey(phone, identity, TokenConstant.REFERESHTOKEN_TYPE);

        //获取token key的reids值
        String refreshTokenRedisValue = stringRedisTemplate.opsForValue().get(refreshTokenKey);

        //比对传入token与redis中保存的值
        if ((StringUtils.isBlank(refreshTokenRedisValue)) || (!refreshTokensrc.trim().equals(refreshTokenRedisValue.trim()))) {
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(), CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        //生成新的token并保存到TokenResponce中
        String accessTokenKey = RedisPrefixUtils.generalTokenKey(phone, identity, TokenConstant.ACCESSTOKEN_TYPE);
        String accessToken = JwtUtils.getGeneratorToken(phone, identity, TokenConstant.ACCESSTOKEN_TYPE);
        String refreshToken = JwtUtils.getGeneratorToken(phone, identity, TokenConstant.REFERESHTOKEN_TYPE);
        //保存到redis中
        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);
        //返回重新生成的token
        TokenResponce tokenResponce = new TokenResponce();
        tokenResponce.setAccessToken(accessToken);
        tokenResponce.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponce);
    }
}
