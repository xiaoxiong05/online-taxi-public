package com.mashibing.internalcommon.until;

public class RedisPrefixUtils {

    /**
     * verificationCodepPrefix: 验证码Key的前缀
     */
    public static String verificationCodepPrefix = "passenger-verification-code-";
    /**
     * tokenPrefix: Token Key的前缀
     */
    public static String tokenPrefix = "token-";

    /**
     * 拼redis中的key6
     *
     * @param passengerPhone 传入手机号
     * @return redis的key
     */
    public static String generalKeyByPhone(String passengerPhone) {
        return verificationCodepPrefix + passengerPhone;
    }

    /**
     * 根据手机及用户身份标识生成token
     *
     * @param phone    用户手机号码
     * @param identity 用户身份标识
     * @return token
     */
    public static String generalTokenKey(String phone, String identity, String tokenType) {
        return tokenPrefix + phone + "-" + identity + "-" + tokenType;
    }
}
