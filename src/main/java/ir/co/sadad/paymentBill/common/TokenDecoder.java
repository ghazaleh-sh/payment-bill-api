package ir.co.sadad.paymentBill.common;

//import javax.json.Json;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import javax.json.stream.JsonParsingException;
import java.io.StringReader;
import java.util.Base64;

public class TokenDecoder {

//    public String getSpecificField(String authorization, String fieldName) {
//        String token = authorization.split("\\.")[1];
//        Base64.Decoder decoder = Base64.getUrlDecoder();
//
//        token = new String(decoder.decode(token));
//
//        String demandedField;
//        try {
//            JsonReader jsonReader = Json.createReader(new StringReader(token));
//            JsonObject jsonObject = jsonReader.readObject();
//            jsonReader.close();
//            demandedField = jsonObject.get(fieldName).toString();
//
//        } catch (JsonParsingException x) {
//            throw new IllegalArgumentException();
//        }
//        return demandedField;
//    }
}