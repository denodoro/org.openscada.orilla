/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.openscada.ui.chart.model.ChartModel.ArchiveChannel;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.Axis;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.Controller;
import org.openscada.ui.chart.model.ChartModel.CurrentTimeController;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.DataSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.ItemDataSeries;
import org.openscada.ui.chart.model.ChartModel.LineProperties;
import org.openscada.ui.chart.model.ChartModel.ScriptSeries;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class ChartPackageImpl extends EPackageImpl implements ChartPackage
{
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass chartEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass xAxisEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass yAxisEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass axisEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass dataSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass dataItemSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass archiveSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass itemEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass uriItemEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass idItemEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass itemDataSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass archiveChannelEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass linePropertiesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass scriptSeriesEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass controllerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass currentTimeControllerEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType rgbEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.openscada.ui.chart.model.ChartModel.ChartPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ChartPackageImpl ()
    {
        super ( eNS_URI, ChartFactory.eINSTANCE );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link ChartPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ChartPackage init ()
    {
        if ( isInited )
            return (ChartPackage)EPackage.Registry.INSTANCE.getEPackage ( ChartPackage.eNS_URI );

        // Obtain or create and register package
        ChartPackageImpl theChartPackage = (ChartPackageImpl) ( EPackage.Registry.INSTANCE.get ( eNS_URI ) instanceof ChartPackageImpl ? EPackage.Registry.INSTANCE.get ( eNS_URI ) : new ChartPackageImpl () );

        isInited = true;

        // Create package meta-data objects
        theChartPackage.createPackageContents ();

        // Initialize created meta-data
        theChartPackage.initializePackageContents ();

        // Mark meta-data to indicate it can't be changed
        theChartPackage.freeze ();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put ( ChartPackage.eNS_URI, theChartPackage );
        return theChartPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getChart ()
    {
        return chartEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getChart_Title ()
    {
        return (EAttribute)chartEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getChart_ShowCurrenTimeRuler ()
    {
        return (EAttribute)chartEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getChart_BackgroundColor ()
    {
        return (EAttribute)chartEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_Bottom ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 3 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_Top ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 4 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_Left ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 5 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_Right ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 6 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_SelectedYAxis ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 7 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_SelectedXAxis ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 8 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChart_Inputs ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 9 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getChart_Mutable ()
    {
        return (EAttribute)chartEClass.getEStructuralFeatures ().get ( 10 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getChart_Scrollable ()
    {
        return (EAttribute)chartEClass.getEStructuralFeatures ().get ( 11 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getChart_Controllers ()
    {
        return (EReference)chartEClass.getEStructuralFeatures ().get ( 12 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getXAxis ()
    {
        return xAxisEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getXAxis_Minimum ()
    {
        return (EAttribute)xAxisEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getXAxis_Maximum ()
    {
        return (EAttribute)xAxisEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getYAxis ()
    {
        return yAxisEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getYAxis_Minimum ()
    {
        return (EAttribute)yAxisEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getYAxis_Maximum ()
    {
        return (EAttribute)yAxisEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAxis ()
    {
        return axisEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getAxis_Format ()
    {
        return (EAttribute)axisEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAxis_TextPadding ()
    {
        return (EAttribute)axisEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getAxis_Label ()
    {
        return (EAttribute)axisEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDataSeries ()
    {
        return dataSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDataSeries_Label ()
    {
        return (EAttribute)dataSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDataSeries_X ()
    {
        return (EReference)dataSeriesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDataSeries_Y ()
    {
        return (EReference)dataSeriesEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDataSeries_Visible ()
    {
        return (EAttribute)dataSeriesEClass.getEStructuralFeatures ().get ( 3 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDataItemSeries ()
    {
        return dataItemSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDataItemSeries_LineProperties ()
    {
        return (EReference)dataItemSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchiveSeries ()
    {
        return archiveSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchiveSeries_Channels ()
    {
        return (EReference)archiveSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchiveSeries_LineProperties ()
    {
        return (EReference)archiveSeriesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItem ()
    {
        return itemEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getItem_ItemId ()
    {
        return (EAttribute)itemEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUriItem ()
    {
        return uriItemEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUriItem_ConnectionUri ()
    {
        return (EAttribute)uriItemEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIdItem ()
    {
        return idItemEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIdItem_ConnectionId ()
    {
        return (EAttribute)idItemEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItemDataSeries ()
    {
        return itemDataSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemDataSeries_Item ()
    {
        return (EReference)itemDataSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchiveChannel ()
    {
        return archiveChannelEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getArchiveChannel_Name ()
    {
        return (EAttribute)archiveChannelEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getArchiveChannel_Label ()
    {
        return (EAttribute)archiveChannelEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchiveChannel_LineProperties ()
    {
        return (EReference)archiveChannelEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLineProperties ()
    {
        return linePropertiesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLineProperties_Width ()
    {
        return (EAttribute)linePropertiesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLineProperties_Color ()
    {
        return (EAttribute)linePropertiesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getScriptSeries ()
    {
        return scriptSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getScriptSeries_LineProperties ()
    {
        return (EReference)scriptSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getScriptSeries_Script ()
    {
        return (EAttribute)scriptSeriesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getController ()
    {
        return controllerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCurrentTimeController ()
    {
        return currentTimeControllerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurrentTimeController_Diff ()
    {
        return (EAttribute)currentTimeControllerEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCurrentTimeController_Axis ()
    {
        return (EReference)currentTimeControllerEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurrentTimeController_AlignDateFormat ()
    {
        return (EAttribute)currentTimeControllerEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getRGB ()
    {
        return rgbEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ChartFactory getChartFactory ()
    {
        return (ChartFactory)getEFactoryInstance ();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents ()
    {
        if ( isCreated )
            return;
        isCreated = true;

        // Create classes and their features
        chartEClass = createEClass ( CHART );
        createEAttribute ( chartEClass, CHART__TITLE );
        createEAttribute ( chartEClass, CHART__SHOW_CURREN_TIME_RULER );
        createEAttribute ( chartEClass, CHART__BACKGROUND_COLOR );
        createEReference ( chartEClass, CHART__BOTTOM );
        createEReference ( chartEClass, CHART__TOP );
        createEReference ( chartEClass, CHART__LEFT );
        createEReference ( chartEClass, CHART__RIGHT );
        createEReference ( chartEClass, CHART__SELECTED_YAXIS );
        createEReference ( chartEClass, CHART__SELECTED_XAXIS );
        createEReference ( chartEClass, CHART__INPUTS );
        createEAttribute ( chartEClass, CHART__MUTABLE );
        createEAttribute ( chartEClass, CHART__SCROLLABLE );
        createEReference ( chartEClass, CHART__CONTROLLERS );

        xAxisEClass = createEClass ( XAXIS );
        createEAttribute ( xAxisEClass, XAXIS__MINIMUM );
        createEAttribute ( xAxisEClass, XAXIS__MAXIMUM );

        yAxisEClass = createEClass ( YAXIS );
        createEAttribute ( yAxisEClass, YAXIS__MINIMUM );
        createEAttribute ( yAxisEClass, YAXIS__MAXIMUM );

        axisEClass = createEClass ( AXIS );
        createEAttribute ( axisEClass, AXIS__LABEL );
        createEAttribute ( axisEClass, AXIS__FORMAT );
        createEAttribute ( axisEClass, AXIS__TEXT_PADDING );

        dataSeriesEClass = createEClass ( DATA_SERIES );
        createEAttribute ( dataSeriesEClass, DATA_SERIES__LABEL );
        createEReference ( dataSeriesEClass, DATA_SERIES__X );
        createEReference ( dataSeriesEClass, DATA_SERIES__Y );
        createEAttribute ( dataSeriesEClass, DATA_SERIES__VISIBLE );

        dataItemSeriesEClass = createEClass ( DATA_ITEM_SERIES );
        createEReference ( dataItemSeriesEClass, DATA_ITEM_SERIES__LINE_PROPERTIES );

        archiveSeriesEClass = createEClass ( ARCHIVE_SERIES );
        createEReference ( archiveSeriesEClass, ARCHIVE_SERIES__CHANNELS );
        createEReference ( archiveSeriesEClass, ARCHIVE_SERIES__LINE_PROPERTIES );

        itemEClass = createEClass ( ITEM );
        createEAttribute ( itemEClass, ITEM__ITEM_ID );

        uriItemEClass = createEClass ( URI_ITEM );
        createEAttribute ( uriItemEClass, URI_ITEM__CONNECTION_URI );

        idItemEClass = createEClass ( ID_ITEM );
        createEAttribute ( idItemEClass, ID_ITEM__CONNECTION_ID );

        itemDataSeriesEClass = createEClass ( ITEM_DATA_SERIES );
        createEReference ( itemDataSeriesEClass, ITEM_DATA_SERIES__ITEM );

        archiveChannelEClass = createEClass ( ARCHIVE_CHANNEL );
        createEAttribute ( archiveChannelEClass, ARCHIVE_CHANNEL__NAME );
        createEAttribute ( archiveChannelEClass, ARCHIVE_CHANNEL__LABEL );
        createEReference ( archiveChannelEClass, ARCHIVE_CHANNEL__LINE_PROPERTIES );

        linePropertiesEClass = createEClass ( LINE_PROPERTIES );
        createEAttribute ( linePropertiesEClass, LINE_PROPERTIES__WIDTH );
        createEAttribute ( linePropertiesEClass, LINE_PROPERTIES__COLOR );

        scriptSeriesEClass = createEClass ( SCRIPT_SERIES );
        createEReference ( scriptSeriesEClass, SCRIPT_SERIES__LINE_PROPERTIES );
        createEAttribute ( scriptSeriesEClass, SCRIPT_SERIES__SCRIPT );

        controllerEClass = createEClass ( CONTROLLER );

        currentTimeControllerEClass = createEClass ( CURRENT_TIME_CONTROLLER );
        createEAttribute ( currentTimeControllerEClass, CURRENT_TIME_CONTROLLER__DIFF );
        createEReference ( currentTimeControllerEClass, CURRENT_TIME_CONTROLLER__AXIS );
        createEAttribute ( currentTimeControllerEClass, CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT );

        // Create data types
        rgbEDataType = createEDataType ( RGB );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents ()
    {
        if ( isInitialized )
            return;
        isInitialized = true;

        // Initialize package
        setName ( eNAME );
        setNsPrefix ( eNS_PREFIX );
        setNsURI ( eNS_URI );

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        xAxisEClass.getESuperTypes ().add ( this.getAxis () );
        yAxisEClass.getESuperTypes ().add ( this.getAxis () );
        dataItemSeriesEClass.getESuperTypes ().add ( this.getItemDataSeries () );
        archiveSeriesEClass.getESuperTypes ().add ( this.getItemDataSeries () );
        uriItemEClass.getESuperTypes ().add ( this.getItem () );
        idItemEClass.getESuperTypes ().add ( this.getItem () );
        itemDataSeriesEClass.getESuperTypes ().add ( this.getDataSeries () );
        scriptSeriesEClass.getESuperTypes ().add ( this.getDataSeries () );
        currentTimeControllerEClass.getESuperTypes ().add ( this.getController () );

        // Initialize classes and features; add operations and parameters
        initEClass ( chartEClass, Chart.class, "Chart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getChart_Title (), ecorePackage.getEString (), "title", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_ShowCurrenTimeRuler (), ecorePackage.getEBoolean (), "showCurrenTimeRuler", "true", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_BackgroundColor (), this.getRGB (), "backgroundColor", "#FFFFFF", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Bottom (), this.getXAxis (), null, "bottom", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Top (), this.getXAxis (), null, "top", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Left (), this.getYAxis (), null, "left", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Right (), this.getYAxis (), null, "right", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_SelectedYAxis (), this.getYAxis (), null, "selectedYAxis", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_SelectedXAxis (), this.getXAxis (), null, "selectedXAxis", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Inputs (), this.getDataSeries (), null, "inputs", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_Mutable (), ecorePackage.getEBoolean (), "mutable", "true", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_Scrollable (), ecorePackage.getEBoolean (), "scrollable", "true", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Controllers (), this.getController (), null, "controllers", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( xAxisEClass, XAxis.class, "XAxis", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getXAxis_Minimum (), ecorePackage.getELong (), "minimum", "0", 1, 1, XAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getXAxis_Maximum (), ecorePackage.getELong (), "maximum", "1000", 1, 1, XAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( yAxisEClass, YAxis.class, "YAxis", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getYAxis_Minimum (), ecorePackage.getEDouble (), "minimum", "-100.0", 1, 1, YAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getYAxis_Maximum (), ecorePackage.getEDouble (), "maximum", "100.0", 1, 1, YAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( axisEClass, Axis.class, "Axis", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getAxis_Label (), ecorePackage.getEString (), "label", null, 0, 1, Axis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getAxis_Format (), ecorePackage.getEString (), "format", null, 0, 1, Axis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getAxis_TextPadding (), ecorePackage.getEInt (), "textPadding", "10", 1, 1, Axis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( dataSeriesEClass, DataSeries.class, "DataSeries", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getDataSeries_Label (), ecorePackage.getEString (), "label", null, 0, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getDataSeries_X (), this.getXAxis (), null, "x", null, 1, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getDataSeries_Y (), this.getYAxis (), null, "y", null, 1, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getDataSeries_Visible (), ecorePackage.getEBoolean (), "visible", "true", 1, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( dataItemSeriesEClass, DataItemSeries.class, "DataItemSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getDataItemSeries_LineProperties (), this.getLineProperties (), null, "lineProperties", null, 1, 1, DataItemSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( archiveSeriesEClass, ArchiveSeries.class, "ArchiveSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getArchiveSeries_Channels (), this.getArchiveChannel (), null, "channels", null, 0, -1, ArchiveSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getArchiveSeries_LineProperties (), this.getLineProperties (), null, "lineProperties", null, 1, 1, ArchiveSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( itemEClass, Item.class, "Item", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getItem_ItemId (), ecorePackage.getEString (), "itemId", null, 1, 1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( uriItemEClass, UriItem.class, "UriItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getUriItem_ConnectionUri (), ecorePackage.getEString (), "connectionUri", null, 1, 1, UriItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( idItemEClass, IdItem.class, "IdItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getIdItem_ConnectionId (), ecorePackage.getEString (), "connectionId", null, 1, 1, IdItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( itemDataSeriesEClass, ItemDataSeries.class, "ItemDataSeries", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getItemDataSeries_Item (), this.getItem (), null, "item", null, 1, 1, ItemDataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( archiveChannelEClass, ArchiveChannel.class, "ArchiveChannel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getArchiveChannel_Name (), ecorePackage.getEString (), "name", null, 1, 1, ArchiveChannel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getArchiveChannel_Label (), ecorePackage.getEString (), "label", null, 0, 1, ArchiveChannel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getArchiveChannel_LineProperties (), this.getLineProperties (), null, "lineProperties", null, 1, 1, ArchiveChannel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( linePropertiesEClass, LineProperties.class, "LineProperties", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getLineProperties_Width (), ecorePackage.getEFloat (), "width", "1.0", 1, 1, LineProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getLineProperties_Color (), this.getRGB (), "color", "#000000", 1, 1, LineProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( scriptSeriesEClass, ScriptSeries.class, "ScriptSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getScriptSeries_LineProperties (), this.getLineProperties (), null, "lineProperties", null, 1, 1, ScriptSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getScriptSeries_Script (), ecorePackage.getEString (), "script", null, 0, 1, ScriptSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( controllerEClass, Controller.class, "Controller", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );

        initEClass ( currentTimeControllerEClass, CurrentTimeController.class, "CurrentTimeController", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getCurrentTimeController_Diff (), ecorePackage.getELong (), "diff", "0", 1, 1, CurrentTimeController.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getCurrentTimeController_Axis (), this.getXAxis (), null, "axis", null, 1, 1, CurrentTimeController.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getCurrentTimeController_AlignDateFormat (), ecorePackage.getEString (), "alignDateFormat", null, 0, 1, CurrentTimeController.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        // Initialize data types
        initEDataType ( rgbEDataType, org.eclipse.swt.graphics.RGB.class, "RGB", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS );

        // Create resource
        createResource ( eNS_URI );

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations ();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaDataAnnotations ()
    {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
        addAnnotation ( getChart_BackgroundColor (), source, new String[] { "name", "backgroundColor" } );
        addAnnotation ( getAxis_Label (), source, new String[] { "name", "label" } );
        addAnnotation ( getArchiveSeries_LineProperties (), source, new String[] { "wildcards", "", "name", "" } );
    }

} //ChartPackageImpl
