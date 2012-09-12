package cz.muni.fi.pv243.library.model;

import cz.muni.fi.pv243.library.validation.Isbn;
import cz.muni.fi.pv243.library.validation.PastYear;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@Entity
public class Book implements Serializable {
    
    private static final long serialVersionUID = 65465416546515L;
            
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    @NotEmpty
    @Size(max = 150, message = "Title must be at most 150 characters long.")
    private String title;
    
    @Column
    @Size(min = 3, max = 75, message = "Author must be between 3 and 75 characters.")
    @Pattern(regexp = "[A-Ža-ž ]*", message = "Author must contain only characters and spaces")
    private String author;
    
    @PastYear
    @Column(name = "yearOfBook")
    private int year;
    
    @Column
    @NotEmpty
    @Isbn
    private String isbn;
    
    @ManyToOne
    @JoinColumn(name="reader")
    private LibraryUser user;
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public boolean isLoan() {
        return user != null;
    }

    public LibraryUser getUser() {
        return user;
    }

    public void setUser(LibraryUser user) {
        this.user = user;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if ((this.author == null) ? (other.author != null) : !this.author.equals(other.author)) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if ((this.isbn == null) ? (other.isbn != null) : !this.isbn.equals(other.isbn)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 53 * hash + (this.author != null ? this.author.hashCode() : 0);
        hash = 53 * hash + this.year;
        hash = 53 * hash + (this.isbn != null ? this.isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", author=" + author + ", year=" + year + ", isbn=" + isbn + '}';
    }

    
}
