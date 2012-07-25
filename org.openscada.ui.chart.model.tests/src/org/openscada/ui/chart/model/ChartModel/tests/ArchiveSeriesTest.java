/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.tests;

import junit.textui.TestRunner;

import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Archive Series</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ArchiveSeriesTest extends ItemDataSeriesTest
{

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main ( String[] args )
    {
        TestRunner.run ( ArchiveSeriesTest.class );
    }

    /**
     * Constructs a new Archive Series test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArchiveSeriesTest ( String name )
    {
        super ( name );
    }

    /**
     * Returns the fixture for this Archive Series test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected ArchiveSeries getFixture ()
    {
        return (ArchiveSeries)fixture;
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
        setFixture ( ChartFactory.eINSTANCE.createArchiveSeries () );
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

} //ArchiveSeriesTest
