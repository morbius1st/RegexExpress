package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.JPanel;

import static pro.cyberstudio.regexexpress.RegexExpress.*;

/**
 * @author jeffs
 *         File:    RxBackground
 *         Created: 4/27/2017 @ 9:50 PM
 *         Project: RegexExpress
 */

class RegexBackground extends JPanel implements iRxLayer {
	
	private double zoomFactor = 1.0;
	private static AffineTransform afInv = new AffineTransform();
	
	private static Color gridColor = new Color(40, 40, 40);
	private static Color coordColor = new Color(80, 80, 80);
	
	@Override
	public void setZoomScale(double zoomFactor) {
		this.zoomFactor = zoomFactor;
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
		
		int lineLength = 30;
		int lineLenHalf = lineLength/2;
		int textOffsetHoriz = 10;
		int textOffsetVert = 15;
		
		Font font = new Font("Verdana", Font.PLAIN, 10);
		
		Point pt = new Point(0, 0);
		
		
		g2.setFont(font);
		
		for (; pt.x < getMinimumSize().width; pt.x += 100) {
			for (pt.y = 0; pt.y < getMinimumSize().height; pt.y += 100) {
				
				g2.setColor(gridColor);
				// draw the horizontal ine
				g2.drawLine(pt.x - lineLenHalf, pt.y, pt.x + lineLenHalf, pt.y);
				
				// draw the vertical line
				g2.drawLine(pt.x, pt.y - lineLenHalf, pt.x, pt.y + lineLenHalf);
				
				g2.setColor(coordColor);
				g2.drawString("x: " + pt.x, pt.x + textOffsetHoriz, pt.y + textOffsetVert);
				g2.drawString("y: " + pt.y, pt.x + textOffsetHoriz, pt.y + textOffsetVert + 10);
			}
		}
	}
}
