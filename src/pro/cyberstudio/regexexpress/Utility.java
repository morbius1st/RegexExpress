package pro.cyberstudio.regexexpress;

import org.jetbrains.annotations.*;

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
	
	static void listVPInfo(String name) {
		LogMsgFmtln("*** from: ", name);
		
		if (RegexExpress.regexViewport == null) return;
		
		LogMsgFmtln("vp vis size: ", dispVal(RegexExpress.regexViewport.getVisibleRect()));
		LogMsgFmtln("vp view rect: ", dispVal(RegexExpress.regexViewport.getViewRect()));
		LogMsgFmtln("vp view pos: ", dispVal(RegexExpress.regexViewport.getViewPosition()));
		LogMsgFmtln("vp loc: ", dispVal(RegexExpress.regexViewport.getLocation()));
		LogMsgFmtln("vp ext size: ", dispVal(RegexExpress.regexViewport.getExtentSize()));
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

	
	static void LogMsgln(String msg) { System.out.println(msg); }
	
	static void LogMsg(String msg) {System.out.print(msg); }
	
	static void LogMsgFmtln(String msg1, String msg2) {
		if (msg2 == null) {msg2 = "";}
		LogMsgln(String.format("%1$30s%2$s", msg1, msg2));
	}
	
	static void LogMsgFmtln(String msg, Point pt) { LogMsgFmtln(msg, dispVal(pt)); }
	
	static void LogMsgFmtln(String msg, Point2D.Double pt) { LogMsgFmtln(msg, dispVal(pt)); }
	
	static void LogMsgFmtln(String msg, Dimension2dx dim) { LogMsgFmtln(msg, dispVal(dim)); }
	
	static void LogMsgFmtln(String msg, Dimension dim) { LogMsgFmtln(msg, dispVal(dim)); }
	
	static void LogMsgFmtln(String msg, double x, double y) { LogMsgFmtln(msg, dispVal(x, y)); }
	
	static void LogMsgFmtln(String msg, int x, int y) { LogMsgFmtln(msg, dispVal(x, y)); }
	
	static void LogMsgFmtln(String msg, Rectangle rect) { LogMsgFmtln(msg, dispVal(rect)); }
	
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
	static String dispVal(double x, double y) { return "x: " + x + " y: " + y; }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(int x, int y) { return "x: " + x + " y: " + y; }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(Rectangle rect) { return "x: " + rect.x + " y: " + rect.y + " w: " + rect.width + " h: " + rect.height; }
	
}
