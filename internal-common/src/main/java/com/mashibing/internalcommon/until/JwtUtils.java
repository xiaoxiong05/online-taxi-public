package com.mashibing.internalcommon.until;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mashibing.internalcommon.constart.IdentityConstant;
import com.mashibing.internalcommon.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    public static final String SIGN = "5tgb&UJM";
    public static final String JWT_KEY_PHONE = "passengerPhone";
    public static final String JWT_KEY_IDENTITY = "identity";
    public static final String JWT_TOKEN_TYPE = "tokenType";
    public static final String JWT_TOKEN_TIME = "tokenTime";


    public static String getGeneratorToken(String passengerPhone, String identity, String tokenType) {
        Map<String, String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE, passengerPhone);
        map.put(JWT_KEY_IDENTITY, identity);
        map.put(JWT_TOKEN_TYPE, tokenType);
        map.put(JWT_TOKEN_TIME, Calendar.getInstance().getTime().toString());
        //规定超期时间
        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,5);
        Date date = calendar.getTime();*/

        JWTCreator.Builder builder = JWT.create();
        map.forEach(
                (k, v) -> {
                    builder.withClaim(k, v);
                }
        );
        //添加过期时间
//        builder.withExpiresAt(date);
        //生成token
        String token = builder.sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    public static TokenResult parseToken(String token) {

        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
//        Claim claim = verify.getClaim("passengerPhone");
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String name = verify.getClaim("passengerName").asString();
        String identity = verify.getClaim("identity").asString();
        String tokenType = verify.getClaim("tokenType").asString();

        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);
        return tokenResult;
    }

    public static TokenResult checkToken(String token) {
        TokenResult tokenResult = null;
        try {
            tokenResult = JwtUtils.parseToken(token);
        } catch (SignatureVerificationException e) {
//            resultString = "token sign error";
        } catch (TokenExpiredException e) {
//            resultString = "token time out";
        } catch (AlgorithmMismatchException e) {
//            resultString = "token AlgorithmMismatch error";
        } catch (Exception e) {
//            resultString = "token error";
        }
        return tokenResult;
    }

    public static void main(String[] args) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("passengerPhone","18616689146");
//        map.put("passengerName","zhangsan");
//        map.put("identity", IdentityConstant.PASSENGER_IDENTITY);
//        String token = getGeneratorToken(map);
//        System.out.println(token);
//        System.out.println(parseToken(token));
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzZW5nZXJQaG9uZSI6IjE4MTM1ODg5Nzk4IiwiaWRlbnRpdHkiOiIxIiwiZXhwIjoxNjY3MTgyNDM0fQ.dezf81WlpE9TCi_8t3D-coEUzXjCFDbb-PE-IFKAafI";
        TokenResult tokenResult = parseToken(token);
        System.out.println(tokenResult.getPhone());
        System.out.println(tokenResult.getIdentity());
    }

}
