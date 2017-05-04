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

	static RegexScroll regexAnalysisScroll  = new RegexScroll();
	
	private static JFrame frame;
	private static RegexExpress rx;
	private static JTextArea textAreaCoords;
	static JViewport regexViewport;

	private static RegexLayeredPane regexLayerPane = RegexLayeredPane.getInstance();
	
	static final int CANVASX = 1000;
	static final int CANVASY = 750;

	private static final int FRAMEPREFWIDTH = CANVASX - 250;
	private static final int FRAMEPREFHEIGHT = CANVASY - 150;

	private static final int SCROLLPREFWIDTH = CANVASX;
	private static final int SCROLLPREFHEIGHT = CANVASY;
	
	static final int IMAGEPOSX = (CANVASX / 2) - 50;
	static final int IMAGEPOSY = (CANVASY / 2) - 50;
	
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

	public void RegexExpressUI() {

		this.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));

		JButton btnA = new JButton("Zoom up");
		buttons.add(btnA);
		btnA.addActionListener(btn1);
		
		JButton btnB = new JButton("Zoom down");
		btnB.addActionListener(btn2);
		buttons.add(btnB);
		
		JButton btnC = new JButton("Add");
		btnC.addActionListener(btn4);
		buttons.add(btnC);
		
		JButton btnD = new JButton("Layer Info");
		btnD.addActionListener(btn5);
		buttons.add(btnD);
		
		JButton btnE = new JButton("View Info");
		btnE.addActionListener(btn3);
		buttons.add(btnE);
		
		JButton btnF = new JButton("Test");
		btnF.addActionListener(btn6);
		buttons.add(btnF);
		
		buttons.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 30));
		buttons.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 30));
		buttons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons);
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		JPanel textAreas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
		
//		textAreas.add(Box.createRigidArea(new Dimension(0,5)));
		
		textAreaCoords = new JTextArea("this is a test");
		textAreaCoords.setMaximumSize(new Dimension(175, 25));
		textAreaCoords.setPreferredSize(new Dimension(175, 25));
		textAreaCoords.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5 ));
		
		textAreas.add(textAreaCoords);
		
		add(textAreas);
		

		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		regexLayerPane.setMinimumSize(new Dimension(CANVASX, CANVASY));
		regexLayerPane.setPreferredSize(new Dimension(CANVASX, CANVASY));
		regexLayerPane.setBackground(Color.LIGHT_GRAY);
		regexLayerPane.initialize(regexAnalysisScroll.getHorizontalScrollBar(),
				regexAnalysisScroll.getVerticalScrollBar(), (new Dimension2dx(CANVASX, CANVASY)));
		regexLayerPane.add(getLayer());
		
		regexAnalysisScroll.setViewportView(regexLayerPane);
//		regexAnalysisScroll.setMinimumSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT)); - do not use
		regexAnalysisScroll.setPreferredSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT));
		regexAnalysisScroll.setViewportBorder(BorderFactory.createBevelBorder(3, Color.WHITE, Color.BLUE));
		regexAnalysisScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		regexAnalysisScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		regexAnalysisScroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5 ));
		regexAnalysisScroll.setWheelScrollingEnabled(false);
		regexAnalysisScroll.setName("scroll pane");
		
		regexViewport = regexAnalysisScroll.getViewport();
		regexViewport.setName("viewport");
		
		RegexScroll.addCL(regexLayerPane);
		RegexScroll.addML(regexLayerPane);

		add(regexAnalysisScroll);

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
		
//		scrollView(CANVASX / 2,CANVASY / 2);
		
		regexLayerPane.zoomTo(1.0, new Point(CANVASX / 2 - 50,CANVASY / 2 - 50));

		frame.setVisible(true);
	}
	
	private void scrollView(int centerX, int centerY) {
		int x = regexAnalysisScroll.getViewport().getWidth();
		int y = regexAnalysisScroll.getViewport().getHeight();
		
		LogMsgln("viewport H & w: " + Utility.dispVal(x, y));
		
		
		x = (int) (centerX * zoomFactor) - (x / 2);
		y = (int) (centerY * zoomFactor) - (y / 2);
		
		if (x < 0) { x = 0;}
		if (y < 0) { y = 0; }
		
		LogMsgln("viewport corner adj: " + Utility.dispVal(x, y));
		
		regexAnalysisScroll.getViewport().setViewPosition(new Point(x, y));
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
			regexLayerPane.zoomTo(2.0, new Point(CANVASX / 2 - 50,CANVASY / 2 - 50));
		}
	};
	
	private ActionListener btn5 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			LogMsgln(regexLayerPane.toString());
		}
	};
	
	private ActionListener btn4 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			regexLayerPane.add(getLayer());
		}
	};
	
	private ActionListener btn2 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			
			regexLayerPane.zoomToScale(.5);
			regexAnalysisScroll.getVerticalScrollBar().repaint();
			regexAnalysisScroll.getViewport().repaint();
		}
	};

	private ActionListener btn1 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			
			
			regexLayerPane.zoomToScale(2.0);
			regexAnalysisScroll.getVerticalScrollBar().repaint();
			regexAnalysisScroll.getViewport().repaint();
		}
	};
	
	private ActionListener btn3 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			
			// scroll
			LogMsgln("\n**************************************************");
			LogMsgln("scroll panel");
			LogMsgln(viewSizes(regexAnalysisScroll, viewSizeMask.all()));
			LogMsgln("vp bounds: " + Utility.dispVal(regexAnalysisScroll.getViewportBorderBounds()));
			
			ScrollPaneLayout layout = (ScrollPaneLayout) regexAnalysisScroll.getLayout();
			LogMsgln("\nscroll panel layout");
			LogMsgln("     size: " + Utility.dispVal(layout.preferredLayoutSize(regexAnalysisScroll)));
			
			LogMsgln("\nviewport");
			LogMsgln(viewSizes(regexAnalysisScroll.getViewport(), viewSizeMask.all()));
			LogMsgln("view size: " + Utility.dispVal(regexAnalysisScroll.getViewport().getViewSize()));
			LogMsgln(" ext size: " + Utility.dispVal(regexAnalysisScroll.getViewport().getExtentSize()));
			LogMsgln(" view pos: " + dispVal(regexAnalysisScroll.getViewport().getViewPosition()));
			
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
