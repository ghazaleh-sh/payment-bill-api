package ir.co.sadad.paymentBill.common;

import ir.co.sadad.paymentBill.exceptions.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Base64;

@Component
public class Encoder {

    @Value(value = "${tripleDesKey}")
    private String tripleDesKey;

    public String prepareSignDataWithToken(String token) {
        try {
            byte[] tripleDesEncryptedValue = TripleDES.encrypt(token, tripleDesKey);
//            byte[] base64EncodedValue = Base64.getEncoder().encode(tripleDesEncryptedValue);
            return new String(tripleDesEncryptedValue);
        } catch (GeneralSecurityException e) {
            // todo, log exceptions
            e.printStackTrace();
            throw new InternalServerException("internal server error");
        } catch (UnsupportedEncodingException e) {
            // todo, log exceptions
            e.printStackTrace();
            throw new InternalServerException("internal server error");
        }
    }

    public String prepareSignDataForRegistration(String terminalId, String orderId, String amount) {

        StringBuffer compoundString = new StringBuffer(terminalId).append(";").append(orderId).append(";").append(amount);
        try {
            return new String(TripleDES.encrypt(compoundString.toString(), tripleDesKey));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String toBase64(String unencryptedValue) {
        return new String(Base64.getEncoder().encode(unencryptedValue.getBytes()));
    }

}
