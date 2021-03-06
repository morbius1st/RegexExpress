package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

import javax.swing.*;


import pro.cyberstudio.displaylist.GraphElement;

import static pro.cyberstudio.regexexpress.RegexExpress.regexViewport;
import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.regexexpress.Utility.viewSizeMask;
import static pro.cyberstudio.regexexpress.RegexLayeredPane.DragModes.*;
import static pro.cyberstudio.regexexpress.RegexExpress.viewSizes;

import static pro.cyberstudio.utilities.point.*;

/**
 * @author jeffs
 *         File:    RegexLayeredPane
 *         Created: 4/23/2017 @ 11:54 AM
 *         Project: RegexExpress
 */

class viewSizeData {
	Dimension2dx minSize;
	Dimension2dx viewSize;
	Dimension2dx layerSize;
}

class PriorZoom {
	double priorZoomFactor;
	Point priorVPCornerPtScreen;
}

class RegexLayeredPane extends JLayeredPane implements iCompListener, iMouseListener, iMWListener, iMMListener  { //, MouseWheelListener{
	
	boolean runOnce1 = true;
	
	
	enum DragModes {NONE, XHAIRS, PAN, STARTWINDOW, WINDOW, SELECTION, LINE }
	
	static private final int POINTER_LAYER = JLayeredPane.PALETTE_LAYER - 1;
	
	// hold the actual instance
	private static RegexLayeredPane me;
//	private static JViewport viewport;
	private static RegexBackground rxBackground;
	private static RegexPointer rxPointer;
	static RegexZero rxZero;
	
	private static viewSizeData viewSizeParams = new viewSizeData();
	
	AffineTransform aft = new AffineTransform();
	
	private static double zoomFactor = 1.0;
	private static double zoomRatio = 1.0;
	
	private static double zoomFactorMax = 8.0;
	private static double zoomFactorMin = 1 / zoomFactorMax;
	
	private static final double ZOOMWHEELRATIOIN = 1.1;
	private static final double ZOOMRATIOIN = 1.5;
	
	private static double zoomWheelRatioOut = 1 / ZOOMWHEELRATIOIN; // initial value
	private static double zoomWheelRatioIn = ZOOMWHEELRATIOIN; // initial value
	
	private static double getZoomRatioOut = 1 / ZOOMRATIOIN;
	private static double getZoomRatioIn = ZOOMRATIOIN;
	
	private static DragModes dragMode = PAN;
	
	private static Point anchorPoint = new Point(0, 0);
	private static Point winCornerPoint = new Point();
	
	private static final int MAXPRIORZOOMS = 3;
	private static PriorZoom rootPriorZoom = new PriorZoom();
	private static boolean priorSaved = false;
	
	private static TreeMap<String, RegexLayer> layerTable = new TreeMap<>();
	private static CircularStack<PriorZoom> priorZooms = new CircularStack<>(MAXPRIORZOOMS);
	
	private static int layerID = 1;
	
	private static RegexLayer foundLayer;
	
	private static final String compName = "lay pane| ";
	
	
	// private constructor to prevent creating
	// an instance of this class
	private RegexLayeredPane() {
		// assign minimal sizes to the view size parameters
		viewSizeParams.minSize = new Dimension2dx(100, 100);
		viewSizeParams.viewSize = new Dimension2dx(100, 100);
		viewSizeParams.layerSize = new Dimension2dx(100, 100);
		setName("RegexLayeredPane");
		
		
		aft.setToScale(zoomFactor, zoomFactor);
		
		RegexScroll.addMWL(this);
		RegexScroll.addCL(this);
		RegexScroll.addML(this);
		RegexScroll.addMMovL(this);
		RegexScroll.addMDragL(this);
		
	}
	
	/**
	 * static initializer to construct an instance of this class
	 */
	static {
		me = new RegexLayeredPane();
		rxPointer = new RegexPointer();
		rxBackground = new RegexBackground();
		rxZero = new RegexZero();
	}
	
	/**
	 * @return instance of this class
	 */
	static RegexLayeredPane getInstance() {
		return me;
	}
	
