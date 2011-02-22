//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/tango/TangoAlternateSelectionManagerFactory.java,v $
//
//Project:      TangoAlternateSelection Archiving Service
//
//Description:  Java source code for the class  TangoAlternateSelectionManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.3 $
//
//$Log: TangoAlternateSelectionManagerFactory.java,v $
//Revision 1.3  2007/03/15 14:26:35  ounsy
//corrected the table mode add bug and added domains buffer
//
//Revision 1.3  2006/11/23 10:04:31  ounsy
//refactroring
//
//Revision 1.2  2006/10/17 14:33:34  ounsy
//minor changes
//
//Revision 1.1  2006/09/20 12:47:32  ounsy
//moved from mambo.datasources.tango
//
//Revision 1.5  2006/09/19 13:45:38  ounsy
//added a threaded mode
//
//Revision 1.4  2006/09/14 11:29:29  ounsy
//complete buffering refactoring
//
//Revision 1.3  2006/05/29 15:48:01  ounsy
//added an unbuffered but ordered type of implementation
//
//Revision 1.2  2006/05/17 09:27:20  ounsy
//added BufferedTangoAlternateSelectionManagerImpl to the list of possible implementations
//
//Revision 1.1  2005/11/29 18:28:12  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.datasources.tango;


public class TangoAlternateSelectionManagerFactory
{
    public static final int DUMMY = 1;
    public static final int BASIC = 2;
    public static final int BUFFERED = 3;
    public static final int ORDERED = 4;
    public static final int BUFFERED_AND_ORDERED = 5;

    private static ITangoAlternateSelectionManager currentImpl = null;
    //private static int currentImplType;
    
    private static boolean canBeBuffered = true;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static ITangoAlternateSelectionManager getImpl ( int typeOfImpl )
    {
        //currentImplType = typeOfImpl;
        
        switch ( typeOfImpl )
        {
            case DUMMY:
                currentImpl = new DummyTangoAlternateSelectionManagerImpl();
            break;

            case BASIC:
                currentImpl = new BasicTangoAlternateSelectionManager ();
            break;
            
            case ORDERED:
                currentImpl = new OrderedTangoAlternateSelectionManager ( new BasicTangoAlternateSelectionManager () );
            break;

            case BUFFERED:
                if ( canBeBuffered )
                {
                    currentImpl = new ThreadedBufferedTangoAlternateSelectionManager ( new BasicTangoAlternateSelectionManager () );
                    //currentImpl = new MoreThreadedBufferedTangoAlternateSelectionManager ( new BasicTangoAlternateSelectionManager () );
                }
                else
                {
                    currentImpl = new BasicTangoAlternateSelectionManager ();
                }  
            break;
            
            case BUFFERED_AND_ORDERED:
                if ( canBeBuffered )
                {
                    currentImpl = new OrderedTangoAlternateSelectionManager ( new ThreadedBufferedTangoAlternateSelectionManager ( new BasicTangoAlternateSelectionManager () ) );
                    //currentImpl = new OrderedTangoAlternateSelectionManager ( new MoreThreadedBufferedTangoAlternateSelectionManager ( new BasicTangoAlternateSelectionManager () ) );
                }
                else
                {
                    currentImpl = new OrderedTangoAlternateSelectionManager ( new BasicTangoAlternateSelectionManager () );
                }  
            break;
            
            default:
                throw new IllegalStateException( "Expected either DUMMY(1) or BASIC(2) or BUFFERED(3) or ORDERED(4) or BUFFERED_AND_ORDERED(5), got " + typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * @return 28 juin 2005
     */
    public static ITangoAlternateSelectionManager getCurrentImpl ()
    {
        return currentImpl;
    }
    
    public static void setBuffered ( boolean _canBeBuffered )
    {
        TangoAlternateSelectionManagerFactory.canBeBuffered = _canBeBuffered;        
        //getImpl ( currentImplType ); 
        //CLA 14/09/06
    }

}
