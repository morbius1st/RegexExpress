package pro.cyberstudio.regexexpress;

import java.awt.*;

import javax.swing.JPanel;

/**
 * @author jeffs
 *         File:    RxZero
 *         Created: 4/27/2017 @ 9:50 PM
 *         Project: RegexExpress
 */

class RegexZero extends iRxLayer {
	
	private double zoomFactor = 1.0;
	
	// set the absolute zoom amount
	public void setZoomScale(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoomFactor, zoomFactor);
		super.paint(g);
	}
	
}
