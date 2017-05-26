package pro.cyberstudio.regexexpress;

import org.jetbrains.annotations.*;

import pro.cyberstudio.utilities.log;

/**
 * @author jeffs
 *         File:    Utility
 *         Created: 5/25/2017 @ 6:08 PM
 *         Project: RegexExpress
 */

class Utility extends log {
	
	
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
	
	
	static String LogMsgStr(String msg, Dimension2dx dim) 		{ return LogMsgStr(msg, dispVal(dim)); }
	static void LogMsgFmtln(String msg, Dimension2dx dim) 		{ LogMsgln(LogMsgStr(msg, dim)); }
	
	@NotNull
	@Contract(pure = true)
	static String dispVal(Dimension2dx dim) { return dispVal(dim.width, dim.height); }
	
	@NotNull
	static String dispZoom(double zoomFactor) { return "zoom factor| " + zoomFactor; }
	
	
}
