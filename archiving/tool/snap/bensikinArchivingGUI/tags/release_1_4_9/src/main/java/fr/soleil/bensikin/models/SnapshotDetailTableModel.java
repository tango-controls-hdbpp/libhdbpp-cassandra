//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/SnapshotDetailTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotDetailTableModel.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.20 $
//
// $Log: SnapshotDetailTableModel.java,v $
// Revision 1.20  2007/08/21 08:44:39  ounsy
// Snapshot Detail : Transfer Read To Write (Mantis bug 5543)
//
// Revision 1.19  2007/07/04 12:39:26  ounsy
// sort by name by default
//
// Revision 1.18  2007/07/03 09:44:22  ounsy
// sorting ok
//
// Revision 1.17  2007/06/29 09:18:58  ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.16  2007/06/28 12:51:24  ounsy
// spectrum and image values represented as arrays
//
// Revision 1.15  2006/10/31 16:54:08  ounsy
// milliseconds and null values management
//
// Revision 1.14  2006/06/28 12:53:20  ounsy
// minor changes
//
// Revision 1.13  2006/04/13 12:36:39  ounsy
// new spectrum types support
//
// Revision 1.12  2006/04/13 12:27:46  ounsy
// added a sort on the snapshot detail table
//
// Revision 1.11  2006/03/20 15:51:04  ounsy
// added the case of Snapshot delta value for spectrums which
// read and write parts are the same length.
//
// Revision 1.10  2006/03/16 16:41:25  ounsy
// taking care of String formating
//
// Revision 1.9  2006/02/23 13:36:09  ounsy
// now the value really changes
//
// Revision 1.8  2006/02/23 10:06:55  ounsy
// notice when modified
//
// Revision 1.7  2006/02/17 13:19:29  ounsy
// commented a useless getDisplayFormat call
//
// Revision 1.6  2006/02/15 09:22:53  ounsy
// spectrums rw management
//
// Revision 1.5  2005/12/14 16:44:50  ounsy
// added a selectAllOrNone method
//
// Revision 1.4  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.models;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotAttributeComparator;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotAttribute;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeReadValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeWriteValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributes;
import fr.soleil.bensikin.tools.Messages;

/**
 * The table model used by SnapshotDetailTable, this model lists the current list of snapshots.
 * Its rows are SnapshotAttribute objects.
 * A static reference Hashtable allows for keeping track of precedently created instances
 * (using as reference the Snapshot the rows of which the table is displaying).
 *
 * @author CLAISSE
 */
public class SnapshotDetailTableModel extends SortedTableModel
{
    private SnapshotAttribute[] rows;
    private static String[] columnsNames;

    private static Hashtable snapshotDetailTableModelHashtable = null;

    private boolean selectToChoose;

    /**
     * Creates a statically referenced instance of model for the given Snapshot.
     *
     * @param snapshot The model of which we're displaying the details
     * @return The instance found for the specified Snapshot reference, or a new one is none exist yet.
     */
    public static SnapshotDetailTableModel getInstance ( Snapshot snapshot )
    {
        if ( snapshotDetailTableModelHashtable == null )
        {
            snapshotDetailTableModelHashtable = new Hashtable();
        }

        if ( snapshotDetailTableModelHashtable.containsKey( snapshot ) )
        {
            return ( SnapshotDetailTableModel ) snapshotDetailTableModelHashtable.get( snapshot );
        }
        else
        {
            SnapshotDetailTableModel newSnapshotDetailTableModel = new SnapshotDetailTableModel();
            newSnapshotDetailTableModel.load( snapshot );
            snapshotDetailTableModelHashtable.put( snapshot , newSnapshotDetailTableModel );
            return newSnapshotDetailTableModel;
        }
    }

