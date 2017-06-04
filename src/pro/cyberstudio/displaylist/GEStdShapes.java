package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 * @author jeffs
 *         File:    GEStdShapes
 *         Created: 6/4/2017 @ 2:50 PM
 *         Project: RegexExpress
 */

class GEStdShapes {
	
	static void drawLocation(Graphics2D g2, Color color, Point pt) {
		int l = 5;
		int tx = 5;
		int ty1 = 7;
		int ty2 = 7;

		Color save_color = g2.getColor();
		Stroke save_stroke = g2.getStroke();
//		Font save_font = g2.getFont();
		
		BasicStroke s = new BasicStroke(0.25f);
//		Font f = new Font("Sans Serif", Font.PLAIN, 7);
		
		AttributedString ax = new AttributedString("x=" + pt.x);
		ax.addAttribute(TextAttribute.FONT, new Font("Sans Serif", Font.PLAIN, 7));
		ax.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);

		AttributedString ay = new AttributedString("y=" + pt.y);
		ay.addAttribute(TextAttribute.FONT, new Font("Sans Serif", Font.PLAIN, 7));
		ay.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);
		
		g2.setColor(color);
		g2.setStroke(s);
//		g2.setFont(f);
		
		g2.drawLine(pt.x - l, pt.y, pt.x - 2, pt.y);
		g2.drawLine(pt.x + 2, pt.y, pt.x + l, pt.y);
		g2.drawLine(pt.x, pt.y - l, pt.x, pt.y - 2);
		g2.drawLine(pt.x, pt.y + 2, pt.x, pt.y + l);
		g2.drawLine(pt.x, pt.y, pt.x, pt.y);
		g2.drawString(ax.getIterator(), pt.x + tx, pt.y + ty1);
		g2.drawString(ay.getIterator(), pt.x + tx, pt.y + ty1 + ty2);
		
//		g2.setFont(save_font);
		g2.setStroke(save_stroke);
		g2.setColor(save_color);
		
	}
	
}
