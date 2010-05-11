package org.openscada.ae.ui.views.views;

public class ColumnProperties
{
    private int no;

    private int width;

    public ColumnProperties ()
    {
    }

    public ColumnProperties ( final int no, final int width )
    {
        this.no = no;
        this.width = width;
    }

    public int getNo ()
    {
        return this.no;
    }

    public void setNo ( final int no )
    {
        this.no = no;
    }

    public int getWidth ()
    {
        return this.width;
    }

    public void setWidth ( final int width )
    {
        this.width = width;
    }
}
