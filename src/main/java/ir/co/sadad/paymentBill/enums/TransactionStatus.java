package ir.co.sadad.paymentBill.enums;

public enum TransactionStatus {
    PROCESSING("PROCESSING"),
    FAILED("FAILED"), SUCCEED("SUCCEED"),
    INCONCLUSIVE("INCONCLUSIVE");

    private final String text;

    /**
     * @param text
     */
    TransactionStatus(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static TransactionStatus getEnum(String text) {
        switch (text) {
            case "PROCESSING":
                return PROCESSING;
            case "FAILED":
                return FAILED;
            case "INCONCLUSIVE":
                return INCONCLUSIVE;
            default:
                return null;
        }
    }
}
