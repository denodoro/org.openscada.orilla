package org.openscada.da.ui.connection.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.openscada.da.ui.connection.data.Item;

/**
 * Class for serializing gadgets to/from a byte array
 */
public class ItemTransfer extends ByteArrayTransfer
{
    private static ItemTransfer instance = new ItemTransfer ();

    private static final String TYPE_NAME = "openscada-da-item-transfer-format-v2"; //$NON-NLS-1$

    private static final int TYPEID = registerType ( TYPE_NAME );

    /**
     * Returns the singleton gadget transfer instance.
     */
    public static ItemTransfer getInstance ()
    {
        return instance;
    }

    /**
     * Avoid explicit instantiation
     */
    private ItemTransfer ()
    {
    }

    protected Item[] fromByteArray ( final byte[] bytes )
    {
        try
        {
            final ObjectInputStream in = new ObjectInputStream ( new ByteArrayInputStream ( bytes ) );
            return (Item[])in.readObject ();
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    /*
     * Method declared on Transfer.
     */
    @Override
    protected int[] getTypeIds ()
    {
        return new int[] { TYPEID };
    }

    /*
     * Method declared on Transfer.
     */
    @Override
    protected String[] getTypeNames ()
    {
        return new String[] { TYPE_NAME };
    }

    /*
     * Method declared on Transfer.
     */
    @Override
    protected void javaToNative ( final Object object, final TransferData transferData )
    {
        final byte[] bytes = toByteArray ( (Item[])object );
        if ( bytes != null )
        {
            super.javaToNative ( bytes, transferData );
        }
    }

    /*
     * Method declared on Transfer.
     */
    @Override
    protected Object nativeToJava ( final TransferData transferData )
    {
        final byte[] bytes = (byte[])super.nativeToJava ( transferData );
        return fromByteArray ( bytes );
    }

    protected byte[] toByteArray ( final Item[] items )
    {

        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream ();

        byte[] bytes = null;

        try
        {
            final ObjectOutputStream out = new ObjectOutputStream ( byteOut );
            out.writeObject ( items );
            out.close ();
            bytes = byteOut.toByteArray ();
        }
        catch ( final IOException e )
        {
            //when in doubt send nothing
        }
        return bytes;
    }

}