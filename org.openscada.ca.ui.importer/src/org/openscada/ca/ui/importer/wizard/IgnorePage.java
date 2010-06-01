package org.openscada.ca.ui.importer.wizard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;
import org.openscada.ca.ui.importer.data.DiffController;

public class IgnorePage extends WizardPage
{

    private final DiffController mergeController;

    private TableViewer factoriesViewer;

    private TreeViewer fieldsViewer;

    public IgnorePage ( final DiffController mergeController )
    {
        super ( "ignorePage" );//$NON-NLS-1$
        setTitle ( Messages.IgnorePage_Title );

        this.mergeController = mergeController;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new FillLayout () );

        final TabFolder folder = new TabFolder ( wrapper, SWT.TOP );

        {
            final TabItem item = new TabItem ( folder, SWT.NONE );
            item.setText ( Messages.IgnorePage_TabItem_Factories_Text );
            item.setControl ( createFactoriesTab ( folder ) );
        }

        {
            final TabItem item = new TabItem ( folder, SWT.NONE );
            item.setText ( Messages.IgnorePage_TabItem_Fields_Text );
            item.setControl ( createFieldsTab ( folder ) );
        }

        setControl ( wrapper );
    }

    @Override
    public void setVisible ( final boolean visible )
    {
        super.setVisible ( visible );

        if ( visible )
        {
            final Object[] data = this.mergeController.makeKnownFactories ().toArray ();
            this.factoriesViewer.setInput ( data );

            final Set<String> factories = this.mergeController.getIgnoreFactories ();

            // select all
            for ( final TableItem item : this.factoriesViewer.getTable ().getItems () )
            {
                item.setChecked ( factories.contains ( item.getData () ) );
            }

            // set ignore fields
            this.fieldsViewer.setInput ( this.mergeController.getIgnoreFields () );
        }
    }

    private Control createFieldsTab ( final Composite parent )
    {
        this.fieldsViewer = new TreeViewer ( parent );

        this.fieldsViewer.setContentProvider ( new ITreeContentProvider () {

            public void inputChanged ( final Viewer viewer, final Object oldInput, final Object newInput )
            {
            }

            public void dispose ()
            {
            }

            public Object[] getElements ( final Object inputElement )
            {
                return getChildren ( inputElement );
            }

            public boolean hasChildren ( final Object element )
            {
                final Object[] childs = getChildren ( element );
                if ( childs == null )
                {
                    return false;
                }
                return childs.length > 0;
            }

            public Object getParent ( final Object element )
            {
                return null;
            }

            @SuppressWarnings ( "unchecked" )
            public Object[] getChildren ( final Object parentElement )
            {
                if ( parentElement instanceof Map<?, ?> )
                {
                    return ( (Map<?, ?>)parentElement ).entrySet ().toArray ();
                }
                else if ( parentElement instanceof Map.Entry<?, ?> )
                {
                    return ( (Map.Entry<?, Collection<Object>>)parentElement ).getValue ().toArray ();
                }
                return null;
            }
        } );
        this.fieldsViewer.setInput ( this.mergeController.getIgnoreFields () );
        this.fieldsViewer.setLabelProvider ( new LabelProvider () {
            @Override
            public String getText ( final Object element )
            {
                if ( element instanceof Map.Entry<?, ?> )
                {
                    return String.format ( "%s", ( (Map.Entry<?, ?>)element ).getKey () );
                }
                return String.format ( "%s", element );
            }
        } );
        this.fieldsViewer.setAutoExpandLevel ( TreeViewer.ALL_LEVELS );

        return this.fieldsViewer.getControl ();
    }

    private Control createFactoriesTab ( final Composite parent )
    {
        this.factoriesViewer = new TableViewer ( parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK );

        this.factoriesViewer.setContentProvider ( new ArrayContentProvider () );
        this.factoriesViewer.getControl ().addListener ( SWT.Selection, new Listener () {
            public void handleEvent ( final Event event )
            {
                IgnorePage.this.mergeController.setIgnoreFactories ( gatherIgnoredFactories () );
            }
        } );

        return this.factoriesViewer.getControl ();
    }

    protected Set<String> gatherIgnoredFactories ()
    {
        final Set<String> result = new HashSet<String> ();
        for ( final TableItem item : this.factoriesViewer.getTable ().getItems () )
        {
            if ( item.getChecked () )
            {
                final String data = (String)item.getData ();
                result.add ( data );
            }
        }
        return result;
    }

}
