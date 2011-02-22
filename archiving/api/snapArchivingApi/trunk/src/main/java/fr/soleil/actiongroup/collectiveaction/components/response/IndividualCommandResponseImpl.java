package fr.soleil.actiongroup.collectiveaction.components.response;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;

public class IndividualCommandResponseImpl extends IndividualResponseImpl implements IndividualCommandResponse 
{
    private DeviceDataWrapper data;
    
    public IndividualCommandResponseImpl(String deviceName) 
    {
        super ( deviceName );
    }

    public DeviceDataWrapper get_data() 
    {
        return this.data;
    }

    public void setData(DeviceDataWrapper _data) 
    {
        this.data = _data;
    }
}
