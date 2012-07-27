/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.openscada.ui.chart.model.ChartModel.ArchiveChannel;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Archive Channel</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ArchiveChannelTest extends TestCase
{

    /**
     * The fixture for this Archive Channel test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArchiveChannel fixture = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main ( String[] args )
    {
        TestRunner.run ( ArchiveChannelTest.class );
    }

    /**
     * Constructs a new Archive Channel test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArchiveChannelTest ( String name )
    {
        super ( name );
    }

    /**
     * Sets the fixture for this Archive Channel test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void setFixture ( ArchiveChannel fixture )
    {
        this.fixture = fixture;
    }

    /**
     * Returns the fixture for this Archive Channel test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArchiveChannel getFixture ()
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
        setFixture ( ChartFactory.eINSTANCE.createArchiveChannel () );
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

} //ArchiveChannelTest
