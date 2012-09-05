/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Script Series</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries#getLineProperties <em>Line Properties</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries#getScript <em>Script</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getScriptSeries()
 * @model
 * @generated
 */
public interface ScriptSeries extends DataSeries
{
    /**
     * Returns the value of the '<em><b>Line Properties</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Properties</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line Properties</em>' containment reference.
     * @see #setLineProperties(LineProperties)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getScriptSeries_LineProperties()
     * @model containment="true" required="true"
     * @generated
     */
    LineProperties getLineProperties ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries#getLineProperties <em>Line Properties</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line Properties</em>' containment reference.
     * @see #getLineProperties()
     * @generated
     */
    void setLineProperties ( LineProperties value );

    /**
     * Returns the value of the '<em><b>Script</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Script</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Script</em>' attribute.
     * @see #setScript(String)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getScriptSeries_Script()
     * @model
     * @generated
     */
    String getScript ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries#getScript <em>Script</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Script</em>' attribute.
     * @see #getScript()
     * @generated
     */
    void setScript ( String value );

} // ScriptSeries
