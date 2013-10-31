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
package org.gdms;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.orbisgis.utils.FileUtils;

public abstract class TestData<T, G> {

	public static final int HSQLDB = 1;

	public static final int CSV = 2;

	public static final int DBF = 4;

	public static final int SHAPEFILE = 8;

	public static final int NONE = 0;

	public static final int H2 = 16;

	protected String name;

	private long rowCount;

	private boolean isDB;

	private String noPKField;

	private boolean hasRepeatedRows;

	/* Optional attributes */

	private String stringField;

	private String nullField;

	private NumericInfo numericInfo;

	protected  String spatialField;
        private PKInfo<T> info;

	

	private boolean write;

	private int driver;

	public TestData(String name, boolean write, int driver, long rowCount,
			boolean isDB, String noPKField, boolean hasRepeatedRows) {
		super();
		this.name = name;
		this.write = write;
		this.driver = driver;
		this.rowCount = rowCount;
		this.isDB = isDB;
		this.noPKField = noPKField;
		this.hasRepeatedRows = hasRepeatedRows;
	}

	public class NumericInfo {
		private String numericFieldName;

		private double min;

		private double max;

		public NumericInfo(String numericFieldName, double min, double max) {
			super();
			this.numericFieldName = numericFieldName;
			this.min = min;
			this.max = max;
		}

		public double getMax() {
			return max;
		}

		public double getMin() {
			return min;
		}

		public String getNumericFieldName() {
			return numericFieldName;
		}
	}

	public boolean hasRepeatedRows() {
		return hasRepeatedRows;
	}

	public boolean isDB() {
		return isDB;
	}

	public String getNoPKField() {
		return noPKField;
	}

        public class PKInfo<T> {

               private String pkField;
                private T newPK;

                public PKInfo(String pkField, T newPK) {
                        super();
                        this.pkField = pkField;
                        this.newPK = newPK;
                }

                public T getNewPK() {
                        return newPK;
                }

                public String getPkField() {
                        return pkField;
                }
        }

        public PKInfo<T> getPKInfo() {
                return info;
        }

        public void setPKInfo(String pkField, T newPK) {
                this.info = new PKInfo(pkField, newPK);
        }



	public long getRowCount() {
		return rowCount;
	}

	public String getSpatialField() {
		return spatialField;
	}

	public String getNullField() {
		return nullField;
	}

	public void setNullField(String nullField) {
		this.nullField = nullField;
	}

	public NumericInfo getNumericInfo() {
		return numericInfo;
	}

	public void setNumericInfo(String fieldName, double min, double max) {
		this.numericInfo = new NumericInfo(fieldName, min, max);
	}

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public String getName() {
		return name;
	}

	public int getDriver() {
		return driver;
	}

	public boolean isWrite() {
		return write;
	}

	public void copyGroup(final File prefix, File dir) throws IOException {
		File[] dbFiles = prefix.getParentFile().listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().startsWith(prefix.getName());
			}
		});

		for (int i = 0; i < dbFiles.length; i++) {
			FileUtils.copy(dbFiles[i], new File(dir, dbFiles[i].getName()));
		}
	}

        private G[] newGeometry;

        public G[] getNewGeometry() {
                return newGeometry;
        }

        public void setNewGeometry(String spatialFieldName, G[] newGeometry) {
                this.newGeometry = newGeometry;
                this.spatialField = spatialFieldName;
        }

}
