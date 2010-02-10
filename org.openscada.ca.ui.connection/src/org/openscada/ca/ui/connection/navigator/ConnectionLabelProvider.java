package org.openscada.ca.ui.connection.navigator;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.StyledString;
import org.openscada.ca.ui.connection.data.ConfigurationInformationBean;
import org.openscada.ca.ui.connection.data.FactoryInformationBean;
import org.openscada.ui.databinding.CommonListeningLabelProvider;
import org.openscada.ui.databinding.StyledViewerLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionLabelProvider extends CommonListeningLabelProvider
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionLabelProvider.class );

    private final ResourceManager resource = new LocalResourceManager ( JFaceResources.getResources () );

    public ConnectionLabelProvider ()
    {
        super ( "org.openscada.ca.ui.connection.provider" );
    }

    @Override
    public void dispose ()
    {
        this.resource.dispose ();
        super.dispose ();
    }

    @Override
    public void updateLabel ( final StyledViewerLabel label, final Object element )
    {
        logger.debug ( "Update label: {}", element );

        if ( element instanceof FactoryInformationBean )
        {
            final FactoryInformationBean entry = (FactoryInformationBean)element;
            final StyledString string = new StyledString ( entry.getFactoryInformation ().getId () );

            string.append ( String.format ( " [%s]", entry.getFactoryInformation ().getState () ), StyledString.COUNTER_STYLER );

            label.setStyledText ( string );
        }
        else if ( element instanceof ConfigurationInformationBean )
        {
            final ConfigurationInformationBean entry = (ConfigurationInformationBean)element;

            final StyledString string = new StyledString ( entry.getConfigurationInformation ().getId () );

            string.append ( String.format ( " [%s]", entry.getConfigurationInformation ().getState () ), StyledString.COUNTER_STYLER );

            label.setStyledText ( string );
        }
        else
        {
            super.updateLabel ( label, element );
        }
    }

    @Override
    public String getDescription ( final Object element )
    {
        if ( element instanceof FactoryInformationBean )
        {
            return ( (FactoryInformationBean)element ).getFactoryInformation ().getDescription ();
        }
        else
        {
            return super.getDescription ( element );
        }
    }

    @Override
    protected void addListenerTo ( final Object next )
    {
        super.addListenerTo ( next );
    }

    @Override
    protected void removeListenerFrom ( final Object next )
    {
        super.removeListenerFrom ( next );
    }

}
