package pro.cyberstudio.regexexpress;

import java.awt.*;
import java.awt.geom.Point2D;

import static pro.cyberstudio.regexexpress.Utility.viewSizeMask.*;

/**
 * @author jeffs
 *         File:    Utility
 *         Created: 4/25/2017 @ 7:22 PM
 *         Project: RegexExpress
 */

class Utility {
	
	static void LogMsgln(String msg) {
		System.out.println(msg);
	}
	
	static void LogMsg(String msg) {
		System.out.print(msg);
	}
	
	enum viewSizeMask { min(1), perf(2), max(4), size(8), bounds(16);
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
		
		if ((which & min.value) > 0) {
			sb.append("  Min Size: ").append(dispVal(c.getMinimumSize()));
			sb.append("\n");
		}
		
		if ((which & perf.value) > 0) {
			sb.append(" Pref Size: ").append(dispVal(c.getPreferredSize()));
			sb.append("\n");
		}
		
		if ((which & max.value) > 0) {
			sb.append("  Max Size: ").append(dispVal(c.getMaximumSize()));
			sb.append("\n");
		}
		
		if ((which & size.value) > 0) {
			sb.append("      Size: ").append(dispVal(c.getSize()));
			sb.append("\n");
		}
		
		if ((which & bounds.value) > 0) {
			sb.append("    Bounds: ").append(dispVal(c.getBounds()));
			sb.append("\n");
		}
		
		
		return sb.toString();
	}
	
	static String dispVal(Point p)  {
		return dispVal(p.getX(), p.getY());
	}
	
	static String dispVal(Point2D.Double p)  {
		return dispVal(p.getX(), p.getY());
	}
	
	static String dispVal(Dimension2dx dim) {
		return dispVal(dim.width, dim.height);
	}
	
	static String dispVal(Dimension dim) {
		return dispVal(dim.width, dim.height);
	}
	
	static String dispVal(double x, double y) { return "x: " + x + " y: " + y; }
	
	static String dispVal(int x, int y) { return "x: " + x + " y: " + y; }
	
	static String dispVal(Rectangle rect) {
		return "x: " + rect.x + " y: " + rect.y + " w: " + rect.width + " h: " + rect.height;
	}
	
}
