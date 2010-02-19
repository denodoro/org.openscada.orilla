package org.openscada.ae.ui.views.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.Variant;

public abstract class BaseLabelProvider extends CellLabelProvider
{
    protected final DateFormat df = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        return null;
    }

    protected String toLabel ( final DecoratedEvent event, final Fields field )
    {
        Variant value = event.getEvent ().getField ( field );
        return toLabel ( value );
    }

    protected String toLabel ( final Variant value )
    {
        if ( value == null )
        {
            return "";
        }
        return value.toLabel ( "" );
    }

    protected String formatDate ( final Date date )
    {
        if ( date == null )
        {
            return "";
        }
        return this.df.format ( date );
    }
}
