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
package org.orbisgis.view.toc.actions.cui.label;

import java.awt.BorderLayout;
import javax.swing.Icon;
import org.orbisgis.coremap.renderer.se.label.ExclusionRadius;
import org.orbisgis.coremap.renderer.se.label.ExclusionZone;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.view.toc.actions.cui.LegendUIComponent;
import org.orbisgis.view.toc.actions.cui.LegendUIController;
import org.orbisgis.view.toc.actions.cui.components.UomInput;
import org.orbisgis.view.toc.actions.cui.parameter.real.LegendUIMetaRealPanel;
import org.orbisgis.view.icons.OrbisGISIcon;


/**
 *
 * @author Maxence Laurent
 */
public abstract class LegendUIExclusionRadiusPanel extends LegendUIComponent implements LegendUIExclusionZoneComponent {


    private ExclusionRadius zone;
    private UomInput uom;
    private LegendUIMetaRealPanel radius;

    public LegendUIExclusionRadiusPanel(String string, LegendUIController controller, LegendUIMetaExclusionZonePanel parent, ExclusionRadius excRadius, boolean isNullable) {
        super(string, controller, parent, 0, isNullable);

        this.zone = excRadius;

        uom = new UomInput(zone);

        radius = new LegendUIMetaRealPanel("Radius", controller, this, zone.getRadius(), false) {

            @Override
            public void realChanged(RealParameter newReal) {
                zone.setRadius(newReal);
            }
        };
        radius.init();
    }

    @Override
    public Icon getIcon() {
        return OrbisGISIcon.getIcon("palette");
    }

    @Override
    protected void mountComponent() {
        editor.add(uom, BorderLayout.WEST);
        editor.add(radius, BorderLayout.EAST);
    }

    @Override
    public Class getEditedClass() {
        return ExclusionRadius.class;
    }

    @Override
    public ExclusionZone getExclusionZone() {
        return zone;
    }

}
