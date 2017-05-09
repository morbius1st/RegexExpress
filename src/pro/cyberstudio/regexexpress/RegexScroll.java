package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    RegexScroll
 *         Created: 4/22/2017 @ 3:42 PM
 *         Project: RegexExpress
 */

class RegexScroll extends JScrollPane implements MouseWheelListener, MouseListener, ComponentListener, MouseMotionListener {
	
	private static ArrayList<iMWListener> mwlChain = new ArrayList<>(5);
	private static ArrayList<iCompListener> clChain = new ArrayList<>(5);
	private static ArrayList<iMouseListener> mlChain = new ArrayList<>(5);
	private static ArrayList<iMMListener> mmovlChain = new ArrayList<>(5);
	private static ArrayList<iMMListener> mdraglChain = new ArrayList<>(5);
	

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
	
	static void addML(iMouseListener ml) {
		if (ml != null && !mlChain.contains(ml)) {
			mlChain.add(ml);
		}
	}
	
	static void addMMovL(iMMListener mml) {
		if (mml != null && !mmovlChain.contains(mml)) {
			mmovlChain.add(mml);
		}
	}
	
	static void addMDragL(iMMListener mdl) {
		if (mdl != null && !mdraglChain.contains(mdl)) {
			mdraglChain.add(mdl);
		}
	}
	
	
	private <T extends iListener> String  listListeners(ArrayList<T> chain) {
		StringBuilder sb = new StringBuilder();
		for (T al : chain) {
			sb.append(al.getClass()).append("\n");
			sb.append(al.getName()).append("\n");
		}
		return sb.toString();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		for (iCompListener cl : clChain) {
			cl.componentResized(e);
		}
	}
	
	String listCompListeners() {
		return listListeners(clChain);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
//		LogMsgFmtln("scroll pane: mouse wheel: ", e.getPoint());
		for (iMWListener mwl : mwlChain) {
			mwl.mouseWheelMoved(e);
		}
	}

	String listMouseWheelListeners() {
		return listListeners(mwlChain);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
//		LogMsgFmtln("scroll pane: mouse clicked: ", e.getPoint());
		for (iMouseListener ml : mlChain) {
			ml.mouseClicked(e);
		}
	}
	
	String listMouseClickListeners() { return listListeners(mlChain); }
	
	@Override
	public void mouseMoved(MouseEvent e) {
//		LogMsgFmtln("scroll pane: mouse moved: ", e.getPoint());
		for (iMMListener mml : mmovlChain) {
			mml.mouseMoved(e);
		}
	}
	
	String listMouseMovedListeners() { return listListeners(mmovlChain); }
	
	@Override
	public void mouseDragged(MouseEvent e) {
//		LogMsgFmtln("scroll pane: mouse moved: ", e.getPoint());
		for (iMMListener mdl : mdraglChain) {
			mdl.mouseDragged(e);
		}
	}
	
	String listMouseDraggedListeners() { return listListeners(mdraglChain); }
	
	// mouse listener
	@Override
	public void mousePressed(MouseEvent e) {
//		LogMsgFmtln("scroll: mouse pressed: ", e.getPoint());
		for (iMouseListener ml : mlChain) {
			ml.mousePressed(e);
		}
	}
	
	// mouse listener
	@Override
	public void mouseReleased(MouseEvent e) {
//		LogMsgFmtln("scroll: mouse released: ", e.getPoint());
		for (iMouseListener ml : mlChain) {
			ml.mouseReleased(e);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) { }
	
	@Override
	public void mouseExited(MouseEvent e) { }
	
	@Override
	public void componentMoved(ComponentEvent e) { }
	
	@Override
	public void componentShown(ComponentEvent e) { }
	
	@Override
	public void componentHidden(ComponentEvent e) { }
}
