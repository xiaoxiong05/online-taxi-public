package com.mashibing.apipassenger.intercepter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mashibing.internalcommon.constart.TokenConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.TokenResult;
import com.mashibing.internalcommon.until.JwtUtils;
import com.mashibing.internalcommon.until.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtIntercepter implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean result = true;
        String resultString = "";
        String token = request.getHeader("Authorization");
//        System.out.println("请求token："+token.toString());
        //检查token返回token携带的信息
        TokenResult tokenResult = JwtUtils.checkToken(token);

        if (tokenResult == null) {
            resultString = "token error";
            result = false;
        } else {
            //从redis中读取token
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String accessTokenKey = RedisPrefixUtils.generalTokenKey(phone, identity, TokenConstant.ACCESSTOKEN_TYPE);
            String refreshTokenKey = RedisPrefixUtils.generalTokenKey(phone, identity, TokenConstant.REFERESHTOKEN_TYPE);
            String accessTokenRedis = stringRedisTemplate.opsForValue().get(accessTokenKey);
            String refreshTokenRedis = stringRedisTemplate.opsForValue().get(refreshTokenKey);

            if ((StringUtils.isBlank(accessTokenRedis)) || (!token.trim().equals(accessTokenRedis.trim()))) {
                resultString = "token error";
                result = false;
            } else {
            }
        }

        if (!result) {
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }
        return result;
    }
}
