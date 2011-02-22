//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/tools/SpringUtilities.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SpringUtilities.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: SpringUtilities.java,v $
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:32  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.tools;

import javax.swing.*;
import javax.swing.SpringLayout;
import java.awt.*;

/**
 * A 1.4 file that provides utility methods for
 * creating form- or grid-style layouts with SpringLayout.
 * Note: code copied from the sun website and modified.
 *
 * @author CLAISSE
 */
public class SpringUtilities
{

	/* Used by makeCompactGrid. */
	private static SpringLayout.Constraints getConstraintsForCell(int row , int col ,
	                                                              Container parent ,
	                                                              int cols)
	{
		SpringLayout layout = ( SpringLayout ) parent.getLayout();
		Component c = parent.getComponent(row * cols + col);
		return layout.getConstraints(c);
	}

	/**
	 * Aligns the first <code>rows</code> * <code>cols</code>
	 * components of <code>parent</code> in
	 * a grid. Each component in a column is as wide as the maximum
	 * preferred width of the components in that column;
	 * height is similarly determined for each row.
	 * The parent is made just big enough to fit them all.
	 *
	 * @param rows     number of rows
	 * @param cols     number of columns
	 * @param initialX x location to start the grid at
	 * @param initialY y location to start the grid at
	 * @param xPad     x padding between cells
	 * @param yPad     y padding between cells
	 */
	public static void makeCompactGrid(Container parent ,
	                                   int rows , int cols ,
	                                   int initialX , int initialY ,
	                                   int xPad , int yPad ,
	                                   boolean makeCellsSameWidth)
	{
		SpringLayout layout;
		try
		{
			layout = ( SpringLayout ) parent.getLayout();
		}
		catch ( ClassCastException exc )
		{
			System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
			return;
		}

		//Align all cells in each column and make them the same width.
		Spring x = Spring.constant(initialX);
		for ( int c = 0 ; c < cols ; c++ )
		{
			Spring width = Spring.constant(0);
			for ( int r = 0 ; r < rows ; r++ )
			{
				width = Spring.max(width ,
				                   getConstraintsForCell(r , c , parent , cols).
				                   getWidth());
			}
			for ( int r = 0 ; r < rows ; r++ )
			{
				SpringLayout.Constraints constraints =
				        getConstraintsForCell(r , c , parent , cols);
				constraints.setX(x);
				if ( makeCellsSameWidth )
				{
					constraints.setWidth(width);
				}
			}
			x = Spring.sum(x , Spring.sum(width , Spring.constant(xPad)));
		}

		//Align all cells in each row and make them the same height.
		Spring y = Spring.constant(initialY);
		for ( int r = 0 ; r < rows ; r++ )
		{
			Spring height = Spring.constant(0);
			for ( int c = 0 ; c < cols ; c++ )
			{
				height = Spring.max(height ,
				                    getConstraintsForCell(r , c , parent , cols).
				                    getHeight());
			}
			for ( int c = 0 ; c < cols ; c++ )
			{
				SpringLayout.Constraints constraints =
				        getConstraintsForCell(r , c , parent , cols);
				constraints.setY(y);
				constraints.setHeight(height);
			}
			y = Spring.sum(y , Spring.sum(height , Spring.constant(yPad)));
		}

		//Set the parent's size.
		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH , y);
		pCons.setConstraint(SpringLayout.EAST , x);
	}
}
