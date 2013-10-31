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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SourceTest<T, G> {

        public static final String internalData = "../gdmstest/src/main/resources/";
        private static final int SMALL_THRESHOLD = 5000;
        protected static final String SHPTABLE = "landcover2000shp";
        protected static List<TestData> testMetaData = new ArrayList<TestData>();
        private boolean writingTests = true;
        public static File backupDir = new File("../gdmstest/target/testresources/backup/");
        protected static File testDataInfo = new File(backupDir,
                "test_data_info.csv");
        
	

	protected static List<TestSource> toTest = new ArrayList<TestSource>();

        /**
         * returns the resources with less than SMALL_THRESOLD number of rows
         * 
         * @return
         * @throws IOException
         */
        public String[] getSmallResources() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getRowCount() < SMALL_THRESHOLD;
                        }
                });
        }

        /**
         * returns the resources with less than SMALL_THRESOLD number of rows
         * 
         * @return
         * @throws IOException
         */
        public String[] getResourcesSmallerThan(final int size) throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getRowCount() < size;
                        }
                });
        }

        private String[] getDataSet(Condition c) throws Exception {
                ArrayList<String> ret = new ArrayList<String>();
                for (TestData td : testMetaData) {
                        if (c.evaluateCondition(td)) {
                                TestSource testSource = getTestSource(td.getName());
                                if (testSource != null) {
                                        if (writingTests) {
                                                if (td.isWrite()) {
                                                        backup(testSource);
                                                        ret.add(td.getName());
                                                }
                                        } else {
                                                backup(testSource);
                                                ret.add(td.getName());
                                        }
                                }
                        }
                }

                return ret.toArray(new String[0]);
        }

        protected static TestSource getTestSource(String testDataName) {
                for (TestSource ts : toTest) {
                        if (ts.name.equals(testDataName)) {
                                return ts;
                        }
                }

                return null;
        }

        /**
         * If the test is going to write creates a backup and adds the backup to the
         * DataSourceFactory
         * 
         * @param testSource
         * 
         * @return The name of the backup in the DataSourceFactory
         * @throws IOException
         */
        protected abstract void backup(TestSource testSource) throws Exception;

        /**
         * Get all the resources with primary keys
         * 
         * @return
         * @throws IOException
         */
        public String[] getResourcesWithPK() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getPKInfo() != null;
                        }
                });
        }

        public String[] getNonDBResourcesWithPK() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return (td.getPKInfo() != null) && !td.isDB();
                        }
                });
        }

        public String[] getResourcesWithoutPK() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getPKInfo() == null;
                        }
                });
        }

        protected TestData<T, G> getTestData(String name) {
                for (TestData<T, G> td : testMetaData) {
                        if (td.getName().equals(name)) {
                                return td;
                        }
                }

                throw new RuntimeException("?");
        }

        /**
         * Gets a new unique primary key for the specified resource. This method
         * only should receive as parameters the return values from
         * getResourcesWithPK
         * 
         * @param dsName
         * @return
         */
        public T getNewPKFor(String dsName) {
                return getTestData(dsName).getPKInfo().getNewPK();
        }

        /**
         * Gets the primary key field index for the specified resource. This method
         * only should receive as parameters the return values from
         * getResourcesWithPK
         * 
         * @param dsName
         * @return
         */
        public String getPKFieldFor(String dsName) {
                TestData td = getTestData(dsName);
                if (td.getPKInfo() == null) {
                        return null;
                } else {
                        return td.getPKInfo().getPkField();
                }
        }

        /**
         * Gets the index of a string field in the specified resource
         * 
         * @param dsName
         * @return
         */
        public String getStringFieldFor(String dsName) {
                return getTestData(dsName).getStringField();
        }

        /**
         * Gets database resources
         * 
         * @return
         * @throws IOException
         */
        public String[] getDBResources() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.isDB();
                        }
                });
        }

        public String[] getNonDBSmallResources() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return (td.getRowCount() < SMALL_THRESHOLD) && !td.isDB();
                        }
                });
        }

        /**
         * returns the index of a field that can be set to null and doesn't have to
         * have unique values
         * 
         * @param dsName
         * @return
         */
        public String getNoPKFieldFor(String dsName) {
                return getTestData(dsName).getNoPKField();
        }

        /**
         * Gets any resource without spatial fields
         * 
         * @return
         * @throws IOException
         */
        public String getAnyNonSpatialResource() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return (td.getRowCount() < SMALL_THRESHOLD)
                                        && (td.getNewGeometry() == null);
                        }
                })[0];
        }

        /**
         * Gets any resource with spatial fields
         * 
         * @return
         * @throws IOException
         */
        public String getAnySpatialResource() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return (td.getRowCount() < SMALL_THRESHOLD)
                                        && (td.getNewGeometry() != null);
                        }
                })[0];
        }

        /**
         * Gets resources with null values
         * 
         * @return
         * @throws IOException
         */
        public String[] getSmallResourcesWithNullValues() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return (td.getNullField() != null)
                                        && (td.getRowCount() < SMALL_THRESHOLD);
                        }
                });
        }

        /**
         * Returns any numeric field for the given resource.
         * 
         * @param resource
         * @return
         */
        public String getNumericFieldNameFor(String resource) {
                return getTestData(resource).getNumericInfo().getNumericFieldName();
        }

        /**
         * Return resources which have at leasst one numeric field
         * 
         * @return
         * @throws IOException
         */
        public String[] getResourcesWithNumericField() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getNumericInfo() != null;
                        }
                });
        }

        public String[] getResourcesWithoutNumericField() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getNumericInfo() == null;
                        }
                });
        }

        /**
         * Returns resources that contain null values
         * 
         * @return
         * @throws IOException
         */
        public String[] getResourcesWithNullValues() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getNullField() != null;
                        }
                });
        }

        /**
         * Returns the name of a field containing null values in the specified data
         * source
         * 
         * @param ds
         * @return
         */
        public String getContainingNullFieldNameFor(String ds) {
                return getTestData(ds).getNullField();
        }

        /**
         * Gets the minimum value for the specified field in the specified data
         * source
         * 
         * @param ds
         * @param numericFieldName
         * @return
         */
        public double getMinimumValueFor(String ds, String numericFieldName) {
                return getTestData(ds).getNumericInfo().getMin();
        }

        /**
         * Gets the maximum value for the specified field in the specified data
         * source
         * 
         * @param ds
         * @param numericFieldName
         * @return
         */
        public double getMaximumValueFor(String ds, String numericFieldName) {
                return getTestData(ds).getNumericInfo().getMax();
        }

        /**
         * Gets the resources with repeated rows
         * 
         * @return
         * @throws IOException
         */
        public String[] getResourcesWithRepeatedRows() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.hasRepeatedRows();
                        }
                });
        }

        /**
         * Gets new geometries of a type suitable to be added to the specified data
         * source
         * 
         * @param dsName
         * @return
         */
        public G[] getNewGeometriesFor(String dsName) {
                return getTestData(dsName).getNewGeometry();
        }

        /**
         * Gets new geometries of a type suitable to be added to the specified data
         * source
         * 
         * @param dsName
         * @return
         */
        public String getSpatialFieldName(String dsName) {
                return getTestData(dsName).getSpatialField();
        }

        /**
         * returns all the spatial resources
         * 
         * @return
         * @throws IOException
         */
        public String[] getSpatialResources() throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getNewGeometry() != null;
                        }
                });
        }

        private interface Condition {

                public boolean evaluateCondition(TestData td);
        }

        /**
         * Tell the test system that the tests are going to perform modifications in
         * the data sources
         * 
         * @param writeTests
         */
        public void setWritingTests(boolean writingTests) {
                this.writingTests = writingTests;
        }

        public String[] getNonSpatialResourcesSmallerThan(final int threshold)
                throws Exception {
                return getDataSet(new Condition() {

                        public boolean evaluateCondition(TestData td) {
                                return td.getRowCount() < threshold
                                        && td.getNewGeometry() == null;
                        }
                });
        }

        public String getSHPTABLE() throws Exception {
                getTABLE(SHPTABLE);
                return SHPTABLE;
        }

        public void getTABLE(final String tableName) throws Exception {
                backup(getTestSource(tableName));
        }
}