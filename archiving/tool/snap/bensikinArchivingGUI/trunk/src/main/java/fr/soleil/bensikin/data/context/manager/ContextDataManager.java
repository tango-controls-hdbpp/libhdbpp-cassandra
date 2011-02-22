package fr.soleil.bensikin.data.context.manager;

import java.util.HashMap;

import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.datasources.snapdb.SnapManagerFactory;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class ContextDataManager {
    private static ContextDataManager     instance    = new ContextDataManager();
    private HashMap<Integer, ContextData> contextsMap = new HashMap<Integer, ContextData>();

    public static ContextDataManager getInstance() {
        return instance;
    }

    public ContextData getContextData(int contextId) {
        if (!contextsMap.containsKey(contextId)) {
            try {
                loadContextByDb(contextId);
            } catch (SnapshotingException e) {
                e.printStackTrace();
            }
        }
        return contextsMap.get(contextId);
    }

    private void addContextData(int contextId, ContextData contextData) {
        contextsMap.put(contextId, contextData);
    }

    //
    // private void removeContextData(int contextId) {
    // contextsMap.remove(contextId);
    // }
    //
    // private int getSize() {
    // return contextsMap.size();
    // }

    private void loadContextByDb(int contextId) throws SnapshotingException {
        Condition condition = new Condition(GlobalConst.TAB_CONTEXT[0],
                GlobalConst.OP_EQUALS, String.valueOf(contextId));
        Criterions searchCriterions = new Criterions();
        searchCriterions.addCondition(condition);
        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        SnapContext[] contexts = source.findContexts(searchCriterions);
        if ((contexts != null) && (contexts.length > 0)) {
            ContextData context = new ContextData(contexts[0]);
            this.addContextData(contextId, context);
        }
    }

    public int getContextIdBySnapshotId(int snapshotId)
            throws SnapshotingException {
        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        int contextId = source.findContextId(snapshotId);

        ContextData res = this.getContextData(contextId);
        if (res == null) {
            // ContextData unknown into database
            return -1;
        }
        return contextId;
    }
}
