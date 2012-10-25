/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.CurrentTimeController;
import org.openscada.ui.chart.model.ChartModel.XAxis;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Current Time Controller</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl#getDiff <em>Diff</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl#getAxis <em>Axis</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl#getAlignDateFormat <em>Align Date Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CurrentTimeControllerImpl extends ControllerImpl implements CurrentTimeController
{
    /**
     * The default value of the '{@link #getDiff() <em>Diff</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDiff()
     * @generated
     * @ordered
     */
    protected static final long DIFF_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getDiff() <em>Diff</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDiff()
     * @generated
     * @ordered
     */
    protected long diff = DIFF_EDEFAULT;

    /**
     * The cached value of the '{@link #getAxis() <em>Axis</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxis()
     * @generated
     * @ordered
     */
    protected XAxis axis;

    /**
     * The default value of the '{@link #getAlignDateFormat() <em>Align Date Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlignDateFormat()
     * @generated
     * @ordered
     */
    protected static final String ALIGN_DATE_FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAlignDateFormat() <em>Align Date Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlignDateFormat()
     * @generated
     * @ordered
     */
    protected String alignDateFormat = ALIGN_DATE_FORMAT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CurrentTimeControllerImpl ()
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
        return ChartPackage.Literals.CURRENT_TIME_CONTROLLER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public long getDiff ()
    {
        return diff;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDiff ( long newDiff )
    {
        long oldDiff = diff;
        diff = newDiff;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.CURRENT_TIME_CONTROLLER__DIFF, oldDiff, diff ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XAxis getAxis ()
    {
        if ( axis != null && axis.eIsProxy () )
        {
            InternalEObject oldAxis = (InternalEObject)axis;
            axis = (XAxis)eResolveProxy ( oldAxis );
            if ( axis != oldAxis )
            {
                if ( eNotificationRequired () )
                    eNotify ( new ENotificationImpl ( this, Notification.RESOLVE, ChartPackage.CURRENT_TIME_CONTROLLER__AXIS, oldAxis, axis ) );
            }
        }
        return axis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XAxis basicGetAxis ()
    {
        return axis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAxis ( XAxis newAxis )
    {
        XAxis oldAxis = axis;
        axis = newAxis;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.CURRENT_TIME_CONTROLLER__AXIS, oldAxis, axis ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAlignDateFormat ()
    {
        return alignDateFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAlignDateFormat ( String newAlignDateFormat )
    {
        String oldAlignDateFormat = alignDateFormat;
        alignDateFormat = newAlignDateFormat;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT, oldAlignDateFormat, alignDateFormat ) );
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
        case ChartPackage.CURRENT_TIME_CONTROLLER__DIFF:
            return getDiff ();
        case ChartPackage.CURRENT_TIME_CONTROLLER__AXIS:
            if ( resolve )
                return getAxis ();
            return basicGetAxis ();
        case ChartPackage.CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT:
            return getAlignDateFormat ();
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
        case ChartPackage.CURRENT_TIME_CONTROLLER__DIFF:
            setDiff ( (Long)newValue );
            return;
        case ChartPackage.CURRENT_TIME_CONTROLLER__AXIS:
            setAxis ( (XAxis)newValue );
            return;
        case ChartPackage.CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT:
            setAlignDateFormat ( (String)newValue );
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
        case ChartPackage.CURRENT_TIME_CONTROLLER__DIFF:
            setDiff ( DIFF_EDEFAULT );
            return;
        case ChartPackage.CURRENT_TIME_CONTROLLER__AXIS:
            setAxis ( (XAxis)null );
            return;
        case ChartPackage.CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT:
            setAlignDateFormat ( ALIGN_DATE_FORMAT_EDEFAULT );
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
        case ChartPackage.CURRENT_TIME_CONTROLLER__DIFF:
            return diff != DIFF_EDEFAULT;
        case ChartPackage.CURRENT_TIME_CONTROLLER__AXIS:
            return axis != null;
        case ChartPackage.CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT:
            return ALIGN_DATE_FORMAT_EDEFAULT == null ? alignDateFormat != null : !ALIGN_DATE_FORMAT_EDEFAULT.equals ( alignDateFormat );
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
        result.append ( " (diff: " );
        result.append ( diff );
        result.append ( ", alignDateFormat: " );
        result.append ( alignDateFormat );
        result.append ( ')' );
        return result.toString ();
    }

} //CurrentTimeControllerImpl
