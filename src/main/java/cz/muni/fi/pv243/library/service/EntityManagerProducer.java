package cz.muni.fi.pv243.library.service;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import org.jboss.solder.core.ExtensionManaged;

/**
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class EntityManagerProducer {

//    @ExtensionManaged
//    @Produces
//    @PersistenceUnit
//    @ConversationScoped
//    EntityManagerFactory entityManagerFactory;
    
    @Produces
    @PersistenceContext
    private EntityManager em;
}
