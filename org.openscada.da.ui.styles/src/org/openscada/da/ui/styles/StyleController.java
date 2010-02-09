package org.openscada.da.ui.styles;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.openscada.core.ui.styles.Style;
import org.openscada.core.ui.styles.StyleInformation;
import org.openscada.da.client.DataItemValue;

public class StyleController
{

    public Set<Style> convertToStyle ( final DataItemValue value )
    {
        final EnumSet<Style> result = EnumSet.noneOf ( Style.class );

        if ( value.isAlarm () )
        {
            result.add ( Style.ALARM );
        }

        if ( !value.isConnected () || value.isError () )
        {
            result.add ( Style.ERROR );
        }

        if ( value.isManual () )
        {
            result.add ( Style.MANUAL );
        }

        if ( result.isEmpty () )
        {
            result.add ( Style.OK );
        }

        return result;
    }

    public StyleInformation getStyle ( final Set<Style> styles )
    {
        StyleInformation result = org.openscada.core.ui.styles.Activator.getStyle ( Style.OK );

        for ( final Style style : Style.values () )
        {
            if ( styles.contains ( style ) )
            {
                result = mergeStyle ( result, org.openscada.core.ui.styles.Activator.getStyle ( Style.OK ) );
            }
        }
        return result;
    }

    private StyleInformation mergeStyle ( final StyleInformation result, final StyleInformation style )
    {
        ColorDescriptor foreground = style.getForeground ();
        ColorDescriptor background = style.getBackground ();
        FontDescriptor font = style.getFont ();
        if ( foreground == null )
        {
            foreground = result.getForeground ();
        }
        if ( background == null )
        {
            background = result.getBackground ();
        }
        if ( font == null )
        {
            font = result.getFont ();
        }

        return new StyleInformation ( foreground, background, font );
    }
}
