package fr.soleil.mambo.docking;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.Action;

import fr.soleil.util.docking.ViewFactory;
import fr.soleil.util.docking.action.ViewAction;
import fr.soleil.util.docking.api.IView;
import fr.soleil.util.docking.api.IViewFactory;

/**
 * Another implementation of an {@link IViewFactory}. This implementation is
 * very similar to {@link ViewFactory}, except that the {@link IView} id is not
 * particularly supposed to be an {@link Integer}.
 * 
 * @author GIRARDOT
 */
public class DefaultViewFactory implements IViewFactory {

    protected List<IView>           views;
    protected PropertyChangeSupport support;

    public DefaultViewFactory() {
        super();
        views = new ArrayList<IView>();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public List<Action> getActionList() {
        List<Action> result = new ArrayList<Action>(views.size());
        for (IView view : views) {
            result.add(new ViewAction(view));
        }
        return result;
    }

    @Override
    public IView getView(Object id) {
        for (IView view : views) {
            if (view.getId().equals(id)) {
                return view;
            }
        }
        return null;
    }

    /**
     * Adds an {@link IView} in the list of views, and tries to put it in the
     * docking area.
     * 
     * @param view
     *            the {@link IView} to add
     * @return The {@link IView} if it was successfully added, <code>null</code>
     *         otherwise.
     */
    public IView addView(IView view) {
        if (views.add(view)) {
            support.firePropertyChange(VIEWS, null, view);
        }
        else {
            view = null;
        }
        return view;
    }

    /**
     * Removes an {@link IView}, determined by an id, from list and from the
     * docking area.
     * 
     * @param id
     *            The id of the {@link IView} to remove.
     * @return The removed {@link IView} if such an {@link IView} existed and
     *         was successfully removed, <code>null</code> otherwise.
     */
    public IView removeId(Object id) {
        IView toRemove = getView(id);
        if ((toRemove != null) && (views.remove(toRemove))) {
            support.firePropertyChange(VIEWS, toRemove, null);
            return toRemove;
        }
        else {
            return null;
        }
    }

    @Override
    public List<IView> getViews() {
        return views;
    }

    @Override
    public void loadPreferences(Preferences prefs) {
        // Does nothing in particular. If you expect something to be done, you
        // should extend this class and override this method
    }

    @Override
    public void savePreferences(Preferences prefs) {
        // Does nothing in particular. If you expect something to be done, you
        // should extend this class and override this method
    }

    @Override
    public Object readViewId(ObjectInputStream in) throws IOException {
        Object id = null;
        try {
            id = in.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int size() {
        return views.size();
    }

    @Override
    public void writeViewId(Object id, ObjectOutputStream out)
            throws IOException {
        out.writeObject(id);
    }

}
