package ir.co.sadad.paymentBill.validations;

import ir.co.sadad.paymentBill.dtos.BillPaymentReqDto;
import ir.co.sadad.paymentBill.enums.ExceptionType;
import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.CodedException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;


public class InvoiceValidator implements ConstraintValidator<InvoiceValid, BillPaymentReqDto> {

    @Override
    @SneakyThrows
    public boolean isValid(BillPaymentReqDto billPaymentReqDto, ConstraintValidatorContext constraintValidatorContext) {

//        if (invoicePaymentReqDto.isEmpty()) {
//            return false;
//        }
        checkInvoiceValidation(billPaymentReqDto.getInvoiceNumber(), billPaymentReqDto.getPaymentNumber(), new BigDecimal(billPaymentReqDto.getAmount()));
        return true;
    }

    public void checkInvoiceValidation(String invoiceNumber, String paymentNumber, BigDecimal amount) {
//        if(invoiceNumber.equals("") || paymentNumber.equals(""))
//            throw new BillPaymentException("method.argument.not.valid" , HttpStatus.BAD_REQUEST);

        String tempInvoiceId = "0000000000000" + invoiceNumber;
        String invoiceIdSubstring = tempInvoiceId.substring(tempInvoiceId.length() - 13);
        String tempPaymentId = "0000000000000" + paymentNumber;
        String paymentIdSubstring = tempPaymentId.substring(tempPaymentId.length() - 13);

        int mod = checkInvoiceId(invoiceIdSubstring);

        if (mod != Integer.parseInt(invoiceNumber.substring(invoiceNumber.length() - 1))) {
            throw new BillPaymentException("error.invoice.number.is.invalid" , HttpStatus.BAD_REQUEST);
        }

        int checkPaymentIdValueLastSecond = checkPaymentIdLastSecond(paymentIdSubstring);

        int checkPaymentIdValueLast = checkPaymentIdLast(invoiceNumber + paymentNumber.substring(0, paymentNumber.length() - 1));
        if (paymentNumber.length() == 1) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E4000002", "EINP40010003");

        } else if (checkPaymentIdValueLastSecond != Integer.parseInt(paymentNumber.substring(paymentNumber.length() - 2, paymentNumber.length() - 1))) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E4000003", "EINP40010004");
        }

        if (checkPaymentIdValueLast != Integer.parseInt(paymentNumber.substring(paymentNumber.length() - 1))) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E4000004", "EINP40010005");
        }

        if (!amount.toString().equals(Integer.parseInt(paymentIdSubstring.substring(0, 8)) + "000")) {
            throw new CodedException(ExceptionType.IllegalArgumentCoddedException, "E4000005", "EINP40010006");
        }
    }


    private int checkPaymentIdLast(String concatString) {
        int sum = 0;
        String substring1 = "0000000000000000000000000" + concatString;

        String substring = substring1.substring(substring1.length() - 25);

        sum = sum + Integer.parseInt(substring.substring(24, 25)) * 2;
        sum = sum + Integer.parseInt(substring.substring(23, 24)) * 3;
        sum = sum + Integer.parseInt(substring.substring(22, 23)) * 4;
        sum = sum + Integer.parseInt(substring.substring(21, 22)) * 5;
        sum = sum + Integer.parseInt(substring.substring(20, 21)) * 6;
        sum = sum + Integer.parseInt(substring.substring(19, 20)) * 7;
        sum = sum + Integer.parseInt(substring.substring(18, 19)) * 2;
        sum = sum + Integer.parseInt(substring.substring(17, 18)) * 3;
        sum = sum + Integer.parseInt(substring.substring(16, 17)) * 4;
        sum = sum + Integer.parseInt(substring.substring(15, 16)) * 5;
        sum = sum + Integer.parseInt(substring.substring(14, 15)) * 6;
        sum = sum + Integer.parseInt(substring.substring(13, 14)) * 7;
        sum = sum + Integer.parseInt(substring.substring(12, 13)) * 2;
        sum = sum + Integer.parseInt(substring.substring(11, 12)) * 3;
        sum = sum + Integer.parseInt(substring.substring(10, 11)) * 4;
        sum = sum + Integer.parseInt(substring.substring(9, 10)) * 5;
        sum = sum + Integer.parseInt(substring.substring(8, 9)) * 6;
        sum = sum + Integer.parseInt(substring.substring(7, 8)) * 7;
        sum = sum + Integer.parseInt(substring.substring(6, 7)) * 2;
        sum = sum + Integer.parseInt(substring.substring(5, 6)) * 3;
        sum = sum + Integer.parseInt(substring.substring(4, 5)) * 4;
        sum = sum + Integer.parseInt(substring.substring(3, 4)) * 5;
        sum = sum + Integer.parseInt(substring.substring(2, 3)) * 6;
        sum = sum + Integer.parseInt(substring.substring(1, 2)) * 7;
        sum = sum + Integer.parseInt(substring.substring(0, 1)) * 2;
        return calculateMod(sum % 11);
    }


    private int checkInvoiceId(String substring) {
        int sum = 0;
        sum = sum + Integer.parseInt(substring.substring(0, 1)) * 7;
        sum = sum + Integer.parseInt(substring.substring(1, 2)) * 6;
        sum = sum + Integer.parseInt(substring.substring(2, 3)) * 5;
        sum = sum + Integer.parseInt(substring.substring(3, 4)) * 4;
        sum = sum + Integer.parseInt(substring.substring(4, 5)) * 3;
        sum = sum + Integer.parseInt(substring.substring(5, 6)) * 2;
        sum = sum + Integer.parseInt(substring.substring(6, 7)) * 7;
        sum = sum + Integer.parseInt(substring.substring(7, 8)) * 6;
        sum = sum + Integer.parseInt(substring.substring(8, 9)) * 5;
        sum = sum + Integer.parseInt(substring.substring(9, 10)) * 4;
        sum = sum + Integer.parseInt(substring.substring(10, 11)) * 3;
        sum = sum + Integer.parseInt(substring.substring(11, 12)) * 2;
        return calculateMod(sum % 11);
    }

    private int checkPaymentIdLastSecond(String paymentIdSubstring) {
        int sum = 0;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(0, 1)) * 6;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(1, 2)) * 5;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(2, 3)) * 4;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(3, 4)) * 3;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(4, 5)) * 2;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(5, 6)) * 7;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(6, 7)) * 6;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(7, 8)) * 5;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(8, 9)) * 4;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(9, 10)) * 3;
        sum = sum + Integer.parseInt(paymentIdSubstring.substring(10, 11)) * 2;
        return calculateMod(sum % 11);
    }

    private int calculateMod(int mod) {
        if ((mod == 0 | mod == 1)) {
            mod = 0;
        } else {
            mod = 11 - mod;
        }
        return mod;
    }

}
