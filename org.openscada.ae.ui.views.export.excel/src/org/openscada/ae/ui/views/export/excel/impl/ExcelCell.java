/**
 * 
 */
package org.openscada.ae.ui.views.export.excel.impl;

import java.util.Date;

import jxl.biff.EmptyCell;
import jxl.write.Boolean;
import jxl.write.DateFormat;
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

    private final static DateFormat customDateFormat = new DateFormat ( "yyyy-MM-dd hh:mm:ss.SSS" );

    private final static WritableCellFormat dateFormat = new WritableCellFormat ( customDateFormat );

    public ExcelCell ( final int row, final int column )
    {
        this.row = row;
        this.column = column;
    }

    public jxl.write.WritableCell getCell ()
    {
        return this.cell;
    }

    public void setDataAsDate ( final Date date )
    {
        this.cell = new DateTime ( this.column, this.row, date, ExcelCell.dateFormat );
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
            setDataAsText ( variant.asString ( "" ) );
            break;
        }
    }

}