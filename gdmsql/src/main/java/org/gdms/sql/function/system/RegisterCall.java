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
package org.gdms.sql.function.system;

import java.io.File;
import org.apache.log4j.Logger;
import org.gdms.data.SQLDataSourceFactory;
import org.gdms.data.SourceAlreadyExistsException;
import org.gdms.data.db.DBSource;
import org.gdms.data.db.DBTableSourceDefinition;
import org.gdms.data.file.FileSourceCreation;
import org.gdms.data.schema.Metadata;
import org.gdms.data.values.Value;
import org.gdms.driver.DataSet;
import org.gdms.source.SourceManager;
import org.gdms.sql.function.executor.AbstractExecutorFunction;
import org.gdms.sql.function.executor.ExecutorFunctionSignature;
import org.gdms.sql.function.FunctionException;
import org.gdms.sql.function.FunctionSignature;
import org.gdms.sql.function.ScalarArgument;
import org.orbisgis.progress.ProgressMonitor;
import org.orbisgis.utils.FileUtils;

public final class RegisterCall extends AbstractExecutorFunction {

        private static final Logger LOG = Logger.getLogger(RegisterCall.class);

        @Override
        public void evaluate(SQLDataSourceFactory dsf, DataSet[] tables,
                Value[] values, ProgressMonitor pm) throws FunctionException {
                LOG.trace("Evaluating");
                try {
                        final SourceManager sourceManager = dsf.getSourceManager();
                        if (values.length == 1) {
                                String file = values[0].toString();
                                final FileSourceCreation fsc = new FileSourceCreation(new File(file), null);
                                String name = FileUtils.getFileNameWithoutExtensionU(fsc.getFile());
                                sourceManager.register(name, fsc);
                        } else if (values.length == 2) {
                                final String file = values[0].toString();
                                final String name = values[1].toString();
                                sourceManager.register(name, new FileSourceCreation(new File(file), null));
                        } else if ((values.length == 8) || (values.length == 9)) {
                                final String vendor = values[0].toString();
                                final String host = values[1].toString();
                                final String port = values[2].toString();
                                final String dbName = values[3].toString();
                                final String user = values[4].toString();
                                final String password = values[5].toString();
                                String schemaName = null;
                                String tableName = null;
                                String name = null;
                                if (values.length == 8) {
                                        tableName = values[6].toString();
                                        name = values[7].toString();
                                }
                                if (values.length == 9) {
                                        schemaName = values[6].toString();
                                        tableName = values[7].toString();
                                        name = values[8].toString();
                                }

                                if (tableName == null) {
                                        throw new FunctionException("Not implemented yet");
                                }
                                sourceManager.register(name, new DBTableSourceDefinition(
                                        new DBSource(host, Integer.parseInt(port), dbName,
                                        user, password, schemaName, tableName, "jdbc:" + vendor)));
                        } else {
                                throw new FunctionException("Usage: \n"
                                        + "1) EXECUTE register ('path_to_file');\n"
                                        + "2) EXECUTE register ('path_to_file', 'name');\n"
                                        + "3) EXECUTE register ('vendor', 'host', port, "
                                        + "dbName, user, password, tableName, dsEntryName);\n"
                                        + "4) EXECUTE register ('vendor', 'host', port, "
                                        + "dbName, user, password, schema, tableName, dsEntryName);\n");
                        }
                } catch (SourceAlreadyExistsException e) {
                        throw new FunctionException(e);
                }
        }

        @Override
        public String getName() {
                return "Register";
        }

        @Override
        public String getDescription() {
                return "Register an existing file or a database.";
        }

        @Override
        public String getSqlOrder() {
                return "Usage: \n"
                        + "1) EXECUTE register ('name');\n"
                        + "2) EXECUTE register ('path_to_file', 'name');\n"
                        + "3) EXECUTE register ('vendor', 'host', port, "
                        + "dbName, user, password, tableName, dsEntryName);\n"
                        + "4) EXECUTE register ('vendor', 'host', port, "
                        + "dbName, user, password, schema, tableName, dsEntryName);\n";
        }

        public Metadata getMetadata(Metadata[] tables) {
                return null;
        }

        @Override
        public FunctionSignature[] getFunctionSignatures() {
                return new FunctionSignature[]{
                                new ExecutorFunctionSignature(ScalarArgument.STRING),
                                new ExecutorFunctionSignature(ScalarArgument.STRING,
                                ScalarArgument.STRING),
                                new ExecutorFunctionSignature(ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING,
                                ScalarArgument.STRING),
                                new ExecutorFunctionSignature(ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING,
                                ScalarArgument.STRING, ScalarArgument.STRING)
                        };
        }
}
