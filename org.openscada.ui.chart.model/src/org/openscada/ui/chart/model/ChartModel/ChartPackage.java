/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openscada.ui.chart.model.ChartModel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.openscada.ui.chart.model.ChartModel.ChartFactory
 * @model kind="package"
 * @generated
 */
public interface ChartPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "ChartModel";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://openscada.org/UI/Chart";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "chart";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ChartPackage eINSTANCE = org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl.init ();

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ChartImpl <em>Chart</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getChart()
     * @generated
     */
    int CHART = 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__TITLE = 0;

    /**
     * The feature id for the '<em><b>Show Current Time Ruler</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__SHOW_CURRENT_TIME_RULER = 1;

    /**
     * The feature id for the '<em><b>Background Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__BACKGROUND_COLOR = 2;

    /**
     * The feature id for the '<em><b>Bottom</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__BOTTOM = 3;

    /**
     * The feature id for the '<em><b>Top</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__TOP = 4;

    /**
     * The feature id for the '<em><b>Left</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__LEFT = 5;

    /**
     * The feature id for the '<em><b>Right</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__RIGHT = 6;

    /**
     * The feature id for the '<em><b>Selected YAxis</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__SELECTED_YAXIS = 7;

    /**
     * The feature id for the '<em><b>Selected XAxis</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__SELECTED_XAXIS = 8;

    /**
     * The feature id for the '<em><b>Inputs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__INPUTS = 9;

    /**
     * The feature id for the '<em><b>Mutable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__MUTABLE = 10;

    /**
     * The feature id for the '<em><b>Scrollable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__SCROLLABLE = 11;

    /**
     * The feature id for the '<em><b>Controllers</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__CONTROLLERS = 12;

    /**
     * The feature id for the '<em><b>Hoverable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART__HOVERABLE = 13;

    /**
     * The number of structural features of the '<em>Chart</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHART_FEATURE_COUNT = 14;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.AxisImpl <em>Axis</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.AxisImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getAxis()
     * @generated
     */
    int AXIS = 3;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS__LABEL = 0;

    /**
     * The feature id for the '<em><b>Text Padding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS__TEXT_PADDING = 1;

    /**
     * The feature id for the '<em><b>Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS__COLOR = 2;

    /**
     * The feature id for the '<em><b>Label Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS__LABEL_VISIBLE = 3;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS__FORMAT = 4;

    /**
     * The number of structural features of the '<em>Axis</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.XAxisImpl <em>XAxis</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.XAxisImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getXAxis()
     * @generated
     */
    int XAXIS = 1;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__LABEL = AXIS__LABEL;

    /**
     * The feature id for the '<em><b>Text Padding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__TEXT_PADDING = AXIS__TEXT_PADDING;

    /**
     * The feature id for the '<em><b>Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__COLOR = AXIS__COLOR;

    /**
     * The feature id for the '<em><b>Label Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__LABEL_VISIBLE = AXIS__LABEL_VISIBLE;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__FORMAT = AXIS__FORMAT;

    /**
     * The feature id for the '<em><b>Minimum</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__MINIMUM = AXIS_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Maximum</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS__MAXIMUM = AXIS_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>XAxis</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XAXIS_FEATURE_COUNT = AXIS_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.YAxisImpl <em>YAxis</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.YAxisImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getYAxis()
     * @generated
     */
    int YAXIS = 2;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__LABEL = AXIS__LABEL;

    /**
     * The feature id for the '<em><b>Text Padding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__TEXT_PADDING = AXIS__TEXT_PADDING;

    /**
     * The feature id for the '<em><b>Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__COLOR = AXIS__COLOR;

    /**
     * The feature id for the '<em><b>Label Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__LABEL_VISIBLE = AXIS__LABEL_VISIBLE;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__FORMAT = AXIS__FORMAT;

    /**
     * The feature id for the '<em><b>Minimum</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__MINIMUM = AXIS_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Maximum</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS__MAXIMUM = AXIS_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>YAxis</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YAXIS_FEATURE_COUNT = AXIS_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.DataSeriesImpl <em>Data Series</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.DataSeriesImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getDataSeries()
     * @generated
     */
    int DATA_SERIES = 4;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_SERIES__LABEL = 0;

    /**
     * The feature id for the '<em><b>X</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_SERIES__X = 1;

    /**
     * The feature id for the '<em><b>Y</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_SERIES__Y = 2;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_SERIES__VISIBLE = 3;

    /**
     * The number of structural features of the '<em>Data Series</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_SERIES_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ItemDataSeriesImpl <em>Item Data Series</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ItemDataSeriesImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getItemDataSeries()
     * @generated
     */
    int ITEM_DATA_SERIES = 10;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_DATA_SERIES__LABEL = DATA_SERIES__LABEL;

    /**
     * The feature id for the '<em><b>X</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_DATA_SERIES__X = DATA_SERIES__X;

    /**
     * The feature id for the '<em><b>Y</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_DATA_SERIES__Y = DATA_SERIES__Y;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_DATA_SERIES__VISIBLE = DATA_SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Item</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_DATA_SERIES__ITEM = DATA_SERIES_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Item Data Series</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_DATA_SERIES_FEATURE_COUNT = DATA_SERIES_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl <em>Data Item Series</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getDataItemSeries()
     * @generated
     */
    int DATA_ITEM_SERIES = 5;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES__LABEL = ITEM_DATA_SERIES__LABEL;

    /**
     * The feature id for the '<em><b>X</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES__X = ITEM_DATA_SERIES__X;

    /**
     * The feature id for the '<em><b>Y</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES__Y = ITEM_DATA_SERIES__Y;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES__VISIBLE = ITEM_DATA_SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Item</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES__ITEM = ITEM_DATA_SERIES__ITEM;

    /**
     * The feature id for the '<em><b>Line Properties</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES__LINE_PROPERTIES = ITEM_DATA_SERIES_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Data Item Series</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_ITEM_SERIES_FEATURE_COUNT = ITEM_DATA_SERIES_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveSeriesImpl <em>Archive Series</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ArchiveSeriesImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getArchiveSeries()
     * @generated
     */
    int ARCHIVE_SERIES = 6;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__LABEL = ITEM_DATA_SERIES__LABEL;

    /**
     * The feature id for the '<em><b>X</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__X = ITEM_DATA_SERIES__X;

    /**
     * The feature id for the '<em><b>Y</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__Y = ITEM_DATA_SERIES__Y;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__VISIBLE = ITEM_DATA_SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Item</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__ITEM = ITEM_DATA_SERIES__ITEM;

    /**
     * The feature id for the '<em><b>Channels</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__CHANNELS = ITEM_DATA_SERIES_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Line Properties</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES__LINE_PROPERTIES = ITEM_DATA_SERIES_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Archive Series</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_SERIES_FEATURE_COUNT = ITEM_DATA_SERIES_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ItemImpl <em>Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ItemImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getItem()
     * @generated
     */
    int ITEM = 7;

    /**
     * The feature id for the '<em><b>Item Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__ITEM_ID = 0;

    /**
     * The number of structural features of the '<em>Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.UriItemImpl <em>Uri Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.UriItemImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getUriItem()
     * @generated
     */
    int URI_ITEM = 8;

    /**
     * The feature id for the '<em><b>Item Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URI_ITEM__ITEM_ID = ITEM__ITEM_ID;

    /**
     * The feature id for the '<em><b>Connection Uri</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URI_ITEM__CONNECTION_URI = ITEM_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Uri Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URI_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.IdItemImpl <em>Id Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.IdItemImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getIdItem()
     * @generated
     */
    int ID_ITEM = 9;

    /**
     * The feature id for the '<em><b>Item Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ID_ITEM__ITEM_ID = ITEM__ITEM_ID;

    /**
     * The feature id for the '<em><b>Connection Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ID_ITEM__CONNECTION_ID = ITEM_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Id Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ID_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl <em>Archive Channel</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getArchiveChannel()
     * @generated
     */
    int ARCHIVE_CHANNEL = 11;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_CHANNEL__NAME = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_CHANNEL__LABEL = 1;

    /**
     * The feature id for the '<em><b>Line Properties</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_CHANNEL__LINE_PROPERTIES = 2;

    /**
     * The number of structural features of the '<em>Archive Channel</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIVE_CHANNEL_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.LinePropertiesImpl <em>Line Properties</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.LinePropertiesImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getLineProperties()
     * @generated
     */
    int LINE_PROPERTIES = 12;

    /**
     * The feature id for the '<em><b>Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINE_PROPERTIES__WIDTH = 0;

    /**
     * The feature id for the '<em><b>Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINE_PROPERTIES__COLOR = 1;

    /**
     * The number of structural features of the '<em>Line Properties</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINE_PROPERTIES_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ScriptSeriesImpl <em>Script Series</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ScriptSeriesImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getScriptSeries()
     * @generated
     */
    int SCRIPT_SERIES = 13;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES__LABEL = DATA_SERIES__LABEL;

    /**
     * The feature id for the '<em><b>X</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES__X = DATA_SERIES__X;

    /**
     * The feature id for the '<em><b>Y</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES__Y = DATA_SERIES__Y;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES__VISIBLE = DATA_SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Line Properties</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES__LINE_PROPERTIES = DATA_SERIES_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Script</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES__SCRIPT = DATA_SERIES_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Script Series</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_SERIES_FEATURE_COUNT = DATA_SERIES_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ControllerImpl <em>Controller</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.ControllerImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getController()
     * @generated
     */
    int CONTROLLER = 14;

    /**
     * The number of structural features of the '<em>Controller</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTROLLER_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl <em>Current Time Controller</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getCurrentTimeController()
     * @generated
     */
    int CURRENT_TIME_CONTROLLER = 15;

    /**
     * The feature id for the '<em><b>Diff</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_TIME_CONTROLLER__DIFF = CONTROLLER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Axis</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_TIME_CONTROLLER__AXIS = CONTROLLER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Align Date Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT = CONTROLLER_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Current Time Controller</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_TIME_CONTROLLER_FEATURE_COUNT = CONTROLLER_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '<em>RGB</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.swt.graphics.RGB
     * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getRGB()
     * @generated
     */
    int RGB = 16;

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.Chart <em>Chart</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Chart</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart
     * @generated
     */
    EClass getChart ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Chart#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getTitle()
     * @see #getChart()
     * @generated
     */
    EAttribute getChart_Title ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Chart#isShowCurrentTimeRuler <em>Show Current Time Ruler</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Show Current Time Ruler</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#isShowCurrentTimeRuler()
     * @see #getChart()
     * @generated
     */
    EAttribute getChart_ShowCurrentTimeRuler ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Chart#getBackgroundColor <em>Background Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Background Color</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getBackgroundColor()
     * @see #getChart()
     * @generated
     */
    EAttribute getChart_BackgroundColor ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.Chart#getBottom <em>Bottom</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bottom</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getBottom()
     * @see #getChart()
     * @generated
     */
    EReference getChart_Bottom ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.Chart#getTop <em>Top</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Top</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getTop()
     * @see #getChart()
     * @generated
     */
    EReference getChart_Top ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.Chart#getLeft <em>Left</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Left</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getLeft()
     * @see #getChart()
     * @generated
     */
    EReference getChart_Left ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.Chart#getRight <em>Right</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Right</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getRight()
     * @see #getChart()
     * @generated
     */
    EReference getChart_Right ();

    /**
     * Returns the meta object for the reference '{@link org.openscada.ui.chart.model.ChartModel.Chart#getSelectedYAxis <em>Selected YAxis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Selected YAxis</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getSelectedYAxis()
     * @see #getChart()
     * @generated
     */
    EReference getChart_SelectedYAxis ();

    /**
     * Returns the meta object for the reference '{@link org.openscada.ui.chart.model.ChartModel.Chart#getSelectedXAxis <em>Selected XAxis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Selected XAxis</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getSelectedXAxis()
     * @see #getChart()
     * @generated
     */
    EReference getChart_SelectedXAxis ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.Chart#getInputs <em>Inputs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Inputs</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getInputs()
     * @see #getChart()
     * @generated
     */
    EReference getChart_Inputs ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Chart#isMutable <em>Mutable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mutable</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#isMutable()
     * @see #getChart()
     * @generated
     */
    EAttribute getChart_Mutable ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Chart#isScrollable <em>Scrollable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Scrollable</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#isScrollable()
     * @see #getChart()
     * @generated
     */
    EAttribute getChart_Scrollable ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.Chart#getControllers <em>Controllers</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Controllers</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#getControllers()
     * @see #getChart()
     * @generated
     */
    EReference getChart_Controllers ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Chart#isHoverable <em>Hoverable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hoverable</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Chart#isHoverable()
     * @see #getChart()
     * @generated
     */
    EAttribute getChart_Hoverable ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.XAxis <em>XAxis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>XAxis</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.XAxis
     * @generated
     */
    EClass getXAxis ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.XAxis#getMinimum <em>Minimum</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Minimum</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.XAxis#getMinimum()
     * @see #getXAxis()
     * @generated
     */
    EAttribute getXAxis_Minimum ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.XAxis#getMaximum <em>Maximum</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Maximum</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.XAxis#getMaximum()
     * @see #getXAxis()
     * @generated
     */
    EAttribute getXAxis_Maximum ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.YAxis <em>YAxis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>YAxis</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.YAxis
     * @generated
     */
    EClass getYAxis ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.YAxis#getMinimum <em>Minimum</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Minimum</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.YAxis#getMinimum()
     * @see #getYAxis()
     * @generated
     */
    EAttribute getYAxis_Minimum ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.YAxis#getMaximum <em>Maximum</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Maximum</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.YAxis#getMaximum()
     * @see #getYAxis()
     * @generated
     */
    EAttribute getYAxis_Maximum ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.Axis <em>Axis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Axis</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Axis
     * @generated
     */
    EClass getAxis ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Axis#getTextPadding <em>Text Padding</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Padding</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Axis#getTextPadding()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_TextPadding ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Axis#getColor <em>Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Color</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Axis#getColor()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Color ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Axis#isLabelVisible <em>Label Visible</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label Visible</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Axis#isLabelVisible()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_LabelVisible ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Axis#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Axis#getFormat()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Format ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Axis#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Axis#getLabel()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Label ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.DataSeries <em>Data Series</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Series</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataSeries
     * @generated
     */
    EClass getDataSeries ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.DataSeries#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataSeries#getLabel()
     * @see #getDataSeries()
     * @generated
     */
    EAttribute getDataSeries_Label ();

    /**
     * Returns the meta object for the reference '{@link org.openscada.ui.chart.model.ChartModel.DataSeries#getX <em>X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>X</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataSeries#getX()
     * @see #getDataSeries()
     * @generated
     */
    EReference getDataSeries_X ();

    /**
     * Returns the meta object for the reference '{@link org.openscada.ui.chart.model.ChartModel.DataSeries#getY <em>Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Y</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataSeries#getY()
     * @see #getDataSeries()
     * @generated
     */
    EReference getDataSeries_Y ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.DataSeries#isVisible <em>Visible</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Visible</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataSeries#isVisible()
     * @see #getDataSeries()
     * @generated
     */
    EAttribute getDataSeries_Visible ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries <em>Data Item Series</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Item Series</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataItemSeries
     * @generated
     */
    EClass getDataItemSeries ();

    /**
     * Returns the meta object for the containment reference '{@link org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineProperties <em>Line Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Line Properties</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.DataItemSeries#getLineProperties()
     * @see #getDataItemSeries()
     * @generated
     */
    EReference getDataItemSeries_LineProperties ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.ArchiveSeries <em>Archive Series</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Archive Series</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveSeries
     * @generated
     */
    EClass getArchiveSeries ();

    /**
     * Returns the meta object for the containment reference list '{@link org.openscada.ui.chart.model.ChartModel.ArchiveSeries#getChannels <em>Channels</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Channels</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveSeries#getChannels()
     * @see #getArchiveSeries()
     * @generated
     */
    EReference getArchiveSeries_Channels ();

    /**
     * Returns the meta object for the containment reference '{@link org.openscada.ui.chart.model.ChartModel.ArchiveSeries#getLineProperties <em>Line Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Line Properties</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveSeries#getLineProperties()
     * @see #getArchiveSeries()
     * @generated
     */
    EReference getArchiveSeries_LineProperties ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.Item <em>Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Item</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Item
     * @generated
     */
    EClass getItem ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.Item#getItemId <em>Item Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Item Id</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Item#getItemId()
     * @see #getItem()
     * @generated
     */
    EAttribute getItem_ItemId ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.UriItem <em>Uri Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Uri Item</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.UriItem
     * @generated
     */
    EClass getUriItem ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.UriItem#getConnectionUri <em>Connection Uri</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Connection Uri</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.UriItem#getConnectionUri()
     * @see #getUriItem()
     * @generated
     */
    EAttribute getUriItem_ConnectionUri ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.IdItem <em>Id Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Id Item</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.IdItem
     * @generated
     */
    EClass getIdItem ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.IdItem#getConnectionId <em>Connection Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Connection Id</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.IdItem#getConnectionId()
     * @see #getIdItem()
     * @generated
     */
    EAttribute getIdItem_ConnectionId ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.ItemDataSeries <em>Item Data Series</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Item Data Series</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ItemDataSeries
     * @generated
     */
    EClass getItemDataSeries ();

    /**
     * Returns the meta object for the containment reference '{@link org.openscada.ui.chart.model.ChartModel.ItemDataSeries#getItem <em>Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Item</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ItemDataSeries#getItem()
     * @see #getItemDataSeries()
     * @generated
     */
    EReference getItemDataSeries_Item ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel <em>Archive Channel</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Archive Channel</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveChannel
     * @generated
     */
    EClass getArchiveChannel ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveChannel#getName()
     * @see #getArchiveChannel()
     * @generated
     */
    EAttribute getArchiveChannel_Name ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveChannel#getLabel()
     * @see #getArchiveChannel()
     * @generated
     */
    EAttribute getArchiveChannel_Label ();

    /**
     * Returns the meta object for the containment reference '{@link org.openscada.ui.chart.model.ChartModel.ArchiveChannel#getLineProperties <em>Line Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Line Properties</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ArchiveChannel#getLineProperties()
     * @see #getArchiveChannel()
     * @generated
     */
    EReference getArchiveChannel_LineProperties ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.LineProperties <em>Line Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Line Properties</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.LineProperties
     * @generated
     */
    EClass getLineProperties ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.LineProperties#getWidth <em>Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Width</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.LineProperties#getWidth()
     * @see #getLineProperties()
     * @generated
     */
    EAttribute getLineProperties_Width ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.LineProperties#getColor <em>Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Color</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.LineProperties#getColor()
     * @see #getLineProperties()
     * @generated
     */
    EAttribute getLineProperties_Color ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries <em>Script Series</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Script Series</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ScriptSeries
     * @generated
     */
    EClass getScriptSeries ();

    /**
     * Returns the meta object for the containment reference '{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries#getLineProperties <em>Line Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Line Properties</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ScriptSeries#getLineProperties()
     * @see #getScriptSeries()
     * @generated
     */
    EReference getScriptSeries_LineProperties ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.ScriptSeries#getScript <em>Script</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Script</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.ScriptSeries#getScript()
     * @see #getScriptSeries()
     * @generated
     */
    EAttribute getScriptSeries_Script ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.Controller <em>Controller</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Controller</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.Controller
     * @generated
     */
    EClass getController ();

    /**
     * Returns the meta object for class '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController <em>Current Time Controller</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Current Time Controller</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.CurrentTimeController
     * @generated
     */
    EClass getCurrentTimeController ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getDiff <em>Diff</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Diff</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getDiff()
     * @see #getCurrentTimeController()
     * @generated
     */
    EAttribute getCurrentTimeController_Diff ();

    /**
     * Returns the meta object for the reference '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAxis <em>Axis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Axis</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAxis()
     * @see #getCurrentTimeController()
     * @generated
     */
    EReference getCurrentTimeController_Axis ();

    /**
     * Returns the meta object for the attribute '{@link org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAlignDateFormat <em>Align Date Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Align Date Format</em>'.
     * @see org.openscada.ui.chart.model.ChartModel.CurrentTimeController#getAlignDateFormat()
     * @see #getCurrentTimeController()
     * @generated
     */
    EAttribute getCurrentTimeController_AlignDateFormat ();

    /**
     * Returns the meta object for data type '{@link org.eclipse.swt.graphics.RGB <em>RGB</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>RGB</em>'.
     * @see org.eclipse.swt.graphics.RGB
     * @model instanceClass="org.eclipse.swt.graphics.RGB"
     * @generated
     */
    EDataType getRGB ();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ChartFactory getChartFactory ();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ChartImpl <em>Chart</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getChart()
         * @generated
         */
        EClass CHART = eINSTANCE.getChart ();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHART__TITLE = eINSTANCE.getChart_Title ();

        /**
         * The meta object literal for the '<em><b>Show Current Time Ruler</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHART__SHOW_CURRENT_TIME_RULER = eINSTANCE.getChart_ShowCurrentTimeRuler ();

        /**
         * The meta object literal for the '<em><b>Background Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHART__BACKGROUND_COLOR = eINSTANCE.getChart_BackgroundColor ();

        /**
         * The meta object literal for the '<em><b>Bottom</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__BOTTOM = eINSTANCE.getChart_Bottom ();

        /**
         * The meta object literal for the '<em><b>Top</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__TOP = eINSTANCE.getChart_Top ();

        /**
         * The meta object literal for the '<em><b>Left</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__LEFT = eINSTANCE.getChart_Left ();

        /**
         * The meta object literal for the '<em><b>Right</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__RIGHT = eINSTANCE.getChart_Right ();

        /**
         * The meta object literal for the '<em><b>Selected YAxis</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__SELECTED_YAXIS = eINSTANCE.getChart_SelectedYAxis ();

        /**
         * The meta object literal for the '<em><b>Selected XAxis</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__SELECTED_XAXIS = eINSTANCE.getChart_SelectedXAxis ();

        /**
         * The meta object literal for the '<em><b>Inputs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__INPUTS = eINSTANCE.getChart_Inputs ();

        /**
         * The meta object literal for the '<em><b>Mutable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHART__MUTABLE = eINSTANCE.getChart_Mutable ();

        /**
         * The meta object literal for the '<em><b>Scrollable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHART__SCROLLABLE = eINSTANCE.getChart_Scrollable ();

        /**
         * The meta object literal for the '<em><b>Controllers</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHART__CONTROLLERS = eINSTANCE.getChart_Controllers ();

        /**
         * The meta object literal for the '<em><b>Hoverable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHART__HOVERABLE = eINSTANCE.getChart_Hoverable ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.XAxisImpl <em>XAxis</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.XAxisImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getXAxis()
         * @generated
         */
        EClass XAXIS = eINSTANCE.getXAxis ();

        /**
         * The meta object literal for the '<em><b>Minimum</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute XAXIS__MINIMUM = eINSTANCE.getXAxis_Minimum ();

        /**
         * The meta object literal for the '<em><b>Maximum</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute XAXIS__MAXIMUM = eINSTANCE.getXAxis_Maximum ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.YAxisImpl <em>YAxis</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.YAxisImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getYAxis()
         * @generated
         */
        EClass YAXIS = eINSTANCE.getYAxis ();

        /**
         * The meta object literal for the '<em><b>Minimum</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute YAXIS__MINIMUM = eINSTANCE.getYAxis_Minimum ();

        /**
         * The meta object literal for the '<em><b>Maximum</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute YAXIS__MAXIMUM = eINSTANCE.getYAxis_Maximum ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.AxisImpl <em>Axis</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.AxisImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getAxis()
         * @generated
         */
        EClass AXIS = eINSTANCE.getAxis ();

        /**
         * The meta object literal for the '<em><b>Text Padding</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS__TEXT_PADDING = eINSTANCE.getAxis_TextPadding ();

        /**
         * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS__COLOR = eINSTANCE.getAxis_Color ();

        /**
         * The meta object literal for the '<em><b>Label Visible</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS__LABEL_VISIBLE = eINSTANCE.getAxis_LabelVisible ();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS__FORMAT = eINSTANCE.getAxis_Format ();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS__LABEL = eINSTANCE.getAxis_Label ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.DataSeriesImpl <em>Data Series</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.DataSeriesImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getDataSeries()
         * @generated
         */
        EClass DATA_SERIES = eINSTANCE.getDataSeries ();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DATA_SERIES__LABEL = eINSTANCE.getDataSeries_Label ();

        /**
         * The meta object literal for the '<em><b>X</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_SERIES__X = eINSTANCE.getDataSeries_X ();

        /**
         * The meta object literal for the '<em><b>Y</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_SERIES__Y = eINSTANCE.getDataSeries_Y ();

        /**
         * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DATA_SERIES__VISIBLE = eINSTANCE.getDataSeries_Visible ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl <em>Data Item Series</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.DataItemSeriesImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getDataItemSeries()
         * @generated
         */
        EClass DATA_ITEM_SERIES = eINSTANCE.getDataItemSeries ();

        /**
         * The meta object literal for the '<em><b>Line Properties</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_ITEM_SERIES__LINE_PROPERTIES = eINSTANCE.getDataItemSeries_LineProperties ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveSeriesImpl <em>Archive Series</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ArchiveSeriesImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getArchiveSeries()
         * @generated
         */
        EClass ARCHIVE_SERIES = eINSTANCE.getArchiveSeries ();

        /**
         * The meta object literal for the '<em><b>Channels</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIVE_SERIES__CHANNELS = eINSTANCE.getArchiveSeries_Channels ();

        /**
         * The meta object literal for the '<em><b>Line Properties</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIVE_SERIES__LINE_PROPERTIES = eINSTANCE.getArchiveSeries_LineProperties ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ItemImpl <em>Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ItemImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getItem()
         * @generated
         */
        EClass ITEM = eINSTANCE.getItem ();

        /**
         * The meta object literal for the '<em><b>Item Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ITEM__ITEM_ID = eINSTANCE.getItem_ItemId ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.UriItemImpl <em>Uri Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.UriItemImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getUriItem()
         * @generated
         */
        EClass URI_ITEM = eINSTANCE.getUriItem ();

        /**
         * The meta object literal for the '<em><b>Connection Uri</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute URI_ITEM__CONNECTION_URI = eINSTANCE.getUriItem_ConnectionUri ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.IdItemImpl <em>Id Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.IdItemImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getIdItem()
         * @generated
         */
        EClass ID_ITEM = eINSTANCE.getIdItem ();

        /**
         * The meta object literal for the '<em><b>Connection Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ID_ITEM__CONNECTION_ID = eINSTANCE.getIdItem_ConnectionId ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ItemDataSeriesImpl <em>Item Data Series</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ItemDataSeriesImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getItemDataSeries()
         * @generated
         */
        EClass ITEM_DATA_SERIES = eINSTANCE.getItemDataSeries ();

        /**
         * The meta object literal for the '<em><b>Item</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITEM_DATA_SERIES__ITEM = eINSTANCE.getItemDataSeries_Item ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl <em>Archive Channel</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ArchiveChannelImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getArchiveChannel()
         * @generated
         */
        EClass ARCHIVE_CHANNEL = eINSTANCE.getArchiveChannel ();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARCHIVE_CHANNEL__NAME = eINSTANCE.getArchiveChannel_Name ();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARCHIVE_CHANNEL__LABEL = eINSTANCE.getArchiveChannel_Label ();

        /**
         * The meta object literal for the '<em><b>Line Properties</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIVE_CHANNEL__LINE_PROPERTIES = eINSTANCE.getArchiveChannel_LineProperties ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.LinePropertiesImpl <em>Line Properties</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.LinePropertiesImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getLineProperties()
         * @generated
         */
        EClass LINE_PROPERTIES = eINSTANCE.getLineProperties ();

        /**
         * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LINE_PROPERTIES__WIDTH = eINSTANCE.getLineProperties_Width ();

        /**
         * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LINE_PROPERTIES__COLOR = eINSTANCE.getLineProperties_Color ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ScriptSeriesImpl <em>Script Series</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ScriptSeriesImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getScriptSeries()
         * @generated
         */
        EClass SCRIPT_SERIES = eINSTANCE.getScriptSeries ();

        /**
         * The meta object literal for the '<em><b>Line Properties</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SCRIPT_SERIES__LINE_PROPERTIES = eINSTANCE.getScriptSeries_LineProperties ();

        /**
         * The meta object literal for the '<em><b>Script</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCRIPT_SERIES__SCRIPT = eINSTANCE.getScriptSeries_Script ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.ControllerImpl <em>Controller</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.ControllerImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getController()
         * @generated
         */
        EClass CONTROLLER = eINSTANCE.getController ();

        /**
         * The meta object literal for the '{@link org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl <em>Current Time Controller</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.openscada.ui.chart.model.ChartModel.impl.CurrentTimeControllerImpl
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getCurrentTimeController()
         * @generated
         */
        EClass CURRENT_TIME_CONTROLLER = eINSTANCE.getCurrentTimeController ();

        /**
         * The meta object literal for the '<em><b>Diff</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CURRENT_TIME_CONTROLLER__DIFF = eINSTANCE.getCurrentTimeController_Diff ();

        /**
         * The meta object literal for the '<em><b>Axis</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CURRENT_TIME_CONTROLLER__AXIS = eINSTANCE.getCurrentTimeController_Axis ();

        /**
         * The meta object literal for the '<em><b>Align Date Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT = eINSTANCE.getCurrentTimeController_AlignDateFormat ();

        /**
         * The meta object literal for the '<em>RGB</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.swt.graphics.RGB
         * @see org.openscada.ui.chart.model.ChartModel.impl.ChartPackageImpl#getRGB()
         * @generated
         */
        EDataType RGB = eINSTANCE.getRGB ();

    }

} //ChartPackage
