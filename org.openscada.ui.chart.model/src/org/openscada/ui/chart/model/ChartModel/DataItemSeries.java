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
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineColor <em>Line Color</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineWidth <em>Line Width</em>}</li>
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
     * Returns the value of the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line Color</em>' attribute.
     * @see #setLineColor(RGB)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getDataItemSeries_LineColor()
     * @model dataType="org.openscada.ui.chart.model.ChartModel.RGB"
     * @generated
     */
    RGB getLineColor ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineColor <em>Line Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line Color</em>' attribute.
     * @see #getLineColor()
     * @generated
     */
    void setLineColor ( RGB value );

    /**
     * Returns the value of the '<em><b>Line Width</b></em>' attribute.
     * The default value is <code>"1"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Width</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line Width</em>' attribute.
     * @see #setLineWidth(float)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getDataItemSeries_LineWidth()
     * @model default="1" required="true"
     * @generated
     */
    float getLineWidth ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineWidth <em>Line Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line Width</em>' attribute.
     * @see #getLineWidth()
     * @generated
     */
    void setLineWidth ( float value );
} // DataItemSeries
