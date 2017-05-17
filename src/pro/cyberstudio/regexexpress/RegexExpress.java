package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;

import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    main
 *         Created: 4/15/2017 @ 5:58 AM
 *         Project: RegexExpress
 *
 */

class RegexExpress extends JPanel {
	
	static RegexScroll regexScroll = new RegexScroll();
	
	private static JFrame frame;
	private static RegexExpress rx;
	static JTextArea textAreaCoords;
	static JTextArea textAreaZmFactor;
	
	static JViewport regexViewport;
	
	private static RegexLayeredPane regexLayerPane = RegexLayeredPane.getInstance();
	
	// initial layer size
	static final int LAYERX = 8000;
	static final int LAYERY = 4000;
	
	// initial layered pane size
	static final int LAYPANEX = 1000;
	static final int LAYPANEY = 750;
	static final int LAYPANEOFFSETX = (LAYERX - LAYPANEX) / 2;
	static final int LAYPANEOFFSETY = (LAYERY - LAYPANEY) / 2;
	
	private static final int FRAMEPREFWIDTH = LAYPANEX - 250;
	private static final int FRAMEPREFHEIGHT = LAYPANEY - 150;
	
	private static final int SCROLLPREFWIDTH = LAYERX;
	private static final int SCROLLPREFHEIGHT = LAYERY;
	
	private static final String ESCAPE = "ESCAPE";
	
	private static JButton btnZWindow;
	private static boolean zWindowMode = false;

//	static final int IMAGEPOSX = ((LAYPANEX / 2) - 50) + LAYPANEOFFSETX;
//	static final int IMAGEPOSY = ((LAYPANEY / 2) - 50) + LAYPANEOFFSETY;

//	private static final int CROSSORIGINOFFSETX = 200;
//	private static final int CROSSORIGINOFFSETY = 200;
//
//	private static final int CROSSOFFSETX = LAYPANEX - (CROSSORIGINOFFSETX *2);
//	private static final int CROSSOFFSETY = LAYPANEY - (CROSSORIGINOFFSETY *2);
//
//	private static final int CROSSORIGINX = LAYPANEOFFSETX + CROSSORIGINOFFSETX;
//	private static final int CROSSORIGINY = LAYPANEOFFSETY + CROSSORIGINOFFSETY;
//
//	static final Point[] CROSSORIGINPTS = {
//			new Point(CROSSORIGINX, CROSSORIGINY),
//			new Point(CROSSORIGINX + CROSSOFFSETX, CROSSORIGINY),
//			new Point(CROSSORIGINX, CROSSORIGINY + CROSSOFFSETY),
//			new Point(CROSSORIGINX + CROSSOFFSETX, CROSSORIGINY + CROSSOFFSETY)};
	
	
	private static int layerIdx = 0;
	
	
	public static void main(String[] args) {
		rx = new RegexExpress();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				rx.createAndShowGUI();
			}
		});
	}
	
	
	public void createAndShowGUI() {
		
		int screenX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		frame = new JFrame("Regex Express");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rx.RegexExpressUI();
		rx.setOpaque(true);
		frame.setContentPane(rx);
		frame.setMinimumSize(new Dimension(FRAMEPREFWIDTH, FRAMEPREFHEIGHT));
		frame.setPreferredSize(new Dimension(FRAMEPREFWIDTH, FRAMEPREFHEIGHT));
		frame.setLocation(((screenX / 2) - (FRAMEPREFWIDTH / 2)), ((screenY / 3) - (FRAMEPREFHEIGHT / 2)));
		
		frame.pack();
		
		regexLayerPane.setInitialZoom(1.0, new Point(new Point(LAYERX / 2, LAYERY / 2)));
		
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(ESCAPE), ESCAPE);
		this.getActionMap().put(ESCAPE, key1);
		
		frame.setVisible(true);
		
		btnZWindow.setPreferredSize(btnZWindow.getSize());
		btnZWindow.setMinimumSize(btnZWindow.getSize());
		btnZWindow.setFocusPainted(false);
	}
	
	private JButton makeButton(ActionListener b, String text) {
		JButton jb = new JButton("<html><center>" + text + "</center></html>");
		jb.addActionListener(b);
		return jb;
	}
	
	
	public void RegexExpressUI() {
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

//		JButton btnA = new JButton("Zoom\nUp");
//		btnA.addActionListener(btn1);
		
		buttons1.add(makeButton(btn4, "Add<br>Layer"));
		buttons1.add(makeButton(btn3, "View<br>Info"));
		buttons1.add(makeButton(btn5, "Layer<br>Info"));
		buttons1.add(makeButton(btn31, "Layer 1<br>Info"));
		
		
		buttons1.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons1.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons1);
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		buttons2.add(makeButton(btn6, "Misc<br>Test"));
		
		
		buttons2.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons2.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons2);
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		
		buttons3.add(makeButton(btn2, "Zoom In<br>New"));
		buttons3.add(makeButton(btn1, "Zoom Out<br>New"));
		buttons3.add(makeButton(btn32, "Zoom Ratio<br>x 2.0"));
		buttons3.add(makeButton(btn33, "Zoom to<br>500x1000"));
		btnZWindow = makeButton(btn34, "Zoom<br>Window");
		buttons3.add((btnZWindow));
		buttons3.add(makeButton(btn35, "Zoom<br>Previous"));
		
		buttons3.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons3.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons3.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons3);
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel textAreas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		textAreaCoords = new JTextArea("coordinates");
		textAreaCoords.setMaximumSize(new Dimension(350, 25));
		textAreaCoords.setPreferredSize(new Dimension(350, 25));
		textAreaCoords.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		textAreaZmFactor = new JTextArea("coordinates");
		textAreaZmFactor.setMaximumSize(new Dimension(175, 25));
		textAreaZmFactor.setPreferredSize(new Dimension(175, 25));
		textAreaZmFactor.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		textAreas.add(textAreaCoords);
		textAreas.add(textAreaZmFactor);
		
		add(textAreas);
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		
		regexLayerPane.setMinimumSize(new Dimension(LAYERX, LAYERY));
		regexLayerPane.setPreferredSize(new Dimension(LAYERX, LAYERY));
		regexLayerPane.setBackground(Color.LIGHT_GRAY);
		
		regexScroll.setViewportView(regexLayerPane);
