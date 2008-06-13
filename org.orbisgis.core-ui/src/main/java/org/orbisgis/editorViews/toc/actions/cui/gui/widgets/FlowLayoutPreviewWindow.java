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
/*
 * VentanaFlowLayoutPreview.java
 *
 * Created on 1 de mayo de 2008, 8:50
 */

package org.orbisgis.editorViews.toc.actions.cui.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.gdms.data.types.GeometryConstraint;
import org.orbisgis.ExtendedWorkspace;
import org.orbisgis.Services;
import org.orbisgis.editorViews.toc.actions.cui.gui.JPanelUniqueSymbolLegend;
import org.orbisgis.editorViews.toc.actions.cui.persistence.Compositesymboltype;
import org.orbisgis.editorViews.toc.actions.cui.persistence.ObjectFactory;
import org.orbisgis.editorViews.toc.actions.cui.persistence.Simplesymboltype;
import org.orbisgis.editorViews.toc.actions.cui.persistence.Symbolcollection;
import org.orbisgis.renderer.legend.CircleSymbol;
import org.orbisgis.renderer.legend.LegendFactory;
import org.orbisgis.renderer.legend.LineSymbol;
import org.orbisgis.renderer.legend.PolygonSymbol;
import org.orbisgis.renderer.legend.Symbol;
import org.orbisgis.renderer.legend.SymbolComposite;
import org.orbisgis.renderer.legend.SymbolFactory;
import org.orbisgis.renderer.legend.UniqueSymbolLegend;
import org.sif.UIFactory;
import org.sif.UIPanel;

/**
 * 
 * @author david
 */
