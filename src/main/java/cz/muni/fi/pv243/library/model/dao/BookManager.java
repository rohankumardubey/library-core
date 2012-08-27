/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv243.library.model.dao;

import cz.muni.fi.pv243.library.model.Book;
import java.util.List;

/**
 *
 * @author vramik
 */
public interface BookManager {
    public void addBook(Book book);
    public void removeBook(Book book);
    public void updateBook(Book book);
    public List<Book> getAllBooks();
    public Book getBookById(Long id);
}
