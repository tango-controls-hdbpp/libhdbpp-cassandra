//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/history/manager/XMLHistoryManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLHistoryManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: XMLHistoryManager.java,v $
// Revision 1.5  2007/02/01 14:15:38  pierrejoseph
// XmlHelper reorg
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2005/12/15 11:38:23  ounsy
// avoiding a null pointer exception
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.history.manager;

import java.io.File;
import java.util.Collections;
import java.util.Hashtable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.mambo.components.archiving.LimitedACStack;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationData;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.history.History;
import fr.soleil.mambo.history.archiving.ACHistory;
import fr.soleil.mambo.history.view.VCHistory;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;
import fr.soleil.mambo.tools.xmlhelpers.ac.ArchivingConfigurationXMLHelperFactory;
import fr.soleil.mambo.tools.xmlhelpers.vc.ViewConfigurationXMLHelper;

public class XMLHistoryManager implements IHistoryManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.history.manager.IHistoryManager#saveHistory(bensikin
	 * .bensikin.history.History, java.lang.String)
	 */
	public void saveHistory(History history, String historyResourceLocation)
			throws Exception {
		XMLUtils.save(history.toString(), historyResourceLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.history.manager.IHistoryManager#loadHistory(java.lang
	 * .String)
	 */
	public History loadHistory(String historyResourceLocation) throws Exception {
		// historyResourceLocation += "_sav"; //to drop
		Hashtable historyHt = loadHistoryIntoHash(historyResourceLocation);

		// BEGIN AC
		Hashtable acBook = (Hashtable) historyHt.get(ACHistory.AC_KEY);
		if (acBook == null) {
			return new History(new ACHistory(), new VCHistory());
		}

		ArchivingConfiguration selectedAc = (ArchivingConfiguration) acBook
				.get(ACHistory.SELECTED_AC_KEY);
		LimitedACStack openedAc = (LimitedACStack) acBook
				.get(ACHistory.OPENED_ACS_KEY);
		// END AC

		// BEGIN VC
		Hashtable vcBook = (Hashtable) historyHt.get(VCHistory.VC_KEY);

		ViewConfiguration selectedVc = (ViewConfiguration) vcBook
				.get(VCHistory.SELECTED_VC_KEY);
		LimitedVCStack openedVc = (LimitedVCStack) vcBook
				.get(VCHistory.OPENED_VCS_KEY);
		// END VC

		ACHistory aCHistory = new ACHistory(selectedAc, openedAc);
		VCHistory vCHistory = new VCHistory(selectedVc, openedVc);

		return new History(aCHistory, vCHistory);
	}

	/**
	 * @param path_xml
	 * @return
	 * @throws Exception
	 *             8 juil. 2005
	 */
	private static Hashtable loadHistoryIntoHash(String path_xml)
			throws Exception {
		File file = new File(path_xml);
		Node rootNode = XMLUtils.getRootNode(file);

		Hashtable retour = loadHistoryIntoHashFromRoot(rootNode);

		return retour;
	}

	/**
	 * @param rootNode
	 * @return 2 sept. 2005
	 * @throws Exception
	 */
	private static Hashtable loadHistoryIntoHashFromRoot(Node rootNode)
			throws Exception {
		Hashtable history = null;

		if (rootNode.hasChildNodes()) {
			NodeList bookNodes = rootNode.getChildNodes();
			history = new Hashtable();

			for (int i = 0; i < bookNodes.getLength(); i++)
			// as many loops as there are history parts in the saved file
			// (which is normally two: AC and VC)
			{
				Node currentBookNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentBookNode)) {
					continue;
				}

				// Hashtable ht =
				// Load AC was duplicated
				// loadACBook ( currentBookNode );

				String currentBookType = currentBookNode.getNodeName().trim();
				// System.out.println (
				// "loadHistoryIntoHashFromRoot/currentBookType/"+currentBookType+"/"
				// );
				Hashtable currentBook;// = new Hashtable ();
				if (ACHistory.AC_KEY.equals(currentBookType)) {
					currentBook = loadACBook(currentBookNode);
					history.put(ACHistory.AC_KEY, currentBook);
				} else if (VCHistory.VC_KEY.equals(currentBookType)) {
					// System.out.println ( "loadHistoryIntoHashFromRoot/VC_KEY"
					// );
					currentBook = loadVCBook(currentBookNode);
					history.put(VCHistory.VC_KEY, currentBook);
				}
			}
		}
		return history;
	}

	/**
	 * @param currentBookNode
	 * @return 2 sept. 2005
	 * @throws Exception
	 */
	private static Hashtable loadVCBook(Node currentBookNode) throws Exception {
		Hashtable ret = new Hashtable();

		if (currentBookNode.hasChildNodes()) {
			NodeList bookNodes = currentBookNode.getChildNodes();

			for (int i = 0; i < bookNodes.getLength(); i++) {
				Node currentSubNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentSubNode)) {
					continue;
				}

				String currentBookType = currentSubNode.getNodeName().trim();
				// System.out.println (
				// "loadVCBook/currentBookType/"+currentBookType+"/" );
				if (VCHistory.SELECTED_VC_KEY.equals(currentBookType)) {
					ViewConfiguration selectedVC = loadSelectedVCBook(currentSubNode);
					ret.put(VCHistory.SELECTED_VC_KEY, selectedVC);
				}
				if (VCHistory.OPENED_VCS_KEY.equals(currentBookType)) {
					LimitedVCStack openedVCs = loadOpenedVCBook(currentSubNode);
					// System.out.println ( "loadVCBook/OPENED_VCS_KEY" );
					ret.put(VCHistory.OPENED_VCS_KEY, openedVCs);
				}
			}
		}
		return ret;
	}

	/**
	 * @param currentSubNode
	 * @return
	 * @throws Exception
	 */
	private static LimitedVCStack loadOpenedVCBook(Node currentSubNode)
			throws Exception {
		LimitedVCStack openedVCs = new LimitedVCStack();

		if (currentSubNode.hasChildNodes()) {
			NodeList bookNodes = currentSubNode.getChildNodes();

			for (int i = 0; i < bookNodes.getLength(); i++) {
				Node currentSubSubNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentSubSubNode)) {
					continue;
				}

				String currentBookType = currentSubSubNode.getNodeName().trim();
				if (ViewConfiguration.XML_TAG.equals(currentBookType)) {

					ViewConfiguration openedVC = ViewConfigurationXMLHelper
							.loadViewConfigurationIntoHashFromRoot(currentSubSubNode);
					// System.out.println (
					// "loadOpenedVCBook/new VC!!!/name/"+openedVC.getData().getName()
					// );
					openedVCs.push(openedVC);
				}
			}
		}

		Collections.reverse(openedVCs);
		return openedVCs;
	}

	/**
	 * @param currentSubNode
	 * @return
	 * @throws Exception
	 */
	private static LimitedACStack loadOpenedACBook(Node currentSubNode)
			throws Exception {
		LimitedACStack openedACs = new LimitedACStack();

		if (currentSubNode.hasChildNodes()) {
			NodeList bookNodes = currentSubNode.getChildNodes();

			for (int i = 0; i < bookNodes.getLength(); i++) {
				Node currentSubSubNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentSubSubNode)) {
					continue;
				}

				String currentBookType = currentSubSubNode.getNodeName().trim();
				if (ArchivingConfiguration.XML_TAG.equals(currentBookType)) {
					// ArchivingConfiguration openedAC =
					// ArchivingConfigurationXMLHelper.loadArchivingConfigurationIntoHashFromRoot
					// ( currentSubSubNode );
					ArchivingConfiguration openedAC = ArchivingConfigurationXMLHelperFactory
							.getCurrentImpl()
							.loadArchivingConfigurationIntoHashFromRoot(
									currentSubSubNode);
					// System.out.println (
					// "loadOpenedACBook/new AC!!!/name/"+openedAC.getData().getName()
					// );
					openedACs.push(openedAC);
				}
			}
		}

		Collections.reverse(openedACs);
		return openedACs;
	}

	/**
	 * @param currentSubNode
	 * @return 2 sept. 2005
	 * @throws Exception
	 */
	private static ViewConfiguration loadSelectedVCBook(Node currentSubNode)
			throws Exception {
		if (currentSubNode.hasChildNodes()) {
			NodeList bookNodes = currentSubNode.getChildNodes();

			for (int i = 0; i < bookNodes.getLength(); i++) {
				Node currentSubSubNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentSubSubNode)) {
					continue;
				}

				String currentBookType = currentSubSubNode.getNodeName().trim();
				if (ViewConfiguration.XML_TAG.equals(currentBookType)) {
					ViewConfiguration selectedVC = ViewConfigurationXMLHelper
							.loadViewConfigurationIntoHashFromRoot(currentSubSubNode);
					return selectedVC;
				}
			}
		}
		return null;
	}

	/**
	 * @param currentBookNode
	 * @return 2 sept. 2005
	 * @throws Exception
	 */
	private static Hashtable loadACBook(Node currentBookNode) throws Exception {
		Hashtable ret = new Hashtable();

		if (currentBookNode.hasChildNodes()) {
			NodeList bookNodes = currentBookNode.getChildNodes();

			for (int i = 0; i < bookNodes.getLength(); i++) {
				Node currentSubNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentSubNode)) {
					continue;
				}

				String currentBookType = currentSubNode.getNodeName().trim();
				if (ACHistory.SELECTED_AC_KEY.equals(currentBookType)) {
					ArchivingConfiguration selectedAC = loadSelectedACBook(currentSubNode);
					ret.put(ACHistory.SELECTED_AC_KEY, selectedAC);
				}
				if (ACHistory.OPENED_ACS_KEY.equals(currentBookType)) {
					LimitedACStack openedACs = loadOpenedACBook(currentSubNode);
					// System.out.println ( "loadVCBook/OPENED_VCS_KEY" );
					ret.put(ACHistory.OPENED_ACS_KEY, openedACs);
				}
			}
		}
		return ret;
	}

	/**
	 * @param currentSubNode
	 * @return 2 sept. 2005
	 * @throws Exception
	 */
	private static ArchivingConfiguration loadSelectedACBook(Node currentSubNode)
			throws Exception {
		if (currentSubNode.hasChildNodes()) {
			NodeList bookNodes = currentSubNode.getChildNodes();

			for (int i = 0; i < bookNodes.getLength(); i++) {
				Node currentSubSubNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentSubSubNode)) {
					continue;
				}

				String currentBookType = currentSubSubNode.getNodeName().trim();
				if (ArchivingConfigurationData.XML_TAG.equals(currentBookType)) {
					ArchivingConfiguration selectedAC = ArchivingConfigurationXMLHelperFactory
							.getCurrentImpl()
							.loadArchivingConfigurationIntoHashFromRoot(
									currentSubSubNode);
					// ArchivingConfiguration selectedAC =
					// ArchivingConfigurationXMLHelper.loadArchivingConfigurationIntoHashFromRoot
					// ( currentSubSubNode );
					return selectedAC;
				}
			}
		}
		return null;
	}

}
