/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.views.export.excel.impl;

import java.util.Date;

import jxl.biff.EmptyCell;
import jxl.write.Boolean;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;

import org.openscada.ae.ui.views.export.excel.Cell;
import org.openscada.core.Variant;

public class ExcelCell implements Cell
{
    private jxl.write.WritableCell cell;

    private final int row;

    private final int column;

    private final WritableCellFormat dateFormat;

    public ExcelCell ( final int row, final int column, final WritableCellFormat dateFormat )
    {
        this.row = row;
        this.column = column;
        this.dateFormat = dateFormat;
    }

    public jxl.write.WritableCell getCell ()
    {
        return this.cell;
    }

    public void setDataAsDate ( final Date date )
    {
        this.cell = new DateTime ( this.column, this.row, date, this.dateFormat );
    }

    public void setDataAsText ( final String text )
    {
        this.cell = new Label ( this.column, this.row, text );
    }

    public void setDataAsVariant ( final Variant variant )
    {
        if ( variant == null )
        {
            return;
        }
        switch ( variant.getType () )
        {
        case INT32:
        case INT64:
        case DOUBLE:
            this.cell = new Number ( this.column, this.row, variant.asDouble ( 0.0 ) );
            break;
        case BOOLEAN:
            this.cell = new Boolean ( this.column, this.row, variant.asBoolean () );
            break;
        case NULL:
            this.cell = new EmptyCell ( this.column, this.row );
            break;
        case STRING:
            setDataAsText ( variant.asString ( "" ) ); //$NON-NLS-1$
            break;
        }
    }

}