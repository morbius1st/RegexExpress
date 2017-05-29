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
	
	public boolean add(GraphElement graphElement) {
		return layerList.add(graphElement);
	}
	
	public void list() {
		for (GraphElement ge : layerList) {
			ge.draw();
		}
	}
	
}
