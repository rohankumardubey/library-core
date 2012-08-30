package cz.muni.fi.pv243.library.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.picketlink.idm.api.User;

/**
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@Entity
public class LibraryUser implements User, Serializable {

    private static final long serialVersionUID = -51311351812L;

    public enum Role {
        ADMIN, LIBRARIAN, READER
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    @Pattern(regexp = "[a-z0-9]*", message = "Username must contain only lowercase characters and numbers.")
    private String username;
    
    @Column
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
    @Pattern(regexp = "[A-Ža-ž ]*", message = "Name must contain only characters and spaces")
    private String name;
    
    @Column
    @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters.")
    @NotEmpty
    private String password;
    
    @NotNull
    private Role userRole;
    
    @Override
    public String getId() {
        return id.toString();
    }
    
    @Override
    public String getKey() {
        return getId();
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LibraryUser other = (LibraryUser) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if (this.userRole != other.userRole) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 97 * hash + (this.userRole != null ? this.userRole.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "LibraryUser{" + "id=" + id + ", username=" + username + ", name=" + name + ", password=" + password + ", UserRole=" + userRole + '}';
    }
}
