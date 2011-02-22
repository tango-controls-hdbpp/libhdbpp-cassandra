package fr.soleil.mambo.thread.manager;

import fr.soleil.mambo.thread.VCRefreshThread;

public class VCRefreshThreadManager {

    private static VCRefreshThreadManager instance = null;
    private VCRefreshThread               refreshThread;

    public static VCRefreshThreadManager getInstance() {
        if (instance == null) {
            instance = new VCRefreshThreadManager();
        }
        return instance;
    }

    private VCRefreshThreadManager() {
        super();
    }

    public VCRefreshThread getRefreshThread() {
        return refreshThread;
    }

    public void setRefreshThread(VCRefreshThread refreshThread) {
        this.refreshThread = refreshThread;
    }

}
