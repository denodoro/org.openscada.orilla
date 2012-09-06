/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.styles;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.openscada.core.ui.styles.StyleHandler.Style;

public class StateManager
{
    private final Set<StyleHandler> handlers = new LinkedHashSet<StyleHandler> ();

    private final DefaultStyleGeneratorImpl generator;

    private Style currentStyle;

    public StateManager ()
    {
        this.generator = new DefaultStyleGeneratorImpl ();
    }

    public void dispose ()
    {
        this.generator.dispose ();
    }

    public void setState ( final StateInformation state )
    {
        setStyle ( generateStyle ( state ) );
    }

    protected void setStyle ( final Style style )
    {
        this.currentStyle = style;
        fireStyleUpdate ( style );
    }

    protected void fireStyleUpdate ( final Style style )
    {
        for ( final StyleHandler handler : this.handlers )
        {
            SafeRunner.run ( new SafeRunnable () {

                @Override
                public void run () throws Exception
                {
                    handler.setStyle ( style );
                }
            } );
        }
    }

    public void addHandler ( final StyleHandler handler )
    {
        if ( this.handlers.add ( handler ) )
        {
            handler.setStyle ( this.currentStyle );
        }
    }

    public void removeHandler ( final StyleHandler handler )
    {
        this.handlers.remove ( handler );
    }

    protected StyleHandler.Style generateStyle ( final StateInformation state )
    {
        if ( state == null || this.generator == null )
        {
            return null;
        }

        return this.generator.generateStyle ( state );
    }
}
