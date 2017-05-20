package pro.cyberstudio.regexexpress;

import org.jetbrains.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;

//import pro.cyberstudio.regexexpress.RegexPointer.PointerModes;

import static pro.cyberstudio.regexexpress.Utility.viewSizeMask.*;

/**
 * @author jeffs
 *         File:    Utility
 *         Created: 4/25/2017 @ 7:22 PM
 *         Project: RegexExpress
 */

class Utility {

	// point math
	// add one point to another = add x1 to x2 and y1 to y2
	static Point addPoints(Point p1, Point p2) {
		return  new Point(p1.x + p2.x, p1.y + p2.y);
	}

	static Point addToPoint(Point p1, int x, int y) {
		return new Point(p1.x + x, p1.y + y);
	}
	
	
	static Point.Double addPoints(Point.Double p1, Point.Double p2) {
		return  new Point.Double(p1.x + p2.x, p1.y + p2.y);
	}
	
	// subtract a point from another = subtract x2 from x1 and y2 from y1
	static Point subtractPoints(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	static Point subtractFromPoint(Point p1, int x, int y) {
		return new Point(p1.x - x, p1.y - y);
	}
	
	// multiply a point = multiply x1 by double d1 and y1 by double d2
	static Point multiplyPoint(Point p1, double dx, double dy) {
		return new Point((int) (p1.x * dx), (int) (p1.y * dy));
	}
	
	static boolean pointsEqual(Point p1, Point p2) {
		return (p1.x == p2.x) && (p1.y == p2.y);
	}
	
	static boolean pointsXEqual(Point p1, Point p2) {
		return (p1.x == p2.x);
	}
	
	static boolean pointsYEqual(Point p1, Point p2) {
		return (p1.y == p2.y);
	}
	
	enum DragModes {NONE, XHAIRS, PAN, STARTWINDOW, WINDOW, SELECTION, LINE }
	
	enum viewSizeMask { MIN(1), PERF(2), MAX(4), SIZE(8), BOUNDS(16);
		int value;
		viewSizeMask(int val) {
			value = (byte) val;
		}
		
		static public int all() {
			int all = 0;
			
			for (viewSizeMask v : values()) {
				all += v.value;
			}
			
			return all;
		}
	}
	

//	static String FmtXY(int x, int y) {
//		return FmtXY((double) x, (double) y);
//	}
	
	static String FmtXY(double x, double y) {
		return String.format("x| %1$8.2f  y| %2$8.2f", x, y);
	}
	
	static String FmtWH(double w, double h) {
		return String.format("w| %1$8.2f  h| %2$8.2f", w, h);
	}
	
	static void LogMsgln(String msg) { System.out.println(msg); }
	
	static void LogMsg(String msg) {System.out.print(msg); }
	
	static String LogMsgStr(String msg1, String msg2) {
		if (msg2 == null) {msg2 = "";}
		return String.format("%1$35s%2$s", msg1, msg2);
	}
	static String LogMsgStr(String msg, Point pt) 				{ return LogMsgStr(msg, dispVal(pt)); }
	static String LogMsgStr(String msg, Point2D.Double pt) 		{ return LogMsgStr(msg, dispVal(pt)); }
	static String LogMsgStr(String msg, Dimension2dx dim) 		{ return LogMsgStr(msg, dispVal(dim)); }
	static String LogMsgStr(String msg, Dimension dim) 			{ return LogMsgStr(msg, dispVal(dim)); }
	static String LogMsgStr(String msg, double x, double y) 	{ return LogMsgStr(msg, dispVal(x, y)); }
	static String LogMsgStr(String msg, int x, int y) 			{ return LogMsgStr(msg, dispVal(x, y)); }
	static String LogMsgStr(String msg, Rectangle rect) 		{ return LogMsgStr(msg, dispVal(rect)); }
	static String LogMsgStr(String msg, int i) 					{ return LogMsgStr(msg, dispVal(i)); }
	static String LogMsgStr(String msg, double d) 				{ return LogMsgStr(msg, dispVal(d)); }
	
	static void LogMsgFmtln(String msg1, String msg2) 			{ LogMsgln(LogMsgStr(msg1, msg2)); }
	static void LogMsgFmtln(String msg, Point pt) 				{ LogMsgln(LogMsgStr(msg, pt)); }
	static void LogMsgFmtln(String msg, Point2D.Double pt) 		{ LogMsgln(LogMsgStr(msg, pt)); }
	static void LogMsgFmtln(String msg, Dimension2dx dim) 		{ LogMsgln(LogMsgStr(msg, dim)); }
	static void LogMsgFmtln(String msg, Dimension dim) 			{ LogMsgln(LogMsgStr(msg, dim)); }
	static void LogMsgFmtln(String msg, double x, double y) 	{ LogMsgln(LogMsgStr(msg, x, y)); }
	static void LogMsgFmtln(String msg, int x, int y) 			{ LogMsgln(LogMsgStr(msg, x, y)); }
	static void LogMsgFmtln(String msg, Rectangle rect) 		{ LogMsgln(LogMsgStr(msg, rect)); }
	static void LogMsgFmtln(String msg, int i) 					{ LogMsgln(LogMsgStr(msg, i)); }
	static void LogMsgFmtln(String msg, double d) 				{ LogMsgln(LogMsgStr(msg, d)); }
	
	static String dispVal(int i)  {return String.valueOf(i); }
	static String dispVal(double d)  {return String.valueOf(d); }
	
	@NotNull
	static String dispZoom(double zoomFactor) { return "zoom factor| " + zoomFactor; }
	
	@NotNull
	static String dispVal(Point pt)  {return dispVal(pt.getX(), pt.getY()); }
	
	@NotNull
	static String dispVal(Point2D.Double pt)  { return dispVal(pt.getX(), pt.getY()); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(Dimension2dx dim) { return dispVal(dim.width, dim.height); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(Dimension dim) { return dispVal(dim.width, dim.height); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(double x, double y) { return FmtXY(x, y); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(int x, int y) { return FmtXY(x, y); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(Rectangle rect) { return FmtXY(rect.x, rect.y) + "  " +  FmtWH(rect.width, rect.height); }
	
}
