package pro.cyberstudio.regexexpress;
import java.awt.*;
import java.awt.event.ComponentEvent;
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
	Dimension2dx size;
}

class RegexLayeredPane extends JLayeredPane implements iCompListener {
	
	static final int POINTER_LAYER = JLayeredPane.PALETTE_LAYER - 1;
	
	TreeMap<String, RegexLayer> layerTable = new TreeMap<>();
	
	// hold the actual instance
	private static RegexLayeredPane me;
	private static RegexBackground rxBackground;
	private static RegexPointer rxPointer;
	private static RegexZero rxZero;
	
	private static viewSizeData viewSizeParams = new viewSizeData();
	
	double zoomFactor = 1.0;
	double zoomRatio = 1.0;
	
	int x = 0;
	
	
	// private constructor to prevent creating
	// an instance of this class
	private RegexLayeredPane() {
		// assign minimal sizes to the view size parameters
		viewSizeParams.minSize = new Dimension2dx(100, 100);
		viewSizeParams.size = new Dimension2dx(100, 100);
		setName("RegexLayeredPane");
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
		viewSizeParams.size = initSize.clone();
		
		rxZero.setOpaque(false);
		rxZero.setVisible(true);
		rxZero.setName("zero");
		add(rxZero, JLayeredPane.DEFAULT_LAYER);
		
		rxPointer.assignScrollBars(hScrollBar, vScrollBar);
		rxPointer.setOpaque(false);
		rxPointer.setVisible(true);
		rxPointer.setName("pointer");
		add(rxPointer, (Integer) (POINTER_LAYER));
		RegexScroll.addMWL(rxPointer);

		rxBackground.setBackground(Color.BLACK);
		rxBackground.setOpaque(true);
		rxBackground.setVisible(true);
		rxBackground.setName("background");
		add(rxBackground, (Integer) (-1));
		
		
		
		updateSize();
	}

	Component add(String layerName) {
		RegexLayer layer = new RegexLayer();
		
		layer.setName(layerName);
		layer.setSize(getPreferredSize());
		layer.setOpaque(false);
		layer.setVisible(true);
		layer.setZoomFactor(zoomFactor);
		
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
	
//	static Point2D.Double calcZoomedPoint(Point2D.Double ptSrc) {
//		Point2D.Double ptDest = new Point2D.Double();
//		afInv.transform(ptSrc, ptDest);
//
//		return ptDest;
//	}
	
	void zoomView(double zRatio) {
		zoomRatio = zRatio;
		
		zoomFactor *= zoomRatio;
		
		viewSizeParams.size = viewSizeParams.size.multiply(zoomRatio);
		
		Dimension viewSize = getZoomedViewSize();
		
//		// set this (Layered Pane's) size
		setPreferredSize(viewSize);
		revalidate();

		
		for (int i = lowestLayer(); i <= highestLayer(); i++) {
			Component[] comps = getComponentsInLayer(i);
			if (comps.length > 0) {
				for (Component c : comps) {
					c.setSize(viewSize);
					((iRxLayer) c).setZoomFactor(zoomFactor);
					c.revalidate();
					c.repaint();
				}
			}
		}
	}
	
	// restrict the zoomed view size to be no smaller than the
	// layered pane minimum size
	private Dimension getZoomedViewSize() {
	
//		Dimension2dx result = new Dimension2dx();
//		Dimension2dx proposed = viewSizeParams.size.clone();
//		Dimension2dx viewport = Dimension2dx.toDimension2dx(getParent().getSize());
//
//		if (proposed.width >= getPreferredSize().width ||
//				proposed.width >= viewSizeParams.minSize.width) {
//			result.width = proposed.width;
//		} else {
//			result.width = getPreferredSize().width;
//		}
//
//		if (proposed.height >= getPreferredSize().height ||
//				proposed.height >= viewSizeParams.minSize.height) {
//			result.height = proposed.height;
//		} else {
//			result.height = getPreferredSize().height;
//		}
//
//		if (viewport.width > result.width) {
//			result.width = viewport.width;
//		}
//		if (viewport.height > result.height) {
//			result.height = viewport.height;
//		}
		
		Dimension2dx result = new Dimension2dx();
		Dimension2dx proposed = viewSizeParams.size.clone();
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
//		LogMsgln("");
//		LogMsgln("proposed size: " + displayDim(proposed));
//		LogMsgln("viewport size: " + displayDim(viewport));
//		LogMsgln("  result size: " + displayDim(result));

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
	
	
	
	void listViewSizes() {
		Component[] comps = getComponentsInLayer(POINTER_LAYER);
		
		LogMsgln("View Size Listing");
		LogMsgln("Lay Pane");
		LogMsg(viewSizes(this, viewSizeMask.perf.value + viewSizeMask.size.value));
		LogMsgln("canvasSize: " + displayDim(viewSizeParams.size));

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
					sb.append("\n\t     size: " + displayDim(comps[j].getSize()));
					sb.append("\n\tperf size: " + displayDim(comps[j].getPreferredSize()));
					sb.append("\n\t min size: " + displayDim(comps[j].getMinimumSize()));
					sb.append("\n\t max size: " + displayDim(comps[j].getMaximumSize()));
					sb.append("\n");
				}
			}
		}
		
		return sb.toString();
	}
}