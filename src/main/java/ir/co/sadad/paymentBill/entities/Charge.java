package ir.co.sadad.paymentBill.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CHARGE")
//@NamedQueries({
//        @NamedQuery(name = Charge.NAMED_QUERY_FIND_BY_ORDERID,
//                query = "SELECT charge FROM Charge charge WHERE charge.orderId = :orderId")})
public class Charge {

//    public static final String NAMED_QUERY_FIND_BY_ORDERID = "Charge.findByOrderId";

    public Charge() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MOBILE", length = 11, nullable = true)
    @Size(max = 11)
    private String mobile;

    @Column(name = "CREATIONDATETIME", nullable = false)
    @NotNull
    private LocalDateTime creationDateTime;

    @Column(name = "OPERATOR", nullable = false)
    @NotNull
    @Size(max = 11)
    private String operator;

    @Column(name = "AMOUNT", nullable = false)
    @NotNull
    private int amount;

    @Column(name = "PRODUCT", nullable = false)
    @NotNull
    private String product;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "TOPUPPLAN")
    private String topupPlan;

    @Column(name = "PIN")
    private String pin;

    @Column(name = "SIMTYPE")
    private String simType;

    @Column(name = "GPRSPLANID")
    private String gprsPlanId;

    @Column(name = "GPRSPLANDESCRIPTION")
    private String gprsPlanDescription;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ORDERID", nullable = false)
    @NotNull
    private String orderId;

    @Column(name = "OPERATORMETADATA")
    private String operatorMetaData;

    @Column(name = "RETRIVALREFNO")
    private String retrivalRefNo;

    @Column(name = "SYSTEMTRACENO")
    private String systemTraceNo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "RESULTCODE")
    private String resultCode;

    @Column(name = "RESULTMESSAGE")
    private String resultMessage;

}
