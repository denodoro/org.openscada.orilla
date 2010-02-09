package org.openscada.core.ui.styles;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class StyleInformation
{
    private final ColorDescriptor foreground;

    private final ColorDescriptor background;

    private final FontDescriptor font;

    public StyleInformation ( final ColorDescriptor foreground, final ColorDescriptor background, final FontDescriptor font )
    {
        super ();
        this.foreground = foreground;
        this.background = background;
        this.font = font;
    }

    public ColorDescriptor getBackground ()
    {
        return this.background;
    }

    public ColorDescriptor getForeground ()
    {
        return this.foreground;
    }

    public FontDescriptor getFont ()
    {
        return this.font;
    }

    public Color createForeground ( final ResourceManager resourceManager )
    {
        if ( this.foreground == null )
        {
            return null;
        }
        return resourceManager.createColor ( this.foreground );
    }

    public Color createBackground ( final ResourceManager resourceManager )
    {
        if ( this.background == null )
        {
            return null;
        }
        return resourceManager.createColor ( this.background );
    }

    public Font createFont ( final ResourceManager resourceManager )
    {
        if ( this.font == null )
        {
            return null;
        }
        return resourceManager.createFont ( this.font );
    }
}
