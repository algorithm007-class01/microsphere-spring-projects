package io.github.microsphere.spring.redis.dynamic.connection.web;

import io.github.microsphere.spring.redis.dynamic.connection.DynamicRedisConnectionFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * {@link DynamicRedisConnectionFactory} 状态清除器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
@WebListener
public class DynamicRedisConnectionFactoryCleanerListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // 清除 ThreadLocal
        DynamicRedisConnectionFactory.clearTarget();
    }
}
