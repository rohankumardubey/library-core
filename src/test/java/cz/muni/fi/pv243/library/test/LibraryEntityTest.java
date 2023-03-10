/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.test;

import cz.muni.fi.pv243.library.model.Book;
import cz.muni.fi.pv243.library.service.BookManager;
import cz.muni.fi.pv243.library.service.BookManagerImpl;
import cz.muni.fi.pv243.library.service.EntityManagerProducer;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picketlink.idm.api.User;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@RunWith(Arquillian.class)
public class LibraryEntityTest {

    @Inject
    private BookManager bookManager;
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    UserTransaction utx;

    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, User.class.getPackage(), Book.class.getPackage())
                .addClasses(BookManager.class, BookManagerImpl.class, EntityManagerProducer.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        startTransaction();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        em.createQuery("delete from Book").executeUpdate();
        em.createQuery("delete from LibraryUser").executeUpdate();
        utx.commit();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        try {
            utx.commit();
        } catch (RollbackException ex) {
            //ok
        }
    }

    @Test
    public void testAddBook() {
        Book book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        bookManager.addBook(book);

        Long bookId = book.getId();
        assertNotNull(bookId);
        Book result = bookManager.getBookById(bookId);
        assertEquals(book, result);
        assertTrue(book.equals(result));
    }

    @Test
    public void testGetBookById() {
        assertNull(bookManager.getBookById((long) 23));

        Book book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        bookManager.addBook(book);
        Long bookId = book.getId();

        Book result = bookManager.getBookById(bookId);
        assertEquals(book, result);
        assertTrue(book.equals(result));
    }

    @Test
    public void testGetAllBooks() {
        assertTrue(bookManager.getAllBooks().isEmpty());

        Book b1 = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        Book b2 = newBook("Patrik op??t zasahuje", "Nick Meguster",
                1998, "60-205-0107-8");
        bookManager.addBook(b1);
        bookManager.addBook(b2);

        Set<Book> expected = new HashSet();
        expected.add(b1);
        expected.add(b2);
        Set<Book> actual = new HashSet(bookManager.getAllBooks());

        assertEquals(expected, actual);

    }
    
    @Test
    public void testUpdateBook() {
        Book book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "60-205-0107-8");
        Book book2 = newBook("Patrik op??t zasahuje", "Nick Meguster",
                1998, "957-70-4708-465-9");
        bookManager.addBook(book);
        bookManager.addBook(book2);
        Long bookId = book.getId();

        book = bookManager.getBookById(bookId);
        book.setTitle("Abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd "); // length = 150
        bookManager.updateBook(book);
        assertEquals("Abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd ", book.getTitle());
        assertEquals("Sergej Vasilijevi??", book.getAuthor());
        assertEquals(2003, book.getYear());
        assertEquals("60-205-0107-8", book.getIsbn());
        assertEquals(false, book.isLoan());

        book = bookManager.getBookById(bookId);
        book.setAuthor("Efgh efgh efgh efgh efgh efgh efgh efgh efgh efgh "
                + "efgh efgh efgh efgh efgh "); // length = 75
        bookManager.updateBook(book);
        assertEquals("Abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd ", book.getTitle());
        assertEquals("Efgh efgh efgh efgh efgh efgh efgh efgh efgh efgh "
                + "efgh efgh efgh efgh efgh ", book.getAuthor());
        assertEquals(2003, book.getYear());
        assertEquals("60-205-0107-8", book.getIsbn());
        assertEquals(false, book.isLoan());

        book = bookManager.getBookById(bookId);
        book.setYear(-28);
        bookManager.updateBook(book);
        assertEquals("Abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd ", book.getTitle());
        assertEquals("Efgh efgh efgh efgh efgh efgh efgh efgh efgh efgh "
                + "efgh efgh efgh efgh efgh ", book.getAuthor());
        assertEquals(-28, book.getYear());
        assertEquals("60-205-0107-8", book.getIsbn());
        assertEquals(false, book.isLoan());

        book = bookManager.getBookById(bookId);
        book.setIsbn("80-205-0107-7");
        bookManager.updateBook(book);
        assertEquals("Abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                + "abcd abcd abcd abcd abcd abcd abcd abcd abcd ", book.getTitle());
        assertEquals("Efgh efgh efgh efgh efgh efgh efgh efgh efgh efgh "
                + "efgh efgh efgh efgh efgh ", book.getAuthor());
        assertEquals(-28, book.getYear());
        assertEquals("80-205-0107-7", book.getIsbn());
        assertEquals(false, book.isLoan());

        
        // Check if updates didn't affected other records
        assertTrue(book2.equals(bookManager.getBookById(book2.getId())));
    }
    
    @Test
    public void testRemoveBook() {
        Book b1 = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        Book b2 = newBook("Patrik op??t zasahuje", "Nick Meguster",
                1998, "60-205-0107-8");
        bookManager.addBook(b1);
        bookManager.addBook(b2);

        assertNotNull(bookManager.getBookById(b1.getId()));
        assertNotNull(bookManager.getBookById(b2.getId()));

        bookManager.removeBook(b1);

        assertNull(bookManager.getBookById(b1.getId()));
        assertNotNull(bookManager.getBookById(b2.getId()));
    }
    
    @Test
    public void testAddBookWithWrongAttributes() throws SystemException {
        try {
            bookManager.addBook(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        Book book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        book.setId((long) 23);
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook(null, "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("", "Sergej Vasilijevi??",
                2003, "957-70-4708-465-9");
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", null,
                2003, "957-70-4708-465-9");
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", "",
                2003, "957-70-4708-465-9");
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, null);
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "");
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "ISBN 957-70-4708-465-9");
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "01234567890123"); // length = 14
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "012345678901"); // length = 12
        try {
            bookManager.addBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }
    }

    @Test
    public void testUpdateBookWithWrongAttributes() {
        Book book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "60-205-0107-8");
        bookManager.addBook(book);
        Long bookId = book.getId();

        try {
            bookManager.updateBook(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setId(null);
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setId(bookId - 1);
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setTitle(null);
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setTitle("");
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setTitle("Abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                    + "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd "
                    + "abcd abcd abcd abcd abcd abcd abcd abcd abcd ."); // length = 151
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setAuthor(null);
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setAuthor("");
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setAuthor("Efgh efgh efgh efgh efgh efgh efgh efgh efgh efgh "
                    + "efgh efgh efgh efgh efgh ."); // length = 76
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setIsbn(null);
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setIsbn("");
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setIsbn("ISBN 957-70-4708-465-9");
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setIsbn("ISBN 0");
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setIsbn("01234567890123"); // length = 14
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book = bookManager.getBookById(bookId);
            book.setIsbn("012345678901"); // length = 12
            bookManager.updateBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }
    }

    @Test
    public void testRemoveBookWithWrongAttributes() {
        Book book = newBook("Spongebob v r??ji med??z", "Sergej Vasilijevi??",
                2003, "60-205-0107-8");

        try {
            bookManager.removeBook(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book.setId(null);
            bookManager.removeBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

        try {
            book.setId(1l);
            bookManager.removeBook(book);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        } catch (EJBTransactionRolledbackException ex) {
            //OK
        }

    }

    private Book newBook(String title, String author, int year, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        book.setIsbn(isbn);
        return book;
    }
}
