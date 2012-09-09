package cz.muni.fi.pv243.library.security;

import cz.muni.fi.pv243.library.model.LibraryUser;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import org.jboss.seam.security.events.LoggedInEvent;
import org.jboss.solder.logging.Logger;

@ApplicationScoped
public class LibraryLogging {

	public void userLoggedIn(@Observes LoggedInEvent event, Logger log) {
        LibraryUser user = (LibraryUser) event.getUser();
		log.info("LibraryUser " +  user.getUsername() + " logged in.");
    }
}
