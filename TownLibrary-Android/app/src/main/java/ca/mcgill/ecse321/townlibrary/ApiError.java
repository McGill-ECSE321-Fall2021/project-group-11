package ca.mcgill.ecse321.townlibrary;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ApiError {

    private ApiError() {
    }

    /**
     * Either the first element of the collection or a default value
     *
     * @param col a potentially empty collection
     * @param <T> the default value
     * @return the first element of the collection or the default value
     */
    public static <T> T firstOr(Collection<T> col, T emptyVal) {
        for (final T val : col)
            // only enters the loop if collection is not empty
            return val;

        return emptyVal;
    }

    /**
     * Decodes a server response indicating an error into some sensible
     * message for lay-humans to read!
     *
     * (This method is directly translated from the vuejs frontend...)
     *
     * @param serverResponse the response string from the server
     * @return a set of errors
     */
    public static Set<String> decodeError(String serverResponse) {
        // Sometimes the server returns a list of error messages separated by
        // ',', '.', or '!'.
        final Set<String> errs = new HashSet<>();
        for (String msg : serverResponse.split("[,!.]")) {
            msg = msg.trim();
            if (msg.isEmpty())
                continue;

            switch (msg) {
                // list of more special/sever errors:
                // if we find these, just report them and ignore everything else
                case "NULL-LIBRARY":
                    return Collections.singleton("Invalid library, has setup been performed yet?");
                case "DUP-LIBRARY":
                    return Collections.singleton("Duplicate library, try reloading the page");
                case "DUP-HEAD-LIBRARIAN":
                    return Collections.singleton("Duplicate head-librarian, try reloading the page");

                // other errors
                default:
                    errs.add(msg);
                    break;
                case "BAD-AUTH-ONLINE-MEMBER":
                    errs.add("Incorrect username or password");
                    break;
                case "BAD-AUTH-LIBRARIAN":
                    errs.add("Incorrect id or password");
                    break;
                case "EMPTY-NAME":
                    errs.add("Name cannot be empty");
                    break;
                case "EMPTY-ADDRESS":
                    errs.add("Address cannot be empty");
                    break;
                case "EMPTY-PASSWORD":
                    errs.add("Password cannot be empty");
                    break;
                case "DUP-USERNAME":
                    errs.add("Username is already taken");
                    break;
                case "DUP-EMAIL":
                    errs.add("Email is already used");
                    break;
                case "EMPTY-EMAIL":
                    errs.add("Email cannot be empty");
                    break;
                case "BADFMT-EMAIL":
                    errs.add("Invalid Email");
                    break;
                case "UNDERSIZED-PASSWORD":
                case "OVERSIZED-PASSWORD":
                    errs.add("Password must be 4 to 32 characters long");
                    break;
                case "BADCHAR-PASSWORD":
                    errs.add("Password can only contain alphanumeric characters");
                    break;
            }
        }
        return errs;
    }
}
