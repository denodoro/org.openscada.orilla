package org.openscada.ca.ui.editor.config.form;

import org.eclipse.swt.widgets.Composite;

public interface ConfigurationForm
{
    public void createFormPart ( Composite parent );

    public void dispose ();

    public void setFocus ();
}
