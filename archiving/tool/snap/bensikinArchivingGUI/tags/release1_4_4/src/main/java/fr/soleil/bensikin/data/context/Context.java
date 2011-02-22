//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/Context.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Context.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.16 $
//
// $Log: Context.java,v $
// Revision 1.16  2007/08/24 14:08:39  ounsy
// bug correction with context printing as text + Context attributes ordering (Mantis bug 3912)
//
// Revision 1.15  2007/08/23 15:28:48  ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.14  2007/08/23 12:58:41  ounsy
// minor changes
//
// Revision 1.13  2006/11/29 10:00:26  ounsy
// minor changes
//
// Revision 1.12  2006/03/20 15:49:25  ounsy
// removed useless logs
//
// Revision 1.11  2006/03/14 13:06:18  ounsy
// removed useless logs
//
// Revision 1.10  2006/01/13 13:25:23  ounsy
// File replacement warning
//
// Revision 1.9  2006/01/13 13:00:19  ounsy
// avoiding NullPointerException
//
// Revision 1.8  2006/01/12 12:57:21  ounsy
// table reset when necessary
//
// Revision 1.7  2005/12/14 16:32:33  ounsy
// added methods necessary for snapshots comparison with current state
//
// Revision 1.6  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:37  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.context;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.comparators.BensikinAttributesComparator;
import fr.soleil.bensikin.components.context.ContextFileFilter;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.list.ContextListTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.data.context.manager.IContextManager;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.datasources.snapdb.SnapManagerFactory;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.AttributesSelectTableModel;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.models.ContextListTableModel;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeHeavy;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;

/**
 * A pivotal class of Bensikin, this class is the upper level modelisation of a context.
 * <p/>
 * A Context object has two main attributes:
 * - a ContextData attribute representing all non-attribute-dependant information
 * - a ContextAttributes attribute representing all attribute-dependant information
 * It also has a Snapshot[] attribute, representing the Snapshots loaded for this context.
 * <p/>
 * The Context class also saves static references to the currently opened contexts, and the selected context.
 *
 * @author CLAISSE
 */
public class Context
{
    private Snapshot[] snapshots;
    private ContextData contextData;
    private ContextAttributes contextAttributes;

    private boolean isModified = true;
    
    private static Context selectedContext;
    private static Hashtable openedContexts = new Hashtable();

    /**
     * The XML tag name used in saving/loading
     */
    public static final String XML_TAG = "Context";
    
    public static final String BENSIKIN_AUTOMATIC_INSERT_COMMENT = "CONTEXT";


    /**
     * Default constructor.
     * The contextData and contextAttributes attributes are initialized but empty.
     */
    public Context ()
    {
        this.contextData = new ContextData();
        this.contextAttributes = new ContextAttributes( this );
    }

    /**
     * Sets the contextData and contextAttributes attributes
     *
     * @param _contextData
     * @param _contextAttributes
     */
    public Context ( ContextData _contextData , ContextAttributes _contextAttributes )
    {
        this.contextData = _contextData;
        this.contextAttributes = _contextAttributes;
    }

    /**
     * Only sets the contextData attribute
     *
     * @param _contextData
     */
    public Context ( ContextData _contextData )
    {
        this.contextData = _contextData;
    }


    /**
     * Looks up in database the referenced attributes for this context, provided they match the search criterions.
     * The join condition on context id is automatically added to searchCriterions.
     * Once the attributes are found ,they are converted to the ContextAttributes attribute.
     *
     * @param searchCriterions The object containing the search criterions
     * @throws Exception
     */
    public void loadAttributes ( Criterions searchCriterions ) throws Exception
    {
        if ( searchCriterions == null )
        {
            searchCriterions = new Criterions();
        }
        searchCriterions.addCondition( new Condition( GlobalConst.TAB_CONTEXT[ 0 ] , GlobalConst.OP_EQUALS , "" + this.getContextData().getId() ) );
        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        SnapContext cont = this.getAsSnapContext();

        SnapAttributeHeavy[] sah = source.findContextAttributes( cont , searchCriterions );
        this.setContextAttributes( sah );
    }

