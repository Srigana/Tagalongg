package com.tagalong.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    /**
     * Search results per destination are cached for a short window and
     * invalidated whenever a new post lands (see TagAlongEventConsumer).
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer cacheManagerCustomizer(ObjectMapper objectMapper) {
        RedisCacheConfiguration postSearchConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        return builder -> builder.withCacheConfiguration("postSearch", postSearchConfig);
    }
}
