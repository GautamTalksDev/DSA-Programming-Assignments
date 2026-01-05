/*
 This is the main Node class implementation.
 This Node class is also being used by the Partition.java class
 */
public class Node<E> {

    //Elements gets stored here
    E elem;
    Node<E> previous, next;
    Partition.Cluster<E> newNode;

    //Creating a node that holds n
    public Node(E n) {
        this.elem = n;
    }

    //Returning the stores element
    public E getElem() {
        return elem;
    }
}