    /**
     * Looks up in database the referenced snapshots for this context, provided they match the search criterions.
     * The join condition on context id is automatically added to searchCriterions.
     * Once the contexts are found ,they are converted to the Snapshot [] attribute.
     *
     * @param searchCriterions The object containing the search criterions
     * @throws Exception
     */
    public void loadSnapshots ( Criterions searchCriterions ) throws Exception
    {
        searchCriterions.addCondition( new Condition( GlobalConst.TAB_SNAP[ 1 ] , GlobalConst.OP_EQUALS , "" + this.getContextData().getId() ) );

        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        SnapShotLight[] newList = source.findSnapshots( searchCriterions );

        int linesNumber = newList.length;
        snapshots = new Snapshot[ linesNumber ];
        for ( int i = 0 ; i < linesNumber ; i++ )
        {
            SnapShotLight line = newList[ i ];
            SnapshotData sdLine = new SnapshotData( line );
            snapshots[ i ] = new Snapshot( this , sdLine );
        }
    }


    /**
     * Saves a SnapContext object, and results the resulting Context object (which unlike the SnapContext has
     * a known id and date)
     *
     * @param context     The context to save
     * @param saveOptions Not used yet
     * @return A Context object that has the same properties as the context pamareter, except for its id and date
     * @throws Exception
     */
    public static Context save ( SnapContext context , Hashtable saveOptions ) throws Exception
    {
        ISnapManager source = SnapManagerFactory.getCurrentImpl();

        if ( source == null )
        {
            System.out.println( "Context/save/source == null" );
        }
        
        //System.out.println ( "Context/save/BEFORE SAVING/"+context.getAttributeList().size () );
        
        context = source.saveContext( context , saveOptions );

        ContextData savedContextData = new ContextData( context );
        Context savedContext = new Context( savedContextData );
        try {
            savedContext.loadAttributes( null );
        }
        catch (Exception e) {
            // Nothing to do.
            // Just failed to obtain attributes
        }

        //--auto refresh
        Context.addOpenedContext( savedContext );
        Context.setSelectedContext( savedContext );
        ContextListTableModel modelToRefresh = ContextListTableModel.getInstance();
        modelToRefresh.updateList( savedContext );

        return savedContext;
    }

    /**
     * Converts a SnapAttributeHeavy[] object to set the contextAttributes attribute.
     *
     * @param sah The attributes to convert
     */
    private void setContextAttributes ( SnapAttributeHeavy[] sah )
    {
        if ( sah == null )
        {
            return;
        }

        int numberOfAttributes = sah.length;
        ContextAttributes _contextAttributes = new ContextAttributes( this );
        ContextAttribute[] tab = new ContextAttribute[ numberOfAttributes ];

        for ( int i = 0 ; i < numberOfAttributes ; i++ )
        {
            SnapAttributeHeavy currentInAttribute = sah[ i ];
            ContextAttribute currentOutAttribute = new ContextAttribute( _contextAttributes );

            currentOutAttribute.setName( currentInAttribute.getAttribute_name() );
            currentOutAttribute.setCompleteName( currentInAttribute.getAttribute_complete_name() );
            currentOutAttribute.setDevice( currentInAttribute.getAttribute_device_name() );
            currentOutAttribute.setDomain( currentInAttribute.getDomain() );
            currentOutAttribute.setFamily( currentInAttribute.getFamily() );
            currentOutAttribute.setMember( currentInAttribute.getMember() );

            tab[ i ] = currentOutAttribute;
        }

        _contextAttributes.setContextAttributes( tab );
        this.setContextAttributes( _contextAttributes );
    }

