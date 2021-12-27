package ir.co.sadad.paymentBill.entities;

import ir.co.sadad.paymentBill.enums.Channel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "PAY_REQUEST")
public class PayRequest {

    public PayRequest(){

    }

    public PayRequest(Payee payee, Channel channel, PayRequestSource payRequestSource){
         this.payee= payee;
         this.channel=channel;
         this.payRequestSource=payRequestSource;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PAYEE_ID", referencedColumnName = "ID", nullable = false)
    @NotNull
    private Payee payee;

    @OneToMany(mappedBy = "payRequest")
    private Collection<PayRequestInvoice> payRequestInvoices;

    @Column(name="CHANNEL" , length = 15 ,nullable = false, columnDefinition = "CHAR(15) NOT NULL")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "PAY_REQUEST_SOURCE_ID", referencedColumnName = "ID")
    private PayRequestSource payRequestSource;

}
