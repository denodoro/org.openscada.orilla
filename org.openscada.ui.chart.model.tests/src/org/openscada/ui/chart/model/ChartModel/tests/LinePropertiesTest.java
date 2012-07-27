/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.LineProperties;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Line Properties</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class LinePropertiesTest extends TestCase
{

    /**
     * The fixture for this Line Properties test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LineProperties fixture = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main ( String[] args )
    {
        TestRunner.run ( LinePropertiesTest.class );
    }

    /**
     * Constructs a new Line Properties test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinePropertiesTest ( String name )
    {
        super ( name );
    }

    /**
     * Sets the fixture for this Line Properties test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void setFixture ( LineProperties fixture )
    {
        this.fixture = fixture;
    }

    /**
     * Returns the fixture for this Line Properties test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LineProperties getFixture ()
    {
        return fixture;
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
        setFixture ( ChartFactory.eINSTANCE.createLineProperties () );
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

} //LinePropertiesTest
