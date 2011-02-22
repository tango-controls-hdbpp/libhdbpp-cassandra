package fr.soleil.bensikin.models;

import fr.soleil.bensikin.tools.Messages;

public class SpectrumDeltaValueTableModel extends SpectrumWriteValueTableModel
{
    public SpectrumDeltaValueTableModel (int dataType, Object[] spectrumValues)
    {
        super( dataType, spectrumValues );
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int columnIndex)
    {
        if (columnIndex == 0)
        {
            return Messages.getMessage( "DIALOGS_SPECTRUM_ATTRIBUTE_INDEX" );
        }
        if (columnIndex == 1)
        {
            return Messages.getMessage( "DIALOGS_SPECTRUM_ATTRIBUTE_SUBDELTA" );
        }
        return "";
    }
}
