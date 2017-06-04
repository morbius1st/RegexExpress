package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

/**
 * @author jeffs
 *         File:    iRxZoomPanel
 *         Created: 4/27/2017 @ 9:44 PM
 *         Project: RegexExpress
 */

abstract class aRxLayer extends JPanel {
	
	static int testX = 4000;
	static int testY = 2000;
	
	Graphics2D g2;
	
	static double zoomFactor;
	
	static double tx;
	static double ty;
	
	static double theta;
	static double anchorx;
	static double anchory;
	
	
	void setZoomScale(double zoomFactor) { this.zoomFactor = zoomFactor; }
	void setTranslate(double tx, double ty) {this.tx = tx; this.ty = ty; }
	void setRotate(double theta, double anchorx, double anchory) {
		this.theta = theta;
		this.anchorx = anchorx;
		this.anchory = anchory;
	}
	
	@Override
	public Graphics2D getGraphics() {
		return (Graphics2D) super.getGraphics();
	}
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.scale(zoomFactor, zoomFactor);
		if (tx != 0 && ty != 0)
			g2.translate(tx, ty);
		
		if (theta != 0)
			g2.rotate(theta, anchorx, anchory);
		
		this.g2 = g2;
		
		super.paint(g);
	}
	
}
