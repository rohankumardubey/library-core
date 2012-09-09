/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.service;

import cz.muni.fi.pv243.library.model.LibraryUser;
import java.util.List;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@Stateless
@Named("LibraryUserManager")
public class LibraryUserManagerImpl implements LibraryUserManager {

    @Inject
    EntityManager em;
    
    @Override
    public void addLibraryUser(LibraryUser user) {
        checkUserInputParam(user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("user has already set id");
        }
        em.persist(user);
    }

    @Override
    public void removeLibraryUser(LibraryUser user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("user's id is null");
        }
        LibraryUser userInDB = getLibraryUserById(user.getId());
        if (!user.equals(userInDB)) {
            throw new IllegalArgumentException("user's attributes don't equal to attibutes of user stored in DB.");
        }
        em.remove(user);
    }

    @Override
    public void updateLibraryUser(LibraryUser user) {
        checkUserInputParam(user);
        if (user.getId() == null) {
            throw new IllegalArgumentException("id is null");
        }
        em.merge(user);
    }

    @Override
    public List<LibraryUser> getAllLibraryUsers() {
        return em.createQuery("select l from LibraryUser l ORDER BY l.username ASC").getResultList();
    }

    @Override
    public LibraryUser getLibraryUserById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null.");
        }
        LibraryUser result = null;
        try {
            result = (LibraryUser) em.createQuery("SELECT l FROM LibraryUser l WHERE l.id=:id")
				.setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            //ok
        }
        return result;
    }

    @Override
    public LibraryUser getLibraryUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is null.");
        }
        LibraryUser result = null;
        try {
            result = (LibraryUser) em.createQuery("SELECT l FROM LibraryUser l WHERE l.username=:username")
				.setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            //ok
        }
        return result;
    }

    private void checkUserInputParam(LibraryUser user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if (user.getUsername() == null) {
            throw new IllegalArgumentException("user's username is null");
        }
        if (user.getUsername().length() < 3 && user.getUsername().length() > 20) {
            throw new IllegalArgumentException("user's username has less than 3 or more than 20 charactersor");
        }
        if (!Pattern.matches("[a-z0-9]*", user.getUsername())) {
            throw new IllegalArgumentException("user's username contains something else than lowercase letters and numbers.");
        }
        if (user.getName() == null) {
            throw new IllegalArgumentException("user's name is null");
        }
        if (user.getName().length() < 3 && user.getName().length() > 50) {
            throw new IllegalArgumentException("user's name has less than 3 or more than 50 charactersor");
        }
        if (!Pattern.matches("[A-Ža-ž ]*", user.getName())) {
            throw new IllegalArgumentException("user's name contains something else than characters and spaces.");
        }
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("user's password is null");
        }
        if (user.getPassword().length() < 6 && user.getPassword().length() > 15) {
            throw new IllegalArgumentException("user's name has less than 6 or more than 15 charactersor");
        }
        if (user.getUserRole() == null) {
            throw new IllegalArgumentException("user's role is null");
        }
    }
}
