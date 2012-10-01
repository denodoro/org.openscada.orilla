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
import org.openscada.ui.chart.model.ChartModel.LineProperties;
import org.openscada.ui.chart.model.ChartModel.ScriptSeries;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Script Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ScriptSeriesImpl#getLineProperties <em>Line Properties</em>}</li>
 * <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ScriptSeriesImpl#getScript <em>Script</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ScriptSeriesImpl extends DataSeriesImpl implements ScriptSeries
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
     * The default value of the '{@link #getScript() <em>Script</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getScript()
     * @generated
     * @ordered
     */
    protected static final String SCRIPT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getScript() <em>Script</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getScript()
     * @generated
     * @ordered
     */
    protected String script = SCRIPT_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected ScriptSeriesImpl ()
    {
        super ();
        setLineProperties ( ChartFactory.eINSTANCE.createLineProperties () );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass ()
    {
        return ChartPackage.Literals.SCRIPT_SERIES;
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
            final ENotificationImpl notification = new ENotificationImpl ( this, Notification.SET, ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES, oldLineProperties, newLineProperties );
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
                msgs = ( (InternalEObject)this.lineProperties ).eInverseRemove ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES, null, msgs );
            }
            if ( newLineProperties != null )
            {
                msgs = ( (InternalEObject)newLineProperties ).eInverseAdd ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES, null, msgs );
            }
            msgs = basicSetLineProperties ( newLineProperties, msgs );
            if ( msgs != null )
            {
                msgs.dispatch ();
            }
        }
        else if ( eNotificationRequired () )
        {
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES, newLineProperties, newLineProperties ) );
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getScript ()
    {
        return this.script;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setScript ( final String newScript )
    {
        final String oldScript = this.script;
        this.script = newScript;
        if ( eNotificationRequired () )
        {
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.SCRIPT_SERIES__SCRIPT, oldScript, this.script ) );
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
            case ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES:
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
            case ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES:
                return getLineProperties ();
            case ChartPackage.SCRIPT_SERIES__SCRIPT:
                return getScript ();
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
            case ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES:
                setLineProperties ( (LineProperties)newValue );
                return;
            case ChartPackage.SCRIPT_SERIES__SCRIPT:
                setScript ( (String)newValue );
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
            case ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES:
                setLineProperties ( (LineProperties)null );
                return;
            case ChartPackage.SCRIPT_SERIES__SCRIPT:
                setScript ( SCRIPT_EDEFAULT );
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
            case ChartPackage.SCRIPT_SERIES__LINE_PROPERTIES:
                return this.lineProperties != null;
            case ChartPackage.SCRIPT_SERIES__SCRIPT:
                return SCRIPT_EDEFAULT == null ? this.script != null : !SCRIPT_EDEFAULT.equals ( this.script );
        }
        return super.eIsSet ( featureID );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString ()
    {
        if ( eIsProxy () )
        {
            return super.toString ();
        }

        final StringBuffer result = new StringBuffer ( super.toString () );
        result.append ( " (script: " );
        result.append ( this.script );
        result.append ( ')' );
        return result.toString ();
    }

} //ScriptSeriesImpl
