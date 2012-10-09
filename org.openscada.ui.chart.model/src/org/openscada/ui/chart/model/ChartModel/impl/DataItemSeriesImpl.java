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
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl#getLineProperties <em>Line Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataItemSeriesImpl extends ItemDataSeriesImpl implements DataItemSeries
{
    /**
     * The cached value of the '{@link #getLineProperties() <em>Line Properties</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
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
        setLineProperties ( ChartFactory.eINSTANCE.createLineProperties () );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass ()
    {
        return ChartPackage.Literals.DATA_ITEM_SERIES;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public LineProperties getLineProperties ()
    {
        return lineProperties;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLineProperties ( LineProperties newLineProperties, NotificationChain msgs )
    {
        LineProperties oldLineProperties = lineProperties;
        lineProperties = newLineProperties;
        if ( eNotificationRequired () )
        {
            ENotificationImpl notification = new ENotificationImpl ( this, Notification.SET, ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, oldLineProperties, newLineProperties );
            if ( msgs == null )
                msgs = notification;
            else
                msgs.add ( notification );
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLineProperties ( LineProperties newLineProperties )
    {
        if ( newLineProperties != lineProperties )
        {
            NotificationChain msgs = null;
            if ( lineProperties != null )
                msgs = ( (InternalEObject)lineProperties ).eInverseRemove ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, null, msgs );
            if ( newLineProperties != null )
                msgs = ( (InternalEObject)newLineProperties ).eInverseAdd ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, null, msgs );
            msgs = basicSetLineProperties ( newLineProperties, msgs );
            if ( msgs != null )
                msgs.dispatch ();
        }
        else if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES, newLineProperties, newLineProperties ) );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove ( InternalEObject otherEnd, int featureID, NotificationChain msgs )
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
     * @generated
     */
    @Override
    public Object eGet ( int featureID, boolean resolve, boolean coreType )
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
     * @generated
     */
    @Override
    public void eSet ( int featureID, Object newValue )
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
     * @generated
     */
    @Override
    public void eUnset ( int featureID )
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
     * @generated
     */
    @Override
    public boolean eIsSet ( int featureID )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_PROPERTIES:
                return lineProperties != null;
        }
        return super.eIsSet ( featureID );
    }

} //DataItemSeriesImpl
