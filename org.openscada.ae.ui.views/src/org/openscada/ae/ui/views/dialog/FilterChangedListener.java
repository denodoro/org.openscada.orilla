package org.openscada.ae.ui.views.dialog;

import org.openscada.utils.lang.Pair;

public interface FilterChangedListener
{
    public void onFilterChanged ( Pair<SearchType, String> filter );

    public void onFilterParseError ( Pair<SearchType, String> error );
}
