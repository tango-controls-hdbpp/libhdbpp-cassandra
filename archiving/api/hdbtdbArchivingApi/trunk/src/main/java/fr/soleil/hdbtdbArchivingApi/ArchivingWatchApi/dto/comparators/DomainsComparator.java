package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Domain;

public class DomainsComparator implements Comparator<Domain> {
	private Collator referenceComparator;

	public DomainsComparator() {
		referenceComparator = Collator.getInstance(Locale.FRENCH);
	}

	public int compare(Domain o1, Domain o2) {
		boolean doCompare = false;
		if (o1 != null && o2 != null) {
			if (o1 instanceof Domain && o2 instanceof Domain) {
				doCompare = true;
			}
		}

		if (doCompare) {
			String name1 = ((Domain) o1).getName();
			String name2 = ((Domain) o2).getName();
			return referenceComparator.compare(name1, name2);
		} else {
			return 0;
		}
	}
}