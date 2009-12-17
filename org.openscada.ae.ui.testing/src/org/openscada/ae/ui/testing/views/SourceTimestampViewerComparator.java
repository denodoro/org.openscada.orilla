/**
 * 
 */
package org.openscada.ae.ui.testing.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.openscada.ae.Event;

final class SourceTimestampViewerComparator extends ViewerComparator
{
    @Override
    public int compare ( final Viewer viewer, final Object e1, final Object e2 )
    {
        if ( ! ( e1 instanceof Event ) || ! ( e2 instanceof Event ) )
        {
            return -super.compare ( viewer, e1, e2 );
        }
        final Event evt1 = (Event)e1;
        final Event evt2 = (Event)e2;

        return -evt1.getSourceTimestamp ().compareTo ( evt2.getSourceTimestamp () );
    }
}