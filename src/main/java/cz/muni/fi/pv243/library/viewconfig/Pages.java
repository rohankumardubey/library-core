/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.pv243.library.viewconfig;

import cz.muni.fi.pv243.library.security.Admin;
import cz.muni.fi.pv243.library.security.Librarian;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.security.RestrictAtPhase;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;
import org.jboss.seam.security.annotations.LoggedIn;

/**
 *
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@ViewConfig
public interface Pages {
	static enum Pages1 {
        
        @ViewPattern("/index.xhtml")
        INDEX,
        
        @ViewPattern("/addEditBook.xhtml")
        @LoginView("/denied.xhtml")
        @AccessDeniedView("/denied.xhtml")
        @LoggedIn
        @Librarian
        @RestrictAtPhase({ PhaseIdType.RESTORE_VIEW, PhaseIdType.INVOKE_APPLICATION })
        ADD_EDIT_BOOK,
        
        @ViewPattern("/addEditUser.xhtml")
        @LoginView("/denied.xhtml")
        @AccessDeniedView("/denied.xhtml")
        @LoggedIn
        @Admin
        @RestrictAtPhase({ PhaseIdType.RESTORE_VIEW, PhaseIdType.INVOKE_APPLICATION })
        ADD_EDIT_USER,
        
        @ViewPattern("/adminPage.xhtml")
        @LoginView("/denied.xhtml")
        @AccessDeniedView("/denied.xhtml")
        @LoggedIn
        @Admin
        @RestrictAtPhase({ PhaseIdType.RESTORE_VIEW, PhaseIdType.INVOKE_APPLICATION })
        ADMIN_PAGE,
        
        @ViewPattern("/readerDetail.xhtml")
        @LoginView("/denied.xhtml")
        @AccessDeniedView("/denied.xhtml")
        @LoggedIn
        @Librarian
        @RestrictAtPhase({ PhaseIdType.RESTORE_VIEW, PhaseIdType.INVOKE_APPLICATION })
        READER_DETAIL,
        
        @ViewPattern("/selectReader.xhtml")
        @LoginView("/denied.xhtml")
        @AccessDeniedView("/denied.xhtml")
        @LoggedIn
        @Librarian
        @RestrictAtPhase({ PhaseIdType.RESTORE_VIEW, PhaseIdType.INVOKE_APPLICATION })
        SELECT_READER
        
        
    }

}
