package org.openscada.ae.ui.views.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
import org.openscada.utils.filter.Assertion;
import org.openscada.utils.filter.Filter;
import org.openscada.utils.filter.FilterAssertion;
import org.openscada.utils.filter.FilterExpression;
import org.openscada.utils.filter.Operator;

public class FilterAdvancedComposite extends Composite
{
    private final List<OrCondition> orConditions = new ArrayList<OrCondition> ();

    private final CTabFolder tabFolder;

    private final Button addOrConditionButton;

    private final FilterChangedListener filterChangedListener;

    private enum Type
    {
        Text,
        Number,
        DateTime
    }

    /**
     * @author jrose
     *
     */
    private static class AddAssertionComposite extends Composite
    {
        // fields
        private final OrCondition orCondition;

        // widgets
        private final Combo attributeCombo;

        private final Combo typeCombo;

        private final Button addButton;

        public AddAssertionComposite ( final OrCondition orCondition, final Composite parent )
        {
            // fields
            super ( parent, SWT.NONE );
            this.orCondition = orCondition;

            // widgets
            this.attributeCombo = createAttributeCombo ();
            this.typeCombo = createTypeCombo ();
            this.addButton = createAddButton ();

            // layout
            RowLayout layout = new RowLayout ();
            this.setLayout ( layout );
        }

        private Combo createAttributeCombo ()
        {
            final Combo c = new Combo ( this, SWT.NONE );
            c.add ( "sourceTimestamp" );
            c.add ( "entryTimestamp" );
            for ( Event.Fields field : Event.Fields.values () )
            {
                c.add ( field.getName () );
            }
            c.add ( "custom field ..." );
            c.select ( 0 );
            return c;
        }

        private Combo createTypeCombo ()
        {
            final Combo c = new Combo ( this, SWT.NONE );
            for ( Type type : Type.values () )
            {
                c.add ( type.name () );
            }
            c.select ( 0 );
            return c;
        }

