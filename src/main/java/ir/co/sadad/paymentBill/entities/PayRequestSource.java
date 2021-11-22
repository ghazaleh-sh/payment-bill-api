package ir.co.sadad.paymentBill.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "PAY_REQUEST_SOURCE")
public class PayRequestSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IDENTIFIER", length = 50, nullable = false)
    @NotNull
    private String identifier;

    @OneToMany(mappedBy = "payRequestSource")
    private Collection<PayRequest> payRequests;

}
