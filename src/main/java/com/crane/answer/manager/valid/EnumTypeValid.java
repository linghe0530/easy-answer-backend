package com.crane.answer.manager.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author crane
 * @date 2025.09.12 下午1:57
 * @description
 **/
@Documented
@Constraint(
        validatedBy = {EnumTypeValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumTypeValid {

    int[] values() default {}; // 允许的取值范围

    String message() default "无效枚举类型";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}

