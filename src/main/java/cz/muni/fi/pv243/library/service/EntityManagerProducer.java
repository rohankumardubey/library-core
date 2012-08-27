package cz.muni.fi.pv243.library.service;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
public class EntityManagerProducer {

//    @ExtensionManaged
//    @Produces
//    @PersistenceUnit
//    @ConversationScoped
////    @Dependent
//    EntityManagerFactory entityManagerFactory;
    
    @Produces
    @PersistenceContext
    private EntityManager em;
}
