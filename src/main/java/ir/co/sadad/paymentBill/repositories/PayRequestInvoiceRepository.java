package ir.co.sadad.paymentBill.repositories;

import ir.co.sadad.paymentBill.entities.PayRequestInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRequestInvoiceRepository extends JpaRepository<PayRequestInvoice, Long> {

    Optional<PayRequestInvoice> findByPayRequestIdAndInvoiceId(Long payRequestId, Long invoiceId);
}