	void initialize(Dimension2dx initSize) {
		viewSizeParams.minSize = initSize.clone();
		viewSizeParams.layerSize = initSize.clone();
		viewSizeParams.viewSize = initSize.clone();
		
		Dimension layerSize = new Dimension(RegexExpress.LAYERX, RegexExpress.LAYERY);
		
		rxZero.setMinimumSize(layerSize);
		rxZero.setPreferredSize(layerSize);
		rxZero.setOpaque(false);
		rxZero.setVisible(true);
		rxZero.setName("zero");
		rxZero.setZoomScale(zoomFactor);
		add(rxZero, JLayeredPane.DEFAULT_LAYER);
		
		rxPointer.setMinimumSize(layerSize);
		rxPointer.setPreferredSize(layerSize);
		rxPointer.setOpaque(false);
		rxPointer.setVisible(true);
		rxPointer.setName("pointer");
		rxPointer.setPointerModeXhairs();
		add(rxPointer, (Integer) (POINTER_LAYER));
		
		rxBackground.setMinimumSize(layerSize);
		rxBackground.setPreferredSize(layerSize);
		rxBackground.setBackground(Color.BLACK);
		rxBackground.setOpaque(true);
		rxBackground.setVisible(true);
		rxBackground.setName("background");
		rxBackground.setSize(RegexExpress.LAYERX, RegexExpress.LAYERY);
		add(rxBackground, (Integer) (-1));
		
		// assign listeners to pointer so that the coordinates are relative
		// to layered pane (the container of thees objects)
		// this assigns the primary listener
		rxPointer.addMouseListener(RegexExpress.regexScroll);
		rxPointer.addMouseWheelListener(RegexExpress.regexScroll);
		rxPointer.addMouseMotionListener(RegexExpress.regexScroll);
		
//		viewport = ((JViewport) getParent());
		
		// default settings if no initial zoom location set
		rootPriorZoom.priorZoomFactor = 1.0;
		rootPriorZoom.priorVPCornerPtScreen = new Point(0, 0);
		
		setZoomRatio(zoomFactor);
		updateSize();
	}
	
	void getLayerOneInfo() {
		LogMsgln(layerTable.firstEntry().getValue().toString());
	}
	
	String addGraphElement(String layerName, GraphElement ge) {
		RegexLayer layer = layerTable.get(layerName);
		
		if (layer == null) {
			return null;
		}
		
		if (layer.addGraphElem(ge) ) {
			return ge.toString();
		}
		
		return null;
	}
	

	Component add(String layerName) {
		RegexLayer layer = new RegexLayer(layerName, getLayerID());
		layer.setName(layerName);
		layer.setMinimumSize(rxZero.getMinimumSize());
		layer.setPreferredSize(rxZero.getMinimumSize());
		layer.setSize(getPreferredSize());
		layer.setOpaque(false);
		layer.setVisible(true);
		layer.setZoomScale(zoomFactor);
		
		add(layer, (Integer) (layerTable.size() + 1));
		layerTable.put(layerName, layer);

		return layer;
	}
	
	private int getLayerID() {
		return layerID++;
	}
	
	boolean layerOff(String name) {
		foundLayer = layerTable.get(name);
		
		if (foundLayer == null) { return false; }
		
		foundLayer.setVisible(false);
		
		return true;
	}
	
	boolean layerOn(String name) {
		foundLayer = layerTable.get(name);
		
		if (foundLayer == null) { return false; }
		
		foundLayer.setVisible(true);
		
		return true;
	}
	
	
	
