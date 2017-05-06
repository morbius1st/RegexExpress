package pro.cyberstudio.regexexpress;

import java.awt.event.MouseEvent;

/**
 * @author jeffs
 *         File:    iMouseListener
 *         Created: 5/1/2017 @ 9:51 PM
 *         Project: RegexExpress
 */

interface iMMListener extends iListener {
	void mouseMoved(MouseEvent e);
	void mouseDragged(MouseEvent e);
}
