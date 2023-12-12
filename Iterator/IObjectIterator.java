package Iterator;

import java.util.ArrayList;

public interface IObjectIterator<T> {
    boolean hasNext(T object);
    Object getNext(T object);
    Object find(T data);
}
