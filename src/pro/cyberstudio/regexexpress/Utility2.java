package pro.cyberstudio.regexexpress;

import org.jetbrains.annotations.*;

import java.awt.*;

import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.regexexpress.Utility.dispVal;

/**
 * @author jeffs
 *         File:    Utility
 *         Created: 4/25/2017 @ 7:22 PM
 *         Project: RegexExpress
 */

class Utility2 {
	
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
	
	
	static String LogMsgStr2(String msg, Dimension2dx dim) 		{ return LogMsgStr(msg, dispVal2(dim)); }
	static void LogMsgFmtln2(String msg, Dimension2dx dim) 		{ LogMsgln(LogMsgStr(msg, dim)); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal2(Dimension2dx dim) { return dispVal(dim.width, dim.height); }
	
	
	
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
	
	
}