    /**
     * Creates a statically referenced instance of model for the given Snapshot.
     *
     * @param snapshot The model of which we're displaying the details
     * @param choose   A boolean to know wheather there should be checkboxes to
     *                 filter the list of actions (usesfull for "set equipments")
     * @return The instance found for the specified Snapshot reference, or a new
     *         one is none exist yet.
     */
    public static SnapshotDetailTableModel getInstance ( Snapshot snapshot , boolean choose )
    {
        if ( snapshotDetailTableModelHashtable == null )
        {
            snapshotDetailTableModelHashtable = new Hashtable();
        }

        if ( snapshotDetailTableModelHashtable.containsKey( snapshot ) )
        {
            return ( SnapshotDetailTableModel ) snapshotDetailTableModelHashtable.get( snapshot );
        }
        else
        {
            SnapshotDetailTableModel newSnapshotDetailTableModel = new SnapshotDetailTableModel( choose );
            newSnapshotDetailTableModel.load( snapshot );
            snapshotDetailTableModelHashtable.put( snapshot , newSnapshotDetailTableModel );
            return newSnapshotDetailTableModel;
        }
    }

    /**
     * Initializes the columns titles.
     */
    private SnapshotDetailTableModel ()
    {

        super();

        selectToChoose = false;

        String msgAttr = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_NAME" );
        String msgW = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_W" );
        String msgR = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_R" );
        String msgDelta = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_DELTA" );


        columnsNames = new String[ this.getColumnCount() ];
        columnsNames[ 0 ] = msgAttr;
        columnsNames[ 1 ] = msgW;
        columnsNames[ 2 ] = msgR;
        columnsNames[ 3 ] = msgDelta;
    }

    /**
     * Initializes the columns titles.
     */
    private SnapshotDetailTableModel ( boolean choose )
    {

        super();

        selectToChoose = choose;

        String msgAttr = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_NAME" );
        String msgW = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_W" );
        String msgR = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_R" );
        String msgDelta = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_DELTA" );
        String msgCheckBox = Messages.getMessage( "SNAPSHOT_DETAIL_COLUMNS_CHECKBOX" );

        columnsNames = new String[ this.getColumnCount() ];
        columnsNames[ 0 ] = msgAttr;
        columnsNames[ 1 ] = msgW;
        columnsNames[ 2 ] = msgR;
        columnsNames[ 3 ] = msgDelta;
        if ( selectToChoose )
        {
            columnsNames[ 4 ] = msgCheckBox;
        }
    }

