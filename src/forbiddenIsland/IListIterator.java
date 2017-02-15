package forbiddenIsland;

import java.util.Iterator;

//an iterator class
class IListIterator<T> implements Iterator<T> {
    IList<T> items;
    IListIterator(IList<T> items) {
        this.items = items;
    }
    // In IListIterator
    public boolean hasNext() {
        return this.items.isCons();
    }

    // In IListIterator
    public T next() {
        ConsList<T> itemsAsCons = this.items.asCons();
        T answer = itemsAsCons.first;
        this.items = itemsAsCons.rest;
        return answer;
    }

    //removes item in list
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
}
