package software.pxel.pxelsoftwaretestcase.util;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisDbFlusher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDbFlusher.class);
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisDbFlusher(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @PostConstruct
    public void flushRedisDb() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            connection.flushDb();
           LOGGER.info("Redis flushDb");
        } catch (Exception e) {
           LOGGER.error("Failed Redis flushDb");
        }
    }
}