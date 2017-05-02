package pro.cyberstudio.regexexpress;

import java.awt.*;
import javax.swing.*;
import static pro.cyberstudio.regexexpress.RegexExpress.*;

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
	
	
	RegexLayer() {
		super();
		position = idx++;
		offset = position * 10;
		setAutoscrolls(true);
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void setZoomScale(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	@Override
	public void paint(Graphics g) {
	
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoomFactor, zoomFactor);
		super.paint(g);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		int x = IMAGEPOSX + offset;
		int y = IMAGEPOSY + offset;

		g2.setColor(Color.BLUE);
		g2.drawString("this is a scroll layer: " + getName(), x + 5, y + 20);
		g2.setColor(colors[position]);
		g2.drawRect( x, y , 160, 30);
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
