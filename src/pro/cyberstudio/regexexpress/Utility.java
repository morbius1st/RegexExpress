package pro.cyberstudio.regexexpress;

import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.geom.*;

import pro.cyberstudio.utilities.log;

/**
 * @author jeffs
 *         File:    Utility
 *         Created: 5/25/2017 @ 6:08 PM
 *         Project: RegexExpress
 */

public class Utility extends log {
	
	
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
	
	
	public static String LogMsgStr(String msg, Dimension2dx dim) 		{ return LogMsgStr(msg, dispVal(dim)); }
	public static void LogMsgFmtln(String msg, Dimension2dx dim) 		{ LogMsgln(LogMsgStr(msg, dim)); }
	
	@NotNull
	@Contract(pure = true)
	public static String dispVal(Dimension2dx dim) { return dispVal(dim.width, dim.height); }
	
	public static String dispVal(Color color) {return "r| " + color.getRed()
			+ " g| " + color.getGreen() + " b| " + color.getBlue() + " a| " + color.getAlpha();}
	
	public static String dispVal(Paint paint) {
		if (paint instanceof Color) {
			Color color = (Color) paint;
			return String.format("r| %1$3s g| %2$3s b| %3$3s a| %4$3s",
					color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		}
		
		return "paint of some kind";
	}
	
	public static String dispVal(String msg, int width) {
		String pattern = "%1$-" + width + "s";
		
		return String.format(pattern, msg);
	}
	
	public static String dispVal(Point2D pt) {
		return FmtXY(pt.getX(), pt.getY());
	}
	
	public static String dispVal(Rectangle2D rect) {
		if (rect == null) {
			return FmtXY(Double.NaN, Double.NaN) + "  " +  FmtWH(Double.NaN, Double.NaN);
		}
		return FmtXY(rect.getX(), rect.getY()) + "  " +  FmtWH(rect.getWidth(), rect.getHeight());
	}
	
	@NotNull
	public static String dispZoom(double zoomFactor) { return "zoom factor| " + zoomFactor; }
	
	
	
	
}
