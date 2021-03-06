/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
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
package org.orbisgis.view.edition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.orbisgis.viewapi.docking.DockingManager;
import org.orbisgis.viewapi.docking.DockingPanel;
import org.orbisgis.viewapi.docking.DockingPanelLayout;
import org.orbisgis.view.edition.dialogs.SaveDocuments;
import org.orbisgis.viewapi.edition.EditableElement;
import org.orbisgis.viewapi.edition.EditorDockable;
import org.orbisgis.viewapi.edition.EditorFactory;
import org.orbisgis.viewapi.edition.MultipleEditorFactory;
import org.orbisgis.viewapi.edition.SingleEditorFactory;

/**
 * The editor Manager is responsible of all EditorFactories.
 * This service is used to register editors and open editable elements.
 */


public class EditorManagerImpl implements org.orbisgis.viewapi.edition.EditorManager {
    private List<EditorFactory> factories = new ArrayList<EditorFactory>();
    private DockingManager dockingManager;

        public EditorManagerImpl(DockingManager dockingManager) {
                this.dockingManager = dockingManager;
        }

        @Override
        public void addEditor(EditorDockable editor) {
            dockingManager.addDockingPanel(editor);
        }

        @Override
        public void removeEditor(EditorDockable editor) {
            dockingManager.removeDockingPanel(editor.getDockingParameters().getName());
        }

        @Override
        public void addEditorFactory(EditorFactory editorFactory) {
                factories.add(editorFactory);
                if(editorFactory instanceof MultipleEditorFactory) {
                        dockingManager.registerPanelFactory(editorFactory.getId(),
                                new EditorPanelFactoryDecorator((MultipleEditorFactory)editorFactory));
                } else {
                        for(EditorDockable dockPanel : ((SingleEditorFactory)editorFactory).getSinglePanels()) {
                                dockingManager.addDockingPanel(dockPanel);
                        }
                }
        }

        @Override
        public void removeEditorFactory(EditorFactory editorFactory) {
            factories.remove(editorFactory);
            if(editorFactory instanceof MultipleEditorFactory) {
                dockingManager.unregisterPanelFactory(editorFactory.getId());
            } else {
                for(EditorDockable dockPanel : ((SingleEditorFactory)editorFactory).getSinglePanels()) {
                    dockingManager.removeDockingPanel(dockPanel.getDockingParameters().getName());
                }
            }
        }

        @Override
        public Collection<EditableElement> getEditableElements() {
                Set<EditableElement> editables = new HashSet<EditableElement>();
                for(EditorDockable editor : getEditors()) {
                        if(editor.getEditableElement()!=null) {
                                editables.add(editor.getEditableElement());
                        }
                }
                return editables;
        }
        
        @Override
        public Collection<EditorDockable> getEditors() {
                List<EditorDockable> editors = new ArrayList<EditorDockable>();
                for( DockingPanel panel : dockingManager.getPanels()) {
                        if(panel instanceof EditorDockable) {
                                editors.add((EditorDockable)panel);
                        }
                }
                return editors;
        }

        @Override
        public void openEditable(EditableElement editableElement) {
                Set<EditableElement> ignoreModifiedEditables = new HashSet<EditableElement>();
                // Open the element in editors
                for(EditorDockable editor : getEditors()) {
                        if(editor.match(editableElement)) {
                                // Get the current loaded document
                                EditableElement oldEditable = editor.getEditableElement();
                                if(oldEditable!=null) {
                                        if(oldEditable.isModified() && !ignoreModifiedEditables.contains(oldEditable)) {
                                                //Ask the user to save changes
                                                //before loosing the old editable
                                                List<EditableElement> modifiedDocs = new ArrayList<EditableElement>();
                                                modifiedDocs.add(oldEditable);
                                                SaveDocuments.CHOICE userChoice = SaveDocuments.showModal(dockingManager.getOwner(), modifiedDocs);                                                
                                                if(userChoice==SaveDocuments.CHOICE.CANCEL) {
                                                        //The user cancel the loading of elements
                                                        return;
                                                }
                                                if(oldEditable.isModified()) {
                                                        // The user do not want to save this editable, do not ask for it in for the next editor
                                                        ignoreModifiedEditables.add(oldEditable);
                                                }
                                        }
                                }                                
                                editor.setEditableElement(editableElement);
                        }
                }
                
                //Open the element in MultipleEditorFactories
                for( EditorFactory factory : factories) {
                        if(factory instanceof MultipleEditorFactory) {
                                MultipleEditorFactory mFactory = (MultipleEditorFactory)factory;
                                DockingPanelLayout data = mFactory.makeEditableLayout(editableElement);
                                if(data!=null) {
                                        dockingManager.show(mFactory.getId(), data);
                                }
                        }
                }
        }

        /**
        * Release all factories resources
        */
        public void dispose() {
                for(EditorFactory factory : factories) {
                        factory.dispose();
                }
        }
}
