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

class RegexExpress extends JPanel { //implements ComponentListener {

//	private JScrollPane regexAnalysisScroll;
	private RegexScroll regexAnalysisScroll;
	
	
	private static JFrame frame;
	private static RegexExpress rx;

	private static RegexLayeredPane regexLayerPane = RegexLayeredPane.getInstance();
	
	private static final int CANVASX = 1000;
	private static final int CANVASY = 750;

	private static final int FRAMEPREFWIDTH = CANVASX - 250;
	private static final int FRAMEPREFHEIGHT = CANVASY - 150;

	private static final int SCROLLPREFWIDTH = FRAMEPREFWIDTH;
	private static final int SCROLLPREFHEIGHT = FRAMEPREFHEIGHT;
	
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
		
		buttons.setMaximumSize(new Dimension(FRAMEPREFWIDTH, 30));
		buttons.setPreferredSize(new Dimension(FRAMEPREFWIDTH, 30));
		buttons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		add(buttons);

		this.add(Box.createRigidArea(new Dimension(0,10)));

		regexAnalysisScroll = new RegexScroll();
		
		regexLayerPane.setMinimumSize(new Dimension(CANVASX, CANVASY));
		regexLayerPane.setPreferredSize(new Dimension(CANVASX, CANVASY));
		regexLayerPane.setBackground(Color.LIGHT_GRAY);
		regexLayerPane.initialize(regexAnalysisScroll.getHorizontalScrollBar(),
				regexAnalysisScroll.getVerticalScrollBar(), (new Dimension2dx(CANVASX, CANVASY)));
		regexLayerPane.add(getLayer());
		
		regexAnalysisScroll.setViewportView(regexLayerPane);
		regexAnalysisScroll.getViewport().setName("viewport");
//		regexAnalysisScroll.setMinimumSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT));
		regexAnalysisScroll.setPreferredSize(new Dimension(SCROLLPREFWIDTH, SCROLLPREFHEIGHT));
		regexAnalysisScroll.setViewportBorder(BorderFactory.createBevelBorder(3, Color.WHITE, Color.BLUE));
		regexAnalysisScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		regexAnalysisScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		regexAnalysisScroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5 ));
		
		regexAnalysisScroll.setWheelScrollingEnabled(false);
		regexAnalysisScroll.setName("scroll pane");
		
		RegexScroll.addCL(regexLayerPane);

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
		
		scrollView(CANVASX / 2,CANVASY / 2);

		frame.setVisible(true);
	}
	
	private void scrollView(int centerX, int centerY) {
		int x = regexAnalysisScroll.getViewport().getWidth() / 2;
		int y = regexAnalysisScroll.getViewport().getHeight() / 2;
		
		x = (int) (centerX * zoomFactor) - x;
		y = (int) (centerY * zoomFactor) - y;
		
		if (x < 0) { x = 0;}
		if (y < 0) { y = 0; }
		
		regexAnalysisScroll.getViewport().setViewPosition(new Point(x, y));
	}

	
	private String getLayer() {
		return String.format("Layer %1$d", ++layerIdx);
	}
	
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
			
			regexLayerPane.zoomView(.5);
			regexAnalysisScroll.getVerticalScrollBar().repaint();
			regexAnalysisScroll.getViewport().repaint();
		}
	};

	private ActionListener btn1 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			
			regexLayerPane.zoomView(2.0);
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
			LogMsgln("vp bounds: " + displayRect(regexAnalysisScroll.getViewportBorderBounds()));
			
			ScrollPaneLayout layout = (ScrollPaneLayout) regexAnalysisScroll.getLayout();
			LogMsgln("\nscroll panel layout");
			LogMsgln("     size: " + displayDim(layout.preferredLayoutSize(regexAnalysisScroll)));
			
			LogMsgln("\nviewport");
			LogMsgln(viewSizes(regexAnalysisScroll.getViewport(), viewSizeMask.all()));
			LogMsgln("view size: " + displayDim(regexAnalysisScroll.getViewport().getViewSize()));
			LogMsgln(" ext size: " + displayDim(regexAnalysisScroll.getViewport().getExtentSize()));
			LogMsgln(" view pos: " + displayPt(regexAnalysisScroll.getViewport().getViewPosition()));
			
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
//			LogMsgln(" min sz: " + displayDim(sb.getMinimumSize()));
//			LogMsgln(" max sz: " + displayDim(sb.getMaximumSize()));

		}
	};
	
//	@Override
//	public void componentResized(ComponentEvent e) {
////
////		Dimension2dx dxPerf =
////				Dimension2dx.toDimension2dx(regexAnalysisScroll.getViewport().getPreferredSize());
////		Dimension2dx dxSize =
////				Dimension2dx.toDimension2dx(regexAnalysisScroll.getViewport().getSize());
////
////		if (dxSize.eitherGreaterThan(dxPerf)) {
////			regexAnalysisScroll.getViewport().setPreferredSize(dxSize.toDimension());
////			layPane.updateSize();
////		}
//
//	}
//
//	@Override
//	public void componentMoved(ComponentEvent e) {
//
//	}
//
//	@Override
//	public void componentShown(ComponentEvent e) {
//
//	}
//
//	@Override
//	public void componentHidden(ComponentEvent e) {
//
//	}
}
