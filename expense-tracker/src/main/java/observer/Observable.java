package observer;

import java.util.ArrayList;

/**
 * Represents an observable object in the "Observer pattern". Other objects that implement the
 * {@link Observer} interface may observe objects of this class for any changes.
 * 
 * @author Marko Zunic
 */
public class Observable {
  private ArrayList<Observer> observerList = new ArrayList<>();

  /**
   * Registers the provided object as an observer and notifies it of eventual changes made to this
   * object
   */
  public void registerObserver(Observer ob) {
    observerList.add(ob);
  }

  /**
   * Unregisters an observer object. That observer won't be notified of eventual changes made to
   * this object anymore
   */
  public void removeObserver(Observer ob) {
    observerList.remove(ob);
  }

  /**
   * Notifies all observers that some change has been mode to this object.
   */
  protected void notifyObservers() {
    observerList.forEach(ob -> ob.update(this));
  }

}
