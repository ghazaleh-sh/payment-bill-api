package ir.co.sadad.paymentBill.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* ir.co.sadad.paymentBill.services.*.*(..))") //point cut
    public void beforeAllServiceMethods(JoinPoint joinPoint) {
        log.info("********** started executing: " + joinPoint.getSignature().getName() + " with method param: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* ir.co.sadad.paymentBill.services..*(..))", returning = "result")
    public void afterAllServiceMethods(JoinPoint joinPoint, Object result) {
        log.info("********** completed executing: " + joinPoint.getSignature().getName() + " with return value: " + result);
    }
}
