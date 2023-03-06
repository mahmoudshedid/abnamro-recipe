package com.abnamro.recipe.api.validations.validators;

import com.abnamro.recipe.api.validations.annotations.ValidEnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, Object> {

    private ValidEnumValue params;

    private Set<String> availableEnumNames;

    @Override
    public void initialize(ValidEnumValue stringEnumeration) {
        this.params = stringEnumeration;

        Class<? extends Enum<?>> enumSelected = stringEnumeration.enumClass();
        Set<? extends Enum<?>> enumInstances = Set.of(enumSelected.getEnumConstants());
        availableEnumNames = enumInstances.stream().map(Enum::name).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (this.params.required() && value == null) {
            return false;
        } else if (!this.params.required() && value == null) {
            return true;
        } else {
            if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                if (this.params.required() && collection.isEmpty()) {
                    return false;
                }
                return collection.stream().allMatch(this::validateSingle);
            } else {
                return validateSingle(value);
            }
        }
    }

    private boolean validateSingle(Object value) {
        return availableEnumNames.stream().anyMatch(s -> s.equals(value));
    }
}
