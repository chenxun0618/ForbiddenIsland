package forbiddenIsland;

import java.util.Iterator;


class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    //represents this list as an iterator
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    //removes the first item from the list if has any
    public IList<T> removeThis(T t) {
        if (t == first) {
            return rest;
        }
        else {
            return new ConsList<T>(this.first, this.rest.removeThis(t));
        }
    }

    //determines if this list is a cons list
    public boolean isCons() {
        return true;
    }

    //makes this list to be a cons list
    public ConsList<T> asCons() {
        return this;
    }


}
