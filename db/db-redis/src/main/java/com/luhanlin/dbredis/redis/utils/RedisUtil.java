package com.luhanlin.dbredis.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类详细描述：redis 常用工具类
 *
 * @author liyongqiang
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 上午8:26
 */
@Component
public class RedisUtil {

    private static  StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static void set(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    public static String get(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }
    /**
     * 移出并获取列表的第一个元素，当列表不存在或者为空时，返回Null
     * @param key
     * @return String
     */
    public static String lPop(String key) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        return ops.leftPop(key);
    }

    /**
     * 根据key取出list所有值
     * @param key
     * @return
     */
    public static String getListValue(String key) {
        return String.valueOf(redisTemplate.opsForList().range(key,0,-1));
    }

    /**
     * 根据key取出list
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        return redisTemplate.opsForList().range(key,0,-1);
    }

    /**
     * 在列表中的尾部添加一个个值，返回列表的长度
     * @param key
     * @param value
     * @return Long
     */
    public static String rPush(String key,String value) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        return String.valueOf(ops.rightPush(key,value));
    }


    public static boolean del(String key) throws Exception{
        boolean b = false;
        try {
            final byte[] keys = redisTemplate.getStringSerializer().serialize(key);
            b = ((Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    Long result = null;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式

                    if (nativeConnection instanceof JedisCluster) {
                        result = (Long) ((JedisCluster) nativeConnection).del(keys);
                    }

                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        result= (Long) ((Jedis) nativeConnection).del(keys);
                    }
                    return result.intValue()==0?false:true;
                }
            })).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

    private static RedisScript<Long> rScript = new RedisScript<Long>() {

        @Override
        public String getSha1() {

            return DigestUtils.sha1DigestAsHex(getScriptAsString());
        }

        @Override
        public String getScriptAsString() {
            // --[[
            // 实现访问频率的脚本.
            // 参数:
            // KEY[1] 用来标识同一个用户的id
            // ARGV[1] 过期时间
            // ARGV[2] 过期时间内可以访问的次数
            // 返回值: 如果没有超过指定的频率, 则返回1; 否则返回0
            // ]]
            return  "if redis.call('exists', KEYS[1]) == 0 then\n"+
                    "redis.call('set', KEYS[1], 0)\n" +
                    "redis.call('expire', KEYS[1], ARGV[1])\n" +
                    "return 1\n"+
                    "else\n" +
                    "local times = tonumber(redis.call('get', KEYS[1]))\n" +

                    "if times >= tonumber(ARGV[2]) then\n" +
                    "return 0\n" +
                    "end\n" +

                    "return 1\n"+
                    "end\n";
        }

        @Override
        public Class<Long> getResultType() {
            // TODO Auto-generated method stub
            return Long.class;
        }
    };

    /**
     * 提供限制速率的功能
     *
     * @param key        关键字
     * @param expireTime 过期时间
     * @param count      在过期时间内可以访问的次数
     * @return 没有超过指定次数则返回true, 否则返回false
     */
    public static boolean isExceedRate(String key, long expireTime, int count) throws Exception{
        boolean b = false;
        try {
            final byte[][] keysAndArgs = new byte[3][];
            keysAndArgs[0] = redisTemplate.getStringSerializer().serialize(key);
            keysAndArgs[1] = redisTemplate.getStringSerializer().serialize(Long.toString(expireTime));
            keysAndArgs[2] = redisTemplate.getStringSerializer().serialize(Integer.toString(count));
            b = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    Long result = null;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式

                    if (nativeConnection instanceof JedisCluster) {
                        result = (Long) ((JedisCluster) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(rScript.getScriptAsString()), 1,keysAndArgs);
                    }

                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        result= (Long) ((Jedis) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(rScript.getScriptAsString()),1,keysAndArgs);
                    }
                    return result==null?false:result==0;
                }
            }).booleanValue();

        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }


    private static RedisScript<Long> once = new RedisScript<Long>() {

        @Override
        public String getSha1() {

            return DigestUtils.sha1DigestAsHex(getScriptAsString());
        }

        @Override
        public String getScriptAsString() {
            return  "if redis.call('exists', KEYS[1]) == 0 then\n"+
                    "redis.call('set', KEYS[1], 1)\n" +
                    "redis.call('expire', KEYS[1], 5)\n" +
                    "return 1\n"+
                    "else\n" +
                    "return 0\n"+
                    "end\n";
        }

        @Override
        public Class<Long> getResultType() {
            // TODO Auto-generated method stub
            return Long.class;
        }
    };

    /**
     * 同手机号同时尝试次数超过1次时返回true
     * @param key
     * @return
     * @throws Exception
     */
    public static boolean isExceedRateMeanwhile(String key) throws Exception{
        boolean b = false;
        try {
            final byte[][] keysAndArgs = new byte[1][];
            keysAndArgs[0] = redisTemplate.getStringSerializer().serialize(key);
            b = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    Long result = null;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式

                    if (nativeConnection instanceof JedisCluster) {
                        result = (Long) ((JedisCluster) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(once.getScriptAsString()), 1,keysAndArgs);
                    }

                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        result= (Long) ((Jedis) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(once.getScriptAsString()),1,keysAndArgs);
                    }
                    return result==null?false:result==0;
                }
            }).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

    private static boolean exceptionContainsNoScriptError(Exception e) {

        if (!(e instanceof NonTransientDataAccessException)) {
            return false;
        }

        Throwable current = e;
        while (current != null) {

            String exMessage = current.getMessage();
            if (exMessage != null && exMessage.contains("NOSCRIPT")) {
                return true;
            }

            current = current.getCause();
        }

        return false;
    }


    /**
     * 自加1
     * @param key
     */
    public static boolean incrByOne(final String key) {
        boolean b = false;
        try {
            b = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection){
                    Object nativeConnection;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式
                    try {
                        nativeConnection = connection.getNativeConnection();
                        if (nativeConnection instanceof JedisCluster) {
                            ((JedisCluster) nativeConnection).incr(redisTemplate.getStringSerializer().serialize(key));
                        }
                        // 单机模式
                        else if (nativeConnection instanceof Jedis) {
                            ((Jedis) nativeConnection).incr(redisTemplate.getStringSerializer().serialize(key));
                        }
                    } catch (JedisDataException e) {
                        return false;
                    }
                    return true;
                }
            }).booleanValue();
        }catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

    public static boolean exists(final String key) {
        boolean b = false;
        try {
            b = (redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection;
                    try {
                        nativeConnection = connection.getNativeConnection();
                        Long result = null;
                        // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                        // 集群模式

                        if (nativeConnection instanceof JedisCluster) {
                            return ((JedisCluster) nativeConnection).exists(redisTemplate.getStringSerializer().serialize(key));
                        }

                        // 单机模式
                        else if (nativeConnection instanceof Jedis) {
                           return ((Jedis) nativeConnection).exists(redisTemplate.getStringSerializer().serialize(key));
                        }

                    } catch (JedisDataException e) {
                        return false;
                    }
                    return true;
                }
            })).booleanValue();

        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

    public static boolean set(final String key, final String value,
                              final long time) {
        boolean b = false;
        try {
            b = (redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection;
                    try {
                        nativeConnection = connection.getNativeConnection();
                        // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                        // 集群模式
                        if (nativeConnection instanceof JedisCluster) {
                            byte[] keys = redisTemplate.getStringSerializer().serialize(key);
                            ((JedisCluster) nativeConnection).set(keys, redisTemplate.getStringSerializer().serialize(value));
                            ((JedisCluster) nativeConnection).expireAt(keys, time);
                        }

                    // 单机模式
                        else if (nativeConnection instanceof Jedis) {
                            byte[] keys = redisTemplate.getStringSerializer().serialize(key);
                            ((Jedis) nativeConnection).set(keys, redisTemplate.getStringSerializer().serialize(value));
                            ((Jedis) nativeConnection).expireAt(keys, time);
                         }
                    } catch (JedisDataException e) {
                        return false;
                    }
                    return true;
                }
            })).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return true;
    }


    private static RedisScript<Integer> incrScript = new RedisScript<Integer>() {
        @Override
        public String getSha1() {
            return DigestUtils.sha1DigestAsHex(getScriptAsString());
        }

        @Override
        public String getScriptAsString() {

            return  "if redis.call('exists', KEYS[1]) == 0 then\n"+
                    "redis.call('set', KEYS[1], 1)\n" +
                    "redis.call('expire', KEYS[1], ARGV[1])\n" +
                    "return 1\n"+
                    "else\n" +
                    "redis.call('incr', KEYS[1])\n" +
                    "return 1\n" +
                    "end\n";
        }

        @Override
        public Class<Integer> getResultType() {
            return Integer.class;
        }
    };


    private static Map<String,byte[]> keyMap = new HashMap<String, byte[]>();

    public static boolean incrWithExpire(final String key,final String expireTime) {
        boolean b = false;
        try {
            final byte[][] keysAndArgs = new byte[2][];
            if(keyMap.containsKey(key)){
                keysAndArgs[0] = keyMap.get(key);
            }else{
                keysAndArgs[0] = redisTemplate.getStringSerializer().serialize(key);
                keyMap.put(key, keysAndArgs[0]);
            }
            if(keyMap.containsKey(expireTime)){
                keysAndArgs[1] = keyMap.get(expireTime);
            }else{
                keysAndArgs[1] = redisTemplate.getStringSerializer().serialize(expireTime);
                keyMap.put(expireTime, keysAndArgs[1]);
            }

            b = ((Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    Long result = null;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式

                    if (nativeConnection instanceof JedisCluster) {
                        result = (Long) ((JedisCluster) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(incrScript.getScriptAsString()), 1,keysAndArgs);
                    }

                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        result= (Long) ((Jedis) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(incrScript.getScriptAsString()), 1,keysAndArgs);
                    }
                    return result==null?false:result.longValue()==1;
                }
            })).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }


    private static RedisScript<Integer> queueScript = new RedisScript<Integer>() {
        @Override
        public String getSha1() {
            return DigestUtils.sha1DigestAsHex(getScriptAsString());
        }

        @Override
        public String getScriptAsString() {

            return  "if redis.call('exists', KEYS[1]) == 0 then\n"+
                    "redis.call('RPUSH', KEYS[1], ARGV[2])\n" +
                    "return 1\n"+
                    "else\n" +
                    "local lenght = redis.call('LLEN', KEYS[1])\n" +
                    "if lenght >= tonumber(ARGV[1]) then\n"+
                    "redis.call('LPOP', KEYS[1])\n" +
                    "end\n"+
                    "redis.call('RPUSH', KEYS[1], ARGV[2])\n" +
                    "return 1\n" +
                    "end\n";
        }

        @Override
        public Class<Integer> getResultType() {
            return Integer.class;
        }
    };

    public static List<String> getElesFromQueue(final String key) {
        List<String> b ;
        try {
            b = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection con)
                        throws DataAccessException {
                    List<String> list = new ArrayList<String>();
                    try {
                        byte[] keys = redisTemplate.getStringSerializer()
                                .serialize(key);
                        int len = Integer.valueOf(QUEUE_SIZE);

                        while(len>0){
                            len--;
                            byte[] value = con.lPop(keys);
                            if(value==null){
                                break;
                            }
                            list.add(redisTemplate.getStringSerializer().deserialize(value));
                        }

                    } catch (JedisDataException e) {
                        return null;
                    }
                    return list;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            b = null;
        }
        return b;
    }

    public static final String QUEUE_SIZE = "3";

    public static boolean addEle2Queue(final String key,final String value) {
        boolean b = false;
        try {
            final byte[][] keysAndArgs = new byte[3][];
            if(keyMap.containsKey(key)){
                keysAndArgs[0] = keyMap.get(key);
            }else{
                keysAndArgs[0] = redisTemplate.getStringSerializer().serialize(key);
                keyMap.put(key, keysAndArgs[0]);
            }

            if(keyMap.containsKey(QUEUE_SIZE)){
                keysAndArgs[1] = keyMap.get(QUEUE_SIZE);
            }else{
                keysAndArgs[1] = redisTemplate.getStringSerializer().serialize(QUEUE_SIZE);
                keyMap.put(QUEUE_SIZE, keysAndArgs[1]);
            }

            keysAndArgs[2] = redisTemplate.getStringSerializer().serialize(value);

            b = ((Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    Long result = null;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式

                    if (nativeConnection instanceof JedisCluster) {
                        result = (Long) ((JedisCluster) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(queueScript.getScriptAsString()), 1, keysAndArgs);
                    }

                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        result= (Long) ((Jedis) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(queueScript.getScriptAsString()), 1, keysAndArgs);
                    }
                    return result==null?false:result.longValue()==1;
                }
            })).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }


    private static RedisScript<Integer> addScript = new RedisScript<Integer>() {
        @Override
        public String getSha1() {
            return DigestUtils.sha1DigestAsHex(getScriptAsString());
        }

        @Override
        public String getScriptAsString() {

            return  "if redis.call('exists', KEYS[1]) == 0 then\n"+
                    "redis.call('set', KEYS[1], ARGV[2])\n" +
                    "redis.call('expire', KEYS[1], ARGV[1])\n" +
                    "return 1\n"+
                    "else\n" +
                    "local money = tonumber(redis.call('get', KEYS[1]))\n" +
                    "money = money+ tonumber(ARGV[2])\n" +
                    "redis.call('set', KEYS[1],money)\n" +
                    "return 1\n" +
                    "end\n";
        }

        @Override
        public Class<Integer> getResultType() {
            return Integer.class;
        }
    };

    public static boolean addWithExpire(final String key,final String money,final String expireTime) {
        boolean b = false;
        try {
            final byte[][] keysAndArgs = new byte[3][];
            if(keyMap.containsKey(key)){
                keysAndArgs[0] = keyMap.get(key);
            }else{
                keysAndArgs[0] = redisTemplate.getStringSerializer().serialize(key);
                keyMap.put(key, keysAndArgs[0]);
            }
            if(keyMap.containsKey(expireTime)){
                keysAndArgs[1] = keyMap.get(expireTime);
            }else{
                keysAndArgs[1] = redisTemplate.getStringSerializer().serialize(expireTime);
                keyMap.put(expireTime, keysAndArgs[1]);
            }

            keysAndArgs[2] = redisTemplate.getStringSerializer().serialize(money);

            b = ((Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    Long result = null;
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式

                    if (nativeConnection instanceof JedisCluster) {
                        result = (Long) ((JedisCluster) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(addScript.getScriptAsString()), 1, keysAndArgs);
                    }

                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        result= (Long) ((Jedis) nativeConnection).eval(redisTemplate.getStringSerializer().serialize(addScript.getScriptAsString()), 1, keysAndArgs);
                    }
                    return result==null?false:result.longValue()==1;
                }
            })).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static Object lGetIndex(String key,long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Long del(String key,Long count,String value) {
        try {
            return redisTemplate.opsForList().remove(key,count,value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public static boolean lSet(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     *
     * @param key
     * @return
     */
    public static Long incr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        return increment;
    }

}
