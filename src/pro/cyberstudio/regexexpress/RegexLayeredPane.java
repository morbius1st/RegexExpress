package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.regexexpress.Utility.DragModes.*;


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
	
//	PriorZoom(double zFactor, Point vpCornerScreen) {
//		priorZoomFactor = zFactor;
//		priorVPCornerPtScreen = vpCornerScreen;
//	}
}

class RegexLayeredPane extends JLayeredPane implements iCompListener, iMouseListener, iMWListener, iMMListener  { //, MouseWheelListener{
	
	static private final int POINTER_LAYER = JLayeredPane.PALETTE_LAYER - 1;
	
	// hold the actual instance
	private static RegexLayeredPane me;
	private static JViewport viewport;
	private static RegexBackground rxBackground;
	private static RegexPointer rxPointer;
	private static RegexZero rxZero;
	
	private static viewSizeData viewSizeParams = new viewSizeData();
	
	AffineTransform aff = new AffineTransform();
	
	private static double zoomFactor = 1.0;
	private static double zoomRatio = 1.0;
	private static double zoomMin = 0.16;
	private static double zoomMax = 12.0;
	
	private static final int MAXPRIORZOOMS = 20;
	
	private static final double ZOOMWHEELRATIOIN = 1.1;
	private static final double ZOOMRATIOIN = 1.5;
	
	private static double zoomWheelRatioOut = 1 / ZOOMWHEELRATIOIN; // initial value
	private static double zoomWheelRatioIn = ZOOMWHEELRATIOIN; // initial value
	
	private static double getZoomRatioOut = 1 / ZOOMRATIOIN;
	private static double getZoomRatioIn = ZOOMRATIOIN;
	
	private static DragModes dragMode = PAN;
	
	private static Point anchorPoint = new Point(0, 0);
	private static Point winCornerPoint = new Point();
	
	private static RegexLayer firstLayer = null;
	
	private static Point priorVPCornerPointScreen;
	private static double priorZoomFactor = 1.0;
	private static boolean priorSaved = false;
	
	private TreeMap<String, RegexLayer> layerTable = new TreeMap<>();
	private CircularStack<PriorZoom> priorZooms = new CircularStack<>(MAXPRIORZOOMS);
	
