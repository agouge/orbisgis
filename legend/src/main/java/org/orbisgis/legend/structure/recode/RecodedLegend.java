/*
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

package org.orbisgis.legend.structure.recode;

import org.orbisgis.coremap.renderer.se.parameter.Literal;
import org.orbisgis.coremap.renderer.se.parameter.Recode;
import org.orbisgis.coremap.renderer.se.parameter.SeParameter;
import org.orbisgis.coremap.renderer.se.parameter.ValueReference;
import org.orbisgis.coremap.renderer.se.parameter.string.StringAttribute;
import org.orbisgis.coremap.renderer.se.parameter.string.StringParameter;
import org.orbisgis.legend.structure.parameter.ParameterLegend;
import org.orbisgis.legend.structure.recode.type.TypeEvent;
import org.orbisgis.legend.structure.recode.type.TypeListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This abstract class intends to provide some useful methods for the
 * representation of parameters included in unique value classifications. We
 * can retrieve the {@link ValueReference} used to get data from the input
 * source, change the field on which the analysis is made. Finally, we can add
 * listeners that will be notified when the type of the inner parameter
 * changes.
 *
 * @author Alexis
 * @author Adam Gouge (translation from Scala to Java)
 */
abstract class RecodedLegend implements ParameterLegend {

    private String field = "";
    private List<TypeListener> listeners = new ArrayList<>();

    /**
     * Gets the {@code ValueReference} used to retrieve the input values for this
     * value classification.
     */
    public ValueReference getValueReference() {
        final SeParameter parameter = getParameter();
        if (parameter instanceof Recode) {
            final StringParameter lookupValue = ((Recode) parameter).getLookupValue();
            if (lookupValue instanceof ValueReference) {
                return (ValueReference) lookupValue;
            }
            throw new ClassCastException("We're not working with an authorized Recode2String");
        }
        return null;
    }

    /**
     * Sets the field used to make the analysis
     *
     * @param s The new field name.
     */
    public void setField(String s) {
        field = s;
        final SeParameter parameter = getParameter();
        if (parameter instanceof Recode) {
            ((Recode) parameter).setLookupValue(new StringAttribute(s));
        }
    }

    /**
     * Adds a listener that will be notified when {@link #fireTypeChanged} is called.
     *
     * @param l The listener that will be added.
     */
    public void addListener(TypeListener l) {
        listeners.add(l);
    }

    /**
     * Notifies that the actual type of the inner {@code SeParameter} has changed.
     */
    public void fireTypeChanged() {
        final TypeEvent typeEvent = new TypeEvent(this);
        for (TypeListener l : listeners) {
            l.typeChanged(typeEvent);
        }
    }

    /**
     * Accepts the given visitor.
     *
     * @param visitor A visitor for RecodedLegend instances.
     */
    public void acceptVisitor(RecodedParameterVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the keys that define this RecodedLegend.
     *
     * @return The string keys in a Set.
     */
    public Set<String> getKeys() {
        final SeParameter parameter = getParameter();
        if (parameter instanceof Literal) {
            return new HashSet<>();
        } else if (parameter instanceof Recode) {
            return ((Recode) parameter).getKeys();
        }
        return null;
    }
}
