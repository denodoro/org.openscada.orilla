/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.LineProperties;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Data Item Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl#getLineProperties <em>Line Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DataItemSeriesImpl extends ItemDataSeriesImpl implements DataItemSeries
{
    /**
     * The cached value of the '{@link #getLineProperties() <em>Line Properties</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getLineProperties()
     * @generated
     * @ordered
     */
    protected LineProperties lineProperties;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected DataItemSeriesImpl ()
    {
        super ();
        this.lineProperties = ChartFactory.eINSTANCE.createLineProperties ();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass ()
    {
        return ChartPackage.Literals.DATA_ITEM_SERIES;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public LineProperties getLineProperties ()
    {
        return this.lineProperties;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetLineProperties ( final LineProperties newLineProperties, NotificationChain msgs )
    {
        final LineProperties oldLineProperties = this.lineProperties;
        this.lineProperties = newLineProperties;
        if ( eNotificationRequired () )
        {
            final ENotificationImpl notification = new ENotificationImpl ( this, Notification.SET, ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, oldLineProperties, newLineProperties );
            if ( msgs == null )
            {
                msgs = notification;
            }
            else
            {
                msgs.add ( notification );
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setLineProperties ( final LineProperties newLineProperties )
    {
        if ( newLineProperties != this.lineProperties )
        {
            NotificationChain msgs = null;
            if ( this.lineProperties != null )
            {
                msgs = ( (InternalEObject)this.lineProperties ).eInverseRemove ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, null, msgs );
            }
            if ( newLineProperties != null )
            {
                msgs = ( (InternalEObject)newLineProperties ).eInverseAdd ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, null, msgs );
            }
            msgs = basicSetLineProperties ( newLineProperties, msgs );
            if ( msgs != null )
            {
                msgs.dispatch ();
            }
        }
        else if ( eNotificationRequired () )
        {
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, newLineProperties, newLineProperties ) );
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove ( final InternalEObject otherEnd, final int featureID, final NotificationChain msgs )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES:
                return basicSetLineProperties ( null, msgs );
        }
        return super.eInverseRemove ( otherEnd, featureID, msgs );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet ( final int featureID, final boolean resolve, final boolean coreType )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES:
                return getLineProperties ();
        }
        return super.eGet ( featureID, resolve, coreType );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet ( final int featureID, final Object newValue )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES:
                setLineProperties ( (LineProperties)newValue );
                return;
        }
        super.eSet ( featureID, newValue );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset ( final int featureID )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES:
                setLineProperties ( (LineProperties)null );
                return;
        }
        super.eUnset ( featureID );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet ( final int featureID )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES:
                return this.lineProperties != null;
        }
        return super.eIsSet ( featureID );
    }

} //DataItemSeriesImpl
