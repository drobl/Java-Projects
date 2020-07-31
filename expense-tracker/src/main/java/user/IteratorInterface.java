package user;

/**
 * Interface part of the iterator design pattern, implemented in AccountIterator class
 */

public interface IteratorInterface {
  boolean hasNext();

  Object next();
}
