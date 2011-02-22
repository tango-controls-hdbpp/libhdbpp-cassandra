package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import fr.soleil.bensikin.data.snapshot.SnapshotComparison;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;


public class SnapshotCompareTableHeaderRenderer extends
		DefaultTableCellRenderer {

	private TableCellRenderer renderer;

	public SnapshotCompareTableHeaderRenderer(TableCellRenderer renderer) {
		super();
		this.renderer = renderer;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
        Component ret = renderer.getTableCellRendererComponent( table , value , isSelected , hasFocus , row , column );

        int mcol = table.convertColumnIndexToModel(column); 
        Color color = this.getColumnColor ( mcol );
        ret.setBackground( color );
        
        if ( isSelected )
        {
            ret.setBackground( Color.GRAY );
            ret.setForeground( Color.WHITE );
        }
        
        ret.repaint();

        return ret;
	}
    
    private Color getColumnColor(int column) 
    {
        SnapshotCompareTablePrintModel tableModel = SnapshotCompareTablePrintModel.getInstance ();
        Color ret;
        int comparisonType = tableModel.getSnapshotType ( column );
        switch ( comparisonType )
        {
            case SnapshotComparison.SNAPSHOT_TYPE_1 :
                ret = SnapshotComparison.getFirstSnapshotColor ();
            break;
            
            case SnapshotComparison.SNAPSHOT_TYPE_2 :
                ret = SnapshotComparison.getSecondSnapshotColor ();
            break;
            
            case SnapshotComparison.SNAPSHOT_TYPE_DIFF :
                ret = SnapshotComparison.getDiffSnapshotColor ();
            break;
            
            default:
                ret = Color.GRAY; 
        }
        
        return ret;
    }
}
