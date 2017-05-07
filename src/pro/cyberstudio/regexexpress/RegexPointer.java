package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    RegexAnalysis
 *         Created: 4/15/2017 @ 10:21 PM
 *         Project: RegexExpress
 */

class RegexPointer extends JPanel implements Scrollable, iRxLayer, iMMListener {//MouseMotionListener { //}, iMWListener {
	
	
	private int maxUnitIncrement = 1;
	
	private JScrollBar vScrollBar;
	private JScrollBar hScrollBar;
	
	private Point cursorPoint = new Point();
	
	RegexPointer() {
 		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentX(CENTER_ALIGNMENT);
		setAutoscrolls(true);
//		addMouseMotionListener(this);
		
		RegexScroll.addMMovL(this);
		RegexScroll.addMDragL(this);
	}
	
	public void assignScrollBars(JScrollBar hBar, JScrollBar vBar) {
		hScrollBar = hBar;
		vScrollBar = vBar;
//
	
	}

	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(Color.BLUE);
		// draw cursor lines
		g.drawLine(0, cursorPoint.y, this.getWidth(), cursorPoint.y);
		g.drawLine(cursorPoint.x, 0, cursorPoint.x, this.getHeight());
		
//		LogMsgln("cursor: " + dispVal(cursorPoint));
	}
	
	@Override
	public void setZoomScale(double zoomFactor) { }
	
	void test() {

	}

	// required for mouse motion
	@Override
	public void mouseDragged(MouseEvent e) { }

//	@Override
	public void mouseMoved(MouseEvent e) {
//		LogMsgFmtln("mouse moved point: ", e.getPoint());

		updateCursor(e.getPoint());
	}
	
	void updateCursor(Point pt) {
		cursorPoint = pt;
		repaint();
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
