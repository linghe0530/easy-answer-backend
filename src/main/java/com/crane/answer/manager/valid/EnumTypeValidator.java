package com.crane.answer.manager.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

/**
 * @author crane
 * @date 2025.09.12 下午1:59
 * @description
 **/
public class EnumTypeValidator implements ConstraintValidator<EnumTypeValid, Integer> {
    private final Set<Integer> allowedValues = new HashSet<>();

    @Override
    public void initialize(EnumTypeValid appTypeValid) {
        int[] values = appTypeValid.values();
        for (int value : values) {
            allowedValues.add(value);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        // 允许为 null（如果需要非空校验，需额外加 @NotNull）
        if (value == null) {
            return false;
        }
        // 校验值是否在允许范围内
        return allowedValues.contains(value);
    }
}
