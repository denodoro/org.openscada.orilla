package org.openscada.core.ui.connection.login.dialog;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.login.Activator;
import org.openscada.core.ui.connection.login.LoginConnection;
import org.openscada.core.ui.connection.login.LoginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginDialog extends TitleAreaDialog
{

    private final static Logger logger = LoggerFactory.getLogger ( LoginDialog.class );

    private ComboViewer contextSelector;

    private Text userText;

    private Text passwordText;

    private final LoginContext[] contexts;

    private LoginContext loginContext;

    private ConnectionAnalyzer analyzer;

    private ContextCreator creator;

    private String user;

    private String password;

    private final IDialogSettings dialogSettings;

    public LoginDialog ( final Shell parentShell )
    {
        super ( parentShell );
        setBlockOnOpen ( true );
        setTitle ( "Log on to system" );
        setHelpAvailable ( false );

        this.contexts = Activator.getDefault ().getContextList ();
        this.dialogSettings = getDialogSection ();
    }

    private IDialogSettings getDialogSection ()
    {
        IDialogSettings section = Activator.getDefault ().getDialogSettings ().getSection ( "LoginDialog" );
        if ( section == null )
        {
            section = Activator.getDefault ().getDialogSettings ().addNewSection ( "LoginDialog" );
        }
        return section;
    }

    /**
     * Save current state to the dialog settings
     */
    private void saveTo ()
    {
        if ( this.loginContext != null && this.user != null )
        {
            this.dialogSettings.put ( "context", this.loginContext.getId () );
            this.dialogSettings.put ( "user", this.user );
        }
    }

    /**
     * Load the current state from the dialog settings
     */
    private void loadFrom ()
    {
        String user = this.dialogSettings.get ( "user" );
        String contextId = this.dialogSettings.get ( "context" );
        if ( user != null && contextId != null )
        {
            this.userText.setText ( user );
            for ( LoginContext context : this.contexts )
            {
                if ( context.getId ().equals ( contextId ) )
                {
                    this.contextSelector.setSelection ( new StructuredSelection ( context ), true );
                }
            }
        }
    }

    private void update ()
    {
        final Button button = getButton ( OK );
        try
        {
            setMessage ( "Enter your login information", IMessageProvider.INFORMATION );
            validate ();

            button.setEnabled ( true );
        }
        catch ( final Exception e )
        {
            button.setEnabled ( false );
            setMessage ( e.getMessage (), IMessageProvider.ERROR );
        }

    }

    private void validate ()
    {
        this.user = this.userText.getText ();
        this.password = this.passwordText.getText ();

        this.loginContext = null;
        final ISelection sel = this.contextSelector.getSelection ();
        if ( sel instanceof IStructuredSelection )
        {
            if ( ! ( (IStructuredSelection)sel ).isEmpty () )
            {
                final Object o = ( (IStructuredSelection)sel ).getFirstElement ();
                if ( o instanceof LoginContext )
                {
                    this.loginContext = (LoginContext)o;
                }
            }
        }

        if ( this.loginContext == null )
        {
            throw new IllegalStateException ( "No login context selected" );
        }

        if ( this.password == null || this.password.length () == 0 )
        {
            // set to null in case this is an empty string
            this.password = null;
            setMessage ( "Empty password", IMessageProvider.WARNING );
        }
        if ( this.user == null || this.user.length () == 0 )
        {
            // set to null in case this is an empty string
            this.user = null;
            setMessage ( "Empty user name", IMessageProvider.WARNING );
        }
    }

    @Override
    protected void configureShell ( final Shell newShell )
    {
        super.configureShell ( newShell );
        newShell.setText ( "Logon" );
    }

    protected Control createDialogArea ( final Composite parent )
    {
        final Composite wrapper = (Composite)super.createDialogArea ( parent );

        initializeDialogUnits ( wrapper );
        final Composite contents = createComposite ( wrapper );
        contents.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );
        Dialog.applyDialogFont ( wrapper );

        setTitle ( "Log on to system" );

        loadFrom ();

        return wrapper;
    }

    @Override
    protected Control createButtonBar ( final Composite parent )
    {
        final Control control = super.createButtonBar ( parent );

        update ();

        return control;
    }

    private Composite createComposite ( final Composite parent )
    {
        final Composite contents = new Composite ( parent, SWT.NONE );

        contents.setLayout ( new GridLayout ( 2, false ) );

        Label label;

        // context
        label = new Label ( contents, SWT.NONE );
        label.setText ( "Context:" );
        this.contextSelector = new ComboViewer ( contents, SWT.READ_ONLY );
        this.contextSelector.setContentProvider ( new ArrayContentProvider () );
        this.contextSelector.setLabelProvider ( new LabelProvider () {
            public String getText ( final Object element )
            {
                final LoginContext ctx = (LoginContext)element;
                return ctx.getName ();
            };
        } );
        this.contextSelector.setInput ( this.contexts );
        applyLayout ( this.contextSelector.getControl () );
        this.contextSelector.addSelectionChangedListener ( new ISelectionChangedListener () {

            public void selectionChanged ( final SelectionChangedEvent event )
            {
                update ();
            }
        } );

        // username
        label = new Label ( contents, SWT.NONE );
        label.setText ( "User:" );
        this.userText = new Text ( contents, SWT.BORDER );
        this.userText.setMessage ( "Enter username" );
        applyLayout ( this.userText );
        this.userText.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        // password
        label = new Label ( contents, SWT.NONE );
        label.setText ( "Password:" );
        this.passwordText = new Text ( contents, SWT.BORDER | SWT.PASSWORD );
        this.passwordText.setMessage ( "Enter password" );
        applyLayout ( this.passwordText );
        this.passwordText.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        this.analyzer = new ConnectionAnalyzer ( contents, SWT.NONE );
        this.analyzer.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
        this.analyzer.setBackground ( Display.getCurrent ().getSystemColor ( SWT.COLOR_RED ) );

        return contents;
    }

    private void applyLayout ( final Control control )
    {
        control.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );
    }

    @Override
    protected void okPressed ()
    {
        startLogin ( this.loginContext );
    }

    private void startLogin ( final LoginContext loginContext )
    {
        if ( this.creator != null )
        {
            logger.error ( "Found pre-existing creator instance. This should not happen!" );
            this.creator.dispose ();
            this.creator = null;
        }

        final Button button = getButton ( OK );

        button.setEnabled ( false );
        this.contextSelector.getControl ().setEnabled ( false );
        this.userText.setEnabled ( false );
        this.passwordText.setEnabled ( false );

        this.analyzer.clear ();

        final Realm realm = SWTObservables.getRealm ( getShell ().getDisplay () );
        this.creator = new ContextCreator ( realm, injectUserInformation ( loginContext ), this.analyzer, new ContextCreatorResultListener () {

            public void complete ( final Map<LoginConnection, ConnectionService> result )
            {
                handleComplete ( result );
            }
        } );
        this.creator.start ();
    }

    private LoginContext injectUserInformation ( final LoginContext loginContext )
    {
        final List<LoginConnection> connections = new LinkedList<LoginConnection> ();
        for ( final LoginConnection c : loginContext.getConnections () )
        {
            final ConnectionInformation ci = c.getConnectionInformation ();
            ci.setUser ( this.user );
            ci.setPassword ( this.password );
            connections.add ( new LoginConnection ( ci, c.getServicePid (), c.getAutoReconnectDelay (), c.getPriority () ) );
        }

        return new LoginContext ( loginContext.getId (), loginContext.getName (), connections );
    }

    protected void handleComplete ( final Map<LoginConnection, ConnectionService> result )
    {
        if ( this.creator == null )
        {
            logger.error ( "Creating is null but we got a result. This should also never happen!" );
            return;
        }

        this.creator.dispose ();
        this.creator = null;

        if ( result == null )
        {
            final Button button = getButton ( OK );
            button.setEnabled ( true );

            this.contextSelector.getControl ().setEnabled ( true );
            this.userText.setEnabled ( true );
            this.passwordText.setEnabled ( true );
        }
        else
        {
            saveTo ();
            Activator.getDefault ().setLoginSession ( this.user, this.password, this.loginContext, result );
            super.okPressed ();
        }
    }

    @Override
    protected void cancelPressed ()
    {
        if ( this.creator != null )
        {
            this.creator.stop ();
            // will receive result
        }
        else
        {
            super.cancelPressed ();
        }
    }
}
