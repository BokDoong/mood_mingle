package uni.capstone.moodmingle.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Client 로부터 받는 Enum 을 검증하는 Validator
 *
 * @author ijin
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
    /**
     * 검증할 커스텀 어노테이션
     */
    private ValidEnum annotation;

    /**
     * 해당 Validator 가 검증할 커스텀 어노테이션 지정
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    /**
     * Controller 로부터 받은 Enum Data 를 검증
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return 검증 결과(Boolean)
     */
    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value == enumValue) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
