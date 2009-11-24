/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006 inavare GmbH (http://inavare.com)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.openscada.da.client.viewer.model.impl.converter;

import org.openscada.core.Variant;
import org.openscada.da.client.viewer.model.impl.BaseDynamicObject;
import org.openscada.da.client.viewer.model.impl.BooleanSetterOutput;
import org.openscada.da.client.viewer.model.impl.PropertyInput;

public class VariantBooleanConverter extends BaseDynamicObject
{
    private BooleanSetterOutput _output = new BooleanSetterOutput ( "value" );
    private BooleanSetterOutput _errorOutput = new BooleanSetterOutput ( "error" );
    private Variant _value = null;
    
    public VariantBooleanConverter ( String id )
    {
        super ( id );
        
        addOutput ( _output );
        addOutput ( _errorOutput );
        addInput ( new PropertyInput ( this, "value" ) );
    }
    
    public void setValue ( Variant value )
    {
        _value = value;
        update ();
    }
    
    public void update ()
    {
        try
        {
            if ( _value.isNull () )
            {
                _output.setValue ( null );
            }
            else
            {
                _output.setValue ( _value.asBoolean () );
            }
            
            _errorOutput.setValue ( false );
        }
        catch ( Exception e )
        {
            _output.setValue ( (Boolean)null );
            _errorOutput.setValue ( true );
        }
    }
}
