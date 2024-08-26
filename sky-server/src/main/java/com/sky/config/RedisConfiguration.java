package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// 配置类，用于配置Redis的相关属性
@Configuration
// 使用Slf4j日志框架记录日志
@Slf4j
public class RedisConfiguration {
    /**
     * 创建并配置RedisTemplate对象
     * 该方法用于配置RedisTemplate，包括设置连接工厂和序列化器
     *
     * @param redisConnectionFactory Redis连接工厂，用于建立与Redis服务器的连接
     * @return 配置好的RedisTemplate对象
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 初始化RedisTemplate对象
        log.info("开始创建redisTemplate对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置redis连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置Redis key的序列化器为StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}

