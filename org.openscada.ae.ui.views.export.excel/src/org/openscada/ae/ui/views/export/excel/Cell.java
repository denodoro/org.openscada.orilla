/**
 * 
 */
package org.openscada.ae.ui.views.export.excel;

import java.util.Date;

import org.openscada.core.Variant;

public interface Cell
{
    public void setDataAsDate ( final Date date );

    public void setDataAsText ( final String text );

    public void setDataAsVariant ( final Variant variant );
}