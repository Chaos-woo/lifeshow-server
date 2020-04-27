package per.chao.lifeshow.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/29 13:11
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		setSerializer(redisTemplate);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * 为redistemplate配置序列化方式
	 * @param redisTemplate
	 */
	@SuppressWarnings("all")
	private void setSerializer(RedisTemplate<String, Object> redisTemplate) {
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisTemplate = new StringRedisSerializer();
		// key采用string序列化
		redisTemplate.setKeySerializer(stringRedisTemplate);
		// key的hash采用string序列化
		redisTemplate.setHashKeySerializer(stringRedisTemplate);
		// value采用jackson序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// value的hash采用jackson序列化
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
	}
}
