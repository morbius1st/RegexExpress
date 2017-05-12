package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import javafx.embed.swing.JFXPanel;
import sun.rmi.log.ReliableLog.LogFile;

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
	private static RegexBackground rxBackground;
	private static RegexPointer rxPointer;
	private static RegexZero rxZero;
	
	private static viewSizeData viewSizeParams = new viewSizeData();
	
	AffineTransform aft = new AffineTransform();
	
	private double zoomFactor = 1.0;
	private double zoomRatio = 1.0;
	private double zoomMin = 0.16;
	private double zoomMax = 12.0;
//

//	private static final double ZOOMWHEELRATIOOUT = 1.2;
	private static final double ZOOMWHEELRATIOOUT = 1.1;
	private static final double ZOOMWHEELRATIOIN = 1 / ZOOMWHEELRATIOOUT;
	private static final double ZOOMRATIOINOUT = 1.5;
	
	DragModes dragMode = PAN;
	
	static Point anchorPoint = new Point(0, 0);
	static Point winCornerPt = new Point();
	
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
		
		updateSize();
	}

	Component add(String layerName) {
		RegexLayer layer = new RegexLayer();
		
		layer.setMinimumSize(rxZero.getMinimumSize());
		layer.setPreferredSize(rxZero.getMinimumSize());
		layer.setName(layerName);
		layer.setSize(getPreferredSize());
		layer.setOpaque(false);
		layer.setVisible(true);
		layer.setZoomScale(zoomFactor);
		
		add(layer, (Integer) (layerTable.size() + 1));
		layerTable.put(layerName, layer);
		
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
	
	
	void testVisRect() {
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
	
	private void getViewportRect() {

		Point srcPt = new Point(RegexExpress.regexViewport.getViewRect().x, RegexExpress.regexViewport.getViewRect().y);
		Point transPt = SwingUtilities.convertPoint(RegexExpress.regexScroll, srcPt, this);
		
		LogMsgln("vp  view rect: " + Utility.dispVal(RegexExpress.regexViewport.getViewRect()));
		LogMsgln("vp loc pt raw: " + dispVal(srcPt));
		LogMsgln("vp loc pt adj: " + dispVal(transPt));
		
		LogMsgln("vp   vis rect: " + Utility.dispVal(RegexExpress.regexViewport.getVisibleRect()));
		LogMsgln("vp    ext dim: " + Utility.dispVal(RegexExpress.regexViewport.getExtentSize()));
		LogMsgln("vp to view co: " + Utility.dispVal(RegexExpress.regexViewport.toViewCoordinates(RegexExpress.regexViewport.getExtentSize())));
		LogMsgln("this vis rect: " + Utility.dispVal(getVisibleRect())); // yes
		LogMsgln("this     size: " + Utility.dispVal(getSize()));  // no
		LogMsgln("this    w x h: " + Utility.dispVal(getWidth(), getHeight()));  // no
	}
	
	private Point calcLayPtFromScnPt(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aft.inverseTransform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point calcLayPtFromScnPt(int x, int y) {
		return calcLayPtFromScnPt(new Point(x, y));
	}
	
	private Point calcScnPtFromLayPt(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aft.transform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point calcScnPtFromLayPt(int x, int y) {
		return calcScnPtFromLayPt(new Point(x, y));
	}
	
	void zoomOut() {
		int x = (getVisibleRect().width) / 2;
		int y = (getVisibleRect().height) / 2;
		
		Point vpCenter = new Point(getVisibleRect().x + x,
				getVisibleRect().y + y);
		
		zoomCentered(ZOOMRATIOINOUT, vpCenter);
	}
	
	
	void zoomIn() {
		int x = (getVisibleRect().width) / 2;
		int y = (getVisibleRect().height) / 2;
		
		Point vpCenter = new Point(getVisibleRect().x + x,
				getVisibleRect().y + y);
		
		zoomCentered(1 / ZOOMRATIOINOUT, vpCenter);
	}
	
	
	void zoomCenteredDwgCoord(double zRatio, Point zoomCenterDwgCoord) {
		zoomCentered(zRatio, calcScnPtFromLayPt(zoomCenterDwgCoord));
	}
	
	
	// zoom centerered in viewport (screen) coordinates
	void zoomCentered(double zRatio, Point zoomCenter) {

		int x = (getVisibleRect().width) / 2;
		int y = (getVisibleRect().height) / 2;
		
		zoom(zRatio, zoomCenter, x, y);
	}
	
	// zoom about a point (screen) coordinates
	private void zoomAboutPoint(double zRatio, Point vpPoint) {
	
//		LogMsgFmtln("zoomAboutPt| ratio| ", zRatio + "  point| " + dispVal(vpPoint));
		
		int x = vpPoint.x - getVisibleRect().x;
		int y = vpPoint.y - getVisibleRect().y;
		
		zoom(zRatio, vpPoint, x, y);
	}
	
	void windowZoom() {
		dragMode = STARTWINDOW;
	}
	
	void zoomWindow() {
		// got corners of zoom window
		// points are in screen (scaled) coordinates
		
//		RegexExpress.textAreaCoords.setText("start: " +
//				dispVal(anchorPoint) + "  end: " +
//				dispVal(winCornerPt));
		
		Point visRectCorner = addToPoint(getVisibleRect().getLocation(),
				getVisibleRect().width, getVisibleRect().height);
		
//		LogMsgFmtln("vis rect bounds: ", getVisibleRect().getBounds());
		

		// determine scale ratio - use the smaller of the comparison of
		// the sides
		int winWidth = Math.abs(anchorPoint.x - winCornerPt.x);
		int winHeight = Math.abs(anchorPoint.y - winCornerPt.y);
		
		double ratioX = getVisibleRect().getWidth() / winWidth;
		double ratioY = getVisibleRect().getHeight() / winHeight;
		
		LogMsgFmtln("ratios| x| ", ratioX + " Y| " + ratioY);
		
		double scaleRatio = Math.min(getVisibleRect().getWidth() / winWidth,
				getVisibleRect().getHeight() / winHeight);
		
		
		Point center = new Point(anchorPoint.x - (anchorPoint.x - winCornerPt.x) / 2,
				anchorPoint.y  - (anchorPoint.y - winCornerPt.y) / 2);
		
		LogMsgFmtln("anchor pt: ", anchorPoint);
		LogMsgFmtln("wincornerpt: ", winCornerPt);
		LogMsgFmtln("center| ", center);
//		LogMsgFmtln("center to lay| ", dispVal(calcLayPtFromScnPt(center)));
//		LogMsgFmtln("z ratio| ", scaleRatio);
//		LogMsgFmtln("z factor| ", dispVal(zoomFactor));
//		LogMsgFmtln("z factor final| ", dispVal(zoomFactor * scaleRatio));


//		if (scaleRatio > 2.0)
//			scaleRatio = 1.9;

		Point dwgCoord = calcLayPtFromScnPt(center);
		
		aft.transform(dwgCoord, center);
//		center = calcScnPtFromLayPt(center);
		
		moveToPoint(center);
		zoomAboutPoint(scaleRatio, center);
	
	}
	
	double calcWindowDiagonal(Point p1, Point p2) {
		return Math.hypot(p1.x - p2.x, p1.y - p2.y);
	}
	
	private void zoom(double zRatio, Point vpPoint, int vpOffsetX, int vpOffsetY) {
		Point drawingCoord = calcLayPtFromScnPt(vpPoint);
		setZoomRatio(zRatio);
		Point zoomedCoord = calcScnPtFromLayPt(drawingCoord);
		
		positionViewport(zoomedCoord, vpOffsetX, vpOffsetY);
		
		zoomViews();
		
//		positionViewport(zoomedCoord, vpOffsetX, vpOffsetY);
	}
	
	private void positionViewport(Point zoomedCoord, Point vpOffset) {
		positionViewport(zoomedCoord, vpOffset.x, vpOffset.y);
	}
	
	private void positionViewport(Point zoomedCoord, int vpOffsetX, int vpOffsetY) {
		
		int vpX = zoomedCoord.x - vpOffsetX;
		int vpY = zoomedCoord.y - vpOffsetY;
		
		vpX = vpX > 0 ? vpX : 0;
		vpY = vpY > 0 ? vpY : 0;
		
		Point vpCorner = new Point(vpX, vpY);
		
		RegexExpress.regexViewport.setViewPosition(vpCorner);
	}
	
	// move to a point (layer type) coordinates
	void moveToPoint2(Point drawingCoord) {
		moveToPoint(calcScnPtFromLayPt(drawingCoord));
	}
	
	// move to a point (screen type) coordinates
	void moveToPoint(Point screenCoord) {
	
		Point existPoint = new Point(
				getVisibleRect().x + getVisibleRect().width/2,
				getVisibleRect().y + getVisibleRect().height/2);
		
		int x = screenCoord.x - existPoint.x;
		int y = screenCoord.y - existPoint.y;
		
		positionViewport(new Point(getVisibleRect().x + x, getVisibleRect().y + y), 0, 0);
	}

	private void setZoomRatio(double zRatio) {
	
//		if (zRatio > 1.75) {zRatio = 1.75;}
		
		zoomFactor *= zRatio;
		
		if (zoomFactor > zoomMax) {
			zoomFactor = zoomMax;
			zoomRatio = 1;
		} else if (zoomFactor < zoomMin) {
			zoomFactor = zoomMin;
			zoomRatio = 1;
		} else {
			zoomRatio = zRatio;
		}
		
		aft.setToScale(zoomFactor, zoomFactor);
		
		RegexExpress.textAreaZmFactor.setText(dispZoom(zoomFactor));
		
	}
	
	private void zoomViews() {
		Dimension viewSize = getZoomedViewSize();

		// set this (Layered Pane's) size
		setPreferredSize(viewSize);
		revalidate();
		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			if (comps.length > 0) {
				for (Component c : comps) {
					c.setSize(viewSize);
					((iRxLayer) c).setZoomScale(zoomFactor);
					c.revalidate();
					c.repaint();
				}
			}
		}
	}
	
	// restrict the zoomed view size to be no smaller than the
	// layered pane minimum size
	private Dimension getZoomedViewSize() {
		viewSizeParams.viewSize = viewSizeParams.viewSize.multiply(zoomRatio);
		
		LogMsgFmtln("getZoomedViewSize| viewsize 1| ", dispVal(viewSizeParams.viewSize));

		Dimension2dx result = new Dimension2dx();
		Dimension2dx proposed = viewSizeParams.viewSize.clone();
		Dimension2dx viewport = Dimension2dx.toDimension2dx(getParent().getSize());
		
		if (proposed.width > viewport.width) {
			result.width = proposed.width;
		} else if (proposed.width < viewport.width) {
			result.width = viewport.width;
		}
		
		if (proposed.height > viewport.height) {
			result.height = proposed.height;
		} else if (proposed.height < viewport.height) {
			result.height = viewport.height;
		}
		
		if (result.width < viewSizeParams.minSize.width) {
			result.width = viewSizeParams.minSize.width;
		}
// 			else if (result.width > viewSizeParams.minSize.width * 12) {
//			result.width = viewSizeParams.minSize.width * 12;
//		}
//
		if (result.height < viewSizeParams.minSize.height) {
			result.height = viewSizeParams.minSize.height;
		}
// 			else if (result.height > viewSizeParams.minSize.height * 12) {
//			result.height = viewSizeParams.minSize.height * 12;
//		}
		
		LogMsgFmtln("getZoomedViewSize| viewsize 2| ", dispVal(viewSizeParams.viewSize));
		
		return result.toDimension();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		// positive = dn
		// negative = up
		
		if (e.getWheelRotation() < 0) {
			zoomAboutPoint(ZOOMWHEELRATIOOUT, e.getPoint());
		} else {
			zoomAboutPoint(ZOOMWHEELRATIOIN, e.getPoint());
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
			winCornerPt = e.getPoint();
			
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
		LogMsgFmtln("lay pane: ", "mouse clicked");
		LogMsgFmtln("event point: ", e.getPoint());
		
		Point mouseLayPoint = calcLayPtFromScnPt(new Point(e.getX(), e.getY()));

		RegexExpress.textAreaCoords.setText(dispVal(mouseLayPoint));

		LogMsgFmtln("lay point: ", mouseLayPoint);
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