/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.security;

import cz.muni.fi.pv243.library.model.LibraryUser;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class UserAuthenticator extends BaseAuthenticator {

    @Inject
    Credentials credentials;
    
    @Inject
    EntityManager em;
    
    @Override
    public void authenticate() {
        //TODO
//        LibraryUser user = em.createQuery("select m from Manager m where m.username = :username and m.password = :password")
//         .setParameter("username", credentials.getUsername())
//         .setParameter("password", ((PasswordCredential)credentials.getCredential()).getValue()).getSingleResult();
    }

}
