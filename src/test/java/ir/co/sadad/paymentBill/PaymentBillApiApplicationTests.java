package ir.co.sadad.paymentBill;

//import ir.co.sadad.paymentBill.validations.InvoiceValid;
//import ir.co.sadad.paymentBill.validations.InvoiceValidator;
import ir.co.sadad.paymentBill.validations.InvoiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@SpringBootTest
@ActiveProfiles(profiles = {"qa"})
public class PaymentBillApiApplicationTests {

	protected UserVO userVO;
//	protected InvoiceValidator validator;

	protected String authToken = "accessToken";
	private String userId = "158";
	private String ssn = "0079993141";
	private String cellphone = "9124150188";
	private String deviceId = "5700cd58-3cd6-4ce3-81ff-ee519e1f6df7";

//	@Autowired
	protected InvoiceValidator validator;

	@BeforeEach
	public void setUp() {

		userVO = UserVO.of(userId, cellphone, deviceId, ssn);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = (InvoiceValidator) factory.getValidator();

	}

}
