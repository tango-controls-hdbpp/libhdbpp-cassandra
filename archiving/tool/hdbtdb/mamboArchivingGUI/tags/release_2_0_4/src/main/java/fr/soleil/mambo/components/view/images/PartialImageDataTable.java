package fr.soleil.mambo.components.view.images;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class PartialImageDataTable extends JTable
{
    private String imageName;
    
    public PartialImageDataTable ( Vector partialImageData, String displayFormat )
    {
        super ();
        PartialImageDataTableModel model = new PartialImageDataTableModel ( partialImageData );
        super.setModel ( model );
        
        //this.setDefaultRenderer( Object.class , new PartialImageDataTableRenderer() );
        
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = this.getSelectionModel();
        rowSM.addListSelectionListener ( new ImagesListSelectionListener ( model, displayFormat ) );
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row , int column)
    {
        if ( column != 3 )
        {
            return false;    
        }
        return true;
        //return false;
    }

    public void setImageName(String _imageName) 
    {
        this.imageName = _imageName;
    }

    /**
     * @return Returns the imageName.
     */
    public String getImageName() {
        return imageName;
    }
}
