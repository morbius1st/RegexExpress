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
	
	static String viewSizes(Component c, int which) {
		StringBuilder sb = new StringBuilder();
		
		if ((which & MIN.value) > 0) {
			sb.append("  Min Size: ").append(dispVal(c.getMinimumSize()));
			sb.append("\n");
		}
		
		if ((which & PERF.value) > 0) {
			sb.append(" Pref Size: ").append(dispVal(c.getPreferredSize()));
			sb.append("\n");
		}
		
		if ((which & MAX.value) > 0) {
			sb.append("  Max Size: ").append(dispVal(c.getMaximumSize()));
			sb.append("\n");
		}
		
		if ((which & SIZE.value) > 0) {
			sb.append("      Size: ").append(dispVal(c.getSize()));
			sb.append("\n");
		}
		
		if ((which & BOUNDS.value) > 0) {
			sb.append("    Bounds: ").append(dispVal(c.getBounds()));
			sb.append("\n");
		}
		
		
		return sb.toString();
	}

	
	static void LogMsgln(String msg) { System.out.println(msg); }
	
	static void LogMsg(String msg) {System.out.print(msg); }
	
	static void LogMsgFmtln(String msg1, String msg2) {
		if (msg2 == null) {msg2 = "";}
		LogMsgln(String.format("%1$35s%2$s", msg1, msg2));
	}
	
	static void LogMsgFmtln(String msg, Point pt) { LogMsgFmtln(msg, dispVal(pt)); }
	
	static void LogMsgFmtln(String msg, Point2D.Double pt) { LogMsgFmtln(msg, dispVal(pt)); }
	
	static void LogMsgFmtln(String msg, Dimension2dx dim) { LogMsgFmtln(msg, dispVal(dim)); }
	
	static void LogMsgFmtln(String msg, Dimension dim) { LogMsgFmtln(msg, dispVal(dim)); }
	
	static void LogMsgFmtln(String msg, double x, double y) { LogMsgFmtln(msg, dispVal(x, y)); }
	
	static void LogMsgFmtln(String msg, int x, int y) { LogMsgFmtln(msg, dispVal(x, y)); }
	
	static void LogMsgFmtln(String msg, Rectangle rect) { LogMsgFmtln(msg, dispVal(rect)); }
	
	static void LogMsgFmtln(String msg, int i) { LogMsgFmtln(msg, dispVal(i)); }
	
	static void LogMsgFmtln(String msg, double d) { LogMsgFmtln(msg, dispVal(d)); }
	
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
	static String dispVal(double x, double y) { return "x| " + x + " y| " + y; }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(int x, int y) { return "x| " + x + " y| " + y; }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(Rectangle rect) { return "x| " + rect.x + " y| " + rect.y + " w| " + rect.width + " h| " + rect.height; }
	
}
