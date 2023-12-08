/**
 * 
 */
package RuneWord;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * @author U205340
 *
 */
public class TextRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object texte, boolean isSelected, boolean hasFocus, int row, int column) {
		String s = (String) texte;

		setEditable( false );
		setText( s );

		if ( isSelected ) {
			this.setBackground( table.getSelectionBackground() );
			this.setForeground( table.getSelectionForeground() );
		} else {
			this.setBackground( table.getBackground() );
			this.setForeground( table.getForeground() );
		}
		return this;
	}
}
