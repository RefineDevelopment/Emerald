package io.github.zowpy.jedisapi.redis.subscription;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Project is property of Zowpy © 2021
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: JedisAPI
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IncomingMessage {

    String payload();

}
