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

package org.openscada.ae.ui.views.contributions;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.openscada.ae.MonitorStatus;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.config.AlarmNotifierConfiguration;
import org.openscada.ae.ui.views.config.ConfigurationHelper;
import org.openscada.ae.ui.views.preferences.PreferenceConstants;
import org.openscada.core.Variant;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionTracker;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.openscada.da.client.DataItem;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.connection.provider.ConnectionService;
import org.openscada.ui.utils.blink.Blinker;
import org.openscada.ui.utils.blink.Blinker.Handler;
import org.openscada.ui.utils.blink.Blinker.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmNotifier extends WorkbenchWindowControlContribution
{
    private static final List<String> ALARM_STATES = Arrays.asList ( new String[] { "NOT_OK", "NOT_OK_AKN", "NOT_OK_NOT_AKN" } ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final List<String> ACK_STATES = Arrays.asList ( new String[] { "NOT_AKN", "NOT_OK_NOT_AKN" } ); //$NON-NLS-1$ //$NON-NLS-2$

    private final static Logger logger = LoggerFactory.getLogger ( AlarmNotifier.class );

    public static final String ID = "org.openscada.ae.ui.views.contributions.alarmnotifier"; //$NON-NLS-1$

    private ResourceManager resourceManager;

    private Label label;

    private ParameterizedCommand ackAlarmsAvailableCommand;

    private ParameterizedCommand alarmsAvailableCommand;

    private String connectionId;

    protected ConnectionService connectionService;

    private ConnectionIdTracker connectionTracker;

    private String prefix;

    private final Map<String, AtomicInteger> monitorStatus = new HashMap<String, AtomicInteger> ();

    private Composite panel;

    private Clip clip;

    private URL soundFile;

    private volatile boolean connected = false;

    private final Collection<DataItem> items = new HashSet<DataItem> ();

    private Display display;

    private Label bellIcon;

    private Blinker blinker;

    public AlarmNotifier ()
    {
        super ();
    }

    public AlarmNotifier ( final String id )
    {
        super ( id );
    }

    @Override
    public void dispose ()
    {
        onDisconnect ();

        if ( this.blinker != null )
        {
            this.blinker.dispose ();
        }

        this.resourceManager.dispose ();
        super.dispose ();
    }

    @Override
    protected Control createControl ( final Composite parent )
    {
        this.display = parent.getDisplay ();

        this.resourceManager = new LocalResourceManager ( JFaceResources.getResources () );

        for ( final MonitorStatus ms : MonitorStatus.values () )
        {
            this.monitorStatus.put ( ms.name (), new AtomicInteger ( 0 ) );
        }

        this.panel = new Composite ( parent, SWT.NONE );
        final GridLayout layout = new GridLayout ( 2, false );
        layout.marginHeight = layout.marginWidth = 0;
        this.panel.setLayout ( layout );
        this.panel.setCursor ( this.display.getSystemCursor ( SWT.CURSOR_HAND ) );
        // this.panel.addMouseListener ( this );

        this.label = new Label ( this.panel, SWT.NONE );
        this.label.setText ( getLabel () );
        this.label.setAlignment ( SWT.CENTER );
        GridData gd = new GridData ( SWT.CENTER, SWT.CENTER, true, true );
        gd.widthHint = gd.minimumWidth = 110;
        this.label.setLayoutData ( gd );
        this.label.addMouseListener ( new MouseAdapter () {
            @Override
            public void mouseUp ( final MouseEvent e )
            {
                triggerMainCommand ();
            };
        } );

        this.bellIcon = new Label ( this.panel, SWT.NONE );
        this.bellIcon.setAlignment ( SWT.CENTER );
        gd = new GridData ( SWT.FILL, SWT.FILL, false, true );
        gd.widthHint = gd.minimumWidth = getBellIcon ().getBounds ().width;
        this.bellIcon.setLayoutData ( gd );
        this.bellIcon.addMouseListener ( new MouseAdapter () {
            @Override
            public void mouseUp ( final MouseEvent e )
            {
                triggerBellSwitch ();
            }
        } );

        this.blinker = new Blinker ( new Handler () {

            @Override
            public void setState ( final State state )
            {
                AlarmNotifier.this.setBlinkerState ( state );
            }
        } );
        this.blinker.setState ( false, false, false, true, false, false );

        loadConfiguration ();

        return this.panel;
    }

    protected void setBlinkerState ( final State state )
    {
        trigger ( new Runnable () {

            @Override
            public void run ()
            {
                if ( AlarmNotifier.this.panel.isDisposed () )
                {
                    return;
                }

                switch ( state )
                {
                case ERROR:
                case DISCONNECTED:
                    setBackground ( AlarmNotifier.this.display.getSystemColor ( SWT.COLOR_MAGENTA ) );
                    break;

                case ALARM_1:
                case ALARM:
                    setBackground ( AlarmNotifier.this.display.getSystemColor ( SWT.COLOR_RED ) );
                    break;

                case OK:
                case ALARM_0:
                default:
                    setBackground ( null );
                    break;
                }
            }
        } );
    }

    protected void setBackground ( final Color color )
    {
        this.panel.setBackground ( color );
        this.label.setBackground ( color );
        this.bellIcon.setBackground ( color );
    }

    protected void triggerBellSwitch ()
    {
        try
        {
            this.connectionService.getConnection ().write ( getItemId ( "ALERT_ACTIVE" ), Variant.FALSE, null ); //$NON-NLS-1$
        }
        catch ( final Exception e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.AlarmNotifier_Status_ErrorWriteBellCommand, e ) );
        }
    }

    private void loadConfiguration ()
    {
        final AlarmNotifierConfiguration cfg = ConfigurationHelper.findAlarmNotifierConfiguration ();
        if ( cfg != null )
        {
            try
            {
                setConfiguration ( cfg );
            }
            catch ( final Exception e )
            {
                logger.warn ( "Failed to apply configuration", e ); //$NON-NLS-1$
            }
        }
        else
        {
            logger.info ( "no configuration found" ); //$NON-NLS-1$
        }
    }

    private void setConfiguration ( final AlarmNotifierConfiguration cfg ) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        this.connectionId = cfg.getConnectionId ();
        this.prefix = cfg.getPrefix ();
        this.soundFile = cfg.getSoundFile ();
        this.ackAlarmsAvailableCommand = cfg.getAckAlarmsAvailableCommand ();
        this.alarmsAvailableCommand = cfg.getAlarmsAvailableCommand ();
        initConnection ();
    }

    private void initConnection ()
    {
        if ( this.connectionId == null )
        {
            return;
        }
        final ConnectionTracker.Listener connectionServiceListener = new Listener () {
            @Override
            public void setConnection ( final org.openscada.core.connection.provider.ConnectionService connectionService )
            {
                AlarmNotifier.this.setConnectionService ( connectionService );
            }
        };
        this.connectionTracker = new ConnectionIdTracker ( Activator.getDefault ().getBundle ().getBundleContext (), this.connectionId, connectionServiceListener );
        this.connectionService = null;
        this.connectionTracker.open ();
    }

    private void onDisconnect ()
    {
        for ( final DataItem item : this.items )
        {
            item.deleteObservers ();
            item.unregister ();
        }
        this.items.clear ();

        this.monitorStatus.clear ();
        this.connected = false;

        disableHorn ();
        updateAlarms ();
    }

    private String getItemId ( final String localId )
    {
        return this.prefix + "." + localId; //$NON-NLS-1$
    }

    private void onConnect ()
    {
        for ( final MonitorStatus ms : MonitorStatus.values () )
        {
            final String id = getItemId ( ms.name () );

            final DataItem item = new DataItem ( id );
            item.addObserver ( new Observer () {

                @Override
                public void update ( final Observable o, final Object arg )
                {
                    updateMonitorStatus ( ms, (DataItemValue)arg );
                }
            } );
            item.register ( this.connectionService.getItemManager () );
            this.items.add ( item );
        }

        final String id = getItemId ( "ALERT_ACTIVE" ); //$NON-NLS-1$
        final DataItem item = new DataItem ( id );
        item.addObserver ( new Observer () {

            @Override
            public void update ( final Observable o, final Object arg )
            {
                trigger ( new Runnable () {

                    @Override
                    public void run ()
                    {
                        updateActiveState ( (DataItemValue)arg );
                    }
                } );
            }
        } );
        item.register ( this.connectionService.getItemManager () );
        this.items.add ( item );
    }

    private void triggerMainCommand ()
    {
        try
        {
            if ( numberOfAckAlarms () > 0 )
            {
                executeCommand ( this.ackAlarmsAvailableCommand );
            }
            else
            {
                executeCommand ( this.alarmsAvailableCommand );
            }
        }
        catch ( final PartInitException ex )
        {
            throw new RuntimeException ( ex );
        }
    }

    private void executeCommand ( final ParameterizedCommand command ) throws PartInitException
    {
        final IHandlerService handlerService = (IHandlerService)getWorkbenchWindow ().getService ( IHandlerService.class );
        if ( command.getCommand ().isDefined () )
        {
            try
            {
                handlerService.executeCommand ( command, null );
            }
            catch ( final Exception e )
            {
                throw new RuntimeException ( e );
            }
        }
    }

    protected void trigger ( final Runnable run )
    {
        if ( this.display == null || this.display.isDisposed () )
        {
            return;
        }
        this.display.asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                if ( AlarmNotifier.this.display.isDisposed () )
                {
                    return;
                }
                run.run ();
            }
        } );
    }

    private void updateAlarms ()
    {
        trigger ( new Runnable () {
            @Override
            public void run ()
            {
                if ( !AlarmNotifier.this.panel.isDisposed () && !AlarmNotifier.this.label.isDisposed () )
                {
                    updateState ();
                    AlarmNotifier.this.label.setText ( getLabel () );
                }
            }
        } );
    }

    protected void updateState ()
    {
        this.blinker.setState ( numberOfAlarms () > 0, numberOfAckAlarms () > 0, false, !this.connected, false, false );
    }

    private void disableHorn ()
    {
        if ( this.clip != null )
        {
            this.clip.stop ();
            this.clip.close ();
            this.clip = null;

        }
        if ( !this.bellIcon.isDisposed () )
        {
            this.bellIcon.setImage ( null );
        }
    }

    private void enableHorn () throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        if ( ( this.clip == null || !this.clip.isRunning () ) && Activator.getDefault ().getPreferenceStore ().getBoolean ( PreferenceConstants.BELL_ACTIVATED_KEY ) )
        {
            final AudioInputStream sound = AudioSystem.getAudioInputStream ( this.soundFile );
            final DataLine.Info info = new DataLine.Info ( Clip.class, sound.getFormat () );
            this.clip = (Clip)AudioSystem.getLine ( info );
            this.clip.open ( sound );
            this.clip.loop ( Clip.LOOP_CONTINUOUSLY );
        }
        if ( !this.bellIcon.isDisposed () )
        {
            this.bellIcon.setImage ( getBellIcon () );
        }
    }

    private Image getBellIcon ()
    {
        return this.resourceManager.createImageWithDefault ( ImageDescriptor.createFromFile ( AlarmNotifier.class, "icons/bell.png" ) ); //$NON-NLS-1$
    }

    private int numberOfAckAlarms ()
    {
        int alarms = 0;
        for ( final Entry<String, AtomicInteger> entry : this.monitorStatus.entrySet () )
        {
            if ( ACK_STATES.contains ( entry.getKey () ) )
            {
                alarms += entry.getValue ().get ();
            }
        }
        return alarms;
    }

    private int numberOfAlarms ()
    {
        int alarms = 0;
        for ( final Entry<String, AtomicInteger> entry : this.monitorStatus.entrySet () )
        {
            if ( ALARM_STATES.contains ( entry.getKey () ) )
            {
                alarms += entry.getValue ().get ();
            }
        }
        return alarms;
    }

    private String getLabel ()
    {
        if ( this.connectionService == null || this.connectionService.getConnection ().getState () != ConnectionState.BOUND )
        {
            return Messages.AlarmNotifier_Label_State_Disconnected;
        }
        if ( numberOfAlarms () + numberOfAckAlarms () == 0 )
        {
            return Messages.AlarmNotifier_Label_State_NoAlarm;
        }
        return String.format ( Messages.AlarmNotifier_Label_State_AlarmsFormat, numberOfAckAlarms (), numberOfAlarms () );
    }

    private void setConnectionService ( final org.openscada.core.connection.provider.ConnectionService connectionService )
    {
        if ( connectionService == null )
        {
            onDisconnect ();

            AlarmNotifier.this.connectionService = null;
            return;
        }
        else
        {
            AlarmNotifier.this.connectionService = (ConnectionService)connectionService;
            onConnect ();
        }
    }

    protected void updateMonitorStatus ( final MonitorStatus ms, final DataItemValue value )
    {
        this.monitorStatus.get ( ms.name () ).set ( value.getValue ().asInteger ( 0 ) );
        updateAlarms ();
    }

    protected void updateActiveState ( final DataItemValue value )
    {
        AlarmNotifier.this.connected = value.isConnected ();
        updateState ();

        if ( value.getValue ().asBoolean ( false ) )
        {
            try
            {
                enableHorn ();
            }
            catch ( final Exception e )
            {
                logger.error ( "could not play sound!", e ); //$NON-NLS-1$
            }
        }
        else
        {
            disableHorn ();
        }
    }
}
