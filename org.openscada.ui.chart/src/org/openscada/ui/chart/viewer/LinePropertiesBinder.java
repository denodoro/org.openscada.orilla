package org.openscada.ui.chart.viewer;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinePropertiesBinder
{

    private final static Logger logger = LoggerFactory.getLogger ( LinePropertiesBinder.class );

    public static Collection<Binding> bind ( final Realm realm, final DataBindingContext dbc, final IObservableValue lineInputObservable, final IObservableValue linePropertiesObservable )
    {
        final Collection<Binding> result = new LinkedList<Binding> ();

        try
        {
            result.add ( dbc.bindValue ( PojoObservables.observeDetailValue ( lineInputObservable, "lineWidth", null ), EMFObservables.observeDetailValue ( realm, linePropertiesObservable, ChartPackage.Literals.LINE_PROPERTIES__WIDTH ) ) );
            result.add ( dbc.bindValue ( PojoObservables.observeDetailValue ( lineInputObservable, "lineColor", null ), EMFObservables.observeDetailValue ( realm, linePropertiesObservable, ChartPackage.Literals.LINE_PROPERTIES__COLOR ) ) );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to bind line properties", e );
        }

        return result;
    }
}
