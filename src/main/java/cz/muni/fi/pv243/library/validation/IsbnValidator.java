package cz.muni.fi.pv243.library.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class IsbnValidator implements ConstraintValidator<Isbn, String> {

    @Override
    public void initialize(Isbn constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.startsWith("ISBN ")) {
            value = value.replace("ISBN ", "");
        }
        if (value.startsWith("ISBN: ")) {
            value = value.replace("ISBN: ", "");
        }
        List<String> parts = Arrays.asList(value.split("-"));

        if (parts.size() != 5 || parts.get(0).length() != 3 || parts.get(1).length() != 2 || parts.get(2).length() != 4
                || parts.get(3).length() != 3 || parts.get(4).length() != 1) {
            return false;
        }
        value = value.replaceAll("-", "");
        try {
            return isISBN13Valid(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isISBN13Valid(String isbn) {
        int check = 0;
        for (int i = 0; i < 12; i += 2) {
            check += Integer.valueOf(isbn.substring(i, i + 1));
        }
        for (int i = 1; i < 12; i += 2) {
            check += Integer.valueOf(isbn.substring(i, i + 1)) * 3;
        }
        check += Integer.valueOf(isbn.substring(12));
        return check % 10 == 0;
    }
}
