package Alumni.backend.module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = true)
    public String getValue(String key) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        return value.get(key); // 없으면 null 반환
    }

    public void setValue(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setValueWithDate(String key, String value, Date expiresAt) {
        long durationMills = expiresAt.getTime() - System.currentTimeMillis();
        if (durationMills <= 0)
            return;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, durationMills, TimeUnit.MILLISECONDS);
    }

    public void setValueWithSeconds(String key, String value, Long expiredTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void updateValueWithDate(String key, Date newExpiresAt) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (value != null) {
            long durationMills = newExpiresAt.getTime() - System.currentTimeMillis();
            if (durationMills <= 0) {
                redisTemplate.delete(key);
            } else {
                valueOperations.set(key, value, durationMills, TimeUnit.MILLISECONDS);
            }
        }
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public void incrValue(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public void incrValueByDelta(String key, Integer delta) {
        redisTemplate.opsForValue().increment(key, delta);
    }

    public void decrValue(String key) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String str = value.get(key);
        if (str != null) {
            if (Integer.parseInt(str) <= 0) {
                deleteValue(key);
            } else {
                redisTemplate.opsForValue().decrement(key);
            }
        }
    }

    public void decrValueByDelta(String key, Integer delta) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String str = value.get(key);
        if (str != null) {
            if (Integer.parseInt(str) <= 0) {
                deleteValue(key);
            } else {
                redisTemplate.opsForValue().decrement(key, delta);
            }
        }
    }

    @Transactional(readOnly = true)
    public Integer getValueCount(String key) {
        String str = redisTemplate.opsForValue().get(key);
        if (str == null) {
            return 0;
        }
        return Integer.valueOf(str);
    }
}
