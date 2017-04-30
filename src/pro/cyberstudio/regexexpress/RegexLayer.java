package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.RegexExpress.*;
import static pro.cyberstudio.regexexpress.Utility.LogMsgln;
import static pro.cyberstudio.regexexpress.Utility.displayXY;

/**
 * @author jeffs
 *         File:    RegexAnalysis
 *         Created: 4/15/2017 @ 10:21 PM
 *         Project: RegexExpress
 */



class RegexLayer extends JPanel implements Scrollable, iRxLayer {
	
	static int idx = 0;
	int position;
	int offset = 0;
	
	static Color[] colors = {Color.RED, Color.GREEN,
		Color.BLUE, Color.CYAN, Color.MAGENTA};
	
	private int maxUnitIncrement = 1;
	
	private double zoomFactor = 1.0;
	
//	AffineTransform afInv = new AffineTransform();
	
	
	RegexLayer(String name) {
		super();
		setName(name);
		position = idx++;
		offset = position * 10;
		setAutoscrolls(true);
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentX(CENTER_ALIGNMENT);
		
		int x = IMAGEPOSX + offset;
		int y = IMAGEPOSY + offset;
		
		LogMsgln("start point: " + displayXY(x, y));
		
	}
	
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	@Override
	public void paint(Graphics g) {
	
		Graphics2D g2 = (Graphics2D) g;

		g2.scale(zoomFactor, zoomFactor);
//
//		try {
//			afInv = g2.getTransform().createInverse();
//		} catch (Exception e) {
//			System.exit(-2);
//		}

		super.paint(g);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		int x = IMAGEPOSX + offset;
		int y = IMAGEPOSY + offset;

		g2.setColor(Color.BLUE);
		g2.drawString("this is a scroll viewport", x + 5, y + 20);
		g2.setColor(colors[position]);
		g2.drawRect( x, y , 135, 30);
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
