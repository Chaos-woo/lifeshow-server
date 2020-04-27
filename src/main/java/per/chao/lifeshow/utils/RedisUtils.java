package per.chao.lifeshow.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/1/8 16:48
 **/
@Component
public class RedisUtils {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	// ==================> common <========================

	/**
	 * 指定缓存失效时间
	 *
	 * @param key
	 * @param expire
	 * @return
	 */
	public boolean expire(String key, Long expire) {
		try {
			if (expire > 0) {
				redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取过期时间
	 *
	 * @param key
	 * @return 返回0时代表永久有效
	 */
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key
	 * @return
	 */
	public Boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存，可多个
	 *
	 * @param keys
	 */
	@SuppressWarnings("unchecked")
	public void del(String... keys) {
		if (keys != null && keys.length > 0) {
			if (keys.length == 1) {
				redisTemplate.delete(keys[0]);
			} else {
				redisTemplate.delete(CollectionUtils.arrayToList(keys));
			}
		}
	}

	// ==================> string <========================

	/**
	 * 普通缓存获取
	 *
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置失效时间
	 *
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	@Transactional
	public boolean set(String key, Object value, Long expire) {
		try {
			redisTemplate.opsForValue().set(key, value);
			if (expire > 0) {
				expire(key, expire);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 *
	 * @param key
	 * @param delta
	 * @return
	 */
	public Long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子应该大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 *
	 * @param key
	 * @param delta
	 * @return
	 */
	public Long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子应该大于0");
		}
		return redisTemplate.opsForValue().decrement(key, delta);
	}

	// ==================> hash <========================

	/**
	 * HashGet
	 *
	 * @param key
	 * @param item
	 * @return
	 */
	public Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取key所对应的的所有键值
	 *
	 * @param key
	 * @return
	 */
	public Map<Object, Object> hmget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 向一个key中设置多个键值
	 *
	 * @param key
	 * @param map
	 * @return true成功 false失败
	 */
	public Boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 设置多个值，并设置失效时间
	 *
	 * @param key
	 * @param map
	 * @param expire
	 * @return true成功 false失败
	 */
	@Transactional
	public Boolean hmset(String key, Map<String, Object> map, Long expire) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (expire > 0) {
				expire(key, expire);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向hash结构中放入数据，若item存在则会覆盖
	 *
	 * @param key
	 * @param item
	 * @param value
	 * @return
	 */
	public Boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向hash结构中放入数据，若item存在则会覆盖,并设置失效时间
	 *
	 * @param key
	 * @param item
	 * @param value
	 * @param expire
	 * @return
	 */
	@Transactional
	public Boolean hset(String key, String item, Object value, Long expire) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (expire > 0) {
				expire(key, expire);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash结构中的键值
	 *
	 * @param key
	 * @param item
	 */
	public void hdel(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}


	/**
	 * 判断某个键值是否在hash表中
	 *
	 * @param key
	 * @param item
	 * @return
	 */
	public Boolean hHasKey(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash减少，如果不存在，就会创建一个并把新增后的值返回
	 *
	 * @param key
	 * @param item
	 * @param by
	 * @return
	 */
	public Long hincr(String key, String item, long by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash减少，如果不存在，就会创建一个并把新增后的值返回
	 *
	 * @param key
	 * @param item
	 * @param by
	 * @return
	 */
	public Long hdecr(String key, String item, long by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	// ==================> set <========================

	/**
	 * 根据key获取set中的所有值
	 *
	 * @param key
	 * @return
	 */
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询key的set中是否存在value
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set中
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 将数据放入set中并设置失效时间
	 *
	 * @param key
	 * @param expire
	 * @param values
	 * @return
	 */
	@Transactional
	public Long sSetAndExpire(String key, Long expire, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (expire > 0) {
				expire(key, expire);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 获取set的长度
	 *
	 * @param key
	 * @return
	 */
	public Long sGetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 移除set中的多个value值
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sRemove(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	// ==================> list <========================

	/**
	 * 获取list缓存内容
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Object> lGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存长度
	 *
	 * @param key
	 * @return
	 */
	public Long lGetSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 通过索引获取list中的值
	 *
	 * @param key
	 * @param index 0->第一个元素，1->第二个元素，-1->最后一个元素，-2->倒数第二个元素
	 * @return
	 */
	public Object lGetByIndex(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将值从list的左边插入
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean llPush(String key, Object value) {
		try {
			redisTemplate.opsForList().leftPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将值从list的左边弹出
	 *
	 * @param key
	 * @return
	 */
	public Object llPop(String key) {
		try {
			redisTemplate.opsForList().leftPop(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将值从list的右边插入
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean lrPush(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将值从list的右边弹出
	 *
	 * @param key
	 * @return
	 */
	public Object lrPop(String key) {
		try {
			redisTemplate.opsForList().rightPop(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将多个数据从左边放入list
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Boolean llPushAll(String key, List<Object> values) {
		try {
			redisTemplate.opsForList().leftPushAll(key, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将多个数据从右边放入list
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Boolean lrPushAll(String key, List<Object> values) {
		try {
			redisTemplate.opsForList().rightPushAll(key, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的值
	 *
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public Boolean lUpdateByIndex(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除list中N个值为value的元素
	 *
	 * @param key
	 * @param count N
	 * @param value
	 * @return
	 */
	public Long lRemove(String key, long count, Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	// ==================> 位图操作 <========================

	/**
	 * 将位图key中的pos位设置为0（false）或1（true）
	 *
	 * @param key
	 * @param pos
	 * @param status
	 * @return
	 */
	public Boolean setBit(String key, Integer pos, Boolean status) {
		return redisTemplate.execute((RedisCallback<Boolean>) con ->
				con.setBit(key.getBytes(), pos - 1, status)
		);
	}

	/**
	 * 获得位图key中pos位的值为0或1
	 *
	 * @param key
	 * @param pos
	 * @return
	 */
	public Boolean getBit(String key, Integer pos) {
		return redisTemplate.execute((RedisCallback<Boolean>) con ->
				con.getBit(key.getBytes(), pos - 1)
		);
	}

	/**
	 * 统计某个位图键值中值为1的数量
	 *
	 * @param key
	 * @return
	 */
	public Long bitCount(String key) {
		return redisTemplate.execute((RedisCallback<Long>) con ->
				con.bitCount(key.getBytes())
		);
	}

	/**
	 * 统计某个位图键值中值为1的数量，范围限制在[start,end]
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long bitCount(String key, long start, long end) {
		return redisTemplate.execute((RedisCallback<Long>) con ->
				con.bitCount(key.getBytes(), start - 1, end - 1)
		);
	}

	/**
	 * Bit OP 操作
	 *
	 * @param op
	 * @param saveKey
	 * @param desKey
	 * @return
	 */
	public Long bitOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
		byte[][] bytes = new byte[desKey.length][];
		for (int i = 0; i < desKey.length; i++) {
			bytes[i] = desKey[i].getBytes();
		}
		return redisTemplate.execute((RedisCallback<Long>) con ->
				con.bitOp(op, saveKey.getBytes(), bytes)
		);
	}

}
