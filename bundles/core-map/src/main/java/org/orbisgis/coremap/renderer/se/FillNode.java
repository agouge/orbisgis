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
package org.orbisgis.coremap.renderer.se;

import org.orbisgis.coremap.renderer.se.fill.Fill;

/**
 * Interface to be implemented by every node that can contain a <code>Fill</code> element.
 * @author Maxence Laurent, Alexis Guéganno
 */
public interface FillNode {

        /**
         * Replace the current fill with the one given in argument.
         * @param f 
         * A {@link Fill} implementation. It's up to the realization to decide
         * if it can be null or not.
         */
	void setFill(Fill f);
        
        /**
         * Gets the {@code Fill} associated to this {@code FillNode}.
         * @return 
         * A {@link Fill} instance.
         */
	Fill getFill();
}
