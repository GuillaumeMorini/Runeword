/**
 * 
 */
package RuneWord;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author U205340
 *
 */
public class ModeleTable extends AbstractTableModel implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String[] columnNames;
	protected Object[][] data;
	protected RuneWord runeWord;
 	
	public ModeleTable( RuneWord rw, Object[][] lData, String[] lColumnNames ) {
		super();
		runeWord = rw;
		data = lData;
		columnNames = lColumnNames; 
	}

    // By default forward all events to all the listeners. 
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }

    public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}
	
	public String getColumnName( int col ) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass( int c ) {
		return getValueAt( 0, c ).getClass();
	}
	
	public void setData( Object[][] lData ) {
		data = lData;
		this.fireTableDataChanged();
	}

}