    /**
     * Builds itself with the list of attributes contained in <code>snapshot</code>.
     *
     * @param snapshot The Snapshot containing the attributes to display
     */
    public void load ( Snapshot snapshot )
    {
        SnapshotAttributes snapshotAttributes = snapshot.getSnapshotAttributes();
        this.rows = snapshotAttributes.getSnapshotAttributes();
        sort(0);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        if ( selectToChoose )
        {
            return 5;
        }
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount ()
    {
        if ( rows == null )
        {
            return 0;
        }

        return rows.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
     */
    public void setValueAt ( Object aValue , int rowIndex , int columnIndex )
    {
        switch ( columnIndex )
        {
            case 1:
                SnapshotAttributeWriteValue writeValue = ( SnapshotAttributeWriteValue ) getValueAt( rowIndex , columnIndex );

                // Test the value validity
                if (writeValue.getDataFormat() == SnapshotAttributeValue.SPECTRUM_DATA_FORMAT)
                {
                    if ( aValue instanceof Double[]
                         || aValue instanceof Float[]
                         || aValue instanceof Integer[]
                         || aValue instanceof Short[]
                         || aValue instanceof Byte[]
                         || aValue instanceof String[]
                         || aValue instanceof Boolean[]
                       )
                    {
                        writeValue.setSpectrumValue((Object[])aValue);
                        writeValue.setModified( true );

                        SnapshotDetailTabbedPane pane = SnapshotDetailTabbedPane.getInstance();
                        SnapshotDetailTabbedPaneContent currentTab = ( SnapshotDetailTabbedPaneContent ) pane.getSelectedComponent();
                        currentTab.setEditCommentDisabled( true );
                        currentTab.setModified( true );
                        return;
                    }
                    else
                    {
                        String title = Messages.getMessage( "MODIFY_SNAPSHOT_INVALID_VALUE_TITLE" );
                        String msg = Messages.getMessage( "MODIFY_SNAPSHOT_INVALID_VALUE_MESSAGE" );
                        JOptionPane.showMessageDialog( null , msg , title , JOptionPane.ERROR_MESSAGE );
                        return;
                    }
                }
                boolean isOKValue = writeValue.validateValue( aValue );
                if ( !isOKValue )
                {
                    String title = Messages.getMessage( "MODIFY_SNAPSHOT_INVALID_VALUE_TITLE" );
                    String msg = Messages.getMessage( "MODIFY_SNAPSHOT_INVALID_VALUE_MESSAGE" );
                    JOptionPane.showMessageDialog( null , msg , title , JOptionPane.ERROR_MESSAGE );

                    return;
                }

                SnapshotAttributeWriteValue afterValue = new SnapshotAttributeWriteValue( writeValue.getDataFormat() , writeValue.getDataType() , writeValue.getValue() );
                afterValue.setDisplayFormat( writeValue.getDisplayFormat() );
                afterValue.setValue( aValue );
                afterValue.setSettable( writeValue.isSettable() );
                if ( ! ( afterValue.toXMLString().equals( writeValue.toXMLString() )
                         || afterValue.toFormatedString().equals( writeValue.toFormatedString() )
                       ) 
                   )
                // Display the value cell and the snapshot tab as modified only if the new value is effectively different from the old one
                {
                    writeValue.setModified( true );

                    SnapshotDetailTabbedPane pane = SnapshotDetailTabbedPane.getInstance();
                    SnapshotDetailTabbedPaneContent currentTab = ( SnapshotDetailTabbedPaneContent ) pane.getSelectedComponent();
                    currentTab.setEditCommentDisabled( true );
                    currentTab.setModified( true );
                }

                writeValue.setValue( aValue );
                rows[ rowIndex ].setWriteValue( writeValue );
                break;

            case 4:
                writeValue = (SnapshotAttributeWriteValue) getValueAt(rowIndex,1);
                if (aValue instanceof Boolean)
                {
                    writeValue.setSettable(((Boolean) aValue).booleanValue());
                    SnapshotDetailTabbedPaneContent currentTab = (SnapshotDetailTabbedPaneContent) SnapshotDetailTabbedPane
                            .getInstance().getSelectedComponent();
                    currentTab.setMayFilter(true);
                }
                break;

            default:
                //do nothing, only write values and selection can be edited
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt ( int rowIndex , int columnIndex )
    {

        switch ( columnIndex )
        {
            case 0:
                return rows[ rowIndex ].getAttribute_complete_name();

            case 1:
                return rows[ rowIndex ].getWriteValue();

            case 2:
                return rows[ rowIndex ].getReadValue();

            case 3:
                return rows[ rowIndex ].getDeltaValue();

            case 4:
                if ( selectToChoose )
                {
                    return new Boolean( rows[ rowIndex ].getWriteValue().isSettable() );
                }

            default:
                return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName ( int columnIndex )
    {
        return columnsNames[ columnIndex ];
    }

    public SnapshotAttribute getSnapshotAttributeAtRow ( int rowIndex )
    {
        if ( rows != null )
        {
            return rows[ rowIndex ];
        }
        else
        {
            return null;
        }
    }
    
    /**
     * @param newValue
     */
    public void selectAllOrNone ( boolean newValue )
    {
        if ( rows == null )
        {
            return;
        }

        boolean hasCausedChanges = false;
        for ( int i = 0 ; i < rows.length ; i++ )
        {
            SnapshotAttribute attr = this.getSnapshotAttributeAtRow( i );
            SnapshotAttributeWriteValue writeValue = attr.getWriteValue ();
            boolean oldValue = writeValue.isSettable ();
            if ( oldValue != newValue )
            {
                hasCausedChanges = true;    
            }
            
            writeValue.setSettable ( newValue );
        }
        this.fireTableRowsUpdated( 0 , this.getRowCount() - 1 );
        
        if ( hasCausedChanges )
        {
            SnapshotDetailTabbedPaneContent currentTab = ( SnapshotDetailTabbedPaneContent ) SnapshotDetailTabbedPane.getInstance().getSelectedComponent();
            currentTab.setMayFilter( true );    
        }
    }

    public void transferSelectedReadToWrite() {
        if (rows == null){
            return;
        }
        for (int i = 0 ; i < rows.length ; i++) {
            SnapshotAttribute attr = this.getSnapshotAttributeAtRow(i);
            SnapshotAttributeReadValue readValue = attr.getReadValue();
            SnapshotAttributeWriteValue writeValue = attr.getWriteValue ();
            if ( writeValue.isSettable()
                    && ( !writeValue.isNotApplicable() )
                    && ( !readValue.isNotApplicable() ) ) {
                switch( attr.getData_format() ) {
                    case AttrDataFormat._SCALAR:
                        Object scalarValue = readValue.getScalarValue();
                        if (scalarValue != null) {
                            setValueAt(scalarValue, i, 1);
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        Object[] spectrumValue = readValue.getSpectrumValue();
                        if (spectrumValue != null) {
                            setValueAt(spectrumValue, i, 1);
                        }
                        break;
                    case AttrDataFormat._IMAGE:
                        Object[] imageValue = readValue.getImageValue();
                        if (imageValue != null) {
                            setValueAt(imageValue, i, 1);
                        }
                        break;
                }
            }
        }
    }

    /**
	 * Sorts the table's lines relative to the specified column.
	 * If the the table is already sorted relative to this column, reverses the sort.
	 *
	 * @param clickedColumnIndex The index of the column to sort the lines by
	 */
	public void sort(int clickedColumnIndex)
	{
		switch ( clickedColumnIndex )
		{
			case 0:
				sortByColumn(SnapshotAttributeComparator.COMPARE_NAME);
                sortedColumn = clickedColumnIndex;
                break;

			case 1:
				sortByColumn(SnapshotAttributeComparator.COMPARE_WRITE_VALUE);
                sortedColumn = clickedColumnIndex;
                break;

			case 2:
				sortByColumn(SnapshotAttributeComparator.COMPARE_READ_VALUE);
                sortedColumn = clickedColumnIndex;
                break;
				
			case 3:
				sortByColumn(SnapshotAttributeComparator.COMPARE_DELTA_VALUE);
                sortedColumn = clickedColumnIndex;
                break;
        }
	}

	/**
	 * Sorts the table's lines relative to the specified field.
	 * If the the table is already sorted relative to this column, reverses the sort.
	 *
	 * @param compareCase The type of field to sort the lines by
	 */
	protected void sortByColumn (int compareCase) {
        int newSortType = SnapshotAttributeComparator
                .getNewSortType( this.idSort );
        Vector<SnapshotAttribute> v = new Vector<SnapshotAttribute>();
        for (int i = 0; i < rows.length; i++) {
            v.add( this.getSnapshotAttributeAtRow( i ) );
        }
        SnapshotAttributeComparator comparator = new SnapshotAttributeComparator(compareCase); 
        if (compareCase == SnapshotAttributeComparator.COMPARE_NAME) {
            Collections.sort(v, comparator);
        }
        else {
            Vector<SnapshotAttribute> notComparableAttrs = new Vector<SnapshotAttribute>();

            Vector<SnapshotAttribute> booleanScalarValues = new Vector<SnapshotAttribute>();
            Vector<SnapshotAttribute> stringScalarValues = new Vector<SnapshotAttribute>();
            Vector<SnapshotAttribute> numberScalarValues = new Vector<SnapshotAttribute>();

            Vector<SnapshotAttribute> booleanSpectrumValues = new Vector<SnapshotAttribute>();
            Vector<SnapshotAttribute> stringSpectrumValues = new Vector<SnapshotAttribute>();
            Vector<SnapshotAttribute> numberSpectrumValues = new Vector<SnapshotAttribute>();

            Vector<SnapshotAttribute> booleanImageValues = new Vector<SnapshotAttribute>();
            Vector<SnapshotAttribute> stringImageValues = new Vector<SnapshotAttribute>();
            Vector<SnapshotAttribute> numberImageValues = new Vector<SnapshotAttribute>();
            // Prepare the collection of null attributes
            int notNull = -1;
            notNull = sortNotComparableAttrFirst(v);
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( v.get(i) );
            }
            int index = notNull;
            while (index > 0) {
                v.remove(0);
                index--;
            }

            for (int i = 0; i < v.size(); i++) {
                SnapshotAttribute attr = v.get(i);
                switch( attr.getData_format() ) {
                    case AttrDataFormat._SCALAR:
                        switch( attr.getData_type() ) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                booleanScalarValues.add(attr);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                stringScalarValues.add(attr);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                numberScalarValues.add(attr);
                                break;
                            default:
                                notComparableAttrs.add(attr);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        switch( attr.getData_type() ) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                booleanSpectrumValues.add(attr);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                stringSpectrumValues.add(attr);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                numberSpectrumValues.add(attr);
                                break;
                            default:
                                notComparableAttrs.add(attr);
                                break;
                        }
                        break;
                    case AttrDataFormat._IMAGE:
                        switch( attr.getData_type() ) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                booleanImageValues.add(attr);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                stringImageValues.add(attr);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                numberImageValues.add(attr);
                                break;
                            default:
                                notComparableAttrs.add(attr);
                                break;
                        }
                        break;
                    default:
                        notComparableAttrs.add(attr);
                        break;
                }
            }

            //classify scalars
            notNull = sortNotComparableValueFirst(
                    booleanScalarValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( booleanScalarValues.get(i) );
            }
            if (booleanScalarValues.size() > 0)
            index = notNull;
            while (index > 0) {
                booleanScalarValues.remove(0);
                index--;
            }

            notNull = sortNotComparableValueFirst(
                    numberScalarValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( numberScalarValues.get(i) );
            }
            if (numberScalarValues.size() > 0)
            index = notNull;
            while (index > 0) {
                numberScalarValues.remove(0);
                index--;
            }

            notNull = sortNotComparableValueFirst(
                    stringScalarValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( stringScalarValues.get(i) );
            }
            if (stringScalarValues.size() > 0)
            index = notNull;
            while (index > 0) {
                stringScalarValues.remove(0);
                index--;
            }



            //classify spectrums
            notNull = sortNotComparableValueFirst(
                    booleanSpectrumValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( booleanSpectrumValues.get(i) );
            }
            index = notNull;
            while (index > 0) {
                booleanSpectrumValues.remove(0);
                index--;
            }

            notNull = sortNotComparableValueFirst(
                    numberSpectrumValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( numberSpectrumValues.get(i) );
            }
            index = notNull;
            while (index > 0) {
                numberSpectrumValues.remove(0);
                index--;
            }

            notNull = sortNotComparableValueFirst(
                    stringSpectrumValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( stringSpectrumValues.get(i) );
            }
            index = notNull;
            while (index > 0) {
                stringSpectrumValues.remove(0);
                index--;
            }



            //classify images
            notNull = sortNotComparableValueFirst(
                    booleanImageValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( booleanImageValues.get(i) );
            }
            index = notNull;
            while (index > 0) {
                booleanImageValues.remove(0);
                index--;
            }

            notNull = sortNotComparableValueFirst(
                    numberImageValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( numberImageValues.get(i) );
            }
            index = notNull;
            while (index > 0) {
                numberImageValues.remove(0);
                index--;
            }

            notNull = sortNotComparableValueFirst(
                    stringImageValues,
                    compareCase
            );
            for (int i = 0; i < notNull; i++) {
                notComparableAttrs.add( stringImageValues.get(i) );
            }
            index = notNull;
            while (index > 0) {
                stringImageValues.remove(0);
                index--;
            }


            //sort scalars
            Collections.sort(booleanScalarValues , comparator);
            Collections.sort(numberScalarValues , comparator);
            Collections.sort(stringScalarValues , comparator);

            //sort spectrums
            Collections.sort(booleanSpectrumValues , comparator);
            Collections.sort(numberSpectrumValues , comparator);
            Collections.sort(stringSpectrumValues , comparator);

            //sort images
            Collections.sort(booleanImageValues , comparator);
            Collections.sort(numberScalarValues , comparator);
            Collections.sort(stringScalarValues , comparator);

            v.clear();
            v.addAll(notComparableAttrs);
            v.addAll(booleanScalarValues);
            v.addAll(numberScalarValues);
            v.addAll(stringScalarValues);
            v.addAll(booleanSpectrumValues);
            v.addAll(numberSpectrumValues);
            v.addAll(stringSpectrumValues);
            v.addAll(booleanImageValues);
            v.addAll(numberImageValues);
            v.addAll(stringImageValues);
        }
        if ( newSortType == SnapshotAttributeComparator.SORT_DOWN ) {
            Collections.reverse( v );
        }
        SnapshotAttribute[] newRows = new SnapshotAttribute[rows.length];
        Enumeration enumer = v.elements();
        int i = 0;
        while (enumer.hasMoreElements()) {
            newRows[i] = (SnapshotAttribute) enumer.nextElement();
            i++;
        }
        this.rows = newRows;
        this.fireTableDataChanged();
        this.idSort = newSortType;
    }

    // Puts null attributes at the begining.
    // Returns the index of the first not null attribute
    protected int sortNotComparableAttrFirst(Vector<SnapshotAttribute> attrs) {
        Vector<SnapshotAttribute> sorted = new Vector<SnapshotAttribute>();
        int index = 0;
        for (int i = 0; i < attrs.size(); i++) {
            SnapshotAttribute attr = attrs.get(i);
            if ( attr == null || attr.getData_format() == SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT ) {
                sorted.add(attrs.get(i));
                index++;
            }
        }
        for (int i = 0; i < attrs.size(); i++) {
            SnapshotAttribute attr = attrs.get(i);
            if ( attr != null && attr.getData_format()!= SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT ) {
                sorted.add(attrs.get(i));
            }
        }
        attrs.clear();
        attrs.addAll(sorted);
        sorted.clear();
        sorted = null;
        return index;
    }

    // Puts null attribute values at the begining.
    // Returns the index of the first not null attribute value
    protected int sortNotComparableValueFirst(Vector<SnapshotAttribute> comps, int valueType) {
        Vector<SnapshotAttribute> sorted = new Vector<SnapshotAttribute>();
        int index = 0;
        for (int i = 0; i < comps.size(); i++) {
            SnapshotAttribute attr = comps.get(i);
            SnapshotAttributeValue value = null;
            switch (valueType) {
                case SnapshotAttributeComparator.COMPARE_READ_VALUE:
                    value = attr.getReadValue();
                    break;
                case SnapshotAttributeComparator.COMPARE_WRITE_VALUE:
                    value = attr.getWriteValue();
                    break;
                case SnapshotAttributeComparator.COMPARE_DELTA_VALUE:
                    value = attr.getDeltaValue();
                    break;
            }
            if ( ( value.getDataFormat() == SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT
                   || value.isNotApplicable()
                 )
                 || (value.getScalarValue() == null
                     && value.getSpectrumValue() == null
                     && value.getImageValue() == null ) ) {
                sorted.add(comps.get(i));
                index++;
            }
            attr = null;
            value = null;
        }
        for (int i = 0; i < comps.size(); i++) {
            SnapshotAttribute attr = comps.get(i);
            SnapshotAttributeValue value = null;
            switch (valueType) {
                case SnapshotAttributeComparator.COMPARE_READ_VALUE:
                    value = attr.getReadValue();
                    break;
                case SnapshotAttributeComparator.COMPARE_WRITE_VALUE:
                    value = attr.getWriteValue();
                    break;
                case SnapshotAttributeComparator.COMPARE_DELTA_VALUE:
                    value = attr.getDeltaValue();
                    break;
            }
            if ( ( value.getDataFormat() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT
                   && !value.isNotApplicable()
                 )
                 && ( value.getScalarValue() != null
                     || value.getSpectrumValue() != null
                     || value.getImageValue() != null ) ) {
                sorted.add(comps.get(i));
            }
            attr = null;
            value = null;
        }
        comps.clear();
        comps.addAll(sorted);
        sorted.clear();
        sorted = null;
        return index;
    }
}
