package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.global;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResult;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResultLine;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ModeData;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.modes.IModeController;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.modes.ModeControllerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

/**
 * A basic implementation. Does real database controls but doesn't do anything
 * with the result except printing it.
 * 
 * @author CLAISSE
 */
public abstract class ControllerAdapter implements IController {
    private Hashtable attributesToControl;

    private static final String GLOBAL_RETRY_COMMAND_NAME = "RetryForAttributes";
    private static final int GLOBAL_RETRY_COMMAND_TIMEOUT = 10000;

    /*
     * (non-Javadoc)
     * 
     * @see archwatch.strategy.control.global.IController#control()
     */
    public ControlResult control(final boolean doArchiverDiagnosis) throws DevFailed {
	// System.out.println ( "BasicController/control/START" );
	// long t0 = System.currentTimeMillis();
	final ControlResult ret = new ControlResult();
	final IModeController modeController = ModeControllerFactory.getCurrentImpl();

	if (attributesToControl == null || attributesToControl.size() == 0) {
	    ret.setCode(ControlResult.NO_ATTRIBUTES_TO_CONTROL);
	    return ret;
	}

	final Enumeration enumeration = attributesToControl.keys();
	while (enumeration.hasMoreElements()) {
	    final String nextCompleteName = (String) enumeration.nextElement();
	    final ArchivingAttribute nextAttr = new ArchivingAttribute();
	    nextAttr.setCompleteName(nextCompleteName);
	    int[] result = null;

	    final ModeData nextModeData = (ModeData) attributesToControl.get(nextCompleteName);
	    if (nextModeData.isIncomplete()) {
		nextAttr.setDetermined(false);
	    } else {
		final Mode nextMode = nextModeData.getMode();

		if (nextMode.getModeP() == null) {
		    Tools.trace("ControllerAdapter/control/modeP == null!",
			    Warnable.LOG_LEVEL_ERROR);
		}

		result = modeController.controlMode(nextMode, nextAttr);
	    }

	    final ControlResultLine line = new ControlResultLine(nextAttr, nextModeData, result);
	    ret.addLine(line);
	}

	return ret;
    }

    private static boolean resultContainsUndeterminedMode(final int[] result) {
	if (result == null) {
	    return false;
	}
	for (final int element : result) {
	    // System.out.println (
	    // "CLA/BasicController/resultContainsUndeterminedMode/i|"+i+"|result [ i ]|"+result
	    // [ i ] );
	    if (element == IModeController.CONTROL_FAILED) {
		return true;
	    }
	}
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * archwatch.strategy.control.global.IController#serAttributesToControl(
     * java.util.Hashtable)
     */
    public void setAttributesToControl(final Hashtable _attributesToControl) {
	attributesToControl = _attributesToControl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * archwatch.strategy.control.global.IController#doSomethingAboutActionResult
     * (int)
     */
    public void doSomethingAboutActionResult(final int actionResult) {
	switch (actionResult) {
	case IController.NO_ACTION_NEEDED:
	    Tools.trace("doSomethingAboutActionResult/No action needed being undertaken",
		    Warnable.LOG_LEVEL_DEBUG);
	    break;

	case IController.ACTION_SUCCESSED:
	    Tools.trace("doSomethingAboutActionResult/The action successed !",
		    Warnable.LOG_LEVEL_INFO);
	    break;

	case IController.ACTION_FAILED:
	    Tools
		    .trace("doSomethingAboutActionResult/The action failed !",
			    Warnable.LOG_LEVEL_WARN);
	    break;
	}
    }

    public static int doRetry(final ControlResult control) {
	final Map<String, Archiver> errorArchivers = control.getErrorArchivers();
	if (errorArchivers == null) {
	    return IController.NO_ACTION_NEEDED;
	}

	for (final Map.Entry<String, Archiver> entry : errorArchivers.entrySet()) {

	    // final Enumeration<String> koArchivers = errorArchivers.keys();
	    // while (koArchivers.hasMoreElements()) {
	    final String nextArchiver = entry.getKey();
	    final Archiver archiver = entry.getValue();

	    final Hashtable htKOAttributess = archiver.getKOAttributes();
	    final Enumeration koAttributes = htKOAttributess.keys();
	    final String[] parameters = new String[htKOAttributess.size()];
	    int i = 0;
	    while (koAttributes.hasMoreElements()) {
		final String nextAttributeName = (String) koAttributes.nextElement();
		parameters[i] = nextAttributeName;
		i++;
	    }

	    try {
		final DeviceProxy proxy = new DeviceProxy(nextArchiver);
		proxy.set_timeout_millis(GLOBAL_RETRY_COMMAND_TIMEOUT);
		final DeviceData argin = new DeviceData();
		argin.insert(parameters);

		proxy.command_inout_asynch(GLOBAL_RETRY_COMMAND_NAME, argin, true);
	    } catch (final DevFailed e) {
		Tools.trace("ControllerAdapter/doRetry/DevFailed for archiver|" + nextArchiver
			+ "| (" + htKOAttributess.size() + " attributes)", e,
			Warnable.LOG_LEVEL_WARN);
		return IController.ACTION_FAILED;
	    } catch (final Throwable e) {
		Tools.trace("ControllerAdapter/doRetry/Exception for archiver|" + nextArchiver
			+ "| (" + htKOAttributess.size() + " attributes)", e,
			Warnable.LOG_LEVEL_ERROR);
		return IController.ACTION_FAILED;
	    }
	}
	return IController.ACTION_SUCCESSED;
    }

}
