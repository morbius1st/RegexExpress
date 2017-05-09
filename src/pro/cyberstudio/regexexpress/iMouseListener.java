package pro.cyberstudio.regexexpress;

import java.awt.event.MouseEvent;

/**
 * @author jeffs
 *         File:    iMouseListener
 *         Created: 5/1/2017 @ 9:51 PM
 *         Project: RegexExpress
 */

interface iMouseListener extends iListener {
	void mouseClicked(MouseEvent e);
	void mousePressed(MouseEvent e);
	void mouseReleased(MouseEvent e);
}
