/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.da.client.dataitem.details.extra.part;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.swt.widgets.Composite;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;

public abstract class AbstractBaseDraw2DDetailsPart extends AbstractBaseDetailsPart
{
    private FigureCanvas canvas;

    private IFigure rootFigure;

    private IFigure naFigure;

    @Override
    public void createPart ( final Composite parent )
    {
        this.canvas = new FigureCanvas ( parent );

        this.canvas.setContents ( createRoot () );
    }

    @Override
    public void dispose ()
    {
        this.canvas.dispose ();
        this.canvas = null;

        super.dispose ();
    }

    protected abstract IFigure createMain ();

    /**
     * Check if the functionality of this tab is available or not
     * @return <code>true</code> if the functionality can be provided,
     * <code>false</code> otherwise
     */
    protected abstract boolean isAvailable ();

    protected IFigure createRoot ()
    {
        this.rootFigure = createMain ();
        this.naFigure = createNaPanel ();

        final Figure baseFigure = new LayeredPane ();

        baseFigure.add ( this.rootFigure );
        baseFigure.add ( this.naFigure );

        return baseFigure;
    }

    private IFigure createNaPanel ()
    {
        final Figure naPanel = new Figure ();

        final BorderLayout layout = new BorderLayout ();
        naPanel.setLayoutManager ( layout );

        final Label label = new Label ();
        label.setText ( Messages.AbstractBaseDraw2DDetailsPart_Label_NotAvail_Text );
        naPanel.add ( label, BorderLayout.CENTER );

        return naPanel;
    }

    @Override
    protected void update ()
    {
        if ( isForceActive () || isAvailable () )
        {
            this.rootFigure.setVisible ( true );
            this.naFigure.setVisible ( false );
        }
        else
        {
            this.rootFigure.setVisible ( false );
            this.naFigure.setVisible ( true );
        }
    }

}
