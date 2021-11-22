package ir.co.sadad.paymentBill.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {InvoiceValidator.class})
public @interface InvoiceValid {

    String message() default
            "the invoice parameters are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
