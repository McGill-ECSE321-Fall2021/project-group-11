package ca.mcgill.ecse321.townlibrary.service;

import java.util.Arrays;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class PasswordValidatorTest {

    @Autowired
    private PasswordValidator passwordValidator;

    @Test
    public void testValidatePasswordCriteriaNull() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertFalse(this.passwordValidator.validatePasswordCriteria(null, errs::add));
        Assertions.assertEquals(Arrays.asList("EMPTY-PASSWORD"), errs);
    }

    @Test
    public void testValidatePasswordCriteriaTooShort() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertFalse(this.passwordValidator.validatePasswordCriteria("", errs::add));
        Assertions.assertEquals(Arrays.asList("UNDERSIZED-PASSWORD"), errs);
    }

    @Test
    public void testValidatePasswordCriteriaTooLong() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertFalse(this.passwordValidator.validatePasswordCriteria(
                "pppppppp" +
                "pppppppp" +
                "pppppppp" +
                "pppppppp" +
                "pppppppp", errs::add));
        Assertions.assertEquals(Arrays.asList("OVERSIZED-PASSWORD"), errs);
    }

    @Test
    public void testValidatePasswordCriteriaIllegalChars() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertFalse(this.passwordValidator.validatePasswordCriteria("abcd    p", errs::add));
        Assertions.assertEquals(Arrays.asList("BADCHAR-PASSWORD"), errs);
    }

    @Test
    public void testValidatePasswordCriteriaLegal() {
        final ArrayList<String> errs = new ArrayList<>();
        Assertions.assertTrue(this.passwordValidator.validatePasswordCriteria("abcd1234", errs::add));
        Assertions.assertTrue(errs.isEmpty());
    }
}
