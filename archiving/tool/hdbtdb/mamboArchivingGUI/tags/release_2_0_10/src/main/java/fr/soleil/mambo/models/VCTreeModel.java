package fr.soleil.mambo.models;

import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.tango.util.entity.data.Attribute;

public abstract class VCTreeModel extends
        AttributesTreeModel<ViewConfigurationAttribute> {

    private static final long serialVersionUID = 1941227864290547610L;

    public VCTreeModel() {
        super();
    }

    @Override
    public ViewConfigurationAttribute getValidAttributeFor(Attribute attr) {
        if (attr == null) {
            return null;
        }
        else if (attr instanceof ViewConfigurationAttribute) {
            return (ViewConfigurationAttribute) attr;
        }
        else {
            return new ViewConfigurationAttribute(attr);
        }
    }

}
