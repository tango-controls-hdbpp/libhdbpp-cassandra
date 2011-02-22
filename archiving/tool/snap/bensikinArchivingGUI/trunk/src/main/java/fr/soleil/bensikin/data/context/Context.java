// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/Context.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Context.
// (Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.16 $
//
// $Log: Context.java,v $
// Revision 1.16 2007/08/24 14:08:39 ounsy
// bug correction with context printing as text + Context attributes ordering
// (Mantis bug 3912)
//
// Revision 1.15 2007/08/23 15:28:48 ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.14 2007/08/23 12:58:41 ounsy
// minor changes
//
// Revision 1.13 2006/11/29 10:00:26 ounsy
// minor changes
//
// Revision 1.12 2006/03/20 15:49:25 ounsy
// removed useless logs
//
// Revision 1.11 2006/03/14 13:06:18 ounsy
// removed useless logs
//
// Revision 1.10 2006/01/13 13:25:23 ounsy
// File replacement warning
//
// Revision 1.9 2006/01/13 13:00:19 ounsy
// avoiding NullPointerException
//
// Revision 1.8 2006/01/12 12:57:21 ounsy
// table reset when necessary
//
// Revision 1.7 2005/12/14 16:32:33 ounsy
// added methods necessary for snapshots comparison with current state
//
// Revision 1.6 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:37 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
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

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.components.context.ContextFileFilter;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.list.ContextListTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.data.context.manager.ContextDataManager;
import fr.soleil.bensikin.data.context.manager.IContextManager;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.datasources.snapdb.SnapManagerFactory;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.models.ContextListTableModel;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeHeavy;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;
import fr.soleil.tango.util.entity.ui.comparator.EntitiesComparator;

/**
 * A pivotal class of Bensikin, this class is the upper level modelisation of a
 * context.
 * <p/>
 * A Context object has two main attributes: - a ContextData attribute
 * representing all non-attribute-dependant information - a ContextAttributes
 * attribute representing all attribute-dependant information It also has a
 * Snapshot[] attribute, representing the Snapshots loaded for this context.
 * <p/>
 * The Context class also saves static references to the currently opened
 * contexts, and the selected context.
 *
 * @author CLAISSE
 */
public class Context {

	final static ILogger logger = GUILoggerFactory.getLogger();

	private Snapshot[] snapshots;
	private int contextId;
	private ContextData contextData;
	private ContextAttributes contextAttributes;

	private boolean isModified = true;
	private boolean isContextFile = false;

	private static Context selectedContext;
	private static Hashtable<String, Context> openedContexts = new Hashtable<String, Context>();

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "Context";

	public static final String BENSIKIN_AUTOMATIC_INSERT_COMMENT = "CONTEXT";

	/**
	 * Default constructor. The contextData and contextAttributes attributes are
	 * initialized but empty.
	 */
	public Context() {
		contextAttributes = new ContextAttributes(this);
	}

	/**
	 * Sets the contextData and contextAttributes attributes
	 *
	 * @param _contextData
	 * @param _contextAttributes
	 */
	public Context(final int _contextId, final ContextAttributes _contextAttributes) {
		contextId = _contextId;
		contextAttributes = _contextAttributes;
	}

	/**
	 * Only sets the contextData attribute
	 *
	 * @param _contextData
	 */
	public Context(final int _contextId) {
		contextId = _contextId;
	}

	/**
	 * Looks up in database the referenced attributes for this context, provided
	 * they match the search criterions. The join condition on context id is
	 * automatically added to searchCriterions. Once the attributes are found
	 * ,they are converted to the ContextAttributes attribute.
	 *
	 * @param searchCriterions
	 *            The object containing the search criterions
	 * @throws SnapshotingException
	 */
	public void loadAttributes(Criterions searchCriterions) throws SnapshotingException {
		if (searchCriterions == null) {
			searchCriterions = new Criterions();
		}
		searchCriterions.addCondition(new Condition(GlobalConst.TAB_CONTEXT[0], GlobalConst.OP_EQUALS, "" + contextId));
		final ISnapManager source = SnapManagerFactory.getCurrentImpl();
		final SnapContext cont = getAsSnapContext();

		final SnapAttributeHeavy[] sah = source.findContextAttributes(cont, searchCriterions);
		this.setContextAttributes(sah);
	}

