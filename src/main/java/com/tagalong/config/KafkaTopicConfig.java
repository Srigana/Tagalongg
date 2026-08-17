package com.tagalong.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.post-created}")
    private String postCreated;

    @Value("${kafka.topics.request-made}")
    private String requestMade;

    @Value("${kafka.topics.status-updated}")
    private String statusUpdated;

    @Bean
    public NewTopic postCreatedTopic() {
        return TopicBuilder.name(postCreated).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic requestMadeTopic() {
        return TopicBuilder.name(requestMade).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic statusUpdatedTopic() {
        return TopicBuilder.name(statusUpdated).partitions(3).replicas(1).build();
    }
}
