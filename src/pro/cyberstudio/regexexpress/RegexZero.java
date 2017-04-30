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

class RegexZero extends JPanel implements iRxLayer {
	
	private double zoomFactor = 1.0;
	private static AffineTransform afInv = new AffineTransform();
	
	@Override
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
	static Point2D.Double calcZoomedPoint(Point2D.Double ptSrc) {
		Point2D.Double ptDest = new Point2D.Double();
		afInv.transform(ptSrc, ptDest);

		return ptDest;
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.scale(zoomFactor, zoomFactor);

		try {
			afInv = g2.getTransform().createInverse();
		} catch (Exception e) {
			System.exit(-2);
		}

		super.paint(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.CYAN);
		g2.drawRect(20,20, CANVASX - 40, CANVASY - 40);
	}

}
