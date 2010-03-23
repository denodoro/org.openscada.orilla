package org.openscada.core.ui.connection.data;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openscada.core.client.Connection;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.utils.str.StringHelper;

public class PropertySourceWrapper implements IPropertySource
{

    private static final String CAT_SESSION_PROPERTIES = Messages.PropertySourceWrapper_CatSessionProperties;

    private static final String CAT_CONNECTION_STATE = Messages.PropertySourceWrapper_CatConnectionState;

    private static final String CAT_CONNECTION_SERVICE = Messages.PropertySourceWrapper_CatConnectionService;

    private static final String CAT_CONNECTION_INFORMATION = Messages.PropertySourceWrapper_CatConnectionInformation;

    private static enum Properties
    {
        URI,
        ID,
        INTERFACES,
        STATE
    }

    private static class SessionPropertyDescriptor
    {
        private final String key;

        public SessionPropertyDescriptor ( final String key )
        {
            this.key = key;
        }

        public String getKey ()
        {
            return this.key;
        }
    }

    private final ConnectionHolder connectionHolder;

    public PropertySourceWrapper ( final ConnectionHolder connectionHolder )
    {
        this.connectionHolder = connectionHolder;
    }

    public boolean isPropertySet ( final Object id )
    {
        return false;
    }

    public Object getEditableValue ()
    {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors ()
    {
        final Collection<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor> ();

        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.URI, Messages.PropertySourceWrapper_UriName );
            pd.setCategory ( CAT_CONNECTION_INFORMATION );
            pd.setDescription ( Messages.PropertySourceWrapper_UriDesc );
            properties.add ( pd );
        }
        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.ID, Messages.PropertySourceWrapper_IdName );
            pd.setCategory ( CAT_CONNECTION_INFORMATION );
            pd.setDescription ( Messages.PropertySourceWrapper_IdDesc );
            properties.add ( pd );
        }
        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.INTERFACES, Messages.PropertySourceWrapper_InterfacesName );
            pd.setCategory ( CAT_CONNECTION_SERVICE );
            pd.setDescription ( Messages.PropertySourceWrapper_InterfacesDesc );
            properties.add ( pd );
        }
        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.STATE, Messages.PropertySourceWrapper_StateName );
            pd.setCategory ( CAT_CONNECTION_STATE );
            pd.setDescription ( Messages.PropertySourceWrapper_StateDesc );
            properties.add ( pd );
        }

        final Connection connection = getConnection ();
        if ( connection != null )
        {
            for ( final String key : connection.getSessionProperties ().keySet () )
            {
                final PropertyDescriptor pd = new PropertyDescriptor ( new SessionPropertyDescriptor ( key ), key );
                pd.setCategory ( CAT_SESSION_PROPERTIES );
                properties.add ( pd );
            }
        }

        return properties.toArray ( new IPropertyDescriptor[0] );
    }

    private Connection getConnection ()
    {
        if ( this.connectionHolder == null )
        {
            return null;
        }
        if ( this.connectionHolder.getConnectionService () == null )
        {
            return null;
        }
        return this.connectionHolder.getConnectionService ().getConnection ();
    }

    public Object getPropertyValue ( final Object id )
    {
        if ( Properties.URI.equals ( id ) )
        {
            return this.connectionHolder.getConnectionInformation ().getConnectionInformation ().toString ();
        }
        else if ( Properties.ID.equals ( id ) )
        {
            return this.connectionHolder.getConnectionInformation ().getServiceId ();
        }

        final ConnectionService service = this.connectionHolder.getConnectionService ();
        if ( service == null )
        {
            return null;
        }

        if ( Properties.INTERFACES.equals ( id ) )
        {
            return String.format ( Messages.PropertySourceWrapper_StateFormat, StringHelper.join ( service.getSupportedInterfaces (), Messages.PropertySourceWrapper_StateFormatDelim ) );
        }

        final Connection connection = service.getConnection ();
        if ( connection == null )
        {
            return connection;
        }

        if ( Properties.STATE.equals ( id ) )
        {
            return connection.getState ();
        }

        if ( id instanceof SessionPropertyDescriptor )
        {
            return connection.getSessionProperties ().get ( ( (SessionPropertyDescriptor)id ).getKey () );
        }

        return null;
    }

    public void resetPropertyValue ( final Object id )
    {
    }

    public void setPropertyValue ( final Object id, final Object value )
    {
    }

}
