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

package org.openscada.ca.ui.importer.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.importer.data.DiffController;
import org.openscada.utils.concurrent.NotifyFuture;

public class LoadPage extends WizardPage
{

    private final ConnectionService service;

    private Label resultText;

    private Collection<FactoryInformation> result;

    private final DiffController mergeController;

    private long count;

    public LoadPage ( final ConnectionService service, final DiffController mergeController )
    {
        super ( "loadPage" );
        setTitle ( "Loading current data" );
        this.service = service;
        this.mergeController = mergeController;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );

        wrapper.setLayout ( new GridLayout ( 1, false ) );

        final Button loadButton = new Button ( wrapper, SWT.PUSH );
        loadButton.setText ( "Load from server" );
        loadButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                performLoad ();
            }
        } );

        final Button clearButton = new Button ( wrapper, SWT.PUSH );
        clearButton.setText ( "Clean import" );
        clearButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                performClean ();
            }
        } );

        this.resultText = new Label ( wrapper, SWT.NONE );
        this.resultText.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );

        setControl ( wrapper );

        update ();
    }

    protected void performClean ()
    {
        // set empty result set
        setResult ( Collections.<FactoryInformation> emptyList () );
    }

    protected void performLoad ()
    {
        final AtomicReference<Collection<FactoryInformation>> result = new AtomicReference<Collection<FactoryInformation>> ();

        try
        {
            getContainer ().run ( true, true, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        result.set ( loadData ( monitor ) );
                    }
                    catch ( final ExecutionException e )
                    {
                        throw new InvocationTargetException ( e );
                    }
                }

            } );
            setResult ( result.get () );
        }
        catch ( final InvocationTargetException e )
        {
            setError ( "Failed to load: " + e.getMessage () );
        }
        catch ( final InterruptedException e )
        {
            setError ( "Failed to load: " + e.getMessage () );
        }
    }

    private void setResult ( final Collection<FactoryInformation> result )
    {
        this.result = result;
        this.count = this.mergeController.setRemoteData ( result );
        update ();
    }

    private Collection<FactoryInformation> loadData ( final IProgressMonitor monitor ) throws InterruptedException, ExecutionException
    {
        final Collection<FactoryInformation> result = new LinkedList<FactoryInformation> ();
        try
        {
            final NotifyFuture<FactoryInformation[]> future = LoadPage.this.service.getConnection ().getFactories ();
            final FactoryInformation[] factories = future.get ();
            monitor.beginTask ( "Loading data", factories.length );
            for ( final FactoryInformation factory : factories )
            {
                monitor.subTask ( "Loading: " + factory.getId () );
                result.add ( this.service.getConnection ().getFactoryWithData ( factory.getId () ).get () );
                monitor.worked ( 1 );
                if ( monitor.isCanceled () )
                {
                    return null;
                }
            }
        }
        finally
        {
            monitor.done ();
        }
        return result;
    }

    private void update ()
    {
        if ( this.service == null )
        {
            setError ( "No service set" );
        }
        else if ( this.service.getConnection () == null )
        {
            setError ( "No connection set" );
        }
        else if ( this.result == null )
        {
            setError ( "No data loaded" );
        }
        else
        {
            setErrorMessage ( null );
            setDescription ( "Data is loaded" );
            setPageComplete ( true );
        }

        if ( this.result != null )
        {
            this.resultText.setText ( String.format ( "%s factories with %s entries loaded", this.result.size (), this.count ) );
        }
        else
        {
            this.resultText.setText ( "No data loaded" );
        }
    }

    private void setError ( final String string )
    {
        setPageComplete ( string == null );
        if ( string == null )
        {
            setDescription ( "Load the data from the factories" );
        }
        else
        {
            setErrorMessage ( string );
        }
    }

}