	void testPointer() {
		rxPointer.test();
	}

	
	// ***** zoom functions ***********************************************
	// zoom to a user selected window
	private void zoomWindow() {
//		saveCurrentZoom();
		
		// window corner points set
		// points are in screen coordinates
		// anchorPoint is the first point entered
		// winCornerPt is the opposite corner
		
		// determine the scale ratio - the smaller of the ratios of the
		// comparison sides to the visible rectangle versus user
		// selected window
		int zoomWindowWidth = Math.abs(anchorPoint.x - winCornerPoint.x);
		int zoomWindowHeight = Math.abs(anchorPoint.y - winCornerPoint.y);
		
		double zRatio = Math.min(getVisibleRect().getWidth() / zoomWindowWidth,
				getVisibleRect().getHeight() / zoomWindowHeight);
		
		Point vpCenterScreen = new Point(anchorPoint.x - ((anchorPoint.x - winCornerPoint.x) / 2),
				anchorPoint.y  - ((anchorPoint.y - winCornerPoint.y) / 2));
		
		int x = getVisibleRect().width / 2;
		int y = getVisibleRect().height / 2;
		
		zoom(zRatio, vpCenterScreen, x, y);
	}
	
	
	// **** this method does NOT get its prior zoom info saved
	// this is associated with the scroll wheel being used and this would
	// produce too many saved zooms
	// zoom about a point (screen) coordinates
	private void zoomScroll(double zRatio, Point vpPtScreen) {
		
		int x = vpPtScreen.x - getVisibleRect().x;
		int y = vpPtScreen.y - getVisibleRect().y;
		
		Zoom(zRatio, vpPtScreen, x, y);
	}

	
	// zoom out by a set amount
	void zoomOut() {
		Point vpCenterScreen = new Point((int) getVisibleRect().getCenterX(),
				(int) getVisibleRect().getCenterY());
		
		int x = getVisibleRect().width / 2;
		int y = getVisibleRect().height / 2;
		
		zoom(getZoomRatioOut, vpCenterScreen, x, y);
	}
	
	// **** adjusted to use zoom
	void zoomIn() {
		Point vpCenterScreen = new Point((int) getVisibleRect().getCenterX(),
				(int) getVisibleRect().getCenterY());
		
		int x = getVisibleRect().width / 2;
		int y = getVisibleRect().height / 2;
		zoom(getZoomRatioIn, vpCenterScreen, x, y);
	
	}

	// this is an exception - it does not go through the zoom method that
	// saves the current viewport location
	void zoomPrevious() {
		PriorZoom pz;

		if (priorZooms.isEmpty()) {
			pz = rootPriorZoom;
		} else {
			pz = priorZooms.pop();
		}
		
		setZoomRatio(pz.priorZoomFactor / zoomFactor);
		positionViewport(pz.priorVPCornerPtScreen, 0, 0);
	}
	
	// primary zoom method that also saves the prior zoom information for later
	// zoom previous
	private void zoom(double zRatio, Point vpCornerPtScreen, int vpOffsetX, int vpOffsetY) {
		saveCurrentZoom();
		
		Zoom(zRatio, vpCornerPtScreen, vpOffsetX, vpOffsetY);
	}
	
	// primary zoom method - the intent is that all (with some exceptions) zoom methods go through this method
	// for every zoom
	private void Zoom(double zRatio, Point vpCornerPtScreen, int vpOffsetX, int vpOffsetY) {
		Point vpCornerPointLayer = calcLayerPtFromScnPt(vpCornerPtScreen);

		setZoomRatio(zRatio);

		Point vpCornerPtScreenZoomed = calcScnPtFromLayerPt(vpCornerPointLayer);
		
		positionViewport(vpCornerPtScreenZoomed, vpOffsetX, vpOffsetY);
	}
	
	
	// ***** display position functions ***********************************************
	void setInitialZoom(double zFactor, Point layerPoint) {
		
		setZoomRatio(zFactor / zoomFactor);
		moveToPoint2(layerPoint);
		
		priorZooms.clear();
		saveCurrentZoom();

		rootPriorZoom = priorZooms.peek();
	}
	
	void moveToPoint2(Point layerPoint) {
		moveToPoint(calcScnPtFromLayerPt(layerPoint));
	}
	
	// **** adjusted to use zoom
	private void moveToPoint(Point screenPoint) {
		
		int x = getVisibleRect().width / 2;
		int y = getVisibleRect().height / 2;
		
		zoom(1.0, screenPoint, x, y);
	}
	
