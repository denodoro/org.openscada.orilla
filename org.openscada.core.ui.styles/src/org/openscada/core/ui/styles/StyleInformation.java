package org.openscada.core.ui.styles;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;

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
}