    /**
     * Converts to a SnapContext object
     *
     * @return The conversion result
     */
    public SnapContext getAsSnapContext ()
    {
        SnapContext val = new SnapContext();
        ContextData data = this.getContextData();

        val.setAuthor_name( data.getAuthor_name() );
        val.setCreation_date( data.getCreation_date() );
        val.setDescription( data.getDescription() );
        val.setId( data.getId() );
        val.setName( data.getName() );
        val.setReason( data.getReason() );

        return val;
    }

    /**
     * @param _selectedContext The currently selected Context
     */
    public static void setSelectedContext ( Context _selectedContext )
    {
        selectedContext = _selectedContext;
    }

    /**
     * @return The currently selected Context
     */
    public static Context getSelectedContext ()
    {
        return selectedContext;
    }

    /**
     * Looks up in database the contexts matching the search criterions.
     * Only loads their ContextData part.
     *
     * @param searchCriterions
     * @return The list of contexts matching the search criterions.
     * @throws Exception
     */
    public static ContextData[] loadContexts ( Criterions searchCriterions ) throws Exception
    {
        ISnapManager source = SnapManagerFactory.getCurrentImpl();

        SnapContext[] requestResult = source.findContexts( searchCriterions );

        if ( requestResult == null )
        {
            return null;
        }

        int numberOfLines = requestResult.length;
        ContextData[] ret = new ContextData[ numberOfLines ];

        for ( int i = 0 ; i < numberOfLines ; i++ )
        {
            ret[ i ] = new ContextData( requestResult[ i ] );
        }

        return ret;
    }

    /**
     * Looks up in database a particular Context, defined by its id.
     * Loads both its ContextData and ContextAttributes part.
     *
     * @param id The key of the context
     * @return The required Context
     * @throws Exception
     */
    public static Context findContext ( String id ) throws Exception
    {
        ISnapManager source = SnapManagerFactory.getCurrentImpl();

        Criterions searchCriterions = new Criterions();
        Condition idCondition = new Condition( GlobalConst.TAB_CONTEXT[ 0 ] , GlobalConst.OP_EQUALS , id );
        searchCriterions.addCondition( idCondition );

        SnapContext[] requestResult = source.findContexts( searchCriterions );

        if ( requestResult == null )
        {
            return null;
        }

        ContextData data = new ContextData( requestResult[ 0 ] );//there is only one context with a given id
        Context ret = new Context();
        data.setContext( ret );
        ret.setContextData( data );

        ret.loadAttributes( new Criterions() );

        return ret;
    }


    /**
     * @return The Hashtable which values are the currently opened Contexts, and which keys are those contexts' ids.
     */
    public static Hashtable getOpenedContexts ()
    {
        return openedContexts;
    }

    /**
     * Adds openedContext to the current set of opened Contexts.
     *
     * @param openedContext The opened context to add to the current set of opened Contexts
     */
    public static void addOpenedContext ( Context openedContext )
    {
        int id = openedContext.getContextData().getId();
        openedContexts.put( String.valueOf( id ) , openedContext );
    }

    /**
     * Removes from the current set of opened Contexts the one that has that id.
     *
     * @param id The id of the Context to remove from the  current set of opened Contexts
     */
    public static void removeOpenedContext ( int id )
    {
        openedContexts.remove( String.valueOf( id ) );
    }

    /**
     * Completely sets the openedContexts Hashtable.
     *
     * @param openedContexts The new  openedContexts Hashtable
     */
    public static void setOpenedContexts ( Hashtable openedContexts )
    {
        Context.openedContexts = openedContexts;
    }

    /**
     * Empties all references to opened and selected contexts,
     * and resets the context displays, list and detail, accordingly.
     */
    public static void reset ()
    {
        selectedContext = null;
        openedContexts = new Hashtable();

        ContextListTable table = ContextListTable.getInstance();
        table.setModel( ContextListTableModel.forceReset() );

        ContextDataPanel dataPanel = ContextDataPanel.getInstance();
        dataPanel.resetFields();

        ContextAttributesTree tree = ContextAttributesTree.getInstance();
        ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance ( true );
        treeModel.setTree( tree );
        tree.setModel( treeModel );
        AttributesSelectTableModel.getInstance().reset();
    }

