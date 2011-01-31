/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.da.client.signalgenerator.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.openscada.da.client.signalgenerator.SimulationTarget;

public class BooleanGeneratorPage implements GeneratorPage
{

    private Spinner iterationsSpinner;

    private Spinner startDelaySpinner;

    private Spinner endDelaySpinner;

    private Composite parent;

    private BooleanGenerator generator;

    private SimulationTarget target;

    public void createPage ( final Composite parent )
    {
        this.parent = parent;
        parent.setLayout ( new FillLayout ( SWT.VERTICAL ) );
        createTimedGroup ( parent );
        update ();
    }

    private void createTimedGroup ( final Composite parent )
    {
        final Group group = new Group ( parent, SWT.BORDER );
        group.setText ( Messages.getString ( "BooleanGeneratorPage.groupTimed.text" ) ); //$NON-NLS-1$

        group.setLayout ( new GridLayout ( 6, false ) );
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.beforeDelay" ) ); //$NON-NLS-1$
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.0to1" ) ); //$NON-NLS-1$
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.afterDelay" ) ); //$NON-NLS-1$
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.1to0" ) ); //$NON-NLS-1$
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.iterations" ) ); //$NON-NLS-1$
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.after" ) ); //$NON-NLS-1$

        this.startDelaySpinner = new Spinner ( group, SWT.BORDER );
        this.startDelaySpinner.setValues ( 1000, 0, Integer.MAX_VALUE, 0, 100, 1000 );
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.transition" ) ); //$NON-NLS-1$
        this.endDelaySpinner = new Spinner ( group, SWT.BORDER );
        this.endDelaySpinner.setValues ( 1000, 0, Integer.MAX_VALUE, 0, 100, 1000 );
        new Label ( group, SWT.NONE ).setText ( Messages.getString ( "BooleanGeneratorPage.timedGroup.transition" ) ); //$NON-NLS-1$
        this.iterationsSpinner = new Spinner ( group, SWT.BORDER );
        this.iterationsSpinner.setValues ( 100, 0, Integer.MAX_VALUE, 0, 5, 100 );
    }

    public void start ()
    {
        final int startDelay = this.startDelaySpinner.getSelection ();
        final int endDelay = this.endDelaySpinner.getSelection ();
        final int iterations = this.iterationsSpinner.getSelection ();
        this.generator = new BooleanGenerator ( this.parent.getDisplay (), this.target );
        this.generator.setStartDelay ( startDelay );
        this.generator.setEndDelay ( endDelay );
        this.generator.setIterations ( iterations );
        this.generator.start ();
    }

    public void stop ()
    {
        this.generator.dispose ();
        this.generator = null;
    }

    public void dispose ()
    {
        if ( this.generator != null )
        {
            this.generator.dispose ();
            this.generator = null;
        }
    }

    public void setTarget ( final SimulationTarget target )
    {
        this.target = target;
        update ();
    }

    private void update ()
    {
        this.startDelaySpinner.setEnabled ( this.generator == null );
        this.endDelaySpinner.setEnabled ( this.generator == null );
        this.iterationsSpinner.setEnabled ( this.generator == null );
    }

}