	/**
	 * Looks up in database the referenced snapshots for this context, provided
	 * they match the search criterions. The join condition on context id is
	 * automatically added to searchCriterions. Once the contexts are found
	 * ,they are converted to the Snapshot [] attribute.
	 *
	 * @param searchCriterions
	 *            The object containing the search criterions
	 * @throws Exception
	 */
	public void loadSnapshots(final Criterions searchCriterions) throws SnapshotingException {
		searchCriterions.addCondition(new Condition(GlobalConst.TAB_SNAP[1], GlobalConst.OP_EQUALS, "" + contextId));

		final ISnapManager source = SnapManagerFactory.getCurrentImpl();
		final SnapShotLight[] newList = source.findSnapshots(searchCriterions);

		final int linesNumber = newList.length;
		snapshots = new Snapshot[linesNumber];
		for (int i = 0; i < linesNumber; i++) {
			final SnapShotLight line = newList[i];
			final SnapshotData sdLine = new SnapshotData(line);
			snapshots[i] = new Snapshot(sdLine);
		}
	}

	/**
	 * Saves a SnapContext object, and results the resulting Context object
	 * (which unlike the SnapContext has a known id and date)
	 *
	 * @param context
	 *            The context to save
	 * @return A Context object that has the same properties as the context
	 *         pamareter, except for its id and date
	 * @throws Exception
	 */
	public static Context save(SnapContext context) throws Exception {
		final ISnapManager source = SnapManagerFactory.getCurrentImpl();

		if (source == null) {
			System.out.println("Context/save/source == null");
		}

		// System.out.println (
		// "Context/save/BEFORE SAVING/"+context.getAttributeList().size () );

		context = source.saveContext(context);

		final ContextData savedContextData = new ContextData(context);
		final Context savedContext = new Context(savedContextData.getId());
		try {
			savedContext.loadAttributes(null);
		} catch (final SnapshotingException e) {
			// Failed to obtain attributes --> log the problem
			String msg;
			if (e.computeIsDueToATimeOut()) {
				msg = Messages.getLogMessage("LOAD_CONTEXT_ATTRIBUTES_TIMEOUT");
			} else {
				msg = Messages.getLogMessage("LOAD_CONTEXT_ATTRIBUTES_KO");
			}
		}

		// --auto refresh
		Context.addOpenedContext(savedContext);
		Context.setSelectedContext(savedContext);
		final ContextListTableModel modelToRefresh = ContextListTableModel.getInstance();
		modelToRefresh.updateList(savedContext);

		return savedContext;
	}

	/**
	 * Converts a SnapAttributeHeavy[] object to set the contextAttributes
	 * attribute.
	 *
	 * @param sah
	 *            The attributes to convert
	 */
	private void setContextAttributes(final SnapAttributeHeavy[] sah) {
		if (sah == null) {
			return;
		}

		final int numberOfAttributes = sah.length;
		final ContextAttributes _contextAttributes = new ContextAttributes(this);
		final ContextAttribute[] tab = new ContextAttribute[numberOfAttributes];

		for (int i = 0; i < numberOfAttributes; i++) {
			final SnapAttributeHeavy currentInAttribute = sah[i];
			final ContextAttribute currentOutAttribute = new ContextAttribute(_contextAttributes);

			currentOutAttribute.setName(currentInAttribute.getAttribute_name());
			currentOutAttribute.setCompleteName(currentInAttribute.getAttribute_complete_name());
			currentOutAttribute.setDevice(currentInAttribute.getAttribute_device_name());
			currentOutAttribute.setDomain(currentInAttribute.getDomain());
			currentOutAttribute.setFamily(currentInAttribute.getFamily());
			currentOutAttribute.setMember(currentInAttribute.getMember());

			tab[i] = currentOutAttribute;
		}

		_contextAttributes.setContextAttributes(tab);
		this.setContextAttributes(_contextAttributes);
	}

