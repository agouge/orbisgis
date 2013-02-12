/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
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
package org.orbisgis.view.map.tools.generated;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.view.map.tool.*;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public abstract class Multipolygon implements Automaton {
        protected static final I18n I18N = I18nFactory.getI18n(Multipolygon.class);
	private static Logger logger = Logger.getLogger(Multipolygon.class);

	private Status status = Status.STANDBY;

	private MapContext ec;

	private ToolManager tm;

        @Override
        public ImageIcon getCursor() {
            return null;
        }

        @Override
	public String[] getTransitionLabels() {
                switch(status){
                        case STANDBY:
                                return new String[]{I18N.tr("Cancel"),I18N.tr("Terminate multipolygon")};
                        case POINT :
                                return new String[]{I18N.tr("Cancel"),I18N.tr("Terminate polygon"),I18N.tr("Terminate multipolygon")};
                        default :
                                return new String[0];
                }
	}

        @Override
	public Code[] getTransitionCodes() {
                switch(status){
                        case STANDBY:
                                return new Code[]{Code.ESC, Code.TERMINATE};
                        case POINT :
                                return new Code[]{Code.ESC, Code.P, Code.TERMINATE};
                        default :
                                return new Code[]{};
                }
	}

        @Override
	public void init(MapContext ec, ToolManager tm) throws TransitionException,
			FinishedAutomatonException {
		logger.info("status: " + status);
		this.ec = ec;
		this.tm = tm;
		status = Status.STANDBY;
		transitionTo_Standby(ec, tm);
		if (isFinished(status)) {
			throw new FinishedAutomatonException();
		}
	}

        @Override
	public void transition(Code code) throws NoSuchTransitionException,
			TransitionException, FinishedAutomatonException {
		logger.info("transition code: " + code);
                Status preStatus;
                switch(status){
                        case STANDBY:
                                if (Code.PRESS.equals(code)) {
                                        preStatus = status;
                                        try {
                                                status = Status.POINT;
                                                logger.info("status: " + status);
                                                double[] v = tm.getValues();
                                                for (int i = 0; i < v.length; i++) {
                                                        logger.info("value: " + v[i]);
                                                }
                                                transitionTo_Point(ec, tm);
                                                if (isFinished(status)) {
                                                        throw new FinishedAutomatonException();
                                                }
                                        } catch (TransitionException e) {
                                                status = preStatus;
                                                throw e;
                                        }
                                } else if (Code.TERMINATE.equals(code)) {
                                        preStatus = status;
                                        try {
                                                status = Status.DONE;
                                                logger.info("status: " + status);
                                                double[] v = tm.getValues();
                                                for (int i = 0; i < v.length; i++) {
                                                        logger.info("value: " + v[i]);
                                                }
                                                transitionTo_Done(ec, tm);
                                                if (isFinished(status)) {
                                                        throw new FinishedAutomatonException();
                                                }
                                        } catch (TransitionException e) {
                                                status = preStatus;
                                                throw e;
                                        }
                                }
                                break;
                        case POINT :
                                switch(code){
                                        case PRESS :
                                                preStatus = status;
                                                try {
                                                        status = Status.POINT;
                                                        logger.info("status: " + status);
                                                        double[] v = tm.getValues();
                                                        for (int i = 0; i < v.length; i++) {
                                                                logger.info("value: " + v[i]);
                                                        }
                                                        transitionTo_Point(ec, tm);
                                                        if (isFinished(status)) {
                                                                throw new FinishedAutomatonException();
                                                        }
                                                } catch (TransitionException e) {
                                                        status = preStatus;
                                                        throw e;
                                                }
                                                break;
                                        case P :
                                                preStatus = status;
                                                try {
                                                        status = Status.LINE;
                                                        logger.info("status: " + status);
                                                        double[] v = tm.getValues();
                                                        for (int i = 0; i < v.length; i++) {
                                                                logger.info("value: " + v[i]);
                                                        }
                                                        transitionTo_Line(ec, tm);
                                                        if (isFinished(status)) {
                                                                throw new FinishedAutomatonException();
                                                        }
                                                } catch (TransitionException e) {
                                                        status = preStatus;
                                                        throw e;
                                                }
                                                break;
                                        case TERMINATE:
                                                preStatus = status;
                                                try {
                                                        status = Status.DONE;
                                                        logger.info("status: " + status);
                                                        double[] v = tm.getValues();
                                                        for (int i = 0; i < v.length; i++) {
                                                                logger.info("value: " + v[i]);
                                                        }
                                                        transitionTo_Done(ec, tm);
                                                        if (isFinished(status)) {
                                                                throw new FinishedAutomatonException();
                                                        }
                                                } catch (TransitionException e) {
                                                        status = preStatus;
                                                        throw e;
                                                }
                                                break;
                                }
                                break;
                        case LINE :
                                if (Code.INIT.equals(code)) {
                                        preStatus = status;
                                        try {
                                                status = Status.STANDBY;
                                                logger.info("status: " + status);
                                                double[] v = tm.getValues();
                                                for (int i = 0; i < v.length; i++) {
                                                        logger.info("value: " + v[i]);
                                                }
                                                transitionTo_Standby(ec, tm);
                                                if (isFinished(status)) {
                                                        throw new FinishedAutomatonException();
                                                }
                                        } catch (TransitionException e) {
                                                status = preStatus;
                                                throw e;
                                        }
                                }
                                break;
                        case DONE :
                                if ("init".equals(code)) {
                                        preStatus = status;
                                        try {
                                                status = Status.STANDBY;
                                                logger.info("status: " + status);
                                                double[] v = tm.getValues();
                                                for (int i = 0; i < v.length; i++) {
                                                        logger.info("value: " + v[i]);
                                                }
                                                transitionTo_Standby(ec, tm);
                                                if (isFinished(status)) {
                                                        throw new FinishedAutomatonException();
                                                }
                                        } catch (TransitionException e) {
                                                status = preStatus;
                                                throw e;
                                        }
                                }
                                break;
                        default :
                                if (Code.ESC.equals(code)) {
                                        status = Status.CANCEL;
                                        transitionTo_Cancel(ec, tm);
                                        if (isFinished(status)) {
                                                throw new FinishedAutomatonException();
                                        }
                                } else {
                                        throw new NoSuchTransitionException(code.toString());
                                }
                }
	}

	public boolean isFinished(Status status) {
                switch (status) {
                        case STANDBY:
                        case POINT :
                        case LINE :
                        case DONE :
                                return false;
                        case CANCEL :
                                return true;
                        default:
                                throw new RuntimeException("Invalid status: " + status);
                }
	}

        @Override
	public void draw(Graphics g) throws DrawingException {
                switch(status){
                        case STANDBY:
                                drawIn_Standby(g, ec, tm);
                                break;
                        case POINT :
                                drawIn_Point(g, ec, tm);
                                break;
                        case LINE :
                                drawIn_Line(g, ec, tm);
                                break;
                        case DONE :
                                drawIn_Done(g, ec, tm);
                                break;
                        case CANCEL:
                                drawIn_Cancel(g, ec, tm);
                                break;
                }

	}

	public abstract void transitionTo_Standby(MapContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException;

	public abstract void drawIn_Standby(Graphics g, MapContext vc,
			ToolManager tm) throws DrawingException;

	public abstract void transitionTo_Point(MapContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException;

	public abstract void drawIn_Point(Graphics g, MapContext vc, ToolManager tm)
			throws DrawingException;

	public abstract void transitionTo_Line(MapContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException;

	public abstract void drawIn_Line(Graphics g, MapContext vc, ToolManager tm)
			throws DrawingException;

	public abstract void transitionTo_Done(MapContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException;

	public abstract void drawIn_Done(Graphics g, MapContext vc, ToolManager tm)
			throws DrawingException;

	public abstract void transitionTo_Cancel(MapContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException;

	public abstract void drawIn_Cancel(Graphics g, MapContext vc, ToolManager tm)
			throws DrawingException;

	protected void setStatus(Status status) throws NoSuchTransitionException {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

        @Override
	public String getName() {
		return "Multipolygon";
	}

	public String getMessage() {
                switch(status){
                        case STANDBY:
                                return I18N.tr("Select the first point/terminate multipolygon");
                        case POINT :
                                return I18N.tr("Select the next point/terminate polygon/terminate multipolygon");
                        case LINE :
                        case DONE :
                        case CANCEL :
                                return "";
                        default :
                                throw new RuntimeException();
                }
	}

	public String getConsoleCommand() {
		return "multipolygon";
	}

        @Override
	public String getTooltip() {
		return I18N.tr("Draw a multipolygon");
	}

	private ImageIcon mouseCursor;

        @Override
	public ImageIcon getImageIcon() {
		if (mouseCursor != null) {
			return mouseCursor;
		} else {
			return null;
		}
	}

	public void setMouseCursor(ImageIcon mouseCursor) {
		this.mouseCursor = mouseCursor;
	}

        @Override
	public void toolFinished(MapContext vc, ToolManager tm)
			throws NoSuchTransitionException, TransitionException,
			FinishedAutomatonException {
		if (Status.POINT.equals(status)) {
			transition(Code.TERMINATE);
		}
	}

        @Override
	public java.awt.Point getHotSpotOffset() {

		return new java.awt.Point(8, 8);

	}

}