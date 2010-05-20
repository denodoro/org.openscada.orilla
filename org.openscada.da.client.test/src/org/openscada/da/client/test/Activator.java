/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

package org.openscada.da.client.test;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class Activator extends AbstractUIPlugin
{
    private static Logger _log = Logger.getLogger ( "org.openscada.da.client.test.Plugin" );

    public static final String PLUGIN_ID = "org.openscada.da.client.test";

    //The shared instance.
    private static Activator plugin = null;

    /**
     * The constructor.
     */
    public Activator ()
    {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    @Override
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
    }

    /**
     * This method is called when the plug-in is stopped
     */
    @Override
    public void stop ( final BundleContext context ) throws Exception
    {
        super.stop ( context );
        plugin = null;
    }

    /**
     * Returns the shared instance.
     */
    public static Activator getDefault ()
    {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor ( final String path )
    {
        return AbstractUIPlugin.imageDescriptorFromPlugin ( "org.openscada.da.client.test", path );
    }

    public static String getId ()
    {
        return getDefault ().getBundle ().getSymbolicName ();
    }

    public static void logError ( final int code, final String msg, final Throwable ex )
    {
        getDefault ().getLog ().log ( new Status ( IStatus.ERROR, getId (), code, msg, ex ) );
    }

    /**
     * Notify error using message box (thread safe).
     * @param message The message to display
     * @param error The error that occurred
     */
    public void notifyError ( final String message, final Throwable error )
    {
        final Display display = getWorkbench ().getDisplay ();

        if ( !display.isDisposed () )
        {
            display.asyncExec ( new Runnable () {

                public void run ()
                {
                    final Shell shell = getWorkbench ().getActiveWorkbenchWindow ().getShell ();
                    _log.debug ( String.format ( "Shell disposed: %s", shell.isDisposed () ) );
                    if ( !shell.isDisposed () )
                    {
                        final IStatus status = new OperationStatus ( OperationStatus.ERROR, PLUGIN_ID, 0, message + ":" + error.getMessage (), error );
                        ErrorDialog.openError ( shell, null, message, status );
                    }
                }
            } );
        }
    }
}
