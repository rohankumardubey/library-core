package cz.muni.fi.pv243.library.security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.seam.security.Identity;
import org.jboss.seam.security.events.LoggedInEvent;
import org.jboss.solder.logging.Logger;

@ApplicationScoped
public class LibraryLogging {

    
	public void userLoggedIn(@Observes LoggedInEvent event, Logger log) {
		log.info("LibraryUser " + event.getUser().getId() + " logged in.");
        throw new RuntimeException(log.getName());
    }
	
}
