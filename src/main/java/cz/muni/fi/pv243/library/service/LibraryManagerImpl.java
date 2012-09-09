/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.service;

import cz.muni.fi.pv243.library.model.Book;
import cz.muni.fi.pv243.library.model.LibraryUser;
import cz.muni.fi.pv243.library.model.LibraryUser.UserRole;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
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
    
//    @Inject
//    private FacesContext facesContext;
    
    private List<Book> allBooksCache;
    private Map<Long, Boolean> checkedToLoan = new HashMap<Long, Boolean>();
    private String usernameOfReader;
    private String searchFieldBooks;
    private String searchFieldUsers;
    private String bookAttributeType;
    private String userAttributeType;
    private String userRoleType;
    private LibraryUser reader;
    private LibraryUser editReader;
    private Book book;
    
    @Inject
    private BookManager bookManager;
    
    @Inject
    private LibraryUserManager libraryUserManager;

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

    public String getSearchFieldBooks() {
        return searchFieldBooks;
    }

    public void setSearchFieldBooks(String searchFieldBooks) {
        this.searchFieldBooks = searchFieldBooks;
    }

    public String getSearchFieldUsers() {
        return searchFieldUsers;
    }

    public void setSearchFieldUsers(String searchFieldUsers) {
        this.searchFieldUsers = searchFieldUsers;
    }

    public String getBookAttributeType() {
        return bookAttributeType;
    }

    public void setBookAttributeType(String bookAttributeType) {
        this.bookAttributeType = bookAttributeType;
    }

    public String getUserAttributeType() {
        return userAttributeType;
    }

    public void setUserAttributeType(String userAttributeType) {
        this.userAttributeType = userAttributeType;
    }

    public String getUserRoleType() {
        return userRoleType;
    }

    public void setUserRoleType(String userRoleType) {
        this.userRoleType = userRoleType;
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

    public LibraryUser getEditReader() {
        return editReader;
    }

    public void setEditReader(LibraryUser editReader) {
        this.editReader = editReader;
    }

    public void selectReader() {
        reader = libraryUserManager.getLibraryUserByUsername(usernameOfReader);
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
    public List<Book> findBooks() {
        if (searchFieldBooks == null || searchFieldBooks.isEmpty()) {
            return bookManager.getAllBooks();
        }
        BookAttributeType attributeType = BookAttributeType.valueOf(bookAttributeType);
        return em.createQuery("SELECT b FROM Book b WHERE b." + attributeType.toString().toLowerCase() + " LIKE :searchValue")
                .setParameter("searchValue", "%" + searchFieldBooks + "%").getResultList();
    }

    @Produces
    @Named("users")
    @Override
    public List<LibraryUser> findUsers() {
        if ("USERROLE".equals(userAttributeType)) {
            UserRole userRole = UserRole.valueOf(userRoleType);
            return em.createQuery("SELECT l FROM LibraryUser l WHERE l.userRole=:userRole")
                    .setParameter("userRole", userRole).getResultList();
        }
        if (searchFieldUsers == null || searchFieldUsers.isEmpty()) {
            return libraryUserManager.getAllLibraryUsers();
        }
        UserAttributeType attributeType = UserAttributeType.valueOf(userAttributeType);
        return em.createQuery("SELECT l FROM LibraryUser l WHERE l." + attributeType.toString().toLowerCase() + " LIKE :searchValue")
                .setParameter("searchValue", "%" + searchFieldUsers + "%").getResultList();
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
        bookManager.updateBook(book);
        book = null;
    }

    public void addBook() {
        bookManager.addBook(book);
        book = null;
    }
    
    public void loadReader(String readerId) {
        if (readerId != null) {
            editReader = libraryUserManager.getLibraryUserById(readerId);
        } else {
            editReader = new LibraryUser();
        }
    }
    
    public void updateReader() {
        libraryUserManager.updateLibraryUser(editReader);
        editReader = null;
    }

    public void addReader() {
        editReader.setPassword("password");
        libraryUserManager.addLibraryUser(editReader);
        editReader = null;
    }
    
}