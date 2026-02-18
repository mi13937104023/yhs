package com.ddzn.dd.framework.common.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.base.UserTokenResult;
import com.ddzn.dd.model.constant.CommonConstants;
import com.ddzn.dd.model.constant.HttpStatusCodeConstants;
import com.ddzn.dd.model.constant.RedisKeyConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SYS_SECRET_KEY = "24ff4c6f-5d5d-11f0-9735-0242ac120006";
    private static final String MINI_SECRET_KEY = "ad6320db-63e0-11f0-a39b-c65c6d318158";
    private static final long EXPIRATION_TIME = 60 * 60 * 1000 * 72;

    private static Key getSigningKey(String source) {
        if (source.equals(CommonConstants.SYS_LABEL)) {
            return Keys.hmacShaKeyFor(SYS_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        } else if (source.equals(CommonConstants.MINI_LABEL)) {
            return Keys.hmacShaKeyFor(MINI_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    private static String getRedisKey(String source, String id) {
        if (source.equals(CommonConstants.SYS_LABEL)) {
            return RedisKeyConstants.sys_user_login_token + id;
        }
        if (source.equals(CommonConstants.MINI_LABEL)) {
            return RedisKeyConstants.mini_user_login_token + id;
        }
        return "";
    }

    /**
     * 生成 token（用户ID存在 subject，其他用户信息放入 claims）并写入 Redis
     */
    public String generateToken(UserTokenResult user) {
        Key key = getSigningKey(user.getSource());
        if (key == null) {
            throw new BusinessException("来源不合法");
        }
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + EXPIRATION_TIME);

        // 创建 claims 并把 userId 一起放进去
        Claims claims = Jwts.claims();
        claims.setSubject(String.valueOf(user.getUserId()));  // 放入 userId
        claims.put("nickName", user.getNickName());
        claims.put("realName", user.getRealName());
        claims.put("shortName", user.getShortName());
        claims.put("mobile", user.getMobile());
        claims.put("avatar", user.getAvatar());

        String token = Jwts.builder()
                .setClaims(claims) // 统一放 claims，不要再 setSubject 单独放
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        redisTemplate.opsForValue().set(getRedisKey(user.getSource(), String.valueOf(user.getUserId())), token, EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        return token;
    }

    /**
     * 解析 token 获取用户 ID
     */
    public Long parseUserId(String token, String source) {
        return Long.parseLong(parseClaims(token, source).getSubject());
    }

    /**
     * 解析 token 获取完整的用户信息
     */
    public UserTokenResult parseUser(String token, String source) {
        //校验token
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(HttpStatusCodeConstants.NO_LOGIN.toString(), "token 为空");
        }
        log.info("token:{}", token);
        String[] split = token.split(" ");
        if (!token.startsWith("Bearer ") || split.length != 2) {
            throw new BusinessException(HttpStatusCodeConstants.NO_LOGIN.toString(), "token 格式不正确");
        }
        Claims claims = parseClaims(split[1], source);
        UserTokenResult user = new UserTokenResult();
        user.setUserId(Long.parseLong(claims.getSubject()));
        user.setNickName(claims.get("nickName", String.class));
        user.setRealName(claims.get("realName", String.class));
        user.setShortName(claims.get("shortName", String.class));
        user.setMobile(claims.get("mobile", String.class));
        user.setAvatar(claims.get("avatar", String.class));
        return user;
    }

    /**
     * 判断 token 是否已过期
     */
    public boolean expired(String token, String source) {
        try {
            return parseClaims(token, source).getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * 删除 Redis 中的 token（退出登录）
     */
    public void invalid(Long userId, String source) {
        redisTemplate.delete(getRedisKey(source, String.valueOf(userId)));
    }

    /**
     * 校验token是否有效
     *
     * @param token
     * @param source
     * @return
     */
    public boolean isTokenValid(String token, String source) {
        try {
            parseClaims(token, source);  // 解析并校验
            return true;                 // 解析成功说明 token 有效
        } catch (BusinessException e) {
            log.warn("Token 校验失败: {}", e.getMessage());
            return false;                // 解析失败或业务异常说明 token 无效
        }
    }

    /**
     * 内部方法：解析 token 获取 claims
     */
    private Claims parseClaims(String token, String source) {
        try {
            //log.info("parseClaims 校验token-00 :{}", token);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(source))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String redisKey = getRedisKey(source, userId);
            Object cachedToken = redisTemplate.opsForValue().get(redisKey);
            //log.info("parseClaims 校验token-01 :{}", cachedToken);

            // 校验 token 是否与 Redis 中一致
            if (cachedToken == null || !token.equals(cachedToken.toString())) {
                throw new BusinessException("token 已失效，请重新登录");
            }

            return claims;
        } catch (JwtException e) {
            log.error("parseClaims 校验token-02:{},source:{}", token, source, e.getMessage());
            throw new BusinessException("token 不合法");
        }
    }

}
