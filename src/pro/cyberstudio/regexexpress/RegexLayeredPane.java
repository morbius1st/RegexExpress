package pro.cyberstudio.regexexpress;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

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

class RegexLayeredPane extends JLayeredPane implements iCompListener, iMouseListener {
	
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
	
	
	// private constructor to prevent creating
	// an instance of this class
	private RegexLayeredPane() {
		// assign minimal sizes to the view size parameters
		viewSizeParams.minSize = new Dimension2dx(100, 100);
		viewSizeParams.viewSize = new Dimension2dx(100, 100);
		viewSizeParams.layerSize = new Dimension2dx(100, 100);
		setName("RegexLayeredPane");
		aft.setToScale(zoomFactor, zoomFactor);
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
		
		RegexScroll.addMWL(rxPointer);

		rxBackground.setBackground(Color.BLACK);
		rxBackground.setOpaque(true);
		rxBackground.setVisible(true);
		rxBackground.setName("background");
		rxBackground.setSize(RegexExpress.LAYERX, RegexExpress.LAYERY);
		add(rxBackground, (Integer) (-1));
		
		
		rxPointer.addMouseListener(RegexExpress.regexScroll);
		
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
	
	private Point  calcZoomedPoint(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aft.inverseTransform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point  calcZoomedPoint(int x, int y) {
		return calcZoomedPoint(new Point(x, y));
	}
	
	private Point  calcInvZoomedPoint(Point ptSrc) {
		Point ptDest = new Point();
		
		try {
			aft.transform(ptSrc, ptDest);
		} catch (Exception e) {}
		
		return ptDest;
	}
	
	private Point  calcInvZoomedPoint(int x, int y) {
		return calcInvZoomedPoint(new Point(x, y));
	}
	
	// center is in viewport (screen type) coordinates
	void zoomTo(double zRatio, Point zoomCenter) {
//
//		LogMsgln("zoom ratio      : " + zRatio);
//
//		LogMsgln("lay pane size  1: " + dispVal(this.getSize()));
//
//		LogMsgln("zoomed view sz 1: " + dispVal(getZoomedViewSize()));
//
//		LogMsgln("vis rect        : " + dispVal(getVisibleRect().width, getVisibleRect().height));
//		LogMsgln("center          : " + dispVal(zoomCenter));
//
		int x = (getVisibleRect().width) / 2;
		int y = (getVisibleRect().height) / 2;
		
		Point drawingCoord = calcZoomedPoint(zoomCenter);
//
//		LogMsgln("dwg coord before: " + dispVal(drawingCoord));
		
		setZoomRatio(zRatio);
//
//		LogMsgln("lay pane size  2: " + dispVal(this.getSize()));
		
		Dimension viewSize = getZoomedViewSize();
//
//		LogMsgln("zoomed view sz 2: " + dispVal(viewSize));
		
		Point zoomedCoord = calcInvZoomedPoint(drawingCoord);
//
//		LogMsgln("dwg coord after : " + dispVal(zoomedCoord));
		
		Point zoomedOffset = new Point(x, y);
//		zoomedOffset = calcInvZoomedPoint(zoomedOffset);
//		zoomedOffset = calcZoomedPoint(zoomedOffset);
//
//		LogMsgln("offset          : " + dispVal(x, y));
//		LogMsgln("zoom offset     : " + dispVal(zoomedOffset));
		
		Point vpCorner = new Point(zoomedCoord.x - zoomedOffset.x,
				zoomedCoord.y - zoomedOffset.y);
//
//		LogMsgln("vp corner       : " + dispVal(vpCorner));
		
		Point test = new Point(554, 425);
		
		zoom();
		
		JViewport vPort = (JViewport) getParent();
		vPort.setViewSize(viewSize);
//
//		LogMsgln("vp view size    : " + dispVal(vPort.getViewSize()));
//		LogMsgln("vp ext size     : " + dispVal(vPort.getExtentSize()));
//		LogMsgln("vp size         : " + dispVal(vPort.getSize()));
//
//		if (zRatio == 2.0) {
//			LogMsgln("zoom 2.0");
//			vPort.setViewPosition(new Point( 400, 400));
//		} else {
//			LogMsgln("zoom 1.0");
			vPort.setViewPosition(vpCorner);
//		}
	
	}
	
	void setZoomRatio(double zRatio) {
		zoomRatio = zRatio;
		zoomFactor *= zoomRatio;
		
		aft.setToScale(zoomFactor, zoomFactor);
//
//		LogMsgln("aft scale       : " + dispVal(aft.getScaleX(), aft.getScaleY()));
	}
	
	private void zoom() {
		
		Dimension viewSize = getZoomedViewSize();

//		// set this (Layered Pane's) size
		setPreferredSize(viewSize);
		revalidate();

//		LogMsgln("vis rect: " + dispVal(getVisibleRect()));
		
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
	
	
	
	
	void zoomToScale(double zRatio) {
		zoomRatio = zRatio;
		zoomFactor *= zRatio;
		
		aft.setToScale(zoomFactor, zoomFactor);
		
		Dimension viewSize = getZoomedViewSize();
		
//		// set this (Layered Pane's) size
		setPreferredSize(viewSize);
		revalidate();
		
//		LogMsgln("vis rect: " + dispVal(getVisibleRect()));
		
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
	

	@Override
	public void componentResized(ComponentEvent e) {
//		Dimension2dx viewportSize = Dimension2dx.toDimension2dx(getParent().getSize());
		Dimension2dx zoomedSize = Dimension2dx.toDimension2dx(getZoomedViewSize());
		
//		LogMsgln("resizing component");

//		if (viewportSize.eitherGreaterThanOrEqual(zoomedSize)) {
//			LogMsgln("processing resized");
			setPreferredSize(zoomedSize.toDimension());
			updateSize();
			revalidate();
//		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point mousePointScaleAdjusted;

//		Point2D.Double mousePointComponent;
//		mousePointComponent = new Point2D.Double(e.getX(), e.getY());
//		mousePointScaled = calcZoomedPoint(mousePointComponent);
		Point mousePointUnscaled;
		mousePointUnscaled = new Point(e.getX(), e.getY());
		mousePointScaleAdjusted = calcZoomedPoint(new Point(e.getX(), e.getY()));

		RegexExpress.addCoordText(mousePointScaleAdjusted);

		LogMsgln("  comp point: " + dispVal(mousePointUnscaled));
		LogMsgln("scaled point: " + dispVal(mousePointScaleAdjusted));
	}
	
	void listViewSizes() {
		Component[] comps = getComponentsInLayer(POINTER_LAYER);
		
		LogMsgln("View Size Listing");
		LogMsgln("Lay Pane");
		LogMsg(viewSizes(this, viewSizeMask.perf.value + viewSizeMask.size.value));
		LogMsgln("canvasSize: " + Utility.dispVal(viewSizeParams.viewSize));

//		LogMsgln("Pointer Layer");
//		LogMsgln(viewSizes(comps[0]));
//		LogMsgln("");
//		LogMsgln("View Port");
//		LogMsgln(viewSizes(getParent()));
	
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