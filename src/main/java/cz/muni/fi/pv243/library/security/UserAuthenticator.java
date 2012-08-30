/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.security;

import cz.muni.fi.pv243.library.model.LibraryUser;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.picketlink.idm.impl.api.PasswordCredential;

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
        try {
            final LibraryUser user = (LibraryUser) em.createQuery("select l from LibraryUser l where l.username = :username and l.password = :password")
            .setParameter("username", credentials.getUsername())
            .setParameter("password", ((PasswordCredential)credentials.getCredential()).getValue()).getSingleResult();
            
            setStatus(AuthenticationStatus.SUCCESS);
            setUser(user);
        } catch (NoResultException ex) {
            setStatus(AuthenticationStatus.FAILURE);
        }
    }

}
