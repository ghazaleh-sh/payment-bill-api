package ir.co.sadad.paymentBill.entities;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "PAYEE")
//@NamedQueries({
//        @NamedQuery(name = Payee.NAMED_QUERY_FIND_BY_PAYEEIDENTIFIER,
//                query = "SELECT p FROM Payee p WHERE p.payeeIdentifier = :payeeIdentifier")
//})
public class Payee implements Serializable {
//    public static final String NAMED_QUERY_FIND_BY_PAYEEIDENTIFIER = "Payee.findByPayeeIdentifier";

    public Payee() {
    }

    public Payee(String payeeIdentifier) {
        this.creationDateTime = new Timestamp(DateTime.now().getMillis());
        this.payeeIdentifier = payeeIdentifier;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATION_DATE_TIME", nullable = false)
    @NotNull
    private Timestamp creationDateTime;

    @Column(name = "PAYEE_IDENTIFIER", length = 50, nullable = false)
    @NotNull
    private String payeeIdentifier;

}