	/**
	 * Converts to a SnapContext object
	 *
	 * @return The conversion result
	 */
	public SnapContext getAsSnapContext() {
		final SnapContext val = new SnapContext();
		if (contextData == null) {
			contextData = ContextDataManager.getInstance().getContextData(contextId);
		}

		if (contextData != null) {
			val.setAuthor_name(contextData.getAuthor_name());
			val.setCreation_date(contextData.getCreation_date());
			val.setDescription(contextData.getDescription());
			val.setId(contextData.getId());
			val.setName(contextData.getName());
			val.setReason(contextData.getReason());
		}

		return val;
	}

	/**
	 * @param _selectedContext
	 *            The currently selected Context
	 */
	public static void setSelectedContext(final Context _selectedContext) {
		selectedContext = _selectedContext;
	}

	/**
	 * @return The currently selected Context
	 */
	public static Context getSelectedContext() {
		return selectedContext;
	}

	/**
	 * Looks up in database the contexts matching the search criterions. Only
	 * loads their ContextData part.
	 *
	 * @param searchCriterions
	 * @return The list of contexts matching the search criterions.
	 * @throws Exception
	 */
	public static ContextData[] loadContexts(final Criterions searchCriterions) throws Exception {
		final ISnapManager source = SnapManagerFactory.getCurrentImpl();

		final SnapContext[] requestResult = source.findContexts(searchCriterions);

		if (requestResult == null) {
			return null;
		}

		final int numberOfLines = requestResult.length;
		final ContextData[] ret = new ContextData[numberOfLines];

		for (int i = 0; i < numberOfLines; i++) {
			ret[i] = new ContextData(requestResult[i]);
		}

		return ret;
	}

	/**
	 * Looks up in database a particular Context, defined by its id. Loads both
	 * its ContextData and ContextAttributes part.
	 *
	 * @param id
	 *            The key of the context
	 * @return The required Context
	 * @throws Exception
	 */
	public static Context findContext(final String id) throws Exception {
		final Context ret = new Context(Integer.parseInt(id));
		ret.loadAttributes(new Criterions());

		return ret;
	}

	/**
	 * @return The Hashtable which values are the currently opened Contexts, and
	 *         which keys are those contexts' ids.
	 */
	public static Hashtable<String, Context> getOpenedContexts() {
		return openedContexts;
	}

	/**
	 * Adds openedContext to the current set of opened Contexts.
	 *
	 * @param openedContext
	 *            The opened context to add to the current set of opened
	 *            Contexts
	 */
	public static void addOpenedContext(final Context openedContext) {
		final int id = openedContext.getContextId();
		openedContexts.put(String.valueOf(id), openedContext);
	}

	/**
	 * Removes from the current set of opened Contexts the one that has that id.
	 *
	 * @param id
	 *            The id of the Context to remove from the current set of opened
	 *            Contexts
	 */
	public static void removeOpenedContext(final int id) {
		openedContexts.remove(String.valueOf(id));
	}

	/**
	 * Completely sets the openedContexts Hashtable.
	 *
	 * @param openedContexts
	 *            The new openedContexts Hashtable
	 */
	public static void setOpenedContexts(final Hashtable<String, Context> openedContexts) {
		Context.openedContexts = openedContexts;
	}

	/**
	 * Empties all references to opened and selected contexts, and resets the
	 * context displays, list and detail, accordingly.
	 */
	public static void reset() {
		selectedContext = null;
		openedContexts = new Hashtable<String, Context>();

		final ContextListTable table = ContextListTable.getInstance();
		table.setModel(ContextListTableModel.forceReset());

		final ContextDataPanel dataPanel = ContextDataPanel.getInstance();
		dataPanel.resetFields();

		final ContextAttributesTree tree = ContextAttributesTree.getInstance();
		final ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance(true);
		treeModel.setTree(tree);
		tree.setModel(treeModel);
		ContextDetailPanel.getInstance().getAttributeTableSelectionBean().getSelectionPanel().getAttributesSelectTable().getModel().reset();
	}

