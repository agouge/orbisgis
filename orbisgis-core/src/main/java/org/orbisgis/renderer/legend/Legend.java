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
package org.orbisgis.renderer.legend;

import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.driver.DriverException;

/**
 * Interface used by the layer model and the renderer to draw the sources
 *
 * @author Fernando Gonzalez Cortes
 *
 */
public interface Legend {

	/**
	 * Gets the symbol to draw the specified row of the DataSource specified by
	 * the setDataSource method
	 *
	 * @param row
	 * @return
	 * @throws RenderException
	 *             if there is some problem that makes impossible the drawing of
	 *             the layer
	 */
	Symbol getSymbol(long row) throws RenderException;

	/**
	 * associates the specified DataSource with this legend. This method is
	 * suitable to perform a catch of the symbols for each of the row in the
	 * DataSource
	 *
	 * @param ds
	 * @throws DriverException
	 *             If there is some problem exploring the DataSource
	 */
	void setDataSource(SpatialDataSourceDecorator ds) throws DriverException;

	/**
	 * Gets the legend's name
	 *
	 * @return
	 */
	String getName();

	/**
	 * Sets the legend's name. Only meaningful for user interface purposes
	 *
	 * @param name
	 */
	void setName(String name);

	/**
	 * Gets the name of the legend type
	 *
	 * @return
	 */
	String getLegendTypeName();

}