//		regexAnalysisScroll.setMinimumSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT)); - do not use
		regexScroll.setPreferredSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT));
		regexScroll.setViewportBorder(BorderFactory.createBevelBorder(3, Color.WHITE, Color.BLUE));
		regexScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		regexScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		regexScroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		regexScroll.setWheelScrollingEnabled(false);
		regexScroll.setName("scroll pane");
		
		regexViewport = regexScroll.getViewport();
		regexViewport.setName("viewport");
		regexViewport.setLocation(new Point(LAYPANEOFFSETX, LAYPANEOFFSETY));
		
		regexLayerPane.initialize(regexScroll.getHorizontalScrollBar(),
				regexScroll.getVerticalScrollBar(), (new Dimension2dx(LAYERX, LAYERY)));
		regexLayerPane.add(getLayer());
		
		add(regexScroll);
		
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
	}
	
	private String getLayer() {
		return String.format("Layer %1$d", ++layerIdx);
	}
	
	static void addCoordText(Point pt2D) {
		textAreaCoords.setText(dispVal(pt2D));
	}
	
	private void cancelZWindow() {
		if (zWindowMode) {
			zWindowMode = false;
			btnZWindow.setForeground(Color.BLACK);
			regexLayerPane.stopZoomWindow();
		}
	}
	
	// **** keys ***************************************************
	private Action key1 = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			cancelZWindow();
		}
	};


