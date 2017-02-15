package forbiddenIsland;


//to represent an IList
interface IList<T> extends Iterable<T> {
    //removes the first item from the list if has any
    IList<T> removeThis(T t);

    //determines if this list is a cons list
    public boolean isCons();

    //makes this list to be a cons list
    public ConsList<T> asCons();

}