	// private constructor to prevent creating
	// an instance of this class
	private RegexLayeredPane() {
		// assign minimal sizes to the view size parameters
		viewSizeParams.minSize = new Dimension2dx(100, 100);
		viewSizeParams.viewSize = new Dimension2dx(100, 100);
		viewSizeParams.layerSize = new Dimension2dx(100, 100);
		setName("RegexLayeredPane");
		
		
		aff.setToScale(zoomFactor, zoomFactor);
		
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
	
	void initialize(JScrollBar hScrollBar, JScrollBar vScrollBar, Dimension2dx initSize) {
		viewSizeParams.minSize = initSize.clone();
		viewSizeParams.layerSize = initSize.clone();
		viewSizeParams.viewSize = initSize.clone();
		
		Dimension layerSize = new Dimension(RegexExpress.LAYERX, RegexExpress.LAYERY);
		
		rxZero.setMinimumSize(layerSize);
		rxZero.setPreferredSize(layerSize);
		rxZero.setOpaque(false);
		rxZero.setVisible(true);
		rxZero.setName("zero");
		add(rxZero, JLayeredPane.DEFAULT_LAYER);
		
		rxPointer.setMinimumSize(layerSize);
		rxPointer.setPreferredSize(layerSize);
		rxPointer.assignScrollBars(hScrollBar, vScrollBar);
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
		
		viewport = ((JViewport) getParent());
		
		setZoomRatio(zoomFactor);
		updateSize();
	}
	
	void getLayerOneInfo() {
		LogMsgln(firstLayer.toString());
	}
	

	Component add(String layerName) {
		RegexLayer layer = new RegexLayer();
		layer.setName(layerName);
		layer.setMinimumSize(rxZero.getMinimumSize());
		layer.setPreferredSize(rxZero.getMinimumSize());
		layer.setSize(getPreferredSize());
		layer.setOpaque(false);
		layer.setVisible(true);
		layer.setZoomScale(zoomFactor);
		
		add(layer, (Integer) (layerTable.size() + 1));
		layerTable.put(layerName, layer);
		
		if (firstLayer == null) {
			firstLayer = layer;
		}
		
		return layer;
	}
	

	void testPointer() {
		rxPointer.test();
	}
	
	void listMyInfo() {
		LogMsgFmtln("lay pane: vis rect| ", dispVal(getVisibleRect()));
		LogMsgFmtln("lay pane: bounds| ", dispVal(getBounds()));
		LogMsgFmtln("lay pane: location| ", dispVal(this.getLocation()));
		LogMsgFmtln("lay pane: pref size| ", dispVal(this.getPreferredSize()));
		LogMsgFmtln("lay pane: min size| ", dispVal(this.getMinimumSize()));
		LogMsgFmtln("lay pane: max size| ", dispVal(this.getMaximumSize()));
		LogMsgFmtln("lay pane: size| ", dispVal(this.getSize()));
		LogMsgFmtln("lay pane: location on screen| ", dispVal(this.getLocationOnScreen()));
		
		LogMsgFmtln("view size param: lay size| ", dispVal(viewSizeParams.layerSize));
		LogMsgFmtln("lview size param: min size| ", dispVal(viewSizeParams.minSize));
		LogMsgFmtln("view size param: view size| ", dispVal(viewSizeParams.viewSize));
	}
	
	
	void listVisRect() {
		// the corner of the rect as a point / integer
		LogMsgFmtln("lay pane: vis rect: location| ", getVisibleRect().getLocation());
		// same as location but as x & y values
		LogMsgFmtln("lay pane: vis rect: corner x| ", dispVal(getVisibleRect().x) +
				" y| " + dispVal(getVisibleRect().y));
		// same as location but as a double
		LogMsgFmtln("lay pane: vis rect: corner x| ", dispVal(getVisibleRect().getX()) +
				" y| " + dispVal(getVisibleRect().getY()));
		// calculated center as an int
		LogMsgFmtln("lay pane: vis rect: center x| ", dispVal(getVisibleRect().getCenterX()) +
				" y| " + dispVal(getVisibleRect().getCenterY()));
		// booth appear to be the same
		LogMsgFmtln("lay pane: vis rect:| ", getVisibleRect());
		LogMsgFmtln("lay pane: vis rect: bounds| ", getVisibleRect().getBounds());
		
	}
	
	void listVisRect2() {
		LogMsgFmtln("lay pane: vis rect:| ", getVisibleRect());
		LogMsgFmtln("lay pane: vis rect: bounds| ", getVisibleRect().getBounds());
	}
	
	private void listViewportRect() {

		Point srcPt = new Point(viewport.getViewRect().x, viewport.getViewRect().y);
		Point transPt = SwingUtilities.convertPoint(RegexExpress.regexScroll, srcPt, this);
		
		LogMsgln("vp  view rect: " + Utility.dispVal(viewport.getViewRect()));
		LogMsgln("vp loc pt raw: " + dispVal(srcPt));
		LogMsgln("vp loc pt adj: " + dispVal(transPt));
		
		LogMsgln("vp   vis rect: " + Utility.dispVal(viewport.getVisibleRect()));
		LogMsgln("vp    ext dim: " + Utility.dispVal(viewport.getExtentSize()));
		LogMsgln("vp to view co: " + Utility.dispVal(viewport.toViewCoordinates(viewport.getExtentSize())));
		LogMsgln("this vis rect: " + Utility.dispVal(getVisibleRect())); // yes
		LogMsgln("this     size: " + Utility.dispVal(getSize()));  // no
		LogMsgln("this    w x h: " + Utility.dispVal(getWidth(), getHeight()));  // no
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

		if (priorZooms.isEmpty()) {
			return;
		}
		
		PriorZoom pz = priorZooms.pop();
		
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
	
	private void saveCurrentZoom() {
		
		PriorZoom pz = new PriorZoom();
		pz.priorZoomFactor = zoomFactor;
		pz.priorVPCornerPtScreen = viewport.getViewPosition();

		priorZooms.push(pz);
	}
	
	void setZoomRatio(double zRatio) {
		Dimension newSize = new Dimension((int) (getMinimumSize().getWidth() * zoomFactor * zRatio),
				(int) (getMinimumSize().getHeight() * zoomFactor * zRatio));
		
		if (newSize.width < getVisibleRect().width ||
				newSize.height < getVisibleRect().height) {
			
			// no size change
			zRatio = 1.0;
			newSize.width = getVisibleRect().width;
			newSize.height = getVisibleRect().height;
		}

		zoomRatio = zRatio;
		zoomFactor *= zRatio;
		
		aff.setToScale(zoomFactor, zoomFactor);

		RegexExpress.textAreaZmFactor.setText(dispZoom(zoomFactor));
		
		setPreferredSize(newSize);
		
		// required to make sure that the size is current
		setSize(newSize);
		revalidate();
		repaint();
		
		// parent is the viewport - required to update the
		// view port and keep its size current
		getParent().doLayout();
		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			if (comps.length > 0) {
				for (Component c : comps) {
					c.setSize(newSize);
					((iRxLayer) c).setZoomScale(zoomFactor);
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
		viewport.setViewPosition(vpCornerPtFinal);

//		RegexExpress.regexViewport.setViewPosition(vpCorner);
	}
	
	// ***** zoom math functions ***********************************************
	private Point calcLayerPtFromScnPt(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aff.inverseTransform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point calcLayerPtFromScnPt(int x, int y) {
		return calcLayerPtFromScnPt(new Point(x, y));
	}
	
	private Point calcScnPtFromLayerPt(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aff.transform(ptSrc, ptDest);
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
			dragMode = PAN;
			rxPointer.setPointerModeXhairs();
			rxPointer.repaint();
			winCornerPoint = e.getPoint();
			
			zoomWindow();
		}
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
//		Dimension2dx zoomedSize = Dimension2dx.toDimension2dx(getZoomedViewSize());
//		setPreferredSize(zoomedSize.toDimension());
//		updateSize();
//		revalidate();
	}
	
	// mouse clicked - point provided is a screen point (scaled)
	@Override
	public void mouseClicked(MouseEvent e) {
		
		// determine the layer point coordinate from a screen coordinate
		Point mouseLayPoint = calcLayerPtFromScnPt(new Point(e.getX(), e.getY()));
		
		// revert - calculate the layer coordinate from a drawing coordinate
		Point mouseScnPoint = calcScnPtFromLayerPt(mouseLayPoint);

		RegexExpress.textAreaCoords.setText(dispVal(mouseLayPoint));

		LogMsgFmtln("lay pane: ", "mouse clicked");
		LogMsgFmtln("scn point: ", e.getPoint());
		LogMsgFmtln("lay point: ", mouseLayPoint);
		LogMsgFmtln("back to scn point: ", mouseScnPoint);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("layer table:  Count: " + layerTable.size());
		sb.append("\n");
		
		for (Map.Entry<String, RegexLayer> entry : layerTable.entrySet()) {
			sb.append("name 1: ").append(entry.getKey())
					.append(" name 2: ").append(entry.getValue().getName());
			sb.append("\n");
		}
		
		sb.append("component count: ").append(getComponentCount());
		sb.append("\n");
		sb.append("paramString: ").append(paramString());
		sb.append("\n");
		
		sb.append("lowest layer : ").append(lowestLayer()).append("\n");
		sb.append("highest layer: ").append(highestLayer()).append("\n");
		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			
			if (comps.length > 0) {
				sb.append("components in layer: ").append(i).append(" = ").append(comps.length);
				sb.append("\n");
				
				for (int j = 0; j < comps.length; j++) {
					sb.append("component #").append(j).append(" in layer: name= ").append(comps[j].getName());
					sb.append("\n\t     size: " + Utility.dispVal(comps[j].getSize()));
					sb.append("\n\tperf size: " + Utility.dispVal(comps[j].getPreferredSize()));
					sb.append("\n\t min size: " + Utility.dispVal(comps[j].getMinimumSize()));
					sb.append("\n\t max size: " + Utility.dispVal(comps[j].getMaximumSize()));
					sb.append("\n");
				}
			}
		}
		
		return sb.toString();
	}
	
}