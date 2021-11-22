package ir.co.sadad.paymentBill.services;

import ir.co.sadad.paymentBill.dtos.payment.PaymentRegistration;
import ir.co.sadad.paymentBill.dtos.payment.PaymentVerificationResponse;
import ir.co.sadad.paymentBill.exceptions.TokenGenerationException;

public interface AdviceApi {

    PaymentVerificationResponse verifyPaymentTransaction(String token, String orderId);

    String getPaymentToken(PaymentRegistration paymentRegistration) throws TokenGenerationException;
}
