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
package org.orbisgis.renderer;

import ij.process.ColorProcessor;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.data.indexes.DefaultSpatialIndexQuery;
import org.gdms.driver.DriverException;
import org.grap.model.GeoRaster;
import org.orbisgis.Services;
import org.orbisgis.layerModel.ILayer;
import org.orbisgis.map.MapTransform;
import org.orbisgis.progress.IProgressMonitor;
import org.orbisgis.progress.NullProgressMonitor;
import org.orbisgis.renderer.legend.Legend;
import org.orbisgis.renderer.legend.RasterLegend;
import org.orbisgis.renderer.legend.RenderException;
import org.orbisgis.renderer.legend.RenderUtils;
import org.orbisgis.renderer.legend.Symbol;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class Renderer {

	private static Logger logger = Logger.getLogger(Renderer.class.getName());

	public void draw(Image img, Envelope extent, ILayer layer,
			IProgressMonitor pm) {
		MapTransform mt = new MapTransform();
		mt.resizeImage(img.getWidth(null), img.getHeight(null));
		mt.setExtent(extent);
		ILayer[] layers = layer.getLayersRecursively();

		long total1 = System.currentTimeMillis();
		DefaultRendererPermission permission = new DefaultRendererPermission();
		for (int i = layers.length - 1; i >= 0; i--) {
			if (pm.isCancelled()) {
				break;
			} else {
				layer = layers[i];
				if (layer.isVisible()) {
					try {
						logger.debug("Drawing " + layer.getName());
						long t1 = System.currentTimeMillis();
						SpatialDataSourceDecorator sds = layer.getDataSource();
						if (sds != null) {
							if (sds.isDefaultVectorial()) {
								drawVectorLayer(mt, layer, img, extent,
										permission, pm);
							} else if (sds.isDefaultRaster()) {
								drawRasterLayer(mt, layer, img, extent, pm);
							} else {
								logger.warn("Not drawn: " + layer.getName());
							}
							pm.progressTo(100 - (100 * i) / layers.length);
						}
						long t2 = System.currentTimeMillis();
						logger.info("Rendering time:" + (t2 - t1));
					} catch (IOException e) {
						Services.getErrorManager().error(
								"Cannot draw raster:" + layer.getName(), e);
					} catch (DriverException e) {
						Services.getErrorManager().error(
								"Cannot draw : " + layer.getName(), e);
					}
				}
			}
		}
		long total2 = System.currentTimeMillis();
		logger.info("Total rendering time:" + (total2 - total1));

	}

	private void drawRasterLayer(MapTransform mt, ILayer layer, Image img,
			Envelope extent, IProgressMonitor pm) throws DriverException,
			IOException {
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		logger.debug("raster envelope: " + layer.getEnvelope());
		Legend[] legends = layer.getLegend();
		for (Legend legend : legends) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					((RasterLegend) legend).getOpacity()));
			for (int i = 0; i < layer.getDataSource().getRowCount(); i++) {
				GeoRaster geoRaster = layer.getDataSource().getRaster(i);
				Envelope layerEnvelope = geoRaster.getMetadata().getEnvelope();
				Envelope layerPixelEnvelope = null;
				if (extent.intersects(layerEnvelope)) {

					BufferedImage mapControlImage = (BufferedImage) img;
					BufferedImage layerImage = new BufferedImage(
							mapControlImage.getWidth(), mapControlImage
									.getHeight(), BufferedImage.TYPE_INT_ARGB);

					// part or all of the GeoRaster is visible
					layerPixelEnvelope = mt.toPixel(layerEnvelope);
					Graphics2D gLayer = layerImage.createGraphics();
					Image dataImage = geoRaster
							.getImage(((RasterLegend) legend).getColorModel());
					gLayer.drawImage(dataImage, (int) layerPixelEnvelope
							.getMinX(), (int) layerPixelEnvelope.getMinY(),
							(int) layerPixelEnvelope.getWidth() + 1,
							(int) layerPixelEnvelope.getHeight() + 1, null);
					pm.startTask("Drawing " + layer.getName());
					String bands = ((RasterLegend) legend).getBands();
					if (bands != null) {
						g2.drawImage(invertRGB(layerImage, bands), 0, 0, null);
					} else {
						g2.drawImage(layerImage, 0, 0, null);
					}
					pm.endTask();
				}
			}
		}
	}

	private void drawVectorLayer(MapTransform mt, ILayer layer, Image img,
			Envelope extent, DefaultRendererPermission permission,
			IProgressMonitor pm) throws DriverException {
		Legend[] legends = layer.getLegend();
		SpatialDataSourceDecorator sds = layer.getDataSource();
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		try {
			/*
			 * Don't check the extent because it's expensive in edition
			 */
			for (Legend legend : legends) {
				DefaultSpatialIndexQuery query = new DefaultSpatialIndexQuery(
						extent, sds.getMetadata().getFieldName(
								sds.getSpatialFieldIndex()));
				Iterator<Integer> it = sds.queryIndex(query);
				long rowCount = sds.getRowCount();
				pm.startTask("Drawing " + layer.getName());
				int i = 0;
				while (it.hasNext()) {
					Integer index = it.next();
					if (i / 10000 == i / 10000.0) {
						if (pm.isCancelled()) {
							break;
						} else {
							pm.progressTo((int) (100 * i / rowCount));
						}
					}
					i++;
					Symbol sym = legend.getSymbol(index);
					Geometry g = sds.getGeometry(index);
					if (g.getEnvelopeInternal().intersects(extent)) {
						Envelope symbolEnvelope;
						if (g.getGeometryType().equals("GeometryCollection")) {
							symbolEnvelope = drawGeometryCollection(mt, g2,
									sym, g, permission);
						} else {
							symbolEnvelope = sym.draw(g2, g, mt
									.getAffineTransform(), permission);
						}
						if (symbolEnvelope != null) {
							permission.addUsedArea(symbolEnvelope);
						}
					}
				}
				pm.endTask();
			}
		} catch (RenderException e) {
			Services.getErrorManager().warning(
					"Cannot draw layer: " + layer.getName(), e);
		}
	}

	/**
	 * For geometry collections we need to filter the symbol composite before
	 * drawing
	 *
	 * @param mt
	 * @param g2
	 * @param sym
	 * @param g
	 * @param permission
	 * @throws DriverException
	 */
	private Envelope drawGeometryCollection(MapTransform mt, Graphics2D g2,
			Symbol sym, Geometry g, DefaultRendererPermission permission)
			throws DriverException {
		if (g.getGeometryType().equals("GeometryCollection")) {
			Envelope ret = null;
			for (int j = 0; j < g.getNumGeometries(); j++) {
				Geometry childGeom = g.getGeometryN(j);
				Envelope area = drawGeometryCollection(mt, g2, sym, childGeom,
						permission);
				if (ret == null) {
					ret = area;
				} else {
					ret.expandToInclude(area);
				}
			}

			return ret;
		} else {
			sym = RenderUtils.buildSymbolToDraw(sym, g);
			return sym.draw(g2, g, mt.getAffineTransform(), permission);
		}
	}

	public void draw(Image img, Envelope extent, ILayer layer) {
		draw(img, extent, layer, new NullProgressMonitor());
	}

	private class DefaultRendererPermission implements RenderPermission {

		private Quadtree quadtree;

		public DefaultRendererPermission() {
			quadtree = new Quadtree();
		}

		public void addUsedArea(Envelope area) {
			quadtree.insert(area, area);
		}

		@SuppressWarnings("unchecked")
		public boolean canDraw(Envelope area) {
			List<Envelope> list = quadtree.query(area);
			for (Envelope envelope : list) {
				if ((envelope.intersects(area)) || envelope.contains(area)) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * Method to change bands order only on the BufferedImage.
	 *
	 * @param bufferedImage
	 * @return new bufferedImage
	 */
	public Image invertRGB(BufferedImage bufferedImage, String bands) {

		ColorModel colorModel = bufferedImage.getColorModel();

		if (colorModel instanceof DirectColorModel) {
			DirectColorModel directColorModel = (DirectColorModel) colorModel;
			int red = directColorModel.getRedMask();
			int blue = directColorModel.getBlueMask();
			int green = directColorModel.getGreenMask();
			int alpha = directColorModel.getAlphaMask();
			int[] components = new int[3];
			bands = bands.toLowerCase();
			components[0] = getComponent(bands.charAt(0), red, green, blue);
			components[1] = getComponent(bands.charAt(1), red, green, blue);
			components[2] = getComponent(bands.charAt(2), red, green, blue);

			directColorModel = new DirectColorModel(32, components[0],
					components[1], components[2], alpha);
			ColorProcessor colorProcessor = new ColorProcessor(bufferedImage);
			colorProcessor.setColorModel(directColorModel);
			return colorProcessor.createImage();
		}
		return bufferedImage;
	}

	/**
	 * Gets the component specified by the char between the int components
	 * passed as parameters in red, green blue
	 *
	 * @param rgbChar
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	private int getComponent(char rgbChar, int red, int green, int blue) {
		if (rgbChar == 'r') {
			return red;
		} else if (rgbChar == 'g') {
			return green;
		} else if (rgbChar == 'b') {
			return blue;
		} else {
			throw new IllegalArgumentException(
					"The RGB code doesn't contain RGB codes");
		}
	}

}
