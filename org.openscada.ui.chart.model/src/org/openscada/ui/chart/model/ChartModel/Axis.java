/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.RGB;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Axis</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.Axis#getLabel <em>Label</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.Axis#getTextPadding <em>Text Padding</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.Axis#getColor <em>Color</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.Axis#isLabelVisible <em>Label Visible</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.Axis#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getAxis()
 * @model abstract="true"
 * @generated
 */
public interface Axis extends EObject
{
    /**
     * Returns the value of the '<em><b>Text Padding</b></em>' attribute.
     * The default value is <code>"10"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Padding</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Padding</em>' attribute.
     * @see #setTextPadding(int)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getAxis_TextPadding()
     * @model default="10" required="true"
     * @generated
     */
    int getTextPadding ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.Axis#getTextPadding <em>Text Padding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Padding</em>' attribute.
     * @see #getTextPadding()
     * @generated
     */
    void setTextPadding ( int value );

    /**
     * Returns the value of the '<em><b>Color</b></em>' attribute.
     * The default value is <code>"#000000"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Color</em>' attribute.
     * @see #setColor(RGB)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getAxis_Color()
     * @model default="#000000" dataType="org.openscada.ui.chart.model.ChartModel.RGB"
     * @generated
     */
    RGB getColor ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.Axis#getColor <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Color</em>' attribute.
     * @see #getColor()
     * @generated
     */
    void setColor ( RGB value );

    /**
     * Returns the value of the '<em><b>Label Visible</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label Visible</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Label Visible</em>' attribute.
     * @see #setLabelVisible(boolean)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getAxis_LabelVisible()
     * @model default="true" required="true"
     * @generated
     */
    boolean isLabelVisible ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.Axis#isLabelVisible <em>Label Visible</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label Visible</em>' attribute.
     * @see #isLabelVisible()
     * @generated
     */
    void setLabelVisible ( boolean value );

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getAxis_Format()
     * @model
     * @generated
     */
    String getFormat ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.Axis#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat ( String value );

    /**
     * Returns the value of the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Label</em>' attribute.
     * @see #setLabel(String)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getAxis_Label()
     * @model extendedMetaData="name='label'"
     * @generated
     */
    String getLabel ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.Axis#getLabel <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label</em>' attribute.
     * @see #getLabel()
     * @generated
     */
    void setLabel ( String value );

} // Axis
