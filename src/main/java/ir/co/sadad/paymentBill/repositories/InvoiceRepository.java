package ir.co.sadad.paymentBill.repositories;

import ir.co.sadad.paymentBill.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    Optional<Invoice> findByInvoiceNumberAndPaymentNumber(String invoiceNumber, String paymentNumber);

    Optional<Invoice> findByOrderId(Long orderId);
}
