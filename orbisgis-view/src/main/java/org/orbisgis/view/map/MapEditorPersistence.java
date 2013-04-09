/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. 
 * 
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 * 
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
 * 
 * This file is part of OrbisGIS.
 * 
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.view.map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.orbisgis.view.docking.DockingPanelLayout;
import org.orbisgis.view.util.PropertyHost;
import org.orbisgis.view.util.XElement;

/**
 * The map editor stores the last open default map context
 * @author Nicolas Fortin
 */
public class MapEditorPersistence implements DockingPanelLayout, Serializable, PropertyHost {
        private static final long serialVersionUID = 2L; // One by new property
        /**
         * Map Context file name to show on application start.
         * {@link org.orbisgis.view.map.MapEditorPersistence#getDefaultMapContext()}
         */
        public static final String PROP_DEFAULTMAPCONTEXT = "defaultMapContext";
        /**
         * Property of server uri list {@link org.orbisgis.view.map.MapEditorPersistence#getMapCatalogUrlList()}
         */
        public static final String PROP_SERVER_URI_LIST = "mapCatalogUrlList";
        private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        
        private String defaultMapContext = "map.ows"; //Default map context on application start
        private List<String> mapCatalogUrlList = new ArrayList<String>();
        
        // XML consts
        private static final String URL_LIST_NODE = "mapCatalogUrlList";
        private static final String URL_NODE = "mapcatalog";
        private static final String URL_NODE_PROPERTY = "url";

        /**
         * Update the default map context
         * @param defaultMapContext 
         */
        final public void setDefaultMapContext(String defaultMapContext) {
                String oldDefaultMapContext = this.defaultMapContext;
                this.defaultMapContext = defaultMapContext;
                propertyChangeSupport.firePropertyChange(PROP_DEFAULTMAPCONTEXT,oldDefaultMapContext,defaultMapContext);
        }

        /**
         * Set the map context server list
         * @param mapCatalogUrlList 
         */
        public void setMapCatalogUrlList(List<String> mapCatalogUrlList) {
                List<String> oldList = this.mapCatalogUrlList;
                this.mapCatalogUrlList = new ArrayList<String>(mapCatalogUrlList);
                propertyChangeSupport.firePropertyChange(PROP_SERVER_URI_LIST,oldList,mapCatalogUrlList);
        }

        /**
         * Retrieve the list of map context
         * @return 
         */
        public List<String> getMapCatalogUrlList() {
                return Collections.unmodifiableList(mapCatalogUrlList);
        }
        
        /**
         * 
         * @return The last loaded map context path, or the default one
         */
        public String getDefaultMapContext() {
                return defaultMapContext;
        }
        
        @Override
        public void writeStream(DataOutputStream out) throws IOException {
                out.writeLong(serialVersionUID);
                out.writeUTF(defaultMapContext);
                out.writeInt(mapCatalogUrlList.size());
                for(String mcUrl : mapCatalogUrlList) {
                        out.writeUTF(mcUrl);
                }
        }

        @Override
        public void readStream(DataInputStream in) throws IOException {
                // Check version
                long version = in.readLong();
                if(version>=1) {
                        setDefaultMapContext(in.readUTF());
                        mapCatalogUrlList.clear();
                        if(version==serialVersionUID) {
                                int size = in.readInt();
                                for(int i=0;i<size;i++) {
                                        mapCatalogUrlList.add(in.readUTF());
                                }
                        }
                }
        }

        @Override
        public void writeXML(XElement element) {
                element.addLong("serialVersionUID", serialVersionUID);
                element.addString(PROP_DEFAULTMAPCONTEXT, defaultMapContext);
                XElement urlList = element.addElement(URL_LIST_NODE);
                for(String mcUrl : mapCatalogUrlList) {
                        urlList.addElement(URL_NODE).addString(URL_NODE_PROPERTY, mcUrl);
                }
        }

        @Override
        public void readXML(XElement element) {
                long version = element.getLong("serialVersionUID");
                if(version>=1) {
                        setDefaultMapContext(element.getString(PROP_DEFAULTMAPCONTEXT));
                        List<String> mapCatalogUrlList = new ArrayList<String>();
                        if(version==serialVersionUID) {
                                for(XElement map : element.getElement(URL_LIST_NODE).children()) {
                                        if(map.getName().equals(URL_NODE)) {
                                            mapCatalogUrlList.add(map.getString(URL_NODE_PROPERTY));
                                        }
                                }
                        }
                        setMapCatalogUrlList(mapCatalogUrlList);
                }
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
                propertyChangeSupport.addPropertyChangeListener(listener);
        }

        @Override
        public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
                propertyChangeSupport.addPropertyChangeListener(prop,listener);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
                propertyChangeSupport.removePropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener(String prop, PropertyChangeListener listener) {
                propertyChangeSupport.removePropertyChangeListener(prop,listener);
        }
}
