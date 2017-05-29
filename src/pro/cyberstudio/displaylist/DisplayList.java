package pro.cyberstudio.displaylist;

import java.util.ArrayList;

/**
 * @author jeffs
 *         File:    DisplayList
 *         Created: 5/25/2017 @ 10:51 PM
 *         Project: RegexExpress
 */

class DisplayList extends ArrayList<GraphElem> {
	
	DisplayList(int initCapacity) { super.ensureCapacity(initCapacity); }

	void addOne(GraphElem item) { super.add(item); }

}
