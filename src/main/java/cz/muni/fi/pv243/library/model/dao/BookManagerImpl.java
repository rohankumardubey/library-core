/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.model.dao;

import cz.muni.fi.pv243.library.model.Book;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
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
@Named("BookManager")
public class BookManagerImpl implements BookManager {

    @Inject
    private EntityManager em;
    
    @Override
    public void addBook(Book book) {
        checkBookParameters(book);
        if (book.getId() != null) {
            throw new IllegalArgumentException("Book can't have set id.");
        }
        em.persist(book);
    }

    @Override
    public void removeBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book is null.");
        }
        if (book.getId() == null) {
            throw new IllegalArgumentException("ID is null.");
        }
        Book bookInDB = getBookById(book.getId());
        if (!book.equals(bookInDB)) {
            throw new IllegalArgumentException("Attributes of book don't equal "
                    + "to the attibutes of book in DB.");
        }
        em.remove(book);
    }

    @Override
    public void updateBook(Book book) {
        checkBookParameters(book);
        if (book.getId() == null) {
            throw new IllegalArgumentException("ID is null.");
        }
        em.merge(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return em.createQuery("select b from Book b ORDER BY b.title ASC").getResultList();
    }

    @Override
    public Book getBookById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null.");
        }
        Book result = null;
        try {
            result = (Book) em.createQuery("SELECT b FROM Book b WHERE b.id=:id")
				.setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            //ok
        }
        return result;
    }

    private void checkBookParameters(Book book) throws IllegalArgumentException {
        if (book == null) {
            throw new IllegalArgumentException("Book is null.");
        }
        if (book.getTitle() == null) {
            throw new IllegalArgumentException("Title is null.");
        }
        if (book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is empty.");
        }
        if (book.getTitle().length() > 150) {
            throw new IllegalArgumentException("Title has more than 150 characters.");
        }
        if (book.getAuthor() == null) {
            throw new IllegalArgumentException("Author is null.");
        }
        if (book.getAuthor().isEmpty()) {
            throw new IllegalArgumentException("Author is empty.");
        }
        if (book.getAuthor().length() < 3) {
            throw new IllegalArgumentException("Author has less than 3 characters.");
        }
        if (book.getAuthor().length() > 75) {
            throw new IllegalArgumentException("Author has more than 75 characters.");
        }
        if (!Pattern.matches("[A-Ža-ž ]*", book.getAuthor())) {
            throw new IllegalArgumentException("Author contains something else than characters and spaces.");
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, book.getYear());
        if (cal.after(GregorianCalendar.getInstance())) {
            throw new IllegalArgumentException("Year is after this year.");
        }
        if (book.getIsbn() == null) {
            throw new IllegalArgumentException("ISBN is null.");
        }
        if (book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("ISBN is empty.");
        }
//        checkIsbnFormat(book.getIsbn());
    }
    
    private void checkIsbnFormat(String isbn) throws IllegalArgumentException {
        if (isbn.startsWith("-") || isbn.endsWith("-") || isbn.contains("--")) {
            throw new IllegalArgumentException("ISBN format is not valid.");
        }
        isbn = isbn.replace("-", "");
        if (!Pattern.matches("\\d{13}", isbn) && !Pattern.matches("\\d{10}", isbn)) {
            throw new IllegalArgumentException("ISBN format is not valid.");
        }
    }
}
