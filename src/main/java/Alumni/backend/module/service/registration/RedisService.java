package Alumni.backend.module.service.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getValue(String key) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        return value.get(key); // 없으면 null 반환
    }

    public void setValue(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void incrValue(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public void decrValue(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    public Long getValueCount(String key) {
        String str = redisTemplate.opsForValue().get(key);
        if (str == null) {
            return 0L;
        }
        return Long.valueOf(str);
    }
}
