package cz.muni.fi.pv243.library.security;

import cz.muni.fi.pv243.library.model.LibraryUser;
import cz.muni.fi.pv243.library.model.LibraryUser.UserRole;
import java.io.Serializable;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class UserAuthorization implements Serializable {

    private static final long serialVersionUID = 813851851L;

	@Secures
	@Admin
	public boolean isAdmin(Identity identity) {
		if (!identity.isLoggedIn()) {
			return false;
		}
		return ((LibraryUser) identity.getUser()).getUserRole().equals(UserRole.ADMIN);
	}

	@Secures
	@Librarian
	public boolean isLibrarian (Identity identity) {
		if (!identity.isLoggedIn()) {
			return false;
		}
		return ((LibraryUser) identity.getUser()).getUserRole().equals(UserRole.LIBRARIAN);
	}

	@Secures
	@Reader
	public boolean isReader(Identity identity) {
		if (!identity.isLoggedIn()) {
			return false;
		}
		return ((LibraryUser) identity.getUser()).getUserRole().equals(UserRole.READER);
	}
}