public class FlowLayoutPreviewWindow extends javax.swing.JPanel implements
		UIPanel {

	public static final String SYMBOL_COLLECTION_FILE = "org.orbisgis.symbol-collection.xml";
	int countSelected = 0;
	int legendConstraint = GeometryConstraint.MIXED;
	boolean isCtrlPressed = false;

	/** Creates new form VentanaFlowLayoutPreview */
	public FlowLayoutPreviewWindow() {
		initComponents();
		try {
			ExtendedWorkspace ew = (ExtendedWorkspace) Services
					.getService("org.orbisgis.ExtendedWorkspace");
			loadXML(new FileInputStream(ew.getFile(SYMBOL_COLLECTION_FILE)));
		} catch (FileNotFoundException e) {
			System.out.println("Collection not loaded: " + e.getMessage());
		} catch (JAXBException e) {
			System.out.println("Collection not loaded: " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Collection not loaded: " + e.getMessage());
		}
		// setAbstractActions();
		refreshInterface();
	}

	// public void setAbstractActions(){
	// AbstractAction actionCtrlPressed = new AbstractAction(){
	// public void actionPerformed( ActionEvent ae ){
	// System.out.println("key pressed");
	// isCtrlPressed=true;
	// }};
	// AbstractAction actionCtrlReleased = new AbstractAction(){
	// public void actionPerformed( ActionEvent ae ){
	// System.out.println("key released");
	// isCtrlPressed=false;
	// }};
	//
	// getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
	// KeyStroke.getKeyStroke( KeyEvent.VK_CONTROL, KeyEvent.KEY_PRESSED ),
	// "CTRL_P" );
	// getActionMap().put( "CTRL_P", actionCtrlPressed );
	// getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
	// KeyStroke.getKeyStroke( KeyEvent.VK_CONTROL, KeyEvent.KEY_RELEASED ),
	// "CTRL_R" );
	// getActionMap().put( "CTRL_R", actionCtrlReleased );
	// }

	// public void setConstraint(int constraint) {
	// this.constraint = constraint;
	// }

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanelPreviewSymbols = new javax.swing.JPanel();
		jToolBar1 = new javax.swing.JToolBar();
		jButtonAdd = new javax.swing.JButton();
		jButtonEdit = new javax.swing.JButton();
		jButtonDel = new javax.swing.JButton();

		jPanelPreviewSymbols.setBorder(javax.swing.BorderFactory
				.createEtchedBorder());
		jPanelPreviewSymbols.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT));

		jToolBar1.setFloatable(false);
		jToolBar1.setRollover(true);

		jButtonAdd.setText("Add");
		jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonAddActionPerformed(evt);
			}
		});
		jToolBar1.add(jButtonAdd);

		jButtonEdit.setText("Edit");
		jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonEditActionPerformed(evt);
			}
		});
		jToolBar1.add(jButtonEdit);

		jButtonDel.setText("Delete");
		jButtonDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonDelActionPerformed(evt);
			}
		});
		jToolBar1.add(jButtonDel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jPanelPreviewSymbols,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																453,
																Short.MAX_VALUE)
														.addComponent(
																jToolBar1,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																453,
																Short.MAX_VALUE))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jToolBar1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												25,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanelPreviewSymbols,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												367, Short.MAX_VALUE)
										.addContainerGap()));
	}// </editor-fold>//GEN-END:initComponents

	private void jButtonDelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDelActionPerformed
		Component[] comps = jPanelPreviewSymbols.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof Canvas) {
				Canvas can = (Canvas) comps[i];

				if (can.isSelected) {
					jPanelPreviewSymbols.remove(can);
				}
			}
		}

		refreshInterface();

	}// GEN-LAST:event_jButtonDelActionPerformed

	private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEditActionPerformed
		Component[] comps = jPanelPreviewSymbols.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof Canvas) {
				Canvas can = (Canvas) comps[i];

				if (can.isSelected) {
					Symbol sym = can.getSymbol();
					int constraint = can.constraint;

					UniqueSymbolLegend leg = LegendFactory
							.createUniqueSymbolLegend();
					leg.setSymbol(sym);
					JPanelUniqueSymbolLegend usl = new JPanelUniqueSymbolLegend(
							leg, constraint, false);
					usl.setPreferredSize(new Dimension(657, 309));

					if (UIFactory.showDialog(usl)) {
						can.setLegend(usl.getSymbolComposite(), constraint);
					}

				}

			}
		}
		refreshInterface();
	}// GEN-LAST:event_jButtonEditActionPerformed

	private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddActionPerformed

		// Symbol sym = SymbolFactory.createCirclePointSymbol(Color.BLACK,
		// Color.BLUE, 20);
		jPanelTypeOfGeometrySelection type = new jPanelTypeOfGeometrySelection(
				true);
		if (!UIFactory.showDialog(type)) {
			return;
		}
		//
		int constraint = type.getConstraint();

		JPanelUniqueSymbolLegend usl = new JPanelUniqueSymbolLegend(constraint,
				false);
		usl.setPreferredSize(new Dimension(657, 309));

		if (!UIFactory.showDialog(usl)) {
			return;
		}

		Symbol sym = usl.getSymbolComposite();

		addSymbolToPanel(sym, constraint);

		refreshInterface();

	}// GEN-LAST:event_jButtonAddActionPerformed

	// private void jButtonMergeActionPerformed(java.awt.event.ActionEvent evt)
	// {// GEN-FIRST:event_jButtonMergeActionPerformed
	// Component[] comps = jPanelPreviewSymbols.getComponents();
	//
	// int count = 0;
	// Symbol[] symList = new Symbol[getCountSelected()];
	//
	// // Get the selected symbols
	// for (int i = 0; i < comps.length; i++) {
	// if (comps[i] instanceof Canvas) {
	// Canvas can = (Canvas) comps[i];
	// if (can.isSelected) {
	// Symbol sym = can.getSymbol();
	// symList[count++] = sym;
	// }
	// }
	// }
	//
	// SymbolComposite comp = (SymbolComposite) SymbolFactory
	// .createSymbolComposite(symList);
	//
	// addSymbolToPanel(comp, GeometryConstraint.MIXED);
	//
	// refreshInterface();
	// }// GEN-LAST:event_jButtonMergeActionPerformed

	private void jPanelPreviewSymbolsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jPanelPreviewSymbolsMouseClicked
		Component[] comps = jPanelPreviewSymbols.getComponents();

		// if (evt.getButton()==MouseEvent.BUTTON3){
		// isCtrlPressed=true;
		// }else{
		// isCtrlPressed=false;
		// }

		if (evt.getModifiers() == (InputEvent.CTRL_MASK | InputEvent.BUTTON1_MASK)) {
			isCtrlPressed = true;
		} else {
			isCtrlPressed = false;
		}

		Component c = evt.getComponent();
		if (c instanceof Canvas) {
			Canvas can = (Canvas) c;
			if (can.isSelected) {
				if (isCtrlPressed || countSelected == 0) {
					countSelected--;
					can.setSelected(!can.isSelected);
				} else {
					for (int i = 0; i < comps.length; i++) {
						if (comps[i] instanceof Canvas) {
							Canvas can2 = (Canvas) comps[i];
							if (comps[i] != c) {
								can2.setSelected(false);
							} else {
								can2.setSelected(true);
							}
						}
					}
					countSelected = 1;
				}
			} else {
				if (isCtrlPressed || countSelected == 0) {
					countSelected++;
					can.setSelected(!can.isSelected);
				} else {
					for (int i = 0; i < comps.length; i++) {
						if (comps[i] instanceof Canvas) {
							Canvas can2 = (Canvas) comps[i];
							if (comps[i] != c) {
								can2.setSelected(false);
							} else {
								can2.setSelected(true);
							}
						}
					}
					countSelected = 1;
				}
			}

		}
		refreshInterface();

	}// GEN-LAST:event_jPanelPreviewSymbolsMouseClicked

	// private void jButtonSwitchActionPerformed(java.awt.event.ActionEvent evt)
	// {// GEN-FIRST:event_jButtonSwitchActionPerformed
	// Component[] comps = jPanelPreviewSymbols.getComponents();
	//
	// int a = -1;
	// int b = -1;
	// int count = 0;
	//
	// for (int i = 0; i < comps.length || count != 2; i++) {
	// if (comps[i] instanceof Canvas) {
	// Canvas can = (Canvas) comps[i];
	// if (can.isSelected) {
	// count++;
	// if (a == -1) {
	// a = i;
	// } else {
	// b = i;
	// }
	// }
	// }
	// }
	//
	// Symbol symA = ((Canvas) comps[a]).s;
	// Symbol symB = ((Canvas) comps[b]).s;
	// int constA = ((Canvas) comps[a]).constraint;
	// int constB = ((Canvas) comps[b]).constraint;
	//
	// ((Canvas) jPanelPreviewSymbols.getComponent(a)).setLegend(symB, constB);
	// ((Canvas) jPanelPreviewSymbols.getComponent(b)).setLegend(symA, constA);
	//
	// refreshInterface();
	//
	// }// GEN-LAST:event_jButtonSwitchActionPerformed

	public int getCountSelected() {
		return countSelected;
	}

	private void refreshInterface() {

		int count = 0;

		try {
			ExtendedWorkspace ew = (ExtendedWorkspace) Services
					.getService("org.orbisgis.ExtendedWorkspace");
			saveXML(new FileOutputStream(ew.getFile(SYMBOL_COLLECTION_FILE)));
		} catch (FileNotFoundException e) {
			System.out.println("Collection not saved: " + e.getMessage());
		} catch (JAXBException e) {
			System.out.println("Collection not saved: " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Collection not saved: " + e.getMessage());
		}

		Component[] comps = jPanelPreviewSymbols.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof Canvas) {
				Canvas can = (Canvas) comps[i];

				if (can.isSelected) {
					count++;
				}

			}
		}

		if (count == 1) {
			jButtonDel.setEnabled(true);
			jButtonEdit.setEnabled(true);
		} else {
			if (count > 1) {
				jButtonDel.setEnabled(true);
				jButtonEdit.setEnabled(false);
			} else { // count = 0
				jButtonDel.setEnabled(false);
				jButtonEdit.setEnabled(false);
			}
		}

		jPanelPreviewSymbols.validate();
		jPanelPreviewSymbols.repaint();
	}

	private void saveXML(FileOutputStream file) throws JAXBException {
		ObjectFactory of = new ObjectFactory();
		Symbolcollection coll = of.createSymbolcollection();

		ArrayList<Simplesymboltype> syms = (ArrayList<Simplesymboltype>) coll
				.getSimpleSymbol();
		ArrayList<Compositesymboltype> comp = (ArrayList<Compositesymboltype>) coll
				.getCompositeSymbol();

		Component[] comps = jPanelPreviewSymbols.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof Canvas) {
				Canvas can = (Canvas) comps[i];

				Symbol sym = can.getSymbol();
				int constr = new Canvas().getConstraintForCanvas(sym);

				if (constr != GeometryConstraint.MIXED) {
					Simplesymboltype sst = createSimple(sym, constr);
					syms.add(sst);

				} else {
					SymbolComposite com = (SymbolComposite) sym;

					Compositesymboltype cst = createComposite(com);

					comp.add(cst);
				}

			}
		}
		saveCollection(coll, file);
	}

	public Simplesymboltype createSimple(Symbol sym, int constraint) {
		ObjectFactory of = new ObjectFactory();

		Simplesymboltype sst = of.createSimplesymboltype();

		sst.setGeometryType(String.valueOf(constraint));

		int constr = new Canvas().getConstraintForCanvas(sym);

		switch (constr) {
		case GeometryConstraint.LINESTRING:
		case GeometryConstraint.MULTI_LINESTRING:
			LineSymbol lin = (LineSymbol) sym;

			sst.setLineColor(lin.getColor().getRGB() + "");

			sst.setOutlineSize(lin.getSize() + "");

			break;
		case GeometryConstraint.POINT:
		case GeometryConstraint.MULTI_POINT:
			CircleSymbol cir = (CircleSymbol) sym;

			sst.setLineColor(cir.getOutlineColor().getRGB() + "");
			sst.setFillColor(cir.getFillColor().getRGB() + "");

			sst.setSimbolSize(cir.getSize() + "");

			break;
		case GeometryConstraint.POLYGON:
		case GeometryConstraint.MULTI_POLYGON:
			PolygonSymbol pol = (PolygonSymbol) sym;

			sst.setLineColor(pol.getOutlineColor().getRGB() + "");
			sst.setFillColor(pol.getFillColor().getRGB() + "");

			break;
		}

		return sst;
	}

	public Compositesymboltype createComposite(SymbolComposite com) {
		ObjectFactory of = new ObjectFactory();

		Compositesymboltype cst = of.createCompositesymboltype();

		ArrayList<Simplesymboltype> simple = (ArrayList<Simplesymboltype>) cst
				.getSimpleSymbol();

		Simplesymboltype sst = of.createSimplesymboltype();

		for (int j = 0; j < com.getSymbolCount(); j++) {

			Symbol sim = com.getSymbol(j);

			sst = of.createSimplesymboltype();

			if (sim instanceof LineSymbol) {
				LineSymbol lineSim = (LineSymbol) sim;
				sst.setGeometryType(String
						.valueOf(GeometryConstraint.LINESTRING));

				sst.setLineColor(lineSim.getColor().getRGB() + "");

				sst.setOutlineSize(lineSim.getSize() + "");

			}

			if (sim instanceof PolygonSymbol) {
				PolygonSymbol poliSim = (PolygonSymbol) sim;
				sst.setGeometryType(String.valueOf(GeometryConstraint.POLYGON));

				sst.setLineColor(poliSim.getOutlineColor().getRGB() + "");
				sst.setFillColor(poliSim.getFillColor().getRGB() + "");

			}

			if (sim instanceof CircleSymbol) {
				CircleSymbol circSim = (CircleSymbol) sim;
				sst.setGeometryType(String.valueOf(GeometryConstraint.POINT));

				sst.setLineColor(circSim.getOutlineColor().getRGB() + "");
				sst.setFillColor(circSim.getFillColor().getRGB() + "");

				sst.setSimbolSize(circSim.getSize() + "");

			}

			simple.add(sst);

		}

		return cst;
	}

	public void saveCollection(Symbolcollection coll, FileOutputStream file)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				"org.orbisgis.editorViews.toc.actions.cui.persistence", this
						.getClass().getClassLoader());
		// JAXBContext
		// jaxbContext=JAXBContext.newInstance("org.orbisgis.geoview.cui.gui.symbolcollection");
		Marshaller m = jaxbContext.createMarshaller();

		m.marshal(coll, file);
	}

	public Symbolcollection loadCollection(FileInputStream file)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				"org.orbisgis.editorViews.toc.actions.cui.persistence", this
						.getClass().getClassLoader());
		Unmarshaller u = jaxbContext.createUnmarshaller();

		Symbolcollection coll = (Symbolcollection) u.unmarshal(file);
		return coll;
	}

	private void loadXML(FileInputStream fileInputStream) throws JAXBException {
		// JAXBContext
		// jaxbContext=JAXBContext.newInstance("org.orbisgis.geoview.cui.gui.symbolcollection");
		Symbolcollection coll = loadCollection(fileInputStream);

		ArrayList<Simplesymboltype> sst = (ArrayList<Simplesymboltype>) coll
				.getSimpleSymbol();
		ArrayList<Compositesymboltype> cst = (ArrayList<Compositesymboltype>) coll
				.getCompositeSymbol();

		for (int i = 0; i < sst.size(); i++) {
			Simplesymboltype sim = sst.get(i);

			int constraint = Integer.parseInt(sim.getGeometryType());
			int out;
			int fill;
			Color outlineColor;
			Color fillColor;
			Stroke stroke;
			String outlineSize;

			switch (constraint) {
			case GeometryConstraint.POLYGON:
				outlineSize = sim.getOutlineSize();
				if (outlineSize == null) {
					outlineSize = "1.0";
				}
				stroke = new BasicStroke(Float.parseFloat(outlineSize));
				out = Integer.parseInt(sim.getLineColor());
				fill = Integer.parseInt(sim.getFillColor());
				if (sim.getLineColor() != null)
					outlineColor = new Color(out);
				else
					outlineColor = Color.white;

				if (sim.getFillColor() != null)
					fillColor = new Color(fill);
				else
					fillColor = Color.white;

				PolygonSymbol pol = (PolygonSymbol) SymbolFactory
						.createPolygonSymbol(stroke, outlineColor, fillColor);
				addSymbolToPanel(pol, constraint);
				break;
			case GeometryConstraint.POINT:
				out = Integer.parseInt(sim.getLineColor());
				fill = Integer.parseInt(sim.getFillColor());

				if (sim.getLineColor() != null)
					outlineColor = new Color(out);
				else
					outlineColor = Color.white;

				if (sim.getFillColor() != null)
					fillColor = new Color(fill);
				else
					fillColor = Color.white;

				String simbolSize = sim.getSimbolSize();
				if (simbolSize == null)
					simbolSize = "10";
				CircleSymbol cir = (CircleSymbol) SymbolFactory
						.createCirclePointSymbol(outlineColor, fillColor,
								Integer.parseInt(simbolSize));
				addSymbolToPanel(cir, constraint);
				break;
			case GeometryConstraint.LINESTRING:
				outlineSize = sim.getOutlineSize();
				if (outlineSize == null)
					outlineSize = "1.0";
				stroke = new BasicStroke(Float.parseFloat(outlineSize));
				out = Integer.parseInt(sim.getLineColor());
				if (sim.getLineColor() != null)
					outlineColor = new Color(out);
				else
					outlineColor = Color.white;

				LineSymbol lin = (LineSymbol) SymbolFactory.createLineSymbol(
						outlineColor, (BasicStroke) stroke);
				addSymbolToPanel(lin, constraint);
				break;

			}
		}

		for (int i = 0; i < cst.size(); i++) {
			Compositesymboltype comp = cst.get(i);
			ArrayList<Simplesymboltype> simp = (ArrayList<Simplesymboltype>) comp
					.getSimpleSymbol();

			Symbol[] simbolComp = new Symbol[simp.size()];

			for (int j = 0; j < simp.size(); j++) {
				Simplesymboltype sym = simp.get(j);

				int constraint = Integer.parseInt(sym.getGeometryType());
				int out;
				int fill;
				Color outlineColor;
				Color fillColor;
				Stroke stroke;
				String outlineSize;

				switch (constraint) {
				case GeometryConstraint.POLYGON:
					outlineSize = sym.getOutlineSize();
					if (outlineSize == null) {
						outlineSize = "1.0";
					}
					stroke = new BasicStroke(Float.parseFloat(outlineSize));
					out = Integer.parseInt(sym.getLineColor());
					fill = Integer.parseInt(sym.getFillColor());
					if (sym.getLineColor() != null)
						outlineColor = new Color(out);
					else
						outlineColor = Color.white;

					if (sym.getFillColor() != null)
						fillColor = new Color(fill);
					else
						fillColor = Color.white;

					PolygonSymbol pol = (PolygonSymbol) SymbolFactory
							.createPolygonSymbol(stroke, outlineColor,
									fillColor);

					simbolComp[j] = pol;
					break;
				case GeometryConstraint.POINT:
					out = Integer.parseInt(sym.getLineColor());
					fill = Integer.parseInt(sym.getFillColor());

					if (sym.getLineColor() != null)
						outlineColor = new Color(out);
					else
						outlineColor = Color.white;

					if (sym.getFillColor() != null)
						fillColor = new Color(fill);
					else
						fillColor = Color.white;

					String simbolSize = sym.getSimbolSize();
					if (simbolSize == null)
						simbolSize = "10";
					CircleSymbol cir = (CircleSymbol) SymbolFactory
							.createCirclePointSymbol(outlineColor, fillColor,
									Integer.parseInt(simbolSize));
					simbolComp[j] = cir;
					break;
				case GeometryConstraint.LINESTRING:
					outlineSize = sym.getOutlineSize();
					if (outlineSize == null)
						outlineSize = "1.0";
					stroke = new BasicStroke(Float.parseFloat(outlineSize));
					out = Integer.parseInt(sym.getLineColor());
					if (sym.getLineColor() != null)
						outlineColor = new Color(out);
					else
						outlineColor = Color.white;

					LineSymbol lin = (LineSymbol) SymbolFactory
							.createLineSymbol(outlineColor,
									(BasicStroke) stroke);
					simbolComp[j] = lin;
					break;

				}

			}
			SymbolComposite compositeSymbol = (SymbolComposite) SymbolFactory
					.createSymbolComposite(simbolComp);
			addSymbolToPanel(compositeSymbol, GeometryConstraint.MIXED);

		}

	}

	private void addSymbolToPanel(Symbol sym, int constraint) {
		Canvas can = new Canvas();

		can.setLegend(sym, constraint);
		can.setPreferredSize(new Dimension(126, 70));

		can.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jPanelPreviewSymbolsMouseClicked(evt);
			}
		});

		jPanelPreviewSymbols.add(can);
	}

	public Symbol getSelectedSymbol() {
		Component[] comps = jPanelPreviewSymbols.getComponents();
		Symbol sym = SymbolFactory.createNullSymbol();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof Canvas) {
				Canvas can = (Canvas) comps[i];

				if (can.isSelected) {
					sym = can.getSymbol();
					break;
				}
			}
		}
		return sym;
	}

	public Canvas getSelectedCanvas() {
		Component[] comps = jPanelPreviewSymbols.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof Canvas) {
				Canvas can = (Canvas) comps[i];

				if (can.isSelected) {
					return can;
				}
			}
		}
		return null;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonAdd;
	private javax.swing.JButton jButtonDel;
	private javax.swing.JButton jButtonEdit;
	private javax.swing.JPanel jPanelPreviewSymbols;
	private javax.swing.JToolBar jToolBar1;

	// End of variables declaration//GEN-END:variables

	public Component getComponent() {
		// TODO Auto-generated method stub
		return this;
	}

	public URL getIconURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInfoText() {
		// TODO Auto-generated method stub
		return "Is the collection of symbols";
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return "Symbol collection";
	}

	public String initialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public String postProcess() {

		return null;
	}

	public String validateInput() {
		if (countSelected != 1) {
			return "You can't select more or less than one symbol";
		} else {
			Canvas can = getSelectedCanvas();
			int cons = can.getConstraint(can.getSymbol());
			switch (legendConstraint) {
			case GeometryConstraint.POINT:
			case GeometryConstraint.MULTI_POINT:
				if (cons != GeometryConstraint.POINT
						&& cons != GeometryConstraint.MULTI_POINT) {
					return "You shall select a symbol with the same type of the legend";
				}
				break;
			case GeometryConstraint.LINESTRING:
			case GeometryConstraint.MULTI_LINESTRING:
				if (cons != GeometryConstraint.LINESTRING
						&& cons != GeometryConstraint.MULTI_LINESTRING) {
					return "You shall select a symbol with the same type of the legend";
				}
				break;
			case GeometryConstraint.POLYGON:
			case GeometryConstraint.MULTI_POLYGON:
				if (cons != GeometryConstraint.POLYGON
						&& cons != GeometryConstraint.MULTI_POLYGON) {
					return "You shall select a symbol with the same type of the legend";
				}
				break;
			case GeometryConstraint.MIXED:
				if (cons != GeometryConstraint.MIXED) {
					return "You shall select a symbol with the same type of the legend";
				}
				break;
			default:
				return "You shall select a symbol with the same type of the legend";
			}
		}

		return null;
	}

	public void setConstraint(int constraint) {
		this.legendConstraint = constraint;

	}

}
