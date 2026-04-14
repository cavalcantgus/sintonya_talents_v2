package com.example.demo.interfaces;

import com.example.demo.enums.AuditAction;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    AuditAction action() default AuditAction.OTHER;
    String entity() default "";
    boolean captureArgs() default true;
}
