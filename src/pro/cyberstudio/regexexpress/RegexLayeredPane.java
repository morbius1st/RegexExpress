package pro.cyberstudio.regexexpress;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.RegexLayeredPane.dragMode.pan;
import static pro.cyberstudio.regexexpress.RegexLayeredPane.dragMode.window;
import static pro.cyberstudio.regexexpress.Utility.*;


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
	
	private static final double ZOOMWHEELRATIOOUT = 1.2;
	private static final double ZOOMWHEELRATIOIN = 1 / ZOOMWHEELRATIOOUT;
	private static final double ZOOMRATIOINOUT = 1.5;
	
//	static boolean inPanningMode = true;

	enum dragMode {pan, window, line; }
	
	dragMode currentDragMode = pan;
	
	static Point panPriorPt = new Point(0, 0);
	Point winCornerPt = new Point();
	
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
		
		rxZero.setOpaque(false);
		rxZero.setVisible(true);
		rxZero.setName("zero");
		add(rxZero, JLayeredPane.DEFAULT_LAYER);
		
		rxPointer.assignScrollBars(hScrollBar, vScrollBar);
		rxPointer.setOpaque(false);
		rxPointer.setVisible(true);
		rxPointer.setName("pointer");
		rxPointer.setPreferredSize(layerSize);
		add(rxPointer, (Integer) (POINTER_LAYER));

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
	
	
	// zoom centerered in viewport (screen type) coordinates
	void zoomCentered(double zRatio, Point zoomCenter) {

		int x = (getVisibleRect().width) / 2;
		int y = (getVisibleRect().height) / 2;
		
		zoom(zRatio, zoomCenter, x, y);
	}
	
	// zoom about a point (layered pane) coordinates
	private void zoomAboutPoint(double zRatio, Point vpPoint) {
		
		int x = vpPoint.x - getVisibleRect().x;
		int y = vpPoint.y - getVisibleRect().y;
		
		zoom(zRatio, vpPoint, x, y);
	}
	
	void zoomWindow() {
		currentDragMode = window;
	}
	
	private void zoom(double zRatio, Point vpPoint, int vpOffsetX, int vpOffsetY) {
		Point drawingCoord = calcLayPtFromScnPt(vpPoint);
		setZoomRatio(zRatio);
		Point zoomedCoord = calcScnPtFromLayPt(drawingCoord);
		
		zoomViews();
		
		positionViewport(zoomedCoord, vpOffsetX, vpOffsetY);
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
	void moveToPoint(Point drawingCoord) {
		
		Point newPoint = calcScnPtFromLayPt(drawingCoord);
		Point existPoint = new Point(
				getVisibleRect().x + getVisibleRect().width/2,
				getVisibleRect().y + getVisibleRect().height/2);
		
		int x = newPoint.x - existPoint.x;
		int y = newPoint.y - existPoint.y;
		
		positionViewport(new Point(getVisibleRect().x + x, getVisibleRect().y + y), 0, 0);
	}

	private void setZoomRatio(double zRatio) {
		
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

		return result.toDimension();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
//		LogMsgFmtln("lay pane: mouse wheel: ", e.getPoint());
		// positive = dn
		// negative = up
		
		if (e.getWheelRotation() < 0)
			zoomAboutPoint(ZOOMWHEELRATIOOUT, e.getPoint());
		else
			zoomAboutPoint(ZOOMWHEELRATIOIN, e.getPoint());

		rxPointer.updateCursor(getMousePosition());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (currentDragMode) {
			case pan:
				Point newPt = subtractPoints(panPriorPt, e.getPoint());
				panPriorPt = addPoints(e.getPoint(), newPt);
				newPt = addPoints(getVisibleRect().getLocation(), newPt);
				positionViewport(newPt, 0, 0);
				break;
			case window:
				winCornerPt = e.getPoint();
				break;
			case line:
				break;
			}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		panPriorPt = e.getPoint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		currentDragMode = pan;
	
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
//		Dimension2dx zoomedSize = Dimension2dx.toDimension2dx(getZoomedViewSize());
//		setPreferredSize(zoomedSize.toDimension());
//		updateSize();
//		revalidate();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		LogMsgFmtln("lay pane: ", "mouse clicked");
		LogMsgFmtln("e point: ", e.getPoint());
		
		Point mousePoint_getXgetY = new Point(e.getX(), e.getY());
		Point mouseLayPoint = calcLayPtFromScnPt(new Point(e.getX(), e.getY()));
		Point mouseScnPoint = calcScnPtFromLayPt(new Point(e.getX(), e.getY()));

		RegexExpress.textAreaCoords.setText(dispVal(mouseLayPoint));

		LogMsgFmtln("comp point: ", mousePoint_getXgetY);
		LogMsgFmtln("lay point: ", mouseLayPoint);
		LogMsgFmtln("scn point: ", mouseScnPoint);
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