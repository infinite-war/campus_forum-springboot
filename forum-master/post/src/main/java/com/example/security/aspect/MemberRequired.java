package com.example.security.aspect;

import java.lang.annotation.*;

/**
 * 需要正式成员身份
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberRequired {
}
