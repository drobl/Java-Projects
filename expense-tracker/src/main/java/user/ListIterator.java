package user;

import java.util.List;

/**
 * This class contains the methods that allow the use of the Iterator Design Pattern on the List
 * collection
 * 
 * @author Daniel Robles
 */
public class ListIterator<Type> implements IteratorInterface {

  private int index;
  private int listSize;
  private List<Type> workingList;

  public ListIterator(List<Type> newList) {
    this.workingList = newList;
    this.listSize = newList.size();
  }

  @Override
  public boolean hasNext() {
    return index < listSize && workingList.get(index) != null;
  }

  @Override
  public Object next() {
    if (this.hasNext()) {
      return workingList.get(index++);
    }
    return null;
  }
}
