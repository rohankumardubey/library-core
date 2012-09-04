/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.service;

import cz.muni.fi.pv243.library.model.Book;
import cz.muni.fi.pv243.library.model.LibraryUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@ApplicationScoped
@Named("LibraryManager")
public class LibraryManagerImpl implements LibraryManager {
    
    @Inject
    private EntityManager em;
    
    @Inject
    private FacesContext facesContext;
    
    private Map<Long, Boolean> checkedToLoan = new HashMap<Long, Boolean>();
    private String usernameOfReader;
    private String searchFieldValue;
    private String attributeType;
    private LibraryUser reader;
    private Book book;
    
    @Inject
    private BookManager bookManager;
    
    @Inject
    private LibraryUserManager userManager;

    public Map<Long, Boolean> getCheckedToLoan() {
        return checkedToLoan;
    }

    public void setCheckedToLoan(Map<Long, Boolean> checkedToLoan) {
        this.checkedToLoan = checkedToLoan;
    }

    public String getUsernameOfReader() {
        return usernameOfReader;
    }

    public void setUsernameOfReader(String usernameOfReader) {
        this.usernameOfReader = usernameOfReader;
    }

    public String getSearchFieldValue() {
        return searchFieldValue;
    }

    public void setSearchFieldValue(String searchFieldValue) {
        this.searchFieldValue = searchFieldValue;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public LibraryUser getReader() {
        return reader;
    }

    public void setReader(LibraryUser reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void selectReader() {
        reader = userManager.getLibraryUserByUsername(usernameOfReader);
        if (reader != null) {
            for (Book b : bookManager.getAllBooks()) {
                if (reader.equals(b.getUser())) {
                    checkedToLoan.put(b.getId(), true);
                } else {
                    checkedToLoan.put(b.getId(), false);
                }
            }
        }
    }

    public void unselectReader() {
        usernameOfReader = "";
        reader = null;
        checkedToLoan.clear();
    }
    
    @Produces
    @Named("books")
    @Override
    public List<Book> findBooksBy() {
        if (searchFieldValue == null || searchFieldValue.isEmpty()) {
            return bookManager.getAllBooks();
        }
        BookAttributeType bookAttributeType = BookAttributeType.valueOf(attributeType);
        return em.createQuery("SELECT b FROM Book b WHERE b." + bookAttributeType.toString().toLowerCase() + " LIKE :searchValue")
                                  .setParameter("searchValue", "%" + searchFieldValue + "%").getResultList();
    }
    
    @Override
    public void loanBooks() {
        for (Long bookId : checkedToLoan.keySet()) {
            if (checkedToLoan.get(bookId)) {
                Book book = bookManager.getBookById(bookId);
                book.setUser(reader);
                em.merge(book);
            }
        }
    }

    @Override
    public void returnBook(Book book) {
        book.setUser(null);
        em.merge(book);
        checkedToLoan.put(book.getId(), false);
    }
    
    public void loadBook(String bookId) {
        if (bookId != null) {
            book = bookManager.getBookById(Long.parseLong(bookId));
        } else {
            book = new Book();
        }
    }
    
    public void updateBook() {
//        checkBookParameters();
        bookManager.updateBook(book);
        book = null;
    }
    
    public void addBook() {
//        checkBookParameters();
        bookManager.addBook(book);
        book = null;
    }
    
    private void checkBookParameters() {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            facesContext.addMessage("addEditForm:title", new FacesMessage("Title cannot be empty."));
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            facesContext.addMessage("addEditForm:author", new FacesMessage("Author cannot be empty."));
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            facesContext.addMessage("addEditForm:isbn", new FacesMessage("Isbn cannot be empty."));
        }
    }
}