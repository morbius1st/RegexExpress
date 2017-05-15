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

class RegexLayeredPane extends JLayeredPane implements iCompListener, iMouseListener, iMWListener, iMMListener  { //, MouseWheelListener{
	
	static private final int POINTER_LAYER = JLayeredPane.PALETTE_LAYER - 1;
	
	private TreeMap<String, RegexLayer> layerTable = new TreeMap<>();
	
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

	private static Point priorVPCornerPointScreen;
	private static double priorZoomRatio;
	
	
//	private static final double ZOOMWHEELRATIOOUT = 1.2;
	private static final double ZOOMWHEELRATIOOUT = 1.1;
	private static final double ZOOMWHEELRATIOIN = 1 / ZOOMWHEELRATIOOUT;
	private static final double ZOOMRATIOINOUT = 1.5;
	
	DragModes dragMode = PAN;
	
	static Point anchorPoint = new Point(0, 0);
	static Point winCornerPoint = new Point();
	
	private static RegexLayer firstLayer = null;
	
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
		
		priorZoomRatio = 1.0;
		
		setZoomRatio_New(zoomFactor);
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
//
//	void zoomOut() {
//		int x = (getVisibleRect().width) / 2;
//		int y = (getVisibleRect().height) / 2;
//
//		Point vpCenter = new Point(getVisibleRect().x + x,
//				getVisibleRect().y + y);
//
//		zoomCentered(ZOOMRATIOINOUT, vpCenter);
//	}
//
//
//	void zoomIn() {
//		int x = (getVisibleRect().width) / 2;
//		int y = (getVisibleRect().height) / 2;
//
//		Point vpCenter = new Point(getVisibleRect().x + x,
//				getVisibleRect().y + y);
//
//		zoomCentered(1 / ZOOMRATIOINOUT, vpCenter);
//	}


//	void zoomCenteredDwgCoord(double zRatio, Point zoomCenterDwgCoord) {
//		zoomCentered(zRatio, calcScnPtFromLayerPt(zoomCenterDwgCoord));
//	}
//
//
//	// zoom centerered in viewport (screen) coordinates
//	void zoomCentered(double zRatio, Point zoomCenter) {
//
//		int x = (getVisibleRect().width) / 2;
//		int y = (getVisibleRect().height) / 2;
//
//		zoom(zRatio, zoomCenter, x, y);
//	}



//	void zoomWindow() {
//		// got corners of zoom window
//		// points are in screen (scaled) coordinates
//
////		RegexExpress.textAreaCoords.setText("start: " +
////				dispVal(anchorPoint) + "  end: " +
////				dispVal(winCornerPt));
//
//		Point visRectCorner = addToPoint(getVisibleRect().getLocation(),
//				getVisibleRect().width, getVisibleRect().height);
//
////		LogMsgFmtln("vis rect bounds: ", getVisibleRect().getBounds());
//
//
//		// determine scale ratio - use the smaller of the comparison of
//		// the sides
//		int winWidth = Math.abs(anchorPoint.x - winCornerPoint.x);
//		int winHeight = Math.abs(anchorPoint.y - winCornerPoint.y);
//
//		double ratioX = getVisibleRect().getWidth() / winWidth;
//		double ratioY = getVisibleRect().getHeight() / winHeight;
//
//		LogMsgFmtln("ratios| x| ", ratioX + " Y| " + ratioY);
//
//		double scaleRatio = Math.min(getVisibleRect().getWidth() / winWidth,
//				getVisibleRect().getHeight() / winHeight);
//
//
//		Point center = new Point(anchorPoint.x - (anchorPoint.x - winCornerPoint.x) / 2,
//				anchorPoint.y  - (anchorPoint.y - winCornerPoint.y) / 2);
//
//		LogMsgFmtln("anchor pt: ", anchorPoint);
//		LogMsgFmtln("wincornerpt: ", winCornerPoint);
//		LogMsgFmtln("center| ", center);
////		LogMsgFmtln("center to lay| ", dispVal(calcLayerPtFromScnPt(center)));
////		LogMsgFmtln("z ratio| ", scaleRatio);
////		LogMsgFmtln("z factor| ", dispVal(zoomFactor));
////		LogMsgFmtln("z factor final| ", dispVal(zoomFactor * scaleRatio));
//
//
////		if (scaleRatio > 2.0)
////			scaleRatio = 1.9;
//
//		Point dwgCoord = calcLayerPtFromScnPt(center);
//
//		aff.transform(dwgCoord, center);
////		center = calcScnPtFromLayerPt(center);
//
//		moveToPoint(center);
//		zoomAboutPoint_New(scaleRatio, center);
//
//	}

//	private void zoom(double zRatio, Point vpPoint, int vpOffsetX, int vpOffsetY) {
//		Point drawingCoord = calcLayerPtFromScnPt(vpPoint);
//		setZoomRatio(zRatio);
//		Point zoomedCoord = calcScnPtFromLayerPt(drawingCoord);
//
//		positionViewport_New(zoomedCoord, vpOffsetX, vpOffsetY);
//
//		zoomViews();
//
////		positionViewport_New(zoomedCoord, vpOffsetX, vpOffsetY);
//	}


//	// move to a point (layer type) coordinates
//	void moveToPoint2(Point drawingCoord) {
//		moveToPoint(calcScnPtFromLayerPt(drawingCoord));
//	}
//
//	// move to a point (screen type) coordinates
//	void moveToPoint(Point screenCoord) {
//
//		Point existPoint = new Point(
//				getVisibleRect().x + getVisibleRect().width/2,
//				getVisibleRect().y + getVisibleRect().height/2);
//
//		int x = screenCoord.x - existPoint.x;
//		int y = screenCoord.y - existPoint.y;
//
//		positionViewport_New(new Point(getVisibleRect().x + x, getVisibleRect().y + y), 0, 0);
//	}

//	private void setZoomRatio(double zRatio) {
//
////		if (zRatio > 1.75) {zRatio = 1.75;}
//
//		zoomFactor *= zRatio;
//
//		if (zoomFactor > zoomMax) {
//			zoomFactor = zoomMax;
//			zoomRatio = 1;
//		} else if (zoomFactor < zoomMin) {
//			zoomFactor = zoomMin;
//			zoomRatio = 1;
//		} else {
//			zoomRatio = zRatio;
//		}
//
//		aff.setToScale(zoomFactor, zoomFactor);
//
//		RegexExpress.textAreaZmFactor.setText(dispZoom(zoomFactor));
//
//	}

//	private void zoomViews() {
//		Dimension viewSize = getZoomedViewSize();
//
//		// set this (Layered Pane's) size
//		setPreferredSize(viewSize);
//		revalidate();
//
//		for (int i = lowestLayer(); i <= highestLayer(); i++) {
//			Component[] comps = getComponentsInLayer(i);
//			if (comps.length > 0) {
//				for (Component c : comps) {
//					c.setSize(viewSize);
//					((iRxLayer) c).setZoomScale(zoomFactor);
//					c.revalidate();
//					c.repaint();
//				}
//			}
//		}
//	}
	
