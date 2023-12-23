package org.gfg.minor1.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    // redis connection bean
    public LettuceConnectionFactory getLettuceConnection(){
        // host
        // port
        // password
        RedisStandaloneConfiguration standaloneConfiguration =
                new RedisStandaloneConfiguration(redisHost,redisPort);
        standaloneConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(standaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

//         convert project data into byte[] array to the redis
        redisTemplate.setStringSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

//         connection between from project to redis we get the object of connection and sent to this
//         method
        redisTemplate.setConnectionFactory(getLettuceConnection());
        return redisTemplate;
    }
}
