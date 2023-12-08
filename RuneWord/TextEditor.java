/**
 * 
 */
package RuneWord;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * @author U205340
 *
 */
public class TextEditor extends AbstractCellEditor implements TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField text;;
	
	public TextEditor() {
		
	}

	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		text = new JTextField( (String) arg1 );
		return (Component) text;
	}

	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return text;
	}
	
	
}
