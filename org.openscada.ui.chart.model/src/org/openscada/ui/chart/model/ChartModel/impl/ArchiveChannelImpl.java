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
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.openscada.ui.chart.model.ChartModel.ArchiveChannel;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.LineProperties;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Archive Channel</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl#getLineProperties <em>Line Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArchiveChannelImpl extends EObjectImpl implements ArchiveChannel
{
    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected String label = LABEL_EDEFAULT;

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
    protected ArchiveChannelImpl ()
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
        return ChartPackage.Literals.ARCHIVE_CHANNEL;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getName ()
    {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setName ( String newName )
    {
        String oldName = name;
        name = newName;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__NAME, oldName, name ) );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getLabel ()
    {
        return label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLabel ( String newLabel )
    {
        String oldLabel = label;
        label = newLabel;
        if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__LABEL, oldLabel, label ) );
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
            ENotificationImpl notification = new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, oldLineProperties, newLineProperties );
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
                msgs = ( (InternalEObject)lineProperties ).eInverseRemove ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, null, msgs );
            if ( newLineProperties != null )
                msgs = ( (InternalEObject)newLineProperties ).eInverseAdd ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, null, msgs );
            msgs = basicSetLineProperties ( newLineProperties, msgs );
            if ( msgs != null )
                msgs.dispatch ();
        }
        else if ( eNotificationRequired () )
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, newLineProperties, newLineProperties ) );
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
        case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
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
        case ChartPackage.ARCHIVE_CHANNEL__NAME:
            return getName ();
        case ChartPackage.ARCHIVE_CHANNEL__LABEL:
            return getLabel ();
        case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
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
        case ChartPackage.ARCHIVE_CHANNEL__NAME:
            setName ( (String)newValue );
            return;
        case ChartPackage.ARCHIVE_CHANNEL__LABEL:
            setLabel ( (String)newValue );
            return;
        case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
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
        case ChartPackage.ARCHIVE_CHANNEL__NAME:
            setName ( NAME_EDEFAULT );
            return;
        case ChartPackage.ARCHIVE_CHANNEL__LABEL:
            setLabel ( LABEL_EDEFAULT );
            return;
        case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
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
        case ChartPackage.ARCHIVE_CHANNEL__NAME:
            return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals ( name );
        case ChartPackage.ARCHIVE_CHANNEL__LABEL:
            return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals ( label );
        case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
            return lineProperties != null;
        }
        return super.eIsSet ( featureID );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString ()
    {
        if ( eIsProxy () )
            return super.toString ();

        StringBuffer result = new StringBuffer ( super.toString () );
        result.append ( " (name: " );
        result.append ( name );
        result.append ( ", label: " );
        result.append ( label );
        result.append ( ')' );
        return result.toString ();
    }

} //ArchiveChannelImpl
