package pro.cyberstudio.displaylist;

/**
 * @author jeffs
 *         File:    Element
 *         Created: 5/27/2017 @ 2:57 PM
 *         Project: RegexExpress
 */

// this is the root class for the display elements
abstract class Element {
	
	private ID id;
	private ElementType elementType;
	
	private static int indexID = 0;
	
	Element() {}
	
	Element(ElementType elementType, GraphElemType graphElemType) {
		this.elementType = elementType;
		
		this.id = new ID(elementType, graphElemType);
	}
	
	String ID() {
		return id.getID();
	}
	
	void setID(ID id) { this.id = id; }
	
	enum ElementType {
		LayerDefault	("LD"),
		GraphicElement	("GE");
		
		private String elementType;
		
		ElementType(String elementType) {
			this.elementType = elementType;
		}
		
		public String getElementType() {
			return elementType;
		}
		
	}
	
	enum GraphElemType {
		UNDEFINED,
		LINE,
		RECT,
		RECTFILLED,
		STRING
	}
	
	class ID {
		
		String elemType;
		String SubElemType;
		int index;
		
		ID(ElementType elementType, GraphElemType graphElementType) {
			elemType = elementType.getElementType();
			SubElemType = graphElementType.name();
			index = indexID++;
		}
		
		String getID() {
			return String.format("%1$s.%2$010d.%3$s", elemType,
					index, SubElemType );
		}
	}
	
}
