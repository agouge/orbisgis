package org.orbisgis.geoview.rasterProcessing.action.utilities;

import ij.ImagePlus;

import java.io.IOException;

import org.grap.io.GeoreferencingException;
import org.orbisgis.geoview.GeoView2D;
import org.orbisgis.geoview.layerModel.ILayer;
import org.orbisgis.geoview.layerModel.RasterLayer;
import org.orbisgis.geoview.views.toc.ILayerAction;

public abstract class AbstractGray16And32Process implements ILayerAction{

	public boolean accepts(ILayer layer) {
		if (layer instanceof RasterLayer){
			RasterLayer rs = (RasterLayer) layer;
			
			try {
				int type = rs.getGeoRaster().getType();
				
				if ((type == ImagePlus.GRAY16)||(type == ImagePlus.GRAY32)) {
					return true;
					
				}
				
			} catch (IOException e) {			
			
			} catch (GeoreferencingException e) {			
				
			}
			}
		return false;
	}

	

}
