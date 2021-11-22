package ir.co.sadad.paymentBill.repositories;

import ir.co.sadad.paymentBill.entities.PayRequest;
import ir.co.sadad.paymentBill.entities.Payee;
import ir.co.sadad.paymentBill.enums.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRequestRepository extends JpaRepository<PayRequest, Long> {

    Optional<PayRequest> findByPayeeAndChannel(Payee payee, Channel channel);
}
