package io.github.microsphere.spring.redis.beans;

import io.github.microsphere.spring.redis.config.RedisConfiguration;
import io.github.microsphere.spring.redis.metadata.ParametersHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;

import static io.github.microsphere.spring.redis.config.RedisConfiguration.STRING_REDIS_TEMPLATE_SOURCE;

/**
 * {@link StringRedisTemplate} Wrapper 类
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class StringRedisTemplateWrapper extends StringRedisTemplate implements BeanNameAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext context;

    private final RedisConfiguration redisConfiguration;

    private String beanName;

    public StringRedisTemplateWrapper(StringRedisTemplate stringRedisTemplate, ApplicationContext context) {
        this.context = context;
        this.redisConfiguration = RedisConfiguration.get(context);
        initSettings(stringRedisTemplate);
    }

    private void initSettings(StringRedisTemplate stringRedisTemplate) {
        // 设置连接
        setConnectionFactory(stringRedisTemplate.getConnectionFactory());
        setExposeConnection(stringRedisTemplate.isExposeConnection());

        // 设置 RedisSerializers
        setEnableDefaultSerializer(stringRedisTemplate.isEnableDefaultSerializer());
        setDefaultSerializer(stringRedisTemplate.getDefaultSerializer());
        setKeySerializer(stringRedisTemplate.getKeySerializer());
        setValueSerializer(stringRedisTemplate.getValueSerializer());
        setHashKeySerializer(stringRedisTemplate.getHashKeySerializer());
        setHashValueSerializer(stringRedisTemplate.getHashValueSerializer());
        setStringSerializer(stringRedisTemplate.getStringSerializer());
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        if (isEnabled()) {
            return RedisTemplateWrapper.newProxyRedisConnection(connection, context, redisConfiguration, beanName, STRING_REDIS_TEMPLATE_SOURCE);
        }
        return connection;
    }

    @Nullable
    @Override
    protected <T> T postProcessResult(@Nullable T result, RedisConnection conn, boolean existingConnection) {
        if (isEnabled()) {
            // 清除方法参数数据
            ParametersHolder.clear();
            logger.debug("方法参数元数据已清除");
        }
        return result;
    }

    public boolean isEnabled() {
        return redisConfiguration.isEnabled();
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
