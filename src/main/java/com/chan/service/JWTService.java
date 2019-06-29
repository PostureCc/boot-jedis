package com.chan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chan.AuthConstant;
import com.chan.context.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: Chan
 * @Date: 2019/6/29 16:17
 * @Description:
 */
@Component
public class JWTService {


    public static String key = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsfa%$@&54614321fdSADFSDAdrow32234545fdf>?N<:{LWPWfas";
    //签发者
    public static String issuer = "wetool";
    //面向用户
    public static String subject = "all";


    @Value("${shiro.expireTime}")
    private Long expireTime;

    private static final int mils = 1000;

    //代表token的有效时间,2小时
    public final static long KEEP_TIME = 7200;


    @Autowired
    private JedisService jedisService;

//    @Autowired
//    private AsyncTaskService asyncTaskService;

//    @Autowired
//    private AuthUserTokenInfoMapper authUserTokenInfoMapper;


    /**
     * 计算失效时间
     */
    private Long countOutTime() {
        HttpServletRequest request = getHttpServletRequest();
        String deviceplatform = request.getHeader("deviceplatform");
//        if (StringUtils.isNotEmpty(deviceplatform) && deviceplatform.equalsIgnoreCase("android")) return expireTime;
        return KEEP_TIME;
    }

    /**
     * 产生token
     *
     * @param userInfo 用户信息
     * @return
     */
    public String produceToken(Token userInfo) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(JSONObject.toJSONString(userInfo.getId())).setIssuedAt(new Date(nowMillis));

        if (subject != null) builder.setSubject(subject);
        if (issuer != null) builder.setIssuer(issuer);
        builder.signWith(signatureAlgorithm, signingKey);
//        if (keepTime >= 0) {
        Long outTime = countOutTime();
        long expMillis = nowMillis + (outTime * mils);
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
//        }
        String token = builder.compact();
        String userInfoJsonStr = JSON.toJSONString(userInfo);
        jedisService.setJedis(AuthConstant.AUTH_REDIS_KEY + token, userInfoJsonStr, outTime.intValue());

        //持久化
//        asyncTaskService.saveToken(token, userInfoJsonStr, expMillis / mils);
        return token;
    }


    /**
     * 更新token
     *
     * @param token
     * @return
     */
    public String updateToken(String token) {
        Token userInfo = verifyToken(token);
//        Object userInfo = claims.getId();
//        authService.logout(token);
        return produceToken(userInfo);
    }

    public void logout() {
        try {
            String token = getToken();
            logout(token);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void logout(String token) {
        try {
//            Long usrId = getUsrId(token);
            jedisService.delJedis(AuthConstant.AUTH_REDIS_KEY + token);

//            asyncTaskService.deleteToken(token);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }


    /**
     * 获取request
     */
    public HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取token
     */
    public String getToken() {
        try {
            HttpServletRequest request = getHttpServletRequest();
            String token = request.getHeader("access-token");
//            if (StringUtils.isNotEmpty(token)) return token;
            return token;
        } catch (Exception e) {
        }
        return null;
//        throw new Exception("登录已经失效，请重新登录！");
    }


    /**
     * 获取用户id
     */
    public Long getUserId(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key)).parseClaimsJws(token).getBody();
            if (body != null && body.getId() != null) return Long.valueOf(body.getId());
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
//        throw new ServiceException2(-1, "登录已经失效，请重新登录！");
    }

    /**
     * 获取用户id
     */
    public Long getUserId() {
        String token = getToken();
        return getUserId(token);
    }

    /**
     * 获取当前店铺id
     */
//    public Long getCurrentShopId() {
//        String token = getToken();
//        Long currentShopId = verifyToken(token).getCurrentShopId();
//        if (currentShopId == null) throw new ServiceException2("非法操作！");
//        return currentShopId;
//    }

    /**
     * 获取用户信息
     */
    public Token getUserInfo() {
        String token = getToken();
        return verifyToken(token);
    }

    /**
     * 验证token/获取用户信息
     *
     * @param token
     * @return
     */
    public Token verifyToken(String token) {
//        getUserId(token);
        String res = jedisService.getJedis(AuthConstant.AUTH_REDIS_KEY + token);
//        if (StringUtils.isNotEmpty(res)) {
        return JSONObject.parseObject(res, Token.class);
//        }

//        res = verifyLastingToken(token);
//        if (StringUtils.isNotEmpty(res)) {
//            return JSONObject.parseObject(res, Token.class);
//        }

//        logout(token);
//        throw new ServiceException2(-1, "登录已经失效，请重新登录！");
    }

    /**
     * 校验持久化token以防缓存击穿
     *
     * @param token
     * @return
     */
//    private synchronized String verifyLastingToken(String token) {
//        String res = jedisService.getJedis(AuthConstant.AUTH_REDIS_KEY + token);
//        if (StringUtils.isNotEmpty(res)) return res;
//
//        AuthUserTokenInfoDTO authUserTokenInfoDTO = authUserTokenInfoMapper.getTokenObj(token);
//        long currentTimeMillis = System.currentTimeMillis() / mils;
//
//        if (authUserTokenInfoDTO != null) {
//            if (currentTimeMillis <= authUserTokenInfoDTO.getInvalidTime()) {
//                jedisService.setJedis(AuthConstant.AUTH_REDIS_KEY + token, authUserTokenInfoDTO.getUserInfo());
//                return authUserTokenInfoDTO.getUserInfo();
//            } else {
//                asyncTaskService.deleteToken(token); //已失效
//            }
//        }
//        return null;
//    }

    /**
     * 修改当前店铺id
     */
    public void updateCurrentShopId(String token, Long shopId, Long hqId) {
        Token user = verifyToken(token);
//        user.setCurrentShopId(shopId);
//        if (hqId != null) {
//            user.setCurrentHqId(hqId);
//            if (hqId.equals(shopId)) user.setIsHq(true);
//            else user.setIsHq(false);
//        } else {
//            user.setIsHq(false);
//        }

        String currentUserInfo = JSON.toJSONString(user);
        jedisService.setJedis(AuthConstant.AUTH_REDIS_KEY + token, currentUserInfo);

//        asyncTaskService.saveToken(token, currentUserInfo, (System.currentTimeMillis() + (expireTime * mils)) / mils);
    }


    /**
     * 获取当前所有有效的登录token
     */
    public Set<String> getCurrentValidTokens() {
        return jedisService.likeKeys(AuthConstant.AUTH_REDIS_KEY + "*");
    }

    /**
     * 踢出指定用户登录
     *
     * @param userIds 需要踢出登录的用户id，null时踢出所有用户
     */
//    public void kickOutTokenByUserId(List<Long> userIds) {
//        Set<String> currentValidTokens = getCurrentValidTokens();
//        for (String currentValidToken : currentValidTokens) {
//            String token = currentValidToken.split(":")[1];
//            if (CollectionUtils.isNotEmpty(userIds)) {
//                try {
//                    Long userId = getUserId(token);
////                    if (userIds.contains(userId)) jedisService.delJedis(currentValidToken);
//                    if (userIds.contains(userId)) logout(token);
//                    continue;
//                } catch (Exception e) {
//                }
//            }
////            jedisService.delJedis(currentValidToken);
//            logout(token);
//        }
//    }


    /**
     * 踢出单个用户
     *
     * @param userId
     */
//    public void kickOutToken(Long userId) {
//        List<String> tokens = authUserTokenInfoMapper.findByUserId("\"userId\":" + userId);
//        if (CollectionUtils.isNotEmpty(tokens)) {
//            for (String token : tokens) {
//                logout(token);
//            }
//        }
//    }
}


