/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.tests;

import junit.textui.TestRunner;

import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.CurrentTimeController;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Current Time Controller</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class CurrentTimeControllerTest extends ControllerTest
{

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main ( String[] args )
    {
        TestRunner.run ( CurrentTimeControllerTest.class );
    }

    /**
     * Constructs a new Current Time Controller test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurrentTimeControllerTest ( String name )
    {
        super ( name );
    }

    /**
     * Returns the fixture for this Current Time Controller test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected CurrentTimeController getFixture ()
    {
        return (CurrentTimeController)fixture;
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
        setFixture ( ChartFactory.eINSTANCE.createCurrentTimeController () );
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

} //CurrentTimeControllerTest
