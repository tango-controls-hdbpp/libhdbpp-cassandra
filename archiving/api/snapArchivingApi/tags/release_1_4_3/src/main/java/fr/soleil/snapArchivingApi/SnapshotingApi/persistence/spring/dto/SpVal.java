package fr.soleil.snapArchivingApi.SnapshotingApi.persistence.spring.dto;

import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance.AnyAttribute;
import fr.soleil.snapArchivingApi.SnapshotingApi.persistence.context.SnapshotPersistenceContext;

public class SpVal extends Val
{
    private int dimX;
    
    public SpVal ()
    {
       
    }
    
    public SpVal(AnyAttribute attribute, SnapshotPersistenceContext context) 
    {
        super ( attribute , context ) ;
        this.dimX = attribute.getDimX();
    }

    /**
     * @return the dimX
     */
    public int getDimX() {
        return this.dimX;
    }

    /**
     * @param dimX the dimX to set
     */
    public void setDimX(int dimX) {
        this.dimX = dimX;
    }
}
