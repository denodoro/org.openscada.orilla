package org.openscada.core.ui.styles;

import java.util.HashMap;
import java.util.Map;

public class StyleManager
{
    private final Map<Style, StyleInformation> styles = new HashMap<Style, StyleInformation> ();

    public StyleInformation getStyle ( final Style style )
    {
        return this.styles.get ( style );
    }

    public void put ( final Style style, final StyleInformation styleInformation )
    {
        this.styles.put ( style, styleInformation );
    }
}
