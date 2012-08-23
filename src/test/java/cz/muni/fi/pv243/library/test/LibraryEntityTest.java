/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.test;

import cz.muni.fi.pv243.library.model.Book;
import cz.muni.fi.pv243.library.model.LibraryUser;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class LibraryEntityTest {

    private static EntityManager em = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (em == null) {
            em = (EntityManager) Persistence.createEntityManagerFactory("libraryPU").
                    createEntityManager();
        }
    }
    
    @Test
    public void testLibraryUser() {
        // Start a transaction
        em.getTransaction().begin();
        
        // Create user u1
        LibraryUser u1 = new LibraryUser();
        u1.setName("name");
        u1.setPassword("password1");
        u1.setUserRole(LibraryUser.Role.ADMIN);
        u1.setUsername("username1");
        
        em.persist(u1);
        em.flush();
        
        em.getTransaction().commit();
    }

    @Test
    public void testBook() {
        // Start a transaction
        em.getTransaction().begin();
        
        // Create book b1
        Book b1 = new Book();
        b1.setAuthor("author");
        b1.setIsbn("80-902734-1-6");
        b1.setTitle("title");
        b1.setYear(2000);
        
        em.persist(b1);
        em.flush();
        
        em.getTransaction().commit();
    }
}