	// restrict the zoomed view size to be no smaller than the
	// layered pane minimum size
//	private Dimension getZoomedViewSize() {
//		viewSizeParams.viewSize = viewSizeParams.viewSize.multiply(zoomRatio);
//
//		LogMsgFmtln("getZoomedViewSize| viewsize 1| ", dispVal(viewSizeParams.viewSize));
//
//		Dimension2dx result = new Dimension2dx();
//		Dimension2dx proposed = viewSizeParams.viewSize.clone();
//		Dimension2dx viewport = Dimension2dx.toDimension2dx(getParent().getSize());
//
//		if (proposed.width > viewport.width) {
//			result.width = proposed.width;
//		} else if (proposed.width < viewport.width) {
//			result.width = viewport.width;
//		}
//
//		if (proposed.height > viewport.height) {
//			result.height = proposed.height;
//		} else if (proposed.height < viewport.height) {
//			result.height = viewport.height;
//		}
//
//		if (result.width < viewSizeParams.minSize.width) {
//			result.width = viewSizeParams.minSize.width;
//		}
//// 			else if (result.width > viewSizeParams.minSize.width * 12) {
////			result.width = viewSizeParams.minSize.width * 12;
////		}
////
//		if (result.height < viewSizeParams.minSize.height) {
//			result.height = viewSizeParams.minSize.height;
//		}
//// 			else if (result.height > viewSizeParams.minSize.height * 12) {
////			result.height = viewSizeParams.minSize.height * 12;
////		}
//
//		LogMsgFmtln("getZoomedViewSize| viewsize 2| ", dispVal(viewSizeParams.viewSize));
//
//		return result.toDimension();
//	}
	
	
	
	
	
	
	
	// ***** new **********************************************************
	
	// ***** zoom functions ***********************************************
	// zoom to a user selected window
	void zoomWindow_New() {
		saveCurrentZoom_New();
		
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
		
		setZoomRatio_New(zRatio);
		moveToPoint2_New(vpCenterScreen);
	}
	
	
	
