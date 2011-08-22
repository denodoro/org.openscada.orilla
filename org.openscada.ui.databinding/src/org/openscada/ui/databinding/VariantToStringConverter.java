/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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