// **** new ****************************************************
	
	// zoom out
	private ActionListener btn1 = actionEvent -> processFunction(1);
	
	// zoom in
	private ActionListener btn2 = actionEvent -> processFunction(2);
	
	// layer one info
	private ActionListener btn31 = actionEvent -> processFunction(31);
	
	// set zoom scale x2.0
	private ActionListener btn32 = actionEvent -> processFunction(32);
	
	// move to coordinate
	private ActionListener btn33 = actionEvent -> processFunction(33);
	
	// zoom window
	private ActionListener btn34 = actionEvent -> processFunction(34);
	
	// zoom previous
	private ActionListener btn35 = actionEvent -> processFunction(35);
	
	
// **** old ****************************************************
	
	// list layer information
	private ActionListener btn5 = actionEvent -> processFunction(5);
	
	// add layer
	private ActionListener btn4 = actionEvent -> processFunction(4);
	
	// unspecified test
	private ActionListener btn6 = actionEvent -> processFunction(6);

	// list view information
	private ActionListener btn3 = actionEvent ->  processFunction(3);
	
	
// **** process functions ****************************************************
	private void processFunction(int function) {
		cancelZWindow();
		switch (function) {
			case 1:
				regexLayerPane.zoomOut();
				break;
			case 2:
				regexLayerPane.zoomIn();
				break;
			
			case 4:
				regexLayerPane.add(getLayer());
				break;
			case 5:
				LogMsgln(regexLayerPane.toString());
				break;
			case 31:
				regexLayerPane.getLayerOneInfo();
				break;
			case 32:
				regexLayerPane.setZoomRatio(2.0);
				break;
			case 33:
				regexLayerPane.moveToPoint2(new Point(500, 1000));
				break;
			case 34:
				zWindowMode = true;
				btnZWindow.setForeground(Color.RED);
				regexLayerPane.startZoomWindow();
				break;
			case 35:
				regexLayerPane.zoomPrevious();
				break;
			case 6:
				//			regexLayerPane.testPointer();
				regexLayerPane.listVisRect2();
				//			regexLayerPane.listMyInfo();
				//			LogMsgln("comp  listeners: " + regexScroll.listCompListeners());
				//			LogMsgln("click listeners: " + regexScroll.listMouseClickListeners());
				//			LogMsgln("wheel listeners: " + regexScroll.listMouseWheelListeners());
				//			LogMsgln("drag  listeners: " + regexScroll.listMouseDraggedListeners());
				break;
			case 3:
				// scroll
				LogMsgln("\n**************************************************");
				LogMsgln("scroll panel");
				LogMsgln(viewSizes(regexScroll, viewSizeMask.all()));
				LogMsgln("vp bounds: " + Utility.dispVal(regexScroll.getViewportBorderBounds()));
				
				ScrollPaneLayout layout = (ScrollPaneLayout) regexScroll.getLayout();
				LogMsgln("\nscroll panel layout");
				LogMsgln("     size: " + Utility.dispVal(layout.preferredLayoutSize(regexScroll)));
				
				LogMsgln("\nviewport");
				LogMsgln(viewSizes(regexScroll.getViewport(), viewSizeMask.all()));
				LogMsgln("view size: " + Utility.dispVal(regexScroll.getViewport().getViewSize()));
				LogMsgln(" ext size: " + Utility.dispVal(regexScroll.getViewport().getExtentSize()));
				LogMsgln(" view pos: " + dispVal(regexScroll.getViewport().getViewPosition()));
				
				LogMsgln("\nlayered pane");
				LogMsgln(viewSizes(regexLayerPane, viewSizeMask.all()));
				//
				//			JScrollBar sb = regexAnalysisScroll.getVerticalScrollBar();
				//
				//			LogMsgln("\nvScrollBar");
				//			LogMsgln("maximum: " + sb.getMaximum() +
				//					" ::Inc " + sb.getUnitIncrement() +
				//					" ::blk inc " + sb.getBlockIncrement());
				//			LogMsgln("minimum: " + sb.getMinimum());
				//			LogMsgln("visible: " + sb.getVisibleAmount());
				//			LogMsgln(" min sz: " + dispVal(sb.getMinimumSize()));
				//			LogMsgln(" max sz: " + dispVal(sb.getMaximumSize()));
				break;
				
		}
	}
	

}
