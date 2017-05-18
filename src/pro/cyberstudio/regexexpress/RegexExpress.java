package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.regexexpress.Utility.viewSizeMask.*;

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
	
	private static final int BUTTONHEIGHT = 40;
	private static final int BUTTONWEIGHT = 85;
	
	private static final Dimension BUTTONDIM =
			new Dimension(BUTTONWEIGHT, BUTTONHEIGHT);
	
	private static final String ESCAPE = "ESCAPE";
	
	private static JButton btnZWindow;
	private static boolean zWindowMode = false;
	
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
		jb.setPreferredSize(BUTTONDIM);
		jb.setFocusPainted(false);
		jb.addActionListener(b);
		return jb;
	}
	
	
	public void RegexExpressUI() {
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		buttons1.add(makeButton(btn4, "Add<br>Layer"));
		buttons1.add(makeButton(btn3, "View<br>Info"));
		buttons1.add(makeButton(btn5, "Layer<br>Info"));
		buttons1.add(makeButton(btn31, "Layer 1<br>Info"));
		buttons1.add(makeButton(btn6, "Misc<br>Test"));
		
		
		buttons1.setMaximumSize(new Dimension(FRAMEPREFWIDTH, BUTTONHEIGHT));
		buttons1.setPreferredSize(new Dimension(FRAMEPREFWIDTH, BUTTONHEIGHT));
		buttons1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons1);
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		buttons2.add(makeButton(btn2, "Zoom<br>In"));
		buttons2.add(makeButton(btn1, "Zoom<br>Out"));
		buttons2.add(makeButton(btn32, "Zoom<br>x 2.0"));
		buttons2.add(makeButton(btn33, "Zoom to<br>500x1000"));
		btnZWindow = makeButton(btn34, "Zoom<br>Window");
		buttons2.add((btnZWindow));
		buttons2.add(makeButton(btn35, "Zoom<br>Previous"));
		
		buttons2.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons2.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons2);
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel textAreas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		textAreaCoords = new JTextArea("coordinates");
		textAreaCoords.setMaximumSize(new Dimension(350, 25));
		textAreaCoords.setPreferredSize(new Dimension(350, 25));
		textAreaCoords.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		textAreaZmFactor = new JTextArea("Zoom Factor");
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
		
		regexLayerPane.initialize(new Dimension2dx(LAYERX, LAYERY));
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
				// misc tests
			case 6:
				//			regexLayerPane.testPointer();
//				regexLayerPane.listVisRect();
//				regexLayerPane.listVisRect2();
//				regexLayerPane.listViewportRect();
//				regexLayerPane.listMyInfo();
				LogMsgln("\ncomponent listeners: " + regexScroll.listCompListeners());
				LogMsgln("\nclick listeners: " + regexScroll.listMouseClickListeners());
				LogMsgln("\nwheel listeners: " + regexScroll.listMouseWheelListeners());
				LogMsgln("\ndrag listeners: " + regexScroll.listMouseDragListeners());
				LogMsgln("\nmove listeners: " + regexScroll.listMouseMoveListeners());
				LogMsgln("\nmouse listeners: " + regexScroll.listMouseListeners());
				
				break;
			case 3:
				LogMsgFmtln("*******************", "*******************************");
				LogMsgFmtln("scroll", " panel");
				LogMsg(viewSizes(regexScroll, viewSizeMask.all()));
				LogMsgFmtln("vp bounds| ", Utility.dispVal(regexScroll.getViewportBorderBounds()));
				
				ScrollPaneLayout layout = (ScrollPaneLayout) regexScroll.getLayout();
				LogMsg("\n");
				LogMsgFmtln("scroll", " panel");
				LogMsgFmtln("layout size| ", Utility.dispVal(layout.preferredLayoutSize(regexScroll)));
				
				LogMsg("\n");
				LogMsgFmtln("viewport", "");
				LogMsg(viewSizes(regexScroll.getViewport(), viewSizeMask.all()));
				LogMsgFmtln("view size| ", Utility.dispVal(regexScroll.getViewport().getViewSize()));
				LogMsgFmtln(" ext size| ", Utility.dispVal(regexScroll.getViewport().getExtentSize()));
				LogMsgFmtln(" view pos| ", dispVal(regexScroll.getViewport().getViewPosition()));
				
				LogMsg("\n");
				LogMsgFmtln("layered", " pane");
				LogMsg(viewSizes(regexLayerPane, viewSizeMask.all()));
				break;
				
		}
	}
	
	String viewSizes(Component c, int which) {
		StringBuilder sb = new StringBuilder();
		
		if ((which & MIN.value) > 0) {
			sb.append(LogMsgStr("Min Size| ", dispVal(c.getMinimumSize())));
			sb.append("\n");
		}
		
		if ((which & PERF.value) > 0) {
			sb.append(LogMsgStr("Pref Size: ", dispVal(c.getPreferredSize())));
			sb.append("\n");
		}
		
		if ((which & MAX.value) > 0) {
			sb.append(LogMsgStr("Max Size: ", dispVal(c.getMaximumSize())));
			sb.append("\n");
		}
		
		if ((which & SIZE.value) > 0) {
			sb.append(LogMsgStr("Size: ", dispVal(c.getSize())));
			sb.append("\n");
		}
		
		if ((which & BOUNDS.value) > 0) {
			sb.append(LogMsgStr("Bounds: ", dispVal(c.getBounds())));
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
