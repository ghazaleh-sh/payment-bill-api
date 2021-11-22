package ir.co.sadad.paymentBill.enums;

public enum InvoiceType {

    NONE(0),
    WA(1),
    EL(2),
    GA(3),
    TC(4),
    MC(5),
    MN(6),
    TX(8),
    UD(9);

    private Integer value;

    InvoiceType(Integer value) {
        this.value = value;
    }

//    public Integer getValue() {
//        return value;
//    }

    public static InvoiceType getEnum(Integer val) {
        switch (val) {
            case 1:
                return WA;
            case 2:
                return EL;
            case 3:
                return GA;
            case 4:
                return TC;
            case 5:
                return MC;
            case 6:
                return MN;
            case 8:
                return TX;
            case 9:
                return UD;
        }

        return NONE;
    }

}
