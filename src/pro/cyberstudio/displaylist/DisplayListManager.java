package pro.cyberstudio.displaylist;

/**
 * @author jeffs
 *         File:    DisplayListManager
 *         Created: 5/25/2017 @ 10:56 PM
 *         Project: RegexExpress
 */

public class DisplayListManager {
	
	private DisplayList layerList;
	
	public DisplayListManager(int initialCapacity) { this.layerList = new DisplayList(initialCapacity); }
	
	public DisplayList getDisplayList() { return layerList; }
	
	public boolean hasElements() { return layerList.size() > 0; }
	
	void addToLayerList(DisplayList dList) {
		if (dList == null) return;
		
		layerList.addAll(dList);
	}
	
	public boolean add(GraphElem graphElem) {
		return layerList.add(graphElem);
	}
	
	public void list() {
		for (GraphElem ge : layerList) {
			ge.draw();
		}
	}
	
	
//
//	public static DisplayList testList() {
//		DisplayList list = new DisplayList(10);
//
//
//		list.add(new GraphicElement("11", "rect1", GraphicType.RECTFILLED, Color.GREEN,
//				null, new Point(30,30), new Size(20, 20), null));
//
//		list.add(new GraphicElement("12", "line1", GraphicType.LINE, Color.RED,
//				null, new Point(20,30), new Size(50, 0), null));
//
//		list.add(new GraphicElement("13", "line2", GraphicType.LINE, Color.BLUE,
//				null, new Point(20,50), new Size(50, 0), null));
//
//		list.add(new GraphicElement("14", "text1", GraphicType.TEXT, Color.ORANGE,
//				null, new Point(30,20), null, "Value"));
//
//		list.add(new GraphicElement("15", "line3", GraphicType.LINE, Color.YELLOW,
//				null, new Point(30,10), new Size(0, 50), null));
//
//		list.add(new GraphicElement("16", "line4", GraphicType.LINE, Color.WHITE,
//				null, new Point(50,10), new Size(00, 50), null));
//
//		list.add(new GraphicElement("17", "line5", GraphicType.LINE, Color.WHITE,
//				null, new Point(-50,-10), new Size(5000, 1000), null));
//
//		return list;
//
//	}
//
}
