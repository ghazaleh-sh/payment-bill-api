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
public class Payee implements Serializable {

    private static final long serialVersionUID = -401265587214644854L;

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
