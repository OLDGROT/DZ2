package org.example.util;

import jakarta.validation.*;

import java.util.Set;

public class ValidationUtil {
    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    public static <T> void validateEntity(T Entity){
        Set<ConstraintViolation<T>> violations = validator.validate(Entity);

        if(!violations.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations){
                sb.append(violation.getPropertyPath().toString())
                        .append(" ")
                        .append(violation.getMessage())
                        .append("; \n");
            }
            throw new ValidationException("Ошибка валидации: " + sb.toString());
        }
    }
}
