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

import java.util.Set;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.openscada.core.ui.styles.StateInformation.State;
import org.openscada.core.ui.styles.StyleHandler.Style;

public class DefaultStyleGeneratorImpl implements StyleGenerator
{

    private final LocalResourceManager resourceManager;

    public DefaultStyleGeneratorImpl ()
    {
        this.resourceManager = new LocalResourceManager ( JFaceResources.getResources () );
    }

    @Override
    public Style generateStyle ( final StateInformation state )
    {
        if ( state == null )
        {
            return null;
        }

        final Set<State> states = state.getStates ();

        Image image = null;
        Color background = null;
        Color foreground = null;

        boolean monitorActive = false;

        if ( states.contains ( State.BLOCK ) )
        {
            background = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 0, 0, 0 ) ) );
            foreground = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 255, 255, 255 ) ) );
            image = Activator.getDefault ().getImageRegistry ().get ( ImageConstants.IMG_BLOCK );
        }
        if ( states.contains ( State.MANUAL ) )
        {
            background = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 100, 149, 237 ) ) );
            foreground = null;
            image = Activator.getDefault ().getImageRegistry ().get ( ImageConstants.IMG_MANUAL );
        }
        if ( states.contains ( State.WARNING ) )
        {
            background = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 255, 222, 0 ) ) );
            foreground = null;
            monitorActive = true;
            image = Activator.getDefault ().getImageRegistry ().get ( ImageConstants.IMG_WARNING );
        }
        if ( states.contains ( State.ALARM ) )
        {
            background = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 235, 53, 37 ) ) );
            foreground = null;
            monitorActive = true;
            image = Activator.getDefault ().getImageRegistry ().get ( ImageConstants.IMG_ALARM );
        }
        if ( states.contains ( State.ERROR ) )
        {
            background = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 233, 88, 254 ) ) );
            foreground = null;
            monitorActive = true;
            image = Activator.getDefault ().getImageRegistry ().get ( ImageConstants.IMG_ERROR );
        }
        if ( states.contains ( State.DISCONNECTED ) )
        {
            background = this.resourceManager.createColor ( ColorDescriptor.createFrom ( new RGB ( 233, 88, 254 ) ) );
            foreground = null;
            image = Activator.getDefault ().getImageRegistry ().get ( ImageConstants.IMG_DISCONNECTED );
        }

        if ( states.contains ( State.ACK ) )
        {
            if ( monitorActive )
            {
                return new Style ( new Image[] { image, null }, new Color[] { foreground, null }, new Color[] { background, null }, null );
            }
            else
            {
                return new Style ( new Image[] { image, image, image, null, null, null }, new Color[] { foreground, foreground, foreground, null, null, null }, new Color[] { background, background, background, null, null, null }, null );
            }
        }
        else
        {
            return new Style ( new Image[] { image }, new Color[] { foreground }, new Color[] { background }, null );
        }
    }

    public void dispose ()
    {
        this.resourceManager.dispose ();
    }
}