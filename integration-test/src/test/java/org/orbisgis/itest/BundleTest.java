/**
 * h2spatial is a library that brings spatial support to the H2 Java database.
 *
 * h2spatial is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 *
 * h2patial is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * h2spatial is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * h2spatial. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */

package org.orbisgis.itest;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import javax.inject.Inject;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jdbc.DataSourceFactory;
import javax.sql.DataSource;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

/**
 * {@see http://felix.apache.org/site/apache-felix-ipojo-junit4osgi-tutorial.html}
 * @author Nicolas Fortin
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class BundleTest {
    @Inject
    BundleContext context;
    private static final String DB_FILE_PATH = "target/test-resources/dbH2";
    private static final String DATABASE_PATH = "jdbc:h2:"+DB_FILE_PATH;
    private DataSource dataSource;
    private ServiceReference<DataSourceFactory> ref;

    @Configuration
    public Option[] config() {
        return options(systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
                mavenBundle("org.osgi", "org.osgi.compendium"),
                mavenBundle("org.orbisgis", "h2spatial-api").version("1.1.1-SNAPSHOT"),
                mavenBundle("org.orbisgis", "spatial-utilities").version("1.1.1-SNAPSHOT"),
                mavenBundle("org.orbisgis", "cts"),
                mavenBundle("org.orbisgis", "jts").version("1.13"),
                mavenBundle("org.orbisgis", "jdelaunay"),
                mavenBundle("com.h2database", "h2").version("1.3.176"),
                mavenBundle("com.fasterxml.jackson.core", "jackson-core").version("2.3.1"),
                mavenBundle("org.orbisgis", "h2spatial").version("1.1.1-SNAPSHOT").noStart(),
                mavenBundle("org.orbisgis", "h2spatial-ext").version("1.1.1-SNAPSHOT").noStart(),
                mavenBundle("org.orbisgis", "h2drivers").version("1.1.1-SNAPSHOT").noStart(),
                mavenBundle("org.orbisgis", "h2spatial-osgi").version("1.1.1-SNAPSHOT"),
                mavenBundle("org.orbisgis", "h2spatial-ext-osgi").version("1.1.1-SNAPSHOT"),
                mavenBundle("org.orbisgis", "java-network-analyzer").version("0.1.6"),
                mavenBundle("org.jgrapht", "jgrapht-core").version("0.9.0"),
                mavenBundle("org.orbisgis", "h2network").version("1.1.1-SNAPSHOT").noStart(),
                junitBundles());
    }

    @Test
    public void checkResolveState() throws BundleException {
        for (Bundle bundle : context.getBundles()) {
            if(bundle.getState() == Bundle.INSTALLED) {
                throw new BundleException("Bundle "+bundle.getSymbolicName()+" not resolved");
            }
        }
    }

    /**
     * Create data source
     */
    @Before
    public void setUp() throws SQLException {
        // Find if DataSource service is already online
        ref =  context.getServiceReference(DataSourceFactory.class);
        Properties properties = new Properties();
        properties.put(DataSourceFactory.JDBC_URL,DATABASE_PATH);
        properties.put(DataSourceFactory.JDBC_USER,"sa");
        properties.put(DataSourceFactory.JDBC_PASSWORD,"");
        dataSource = context.getService(ref).createDataSource(properties);
        assertNotNull(dataSource);
        if(context.getServiceReference(DataSource.class.getName())==null) {
            // First UnitTest
            // Delete database
            File dbFile = new File(DB_FILE_PATH+".h2.db");
            if(dbFile.exists()) {
                assertTrue(dbFile.delete());
            }
            context.registerService(DataSource.class.getName(), dataSource, null);
        }
    }

    /**
     * Validate integration of built-in bundles.
     */
    @Test
    public void testBuiltInBundleActivation() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        System.out.println("Built-In bundle list :");
        System.out.println("ID\t\tState\tBundle name");
        for (Bundle bundle : context.getBundles()) {
            System.out.println(
                    "[" + String.format("%02d", bundle.getBundleId()) + "]\t"
                            + getStateString(bundle.getState()) + "\t"
                            + bundle.getSymbolicName());
            // Print services
            ServiceReference[] refs = bundle.getRegisteredServices();
            if(refs!=null) {
                for(ServiceReference ref : refs) {
                    String refDescr = ref.toString();
                    if(!refDescr.contains("org.osgi") && !refDescr.contains("org.apache")) {
                        System.out.println(
                                "\t\t\t\t" + ref);
                    }
                }
            }
        }
    }

    private String getStateString(int i) {
        switch (i) {
            case Bundle.ACTIVE:
                return "Active   ";
            case Bundle.INSTALLED:
                return "Installed";
            case Bundle.RESOLVED:
                return "Resolved ";
            case Bundle.STARTING:
                return "Starting ";
            case Bundle.STOPPING:
                return "Stopping ";
            default:
                return "Unknown  ";
        }
    }
    @After
    public void tearDown() {
        context.ungetService(ref);
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Create and feed a spatial table, read a Geometry value
     * @throws Exception
     */
    @Test
    public void testCreateGeometryTable() throws Exception  {
        try (Connection connection = getConnection()) {
            Statement stat = connection.createStatement();
            stat.execute("DROP TABLE IF EXISTS POINT2D");
            stat.execute("CREATE TABLE POINT2D (gid int PRIMARY KEY AUTO_INCREMENT , the_geom GEOMETRY)");
            PreparedStatement insert = connection.prepareStatement("INSERT INTO POINT2D (the_geom) VALUES (?)");
            GeometryFactory f = new GeometryFactory();
            insert.setObject(1, f.createPoint(new Coordinate(5, 8, 15)));
            insert.execute();
            // Read the value
            ResultSet rs = stat.executeQuery("select the_geom from POINT2D");
            assertTrue(rs.next());
            assertTrue(rs.getObject(1) instanceof Geometry);
            assertEquals(f.createPoint(new Coordinate(5, 8, 15)), rs.getObject(1));
            System.out.println("testCreateGeometryTable OK");
        }
    }

    private static void createTestTable(Statement stat)  throws SQLException {
        stat.execute("drop table if exists area");
        stat.execute("create table area(idarea int primary key, the_geom geometry)");
        stat.execute("create spatial index on area(the_geom)");
        stat.execute("insert into area values(1, 'POLYGON ((-10 109, 90 109, 90 9, -10 9, -10 109))')");
        stat.execute("insert into area values(2, 'POLYGON ((90 109, 190 109, 190 9, 90 9, 90 109))')");
        stat.execute("insert into area values(3, 'POLYGON ((190 109, 290 109, 290 9, 190 9, 190 109))')");
        stat.execute("insert into area values(4, 'POLYGON ((-10 9, 90 9, 90 -91, -10 -91, -10 9))')");
        stat.execute("insert into area values(5, 'POLYGON ((90 9, 190 9, 190 -91, 90 -91, 90 9))')");
        stat.execute("insert into area values(6, 'POLYGON ((190 9, 290 9, 290 -91, 190 -91, 190 9))')");
        stat.execute("create table roads(idroad int primary key, the_geom geometry)");
        stat.execute("create spatial index on roads(the_geom)");
        stat.execute("insert into roads values(1, 'LINESTRING (27.65595463138 -16.728733459357244, 47.61814744801515 40.435727788279806)')");
        stat.execute("insert into roads values(2, 'LINESTRING (17.674858223062415 55.861058601134246, 55.78449905482046 76.73062381852554)')");
        stat.execute("insert into roads values(3, 'LINESTRING (68.48771266540646 67.65689981096412, 108.4120982986768 88.52646502835542)')");
        stat.execute("insert into roads values(4, 'LINESTRING (177.3724007561437 18.65879017013235, 196.4272211720227 -16.728733459357244)')");
        stat.execute("insert into roads values(5, 'LINESTRING (106.5973534971645 -12.191871455576518, 143.79962192816637 30.454631379962223)')");
        stat.execute("insert into roads values(6, 'LINESTRING (144.70699432892252 55.861058601134246, 150.1512287334594 83.9896030245747)')");
        stat.execute("insert into roads values(7, 'LINESTRING (60.321361058601155 -13.099243856332663, 149.24385633270325 5.955576559546344)')");
    }

    /**
     * Create and feed a spatial table, read a Geometry value
     * @throws Exception
     */
    @Test
    public void testOverlapOperator() throws Exception  {
        Connection connection = getConnection();
        try {
            Statement stat = connection.createStatement();
            createTestTable(stat);
            ResultSet rs = stat.executeQuery("select idarea, COUNT(idroad) roadscount from area,roads where" +
                    " area.the_geom && roads.the_geom GROUP BY idarea ORDER BY idarea");
            assertTrue(rs.next());
            assertEquals(1,rs.getInt("idarea"));
            assertEquals(3,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(2,rs.getInt("idarea"));
            assertEquals(4,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(3,rs.getInt("idarea"));
            assertEquals(1,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(4,rs.getInt("idarea"));
            assertEquals(2,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(5,rs.getInt("idarea"));
            assertEquals(3,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(6,rs.getInt("idarea"));
            assertEquals(1,rs.getInt("roadscount"));
            assertFalse(rs.next());
            rs.close();
            stat.execute("drop table area");
            stat.execute("drop table roads");
        } finally {
            connection.close();
        }
    }

    /**
     * Create and feed a spatial table, read a Geometry value
     * @throws Exception
     */
    @Test
    public void testIntersects() throws Exception  {
        Connection connection = getConnection();
        try {
            Statement stat = connection.createStatement();
            createTestTable(stat);
            ResultSet rs = stat.executeQuery("select idarea, COUNT(idroad) roadscount from area,roads where" +
                    " ST_Intersects(area.the_geom,roads.the_geom) GROUP BY idarea ORDER BY idarea");
            assertTrue(rs.next());
            assertEquals(1,rs.getInt("idarea"));
            assertEquals(3,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(2,rs.getInt("idarea"));
            assertEquals(4,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(4,rs.getInt("idarea"));
            assertEquals(2,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(5,rs.getInt("idarea"));
            assertEquals(3,rs.getInt("roadscount"));
            assertTrue(rs.next());
            assertEquals(6,rs.getInt("idarea"));
            assertEquals(1,rs.getInt("roadscount"));
            assertFalse(rs.next());
            rs.close();
            stat.execute("drop table area");
            stat.execute("drop table roads");
        } finally {
            connection.close();
        }
    }
}
