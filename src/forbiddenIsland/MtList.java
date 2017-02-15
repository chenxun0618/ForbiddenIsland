package forbiddenIsland;

import java.util.Iterator;

//to represent an empty list
class MtList<T> implements IList<T> {

    //represents this list as an iterator
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    //removes the first item from the list if has any
    public IList<T> removeThis(T t) {
        return this;
    }

    //determines if this list is a cons list
    public boolean isCons() {
        return false;
    }

    //makes this list to be a cons list
    public ConsList<T> asCons() {
        throw new IllegalArgumentException("Cannot convert empty to cons");
    }


}
