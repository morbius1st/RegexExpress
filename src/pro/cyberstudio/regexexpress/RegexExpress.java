package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;
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
	
	static final int LAYPANEX = 1000;
	static final int LAYPANEY = 750;
	
	static final int LAYERX = 3500;	// approx 36" @ 92 dpi
	static final int LAYERY = 2200; // approx 24" @ 92 dpi

	private static final int FRAMEPREFWIDTH = LAYPANEX - 250;
	private static final int FRAMEPREFHEIGHT = LAYPANEY - 150;

	private static final int SCROLLPREFWIDTH = LAYPANEX;
	private static final int SCROLLPREFHEIGHT = LAYPANEY;
	
	static final int IMAGEPOSX = (LAYPANEX / 2) - 50;
	static final int IMAGEPOSY = (LAYPANEY / 2) - 50;
	
	static final int CROSSORIGINX = 200;
	static final int CROSSORIGINY = 200;
	
	private static final int CROSSOFFSETX = LAYPANEX - (CROSSORIGINX *2);
	private static final int CROSSOFFSETY = LAYPANEY - (CROSSORIGINY *2);
	
	static final Point[] CROSSORIGINPTS = {
			new Point(CROSSORIGINX, CROSSORIGINY),
			new Point(CROSSORIGINX + CROSSOFFSETX, CROSSORIGINY),
			new Point(CROSSORIGINX, CROSSORIGINY + CROSSOFFSETY),
			new Point(CROSSORIGINX + CROSSOFFSETX, CROSSORIGINY + CROSSOFFSETY)};
	
	private static double zoomFactor = 1.0;
	
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
		frame.setLocation(((screenX / 2) - (FRAMEPREFWIDTH / 2)) , ((screenY / 3) - (FRAMEPREFHEIGHT / 2)));
		
		frame.pack();
		
		regexLayerPane.zoomCentered(1.0, new Point(LAYPANEX / 2 - 50, LAYPANEY / 2 - 50));
		
		frame.setVisible(true);
	}
	
	private JButton makeButton(ActionListener b, String text) {
		JButton jb = new JButton("<html><center>" + text + "</center></html>");
		jb.addActionListener(b);
		return jb;
	}
	
	
	public void RegexExpressUI() {

		this.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel buttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));

//		JButton btnA = new JButton("Zoom\nUp");
//		btnA.addActionListener(btn1);
		buttons1.add(makeButton(btn1, "Zoom<br>Out"));
		buttons1.add(makeButton(btn2, "Zoom<br>In"));
		buttons1.add(makeButton(btn4, "Add<br>Layer"));
		buttons1.add(makeButton(btn3, "View<br>Info"));
		buttons1.add(makeButton(btn5, "Layer<br>Info"));
		buttons1.add(makeButton(btn6, "Misc<br>Test"));
		
//		JButton btnB = new JButton("Zoom\nDn");
//		btnB.addActionListener(btn2);
//		buttons1.add(btnB);
//
//		JButton btnC = new JButton("Add");
//		btnC.addActionListener(btn4);
//		buttons1.add(btnC);
//
//		JButton btnD = new JButton("Layer\nInfo");
//		btnD.addActionListener(btn5);
//		buttons1.add(btnD);
//
//		JButton btnE = new JButton("View\nInfo");
//		btnE.addActionListener(btn3);
//		buttons1.add(btnE);
//
//		JButton btnF = new JButton("Test 1");
//		btnF.addActionListener(btn6);
//		buttons1.add(btnF);
		
		buttons1.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons1.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons1);
		
		this.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
		
		buttons2.add(makeButton(btn7, "Zoom<br>To Pt"));
		buttons2.add(makeButton(btn8, "Zoom<br>Ctr'd"));
		
		buttons2.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons2.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 50));
		buttons2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons2);
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		JPanel textAreas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
		
		textAreaCoords = new JTextArea("coordinates");
		textAreaCoords.setMaximumSize(new Dimension(175, 25));
		textAreaCoords.setPreferredSize(new Dimension(175, 25));
		textAreaCoords.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5 ));
		
		textAreaZmFactor = new JTextArea("coordinates");
		textAreaZmFactor.setMaximumSize(new Dimension(175, 25));
		textAreaZmFactor.setPreferredSize(new Dimension(175, 25));
		textAreaZmFactor.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5 ));
		
		textAreas.add(textAreaCoords);
		textAreas.add(textAreaZmFactor);
		
		add(textAreas);

		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		regexLayerPane.setMinimumSize(new Dimension(LAYERX, LAYERY));
		regexLayerPane.setPreferredSize(new Dimension(LAYERX, LAYERY));
		regexLayerPane.setBackground(Color.LIGHT_GRAY);
		regexLayerPane.initialize(regexScroll.getHorizontalScrollBar(),
				regexScroll.getVerticalScrollBar(), (new Dimension2dx(LAYERX, LAYERY)));
		regexLayerPane.add(getLayer());
		
		regexScroll.setViewportView(regexLayerPane);
//		regexAnalysisScroll.setMinimumSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT)); - do not use
		regexScroll.setPreferredSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT));
		regexScroll.setViewportBorder(BorderFactory.createBevelBorder(3, Color.WHITE, Color.BLUE));
		regexScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		regexScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		regexScroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5 ));
		regexScroll.setWheelScrollingEnabled(false);
		regexScroll.setName("scroll pane");
		
		regexViewport = regexScroll.getViewport();
		regexViewport.setName("viewport");

		add(regexScroll);

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	
	private String getLayer() {
		return String.format("Layer %1$d", ++layerIdx);
	}
	
	static void addCoordText(Point pt2D) {
		textAreaCoords.setText(dispVal(pt2D));
	}
	
	// unspecified test
	private ActionListener btn6 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
//			regexLayerPane.testPointer();

			LogMsgln("comp  listeners: " + regexScroll.listCompListeners());
			LogMsgln("click listeners: " + regexScroll.listMouseClickListeners());
			LogMsgln("wheel listeners: " + regexScroll.listMouseWheelListeners());
			LogMsgln("drag  listeners: " + regexScroll.listMouseDraggedListeners());
		}
	};

	private ActionListener btn7 = actionEvent ->
			regexLayerPane.moveToPoint(CROSSORIGINPTS[3]);
	
	private ActionListener btn8 = actionEvent ->
			regexLayerPane.zoomCenteredDwgCoord(1.0, CROSSORIGINPTS[3]);
	
	private ActionListener btn5 = actionEvent -> LogMsgln(regexLayerPane.toString());
	
	private ActionListener btn4 = actionEvent -> regexLayerPane.add(getLayer());
	
	// zoom in
	private ActionListener btn1 = actionEvent -> regexLayerPane.zoomIn();

	// zoom out
	private ActionListener btn2 = actionEvent -> regexLayerPane.zoomOut();
	
	private ActionListener btn3 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			
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

		}
	};
}
