package pro.cyberstudio.regexexpress;

import java.awt.*;
import javax.swing.*;

import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    RegexAnalysis
 *         Created: 4/15/2017 @ 10:21 PM
 *         Project: RegexExpress
 */



class RegexLayer extends JPanel implements Scrollable, iRxLayer {
	
	private static int idx = 1;
	private int layerIndex;
	private int offset = 0;
	
	static private Color[] colors = {Color.LIGHT_GRAY,
			Color.RED,
			Color.MAGENTA,
			Color.GREEN,
			Color.BLUE
			};
	
	private int maxUnitIncrement = 1;
	
	private double zoomFactor = 1.0;
	
	
	RegexLayer() {
		super();
		layerIndex = idx++;
		offset = (layerIndex - 1) * 10;
//		setAutoscrolls(true);
//		setAlignmentX(CENTER_ALIGNMENT);
//		setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void setZoomScale(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
//	public Dimension getPreferredSize() {
//		return super.getPreferredSize();
//	}

	@Override
	public void paint(Graphics g) {
	
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoomFactor, zoomFactor);
		super.paint(g);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int lineLength = 30;
		int lineLenHalf = lineLength/2;
		int textOffsetHoriz = 10;
		int textOffsetVert = 15 + (7 * (layerIndex-1));
		
		Font font = new Font("Verdana", Font.PLAIN, 10);
		
		Point pt = new Point(0, 0);

		Graphics2D g2 = (Graphics2D) g;
		
		if (layerIndex == 1) {
			
			
			g2.setColor(colors[layerIndex-1]);
			g2.setFont(font);
			
			for (; pt.x < getMinimumSize().width; pt.x += 100) {
				for (; pt.y < getMinimumSize().height; pt.y += 100) {
					
					// draw the horizontal ine
					g2.drawLine(pt.x - lineLenHalf, pt.y, pt.x + lineLenHalf, pt.y);
					
					// draw the vertical line
					g2.drawLine(pt.x, pt.y - lineLenHalf, pt.x, pt.y + lineLenHalf);
					
					
					g2.drawString("x: " + pt.x, pt.x + textOffsetHoriz, pt.y + textOffsetVert);
					g2.drawString("y: " + pt.y, pt.x + textOffsetHoriz, pt.y + textOffsetVert + 10);
				}
				pt.y = 0;
			}
		}

//		int x = IMAGEPOSX + offset;
//		int y = IMAGEPOSY + offset;
//

//
//		g2.setColor(Color.BLUE);
//		g2.drawString("this is a scroll layer: " + getName(), x + 5, y + 20);
//		g2.setColor(colors[layerIndex-1]);
//		g2.drawRect( x, y , 160, 30);
//
//
//		for (int i = 0; i < CROSSORIGINPTS.length; i++) {
//			// line color
//			g2.setColor(colors[layerIndex-1]);
//
//			Point pt = new Point(CROSSORIGINPTS[i].x + offset, CROSSORIGINPTS[i].y + offset);
//
//			g2.drawLine(pt.x - lineLenHalf, pt.y, pt.x + lineLenHalf, pt.y);
//			g2.drawLine(pt.x, pt.y - lineLenHalf, pt.x, pt.y + lineLenHalf);
//
//			g2.setColor(colors[layerIndex-1]);
//			g2.drawString(dispVal(pt), pt.x + textOffset, pt.y - textOffset);
//		}
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
	
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
	
	@Override
	public String toString() {
		if (layerIndex == 1) {
			return "layer index| " + layerIndex
					+ "  **size| " + dispVal(getSize())
					+ "  **min| " + dispVal(getMinimumSize())
					+ "  **max| " + dispVal(getMaximumSize())
					+ "  **pref scrol vp| " + dispVal(getPreferredScrollableViewportSize())
					+ "  **pref| " + dispVal(getPreferredSize());
		}
		return "layer index| " + layerIndex;
	}
}
