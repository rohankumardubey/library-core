/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.util;

import cz.muni.fi.pv243.library.model.Book;
import cz.muni.fi.pv243.library.model.LibraryUser;
import cz.muni.fi.pv243.library.model.LibraryUser.UserRole;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@Singleton
@Startup
public class Initializer {

    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void initialize() {
        //initialize users
        LibraryUser admin = newLibraryUser("admin", "password", "admin admin", UserRole.ADMIN);
        em.persist(admin);
        
        LibraryUser librarian = newLibraryUser("librarian", "password", "librarian librarian", UserRole.LIBRARIAN);
        em.persist(librarian);

        LibraryUser reader1 = newLibraryUser("reader1", "password", "reader One", UserRole.READER);
        em.persist(reader1);

        LibraryUser reader2 = newLibraryUser("reader2", "password", "reader Two", UserRole.READER);
        em.persist(reader2);
        
        Book book1 = newBook("title1", "author one", 2000, "isbn");
        em.persist(book1);
        
        Book book2 = newBook("title2", "author two", 2001, "isbn");
        em.persist(book2);
        
        Book book3 = newBook("title3", "author three", 2000, "isbn");
        em.persist(book3);
    }

    private LibraryUser newLibraryUser(String username, String password, String name, UserRole userRole) {
        LibraryUser result = new LibraryUser();
        result.setUsername(username);
        result.setPassword(password);
        result.setName(name);
        result.setUserRole(userRole);
        return result;
    }

    private Book newBook(String title, String author, int year, String isbn) {
        Book result = new Book();
        result.setTitle(title);
        result.setAuthor(author);
        result.setYear(year);
        result.setIsbn(isbn);
        return result;
    }
}