	/**
	 * Launches a snapshot on this context. If necessary (defined in
	 * SnapshotOptions), automatically updates this snapshot's comment. Also
	 * refreshes the snapshots list.
	 */
	public void launchSnapshot() throws SnapshotingException {
		final ISnapManager source = SnapManagerFactory.getCurrentImpl();
		final SnapContext snap = getAsSnapContext();

		final SnapShot savedSnapshot = source.launchSnapshot(snap);
		final int id = savedSnapshot.getId_snap();
		final Snapshot toUpdate = Snapshot.findSnapshotById(id);

		// auto comment edit if that's in the options
		final Options options = Options.getInstance();
		final SnapshotOptions snapshotOptions = options.getSnapshotOptions();
		snapshotOptions.push();
		final String defaultComment = Snapshot.getSnapshotDefaultComment();
		if (defaultComment != null && !defaultComment.trim().equals("")) {
			toUpdate.updateComment(defaultComment);
		}
		// auto comment edit if that's in the options

		// --auto refresh
		Snapshot.addOpenedSnapshot(toUpdate);
		final SnapshotListTableModel modelToRefresh = SnapshotListTableModel.getInstance();
		modelToRefresh.updateList(toUpdate);
	}

	/**
	 * Builds a SnapContext object from parameters. Verifies the parameters and
	 * attributes list are non empty beforehand.
	 *
	 * @param name
	 *            The name field
	 * @param author
	 *            The author field
	 * @param reason
	 *            The reason field
	 * @param description
	 *            The description field
	 * @param model
	 *            The model to get the selected attributes from
	 * @return A SnapContext object built from parameters.
	 * @throws Exception
	 */
	public static SnapContext fillSnapContext(final String name, final String author, final String reason, final String description, final ContextAttributesTreeModel model) {
		if (!verifyData(name, author, reason, description)) {
			final String title = Messages.getMessage("REGISTER_CONTEXT_EMPTY_DATA_TITLE");
			final String msg = Messages.getMessage("REGISTER_CONTEXT_EMPTY_DATA_MESSAGE");

			JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg, title, JOptionPane.ERROR_MESSAGE);
			return null;
		}

		Iterator<String> enumer = model.getTreeAttributes().iterator();
		if (!verifyAttributes(enumer)) {
			final String title = Messages.getMessage("REGISTER_CONTEXT_EMPTY_ATTRIBUTES_TITLE");
			final String msg = Messages.getMessage("REGISTER_CONTEXT_EMPTY_ATTRIBUTES_MESSAGE");

			JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg, title, JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// both the id and creation date have to be filled automatically
		final SnapContext context = new SnapContext(author, name, -1, null, reason, description);

		// TO REMOVE
		context.setCreation_date(new java.sql.Date(System.currentTimeMillis()));
		// TO REMOVE

		// setting the attributes list of the context to save
		final ArrayList<SnapAttributeLight> attributeList = new ArrayList<SnapAttributeLight>();
		enumer = model.getTreeAttributes().iterator();
		while (enumer.hasNext()) {
			final String nextKey_s = enumer.next();

			final ContextAttribute ca = model.getAttribute(nextKey_s);
			final String attribute_complete_name = ca.getCompleteName();

			final SnapAttributeLight currentAttr = new SnapAttributeLight(attribute_complete_name);
			attributeList.add(currentAttr);
		}
		context.setAttributeList(attributeList);
		return context;
	}

