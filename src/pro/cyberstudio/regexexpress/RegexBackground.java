package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author jeffs
 *         File:    RxBackground
 *         Created: 4/27/2017 @ 9:50 PM
 *         Project: RegexExpress
 */


// this must be the lowest layer
class RegexBackground extends aRxLayer {
	
	private static Color gridColor = new Color(40, 40, 40);
	private static Color coordColor = new Color(80, 80, 80);
	
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
		
//		for (; pt.x < getMinimumSize().width; pt.x += 100) {
//			for (pt.y = 0; pt.y < getMinimumSize().height; pt.y += 100) {
		for (; pt.x < (testX * 2); pt.x += 100) {
			for (pt.y = 0; pt.y < (testY * 2); pt.y += 100) {
				
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

		Rectangle2D.Double rect = new Rectangle2D.Double(4000, 2000, 100, 100);
		g2.setColor(Color.RED);
		g2.draw(rect);

		// note: rotation is + is CW, - is CCW
		g2.rotate(-Math.PI / 8, testX, testY);
		g2.setColor(Color.CYAN);
		g2.draw(rect);

		g2.drawString("this is a test", testX, testY);

	
	}
}
