package org.openscada.ae.ui.views.dialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openscada.core.Variant;
import org.openscada.utils.filter.Assertion;
import org.openscada.utils.filter.Filter;
import org.openscada.utils.filter.FilterAssertion;
import org.openscada.utils.filter.FilterExpression;
import org.openscada.utils.filter.Operator;
import org.openscada.utils.lang.Pair;

public class FilterQueryByExampleComposite extends Composite
{
    private interface FilterModified
    {
        public void onModified ();
    }

    private interface FieldEntry
    {
        public abstract Filter asExpression ();

        public abstract boolean isEmpty ();

        public abstract void clear ();

        public abstract void focus ();
    }

    private static class DateFieldEntry implements FieldEntry
    {
        private final String field;

        private final Label captionLabel;

        private final Button notCheckBox;

        private final Label fromLabel;

        private final CDateTime fromDate;

        private final Label toLabel;

        private final CDateTime toDate;

        private static final DateFormat isoDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

        public DateFieldEntry ( final Composite parent, final String field, final String caption, final FilterModified filterModified )
        {
            this.field = field;
            this.captionLabel = new Label ( parent, SWT.NONE );
            this.captionLabel.setText ( caption );
            this.notCheckBox = new Button ( parent, SWT.CHECK );
            this.notCheckBox.setText ( "not" );
            this.notCheckBox.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    filterModified.onModified ();
                }
            } );
            this.fromLabel = new Label ( parent, SWT.NONE );
            this.fromLabel.setText ( "between" );
            this.fromDate = new CDateTime ( parent, CDT.BORDER | CDT.DATE_MEDIUM | CDT.TIME_MEDIUM | CDT.SPINNER | CDT.CLOCK_24_HOUR | CDT.MULTI | CDT.DROP_DOWN );
            this.fromDate.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    filterModified.onModified ();
                }
            } );
            this.toLabel = new Label ( parent, SWT.NONE );
            this.toLabel.setText ( "and" );
            this.toDate = new CDateTime ( parent, CDT.BORDER | CDT.DATE_MEDIUM | CDT.TIME_MEDIUM | CDT.SPINNER | CDT.CLOCK_24_HOUR | CDT.MULTI | CDT.DROP_DOWN );
            this.toDate.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    filterModified.onModified ();
                }
            } );
        }

        public Filter asExpression ()
        {
            FilterAssertion assertionFrom = null;
            FilterAssertion assertionTo = null;
            FilterExpression expression = new FilterExpression ();
            expression.setOperator ( Operator.AND );
            if ( this.fromDate.getSelection () != null )
            {
                assertionFrom = new FilterAssertion ( this.field, Assertion.GREATEREQ, isoDateFormat.format ( this.fromDate.getSelection () ) );
                expression.getFilterSet ().add ( assertionFrom );
            }
            if ( this.toDate.getSelection () != null )
            {
                assertionTo = new FilterAssertion ( this.field, Assertion.LESSEQ, isoDateFormat.format ( this.toDate.getSelection () ) );
                expression.getFilterSet ().add ( assertionTo );
            }
            if ( this.notCheckBox.getSelection () )
            {
                return FilterExpression.negate ( expression );
            }
            return expression;
        }

        public boolean isEmpty ()
        {
            return ( this.fromDate.getSelection () == null ) && ( this.toDate.getSelection () == null );
        }

        public void clear ()
        {
            this.fromDate.setSelection ( null );
            this.toDate.setSelection ( null );
        }

        public void focus ()
        {
            this.fromDate.setFocus ();
        }
    }

    private static class TextFieldEntry implements FieldEntry
    {
        protected final String field;

        protected final Label captionLabel;

        protected final Button notCheckBox;

        protected final Text textText;

        public TextFieldEntry ( final Composite parent, final String field, final String caption, final FilterModified filterModified )
        {
            this.field = field;
            this.captionLabel = new Label ( parent, SWT.NONE );
            this.captionLabel.setText ( caption );
            this.notCheckBox = new Button ( parent, SWT.CHECK );
            this.notCheckBox.setText ( "not" );
            this.notCheckBox.addSelectionListener ( new SelectionListener () {
                public void widgetSelected ( final SelectionEvent e )
                {
                    filterModified.onModified ();
                }

                public void widgetDefaultSelected ( final SelectionEvent e )
                {
                }
            } );
            this.textText = new Text ( parent, SWT.BORDER );
            GridData layoutData = new GridData ();
            layoutData.horizontalAlignment = GridData.FILL;
            layoutData.horizontalSpan = 4;
            layoutData.grabExcessHorizontalSpace = true;
            this.textText.setLayoutData ( layoutData );
            this.textText.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    filterModified.onModified ();
                }
            } );
            this.textText.addKeyListener ( new KeyAdapter () {
                @Override
                public void keyReleased ( final KeyEvent e )
                {
                    filterModified.onModified ();
                }
            } );
        }

        public Filter asExpression ()
        {
            FilterAssertion assertion = null;
            if ( this.textText.getText ().contains ( "*" ) )
            {
                assertion = new FilterAssertion ( this.field, Assertion.SUBSTRING, this.textText.getText ().split ( "\\*" ) );
            }
            else
            {
                assertion = new FilterAssertion ( this.field, Assertion.EQUALITY, this.textText.getText () );
            }
            if ( this.notCheckBox.getSelection () )
            {
                return FilterExpression.negate ( assertion );
            }
            return assertion;
        }

        public boolean isEmpty ()
        {
            return this.textText.getText ().trim ().length () == 0;
        }

        public void clear ()
        {
            this.textText.setText ( "" );
        }

        public void focus ()
        {
            this.textText.setFocus ();
        }
    }

    private static class NumberFieldEntry extends TextFieldEntry
    {
        public NumberFieldEntry ( final Composite parent, final String field, final String caption, final FilterModified filterModified )
        {
            super ( parent, field, caption, filterModified );
        }

        @Override
        public Filter asExpression ()
        {
            FilterAssertion assertion = null;
            try
            {
                Long l = Long.parseLong ( this.textText.getText () );
                assertion = new FilterAssertion ( this.field, Assertion.EQUALITY, new Variant ( l ).toString () );
            }
            catch ( NumberFormatException el )
            {
                try
                {
                    Double d = Double.parseDouble ( this.textText.getText () );
                    assertion = new FilterAssertion ( this.field, Assertion.EQUALITY, new Variant ( d ).toString () );
                }
                catch ( NumberFormatException ed )
                {
                    assertion = new FilterAssertion ( this.field, Assertion.EQUALITY, new Variant ( this.textText.getText () ) );
                }
            }
            if ( this.notCheckBox.getSelection () )
            {
                return FilterExpression.negate ( assertion );
            }
            return assertion;
        }
    }

    // clear button
    final Button clearButton;

    private final Map<String, FieldEntry> fields = new HashMap<String, FieldEntry> ();

    public FilterQueryByExampleComposite ( final FilterChangedListener filterChangedListener, final Composite parent, final int style )
    {
        super ( parent, style );

        GridLayout layout = new GridLayout ();
        layout.numColumns = 6;
        layout.marginHeight = 12;
        layout.marginWidth = 12;
        this.setLayout ( layout );

        FilterModified filterModified = new FilterModified () {
            public void onModified ()
            {
                String filterString = toFilter ().toString ();
                System.err.println ( filterString );
                filterChangedListener.onFilterChanged ( new Pair<SearchType, String> ( SearchType.SIMPLE, filterString ) );
            };
        };
        this.fields.put ( "sourceTimestamp", new DateFieldEntry ( this, "sourceTimestamp", "Created at", filterModified ) );
        this.fields.put ( "entryTimestamp", new DateFieldEntry ( this, "entryTimestamp", "Created at", filterModified ) );

        this.fields.put ( "message", new TextFieldEntry ( this, "message", "Message", filterModified ) );
        this.fields.put ( "monitorType", new TextFieldEntry ( this, "monitorType", "Monitor", filterModified ) );
        this.fields.put ( "eventType", new TextFieldEntry ( this, "eventType", "Event", filterModified ) );
        this.fields.put ( "value", new NumberFieldEntry ( this, "value", "Value", filterModified ) );
        this.fields.put ( "priority", new NumberFieldEntry ( this, "priority", "Priority", filterModified ) );
        this.fields.put ( "source", new TextFieldEntry ( this, "source", "Source", filterModified ) );
        this.fields.put ( "actorType", new TextFieldEntry ( this, "message", "Actor Type", filterModified ) );
        this.fields.put ( "actorName", new TextFieldEntry ( this, "actorName", "Actor", filterModified ) );

        // clear button
        this.clearButton = new Button ( this, SWT.PUSH );
        this.clearButton.setText ( "Clear" );
        GridData clearButtonLayoutData = new GridData ();
        clearButtonLayoutData.horizontalAlignment = SWT.BEGINNING;
        clearButtonLayoutData.horizontalSpan = 6;
        this.clearButton.setLayoutData ( clearButtonLayoutData );
        this.clearButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                for ( FieldEntry fieldEntry : FilterQueryByExampleComposite.this.fields.values () )
                {
                    fieldEntry.clear ();
                }
                FilterQueryByExampleComposite.this.fields.get ( "sourceTimestamp" ).focus ();
            }
        } );
        this.fields.get ( "sourceTimestamp" ).focus ();
    }

    private Filter toFilter ()
    {
        FilterExpression filter = new FilterExpression ();
        filter.setOperator ( Operator.AND );
        for ( FieldEntry fieldEntry : this.fields.values () )
        {
            if ( !fieldEntry.isEmpty () )
            {
                filter.getFilterSet ().add ( fieldEntry.asExpression () );
            }
        }
        return filter;
    }
}