    /**
     * Launches a snapshot on this context.
     * If necessary (defined in SnapshotOptions), automatically updates this snapshot's comment.
     * Also refreshes the snapshots list.
     */
    public void launchSnapshot () throws Exception
    {
        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        SnapContext snap = this.getAsSnapContext();

        SnapShot savedSnapshot = source.launchSnapshot( snap );
        int id = savedSnapshot.getId_snap();
        Snapshot toUpdate = Snapshot.findSnapshotById( id );

        //auto comment edit if that's in the options
        Options options = Options.getInstance();
        SnapshotOptions snapshotOptions = options.getSnapshotOptions();
        snapshotOptions.push();
        String defaultComment = Snapshot.getSnapshotDefaultComment();
        if ( defaultComment != null && !defaultComment.trim().equals( "" ) )
        {
            toUpdate.updateComment( defaultComment );
        }
        //auto comment edit if that's in the options

        //--auto refresh
        Snapshot.addOpenedSnapshot( toUpdate );
        SnapshotListTableModel modelToRefresh = SnapshotListTableModel.getInstance();
        modelToRefresh.updateList( toUpdate );
    }


    /**
     * Builds a SnapContext object from parameters.
     * Verifies the parameters and attributes list are non empty beforehand.
     *
     * @param name        The name field
     * @param author      The author field
     * @param reason      The reason field
     * @param description The description field
     * @param model       The model to get the selected attributes from
     * @return A SnapContext object built from parameters.
     * @throws Exception
     */
    public static SnapContext fillSnapContext ( String name , String author , String reason , String description , ContextAttributesTreeModel model ) throws Exception
    {
        if ( !verifyData( name , author , reason , description ) )
        {
            String title = Messages.getMessage( "REGISTER_CONTEXT_EMPTY_DATA_TITLE" );
            String msg = Messages.getMessage( "REGISTER_CONTEXT_EMPTY_DATA_MESSAGE" );

            JOptionPane.showMessageDialog( null , msg , title , JOptionPane.ERROR_MESSAGE );
            return null;
        }

        Iterator enumer = model.getTreeAttributes().iterator();
        if ( !verifyAttributes( enumer ) )
        {
            String title = Messages.getMessage( "REGISTER_CONTEXT_EMPTY_ATTRIBUTES_TITLE" );
            String msg = Messages.getMessage( "REGISTER_CONTEXT_EMPTY_ATTRIBUTES_MESSAGE" );

            JOptionPane.showMessageDialog( null , msg , title , JOptionPane.ERROR_MESSAGE );
            return null;
        }

        //both the id and creation date have to be filled automatically
        SnapContext context = new SnapContext( author , name , -1 , null , reason , description );

        //TO REMOVE
        context.setCreation_date( new java.sql.Date( System.currentTimeMillis() ) );
        //TO REMOVE

        //setting the attributes list of the context to save
        ArrayList attributeList = new ArrayList();
        enumer = model.getTreeAttributes().iterator();
        while ( enumer.hasNext() )
        {
            Object nextKey = enumer.next();
            String nextKey_s = ( String ) nextKey;

            ContextAttribute ca = ( ContextAttribute ) model.getAttribute( nextKey_s );
            String attribute_complete_name = ca.getCompleteName();

            SnapAttributeLight currentAttr = new SnapAttributeLight( attribute_complete_name );
            attributeList.add( currentAttr );
        }
        context.setAttributeList( attributeList );
        return context;
    }

