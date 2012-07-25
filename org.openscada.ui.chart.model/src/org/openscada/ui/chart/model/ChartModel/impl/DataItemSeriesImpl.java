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
import org.eclipse.swt.graphics.RGB;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.Item;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Item Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl#getLineColor <em>Line Color</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl#getLineWidth <em>Line Width</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataItemSeriesImpl extends ItemDataSeriesImpl implements DataItemSeries
{
    /**
     * The default value of the '{@link #getLineColor() <em>Line Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineColor()
     * @generated
     * @ordered
     */
    protected static final RGB LINE_COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLineColor() <em>Line Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineColor()
     * @generated
     * @ordered
     */
    protected RGB lineColor = LINE_COLOR_EDEFAULT;

    /**
     * The default value of the '{@link #getLineWidth() <em>Line Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineWidth()
     * @generated
     * @ordered
     */
    protected static final float LINE_WIDTH_EDEFAULT = 1.0F;

    /**
     * The cached value of the '{@link #getLineWidth() <em>Line Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineWidth()
     * @generated
     * @ordered
     */
    protected float lineWidth = LINE_WIDTH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DataItemSeriesImpl ()
    {
        super ();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass ()
    {
        return ChartPackage.Literals.DATA_ITEM_SERIES;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RGB getLineColor ()
    {
        return lineColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLineColor ( RGB newLineColor )
    {
        RGB oldLineColor = lineColor;
        lineColor = newLineColor;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.DATA_ITEM_SERIES__LINE_COLOR, oldLineColor, lineColor ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public float getLineWidth ()
    {
        return lineWidth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLineWidth ( float newLineWidth )
    {
        float oldLineWidth = lineWidth;
        lineWidth = newLineWidth;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.DATA_ITEM_SERIES__LINE_WIDTH, oldLineWidth, lineWidth ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet ( int featureID, boolean resolve, boolean coreType )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_COLOR:
                return getLineColor ();
            case ChartPackage.DATA_ITEM_SERIES__LINE_WIDTH:
                return getLineWidth ();
        }
        return super.eGet ( featureID, resolve, coreType );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet ( int featureID, Object newValue )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_COLOR:
                setLineColor ( (RGB)newValue );
                return;
            case ChartPackage.DATA_ITEM_SERIES__LINE_WIDTH:
                setLineWidth ( (Float)newValue );
                return;
        }
        super.eSet ( featureID, newValue );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset ( int featureID )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_COLOR:
                setLineColor ( LINE_COLOR_EDEFAULT );
                return;
            case ChartPackage.DATA_ITEM_SERIES__LINE_WIDTH:
                setLineWidth ( LINE_WIDTH_EDEFAULT );
                return;
        }
        super.eUnset ( featureID );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet ( int featureID )
    {
        switch ( featureID )
        {
            case ChartPackage.DATA_ITEM_SERIES__LINE_COLOR:
                return LINE_COLOR_EDEFAULT == null ? lineColor != null : !LINE_COLOR_EDEFAULT.equals ( lineColor );
            case ChartPackage.DATA_ITEM_SERIES__LINE_WIDTH:
                return lineWidth != LINE_WIDTH_EDEFAULT;
        }
        return super.eIsSet ( featureID );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString ()
    {
        if ( eIsProxy () )
            return super.toString ();

        StringBuffer result = new StringBuffer ( super.toString () );
        result.append ( " (lineColor: " );
        result.append ( lineColor );
        result.append ( ", lineWidth: " );
        result.append ( lineWidth );
        result.append ( ')' );
        return result.toString ();
    }

} //DataItemSeriesImpl
