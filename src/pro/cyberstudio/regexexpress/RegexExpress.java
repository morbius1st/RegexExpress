package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

import static pro.cyberstudio.regexexpress.Utility.viewSizeMask.*;
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
		
		// watch for the escape key
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(ESCAPE), ESCAPE);
		this.getActionMap().put(ESCAPE, key1);
		
		frame.setVisible(true);
		
		btnZWindow.setPreferredSize(btnZWindow.getSize());
		btnZWindow.setMinimumSize(btnZWindow.getSize());
		btnZWindow.setFocusPainted(false);
		
//		CircularStack<Integer> cx = new CircularStack<>(10);
//
//		cx.push(10);
//		cx.push(20);
//		cx.push(30);
//		cx.push(40);
//		cx.push(50);
//		cx.push(60);
//		cx.push(70);
//		cx.push(80);
//		cx.push(90);
//		cx.push(100);
//		cx.push(110);
//		cx.push(120);
//		cx.push(130);
//		cx.push(140);
//		cx.push(150);
//		cx.pop();
//		cx.pop();
//		cx.pop();
//
//		LogMsgln("stack size| " + cx.size());
//
//		int stackSize = cx.size();
//
//		for (int i = 0; i < stackSize; i++ ) {
//			LogMsgFmtln("cStack| ", "item # " + i + " = " + cx.pop());
//		}
//
//		LogMsgln("stack size| " + cx.size());
	
	}
	
	
	public void RegexExpressUI() {
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		
		buttons1.add(makeButton(btn3, "View<br>Info"));
		buttons1.add(makeButton(btn5, "Layer<br>Info"));
		buttons1.add(makeButton(btn31, "Layer 1<br>Info"));
		buttons1.add(makeButton(btn15, "Lay pane<br>Info"));
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
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel buttons3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		buttons3.add(makeButton(btn4, "Layer<br>Add"));
		buttons3.add(makeButton(btn36, "Layer<br>Off"));
		buttons3.add(makeButton(btn37, "Layer<br>On"));
		
		add(buttons3);
		
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
	
	private JButton makeButton(ActionListener b, String text) {
		JButton jb = new JButton("<html><center>" + text + "</center></html>");
		jb.setPreferredSize(BUTTONDIM);
		jb.setFocusPainted(false);
		jb.addActionListener(b);
		return jb;
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
	private ActionListener btn34 = actionEvent -> {
		if (!zWindowMode) {
			processFunction(34);
		} else {
			cancelZWindow();
		}
	};
	
	// zoom previous
	private ActionListener btn35 = actionEvent -> processFunction(35);
	
	// layer off
	private ActionListener btn36 = actionEvent -> processFunction(36);
	
	// layer on
	private ActionListener btn37 = actionEvent -> processFunction(37);

	
	// list layer information
	private ActionListener btn5 = actionEvent -> processFunction(5);
	
	// add layer
	private ActionListener btn4 = actionEvent -> processFunction(4);
	
	// unspecified test
	private ActionListener btn6 = actionEvent -> processFunction(6);

	// list view information
	private ActionListener btn3 = actionEvent ->  processFunction(3);
	
	// list layered pane information
	private ActionListener btn15 = actionEvent ->  processFunction(15);
	
	
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
				regexLayerPane.listLayers();
				break;
			case 15:
				regexLayerPane.listLayPaneInfo();
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

			// layer off
			case 36:
				regexLayerPane.layerOff("Layer 2");
				break;
			// layer on
			case 37:
				regexLayerPane.layerOn("Layer 2");
				break;
			
			// misc tests
			case 6:
//				regexLayerPane.testPointer();

				listListeners();
				
				break;
			case 3:
				LogMsg("\n");
				LogMsgFmtln("********* view ", "info *******************************");
				LogMsgFmtln("scroll", " panel");
				LogMsg(viewSizes(regexScroll, viewSizeMask.all(), true));
				LogMsgFmtln("vp bounds| ", dispVal(regexScroll.getViewportBorderBounds()));
				
				ScrollPaneLayout layout = (ScrollPaneLayout) regexScroll.getLayout();
				LogMsg("\n");
				LogMsgFmtln("scroll", " panel");
				LogMsgFmtln("layout size| ", dispVal(layout.preferredLayoutSize(regexScroll)));
				
				LogMsg("\n");
				LogMsgFmtln("viewport", "");
				LogMsg(viewSizes(regexScroll.getViewport(), viewSizeMask.all(), true));
				LogMsgFmtln("view size| ", dispVal(regexScroll.getViewport().getViewSize()));
				LogMsgFmtln(" ext size| ", dispVal(regexScroll.getViewport().getExtentSize()));
				LogMsgFmtln(" view pos| ", dispVal(regexScroll.getViewport().getViewPosition()));
				
				LogMsg("\n");
				LogMsgFmtln("layered", " pane");
				LogMsg(viewSizes(regexLayerPane, viewSizeMask.all(), true));
				break;
				
		}
	}

	private void listListeners() {
		LogMsg("\n");
		LogMsgFmtln("********* component ", "listeners *******************************");
		
		LogMsgln("\nclick listeners: " + regexScroll.listMouseClickListeners());
		LogMsgln("\nwheel listeners: " + regexScroll.listMouseWheelListeners());
		LogMsgln("\ndrag listeners: " + regexScroll.listMouseDragListeners());
		LogMsgln("\nmove listeners: " + regexScroll.listMouseMoveListeners());
		LogMsgln("\nmouse listeners: " + regexScroll.listMouseListeners());
	}
	
	
	static String viewSizes(Component c, int which, boolean newLine) {
		StringBuilder sb = new StringBuilder();
		boolean offsetDone = false;

		if ((which & SIZE.value) > 0) {
			offsetDone = true;
			sb.append(LogMsgStr("size| ", dispVal(c.getSize())));
			
			if (newLine) { sb.append("\n"); }
		}
		
		if ((which & MIN.value) > 0) {
			if (!offsetDone || newLine) {
				sb.append(LogMsgStr("min Size| ", dispVal(c.getMinimumSize())));
			} else {
				sb.append(" || min Size| ").append(dispVal(c.getMinimumSize()));
			}
			
			if (newLine) { sb.append("\n"); }
		}
		
		if ((which & PERF.value) > 0) {
			if (!offsetDone || newLine) {
				sb.append(LogMsgStr("pref Size| ", dispVal(c.getPreferredSize())));
			} else {
				sb.append(" || pref Size| ").append(dispVal(c.getPreferredSize()));
			}
			
			if (newLine) { sb.append("\n"); }
		}
		
		if ((which & MAX.value) > 0) {
			if (!offsetDone || newLine) {
				sb.append(LogMsgStr("max Size| ", dispVal(c.getMaximumSize())));
			} else {
				sb.append(" || max Size| ").append(dispVal(c.getMaximumSize()));
			}
			
			if (newLine) { sb.append("\n"); }
		}
		
		if ((which & BOUNDS.value) > 0) {
			
			if (!offsetDone || newLine) {
				sb.append(LogMsgStr("bounds| ", dispVal(c.getBounds())));
			} else {
				sb.append(" || bounds| ").append(dispVal(c.getBounds()));
			}
			
			if (newLine) { sb.append("\n"); }
		}
		return sb.toString();
	}

}
