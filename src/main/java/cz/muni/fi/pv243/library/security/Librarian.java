/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.security;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import org.jboss.seam.security.annotations.SecurityBindingType;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@SecurityBindingType
@Target(
{TYPE, METHOD, PARAMETER, FIELD})
@Retention(RUNTIME)
public @interface Librarian {
}