	// zoom about a point (screen) coordinates
	private void zoomAboutPoint_New(double zRatio, Point vpPtScreen) {
		
		int x = vpPtScreen.x - getVisibleRect().x;
		int y = vpPtScreen.y - getVisibleRect().y;
		
		zoom_New(zRatio, vpPtScreen, x, y);
	}
	
	void zoomOut_New() {
		saveCurrentZoom_New();
		
		Point vpCenterScreen = new Point((int) getVisibleRect().getCenterX(),
				(int) getVisibleRect().getCenterY());
		Point vpCenterLayer = calcLayerPtFromScnPt(vpCenterScreen);
		
		setZoomRatio_New(0.5);
		moveToPoint2_New(vpCenterLayer);
	}
	

	void zoomIn_New() {
//		saveCurrentZoom_New();
		
		Point vpCenterScreen = new Point((int) getVisibleRect().getCenterX(),
				(int) getVisibleRect().getCenterY());
		
		int x = getVisibleRect().width / 2;
		int y = getVisibleRect().height / 2;
		Zoom_New(2.0, vpCenterScreen, x, y);
	
	}

	// this is an exception - it does not go through the zoom method that
	// saves the current viewport location
	void zoomPrevious_New() {
		zoom_New(1 / priorZoomRatio, priorVPCornerPointScreen, 0, 0);
	}
	
	// primary zoom method that also saves the prior zoom information for later
	// zoom previous
	private void Zoom_New(double zoomRatio, Point vpCornerPtScreen, int vpOffsetX, int vpOffsetY) {
		priorVPCornerPointScreen = viewport.getViewPosition();
		priorZoomRatio = zoomRatio;
		
		zoom_New(zoomRatio, vpCornerPtScreen, vpOffsetX, vpOffsetY);
	}
	
	// primary zoom method - the intent is that all (with some exceptions) zoom methods go through this method
	// for every zoom
	private void zoom_New(double zoomRatio, Point vpCornerPtScreen, int vpOffsetX, int vpOffsetY) {
		Point vpCornerPointLayer = calcLayerPtFromScnPt(vpCornerPtScreen);

		setZoomRatio_New(zoomRatio);

		Point vpCornerPtScreenZoomed = calcScnPtFromLayerPt(vpCornerPointLayer);
		
		positionViewport_New(vpCornerPtScreenZoomed, vpOffsetX, vpOffsetY);
	}
	
	
	// ***** display position functions ***********************************************
	void moveToPoint2_New(Point layerCoord) {
		moveToPoint_New(calcScnPtFromLayerPt(layerCoord));
	}
	
	void moveToPoint_New(Point screenCoord) {
		
		saveCurrentZoom_New();
		
		int x = getVisibleRect().width / 2;
		int y = getVisibleRect().height / 2;
		
		positionViewport_New(screenCoord, x, y);
	}
	
	// ***** display utility functions ***********************************************
	void startZoomWindow_New() {
		dragMode = STARTWINDOW;
	}
	
	void saveCurrentZoom_New() {
		priorVPCornerPointScreen = viewport.getViewPosition();
	}
	
	void setZoomRatio_New(double zoomRatio) {
		Dimension newSize = new Dimension((int) (getMinimumSize().getWidth() * zoomFactor * zoomRatio),
				(int) (getMinimumSize().getHeight() * zoomFactor * zoomRatio));
		
		if (newSize.width < getVisibleRect().width ||
				newSize.height < getVisibleRect().height) {
			
			// no size change
			zoomRatio = 1.0;
			newSize.width = getVisibleRect().width;
			newSize.height = getVisibleRect().height;
		}
		
		
		zoomFactor *= zoomRatio;
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
	
	private void positionViewport_New(Point vpCornerPtScreenZoomed, Point vpOffset) {
		positionViewport_New(vpCornerPtScreenZoomed, vpOffset.x, vpOffset.y);
	}
	
	private void positionViewport_New(Point vpCornerPtScreenZoomed, int vpOffsetX, int vpOffsetY) {
		
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
		
		if (e.getWheelRotation() < 0) {
			zoomAboutPoint_New(ZOOMWHEELRATIOOUT, e.getPoint());
		} else {
			zoomAboutPoint_New(ZOOMWHEELRATIOIN, e.getPoint());
		}
		
		rxPointer.updateCursor(getMousePosition());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (dragMode) {
			case PAN:
				Point newPt = subtractPoints(anchorPoint, e.getPoint());
				anchorPoint = addPoints(e.getPoint(), newPt);
				newPt = addPoints(getVisibleRect().getLocation(), newPt);
				positionViewport_New(newPt, 0, 0);
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
			
			zoomWindow_New();
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