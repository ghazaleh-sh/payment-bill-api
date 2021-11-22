package ir.co.sadad.paymentBill;

import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserVO {
    private String userId;
    private String userMobileNo;
    private String serialId;
    private String ssn;

    private UserVO(String userId,
                   String userMobileNo,
                   String serialId,
                   String ssn) {

        if (userId == null) {
            throw new BillPaymentException("to.user.id.must.not.be.null", HttpStatus.BAD_REQUEST);
        }
        this.userId = userId;
        this.userMobileNo = userMobileNo;
        this.serialId = serialId;
        this.ssn = ssn;
    }

    public static UserVO of(String userId) {
        return new UserVO(userId,
                null,
                null,
                null);
    }

    public static UserVO of(String userId, String userMobileNo, String serialId) {
        return new UserVO(userId,
                userMobileNo,
                serialId, null);
    }

    public static UserVO of(String userId,
                            String userMobileNo,
                            String serialId,
                            String ssn) {

        return new UserVO(userId,
                userMobileNo,
                serialId,
                ssn);
    }

}

