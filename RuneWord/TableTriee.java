package RuneWord;

/**
 * A sorter for TableModels. The sorter has a model (conforming to TableModel) 
 * and itself implements TableModel. TableSorter does not store or copy 
 * the data in the TableModel, instead it maintains an array of 
 * integers which it keeps the same size as the number of rows in its 
 * model. When the model changes it notifies the sorter that something 
 * has changed eg. "rowsAdded" so that its internal array of integers 
 * can be reallocated. As requests are made of the sorter (like 
 * getValueAt(row, col) it redirects them to its model via the mapping 
 * array. That way the TableSorter appears to hold another copy of the table 
 * with the rows in a different order. The sorting algorthm used is stable 
 * which means that it does not move around rows when its comparison 
 * function returns 0 to denote that they are equivalent. 
 *
 * @version 1.15 11/17/05
 * @author Philip Milne
 */

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class TableTriee extends ModeleTable
{
	private static final long serialVersionUID = 1L;
	boolean ascending = true;
	boolean sorted = false;
	int column;
	public int indexes[];
	
	public TableTriee( RuneWord rw, Object[][] lData, String[] lColumnNames ) {
		super( rw, lData, lColumnNames);
		runeWord = rw;
		indexes = new int[lData.length];      
		reallocateIndexes();
	}

	public int compareRowsByColumn(int row1, int row2, int column)
	{
		Class type = getColumnClass(column);
		
		// Check for nulls
		Object o1 = getValueAt(row1, column);
		Object o2 = getValueAt(row2, column); 
		
		// If both values are null return 0
		if (o1 == null && o2 == null) {
			return 0; 
		}
		else if (o1 == null) { // Define null less than everything. 
			return -1; 
		} 
		else if (o2 == null) { 
			return 1; 
		}
		
		/* We copy all returned values from the getValue call in case
		 an optimised model is reusing one object to return many values.
		 The Number subclasses in the JDK are immutable and so will not be used in 
		 this way but other subclasses of Number might want to do this to save 
		 space and avoid unnecessary heap allocation. 
		 */
		if (type.getSuperclass() == java.lang.Number.class) {
			Number n1 = (Number)getValueAt(row1, column);
			double d1 = n1.doubleValue();
			Number n2 = (Number)getValueAt(row2, column);
			double d2 = n2.doubleValue();
			
			if (d1 < d2)
				return -1;
			else if (d1 > d2)
				return 1;
			else
				return 0;
		} else if (type == java.util.Date.class) {
			Date d1 = (Date)getValueAt(row1, column);
			long n1 = d1.getTime();
			Date d2 = (Date)getValueAt(row2, column);
			long n2 = d2.getTime();
			
			if (n1 < n2)
				return -1;
			else if (n1 > n2)
				return 1;
			else return 0;
		} else if (type == String.class) {
			String s1 = (String)getValueAt(row1, column);
			String s2    = (String)getValueAt(row2, column);
			int result = s1.compareTo(s2);
			
			if (result < 0)
				return -1;
			else if (result > 0)
				return 1;
			else return 0;
		} else if (type == Boolean.class) {
			Boolean bool1 = (Boolean)getValueAt(row1, column);
			boolean b1 = bool1.booleanValue();
			Boolean bool2 = (Boolean)getValueAt(row2, column);
			boolean b2 = bool2.booleanValue();
			
			if (b1 == b2)
				return 0;
			else if (b1) // Define false < true
				return 1;
			else
				return -1;
		} else {
			Object v1 = getValueAt(row1, column);
			String s1 = v1.toString();
			Object v2 = getValueAt(row2, column);
			String s2 = v2.toString();
			int result = s1.compareTo(s2);
			
			if (result < 0)
				return -1;
			else if (result > 0)
				return 1;
			else return 0;
		}
	}
	
	public int compare( int row1, int row2, int column ) {
		int result = compareRowsByColumn(row1, row2, column);
		return ascending ? result : -result;
	}
	
	public void  reallocateIndexes()
	{
		int rowCount = getRowCount();
		
		// Set up a new array of indexes with the right number of elements
		// for the new data model.
		indexes = new int[rowCount];
		
		// Initialise with the identity mapping.
		for(int row = 0; row < rowCount; row++)
			indexes[row] = row;
	}
	
	public void  sort(int column)
	{
		bubblesort( column );
	}
	
	public void  sort()
	{
		if ( sorted ) {
			sortByColumn( column, ascending );
		}
	}
	
	public void bubblesort( int column ) {
		for(int i = 0; i < getRowCount(); i++) {
			for(int j = 1; j < getRowCount() - i; j++) {
				if ( compare( j-1, j, column ) == 1 ) {
					echange( j-1, j );
				}
			}
		}
	}
	
	public void echange(int i, int j) {
		int tmp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = tmp;
	}
	
	// The mapping only affects the contents of the data rows.
	// Pass all requests to these rows through the mapping array: "indexes".
	public Object getValueAt(int aRow, int aColumn)
	{
		return super.getValueAt(indexes[aRow], aColumn);
	}
	
	public void setValueAt(Object aValue, int aRow, int aColumn)
	{
		super.setValueAt(aValue, indexes[aRow], aColumn);
		
	}

	public void sortByColumn(int column, boolean ascending) {
		this.ascending = ascending;
		this.column = column;
		sort( column );
		super.setData( data );
		adapteTailleColonne();
	}
	
	// There is no-where else to put this. 
	// Add a mouse listener to the Table to trigger a table sort 
	// when a column heading is clicked in the JTable. 
	public void addMouseListenerToHeaderInTable(JTable table) { 
		final TableTriee sorter = this; 
		final JTable tableView = table; 
		tableView.setColumnSelectionAllowed(false); 
		MouseAdapter listMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TableColumnModel columnModel = tableView.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX()); 
				int column = tableView.convertColumnIndexToModel(viewColumn); 
				if(e.getClickCount() == 1 && column != -1) {
					int shiftPressed = e.getModifiers()&InputEvent.SHIFT_MASK; 
					ascending = (shiftPressed == 0); 
					sorter.sorted = true;
					sorter.sortByColumn(column, ascending); 
				}
			}
		};
		JTableHeader th = tableView.getTableHeader(); 
		th.addMouseListener( listMouseListener );
	}

	public void adapteTailleColonne() {
		for ( int i=0; i<data.length; i++ ) {
			String s = (String) getValueAt( i, 5 );
			int nb = 0;
			for ( int j=0; j<s.length(); j++ ) {
				if ( s.charAt( j ) == '\n' ) {
					nb++;
				}
			}
			runeWord.resultTable.setRowHeight( i, nb * 16 + 18 );
		}
	}

	public void setData( Object[][] lData ) {
		super.setData( lData );
		reallocateIndexes();    	
		adapteTailleColonne();
	}
}
