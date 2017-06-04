package pro.cyberstudio.displaylist;

/**
 * @author jeffs
 *         File:    Element
 *         Created: 5/27/2017 @ 2:57 PM
 *         Project: RegexExpress
 */

// this is the root class for the display elements
abstract class Element {
	
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
		
		ID(ElementType elementType, GraphElemType subElementType) {
			elemType = elementType.getElementType();
			SubElemType = subElementType.name();
			index = indexID++;
		}
		
		String getID() {
			return String.format("%1$s.%2$010d.%3$s", elemType,
					index, SubElemType );
		}
	}
	
	private ID id;
	
	private static int indexID = 0;
	
	Element(ElementType elementType, GraphElemType subElementType) {
		id = new ID(elementType, subElementType);
	}
	
	String ID() {
		return id.getID();
	}
	
}
