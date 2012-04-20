/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.core.ui.connection.login.dialog;

import java.util.Collection;

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
import org.openscada.core.ui.connection.login.Activator;
import org.openscada.core.ui.connection.login.LoginContext;
import org.openscada.core.ui.connection.login.LoginHandler;
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
        setTitle ( Messages.LoginDialog_Dlg_Title );
        setHelpAvailable ( false );

        this.contexts = Activator.getDefault ().getContextList ();
        this.dialogSettings = getDialogSection ();
    }

    private IDialogSettings getDialogSection ()
    {
        IDialogSettings section = Activator.getDefault ().getDialogSettings ().getSection ( "LoginDialog" ); //$NON-NLS-1$
        if ( section == null )
        {
            section = Activator.getDefault ().getDialogSettings ().addNewSection ( "LoginDialog" ); //$NON-NLS-1$
        }
        return section;
    }

    @Override
    protected boolean isResizable ()
    {
        return true;
    }

    /**
     * Save current state to the dialog settings
     */
    private void saveTo ()
    {
        if ( this.loginContext != null && this.user != null )
        {
            this.dialogSettings.put ( "context", this.loginContext.getId () ); //$NON-NLS-1$
            this.dialogSettings.put ( "user", this.user ); //$NON-NLS-1$
        }
    }

    /**
     * Load the current state from the dialog settings
     */
    private void loadFrom ()
    {
        final String user = this.dialogSettings.get ( "user" ); //$NON-NLS-1$
        final String contextId = this.dialogSettings.get ( "context" ); //$NON-NLS-1$
        if ( user != null && contextId != null )
        {
            this.userText.setText ( user );
            for ( final LoginContext context : this.contexts )
            {
                if ( context.getId ().equals ( contextId ) )
                {
                    this.contextSelector.setSelection ( new StructuredSelection ( context ), true );
                }
            }
            this.passwordText.setFocus ();
        }
        else
        {
            this.contextSelector.getControl ().setFocus ();
        }
    }

    private void update ()
    {
        final Button button = getButton ( OK );
        try
        {
            setMessage ( Messages.LoginDialog_DefaultMessage, IMessageProvider.INFORMATION );
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
            throw new IllegalStateException ( Messages.LoginDialog_Error_NoLoginContext );
        }

        if ( this.password == null || this.password.length () == 0 )
        {
            // set to null in case this is an empty string
            this.password = null;
            setMessage ( Messages.LoginDialog_Message_EmptyPassword, IMessageProvider.WARNING );
        }
        if ( this.user == null || this.user.length () == 0 )
        {
            // set to null in case this is an empty string
            this.user = null;
            setMessage ( Messages.LoginDialog_Message_EmptyUsername, IMessageProvider.WARNING );
        }
    }

    @Override
    protected void configureShell ( final Shell newShell )
    {
        super.configureShell ( newShell );
        newShell.setText ( Messages.LoginDialog_Shell_Text );
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        final Composite wrapper = (Composite)super.createDialogArea ( parent );

        initializeDialogUnits ( wrapper );
        final Composite contents = createComposite ( wrapper );
        contents.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );
        Dialog.applyDialogFont ( wrapper );

        setTitle ( Messages.LoginDialog_Dlg_Title );

        return wrapper;
    }

    @Override
    protected Control createButtonBar ( final Composite parent )
    {
        final Control control = super.createButtonBar ( parent );

        update ();
        loadFrom ();

        return control;
    }

    private Composite createComposite ( final Composite parent )
    {
        final Composite contents = new Composite ( parent, SWT.NONE );

        contents.setLayout ( new GridLayout ( 2, false ) );

        Label label;

        // context
        label = new Label ( contents, SWT.NONE );
        label.setText ( Messages.LoginDialog_Label_Context_Text );
        this.contextSelector = new ComboViewer ( contents, SWT.READ_ONLY );
        this.contextSelector.setContentProvider ( new ArrayContentProvider () );
        this.contextSelector.setLabelProvider ( new LabelProvider () {
            @Override
            public String getText ( final Object element )
            {
                final LoginContext ctx = (LoginContext)element;
                return ctx.getName ();
            };
        } );
        this.contextSelector.setInput ( this.contexts );
        applyLayout ( this.contextSelector.getControl () );
        this.contextSelector.addSelectionChangedListener ( new ISelectionChangedListener () {

            @Override
            public void selectionChanged ( final SelectionChangedEvent event )
            {
                update ();
            }
        } );

        // username
        label = new Label ( contents, SWT.NONE );
        label.setText ( Messages.LoginDialog_Label_User_Text );
        this.userText = new Text ( contents, SWT.BORDER );
        this.userText.setMessage ( Messages.LoginDialog_Text_User_Message );
        applyLayout ( this.userText );
        this.userText.addModifyListener ( new ModifyListener () {

            @Override
            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        // password
        label = new Label ( contents, SWT.NONE );
        label.setText ( Messages.LoginDialog_Label_Password_Text );
        this.passwordText = new Text ( contents, SWT.BORDER | SWT.PASSWORD );
        this.passwordText.setMessage ( Messages.LoginDialog_Text_Password_Message );
        applyLayout ( this.passwordText );
        this.passwordText.addModifyListener ( new ModifyListener () {

            @Override
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
            logger.error ( "Found pre-existing creator instance. This should not happen!" ); //$NON-NLS-1$
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
        this.creator = new ContextCreator ( realm, loginContext, this.analyzer, new ContextCreatorResultListener () {

            @Override
            public void complete ( final Collection<LoginHandler> result )
            {
                handleComplete ( result );
            }
        } );
        this.creator.start ( this.userText.getText (), this.passwordText.getText () );
    }

    protected void handleComplete ( final Collection<LoginHandler> result )
    {
        if ( this.creator == null )
        {
            logger.error ( "Creating is null but we got a result. This should also never happen!" ); //$NON-NLS-1$
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
            this.creator.dispose ();
            this.creator = null;
            // will receive result
        }
        else
        {
            super.cancelPressed ();
        }
    }
}
