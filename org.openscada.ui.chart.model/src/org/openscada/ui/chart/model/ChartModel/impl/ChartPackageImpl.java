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
 * 
 * @generated
 */
public class ChartPackageImpl extends EPackageImpl implements ChartPackage
{
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass chartEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass xAxisEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass yAxisEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass axisEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass dataSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass dataItemSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass archiveSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass itemEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass uriItemEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass idItemEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass itemDataSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass archiveChannelEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass linePropertiesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass scriptSeriesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType rgbEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also performs initialization of the package, or returns the registered package, if one
     * already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
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
     * 
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * <p>
     * This method is used to initialize {@link ChartPackage#eINSTANCE} when that field is accessed. Clients should not invoke it directly. Instead, they should simply access that field to obtain the
     * package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ChartPackage init ()
    {
        if ( isInited )
        {
            return (ChartPackage)EPackage.Registry.INSTANCE.getEPackage ( ChartPackage.eNS_URI );
        }

        // Obtain or create and register package
        final ChartPackageImpl theChartPackage = (ChartPackageImpl) ( EPackage.Registry.INSTANCE.get ( eNS_URI ) instanceof ChartPackageImpl ? EPackage.Registry.INSTANCE.get ( eNS_URI ) : new ChartPackageImpl () );

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
     * 
     * @generated
     */
    @Override
    public EClass getChart ()
    {
        return this.chartEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getChart_Title ()
    {
        return (EAttribute)this.chartEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getChart_ShowCurrenTimeRuler ()
    {
        return (EAttribute)this.chartEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getChart_BackgroundColor ()
    {
        return (EAttribute)this.chartEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_Bottom ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 3 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_Top ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 4 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_Left ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 5 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_Right ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 6 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_SelectedYAxis ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 7 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_SelectedXAxis ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 8 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getChart_Inputs ()
    {
        return (EReference)this.chartEClass.getEStructuralFeatures ().get ( 9 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getChart_Mutable ()
    {
        return (EAttribute)this.chartEClass.getEStructuralFeatures ().get ( 10 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getXAxis ()
    {
        return this.xAxisEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getXAxis_Minimum ()
    {
        return (EAttribute)this.xAxisEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getXAxis_Maximum ()
    {
        return (EAttribute)this.xAxisEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getYAxis ()
    {
        return this.yAxisEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getYAxis_Minimum ()
    {
        return (EAttribute)this.yAxisEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getYAxis_Maximum ()
    {
        return (EAttribute)this.yAxisEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getAxis ()
    {
        return this.axisEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getAxis_Format ()
    {
        return (EAttribute)this.axisEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getAxis_Label ()
    {
        return (EAttribute)this.axisEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getDataSeries ()
    {
        return this.dataSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getDataSeries_Label ()
    {
        return (EAttribute)this.dataSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getDataSeries_X ()
    {
        return (EReference)this.dataSeriesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getDataSeries_Y ()
    {
        return (EReference)this.dataSeriesEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getDataItemSeries ()
    {
        return this.dataItemSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getDataItemSeries_LineProperties ()
    {
        return (EReference)this.dataItemSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getArchiveSeries ()
    {
        return this.archiveSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getArchiveSeries_Channels ()
    {
        return (EReference)this.archiveSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getArchiveSeries_LineProperties ()
    {
        return (EReference)this.archiveSeriesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getItem ()
    {
        return this.itemEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getItem_ItemId ()
    {
        return (EAttribute)this.itemEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getUriItem ()
    {
        return this.uriItemEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getUriItem_ConnectionUri ()
    {
        return (EAttribute)this.uriItemEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getIdItem ()
    {
        return this.idItemEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getIdItem_ConnectionId ()
    {
        return (EAttribute)this.idItemEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getItemDataSeries ()
    {
        return this.itemDataSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getItemDataSeries_Item ()
    {
        return (EReference)this.itemDataSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getArchiveChannel ()
    {
        return this.archiveChannelEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getArchiveChannel_Name ()
    {
        return (EAttribute)this.archiveChannelEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getArchiveChannel_Label ()
    {
        return (EAttribute)this.archiveChannelEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getArchiveChannel_LineProperties ()
    {
        return (EReference)this.archiveChannelEClass.getEStructuralFeatures ().get ( 2 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getLineProperties ()
    {
        return this.linePropertiesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getLineProperties_Width ()
    {
        return (EAttribute)this.linePropertiesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getLineProperties_Color ()
    {
        return (EAttribute)this.linePropertiesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getScriptSeries ()
    {
        return this.scriptSeriesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getScriptSeries_LineProperties ()
    {
        return (EReference)this.scriptSeriesEClass.getEStructuralFeatures ().get ( 0 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getScriptSeries_Script ()
    {
        return (EAttribute)this.scriptSeriesEClass.getEStructuralFeatures ().get ( 1 );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EDataType getRGB ()
    {
        return this.rgbEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ChartFactory getChartFactory ()
    {
        return (ChartFactory)getEFactoryInstance ();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void createPackageContents ()
    {
        if ( this.isCreated )
        {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.chartEClass = createEClass ( CHART );
        createEAttribute ( this.chartEClass, CHART__TITLE );
        createEAttribute ( this.chartEClass, CHART__SHOW_CURREN_TIME_RULER );
        createEAttribute ( this.chartEClass, CHART__BACKGROUND_COLOR );
        createEReference ( this.chartEClass, CHART__BOTTOM );
        createEReference ( this.chartEClass, CHART__TOP );
        createEReference ( this.chartEClass, CHART__LEFT );
        createEReference ( this.chartEClass, CHART__RIGHT );
        createEReference ( this.chartEClass, CHART__SELECTED_YAXIS );
        createEReference ( this.chartEClass, CHART__SELECTED_XAXIS );
        createEReference ( this.chartEClass, CHART__INPUTS );
        createEAttribute ( this.chartEClass, CHART__MUTABLE );

        this.xAxisEClass = createEClass ( XAXIS );
        createEAttribute ( this.xAxisEClass, XAXIS__MINIMUM );
        createEAttribute ( this.xAxisEClass, XAXIS__MAXIMUM );

        this.yAxisEClass = createEClass ( YAXIS );
        createEAttribute ( this.yAxisEClass, YAXIS__MINIMUM );
        createEAttribute ( this.yAxisEClass, YAXIS__MAXIMUM );

        this.axisEClass = createEClass ( AXIS );
        createEAttribute ( this.axisEClass, AXIS__LABEL );
        createEAttribute ( this.axisEClass, AXIS__FORMAT );

        this.dataSeriesEClass = createEClass ( DATA_SERIES );
        createEAttribute ( this.dataSeriesEClass, DATA_SERIES__LABEL );
        createEReference ( this.dataSeriesEClass, DATA_SERIES__X );
        createEReference ( this.dataSeriesEClass, DATA_SERIES__Y );

        this.dataItemSeriesEClass = createEClass ( DATA_ITEM_SERIES );
        createEReference ( this.dataItemSeriesEClass, DATA_ITEM_SERIES__LINE_PROPERTIES );

        this.archiveSeriesEClass = createEClass ( ARCHIVE_SERIES );
        createEReference ( this.archiveSeriesEClass, ARCHIVE_SERIES__CHANNELS );
        createEReference ( this.archiveSeriesEClass, ARCHIVE_SERIES__LINE_PROPERTIES );

        this.itemEClass = createEClass ( ITEM );
        createEAttribute ( this.itemEClass, ITEM__ITEM_ID );

        this.uriItemEClass = createEClass ( URI_ITEM );
        createEAttribute ( this.uriItemEClass, URI_ITEM__CONNECTION_URI );

        this.idItemEClass = createEClass ( ID_ITEM );
        createEAttribute ( this.idItemEClass, ID_ITEM__CONNECTION_ID );

        this.itemDataSeriesEClass = createEClass ( ITEM_DATA_SERIES );
        createEReference ( this.itemDataSeriesEClass, ITEM_DATA_SERIES__ITEM );

        this.archiveChannelEClass = createEClass ( ARCHIVE_CHANNEL );
        createEAttribute ( this.archiveChannelEClass, ARCHIVE_CHANNEL__NAME );
        createEAttribute ( this.archiveChannelEClass, ARCHIVE_CHANNEL__LABEL );
        createEReference ( this.archiveChannelEClass, ARCHIVE_CHANNEL__LINE_PROPERTIES );

        this.linePropertiesEClass = createEClass ( LINE_PROPERTIES );
        createEAttribute ( this.linePropertiesEClass, LINE_PROPERTIES__WIDTH );
        createEAttribute ( this.linePropertiesEClass, LINE_PROPERTIES__COLOR );

        this.scriptSeriesEClass = createEClass ( SCRIPT_SERIES );
        createEReference ( this.scriptSeriesEClass, SCRIPT_SERIES__LINE_PROPERTIES );
        createEAttribute ( this.scriptSeriesEClass, SCRIPT_SERIES__SCRIPT );

        // Create data types
        this.rgbEDataType = createEDataType ( RGB );
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void initializePackageContents ()
    {
        if ( this.isInitialized )
        {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        setName ( eNAME );
        setNsPrefix ( eNS_PREFIX );
        setNsURI ( eNS_URI );

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.xAxisEClass.getESuperTypes ().add ( getAxis () );
        this.yAxisEClass.getESuperTypes ().add ( getAxis () );
        this.dataItemSeriesEClass.getESuperTypes ().add ( getItemDataSeries () );
        this.archiveSeriesEClass.getESuperTypes ().add ( getItemDataSeries () );
        this.uriItemEClass.getESuperTypes ().add ( getItem () );
        this.idItemEClass.getESuperTypes ().add ( getItem () );
        this.itemDataSeriesEClass.getESuperTypes ().add ( getDataSeries () );
        this.scriptSeriesEClass.getESuperTypes ().add ( getDataSeries () );

        // Initialize classes and features; add operations and parameters
        initEClass ( this.chartEClass, Chart.class, "Chart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getChart_Title (), this.ecorePackage.getEString (), "title", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_ShowCurrenTimeRuler (), this.ecorePackage.getEBoolean (), "showCurrenTimeRuler", "true", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_BackgroundColor (), getRGB (), "backgroundColor", "#FFFFFF", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Bottom (), getXAxis (), null, "bottom", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Top (), getXAxis (), null, "top", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Left (), getYAxis (), null, "left", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Right (), getYAxis (), null, "right", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_SelectedYAxis (), getYAxis (), null, "selectedYAxis", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_SelectedXAxis (), getXAxis (), null, "selectedXAxis", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getChart_Inputs (), getDataSeries (), null, "inputs", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getChart_Mutable (), this.ecorePackage.getEBoolean (), "mutable", "true", 1, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.xAxisEClass, XAxis.class, "XAxis", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getXAxis_Minimum (), this.ecorePackage.getELong (), "minimum", "0", 1, 1, XAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getXAxis_Maximum (), this.ecorePackage.getELong (), "maximum", "1000", 1, 1, XAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.yAxisEClass, YAxis.class, "YAxis", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getYAxis_Minimum (), this.ecorePackage.getEDouble (), "minimum", "-100.0", 1, 1, YAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getYAxis_Maximum (), this.ecorePackage.getEDouble (), "maximum", "100.0", 1, 1, YAxis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.axisEClass, Axis.class, "Axis", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getAxis_Label (), this.ecorePackage.getEString (), "label", null, 0, 1, Axis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getAxis_Format (), this.ecorePackage.getEString (), "format", null, 0, 1, Axis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.dataSeriesEClass, DataSeries.class, "DataSeries", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getDataSeries_Label (), this.ecorePackage.getEString (), "label", null, 0, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getDataSeries_X (), getXAxis (), null, "x", null, 1, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getDataSeries_Y (), getYAxis (), null, "y", null, 1, 1, DataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.dataItemSeriesEClass, DataItemSeries.class, "DataItemSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getDataItemSeries_LineProperties (), getLineProperties (), null, "lineProperties", null, 1, 1, DataItemSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.archiveSeriesEClass, ArchiveSeries.class, "ArchiveSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getArchiveSeries_Channels (), getArchiveChannel (), null, "channels", null, 0, -1, ArchiveSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getArchiveSeries_LineProperties (), getLineProperties (), null, "lineProperties", null, 1, 1, ArchiveSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.itemEClass, Item.class, "Item", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getItem_ItemId (), this.ecorePackage.getEString (), "itemId", null, 1, 1, Item.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.uriItemEClass, UriItem.class, "UriItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getUriItem_ConnectionUri (), this.ecorePackage.getEString (), "connectionUri", null, 1, 1, UriItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.idItemEClass, IdItem.class, "IdItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getIdItem_ConnectionId (), this.ecorePackage.getEString (), "connectionId", null, 1, 1, IdItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.itemDataSeriesEClass, ItemDataSeries.class, "ItemDataSeries", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getItemDataSeries_Item (), getItem (), null, "item", null, 1, 1, ItemDataSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.archiveChannelEClass, ArchiveChannel.class, "ArchiveChannel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getArchiveChannel_Name (), this.ecorePackage.getEString (), "name", null, 1, 1, ArchiveChannel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getArchiveChannel_Label (), this.ecorePackage.getEString (), "label", null, 0, 1, ArchiveChannel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEReference ( getArchiveChannel_LineProperties (), getLineProperties (), null, "lineProperties", null, 1, 1, ArchiveChannel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.linePropertiesEClass, LineProperties.class, "LineProperties", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEAttribute ( getLineProperties_Width (), this.ecorePackage.getEFloat (), "width", "1.0", 1, 1, LineProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getLineProperties_Color (), getRGB (), "color", "#000000", 1, 1, LineProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        initEClass ( this.scriptSeriesEClass, ScriptSeries.class, "ScriptSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS );
        initEReference ( getScriptSeries_LineProperties (), getLineProperties (), null, "lineProperties", null, 1, 1, ScriptSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );
        initEAttribute ( getScriptSeries_Script (), this.ecorePackage.getEString (), "script", null, 0, 1, ScriptSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED );

        // Initialize data types
        initEDataType ( this.rgbEDataType, org.eclipse.swt.graphics.RGB.class, "RGB", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS );

        // Create resource
        createResource ( eNS_URI );

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations ();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void createExtendedMetaDataAnnotations ()
    {
        final String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
        addAnnotation ( getChart_BackgroundColor (), source, new String[] { "name", "backgroundColor" } );
        addAnnotation ( getAxis_Label (), source, new String[] { "wildcards", "", "name", "" } );
        addAnnotation ( getArchiveSeries_LineProperties (), source, new String[] { "wildcards", "", "name", "" } );
    }

} //ChartPackageImpl
