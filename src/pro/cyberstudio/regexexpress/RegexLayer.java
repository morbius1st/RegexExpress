package pro.cyberstudio.regexexpress;

import java.awt.*;

import javax.swing.*;

import pro.cyberstudio.displaylist.*;

import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    RegexLayer
 *         Created: 4/15/2017 @ 10:21 PM
 *         Project: RegexExpress
 */


class RegexLayer extends aRxLayer implements Scrollable {
	
	private DisplayListManager dlm;
	
	private static int idx = 1;
	private int layerIndex;
	private int offsetY = 0;
	private int offsetX = 10;
	
	static private Color[] colors = {Color.LIGHT_GRAY,
			Color.RED,
			Color.MAGENTA,
			Color.GREEN,
			Color.BLUE
			};
	
	private int maxUnitIncrement = 1;
	
	private int layerID;
	
	
	RegexLayer(String name, int layerID) {
		super();
		setName(name);
		this.layerID = layerID;
		layerIndex = idx++;
		offsetY = 6 + (layerIndex) * -12;
		
		dlm = new DisplayListManager(50);
		
	}

	int getLayerID() {
		return layerID;
	}
	
	boolean addGraphElem(GraphElement ge) {
		return dlm.add(ge);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		dlm.draw(g2);
		
//		int colorIdx = (layerIndex - 1) % colors.length;
//		g2.setColor(colors[colorIdx]);
//
//		Font font;
//
//		int x = testX + 2 + offsetX;
//		int y = testY + 50 + offsetY;
//
//		font = new Font("Arial", Font.BOLD + Font.ITALIC, 12);
//
//		g2.setFont(font);
//		g2.drawString("layer: " + getName(), x, y);
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
