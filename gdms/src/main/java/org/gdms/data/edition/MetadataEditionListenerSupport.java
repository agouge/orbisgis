/**
 * The GDMS library (Generic Datasource Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...).
 *
 * Gdms is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV FR CNRS 2488
 *
 * This file is part of Gdms.
 *
 * Gdms is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * info@orbisgis.org
 */
package org.gdms.data.edition;

import java.util.ArrayList;
import java.util.List;

import org.gdms.data.DataSource;

public final class MetadataEditionListenerSupport {

        private DataSource dataSource;
        private List<MetadataEditionListener> listeners = new ArrayList<MetadataEditionListener>();

        public MetadataEditionListenerSupport(DataSource ds) {
                this.dataSource = ds;
        }

        public void callAddField(int fieldIndex) {
                FieldEditionEvent e = new FieldEditionEvent(fieldIndex, dataSource);

                for (MetadataEditionListener listener : listeners) {
                        listener.fieldAdded(e);
                }
        }

        public void callRemoveField(int fieldIndex) {
                FieldEditionEvent e = new FieldEditionEvent(fieldIndex, dataSource);

                for (MetadataEditionListener listener : listeners) {
                        listener.fieldRemoved(e);
                }
        }

        public void callModifyField(int fieldIndex) {
                FieldEditionEvent e = new FieldEditionEvent(fieldIndex, dataSource);

                for (MetadataEditionListener listener : listeners) {
                        listener.fieldModified(e);
                }
        }

        public void addEditionListener(MetadataEditionListener listener) {
                listeners.add(listener);
        }

        public void removeEditionListener(MetadataEditionListener listener) {
                listeners.remove(listener);
        }
}