	// ***** display utility functions ***********************************************
	private void updateSize() {
		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			if (comps.length > 0) {
				for (Component c : comps) {
					c.setSize(getPreferredSize());
					c.revalidate();
					c.repaint();
				}
			}
		}
	}
	
	void startZoomWindow() {
		dragMode = STARTWINDOW;
	}
	
	void stopZoomWindow() {
		dragMode = PAN;
	}
	
	private void saveCurrentZoom() {
		
		PriorZoom pz = new PriorZoom();
		pz.priorZoomFactor = zoomFactor;
		pz.priorVPCornerPtScreen = regexViewport.getViewPosition();

		priorZooms.push(pz);
	}
	
	void setZoomRatio(double zRatio) {
	
//		LogMsgln("\n\t\t****************************");
		
		if (zoomFactor * zRatio > zoomFactorMax || zoomFactor * zRatio < zoomFactorMin) {
			zRatio = 1.0;
		}
		
		Dimension newSize = new Dimension((int) (getMinimumSize().getWidth() * (zoomFactor * zRatio)),
				(int) (getMinimumSize().getHeight() * (zoomFactor * zRatio)));
		
		
		if (newSize.width < getVisibleRect().width ||
				newSize.height < getVisibleRect().height) {
			
			// no size change
			zRatio = 1.0;
			newSize.width = getVisibleRect().width;
			newSize.height = getVisibleRect().height;
		}
		
//		LogMsgFmtln("newsizeA| ", newSize);
		
		Dimension newSize2 = new Dimension(newSize);
		
		zoomRatio = zRatio;
		zoomFactor *= zRatio;
		
		RegexExpress.textAreaZmFactor.setText(dispZoom(zoomFactor));
		
		aft.setToScale(zoomFactor, zoomFactor);
		rxBackground.setZoomScale(zoomFactor);
		

		double xfactor = 1.0;
		double yfactor = 1.0;
		double xfactor2 = 1.0;
		double yfactor2 = 1.0;
		double tx = 0;
		double ty = 0;
		double theta = 0;
		double anchorx = 0;
		double anchory = 0;

		if (false) {
			xfactor = 1.1;
			yfactor = 2.2;
			xfactor2 = 1.1;
			yfactor2 = 2.2;
			tx = 2828;
			ty = 0;
			theta = Math.PI /4;
			anchorx = 0;
			anchory = 0;
		}

		if (tx != 0 && ty != 0) {
			aft.translate(tx, ty);
			rxBackground.setTranslate(tx, ty);
		}
		
		if (theta != 0) {
			aft.rotate(theta, anchorx, anchory);
			rxBackground.setRotate(theta, anchorx, anchory);
		}
		
		newSize = new Dimension((int) (newSize.width * xfactor), (int) (newSize.height * yfactor));

		// this is required
		if (newSize2.width < (int) (getMinimumSize().width)) {
			newSize2.width = (int) (getMinimumSize().width);
		}

		if (newSize2.height < (int) (getMinimumSize().height)) {
			newSize2.height = (int) (getMinimumSize().height);
		}

		newSize2 = new Dimension((int) (newSize2.width * xfactor2), (int) (newSize2.height * yfactor2));
		
//		LogMsgFmtln("newsizeB| ", newSize);
//		LogMsgFmtln("newsize2| ", newSize2);
//		LogMsgFmtln("min size| ", rxBackground.getMinimumSize());
//		LogMsgFmtln("size| ", rxBackground.getSize());
		
		setPreferredSize(newSize);  // must do this - and use newSize

		// parent is the viewport - required to update the
		// view port and keep its size current
		regexViewport.doLayout();
		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			if (comps.length > 0) {
				for (Component c : comps) {
					c.setSize(newSize2);
					c.revalidate();
					c.repaint();
				}
			}
		}
	}
	
	private void positionViewport(Point vpCornerPtScreenZoomed, Point vpOffset) {
		positionViewport(vpCornerPtScreenZoomed, vpOffset.x, vpOffset.y);
	}
	
	private void positionViewport(Point vpCornerPtScreenZoomed, int vpOffsetX, int vpOffsetY) {
		
		int vpX = vpCornerPtScreenZoomed.x - vpOffsetX;
		int vpY = vpCornerPtScreenZoomed.y - vpOffsetY;
		
		vpX = vpX > 0 ? vpX : 0;
		vpY = vpY > 0 ? vpY : 0;
		
		Point vpCornerPtFinal = new Point(vpX, vpY);
		
		// parent is the viewport
		regexViewport.setViewPosition(vpCornerPtFinal);

//		RegexExpress.regexViewport.setViewPosition(vpCorner);
	}
	
	// ***** zoom math functions ***********************************************
	private Point calcLayerPtFromScnPt(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aft.inverseTransform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point calcLayerPtFromScnPt(int x, int y) {
		return calcLayerPtFromScnPt(new Point(x, y));
	}
	
	private Point calcScnPtFromLayerPt(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aft.transform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point calcScnPtFromLayerPt(int x, int y) {
		return calcScnPtFromLayerPt(new Point(x, y));
	}
	
	
	
	// ***** mouse functions ***********************************************
	public void mouseWheelMoved(MouseWheelEvent e) {
		// positive = dn
		// negative = up
		
		if (!priorSaved) {
			saveCurrentZoom();
			priorSaved = true;
		}
		
		if (e.getWheelRotation() < 0) {
			zoomScroll(zoomWheelRatioIn, e.getPoint());
		} else {
			zoomScroll(zoomWheelRatioOut, e.getPoint());
		}
		
		rxPointer.updateCursor(getMousePosition());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		priorSaved = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (dragMode) {
			case PAN:
				if (!priorSaved) {
					saveCurrentZoom();
					priorSaved = true;
				}
				Point newPt = subtractPoints(anchorPoint, e.getPoint());
				anchorPoint = addPoints(e.getPoint(), newPt);
				newPt = addPoints(getVisibleRect().getLocation(), newPt);
				positionViewport(newPt, 0, 0);
				break;
			}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		anchorPoint = e.getPoint();
		if (dragMode == STARTWINDOW) {
			dragMode = WINDOW;
			rxPointer.setPointerModeWindow();
			rxPointer.setWindowAnchorPoint(anchorPoint);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragMode == WINDOW) {
			dragMode = STARTWINDOW;
			rxPointer.setPointerModeXhairs();
			rxPointer.repaint();
			winCornerPoint = e.getPoint();
			
			zoomWindow();
		}
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
		double wRatio = getSize().getWidth() / getPreferredSize().getWidth();
		double hRatio = getSize().getHeight() / getPreferredSize().getHeight();
		
		double zRatio = wRatio > hRatio ? wRatio : hRatio;
		
		setZoomRatio(zRatio);
	}
	
	// mouse clicked - point provided is a screen point (scaled)
	@Override
	public void mouseClicked(MouseEvent e) {
		
		// determine the layer point coordinate from a screen coordinate
		Point mouseLayPoint = calcLayerPtFromScnPt(new Point(e.getX(), e.getY()));
//		Point mouseLayPoint = calcLayerPtFromScnPtRot(new Point(e.getX(), e.getY()));

		RegexExpress.textAreaCoords.setText(dispVal(mouseLayPoint));
		
		LogMsgFmtln("click - lay pt| ", mouseLayPoint);
		LogMsgFmtln("click - scn pt| ", e.getPoint());
		
	}
	
	void listLayPaneInfo() {
		listLayPane();
		listVisRect();
		listViewSize();
		listViewportRect();
	}
	
	void listLayPane() {
		LogMsgln(toString());
	}
	
	void listVisRect() {
		Rectangle visRect = getVisibleRect();
		String subComponent = "vis rect| ";
		
		LogMsg("\n");
		LogMsgFmtln("********* layered pane ", "vis rect *******************************");
		
		LogMsgFmtln(compName + subComponent + "rect| ", visRect);
		LogMsgFmtln(compName + subComponent + "corner x, y| ", visRect.x, visRect.y);
		LogMsgFmtln(compName + subComponent + "getX, getY| ", visRect.getX(), visRect.getY());
		LogMsgFmtln(compName + subComponent + "center| ", visRect.getCenterX(), visRect.getCenterY());
		LogMsgFmtln(compName + subComponent + "bounds| ", visRect.getBounds());
	}
	
	void listViewSize() {
		String subComponent = "v size param| ";
		
		LogMsg("\n");
		LogMsgFmtln("********* layered pane ", "view size param *******************************");
		
		LogMsgFmtln(compName + subComponent + "lay size| ", viewSizeParams.layerSize);
		LogMsgFmtln(compName + subComponent + "min size| ", viewSizeParams.minSize);
		LogMsgFmtln(compName + subComponent + "view size| ", viewSizeParams.viewSize);
	}
	
	void listViewportRect() {
		Point srcPt = new Point(regexViewport.getViewRect().x, regexViewport.getViewRect().y);
		Point transPt = SwingUtilities.convertPoint(RegexExpress.regexScroll, srcPt, this);
		String subComponent = "vp| ";
		
		LogMsg("\n");
		LogMsgFmtln("********* layered pane ", "viewport *******************************");
		
		LogMsgFmtln(compName + subComponent + "view rect| ", regexViewport.getViewRect());
		LogMsgFmtln(compName + subComponent + "loc pt raw| ", srcPt);
		LogMsgFmtln(compName + subComponent + "loc pt adj| ", transPt);
		LogMsgFmtln(compName + subComponent + "vis rect| ", regexViewport.getVisibleRect());
		LogMsgFmtln(compName + subComponent + "ext dim| ", regexViewport.getExtentSize());
		LogMsgFmtln(compName + subComponent + "to view coord| ", regexViewport.toViewCoordinates(regexViewport.getExtentSize()));
	}
	
	void listLayers() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n");
		sb.append(LogMsgStr("********* layer ", "info *******************************"));
		sb.append("\n");
		
		sb.append(LogMsgStr("layer table|  Count| ", dispVal(layerTable.size())));
		sb.append("\n");
		
		for (Map.Entry<String, RegexLayer> entry : layerTable.entrySet()) {
			sb.append(LogMsgStr("name 1| ", entry.getKey()))
					.append(" name 2| ").append(entry.getValue().getName());
			sb.append("\n");
		}
		
		sb.append(LogMsgStr("component count| ", dispVal(getComponentCount())));
		sb.append("\n");
		
		sb.append(LogMsgStr("paramString| ", paramString()));
		sb.append("\n");
		
		sb.append(LogMsgStr("lowest layer| ", dispVal(lowestLayer())));
		sb.append("\n");
		
		sb.append(LogMsgStr("highest layer| ", dispVal(highestLayer())));
		sb.append("\n");
		
		Point pt = new Point();
		Rectangle r = new Rectangle();
		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			
			if (comps.length > 0) {
				sb.append(LogMsgStr("components in layer "
						+ dispVal(i) + "| ", "= "))
						.append(comps.length);
				sb.append("\n");
				
				for (int j = 0; j < comps.length; j++) {
					sb.append(LogMsgStr("component #" + j + "| ", "in layer name| ")).append(comps[j].getName());
					sb.append("\n");
					sb.append(viewSizes(comps[j], viewSizeMask.all(), false));
					sb.append("\n");
					
//					r = comps[j].getBounds();
//					comps[j].setBounds(r.x + 200, r.y + 200, r.width + 400, r.height + 400);
//					comps[j].revalidate();
				}
			}
		}
		
		LogMsgln(sb.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(LogMsgStr("********* layered pane ", "*******************************"));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "location| ", getLocation()));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "pref size| ", getPreferredSize()));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "min size| ", getMinimumSize()));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "max size| ", getMaximumSize()));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "size| ", getSize()));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "bounds| ", getBounds()));
		sb.append("\n");
		
		sb.append(LogMsgStr(compName + "location on screen| ", getLocationOnScreen()));
		
		return sb.toString();
	}
	
}