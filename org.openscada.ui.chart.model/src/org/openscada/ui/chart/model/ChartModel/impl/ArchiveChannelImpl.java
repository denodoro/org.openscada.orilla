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
 * <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl#getName <em>Name</em>}</li>
 * <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl#getLabel <em>Label</em>}</li>
 * <li>{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl#getLineProperties <em>Line Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ArchiveChannelImpl extends EObjectImpl implements ArchiveChannel
{
    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getLabel() <em>Label</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected String label = LABEL_EDEFAULT;

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
    protected ArchiveChannelImpl ()
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
        return ChartPackage.Literals.ARCHIVE_CHANNEL;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getName ()
    {
        return this.name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setName ( final String newName )
    {
        final String oldName = this.name;
        this.name = newName;
        if ( eNotificationRequired () )
        {
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__NAME, oldName, this.name ) );
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getLabel ()
    {
        return this.label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setLabel ( final String newLabel )
    {
        final String oldLabel = this.label;
        this.label = newLabel;
        if ( eNotificationRequired () )
        {
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__LABEL, oldLabel, this.label ) );
        }
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
            final ENotificationImpl notification = new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, oldLineProperties, newLineProperties );
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
                msgs = ( (InternalEObject)this.lineProperties ).eInverseRemove ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, null, msgs );
            }
            if ( newLineProperties != null )
            {
                msgs = ( (InternalEObject)newLineProperties ).eInverseAdd ( this, EOPPOSITE_FEATURE_BASE - ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, null, msgs );
            }
            msgs = basicSetLineProperties ( newLineProperties, msgs );
            if ( msgs != null )
            {
                msgs.dispatch ();
            }
        }
        else if ( eNotificationRequired () )
        {
            eNotify ( new ENotificationImpl ( this, Notification.SET, ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES, newLineProperties, newLineProperties ) );
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
            case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
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
     * 
     * @generated
     */
    @Override
    public void eSet ( final int featureID, final Object newValue )
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
     * 
     * @generated
     */
    @Override
    public void eUnset ( final int featureID )
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
     * 
     * @generated
     */
    @Override
    public boolean eIsSet ( final int featureID )
    {
        switch ( featureID )
        {
            case ChartPackage.ARCHIVE_CHANNEL__NAME:
                return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals ( this.name );
            case ChartPackage.ARCHIVE_CHANNEL__LABEL:
                return LABEL_EDEFAULT == null ? this.label != null : !LABEL_EDEFAULT.equals ( this.label );
            case ChartPackage.ARCHIVE_CHANNEL__LINE_PROPERTIES:
                return this.lineProperties != null;
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
        result.append ( " (name: " );
        result.append ( this.name );
        result.append ( ", label: " );
        result.append ( this.label );
        result.append ( ')' );
        return result.toString ();
    }

} //ArchiveChannelImpl
