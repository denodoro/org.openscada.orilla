package org.openscada.da.client.dataitem.details.part.sum;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.core.Variant;

public class SumEntryLabelProvider extends CellLabelProvider
{

    @Override
    public void update ( final ViewerCell cell )
    {
        final SumEntry element = (SumEntry)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            final String desc = element.getDescription ();
            final String attr = element.getAttributeName ();
            if ( desc != null )
            {
                cell.setText ( desc );
            }
            else
            {
                cell.setText ( attr );
            }
            break;
        case 1:
            final Variant value = element.getValue ();
            if ( value == null )
            {
                cell.setText ( Messages.SumEntryLabelProvider_NullText );
            }
            else if ( value.isBoolean () )
            {
                cell.setText ( value.asBoolean () ? Messages.SumEntryLabelProvider_ActiveText : Messages.SumEntryLabelProvider_InactiveText );
            }
            else
            {
                final StyledString str = new StyledString ();
                str.append ( value.asBoolean () ? Messages.SumEntryLabelProvider_ActiveText : Messages.SumEntryLabelProvider_InactiveText );
                str.append ( ' ' );
                str.append ( value.toString (), StyledString.QUALIFIER_STYLER );
                cell.setText ( str.getString () );
                cell.setStyleRanges ( str.getStyleRanges () );
            }
            break;
        }
    }
}
