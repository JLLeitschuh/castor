/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.persist;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * Registry for {@link PersistenceFactory} implementations
 * obtained from the Castor properties file and used by the
 * JDO database configuration file.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public final class PersistenceFactoryRegistry {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(PersistenceFactoryRegistry.class);
    
    /** Association between factory name and object. */
    private static Hashtable  _factories;

    /**
     * Returns a persistence factory with the specified name.
     * The factories are loaded from the Castor properties
     * file. Returns null if the named factory is not
     * supported.
     *
     * @param name The factory name
     * @return The {@link PersistenceFactory} object, null
     *  if no factory with this name exists
     */
    public static PersistenceFactory getPersistenceFactory(final String name) {
        load();
        return (PersistenceFactory) _factories.get(name);
    }

    /**
     * Returns the names of all the configured persistence
     * factories. The names can be used to obtain a factory
     * from {@link #getPersistenceFactory}.
     *
     * @return Names of persistence factories
     */
    public static String[] getFactoryNames() {
        String[]    names;
        Enumeration enumeration;

        load();
        names = new String[ _factories.size() ];
        enumeration = _factories.keys();
        for (int i = 0; i < names.length; ++i) {
            names[ i ] = (String) enumeration.nextElement();
        }
        return names;
    }


    /**
     * Load the factories from the properties file, if not loaded before.
     */
    private static synchronized void load() {
        if (_factories == null) {
            _factories = new Hashtable();

            Configuration config = CPAConfiguration.getInstance();
            Object[] objects = config.getObjectArray(
                    CPAConfiguration.PERSISTENCE_FACTORIES, config.getApplicationClassLoader());
            for (int i = 0; i < objects.length; i++) {
                PersistenceFactory factory = (PersistenceFactory) objects[i];
                _factories.put(factory.getFactoryName(), factory);
            }
        }
    }
    
    /**
     * Hide Utility Class Constructor.
     */
    private PersistenceFactoryRegistry() { }
}