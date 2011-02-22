package Common.Archiver.Collector;

/**
 * This class manages all the useful counters for the ModeHandler decision for
 * one attribute
 * 
 * @author PIERREJOSEPH
 */
public class ModesCounters {
	private int m_countModePeriodic;
	private int m_countModeAbsolute;
	private int m_countModeRelative;
	private int m_countModeThreshold;
	private int m_countModeDifference;
	private int m_countModeCalcul;
	private int m_countModeExtern;

	private static final int INIT_COUNTER_VALUE = 1;

	/**
	 * Creates a new instance of AttributeModeHandler
	 */
	public ModesCounters() {
		init();
	}

	public void init() {
		initCountModeP();
		initCountModeA();
		initCountModeR();
		initCountModeT();
		initCountModeD();
		initCountModeC();
		initCountModeE();
	}

	/** ======================= Init methods ======================== **/
	public void initCountModeP() {
		m_countModePeriodic = INIT_COUNTER_VALUE;
	}

	public void initCountModeA() {
		m_countModeAbsolute = INIT_COUNTER_VALUE;
	}

	public void initCountModeR() {
		m_countModeRelative = INIT_COUNTER_VALUE;
	}

	public void initCountModeT() {
		m_countModeThreshold = INIT_COUNTER_VALUE;
	}

	public void initCountModeD() {
		m_countModeDifference = INIT_COUNTER_VALUE;
	}

	public void initCountModeC() {
		m_countModeCalcul = INIT_COUNTER_VALUE;
	}

	public void initCountModeE() {
		m_countModeExtern = INIT_COUNTER_VALUE;
	}

	/** ======================= Gets methods ======================== **/
	public int getCountModeP() {
		return m_countModePeriodic;
	}

	public int getCountModeA() {
		return m_countModeAbsolute;
	}

	public int getCountModeR() {
		return m_countModeRelative;
	}

	public int getCountModeT() {
		return m_countModeThreshold;
	}

	public int getCountModeD() {
		return m_countModeDifference;
	}

	public int getCountModeC() {
		return m_countModeCalcul;
	}

	public int getCountModeE() {
		return m_countModeExtern;
	}

	/** ======================= Increments methods ======================== **/
	public void incremCountModeP() {
		m_countModePeriodic++;
	}

	public void incremCountModeA() {
		m_countModeAbsolute++;
	}

	public void incremCountModeR() {
		m_countModeRelative++;
	}

	public void incremCountModeT() {
		m_countModeThreshold++;
	}

	public void incremCountModeD() {
		m_countModeDifference++;
	}

	public void incremCountModeC() {
		m_countModeCalcul++;
	}

	public void incremCountModeE() {
		m_countModeExtern++;
	}

	public String toString() {
		return ("P=" + m_countModePeriodic + " A=" + m_countModeAbsolute
				+ " R=" + m_countModeRelative + " T=" + m_countModeThreshold
				+ " C=" + m_countModeCalcul + " D=" + m_countModeDifference
				+ "E=" + m_countModeExtern);
	}
}
