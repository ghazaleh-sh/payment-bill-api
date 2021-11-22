package ir.co.sadad.paymentBill.repositories;

import ir.co.sadad.paymentBill.entities.Payee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayeeRepository extends JpaRepository<Payee, Long> {

    Optional<Payee> findByPayeeIdentifier(String payeeIdentifier);
}
