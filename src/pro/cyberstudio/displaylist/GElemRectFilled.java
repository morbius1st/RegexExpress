package pro.cyberstudio.displaylist;

import java.awt.*;

import static pro.cyberstudio.regexexpress.Utility.dispVal;
import static pro.cyberstudio.utilities.log.*;

/**
 * @author jeffs
 *         File:    dlItemFilledRect
 *         Created: 5/27/2017 @ 9:01 AM
 *         Project: RegexExpress
 */

public class GElemRectFilled extends GElemRect {
	
	private Color colorFill;
	
	public GElemRectFilled(Point inserPt, Dimension size, double rotation,
						   Color color, BasicStroke stroke, Color colorFill) {
		
		super(inserPt, size, rotation, GraphicType.RECTFILLED, color, stroke);
		
		this.colorFill = colorFill;
	}
	
//	public void draw(Graphics2D g2) {
	public void draw() {
		LogMsgFmtln("this is a rect filled|",
				GraphElem.listElemInfo(ID(), paint, getInsertPt()));
	}
}
