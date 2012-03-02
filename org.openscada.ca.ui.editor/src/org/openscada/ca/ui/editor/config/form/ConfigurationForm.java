package org.openscada.ca.ui.editor.config.form;

import org.eclipse.swt.widgets.Composite;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;

public interface ConfigurationForm
{
    public void createFormPart ( Composite parent, ConfigurationEditorInput input );

    public void dispose ();

    public void setFocus ();
}
