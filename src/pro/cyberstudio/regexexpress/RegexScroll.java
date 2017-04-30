package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javax.swing.*;
import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    RegexScroll
 *         Created: 4/22/2017 @ 3:42 PM
 *         Project: RegexExpress
 */

class RegexScroll extends JScrollPane implements MouseWheelListener, ComponentListener {
	
	private static ArrayList<iMWListener> mwlChain = new ArrayList<>(5);
	private static ArrayList<iCompListener> clChain = new ArrayList<>(5);

	public RegexScroll(Component component, int i, int i1) {
		super(component, i, i1);
		initalize();
	}

	public RegexScroll(Component component) {
		super(component);
		initalize();
	}

	public RegexScroll(int i, int i1) {
		super(i, i1);
		initalize();
	}

	public RegexScroll() {
		initalize();
	}
	
	private void initalize() {
		
		addMouseWheelListener(this);
		addComponentListener(this);
	}
	
	static void addMWL(iMWListener mwl) {
		if (mwl != null && !mwlChain.contains(mwl))
			mwlChain.add(mwl);
	}
	
	static void addCL(iCompListener cl) {
		
		if (cl != null && !clChain.contains(cl)) {
			clChain.add(cl);
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
//		Point2D.Double ptSrc = new Point2D.Double(e.getX(), e.getY());
//		Point2D.Double ptDest = RegexBackground.calcZoomedPoint(ptSrc);
//		LogMsgln("un-converted point: " + displayPt(ptSrc));
//		LogMsgln("   converted point: " + displayPt(ptDest));
		
		for (iMWListener mwl : mwlChain) {
			mwl.mouseWheelMoved(e);
		}
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		for (iCompListener cl : clChain) {
			cl.componentResized(e);
		}
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
	
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
	
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
	
	}
}
