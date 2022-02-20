package ir.co.sadad.paymentBill.validations;

import ir.co.sadad.paymentBill.dtos.BillPaymentResDto;
import ir.co.sadad.paymentBill.entities.Invoice;
import ir.co.sadad.paymentBill.enums.ExceptionType;
import ir.co.sadad.paymentBill.enums.PaymentStatus;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import ir.co.sadad.paymentBill.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Priority(10)
public class VerifyValidator implements ConstraintValidator<VerifyValid, BillPaymentResDto> {


    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public void initialize(VerifyValid constraintAnnotation) {


    }

    @Override
    public boolean isValid(BillPaymentResDto invoiceVerifyReqDto, ConstraintValidatorContext constraintValidatorContext) {
        if (invoiceVerifyReqDto == null)
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException,"E400086","illegal_argument");
        String orderId = invoiceVerifyReqDto.getOrderId();
        try {
            Long.parseLong(orderId);
        } catch (RuntimeException e) {
            return false;
        }
        if (checkOrderState(orderId))
            return true;
        return false;
    }

    private boolean checkOrderState(String orderId) {
        try {
            Optional<Invoice> singleResult = invoiceRepository.findByOrderId(Long.valueOf(orderId));
            if (singleResult.isPresent() && singleResult.get().getPaymentStatus().equals(PaymentStatus.PAID))
                throw new CodedException(ExceptionType.DuplicateResourceCodedException, "E4090001", "EINP40010001");
            else
                return true;
        } catch (NoResultException e) {
            throw new CodedException(ExceptionType.ResourceNotFoundCodedException, "E404001", "EINP40410001");
        }
    }
}
