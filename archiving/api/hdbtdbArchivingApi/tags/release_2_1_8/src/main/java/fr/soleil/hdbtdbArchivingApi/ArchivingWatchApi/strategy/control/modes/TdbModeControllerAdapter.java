package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.modes;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.TdbSpec;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;

/**
 * A minimal implementation that returns CONTROL_NOT_DONE for every mode control
 * @author CLAISSE 
 */
public class TdbModeControllerAdapter implements IModeController 
{
    public TdbModeControllerAdapter() 
    {
        super();
    }
    
    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode, archwatch.dto.Attribute)
     */
    protected int controlPeriodicMode(ModePeriode modeP, TdbSpec spec, ArchivingAttribute attr) throws DevFailed 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu, archwatch.dto.Attribute)
     */
    protected int controlAbsoluteMode(ModeAbsolu modeA, TdbSpec spec, ArchivingAttribute attr) 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif, archwatch.dto.Attribute)
     */
    protected int controlRelativeMode(ModeRelatif modeR, TdbSpec spec, ArchivingAttribute attr) 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil, archwatch.dto.Attribute)
     */
    protected int controlThresholdMode(ModeSeuil modeT, TdbSpec spec, ArchivingAttribute attr) 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference, archwatch.dto.Attribute)
     */
    protected int controlDifferenceMode(ModeDifference modeD, TdbSpec spec, ArchivingAttribute attr) 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul, archwatch.dto.Attribute)
     */
    protected int controlCalculationMode(ModeCalcul modeC, TdbSpec spec, ArchivingAttribute attr) 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne, archwatch.dto.Attribute)
     */
    protected int controlExternalMode(ModeExterne modeE, TdbSpec spec, ArchivingAttribute attr) 
    {
        return IModeController.CONTROL_NOT_DONE;
    }

    /* (non-Javadoc)
     * @see archwatch.strategy.control.modes.IModeController#controlMode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode, archwatch.dto.Attribute)
     */
    public int [] controlMode ( Mode mode, ArchivingAttribute attr ) throws DevFailed 
    {
        int [] ret = new int [ IModeController.NUMBER_OF_MODES_TO_CONTROL ];
        if ( mode == null )
        {
            return ret;
        }
        
        ret [ IModeController.MODE_P_POSITION ] = this.controlPeriodicMode ( mode.getModeP () , mode.getTdbSpec () , attr );
        ret [ IModeController.MODE_A_POSITION ] = this.controlAbsoluteMode ( mode.getModeA () , mode.getTdbSpec () , attr );
        ret [ IModeController.MODE_R_POSITION ] = this.controlRelativeMode ( mode.getModeR () , mode.getTdbSpec () , attr );
        ret [ IModeController.MODE_T_POSITION ] = this.controlThresholdMode ( mode.getModeT () , mode.getTdbSpec () , attr );
        ret [ IModeController.MODE_D_POSITION ] = this.controlDifferenceMode ( mode.getModeD () , mode.getTdbSpec () , attr );
        ret [ IModeController.MODE_C_POSITION ] = this.controlCalculationMode ( mode.getModeC () , mode.getTdbSpec () , attr );
        ret [ IModeController.MODE_E_POSITION ] = this.controlExternalMode ( mode.getModeE () , mode.getTdbSpec () , attr );
        
        return ret;
    }

}
