package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Archiver;

public class ArchiversComparator implements Comparator<Archiver> {
	private Collator referenceComparator;

	public ArchiversComparator() {
		referenceComparator = Collator.getInstance(Locale.FRENCH);
	}

	public int compare(Archiver o1, Archiver o2) {
		boolean doCompare = false;
		if (o1 != null && o2 != null) {
			if (o1 instanceof Archiver && o2 instanceof Archiver) {
				doCompare = true;
			}
		}

		if (doCompare) {
			String name1 = ((Archiver) o1).getName();
			String name2 = ((Archiver) o2).getName();
			System.out.println("ArchiversComparator = " + name1 + " / " + name2);
			return referenceComparator.compare(name1, name2);
		} else {
			return 0;
		}
	}


}