package cz.muni.fi.pv243.library.validation;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class PastYearValidator implements ConstraintValidator<PastYear, Integer> {

    @Override
    public void initialize(PastYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Calendar yearOfBook = new GregorianCalendar();
        yearOfBook.set(Calendar.YEAR, value);
        Calendar rightNow = Calendar.getInstance();
        return yearOfBook.before(rightNow);
    }

}
