/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.service;

import cz.muni.fi.pv243.library.model.Book;
import cz.muni.fi.pv243.library.model.LibraryUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@ApplicationScoped
@Named("LibraryManager")
public class LibraryManager {
    
    public enum BookAttributeType {
        TITLE, AUTHOR, ISBN;
    }

    private List<Book> reservedBooks = new ArrayList<Book>();
    private Map<Long, Boolean> checked = new HashMap<Long, Boolean>();
    private String usernameOfReader;
    private String searchFieldValue;
    private String attributeType;
    private LibraryUser reader;
    
    
    @Inject
    private BookManager bookManager;
    
    @Inject
    private LibraryUserManager userManager;
    
    public Map<Long, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<Long, Boolean> checked) {
        this.checked = checked;
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

    public void selectReader() {
        reader = userManager.getLibraryUserByUsername(usernameOfReader);
    }
    
    @Produces
    @Named("books")
    public List<Book> findBooksBy() {
        if (searchFieldValue == null || searchFieldValue.isEmpty()) {
            return bookManager.getAllBooks();
        }
        BookAttributeType bookAttributeType = BookAttributeType.valueOf(attributeType);
        //TODO
        return null;
    }
    
    public void loanBooks() {
        for (Book book : reservedBooks) {
            //TODO
        }
    }
    
    public void reserve() {
        for (Long bookId : checked.keySet()) {
            Book book = bookManager.getBookById(bookId);
            book.setReserved(true);
            reservedBooks.add(book);
        }
    }

}
