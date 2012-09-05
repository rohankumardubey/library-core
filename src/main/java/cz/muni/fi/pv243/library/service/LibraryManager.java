/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.service;

import cz.muni.fi.pv243.library.model.Book;
import java.util.List;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public interface LibraryManager {

    public enum BookAttributeType {

        TITLE, AUTHOR, ISBN;
    }

    public List<Book> findBooksBy();

    public void loanBooks();

    public void returnBook(Book book);
}
