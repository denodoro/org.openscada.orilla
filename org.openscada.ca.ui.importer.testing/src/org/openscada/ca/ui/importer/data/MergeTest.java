/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.ca.ui.importer.data;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.openscada.ca.DiffEntry;

public class MergeTest
{

    private DiffController mergeController;

    @Before
    public void setup ()
    {
        this.mergeController = new DiffController ();
    }

    @Test
    public void test2 () throws JsonParseException, JsonMappingException, IOException
    {
        final ObjectMapper mapper = new ObjectMapper ();
        final Object result = mapper.readValue ( new File ( "/home/jens/workspace_openscada_015/udtconv1/script/dave/data.json" ), HashMap.class );
        System.out.println ( result );
    }

    @Test
    public void test1 ()
    {
        // local data
        final Map<String, Map<String, Map<String, String>>> localData = new HashMap<String, Map<String, Map<String, String>>> ();
        final HashMap<String, Map<String, String>> factoryA = new HashMap<String, Map<String, String>> ();

        final Map<String, String> cfgA1 = new HashMap<String, String> ();
        cfgA1.put ( "foo", "bar" );
        factoryA.put ( "cfg1", cfgA1 );

        localData.put ( "factoryA", factoryA );

        // remote data

        final Map<String, Map<String, Map<String, String>>> remoteData = new HashMap<String, Map<String, Map<String, String>>> ();
        final HashMap<String, Map<String, String>> factoryB = new HashMap<String, Map<String, String>> ();

        final Map<String, String> cfgB1 = new HashMap<String, String> ();
        cfgB1.put ( "foo", "bar" );
        factoryB.put ( "cfg1", cfgB1 );

        remoteData.put ( "factoryB", factoryB );

        this.mergeController.setLocalData ( localData );
        this.mergeController.setRemoteData ( remoteData );

        final Collection<DiffEntry> result = this.mergeController.merge ( new NullProgressMonitor () );
        System.out.println ( result );
    }
}
