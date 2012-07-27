/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Archive Series</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openscada.ui.chart.model.ChartModel.ArchiveSeries#getChannels <em>Channels</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getArchiveSeries()
 * @model
 * @generated
 */
public interface ArchiveSeries extends ItemDataSeries
{

    /**
     * Returns the value of the '<em><b>Channels</b></em>' containment reference list.
     * The list contents are of type {@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Channels</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Channels</em>' containment reference list.
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#getArchiveSeries_Channels()
     * @model containment="true"
     * @generated
     */
    EList<ArchiveChannel> getChannels ();
} // ArchiveSeries
