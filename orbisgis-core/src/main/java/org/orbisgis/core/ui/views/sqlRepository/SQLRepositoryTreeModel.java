/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.orbisgis.core.ui.views.sqlRepository;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.orbisgis.core.ui.components.resourceTree.AbstractTreeModel;
import org.orbisgis.core.ui.views.sqlRepository.persistence.Category;
import org.orbisgis.core.ui.views.sqlRepository.persistence.SqlInstruction;
import org.orbisgis.core.ui.views.sqlRepository.persistence.SqlScript;

public class SQLRepositoryTreeModel extends AbstractTreeModel {
	private final Category rootCategory;

	public SQLRepositoryTreeModel(final Category rootCategory, final JTree tree) {
		super(tree);
		this.rootCategory = rootCategory;
	}

	public Object getChild(Object parent, int index) {
		final Category parentCategory = (Category) parent;
		return parentCategory.getCategoryOrSqlScriptOrSqlInstruction().get(
				index);
	}

	public int getChildCount(Object parent) {
		final Category parentCategory = (Category) parent;
		return parentCategory.getCategoryOrSqlScriptOrSqlInstruction().size();
	}

	public int getIndexOfChild(Object parent, Object child) {
		final Category parentCategory = (Category) parent;
		return parentCategory.getCategoryOrSqlScriptOrSqlInstruction().indexOf(
				child);
	}

	public Object getRoot() {
		return rootCategory;
	}

	public boolean isLeaf(Object node) {
		return (node instanceof SqlScript) || (node instanceof SqlInstruction);
	}

	public void refresh() {
		fireEvent(new TreePath(rootCategory));
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}
}