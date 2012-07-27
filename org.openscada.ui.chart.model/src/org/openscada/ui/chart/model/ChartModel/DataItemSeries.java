/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel;

import org.eclipse.swt.graphics.RGB;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Item Series</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineProperties <em>Line Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getDataItemSeries()
 * @model
 * @generated
 */
public interface DataItemSeries extends ItemDataSeries
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
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getDataItemSeries_LineProperties()
     * @model containment="true" required="true"
     * @generated
     */
    LineProperties getLineProperties ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineProperties <em>Line Properties</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line Properties</em>' containment reference.
     * @see #getLineProperties()
     * @generated
     */
    void setLineProperties ( LineProperties value );
} // DataItemSeries
