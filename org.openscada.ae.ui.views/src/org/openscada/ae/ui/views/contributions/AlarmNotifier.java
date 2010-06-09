package org.openscada.ae.ui.views.contributions;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.openscada.ae.MonitorStatus;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.config.AlarmNotifierConfiguration;
import org.openscada.ae.ui.views.config.ConfigurationHelper;
import org.openscada.core.Variant;
import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionTracker;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.openscada.core.subscription.SubscriptionState;
import org.openscada.da.client.ItemManager;
import org.openscada.da.client.ItemUpdateListener;
import org.openscada.da.connection.provider.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmNotifier extends WorkbenchWindowControlContribution implements MouseListener
{
    private final static Logger logger = LoggerFactory.getLogger ( AlarmNotifier.class );

    public static final String ID = "org.openscada.ae.ui.views.contributions.alarmnotifier";

    private Label label;

    private ParameterizedCommand ackAlarmsAvailableCommand;

    private ParameterizedCommand alarmsAvailableCommand;

    private String connectionId;

    protected ConnectionService connectionService;

    private ConnectionIdTracker connectionTracker;

    private String prefix;

    private final Map<String, AtomicInteger> monitorStatus = new HashMap<String, AtomicInteger> ();

    private ItemManager itemManager;

    private final Map<String, ItemUpdateListener> listeners = new HashMap<String, ItemUpdateListener> ();

    private Composite panel;

    private Clip clip;

    private URL soundFile;

    private volatile boolean connected = false;

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
        super.dispose ();
    }

    @Override
    protected Control createControl ( final Composite parent )
    {
        for ( MonitorStatus ms : MonitorStatus.values () )
        {
            this.monitorStatus.put ( ms.name (), new AtomicInteger ( 0 ) );
        }

        this.panel = new Composite ( parent, SWT.BORDER );
        this.panel.setLayout ( new RowLayout () );
        this.panel.setBackground ( getColor () );
        this.panel.setCursor ( parent.getDisplay ().getSystemCursor ( SWT.CURSOR_HAND ) );
        this.panel.addMouseListener ( this );

        this.label = new Label ( this.panel, SWT.NONE );
        this.label.setText ( getLabel () );
        this.label.setBackground ( getColor () );
        this.label.setAlignment ( SWT.CENTER );
        this.label.addMouseListener ( this );
        this.label.setLayoutData ( new RowData ( 110, SWT.DEFAULT ) );

        loadConfiguration ();

        return this.panel;
    }

    private void loadConfiguration ()
    {
        AlarmNotifierConfiguration cfg = ConfigurationHelper.findAlarmNotifierConfiguration ();
        if ( cfg != null )
        {
            try
            {
                setConfiguration ( cfg );
            }
            catch ( final Exception e )
            {
                logger.warn ( "Failed to apply configuration", e );
            }
        }
        else
        {
            logger.info ( "no configuration found" );
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
            public void setConnection ( final org.openscada.core.connection.provider.ConnectionService connectionService )
            {
                if ( connectionService == null )
                {
                    onDisconnect ();

                    AlarmNotifier.this.connectionService = null;
                    return;
                }
                AlarmNotifier.this.connectionService = (ConnectionService)connectionService;
                if ( connectionService.getConnection () == null )
                {
                    onDisconnect ();
                    return;
                }
                connectionService.getConnection ().addConnectionStateListener ( new ConnectionStateListener () {
                    public void stateChange ( final Connection connection, final ConnectionState state, final Throwable error )
                    {
                        if ( state == ConnectionState.BOUND )
                        {
                            onConnect ();
                            return;
                        }
                        onDisconnect ();
                    }
                } );
                if ( connectionService.getConnection ().getState () == ConnectionState.BOUND )
                {
                    onConnect ();
                }
                else
                {
                    onDisconnect ();
                }
            }
        };
        this.connectionTracker = new ConnectionIdTracker ( Activator.getDefault ().getBundle ().getBundleContext (), this.connectionId, connectionServiceListener );
        this.connectionService = null;
        this.connectionTracker.open ();
    }

    private void onDisconnect ()
    {
        if ( this.itemManager == null )
        {
            return;
        }
        for ( final MonitorStatus ms : MonitorStatus.values () )
        {
            final String id = this.prefix + "." + ms.name ();
            this.itemManager.removeItemUpdateListener ( this.prefix + "." + ms.name (), this.listeners.get ( id ) );
        }
        final String id = this.prefix + "." + "ALERT_ACTIVE";
        this.itemManager.removeItemUpdateListener ( id, this.listeners.get ( id ) );
        disableHorn ();
    }

    private void onConnect ()
    {
        this.itemManager = new ItemManager ( this.connectionService.getConnection () );
        for ( final MonitorStatus ms : MonitorStatus.values () )
        {
            final String id = this.prefix + "." + ms.name ();
            this.listeners.put ( id, new ItemUpdateListener () {
                public void notifySubscriptionChange ( final SubscriptionState subscriptionState, final Throwable subscriptionError )
                {
                }

                public void notifyDataChange ( final Variant value, final Map<String, Variant> attributes, final boolean cache )
                {
                    AlarmNotifier.this.monitorStatus.get ( ms.name () ).set ( value.asInteger ( 0 ) );
                    updateAlarms ();
                }
            } );
            this.itemManager.addItemUpdateListener ( id, this.listeners.get ( id ) );
        }
        final String id = this.prefix + "." + "ALERT_ACTIVE";
        this.listeners.put ( id, new ItemUpdateListener () {
            public void notifySubscriptionChange ( final SubscriptionState subscriptionState, final Throwable subscriptionError )
            {
                AlarmNotifier.this.connected = subscriptionState == SubscriptionState.CONNECTED;
            }

            public void notifyDataChange ( final Variant value, final Map<String, Variant> attributes, final boolean cache )
            {
                if ( value.asBoolean ( false ) )
                {
                    try
                    {
                        enableHorn ();
                    }
                    catch ( Exception e )
                    {
                        logger.error ( "could not play sound!", e );
                    }
                }
                else
                {
                    disableHorn ();
                }
            }
        } );
        this.itemManager.addItemUpdateListener ( id, this.listeners.get ( id ) );
    }

    public void mouseDoubleClick ( final MouseEvent e )
    {
    }

    public void mouseDown ( final MouseEvent e )
    {
    }

    public void mouseUp ( final MouseEvent e )
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
        catch ( PartInitException ex )
        {
            throw new RuntimeException ( ex );
        }
    }

    private void executeCommand ( final ParameterizedCommand command ) throws PartInitException
    {
        IHandlerService handlerService = (IHandlerService)getWorkbenchWindow ().getService ( IHandlerService.class );
        if ( command.getCommand ().isDefined () )
        {
            try
            {
                handlerService.executeCommand ( command, null );
            }
            catch ( Exception e )
            {
                throw new RuntimeException ( e );
            }
        }
    }

    private void updateAlarms ()
    {
        getWorkbenchWindow ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                if ( !AlarmNotifier.this.panel.isDisposed () && !AlarmNotifier.this.label.isDisposed () )
                {
                    AlarmNotifier.this.panel.setBackground ( getColor () );
                    AlarmNotifier.this.label.setBackground ( getColor () );
                    AlarmNotifier.this.label.setText ( getLabel () );
                }
            }
        } );
    }

    private void disableHorn ()
    {
        if ( this.clip != null )
        {
            this.clip.stop ();
            this.clip.close ();
            this.clip = null;
        }
    }

    private void enableHorn () throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        if ( ( this.clip == null ) || !this.clip.isRunning () )
        {
            AudioInputStream sound = AudioSystem.getAudioInputStream ( this.soundFile );
            DataLine.Info info = new DataLine.Info ( Clip.class, sound.getFormat () );
            this.clip = (Clip)AudioSystem.getLine ( info );
            this.clip.open ( sound );
            this.clip.loop ( Clip.LOOP_CONTINUOUSLY );
        }
    }

    private int numberOfAckAlarms ()
    {
        int alarms = 0;
        for ( Entry<String, AtomicInteger> entry : this.monitorStatus.entrySet () )
        {
            if ( Arrays.asList ( new String[] { "NOT_AKN", "NOT_OK_NOT_AKN" } ).contains ( entry.getKey () ) )
            {
                alarms += entry.getValue ().get ();
            }
        }
        return alarms;
    }

    private int numberOfAlarms ()
    {
        int alarms = 0;
        for ( Entry<String, AtomicInteger> entry : this.monitorStatus.entrySet () )
        {
            if ( Arrays.asList ( new String[] { "NOT_OK", "NOT_OK_AKN", "NOT_OK_NOT_AKN" } ).contains ( entry.getKey () ) )
            {
                alarms += entry.getValue ().get ();
            }
        }
        return alarms;
    }

    private String getLabel ()
    {
        if ( ( this.connectionService == null ) || ( this.connectionService.getConnection ().getState () != ConnectionState.BOUND ) )
        {
            return "disconnected";
        }
        if ( numberOfAlarms () + numberOfAckAlarms () == 0 )
        {
            return "no Alarm";
        }
        return numberOfAckAlarms () + "/" + numberOfAlarms () + " Alarms";
    }

    private Color getColor ()
    {
        if ( !this.connected )
        {
            return this.getWorkbenchWindow ().getWorkbench ().getDisplay ().getSystemColor ( SWT.COLOR_MAGENTA );
        }
        if ( numberOfAlarms () + numberOfAckAlarms () == 0 )
        {
            return this.getWorkbenchWindow ().getWorkbench ().getDisplay ().getSystemColor ( SWT.COLOR_WIDGET_BACKGROUND );
        }
        if ( numberOfAckAlarms () > 0 )
        {
            return this.getWorkbenchWindow ().getWorkbench ().getDisplay ().getSystemColor ( SWT.COLOR_RED );
        }
        return this.getWorkbenchWindow ().getWorkbench ().getDisplay ().getSystemColor ( SWT.COLOR_YELLOW );
    }
}
