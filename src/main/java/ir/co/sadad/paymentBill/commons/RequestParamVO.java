package ir.co.sadad.paymentBill.commons;

import lombok.Getter;

@Getter
public class RequestParamVO {

    private final String token;
    private final String signData;
    private final String orderId;

    private RequestParamVO(String token, String signData, String orderId) {
        this.token = token;
        this.signData = signData;
        this.orderId = orderId;
    }

    public static RequestParamVO of(String token) {
        return new RequestParamVO(token,
                null,
                null);
    }

    public static RequestParamVO of(String token, String signData) {
        return new RequestParamVO(token,
                signData, null);
    }

    public static RequestParamVO of(String token,
                                    String signData,
                                    String orderId) {

        return new RequestParamVO(token,
                signData,
                orderId);
    }

}
