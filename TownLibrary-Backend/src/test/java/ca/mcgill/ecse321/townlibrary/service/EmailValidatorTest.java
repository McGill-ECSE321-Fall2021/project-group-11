package ca.mcgill.ecse321.townlibrary.service;

import java.util.Arrays;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class EmailValidatorTest {

    @Autowired
    private EmailValidator emailValidator;

    @Test
    public void testValidateEmailCriteriaNull() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertFalse(this.emailValidator.validateEmailCriteria(null, errs::add));
        Assertions.assertEquals(Arrays.asList("EMPTY-EMAIL"), errs);
    }

    @Test
    public void testValidateEmailCriteriaBadFormat() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertFalse(this.emailValidator.validateEmailCriteria("foo", errs::add));
        Assertions.assertEquals(Arrays.asList("BADFMT-EMAIL"), errs);
    }

    @Test
    public void testValidateEmailCriteriaLegal() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertTrue(this.emailValidator.validateEmailCriteria("joe.schmoe@kontoso.com", errs::add));
        Assertions.assertTrue(errs.isEmpty());
    }
}