package org.openscada.ae.ui.views.export.excel.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jxl.CellView;
import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.export.excel.Activator;
import org.openscada.ae.ui.views.export.excel.Cell;
import org.openscada.ae.ui.views.export.excel.config.DynamicField;
import org.openscada.ae.ui.views.export.excel.config.Field;
import org.openscada.ae.ui.views.export.excel.config.StaticField;
import org.openscada.ui.databinding.AdapterHelper;

public class ExportImpl
{

    private IStructuredSelection selection;

    private File file;

    public ExportImpl ()
    {
    }

    public void setSelection ( final IStructuredSelection selection )
    {
        this.selection = selection;
    }

    public void setFile ( final File file )
    {
        this.file = file;
    }

    public void check ()
    {
        if ( this.file == null )
        {
            throw new IllegalStateException ( "No output file selected" );
        }
        if ( this.selection == null )
        {
            throw new IllegalStateException ( "No events selected" );
        }
    }

    public IStatus write ( final IProgressMonitor monitor )
    {
        if ( this.selection == null )
        {
            return Status.OK_STATUS;
        }

        if ( this.file.exists () )
        {
            if ( !this.file.delete () )
            {
                return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, String.format ( "Failed to delete file: %s", this.file ) );
            }
        }

        final List<Event> events = new ArrayList<Event> ();

        final Iterator<?> i = this.selection.iterator ();

        while ( i.hasNext () )
        {
            final Event e = (Event)AdapterHelper.adapt ( i.next (), Event.class );
            if ( e != null )
            {
                events.add ( e );
            }
        }

        try
        {
            return storeExcel ( this.file, events, getFields ( events ), monitor );
        }
        catch ( final Exception e )
        {
            return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to export", e );
        }
    }

    private List<Field> getFields ( final Collection<Event> events )
    {
        final Set<Field> fields = new HashSet<Field> ();

        fields.add ( new StaticField ( "id" ) {
            public void render ( final Event event, final Cell cell )
            {
                cell.setDataAsText ( event.getId ().toString () );
            }
        } );
        fields.add ( new StaticField ( "sourceTimestamp" ) {

            public void render ( final Event event, final Cell cell )
            {
                cell.setDataAsDate ( event.getSourceTimestamp () );
            }
        } );
        fields.add ( new StaticField ( "entryTimestamp" ) {
            public void render ( final Event event, final Cell cell )
            {
                cell.setDataAsDate ( event.getEntryTimestamp () );
            }
        } );

        for ( final Event event : events )
        {
            for ( final String key : event.getAttributes ().keySet () )
            {
                fields.add ( new DynamicField ( key ) );
            }
        }

        return new ArrayList<Field> ( fields );
    }

    private IStatus storeExcel ( final File file, final List<Event> events, final List<Field> columns, final IProgressMonitor monitor ) throws IOException, WriteException
    {
        final WorkbookSettings settings = new WorkbookSettings ();
        settings.setEncoding ( "UTF-8" );
        settings.setAutoFilterDisabled ( false );

        WritableWorkbook workbook = null;

        try
        {
            monitor.beginTask ( "Exporting events", events.size () + 3 );

            try
            {
                monitor.subTask ( "Creating workbook" );
                workbook = jxl.Workbook.createWorkbook ( file, settings );
                monitor.worked ( 1 );

                final WritableSheet sheet = createSheet ( events, workbook, columns );
                monitor.worked ( 1 );

                monitor.setTaskName ( "Exporting events" );

                for ( int i = 0; i < events.size (); i++ )
                {
                    final Event e = events.get ( i );
                    for ( int j = 0; j < columns.size (); j++ )
                    {
                        final Field field = columns.get ( j );
                        final ExcelCell cell = new ExcelCell ( i + 1, j );
                        field.render ( e, cell );

                        final WritableCell dataCell = cell.getCell ();
                        if ( dataCell != null )
                        {
                            sheet.addCell ( dataCell );
                        }

                    }
                    monitor.worked ( 1 );
                    if ( monitor.isCanceled () )
                    {
                        return Status.CANCEL_STATUS;
                    }
                }
            }
            finally
            {
                monitor.subTask ( "Closing file" );
                if ( workbook != null )
                {
                    workbook.write ();
                    workbook.close ();
                }
                monitor.worked ( 1 );
            }
        }
        finally
        {
            monitor.done ();
        }

        return Status.OK_STATUS;
    }

    private WritableSheet createSheet ( final List<Event> events, final WritableWorkbook workbook, final List<Field> columns ) throws RowsExceededException, WriteException
    {
        final WritableSheet sheet = workbook.createSheet ( "Events", 0 );
        sheet.setPageSetup ( PageOrientation.LANDSCAPE );

        final HeaderFooter header = new HeaderFooter ();
        header.getLeft ().append ( "A&E data export" );
        header.getRight ().appendDate ();
        header.getRight ().append ( " " );
        header.getRight ().appendTime ();
        sheet.getSettings ().setHeader ( header );

        final HeaderFooter footer = new HeaderFooter ();
        footer.getLeft ().append ( String.format ( "%s entries", events.size () ) );

        footer.getRight ().append ( "Page " );
        footer.getRight ().appendPageNumber ();
        footer.getRight ().append ( " of " );
        footer.getRight ().appendTotalPages ();

        sheet.getSettings ().setFooter ( footer );
        sheet.getSettings ().setVerticalFreeze ( 1 );

        makeHeader ( columns, sheet );

        return sheet;
    }

    private void makeHeader ( final List<Field> columns, final WritableSheet sheet ) throws WriteException, RowsExceededException
    {
        final WritableFont headerFont = new WritableFont ( WritableWorkbook.ARIAL_10_PT );
        headerFont.setBoldStyle ( WritableFont.BOLD );
        headerFont.setColour ( Colour.WHITE );

        final WritableCellFormat cf = new WritableCellFormat ();
        cf.setFont ( headerFont );
        cf.setBackground ( Colour.AUTOMATIC );

        for ( int i = 0; i < columns.size (); i++ )
        {
            final Field field = columns.get ( i );
            final Label headerCell = new Label ( i, 0, field.getHeader () );
            headerCell.setCellFormat ( cf );

            final CellView cv = new CellView ();
            cv.setAutosize ( true );
            sheet.setColumnView ( i, cv );

            sheet.addCell ( headerCell );
        }
    }

    public File getFile ()
    {
        return this.file;
    }
}
