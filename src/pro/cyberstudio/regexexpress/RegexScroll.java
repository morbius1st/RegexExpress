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

class RegexScroll extends JScrollPane implements MouseWheelListener, MouseListener, ComponentListener {
	
	private static ArrayList<iMWListener> mwlChain = new ArrayList<>(5);
	private static ArrayList<iCompListener> clChain = new ArrayList<>(5);
	
	static Point2D.Double mousePointComponent;
	static Point2D.Double mousePointScaled;
	

	public RegexScroll(Component component, int i, int i1) {
		super(component, i, i1);
		initialize();
	}

	public RegexScroll(Component component) {
		super(component);
		initialize();
	}

	public RegexScroll(int i, int i1) {
		super(i, i1);
		initialize();
	}

	public RegexScroll() {
		initialize();
	}
	
	private void initialize() {
		
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
	public void componentResized(ComponentEvent e) {
		for (iCompListener cl : clChain) {
			cl.componentResized(e);
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		for (iMWListener mwl : mwlChain) {
			mwl.mouseWheelMoved(e);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		mousePointComponent = new Point2D.Double(e.getX(), e.getY());
		mousePointScaled = RegexZero.calcZoomedPoint(mousePointComponent);
		
		LogMsgln("  comp point: " + displayPt(mousePointComponent));
		LogMsgln("scaled point: " + displayPt(mousePointScaled));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
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
