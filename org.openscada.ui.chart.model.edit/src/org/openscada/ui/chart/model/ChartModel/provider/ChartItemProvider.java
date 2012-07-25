/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;

/**
 * This is the item provider adapter for a {@link org.openscada.ui.chart.model.ChartModel.Chart} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ChartItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChartItemProvider ( AdapterFactory adapterFactory )
    {
        super ( adapterFactory );
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors ( Object object )
    {
        if ( itemPropertyDescriptors == null )
        {
            super.getPropertyDescriptors ( object );

            addTitlePropertyDescriptor ( object );
            addShowCurrenTimeRulerPropertyDescriptor ( object );
            addBackgroundColorPropertyDescriptor ( object );
            addSelectedYAxisPropertyDescriptor ( object );
            addSelectedXAxisPropertyDescriptor ( object );
            addMutablePropertyDescriptor ( object );
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Title feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTitlePropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_Chart_title_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_Chart_title_feature", "_UI_Chart_type" ), ChartPackage.Literals.CHART__TITLE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null ) );
    }

    /**
     * This adds a property descriptor for the Show Curren Time Ruler feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addShowCurrenTimeRulerPropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_Chart_showCurrenTimeRuler_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_Chart_showCurrenTimeRuler_feature", "_UI_Chart_type" ), ChartPackage.Literals.CHART__SHOW_CURREN_TIME_RULER, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null ) );
    }

    /**
     * This adds a property descriptor for the Background Color feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addBackgroundColorPropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_Chart_backgroundColor_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_Chart_backgroundColor_feature", "_UI_Chart_type" ), ChartPackage.Literals.CHART__BACKGROUND_COLOR, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null ) );
    }

    /**
     * This adds a property descriptor for the Selected YAxis feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addSelectedYAxisPropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_Chart_selectedYAxis_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_Chart_selectedYAxis_feature", "_UI_Chart_type" ), ChartPackage.Literals.CHART__SELECTED_YAXIS, true, false, true, null, null, null ) );
    }

    /**
     * This adds a property descriptor for the Selected XAxis feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addSelectedXAxisPropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_Chart_selectedXAxis_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_Chart_selectedXAxis_feature", "_UI_Chart_type" ), ChartPackage.Literals.CHART__SELECTED_XAXIS, true, false, true, null, null, null ) );
    }

    /**
     * This adds a property descriptor for the Mutable feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addMutablePropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_Chart_mutable_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_Chart_mutable_feature", "_UI_Chart_type" ), ChartPackage.Literals.CHART__MUTABLE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null ) );
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures ( Object object )
    {
        if ( childrenFeatures == null )
        {
            super.getChildrenFeatures ( object );
            childrenFeatures.add ( ChartPackage.Literals.CHART__BOTTOM );
            childrenFeatures.add ( ChartPackage.Literals.CHART__TOP );
            childrenFeatures.add ( ChartPackage.Literals.CHART__LEFT );
            childrenFeatures.add ( ChartPackage.Literals.CHART__RIGHT );
            childrenFeatures.add ( ChartPackage.Literals.CHART__INPUTS );
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature ( Object object, Object child )
    {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature ( object, child );
    }

    /**
     * This returns Chart.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage ( Object object )
    {
        return overlayImage ( object, getResourceLocator ().getImage ( "full/obj16/Chart" ) );
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText ( Object object )
    {
        String label = ( (Chart)object ).getTitle ();
        return label == null || label.length () == 0 ? getString ( "_UI_Chart_type" ) : getString ( "_UI_Chart_type" ) + " " + label;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged ( Notification notification )
    {
        updateChildren ( notification );

        switch ( notification.getFeatureID ( Chart.class ) )
        {
            case ChartPackage.CHART__TITLE:
            case ChartPackage.CHART__SHOW_CURREN_TIME_RULER:
            case ChartPackage.CHART__BACKGROUND_COLOR:
            case ChartPackage.CHART__MUTABLE:
                fireNotifyChanged ( new ViewerNotification ( notification, notification.getNotifier (), false, true ) );
                return;
            case ChartPackage.CHART__BOTTOM:
            case ChartPackage.CHART__TOP:
            case ChartPackage.CHART__LEFT:
            case ChartPackage.CHART__RIGHT:
            case ChartPackage.CHART__INPUTS:
                fireNotifyChanged ( new ViewerNotification ( notification, notification.getNotifier (), true, false ) );
                return;
        }
        super.notifyChanged ( notification );
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors ( Collection<Object> newChildDescriptors, Object object )
    {
        super.collectNewChildDescriptors ( newChildDescriptors, object );

        newChildDescriptors.add ( createChildParameter ( ChartPackage.Literals.CHART__BOTTOM, ChartFactory.eINSTANCE.createXAxis () ) );

        newChildDescriptors.add ( createChildParameter ( ChartPackage.Literals.CHART__TOP, ChartFactory.eINSTANCE.createXAxis () ) );

        newChildDescriptors.add ( createChildParameter ( ChartPackage.Literals.CHART__LEFT, ChartFactory.eINSTANCE.createYAxis () ) );

        newChildDescriptors.add ( createChildParameter ( ChartPackage.Literals.CHART__RIGHT, ChartFactory.eINSTANCE.createYAxis () ) );

        newChildDescriptors.add ( createChildParameter ( ChartPackage.Literals.CHART__INPUTS, ChartFactory.eINSTANCE.createDataItemSeries () ) );

        newChildDescriptors.add ( createChildParameter ( ChartPackage.Literals.CHART__INPUTS, ChartFactory.eINSTANCE.createArchiveSeries () ) );
    }

    /**
     * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getCreateChildText ( Object owner, Object feature, Object child, Collection<?> selection )
    {
        Object childFeature = feature;
        Object childObject = child;

        boolean qualify = childFeature == ChartPackage.Literals.CHART__BOTTOM || childFeature == ChartPackage.Literals.CHART__TOP || childFeature == ChartPackage.Literals.CHART__LEFT || childFeature == ChartPackage.Literals.CHART__RIGHT;

        if ( qualify )
        {
            return getString ( "_UI_CreateChild_text2", new Object[] { getTypeText ( childObject ), getFeatureText ( childFeature ), getTypeText ( owner ) } );
        }
        return super.getCreateChildText ( owner, feature, child, selection );
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator ()
    {
        return ChartEditPlugin.INSTANCE;
    }

}
