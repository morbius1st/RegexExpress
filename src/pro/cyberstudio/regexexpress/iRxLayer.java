package pro.cyberstudio.regexexpress;

import java.awt.*;

import javax.swing.JPanel;

/**
 * @author jeffs
 *         File:    iRxZoomPanel
 *         Created: 4/27/2017 @ 9:44 PM
 *         Project: RegexExpress
 */

abstract class iRxLayer extends JPanel {
	abstract void setZoomScale(double zoomFactor);
	
	@Override
	public Graphics2D getGraphics() {
		return (Graphics2D) super.getGraphics();
	}
	
}
