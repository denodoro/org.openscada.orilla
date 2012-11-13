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
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.YAxis;

/**
 * This is the item provider adapter for a {@link org.openscada.ui.chart.model.ChartModel.YAxis} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class YAxisItemProvider extends AxisItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public YAxisItemProvider ( AdapterFactory adapterFactory )
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

            addMinimumPropertyDescriptor ( object );
            addMaximumPropertyDescriptor ( object );
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Minimum feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addMinimumPropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_YAxis_minimum_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_YAxis_minimum_feature", "_UI_YAxis_type" ), ChartPackage.Literals.YAXIS__MINIMUM, true, false, false, ItemPropertyDescriptor.REAL_VALUE_IMAGE, null, null ) );
    }

    /**
     * This adds a property descriptor for the Maximum feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addMaximumPropertyDescriptor ( Object object )
    {
        itemPropertyDescriptors.add ( createItemPropertyDescriptor ( ( (ComposeableAdapterFactory)adapterFactory ).getRootAdapterFactory (), getResourceLocator (), getString ( "_UI_YAxis_maximum_feature" ), getString ( "_UI_PropertyDescriptor_description", "_UI_YAxis_maximum_feature", "_UI_YAxis_type" ), ChartPackage.Literals.YAXIS__MAXIMUM, true, false, false, ItemPropertyDescriptor.REAL_VALUE_IMAGE, null, null ) );
    }

    /**
     * This returns YAxis.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage ( Object object )
    {
        return overlayImage ( object, getResourceLocator ().getImage ( "full/obj16/YAxis" ) );
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
        String label = ( (YAxis)object ).getLabel ();
        return label == null || label.length () == 0 ? getString ( "_UI_YAxis_type" ) : getString ( "_UI_YAxis_type" ) + " " + label;
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

        switch ( notification.getFeatureID ( YAxis.class ) )
        {
            case ChartPackage.YAXIS__MINIMUM:
            case ChartPackage.YAXIS__MAXIMUM:
                fireNotifyChanged ( new ViewerNotification ( notification, notification.getNotifier (), false, true ) );
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
    }

}
