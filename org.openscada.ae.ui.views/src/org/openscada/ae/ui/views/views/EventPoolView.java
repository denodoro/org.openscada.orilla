package org.openscada.ae.ui.views.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EventPoolView extends AbstractAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.eventpool";

    private Label connectionState;

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        final Text urlText = new Text ( parent, SWT.BORDER | SWT.MULTI );
        urlText.addKeyListener ( new KeyAdapter () {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( e.character == 13 )
                {
                    urlText.setText ( urlText.getText ().trim () );
                    try
                    {
                        EventPoolView.this.setConnectionUri ( urlText.getText () );
                    }
                    catch ( Exception e1 )
                    {
                        e1.printStackTrace ();
                    }
                }
            }
        } );
        this.connectionState = new Label ( parent, SWT.BORDER );
        parent.setLayout ( new FillLayout ( SWT.VERTICAL ) );
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus ()
    {
    }

    @Override
    protected void onConnect ()
    {
        this.getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                EventPoolView.this.connectionState.setText ( "CONNECTED" );
            }
        } );
    }

    @Override
    protected void onDisconnect ()
    {
        this.getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                EventPoolView.this.connectionState.setText ( "DISCONNECTED !!!" );
            }
        } );
    }
}