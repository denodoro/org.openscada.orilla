package org.openscada.da.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory
{

    public void createInitialLayout ( final IPageLayout layout )
    {
        layout.setEditorAreaVisible ( false );

        final IFolderLayout folder = layout.createFolder ( "org.openscada.da.ui.mainFolder", IPageLayout.RIGHT, 1.0f, IPageLayout.ID_EDITOR_AREA );

        folder.addPlaceholder ( "*" );
        folder.addPlaceholder ( "*:*" );
        folder.addView ( "org.openscada.da.test.views.RealTimeList" );
        folder.addPlaceholder ( "org.openscada.da.test.views.RealTimeList:*" );

        layout.addView ( "org.openscada.core.ui.connection.ConnectionView", IPageLayout.LEFT, 0.25f, "org.openscada.da.ui.mainFolder" );
        layout.addPlaceholder ( "org.eclipse.pde.runtime.LogView", IPageLayout.BOTTOM, 0.75f, "org.openscada.da.ui.mainFolder" );
    }

}