	/**
	 * Builds a Context object from parameters.
	 *
	 * @param name
	 *            The name field
	 * @param author
	 *            The author field
	 * @param reason
	 *            The reason field
	 * @param description
	 *            The description field
	 * @param model
	 *            The model to get the selected attributes from
	 * @return The filled Context, with data and attributes
	 * @throws Exception
	 */
	public static Context fillContext(final String id_s, final String creationDate_s, final String name, final String author, final String reason, final String description,
			final ContextAttributesTreeModel model) {
		Iterator<String> enumer = model.getTreeAttributes().iterator();

		final int id = id_s == null || id_s.equals("") ? -1 : Integer.parseInt(id_s);
		final Date creationDate = creationDate_s == null || creationDate_s.equals("") ? null : Date.valueOf(creationDate_s);

		final ContextData data = new ContextData(id, creationDate, name, author, reason, description);
		final Context ret = new Context(data.getId());
		final ContextAttributes attributes = new ContextAttributes(ret);

		// setting the attributes list of the context to save
		enumer = model.getTreeAttributes().iterator();
		final int size = model.size();
		final ContextAttribute[] _contextAttributes = new ContextAttribute[size];

		Vector<ContextAttribute> attrs = new Vector<ContextAttribute>();
		while (enumer.hasNext()) {
			final String nextKey_s = enumer.next();
			final ContextAttribute ca = model.getAttribute(nextKey_s);

			final ContextAttribute nextContextAttribute = new ContextAttribute(attributes);
			nextContextAttribute.setCompleteName(ca.getCompleteName());
			nextContextAttribute.setDevice(ca.getDeviceName());
			nextContextAttribute.setDomain(ca.getDomainName());
			nextContextAttribute.setFamily(ca.getFamilyName());
			nextContextAttribute.setMember(ca.getMember());
			nextContextAttribute.setName(ca.getName());

			attrs.add(nextContextAttribute);
		}
		Collections.sort(attrs, new EntitiesComparator());
		for (int i = 0; i < attrs.size(); i++) {
			_contextAttributes[i] = attrs.elementAt(i);
		}
		attrs.clear();
		attrs = null;

		attributes.setContextAttributes(_contextAttributes);
		ret.setContextAttributes(attributes);

		return ret;
	}

