package org.openscada.ae.ui.views.export.excel.config;

import org.openscada.ae.Event;
import org.openscada.ae.ui.views.export.excel.Cell;

public interface Field
{
    public String getHeader ();

    public void render ( Event event, Cell cell );
}