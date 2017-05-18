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
	
	@Override
	public void setZoomScale(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
}
