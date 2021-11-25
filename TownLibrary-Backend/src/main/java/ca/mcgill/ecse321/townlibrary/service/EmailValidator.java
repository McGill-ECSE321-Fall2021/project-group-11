package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;


import java.util.function.Consumer;

@Service
public class EmailValidator {

    /**
     * Checks to see if an email is considered "valid".
     *
     * Currently it's done through regex (which might not be the best
     * solution, but it's probably good enough for our purposes...)
     *
     * @param email The email being validated
     * @param errcb Supplied with messages stating why it's not valid.
     *
     * @return whether the email is considered valid
     */
    public boolean validateEmailCriteria(String email, Consumer<? super String> errcb) {
        // Again, consider turning it into a service for autowire benefits...
        if (email == null) {
            errcb.accept("EMPTY-EMAIL");
            return false;
        }

        if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            errcb.accept("BADFMT-EMAIL");
            return false;
        }

        return true;
    }
}
