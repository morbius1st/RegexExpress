package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.JPanel;

import static pro.cyberstudio.regexexpress.RegexExpress.*;
import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    RxBackground
 *         Created: 4/27/2017 @ 9:50 PM
 *         Project: RegexExpress
 */

class RegexZero extends JPanel implements iRxLayer {
	
	private double zoomFactor = 1.0;
	private static AffineTransform afInv = new AffineTransform();
	private static AffineTransform aff = new AffineTransform();
	
	@Override
	public void setZoomFactor(double zoomFactor) {
		LogMsgln("@zero: zoom factor: " + zoomFactor);
		LogMsgln("@zero:    zoom inv:" + displayXY(afInv.getScaleX(), afInv.getScaleY()));
		LogMsgln("@zero:        zoom:" + displayXY(aff.getScaleX(), aff.getScaleY()));
		
		this.zoomFactor = zoomFactor;
	}
	
	static Point2D.Double calcZoomedPoint(Point2D.Double ptSrc) {
		Point2D.Double ptDest = new Point2D.Double();
//		afInv.transform(ptSrc, ptDest);
		try {
			aff.inverseTransform(ptSrc, ptDest);
		} catch (Exception e) {
		
		}

		return ptDest;
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.scale(zoomFactor, zoomFactor);

		aff=g2.getTransform();
		
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
