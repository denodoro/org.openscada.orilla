/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.openscada.ui.chart.model.ChartModel.util.ChartAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ChartItemProviderAdapterFactory extends ChartAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable
{
    /**
     * This keeps track of the root adapter factory that delegates to this adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComposedAdapterFactory parentAdapterFactory;

    /**
     * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IChangeNotifier changeNotifier = new ChangeNotifier ();

    /**
     * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Collection<Object> supportedTypes = new ArrayList<Object> ();

    /**
     * This constructs an instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChartItemProviderAdapterFactory ()
    {
        supportedTypes.add ( IEditingDomainItemProvider.class );
        supportedTypes.add ( IStructuredItemContentProvider.class );
        supportedTypes.add ( ITreeItemContentProvider.class );
        supportedTypes.add ( IItemLabelProvider.class );
        supportedTypes.add ( IItemPropertySource.class );
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.Chart} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ChartItemProvider chartItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.Chart}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createChartAdapter ()
    {
        if ( chartItemProvider == null )
        {
            chartItemProvider = new ChartItemProvider ( this );
        }

        return chartItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.XAxis} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XAxisItemProvider xAxisItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.XAxis}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createXAxisAdapter ()
    {
        if ( xAxisItemProvider == null )
        {
            xAxisItemProvider = new XAxisItemProvider ( this );
        }

        return xAxisItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.YAxis} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected YAxisItemProvider yAxisItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.YAxis}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createYAxisAdapter ()
    {
        if ( yAxisItemProvider == null )
        {
            yAxisItemProvider = new YAxisItemProvider ( this );
        }

        return yAxisItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.DataItemSeries} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DataItemSeriesItemProvider dataItemSeriesItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.DataItemSeries}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createDataItemSeriesAdapter ()
    {
        if ( dataItemSeriesItemProvider == null )
        {
            dataItemSeriesItemProvider = new DataItemSeriesItemProvider ( this );
        }

        return dataItemSeriesItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.ArchiveSeries} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArchiveSeriesItemProvider archiveSeriesItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.ArchiveSeries}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createArchiveSeriesAdapter ()
    {
        if ( archiveSeriesItemProvider == null )
        {
            archiveSeriesItemProvider = new ArchiveSeriesItemProvider ( this );
        }

        return archiveSeriesItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.UriItem} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UriItemItemProvider uriItemItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.UriItem}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createUriItemAdapter ()
    {
        if ( uriItemItemProvider == null )
        {
            uriItemItemProvider = new UriItemItemProvider ( this );
        }

        return uriItemItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.IdItem} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IdItemItemProvider idItemItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.IdItem}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createIdItemAdapter ()
    {
        if ( idItemItemProvider == null )
        {
            idItemItemProvider = new IdItemItemProvider ( this );
        }

        return idItemItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArchiveChannelItemProvider archiveChannelItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createArchiveChannelAdapter ()
    {
        if ( archiveChannelItemProvider == null )
        {
            archiveChannelItemProvider = new ArchiveChannelItemProvider ( this );
        }

        return archiveChannelItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.LineProperties} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LinePropertiesItemProvider linePropertiesItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.LineProperties}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createLinePropertiesAdapter ()
    {
        if ( linePropertiesItemProvider == null )
        {
            linePropertiesItemProvider = new LinePropertiesItemProvider ( this );
        }

        return linePropertiesItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.ScriptSeries} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScriptSeriesItemProvider scriptSeriesItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.ScriptSeries}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createScriptSeriesAdapter ()
    {
        if ( scriptSeriesItemProvider == null )
        {
            scriptSeriesItemProvider = new ScriptSeriesItemProvider ( this );
        }

        return scriptSeriesItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController} instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CurrentTimeControllerItemProvider currentTimeControllerItemProvider;

    /**
     * This creates an adapter for a {@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter createCurrentTimeControllerAdapter ()
    {
        if ( currentTimeControllerItemProvider == null )
        {
            currentTimeControllerItemProvider = new CurrentTimeControllerItemProvider ( this );
        }

        return currentTimeControllerItemProvider;
    }

    /**
     * This returns the root adapter factory that contains this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComposeableAdapterFactory getRootAdapterFactory ()
    {
        return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory ();
    }

    /**
     * This sets the composed adapter factory that contains this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParentAdapterFactory ( ComposedAdapterFactory parentAdapterFactory )
    {
        this.parentAdapterFactory = parentAdapterFactory;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isFactoryForType ( Object type )
    {
        return supportedTypes.contains ( type ) || super.isFactoryForType ( type );
    }

    /**
     * This implementation substitutes the factory itself as the key for the adapter.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Adapter adapt ( Notifier notifier, Object type )
    {
        return super.adapt ( notifier, this );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object adapt ( Object object, Object type )
    {
        if ( isFactoryForType ( type ) )
        {
            Object adapter = super.adapt ( object, type );
            if ( ! ( type instanceof Class<?> ) || ( ( (Class<?>)type ).isInstance ( adapter ) ) )
            {
                return adapter;
            }
        }

        return null;
    }

    /**
     * This adds a listener.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void addListener ( INotifyChangedListener notifyChangedListener )
    {
        changeNotifier.addListener ( notifyChangedListener );
    }

    /**
     * This removes a listener.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void removeListener ( INotifyChangedListener notifyChangedListener )
    {
        changeNotifier.removeListener ( notifyChangedListener );
    }

    /**
     * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void fireNotifyChanged ( Notification notification )
    {
        changeNotifier.fireNotifyChanged ( notification );

        if ( parentAdapterFactory != null )
        {
            parentAdapterFactory.fireNotifyChanged ( notification );
        }
    }

    /**
     * This disposes all of the item providers created by this factory. 
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void dispose ()
    {
        if ( chartItemProvider != null )
            chartItemProvider.dispose ();
        if ( xAxisItemProvider != null )
            xAxisItemProvider.dispose ();
        if ( yAxisItemProvider != null )
            yAxisItemProvider.dispose ();
        if ( dataItemSeriesItemProvider != null )
            dataItemSeriesItemProvider.dispose ();
        if ( archiveSeriesItemProvider != null )
            archiveSeriesItemProvider.dispose ();
        if ( uriItemItemProvider != null )
            uriItemItemProvider.dispose ();
        if ( idItemItemProvider != null )
            idItemItemProvider.dispose ();
        if ( archiveChannelItemProvider != null )
            archiveChannelItemProvider.dispose ();
        if ( linePropertiesItemProvider != null )
            linePropertiesItemProvider.dispose ();
        if ( scriptSeriesItemProvider != null )
            scriptSeriesItemProvider.dispose ();
        if ( currentTimeControllerItemProvider != null )
            currentTimeControllerItemProvider.dispose ();
    }

}
