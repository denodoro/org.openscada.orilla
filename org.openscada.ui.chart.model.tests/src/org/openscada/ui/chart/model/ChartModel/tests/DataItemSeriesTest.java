/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.tests;

import junit.textui.TestRunner;

import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Data Item Series</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DataItemSeriesTest extends ItemDataSeriesTest
{

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main ( String[] args )
    {
        TestRunner.run ( DataItemSeriesTest.class );
    }

    /**
     * Constructs a new Data Item Series test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataItemSeriesTest ( String name )
    {
        super ( name );
    }

    /**
     * Returns the fixture for this Data Item Series test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected DataItemSeries getFixture ()
    {
        return (DataItemSeries)fixture;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#setUp()
     * @generated
     */
    @Override
    protected void setUp () throws Exception
    {
        setFixture ( ChartFactory.eINSTANCE.createDataItemSeries () );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#tearDown()
     * @generated
     */
    @Override
    protected void tearDown () throws Exception
    {
        setFixture ( null );
    }

} //DataItemSeriesTest
