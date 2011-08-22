package org.openscada.ui.databinding;

import org.eclipse.core.databinding.conversion.IConverter;
import org.openscada.core.Variant;

public class VariantToStringConverter implements IConverter
{
    private final String defaultValue;

    public VariantToStringConverter ()
    {
        this ( null );
    }

    public VariantToStringConverter ( final String defaultValue )
    {
        this.defaultValue = defaultValue;
    }

    @Override
    public Object getFromType ()
    {
        return Variant.class;
    }

    @Override
    public Object getToType ()
    {
        return String.class;
    }

    @Override
    public Object convert ( final Object fromObject )
    {
        return ( (Variant)fromObject ).asString ( this.defaultValue );
    }
}