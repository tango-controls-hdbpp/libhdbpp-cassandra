package fr.soleil.mambo.models;

import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.tango.util.entity.data.Attribute;

public abstract class ACTreeModel extends
        AttributesTreeModel<ArchivingConfigurationAttribute> {

    private static final long serialVersionUID = 8145772722289232453L;

    public ACTreeModel() {
        super();
    }

    @Override
    public ArchivingConfigurationAttribute getValidAttributeFor(Attribute attr) {
        if (attr == null) {
            return null;
        }
        else if (attr instanceof ArchivingConfigurationAttribute) {
            return (ArchivingConfigurationAttribute) attr;
        }
        else {
            return new ArchivingConfigurationAttribute(attr);
        }
    }

}
