/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Current Time Controller</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getDiff <em>Diff</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAxis <em>Axis</em>}</li>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAlignDateFormat <em>Align Date Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getCurrentTimeController()
 * @model
 * @generated
 */
public interface CurrentTimeController extends Controller
{
    /**
     * Returns the value of the '<em><b>Diff</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Diff</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Diff</em>' attribute.
     * @see #setDiff(long)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getCurrentTimeController_Diff()
     * @model default="0" required="true"
     * @generated
     */
    long getDiff ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getDiff <em>Diff</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Diff</em>' attribute.
     * @see #getDiff()
     * @generated
     */
    void setDiff ( long value );

    /**
     * Returns the value of the '<em><b>Axis</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Axis</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Axis</em>' reference.
     * @see #setAxis(XAxis)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getCurrentTimeController_Axis()
     * @model required="true"
     * @generated
     */
    XAxis getAxis ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAxis <em>Axis</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis</em>' reference.
     * @see #getAxis()
     * @generated
     */
    void setAxis ( XAxis value );

    /**
     * Returns the value of the '<em><b>Align Date Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Align Date Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A SimpleDateFormat which will be formatted and re-parsed in order to align the timestamp.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Align Date Format</em>' attribute.
     * @see #setAlignDateFormat(String)
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getCurrentTimeController_AlignDateFormat()
     * @model
     * @generated
     */
    String getAlignDateFormat ();

    /**
     * Sets the value of the '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAlignDateFormat <em>Align Date Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Align Date Format</em>' attribute.
     * @see #getAlignDateFormat()
     * @generated
     */
    void setAlignDateFormat ( String value );

} // CurrentTimeController
