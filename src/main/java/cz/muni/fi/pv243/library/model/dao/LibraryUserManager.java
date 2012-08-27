/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.model.dao;

import cz.muni.fi.pv243.library.model.LibraryUser;
import java.util.List;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public interface LibraryUserManager {
    public void addLibraryUser(LibraryUser user);
    public void removeLibraryUser(LibraryUser user);
    public void updateLibraryUser(LibraryUser user);
    public List<LibraryUser> getAllLibraryUsers();
    public LibraryUser getLibraryUserById(Long id);
}
