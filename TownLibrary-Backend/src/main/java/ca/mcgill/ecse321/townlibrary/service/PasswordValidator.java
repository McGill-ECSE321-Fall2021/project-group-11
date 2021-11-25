package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;


import java.util.function.Consumer;

@Service
public class PasswordValidator {

    /**
     * Checks to see if a password is considered "valid", which means fitting
     * all the following critera:
     *
     *   * Not null (obviously!?)
     *   * within 4 to 32 characters in length
     *   * consists of only alphanumeric characters
     *
     * @param password  The password being validated
     * @param errcb     Supplied with messages stating why it's not valid.
     *
     * @return whether the password is considered valid
     */
    public boolean validatePasswordCriteria(String password, Consumer<? super String> errcb) {
        // If it turns out lots of components need this, we could promote this
        // into a service and let Spring autowire it!

        if (password == null) {
            errcb.accept("EMPTY-PASSWORD");
            return false;
        }

        boolean valid = true;
        if (password.length() < 4) {
            errcb.accept("UNDERSIZED-PASSWORD");
            valid = false;
        }
        if (password.length() > 32) {
            errcb.accept("OVERSIZED-PASSWORD");
            valid = false;
        }
        if (!password.matches("[a-zA-Z0-9]*")) {
            errcb.accept("BADCHAR-PASSWORD");
            valid = false;
        }

        return valid;
    }
}
