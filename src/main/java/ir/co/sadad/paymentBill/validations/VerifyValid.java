package ir.co.sadad.paymentBill.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {VerifyValidator.class})
public @interface VerifyValid {

    String message() default
            "the orderId is not valid, and we can't find any invoice with that.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
