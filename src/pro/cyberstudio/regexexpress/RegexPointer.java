package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import pro.cyberstudio.regexexpress.RegexLayeredPane.DragModes;

import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.regexexpress.RegexLayeredPane.DragModes.*;

/**
 * @author jeffs
 *         File:    RegexAnalysis
 *         Created: 4/15/2017 @ 10:21 PM
 *         Project: RegexExpress
 */

class RegexPointer extends JPanel implements Scrollable, iMMListener { //}, MouseMotionListener { //}, iMWListener {
	
	private static int maxUnitIncrement = 1;
	
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
	public void paint(Graphics g) { super.paint(g); }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
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
				
				int x = anchorPoint.x;
				int y = anchorPoint.y;
				int w = cursorPoint.x - anchorPoint.x;
				int h = cursorPoint.y - anchorPoint.y;
				
				if (w < 0) {
					x = cursorPoint.x;
					w *= -1;
				}
				if (h < 0) {
					y = cursorPoint.y;
					h *= -1;
				}
				
				g.drawRect(x, y, w, h);
				
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
	
	void test() {
		LogMsgFmtln("this is ", "a test");
	}

	// required for mouse motion
	@Override
	public void mouseDragged(MouseEvent e) {
		updateCursor(e.getPoint());
	}

	public void mouseMoved(MouseEvent e) {
		updateCursor(e.getPoint());
	}
	
	void updateCursor(Point pt) {
		cursorPoint = pt;
		repaint();
		revalidate();
	}


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
