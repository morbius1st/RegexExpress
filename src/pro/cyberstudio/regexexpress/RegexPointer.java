package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.regexexpress.Utility.DragModes.*;

/**
 * @author jeffs
 *         File:    RegexAnalysis
 *         Created: 4/15/2017 @ 10:21 PM
 *         Project: RegexExpress
 */

class RegexPointer extends JPanel implements Scrollable, iRxLayer, iMMListener { //}, MouseMotionListener { //}, iMWListener {
	
	private static int maxUnitIncrement = 1;
	
//	private static JScrollBar vScrollBar;
//	private static JScrollBar hScrollBar;
	
	private static Point cursorPoint = new Point();
	private static Point anchorPoint = new Point();
	
	private static DragModes dragMode = XHAIRS;
	
	private static Color XHAIRCOLOR = Color.BLUE;
	private static Color WINBOXCOLOR = Color.GREEN;
	private static Color SELECTIONBOXCOLOR = Color.CYAN;
	
	
	RegexPointer() {
 		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentX(CENTER_ALIGNMENT);
		setAutoscrolls(true);
		
		RegexScroll.addMMovL(this);
		RegexScroll.addMDragL(this);
	}
	
	public void assignScrollBars(JScrollBar hBar, JScrollBar vBar) {
//		hScrollBar = hBar;
//		vScrollBar = vBar;
//
	
	}

	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	}
	
	void setPointerModeWindow() {
		dragMode = WINDOW;
	}
	
	void setPointerModeXhairs() {
		dragMode = XHAIRS;
	}
	
	void setWindowAnchorPoint(Point pt) {
		anchorPoint = pt;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		switch (dragMode) {
			case XHAIRS:
				if (cursorPoint == null) {
					return;
				}
				drawCursor(g);
				break;
			case WINDOW:
				drawCursor(g);
				// draw a rectangle
				// fixed corner = anchorPoint
				g.setColor(WINBOXCOLOR);
				
				g.drawRect(anchorPoint.x, anchorPoint.y,
						cursorPoint.x - anchorPoint.x,
						cursorPoint.y - anchorPoint.y);
				
				break;
		}
		
	}
	
	void drawCursor(Graphics g) {
		if (cursorPoint == null) {
			return;
		}
		
		g.setColor(XHAIRCOLOR);
		// draw cursor lines
		g.drawLine(0, cursorPoint.y, this.getWidth(), cursorPoint.y);
		g.drawLine(cursorPoint.x, 0, cursorPoint.x, this.getHeight());
	}
	
	@Override
	public void setZoomScale(double zoomFactor) { }
	
	void test() {
		LogMsgFmtln("this is ", "a test");
	}

	// required for mouse motion
	@Override
	public void mouseDragged(MouseEvent e) {
		
		updateCursor(e.getPoint());
	}

//	@Override
	public void mouseMoved(MouseEvent e) {
		updateCursor(e.getPoint());
	}
	
	void updateCursor(Point pt) {
//		LogMsgFmtln("mouse moved point: ", pt);
		cursorPoint = pt;
		repaint();
		revalidate();
	}



//	@Override
//	public void mouseWheelMoved(MouseWheelEvent e) {
//		int adjustment = 5;
//		int value = 0;
//		int min = 0;
//		int max = 0;
//		boolean orientation = false;  // true == vertical, false == horizontal
//		JScrollBar sBar = hScrollBar;
//
//		if (e.getWheelRotation() > 0 ) {
//			adjustment *= -1;
//		}
//
//		if (vScrollBar != null && hScrollBar != null) {
//
//			if (!e.isShiftDown())  {
//				orientation = true;
//				sBar = vScrollBar;
//			}
//
//			value = sBar.getValue();
//			min = sBar.getMinimum();
//			max = Math.max(sBar.getMaximum() - sBar.getVisibleAmount(), 0);
//
//			if ((adjustment < 0 && ((value > min )
//					|| (value == min && adjustment > 0))) ||
//					(adjustment > 0 && ((value < max )
//							|| (value == max && adjustment < 0)))
//					)
//				{
//				sBar.setValue(value + adjustment);
//
//				if (orientation) {
//					cursorPoint.y += adjustment;
//				} else {
//					cursorPoint.x += adjustment;
//				}
//				repaint();
//			}
//		}
//	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRectangle, int orientation, int direction) {
		return maxUnitIncrement;
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle rectangle, int i, int i1) {
		return maxUnitIncrement;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	
}
