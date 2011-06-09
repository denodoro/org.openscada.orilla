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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.openscada.ae.ui.views.export.excel.Cell;
import org.openscada.core.Variant;

public class ExcelCell implements Cell
{
    private final HSSFCell cell;

    private final HSSFCellStyle dateFormat;

    public ExcelCell ( final HSSFRow row, final int column, final HSSFCellStyle dateFormat )
    {
        this.cell = row.createCell ( column );

        this.dateFormat = dateFormat;
    }

    public HSSFCell getCell ()
    {
        return this.cell;
    }

    @Override
    public void setDataAsDate ( final Date date )
    {
        this.cell.setCellValue ( date );
        this.cell.setCellStyle ( this.dateFormat );
    }

    @Override
    public void setDataAsText ( final String text )
    {
        this.cell.setCellValue ( text );
    }

    @Override
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
            this.cell.setCellValue ( variant.asDouble ( 0.0 ) );
            break;
        case BOOLEAN:
            this.cell.setCellValue ( variant.asBoolean () );
            break;
        case NULL:
            break;
        case STRING:
            this.cell.setCellValue ( variant.asString ( "" ) );//$NON-NLS-1$
            break;
        }
    }

}