    /**
     * Builds a Context object from parameters.
     *
     * @param name        The name field
     * @param author      The author field
     * @param reason      The reason field
     * @param description The description field
     * @param model       The model to get the selected attributes from
     * @return The filled Context, with data and attributes
     * @throws Exception
     */
    public static Context fillContext ( String id_s , String creationDate_s , String name , String author , String reason , String description , ContextAttributesTreeModel model )
    {
        Iterator enumer = model.getTreeAttributes().iterator();

        int id = ( id_s == null || id_s.equals( "" ) ) ? -1 : Integer.parseInt( id_s );
        Date creationDate = ( creationDate_s == null || creationDate_s.equals( "" ) ) ? null : Date.valueOf( creationDate_s );

        ContextData data = new ContextData( id , creationDate , name , author , reason , description );
        Context ret = new Context( data );
        ContextAttributes attributes = new ContextAttributes( ret );

        //setting the attributes list of the context to save
        enumer = model.getTreeAttributes().iterator();
        int size = model.size();
        ContextAttribute[] _contextAttributes = new ContextAttribute[ size ];

        Vector<ContextAttribute> attrs = new Vector<ContextAttribute>();
        while ( enumer.hasNext() )
        {
            Object nextKey = enumer.next();
            String nextKey_s = ( String ) nextKey;
            ContextAttribute ca = ( ContextAttribute ) model.getAttribute( nextKey_s );

            ContextAttribute nextContextAttribute = new ContextAttribute( attributes );
            nextContextAttribute.setCompleteName( ca.getCompleteName() );
            nextContextAttribute.setDevice( ca.getDevice() );
            nextContextAttribute.setDomain( ca.getDomain() );
            nextContextAttribute.setFamily( ca.getFamily() );
            nextContextAttribute.setMember( ca.getMember() );
            nextContextAttribute.setName( ca.getName() );

            attrs.add(nextContextAttribute);
        }
        Collections.sort( attrs, new BensikinAttributesComparator() );
        for (int i = 0; i < attrs.size(); i++) {
            _contextAttributes[i] = attrs.elementAt(i);
        }
        attrs.clear();
        attrs = null;

        attributes.setContextAttributes( _contextAttributes );
        ret.setContextAttributes( attributes );

        return ret;
    }

    /**
     * Verifies an enumeration is non-empty.
     *
     * @param enum The Enumeration to check
     * @return True if non-empty, false otherwise
     */
    private static boolean verifyAttributes ( Iterator enumer )
    {
        while ( enumer.hasNext() )
        {
            return true;
        }

        return false;
    }

    /**
     * Verifies all fields are non-empty.
     *
     * @param name
     * @param author
     * @param reason
     * @param description
     * @return true if all fields are non-empty, false otherwise
     */
    private static boolean verifyData ( String name , String author , String reason , String description )
    {
        if ( name == null || name.length() == 0 )
        {
            return false;
        }
        if ( author == null || author.length() == 0 )
        {
            return false;
        }
        if ( reason == null || reason.length() == 0 )
        {
            return false;
        }
        if ( description == null || description.length() == 0 )
        {
            return false;
        }

        return true;
    }


    /**
     * Returns a XML representation of the context
     *
     * @return a XML representation of the context
     */
    public String toString ()
    {
        String ret = "";

        XMLLine openingLine = new XMLLine( Context.XML_TAG , XMLLine.OPENING_TAG_CATEGORY );
        XMLLine closingLine = new XMLLine( Context.XML_TAG , XMLLine.CLOSING_TAG_CATEGORY );

        int id = contextData.getId();
        Date creationDate = contextData.getCreation_date();
        String author = contextData.getAuthor_name();
        String description = contextData.getDescription();
        String name = contextData.getName();
        String reason = contextData.getReason();
        String path = contextData.getPath();
        String isModified = this.isModified + "";

        openingLine.setAttribute( ContextData.ID_PROPERTY_XML_TAG , String.valueOf( id ) );
        openingLine.setAttribute( ContextData.IS_MODIFIED_PROPERTY_XML_TAG , isModified );
        if ( creationDate != null )
        {
            openingLine.setAttribute( ContextData.CREATION_DATE_PROPERTY_XML_TAG , creationDate.toString() );
        }
        if ( author != null )
        {
            openingLine.setAttribute( ContextData.AUTHOR_PROPERTY_XML_TAG , author );
        }
        if ( description != null )
        {
            openingLine.setAttribute( ContextData.DESCRIPTION_DATE_PROPERTY_XML_TAG , description );
        }
        if ( name != null )
        {
            openingLine.setAttribute( ContextData.NAME_PROPERTY_XML_TAG , name );
        }
        if ( reason != null )
        {
            openingLine.setAttribute( ContextData.REASON_PROPERTY_XML_TAG , reason );
        }
        if ( path != null )
        {
            openingLine.setAttribute( ContextData.PATH_PROPERTY_XML_TAG , path );
        }

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if ( this.contextAttributes != null )
        {
            ret += this.contextAttributes.toString();
        }

        ret += closingLine.toString();

        return ret;
    }

