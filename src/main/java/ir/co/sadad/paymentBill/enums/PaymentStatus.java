package ir.co.sadad.paymentBill.enums;

public enum PaymentStatus {
    INCONCLUSIVE("inconclusive"),
    PAID("paid"),
    UNPAID("unpaid") ;

    private final String text;

    PaymentStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static PaymentStatus getEnum(String text) {
        text = text.toUpperCase();
        switch (text) {
            case "INCONCLUSIVE":
                return INCONCLUSIVE;
            case "PAID":
                return PAID;
            case "UNPAID":
                return UNPAID;
            default:
                return null;
        }
    }
}
