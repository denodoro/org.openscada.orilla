package org.openscada.ae.ui.views.config;

import java.util.Map;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.openscada.ae.ui.views.views.LabelProviderSupport;
import org.openscada.ae.ui.views.views.table.EntryTimestampLabelProvider;
import org.openscada.ae.ui.views.views.table.IdLabelProvider;
import org.openscada.ae.ui.views.views.table.SourceTimestampLabelProvider;
import org.openscada.ae.ui.views.views.table.VariantLabelProvider;
import org.openscada.ae.ui.views.views.table.VariantLabelProvider.Decoration;

public class ColumnLabelProviderInformation
{
    private final String label;

    private final String type;

    private final Map<String, String> parameters;

    private final boolean sortable;

    private final int initialSize;

    public static final String TYPE_ID = "id";

    public static final String TYPE_SOURCE_TIMESTAMP = "sourceTimestamp";

    public static final String TYPE_ENTRY_TIMESTAMP = "entryTimestamp";

    public static final String TYPE_VARIANT = "variant";

    public ColumnLabelProviderInformation ( final String label, final String type, final boolean sortable, final int initialSize, final Map<String, String> parameters )
    {
        this.label = label;
        this.type = type;
        this.parameters = parameters;
        this.sortable = sortable;
        this.initialSize = initialSize;
    }

    public int getInitialSize ()
    {
        return this.initialSize;
    }

    public String getLabel ()
    {
        return this.label;
    }

    public Map<String, String> getParameters ()
    {
        return this.parameters;
    }

    public String getType ()
    {
        return this.type;
    }

    public boolean isSortable ()
    {
        return this.sortable;
    }

    public CellLabelProvider createLabelProvider ( final LabelProviderSupport labelProviderSupport )
    {
        if ( TYPE_ID.equals ( this.type ) )
        {
            return new IdLabelProvider ();
        }
        else if ( TYPE_SOURCE_TIMESTAMP.equals ( this.type ) )
        {
            return new SourceTimestampLabelProvider ( labelProviderSupport );
        }
        else if ( TYPE_ENTRY_TIMESTAMP.equals ( this.type ) )
        {
            return new EntryTimestampLabelProvider ( labelProviderSupport );
        }
        else if ( TYPE_VARIANT.equals ( this.type ) )
        {
            final String key = this.parameters.get ( "key" );
            final String decorationString = this.parameters.get ( "decoration" );

            Decoration decoration;
            if ( decorationString == null || decorationString.isEmpty () )
            {
                decoration = null;
            }
            else
            {
                decoration = Decoration.valueOf ( decorationString );
            }

            return new VariantLabelProvider ( key, labelProviderSupport, decoration );
        }
        return null;
    }
}
