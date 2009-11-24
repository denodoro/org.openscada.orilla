package org.openscada.ui.databinding;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;

public class StyledViewerLabel extends ViewerLabel
{

    private StyledString styledText;

    public StyledViewerLabel ( final String initialText, final Image initialImage )
    {
        super ( initialText, initialImage );
    }

    public void setStyledText ( final StyledString styledString )
    {
        this.styledText = styledString;
    }

    public StyledString getStyledText ()
    {
        if ( this.styledText != null )
        {
            return this.styledText;
        }
        final String text = getText ();
        if ( text != null )
        {
            return new StyledString ( text );
        }
        return null;
    }

}