        private Button createAddButton ()
        {
            final Button b = new Button ( this, SWT.PUSH );
            b.setText ( "Add Assertion" );
            b.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    AddAssertionComposite.this.orCondition.addAssertion ();
                }
            } );
            return b;
        }
    }

    /**
     * 
     * @author jrose
     */
    private static class AssertionComposite extends Composite
    {
        // fields
        private final OrCondition orCondition;

        private final Composite parent;

        private final String attribute;

        private final Type type;

        // widgets
        private final Button notCheck;

        private final Text attributeText;

        private final Label fieldTypeLabel;

        private final Text valueText;

        private final Combo assertionCombo;

        private final Button removeButton;

        public AssertionComposite ( final OrCondition orCondition, final Composite parent, final String attribute, final Type type )
        {
            // final fields
            super ( parent, SWT.NONE );
            this.orCondition = orCondition;
            this.parent = parent;
            this.attribute = attribute;
            this.type = type;

            // widgets
            this.notCheck = createNotCheck ();
            this.attributeText = createAttributeText ( attribute );
            this.fieldTypeLabel = createFieldTypeLabel ( type );
            this.assertionCombo = createAssertionCombo ();
            this.valueText = createValueText ();
            this.removeButton = createRemoveButton ();

            // layout
            RowLayout layout = new RowLayout ();
            layout.center = true;
            this.setLayout ( layout );

            parent.notifyListeners ( SWT.Resize, new org.eclipse.swt.widgets.Event () );
        }

        private Button createNotCheck ()
        {
            final Button b = new Button ( this, SWT.CHECK );
            b.setText ( "not" );
            b.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    AssertionComposite.this.orCondition.updateFilter ();
                }
            } );
            return b;
        }

        private Text createAttributeText ( final String attribute )
        {
            Text t = new Text ( this, SWT.BORDER );
            Fields field = Fields.byField ( attribute );
            if ( field == null )
            {
                t.setEditable ( true );
                t.setMessage ( "custom field ..." );
            }
            else
            {
                t.setEditable ( false );
                t.setText ( field.getName () );
            }
            t.addKeyListener ( new KeyAdapter () {
                @Override
                public void keyReleased ( final KeyEvent e )
                {
                    AssertionComposite.this.orCondition.updateFilter ();
                };
            } );
            RowData rowData = new RowData ();
            rowData.width = 132;
            t.setLayoutData ( rowData );
            return t;
        }

        private Label createFieldTypeLabel ( final Type type )
        {
            Label l = new Label ( this, SWT.NONE );
            l.setText ( type.name () );
            return l;
        }

        private Combo createAssertionCombo ()
        {
            Combo c = new Combo ( this, SWT.NONE );
            for ( Assertion assertion : Assertion.values () )
            {
                c.add ( assertion.toString () );
            }
            c.select ( 0 );
            c.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    AssertionComposite.this.orCondition.updateFilter ();
                }
            } );
            RowData rowData = new RowData ();
            rowData.width = 75;
            c.setLayoutData ( rowData );
            return c;
        }

        private Text createValueText ()
        {
            Text t = new Text ( this, SWT.BORDER );
            t.setMessage ( "argument" );
            t.addKeyListener ( new KeyAdapter () {
                @Override
                public void keyReleased ( final KeyEvent e )
                {
                    AssertionComposite.this.orCondition.updateFilter ();
                }
            } );
            RowData rowData = new RowData ();
            rowData.width = 132;
            t.setLayoutData ( rowData );
            return t;
        }

        private Button createRemoveButton ()
        {
            Button b = new Button ( this, SWT.PUSH );
            b.setText ( "Remove" );
            b.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    AssertionComposite.this.orCondition.removeAssertion ( AssertionComposite.this );
                }
            } );
            return b;
        }

        public Filter getExpression ()
        {
            String attr = this.attributeText.getText ();
            String ass = this.assertionCombo.getText ();
            String val = this.valueText.getText ();
            FilterAssertion assertion = new FilterAssertion ( attr, Assertion.fromString ( ass ), val );
            if ( val.contains ( "*" ) )
            {
                assertion.setAssertion ( Assertion.SUBSTRING );
                assertion.setValue ( val.split ( "\\*" ) );
            }
            if ( this.notCheck.getSelection () )
            {
                return FilterExpression.negate ( assertion );
            }
            return assertion;
        }
    }

    /**
     * @author jrose
     *
     */
    private static class OrCondition
    {
        private final FilterAdvancedComposite filterAdvancedComposite;

        private final CTabItem tabItem;

        private final Composite tabContent;

        private final List<AssertionComposite> assertionComposites = new ArrayList<AssertionComposite> ();

        private final AddAssertionComposite addAssertionComposite;

        private final ScrolledComposite assertionScrolledComposite;

        private final Composite assertionComposite;

        public OrCondition ( final FilterAdvancedComposite filterAdvancedComposite, final CTabFolder tabFolder )
        {
            // fields
            this.filterAdvancedComposite = filterAdvancedComposite;
            this.tabItem = new CTabItem ( tabFolder, SWT.NULL );
            this.tabItem.setText ( "a condition" );
            this.tabContent = new Composite ( tabFolder, SWT.NONE );
            this.tabItem.setControl ( this.tabContent );

            // widgets
            this.addAssertionComposite = new AddAssertionComposite ( this, this.tabContent );
            this.assertionScrolledComposite = new ScrolledComposite ( this.tabContent, SWT.V_SCROLL | SWT.NONE );
            this.assertionComposite = new Composite ( this.assertionScrolledComposite, SWT.NONE );
            this.assertionScrolledComposite.setContent ( this.assertionComposite );

            // layout
            GridLayout layout = new GridLayout ();
            layout.marginLeft = 6;
            layout.marginRight = 6;
            layout.marginTop = 6;
            layout.marginBottom = 6;
            layout.verticalSpacing = 12;
            this.tabContent.setLayout ( layout );

            GridData addAssertionCompositeLayoutData = new GridData ();
            addAssertionCompositeLayoutData.grabExcessHorizontalSpace = true;
            addAssertionCompositeLayoutData.horizontalAlignment = GridData.FILL;
            this.addAssertionComposite.setLayoutData ( addAssertionCompositeLayoutData );

            GridData assertionCompositeLayoutData = new GridData ();
            assertionCompositeLayoutData.grabExcessHorizontalSpace = true;
            assertionCompositeLayoutData.horizontalAlignment = GridData.FILL;
            assertionCompositeLayoutData.grabExcessVerticalSpace = true;
            assertionCompositeLayoutData.verticalAlignment = GridData.FILL;
            this.assertionScrolledComposite.setLayoutData ( assertionCompositeLayoutData );
            this.assertionScrolledComposite.setExpandHorizontal ( true );

            RowLayout innerlayout = new RowLayout ( SWT.VERTICAL );
            innerlayout.wrap = false;
            this.assertionComposite.setLayout ( innerlayout );
            this.assertionComposite.addListener ( SWT.Resize, new Listener () {
                public void handleEvent ( final org.eclipse.swt.widgets.Event event )
                {
                    Point s = OrCondition.this.assertionComposite.computeSize ( SWT.DEFAULT, SWT.DEFAULT );
                    OrCondition.this.assertionComposite.setSize ( s.x, s.y );
                    refreshGUI ();
                }
            } );
        }

        private void refreshGUI ()
        {
            this.filterAdvancedComposite.refreshGUI ();
        }

        public void addAssertion ()
        {
            this.assertionComposites.add ( new AssertionComposite ( this, this.assertionComposite, this.addAssertionComposite.attributeCombo.getText (), Type.Text ) );
            refreshGUI ();
            updateFilter ();
        }

        public void removeAssertion ( final AssertionComposite assertion )
        {
            this.assertionComposites.remove ( assertion );
            assertion.dispose ();
            refreshGUI ();
            if ( this.assertionComposites.size () == 0 )
            {
                this.tabItem.setText ( "a condition" );
            }
        }

        public FilterExpression getExpression ()
        {
            FilterExpression expression = new FilterExpression ();
            expression.setOperator ( Operator.AND );
            for ( AssertionComposite assertionComposite : this.assertionComposites )
            {
                expression.getFilterSet ().add ( assertionComposite.getExpression () );
            }
            return expression;
        }

        private void updateFilter ()
        {
            this.tabItem.setText ( getExpression ().toString ().replace ( "&", "&&" ) );
        }
    }

    public FilterAdvancedComposite ( final FilterChangedListener filterChangedListener, final Composite parent, final int style )
    {
        // fields
        super ( parent, style );
        this.filterChangedListener = filterChangedListener;

        // widgets
        this.tabFolder = new CTabFolder ( this, SWT.TOP | SWT.BORDER | SWT.CLOSE );
        this.addOrConditionButton = creteAddOrConditionButton ();
        addOrCondition ();

        // layout
        GridLayout layout = new GridLayout ( 1, true );
        layout.marginLeft = 6;
        layout.marginRight = 6;
        layout.marginTop = 6;
        layout.marginBottom = 6;
        layout.verticalSpacing = 12;
        this.setLayout ( layout );

        GridData tabFolderLayoutData = new GridData ();
        tabFolderLayoutData.horizontalAlignment = GridData.FILL;
        tabFolderLayoutData.grabExcessHorizontalSpace = true;
        tabFolderLayoutData.verticalAlignment = GridData.FILL;
        tabFolderLayoutData.grabExcessVerticalSpace = true;
        this.tabFolder.setLayoutData ( tabFolderLayoutData );
        this.tabFolder.setBackgroundMode ( SWT.INHERIT_FORCE );

        GridData addOrConditionButtonLayoutdata = new GridData ();
        addOrConditionButtonLayoutdata.horizontalAlignment = GridData.END;
        this.addOrConditionButton.setLayoutData ( addOrConditionButtonLayoutdata );
    }

    private Button creteAddOrConditionButton ()
    {
        Button b = new Button ( this, SWT.PUSH );
        b.setText ( "Add OR Condition" );
        b.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                addOrCondition ();
            }
        } );
        b.addDisposeListener ( new DisposeListener () {
            public void widgetDisposed ( final DisposeEvent e )
            {
                int i = FilterAdvancedComposite.this.tabFolder.getSelectionIndex ();
                FilterAdvancedComposite.this.orConditions.remove ( i );
            }
        } );
        return b;
    }

    private void addOrCondition ()
    {
        this.orConditions.add ( new OrCondition ( this, this.tabFolder ) );
        this.tabFolder.setSelection ( this.tabFolder.getItemCount () - 1 );
        refreshGUI ();
    }

    private void refreshGUI ()
    {
        this.layout ( true, true );
    }
}