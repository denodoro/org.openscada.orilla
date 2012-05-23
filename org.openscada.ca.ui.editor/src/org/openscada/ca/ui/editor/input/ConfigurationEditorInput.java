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

package org.openscada.ca.ui.editor.input;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.ui.jobs.LoadJob;
import org.openscada.ca.ui.jobs.UpdateJob;

public class ConfigurationEditorInput implements IEditorInput
{

    private final String factoryId;

    private final String configurationId;

    private final String connectionUri;

    private final WritableMap dataMap = new WritableMap ();

    private final WritableValue dirtyValue = new WritableValue ( false, Boolean.class );

    public ConfigurationEditorInput ( final String connectionUri, final String factoryId, final String configurationId )
    {
        this.connectionUri = connectionUri;
        this.factoryId = factoryId;
        this.configurationId = configurationId;

        this.dataMap.addChangeListener ( new IChangeListener () {

            @Override
            public void handleChange ( final ChangeEvent event )
            {
                ConfigurationEditorInput.this.dirtyValue.setValue ( true );
            }
        } );
    }

    public String getConnectionUri ()
    {
        return this.connectionUri;
    }

    public String getConfigurationId ()
    {
        return this.configurationId;
    }

    @Override
    public String toString ()
    {
        return this.factoryId + "/" + this.configurationId;
    }

    @Override
    public boolean exists ()
    {
        return true;
    }

    @Override
    public ImageDescriptor getImageDescriptor ()
    {
        return null;
    }

    @Override
    public String getName ()
    {
        return toString ();
    }

    @Override
    public IPersistableElement getPersistable ()
    {
        return null;
    }

    @Override
    public String getToolTipText ()
    {
        return toString ();
    }

    @Override
    @SuppressWarnings ( "rawtypes" )
    public Object getAdapter ( final Class adapter )
    {
        return null;
    }

    public void performLoad ( final IProgressMonitor monitor )
    {
        final LoadJob job = load ();
        job.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                handleSetResult ( job.getConfiguration () );
            }
        } );
        job.setProgressGroup ( monitor, 2 );
        job.schedule ();
    }

    protected void setResult ( final ConfigurationInformation configurationInformation )
    {
        this.dataMap.setStale ( true );
        this.dataMap.clear ();
        if ( configurationInformation != null )
        {
            this.dataMap.putAll ( configurationInformation.getData () );
        }
        this.dataMap.setStale ( false );

        this.dirtyValue.setValue ( false );
    }

    protected void handleSetResult ( final ConfigurationInformation configurationInformation )
    {
        final Realm realm = this.dataMap.getRealm ();
        realm.asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                if ( !ConfigurationEditorInput.this.dataMap.isDisposed () )
                {
                    setResult ( configurationInformation );
                }
            }
        } );
    }

    @SuppressWarnings ( "unchecked" )
    public void performSave ( final IProgressMonitor monitor )
    {
        // provide a copy so that it can be accessed outside the realm
        final UpdateJob updateJob = update ( new HashMap<String, String> ( this.dataMap ) );

        updateJob.setProgressGroup ( monitor, 2 );

        updateJob.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                performLoad ( monitor );
            }
        } );

        updateJob.schedule ();
    }

    protected LoadJob load ()
    {
        return new LoadJob ( this.connectionUri, this.factoryId, this.configurationId );
    }

    protected UpdateJob update ( final Map<String, String> data )
    {
        return new UpdateJob ( this.connectionUri, this.factoryId, this.configurationId, data );
    }

    public void updateEntry ( final String oldKey, final String key, final String value )
    {
        this.dataMap.setStale ( true );
        this.dataMap.remove ( oldKey );
        this.dataMap.put ( key, value );
        this.dataMap.setStale ( false );

        this.dirtyValue.setValue ( true );
    }

    public void insertEntry ( final String key, final String value )
    {
        this.dataMap.put ( key, value );
        this.dirtyValue.setValue ( true );
    }

    public void deleteEntry ( final String key )
    {
        this.dataMap.remove ( key );
        this.dirtyValue.setValue ( true );
    }

    public WritableMap getDataMap ()
    {
        return this.dataMap;
    }

    public WritableValue getDirtyValue ()
    {
        return this.dirtyValue;
    }

    public String getFactoryId ()
    {
        return this.factoryId;
    }
}
