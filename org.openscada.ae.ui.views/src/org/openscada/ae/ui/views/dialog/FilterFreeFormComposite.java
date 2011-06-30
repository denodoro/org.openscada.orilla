/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ae.ui.views.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.openscada.ae.ui.views.Messages;
import org.openscada.utils.filter.Filter;
import org.openscada.utils.filter.FilterParseException;
import org.openscada.utils.filter.FilterParser;
import org.openscada.utils.lang.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFreeFormComposite extends Composite
{

    private final static Logger logger = LoggerFactory.getLogger ( FilterFreeFormComposite.class );

    private final FilterChangedListener filterChangedListener;

    private final String filter;

    public FilterFreeFormComposite ( final FilterChangedListener filterChangedListener, final Composite parent, final int style, final String filter )
    {
        super ( parent, style );
        if ( filter == null )
        {
            throw new IllegalArgumentException ( Messages.filter_must_not_be_null );
        }
        this.filterChangedListener = filterChangedListener;
        this.filter = filter;

        createComponents ();
    }

    private void createComponents ()
    {
        final FillLayout layout = new FillLayout ( SWT.VERTICAL );
        layout.marginHeight = 12;
        layout.marginWidth = 12;
        setLayout ( layout );

        final Text filterTextField = new Text ( this, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL );
        filterTextField.setText ( this.filter );
        filterTextField.addKeyListener ( new KeyAdapter () {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                verifyFilter ( filterTextField.getText () );
            }
        } );
    }

    public boolean verifyFilter ( final String filter )
    {
        try
        {
            final Filter parsedFilter = new FilterParser ( filter ).getFilter ();
            this.filterChangedListener.onFilterChanged ( new Pair<SearchType, String> ( SearchType.FREEFORM, parsedFilter.toString () ) );
        }
        catch ( final FilterParseException e )
        {
            this.filterChangedListener.onFilterParseError ( new Pair<SearchType, String> ( SearchType.FREEFORM, "" + e.getMessage () ) ); //$NON-NLS-1$
            return false;
        }
        catch ( final Exception e )
        {
            logger.info ( "Failed to parse", e );
        }
        this.filterChangedListener.onFilterParseError ( new Pair<SearchType, String> ( SearchType.FREEFORM, null ) );
        return true;
    }
}
