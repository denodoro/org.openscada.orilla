/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.swt.graphics.RGB;
import org.openscada.ui.chart.model.ChartModel.Axis;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Axis</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.AxisImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.AxisImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.AxisImpl#getTextPadding <em>Text Padding</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.AxisImpl#getColor <em>Color</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AxisImpl extends EObjectImpl implements Axis
{
    /**
     * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected String label = LABEL_EDEFAULT;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getTextPadding() <em>Text Padding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPadding()
     * @generated
     * @ordered
     */
    protected static final int TEXT_PADDING_EDEFAULT = 10;

    /**
     * The cached value of the '{@link #getTextPadding() <em>Text Padding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPadding()
     * @generated
     * @ordered
     */
    protected int textPadding = TEXT_PADDING_EDEFAULT;

    /**
     * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected static final RGB COLOR_EDEFAULT = (RGB)ChartFactory.eINSTANCE.createFromString ( ChartPackage.eINSTANCE.getRGB (), "#000000" );

    /**
     * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected RGB color = COLOR_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AxisImpl ()
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
        return ChartPackage.Literals.AXIS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat ()
    {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat ( String newFormat )
    {
        String oldFormat = format;
        format = newFormat;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.AXIS__FORMAT, oldFormat, format ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getTextPadding ()
    {
        return textPadding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTextPadding ( int newTextPadding )
    {
        int oldTextPadding = textPadding;
        textPadding = newTextPadding;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.AXIS__TEXT_PADDING, oldTextPadding, textPadding ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RGB getColor ()
    {
        return color;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setColor ( RGB newColor )
    {
        RGB oldColor = color;
        color = newColor;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.AXIS__COLOR, oldColor, color ) );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLabel ()
    {
        return label;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLabel ( String newLabel )
    {
        String oldLabel = label;
        label = newLabel;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.AXIS__LABEL, oldLabel, label ) );
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
        case ChartPackage.AXIS__LABEL:
            return getLabel ();
        case ChartPackage.AXIS__FORMAT:
            return getFormat ();
        case ChartPackage.AXIS__TEXT_PADDING:
            return getTextPadding ();
        case ChartPackage.AXIS__COLOR:
            return getColor ();
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
        case ChartPackage.AXIS__LABEL:
            setLabel ( (String)newValue );
            return;
        case ChartPackage.AXIS__FORMAT:
            setFormat ( (String)newValue );
            return;
        case ChartPackage.AXIS__TEXT_PADDING:
            setTextPadding ( (Integer)newValue );
            return;
        case ChartPackage.AXIS__COLOR:
            setColor ( (RGB)newValue );
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
        case ChartPackage.AXIS__LABEL:
            setLabel ( LABEL_EDEFAULT );
            return;
        case ChartPackage.AXIS__FORMAT:
            setFormat ( FORMAT_EDEFAULT );
            return;
        case ChartPackage.AXIS__TEXT_PADDING:
            setTextPadding ( TEXT_PADDING_EDEFAULT );
            return;
        case ChartPackage.AXIS__COLOR:
            setColor ( COLOR_EDEFAULT );
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
        case ChartPackage.AXIS__LABEL:
            return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals ( label );
        case ChartPackage.AXIS__FORMAT:
            return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals ( format );
        case ChartPackage.AXIS__TEXT_PADDING:
            return textPadding != TEXT_PADDING_EDEFAULT;
        case ChartPackage.AXIS__COLOR:
            return COLOR_EDEFAULT == null ? color != null : !COLOR_EDEFAULT.equals ( color );
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
        result.append ( " (label: " );
        result.append ( label );
        result.append ( ", format: " );
        result.append ( format );
        result.append ( ", textPadding: " );
        result.append ( textPadding );
        result.append ( ", color: " );
        result.append ( color );
        result.append ( ')' );
        return result.toString ();
    }

} //AxisImpl
