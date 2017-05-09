package pro.cyberstudio.regexexpress;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

/**
 * @author jeffs
 *         File:    Dimension2dx
 *         Created: 4/26/2017 @ 8:28 PM
 *         Project: RegexExpress
 */

class Dimension2dx extends Dimension2D {
	
	double height; // same as x
	double width;  // same as y
	
	public Dimension2dx() {
		width = Double.NaN;
		height = Double.NaN;
	}
	
	public Dimension2dx(double w, double h) {
		width = w;
		height = h;
	}
	
	public Dimension2dx(Dimension d) {
		width = d.getWidth();
		height = d.getHeight();
		
	}
	
	@Override
	public double getWidth() {
		return width;
	}
	
	public double getX() {return width; }
	
	@Override
	public double getHeight() {
		return height;
	}
	
	public double getY() {return height; }
	
	@Override
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}

	void setSize(int w, int h) { width = (double) w; height = (double) h; }
	
	void setSize(Dimension2dx d) { width = d.width; height = d.height; }
	
	void setSize(Dimension d) { width = (double) d.width; height = (double) d.height; }
	
	Dimension2dx getSize() {
		return this;
	}
	
	Dimension toDimension() {
		return new Dimension((int) width, (int) height);
	}
	
	static Dimension2dx toDimension2dx(Dimension d) {
		return new Dimension2dx(d);
	}
	
	Dimension2dx multiply(double x) { return multiply(x, x); }
	
	Dimension2dx multiply(double w, double h) { return new Dimension2dx(width * w, height * h); }
	
	Dimension2dx divide(double x) { return divide(x, x); }
	
	Dimension2dx divide(double w, double h) {  return new Dimension2dx(width / w, height / h); }

	boolean eitherLessThan(Dimension2dx d) { return (width < d.width) || (height < d.height); }
	
	boolean bothLessThan(Dimension2dx d) { return (width < d.width) && (height < d.height); }
	
	boolean eitherGreaterThan(Dimension2dx d) { return (width > d.width) || (height > d.height); }
	
	boolean bothGreaterThan(Dimension2dx d) { return (width > d.width) && (height > d.height); }
	
	boolean eitherGreaterThanOrEqual(Dimension2dx d) { return (width >= d.width) || (height >= d.height); }
	
	
	@Override
	public Dimension2dx clone() {
		return new Dimension2dx(width, height);
	}
	
	public boolean equals(Object obj) {
		
		if (obj instanceof Dimension2dx) {
			return ((Dimension2dx) obj).width == width &&
					((Dimension2dx) obj).height == height;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		double sum = width + height;
		
		return (int) (sum * (sum + 1)/2 + width);
		
	}
	
	@Override
	public String toString() {
		if (Double.isNaN(width) || Double.isNaN(width) ||
				Double.isInfinite(width) || Double.isInfinite(width)) {
			return "";
		}
		return String.format("width: %1$d  height: %2$d", width, height);
	}
}