    public String toUserFriendlyString() {
        StringBuffer buffer = new StringBuffer();
        if (this.contextAttributes != null) {
            this.contextAttributes.appendUserFriendlyString(buffer);
        }
        return buffer.toString();
    }

    /**
     * Visibly make this context the current context by pushing its data and attributes
     */
    public void push ()
    {
        if ( this.contextData != null )
        {
            this.contextData.push();
        }

        if ( this.contextAttributes != null )
        {
            this.contextAttributes.push();
        }
    }

    /**
     * @return Returns the contextAttributes.
     */
    public ContextAttributes getContextAttributes ()
    {
        return contextAttributes;
    }

    /**
     * @param contextAttributes The contextAttributes to set.
     */
    public void setContextAttributes ( ContextAttributes contextAttributes )
    {
        this.contextAttributes = contextAttributes;
    }

    /**
     * @return Returns the contextData.
     */
    public ContextData getContextData ()
    {
        return contextData;
    }

    /**
     * @param contextData The contextData to set.
     */
    public void setContextData ( ContextData contextData )
    {
        this.contextData = contextData;
    }

    /**
     * @return Returns the snapshots.
     */
    public Snapshot[] getSnapshots ()
    {
        return snapshots;
    }

    /**
     * @param snapshots The snapshots to set.
     */
    public void setSnapshots ( Snapshot[] snapshots )
    {
        this.snapshots = snapshots;
    }

    /**
     * @return
     */
    public static Context buildContextToBeSaved() 
    {
        //System.out.println ( "Context/buildContextToBeSaved IN/"+Context.getSelectedContext().getPath()+"/" );
        ContextDataPanel contextDataPanel = ContextDataPanel.getInstance();
        String id_s = contextDataPanel.getIDField().getText();
        String creationDate_s = contextDataPanel.getCreationDateField().getText();
        String name = contextDataPanel.getNameField().getText();
        String author = contextDataPanel.getAuthorNameField().getText();
        String reason = contextDataPanel.getReasonField().getText();
        String description = contextDataPanel.getDescriptionField().getText();

        //System.out.println ( "Context/buildContextToBeSaved IN 1/"+Context.getSelectedContext().getPath()+"/" );
        
        ContextAttributesTreeModel model = ContextAttributesTreeModel.getInstance(false);

        //System.out.println ( "Context/buildContextToBeSaved IN 2/"+Context.getSelectedContext().getPath()+"/" );
        
        Context ret = Context.fillContext( id_s , creationDate_s , name , author , reason , description , model );
        
        //System.out.println ( "Context/buildContextToBeSaved IN 3/"+Context.getSelectedContext().getPath()+"/" );
        if (Context.getSelectedContext () != null) {
            Context.getSelectedContext ().setContextAttributes ( ret.getContextAttributes () );
            ret.getContextData ().setPath ( Context.getSelectedContext ().getPath() );
            Context.getSelectedContext ().setContextData ( ret.getContextData () );
            Context.getSelectedContext ().setModified ( ret.isModified () );
        }
        
        //System.out.println ( "Context/buildContextToBeSaved OUT/"+Context.getSelectedContext().getPath()+"/" );
        
        return Context.getSelectedContext ();
    }

