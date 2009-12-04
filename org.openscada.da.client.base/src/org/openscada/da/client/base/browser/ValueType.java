package org.openscada.da.client.base.browser;

import org.openscada.core.NotConvertableException;
import org.openscada.core.NullValueException;
import org.openscada.core.Variant;
import org.openscada.core.VariantType;
import org.openscada.da.client.connector.Activator;

/**
 * value types used for visual input purposes
 * @author Jens Reimann
 *
 */
public enum ValueType
{
    NULL ( 10, Messages.getString ( "ValueType.NULL.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( final String value )
        {
            return new Variant ();
        }
    },
    STRING ( 20, Messages.getString ( "ValueType.STRING.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( String value )
        {
            value = value.replace ( Activator.NATIVE_LS, "\n" ); //$NON-NLS-1$
            return new Variant ( value );
        }
    },
    STRING_CRLF ( 21, Messages.getString ( "ValueType.STRING_CRLF.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( String value )
        {
            value = value.replace ( Activator.NATIVE_LS, "\r\n" ); //$NON-NLS-1$
            return new Variant ( value );
        }
    },
    INT ( 30, Messages.getString ( "ValueType.INT.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( final String value ) throws NotConvertableException
        {
            final Variant stringValue = new Variant ( value );
            try
            {
                return new Variant ( stringValue.asInteger () );
            }
            catch ( final NullValueException e )
            {
                return new Variant ();
            }
        }
    },
    LONG ( 40, Messages.getString ( "ValueType.LONG.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( final String value ) throws NotConvertableException
        {
            final Variant stringValue = new Variant ( value );
            try
            {
                return new Variant ( stringValue.asLong () );
            }
            catch ( final NullValueException e )
            {
                return new Variant ();
            }
        }
    },
    DOUBLE ( 50, Messages.getString ( "ValueType.DOUBLE.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( final String value ) throws NotConvertableException
        {
            final Variant stringValue = new Variant ( value );
            try
            {
                return new Variant ( stringValue.asDouble () );
            }
            catch ( final NullValueException e )
            {
                return new Variant ();
            }
        }
    },
    BOOLEAN ( 60, Messages.getString ( "ValueType.BOOLEAN.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant convertTo ( final String value ) throws NotConvertableException
        {
            final Variant stringValue = new Variant ( value );
            return new Variant ( stringValue.asBoolean () );
        }
    },
    ;

    private int index;

    private String label;

    ValueType ( final int index, final String label )
    {
        this.index = index;
        this.label = label;
    }

    public String label ()
    {
        return this.label;
    }

    public int index ()
    {
        return this.index;
    }

    public abstract Variant convertTo ( String value ) throws NotConvertableException;

    public static ValueType fromVariantType ( final VariantType type )
    {
        switch ( type )
        {
        case BOOLEAN:
            return ValueType.BOOLEAN;
        case DOUBLE:
            return ValueType.DOUBLE;
        case INT32:
            return ValueType.INT;
        case INT64:
            return ValueType.LONG;
        case NULL:
            return ValueType.NULL;
        case STRING:
            return ValueType.STRING;
        default:
            return ValueType.STRING;
        }
    }
}