	/**
	 * Verifies an enumeration is non-empty.
	 *
	 * @param enum
	 *            The Enumeration to check
	 * @return True if non-empty, false otherwise
	 */
	private static boolean verifyAttributes(final Iterator<String> enumer) {
		while (enumer.hasNext()) {
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
	private static boolean verifyData(final String name, final String author, final String reason, final String description) {
		if (name == null || name.length() == 0) {
			return false;
		}
		if (author == null || author.length() == 0) {
			return false;
		}
		if (reason == null || reason.length() == 0) {
			return false;
		}
		if (description == null || description.length() == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Returns a XML representation of the context
	 *
	 * @return a XML representation of the context
	 */
	@Override
	public String toString() {
		String ret = "";

		final XMLLine openingLine = new XMLLine(Context.XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
		final XMLLine closingLine = new XMLLine(Context.XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);
		if (contextData == null) {
			contextData = ContextDataManager.getInstance().getContextData(contextId);
		}

		final int id = contextData.getId();
		final Date creationDate = contextData.getCreation_date();
		final String author = contextData.getAuthor_name();
		final String description = contextData.getDescription();
		final String name = contextData.getName();
		final String reason = contextData.getReason();
		final String path = contextData.getPath();
		final String isModified = this.isModified + "";

		openingLine.setAttribute(ContextData.ID_PROPERTY_XML_TAG, String.valueOf(id));
		openingLine.setAttribute(ContextData.IS_MODIFIED_PROPERTY_XML_TAG, isModified);
		if (creationDate != null) {
			openingLine.setAttribute(ContextData.CREATION_DATE_PROPERTY_XML_TAG, creationDate.toString());
		}
		if (author != null) {
			openingLine.setAttribute(ContextData.AUTHOR_PROPERTY_XML_TAG, author);
		}
		if (description != null) {
			openingLine.setAttribute(ContextData.DESCRIPTION_DATE_PROPERTY_XML_TAG, description);
		}
		if (name != null) {
			openingLine.setAttribute(ContextData.NAME_PROPERTY_XML_TAG, name);
		}
		if (reason != null) {
			openingLine.setAttribute(ContextData.REASON_PROPERTY_XML_TAG, reason);
		}
		if (path != null) {
			openingLine.setAttribute(ContextData.PATH_PROPERTY_XML_TAG, path);
		}

		ret += openingLine.toString();
		ret += GUIUtilities.CRLF;

		if (contextAttributes != null) {
			ret += contextAttributes.toString();
		}

		ret += closingLine.toString();

		return ret;
	}

	public String toUserFriendlyString() {
		final StringBuffer buffer = new StringBuffer();
		if (contextAttributes != null) {
			contextAttributes.appendUserFriendlyString(buffer);
		}
		return buffer.toString();
	}

	/**
	 * Visibly make this context the current context by pushing its data and
	 * attributes
	 */
	public void push() {
		// }
		if (contextData == null) {
			contextData = ContextDataManager.getInstance().getContextData(contextId);
		}
		if (contextData != null) {
			contextData.push();
		}
		if (contextAttributes != null) {
			contextAttributes.push();
		}
	}

	/**
	 * @return Returns the contextAttributes.
	 */
	public ContextAttributes getContextAttributes() {
		return contextAttributes;
	}

	/**
	 * @param contextAttributes
	 *            The contextAttributes to set.
	 */
	public void setContextAttributes(final ContextAttributes contextAttributes) {
		this.contextAttributes = contextAttributes;
	}

	/**
	 * @return Returns the contextData.
	 */
	public ContextData getContextData() {
		if (contextData == null) {
			contextData = ContextDataManager.getInstance().getContextData(contextId);
		}
		return contextData;
	}

	/**
	 * @param contextData
	 *            The contextData to set.
	 */
	public void setContextData(final ContextData contextData) {
		this.contextData = contextData;
	}

	/**
	 * @return Returns the snapshots.
	 */
	public Snapshot[] getSnapshots() {
		return snapshots;
	}

	/**
	 * @param snapshots
	 *            The snapshots to set.
	 */
	public void setSnapshots(final Snapshot[] snapshots) {
		this.snapshots = snapshots;
	}

	/**
	 * @return
	 */
	public static Context buildContextToBeSaved() {
		// System.out.println (
		// "Context/buildContextToBeSaved IN/"+Context.getSelectedContext().getPath()+"/"
		// );
		final ContextDataPanel contextDataPanel = ContextDataPanel.getInstance();
		final String id_s = contextDataPanel.getIDField().getText();
		final String creationDate_s = contextDataPanel.getCreationDateField().getText();
		final String name = contextDataPanel.getNameField().getText();
		final String author = contextDataPanel.getAuthorNameField().getText();
		final String reason = contextDataPanel.getReasonField().getText();
		final String description = contextDataPanel.getDescriptionField().getText();

		// System.out.println (
		// "Context/buildContextToBeSaved IN 1/"+Context.getSelectedContext().getPath()+"/"
		// );

		final ContextAttributesTreeModel model = ContextAttributesTreeModel.getInstance(false);

		// System.out.println (
		// "Context/buildContextToBeSaved IN 2/"+Context.getSelectedContext().getPath()+"/"
		// );

		final Context ret = Context.fillContext(id_s, creationDate_s, name, author, reason, description, model);

		// System.out.println (
		// "Context/buildContextToBeSaved IN 3/"+Context.getSelectedContext().getPath()+"/"
		// );
		if (Context.getSelectedContext() != null) {
			Context.getSelectedContext().setContextAttributes(ret.getContextAttributes());
			ret.getContextData().setPath(Context.getSelectedContext().getPath());
			Context.getSelectedContext().setContextData(ret.getContextData());
			Context.getSelectedContext().setModified(ret.isModified());
		}

		// System.out.println (
		// "Context/buildContextToBeSaved OUT/"+Context.getSelectedContext().getPath()+"/"
		// );

		return Context.getSelectedContext();
	}

	/**
	 * @param manager
	 * @param b
	 */
	public void save(final IContextManager manager, final boolean saveAs) {
		// System.out.println (
		// "Context/save BEFORE/"+Context.getSelectedContext().getPath()+"/" );

		final String pathToUse = getPathToUse(manager, saveAs);
		if (pathToUse == null) {
			return;
		}
		// System.out.println (
		// "Context/save/pathToUse/"+pathToUse+"/saveAs/"+saveAs+"/" );
		manager.setNonDefaultSaveLocation(pathToUse);
		try {
			final Context newContext = new Context(getContextId());
			final ContextData newData = new ContextData(contextData.getId(), contextData.getCreation_date(), contextData.getName(), contextData.getAuthor_name(), contextData.getReason(), contextData
					.getDescription());
			newContext.setContextData(newData);
			newContext.setContextAttributes(getContextAttributes());
			newContext.setSnapshots(getSnapshots());
			newContext.setPath(pathToUse);
			newContext.setModified(false);
			// manager.saveArchivingConfiguration( this , null );
			manager.saveContext(newContext, null);
			Context.setSelectedContext(newContext);

			// System.out.println (
			// "Context/save AFTER/"+Context.getSelectedContext().getPath()+"/"
			// );

			final String msg = Messages.getLogMessage("SAVE_CONTEXT_ACTION_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (final Exception e) {
			final String msg = Messages.getLogMessage("SAVE_CONTEXT_ACTION_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);

			setPath(null);
			setModified(true);

			return;
		}

		// ----WARNING!!!!!!!!!!!!!!!!!!!!!
		/*
		 * ArchivingGeneralPanel panel = ArchivingGeneralPanel.getInstance();
		 * panel.setPath( this.getPath() );
		 */
		// ----WARNING!!!!!!!!!!!!!!!!!!!!!
	}

	/**
	 * @return
	 */
	private String getPathToUse(final IContextManager manager, final boolean saveAs) {
		final String pathToUse = getPath();
		// System.out.println (
		// "Context/getPathToUse/pathToUse/"+pathToUse+"/saveAs/"+saveAs+"/" );
		final JFileChooser chooser = new JFileChooser();
		final ContextFileFilter ACfilter = new ContextFileFilter();
		chooser.addChoosableFileFilter(ACfilter);

		if (pathToUse != null) {
			if (saveAs) {
				chooser.setCurrentDirectory(new File(pathToUse));
			} else {
				return pathToUse;
			}
		} else {
			chooser.setCurrentDirectory(new File(manager.getDefaultSaveLocation()));
		}

		final int returnVal = chooser.showSaveDialog(BensikinFrame.getInstance());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File f = chooser.getSelectedFile();
			String path = f.getAbsolutePath();

			if (f != null) {
				final String extension = ContextFileFilter.getExtension(f);
				final String expectedExtension = ACfilter.getExtension();

				if (extension == null || !extension.equalsIgnoreCase(expectedExtension)) {
					path += ".";
					path += expectedExtension;
				}
			}
			if (f.exists()) {
				final int choice = JOptionPane.showConfirmDialog(BensikinFrame.getInstance(), Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"), Messages
						.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice != JOptionPane.OK_OPTION) {
					return null;
				}
			}
			manager.setNonDefaultSaveLocation(path);
			return path;
		} else {
			return null;
		}

	}

	/**
	 * @param saveLocation
	 */
	public void setPath(final String path) {
		if (contextData == null) {
			contextData = ContextDataManager.getInstance().getContextData(contextId);
		}
		if (contextData != null) {
			contextData.setPath(path);
		}
	}

	public String getPath() {
		if (contextData == null) {
			contextData = ContextDataManager.getInstance().getContextData(contextId);
		}

		if (contextData != null) {
			return contextData.getPath();
		} else {
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
	 * @param isModified
	 *            The isModified to set.
	 */
	public void setModified(final boolean isModified) {
		this.isModified = isModified;
	}

	public int getContextId() {
		return contextId;
	}

	public void setContextId(final int _contextId) {
		contextId = _contextId;
	}

	public void setContextFile(final boolean isContextFile) {
		this.isContextFile = isContextFile;
	}

	public boolean isContextFile() {
		return isContextFile;
	}
}