    /**
     * @param manager
     * @param b
     */
    public void save(IContextManager manager, boolean saveAs) 
    {
        //System.out.println ( "Context/save BEFORE/"+Context.getSelectedContext().getPath()+"/" );
        
        String pathToUse = getPathToUse( manager , saveAs );
        if ( pathToUse == null )
        {
            return;
        }
        //System.out.println ( "Context/save/pathToUse/"+pathToUse+"/saveAs/"+saveAs+"/" );
        manager.setNonDefaultSaveLocation( pathToUse );
        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            this.setPath( pathToUse );
            this.setModified( false );
            //manager.saveArchivingConfiguration( this , null );
            manager.saveContext ( this , null );
            Context.setSelectedContext ( this );

            //System.out.println ( "Context/save AFTER/"+Context.getSelectedContext().getPath()+"/" );
            
            String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_OK" );
            logger.trace( fr.soleil.bensikin.logs.ILogger.LEVEL_DEBUG , msg );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_KO" );
            logger.trace( fr.soleil.bensikin.logs.ILogger.LEVEL_ERROR , msg );
            logger.trace( fr.soleil.bensikin.logs.ILogger.LEVEL_ERROR , e );

            this.setPath( null );
            this.setModified( true );

            return;
        }

        //----WARNING!!!!!!!!!!!!!!!!!!!!!
        /*ArchivingGeneralPanel panel = ArchivingGeneralPanel.getInstance();
        panel.setPath( this.getPath() );*/
        //----WARNING!!!!!!!!!!!!!!!!!!!!!
    }
    
    /**
     * @return
     */
    private String getPathToUse ( IContextManager manager , boolean saveAs )
    {
        String pathToUse = this.getPath();
        //System.out.println ( "Context/getPathToUse/pathToUse/"+pathToUse+"/saveAs/"+saveAs+"/" );
        JFileChooser chooser = new JFileChooser();
        ContextFileFilter ACfilter = new ContextFileFilter();
        chooser.addChoosableFileFilter( ACfilter );

        if ( pathToUse != null )
        {
            if ( saveAs )
            {
                chooser.setCurrentDirectory( new File( pathToUse ) );
            }
            else
            {
                return pathToUse;
            }
        }
        else
        {
            chooser.setCurrentDirectory( new File( manager.getDefaultSaveLocation() ) );
        }

        int returnVal = chooser.showSaveDialog( BensikinFrame.getInstance() );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            String path = f.getAbsolutePath();

            if ( f != null )
            {
                String extension = ContextFileFilter.getExtension( f );
                String expectedExtension = ACfilter.getExtension();

                if ( extension == null || !extension.equalsIgnoreCase( expectedExtension ) )
                {
                    path += ".";
                    path += expectedExtension;
                }
            }
            if (f.exists()) {
                int choice = JOptionPane.showConfirmDialog(BensikinFrame.getInstance(),
                                                           Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"),
                                                           Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"),
                                                           JOptionPane.YES_NO_OPTION,
                                                           JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.OK_OPTION) {
                    return null;
                }
            }
            manager.setNonDefaultSaveLocation( path );
            return path;
        }
        else
        {
            return null;
        }

    }
    
    /**
     * @param saveLocation
     */
    public void setPath ( String path )
    {
        if ( this.contextData != null )
        {
            this.contextData.setPath( path );
        }

    }

    public String getPath ()
    {
        if ( this.contextData != null )
        {
            return this.contextData.getPath();
        }
        else
        {
            return null;
        }
    }
    /**
     * @return Returns the isModified.
     */
    public boolean isModified() {
        return isModified;
    }
    /**
     * @param isModified The isModified to set.
     */
    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }
}
