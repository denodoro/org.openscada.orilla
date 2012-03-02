package org.openscada.ca.ui.editor.forms.common.connection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openscada.ca.ui.editor.config.form.ConfigurationForm;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;

public class ConnectionEditorForm implements ConfigurationForm
{
    private DataBindingContext dbc;

    private ScrolledForm form;

    private FormToolkit toolkit;

    @Override
    public void createFormPart ( final Composite parent, final ConfigurationEditorInput input )
    {
        this.dbc = new DataBindingContext ();

        this.toolkit = new FormToolkit ( parent.getDisplay () );
        this.form = this.toolkit.createScrolledForm ( parent );
        this.form.setText ( "openSCADA Connection: " + input.getConfigurationId () );
        this.toolkit.decorateFormHeading ( this.form.getForm () );

        this.form.getBody ().setLayout ( new GridLayout () );

        final Composite client = this.toolkit.createComposite ( this.form.getBody (), SWT.NONE );
        client.setLayout ( new GridLayout ( 2, false ) );
        client.setLayoutData ( new GridData ( GridData.FILL_BOTH ) );
        this.toolkit.paintBordersFor ( client );

        this.toolkit.createLabel ( client, "Connection URI:" );

        final Text uriText = this.toolkit.createText ( client, "" );
        uriText.setMessage ( "Enter connection URI" );
        uriText.setLayoutData ( new GridData ( GridData.FILL, GridData.BEGINNING, true, false ) );

        final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "connection.uri" );
        this.dbc.bindValue ( SWTObservables.observeText ( uriText, SWT.Modify ), value );
    }

    @Override
    public void dispose ()
    {
        this.dbc.dispose ();
        this.form.dispose ();
        this.toolkit.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.form.setFocus ();
    }

}
