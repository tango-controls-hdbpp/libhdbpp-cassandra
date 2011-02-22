package fr.soleil.mambo.comparators;

import java.text.Collator;
import java.util.Comparator;

import fr.soleil.mambo.data.view.ExpressionAttribute;

public class ExpressionAttributeComparator implements
        Comparator<ExpressionAttribute> {

    public ExpressionAttributeComparator() {
        super();
    }

    @Override
    public int compare(ExpressionAttribute o1, ExpressionAttribute o2) {
        String name1 = null;
        String name2 = null;
        if (o1 != null) {
            name1 = o1.getName();
        }
        if (o2 != null) {
            name2 = o2.getName();
        }
        return Collator.getInstance().compare(name1, name2);
    }

}
