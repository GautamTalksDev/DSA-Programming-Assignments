import java.util.Collections;
import java.util.LinkedList;

//Sequence-based Partition class using linked lists
public class Partition<E> {
    static class Cluster<E> {
        Node<E> head, tail;
        int counter; //This counts the number of nodes in this cluster

        //Appends node p at the end of the cluster
        void addToNode(Node<E> p) {
            if(head == null) {
                head = tail = p;
                p.previous = null;
                p.next = null;
            } else {
                tail.next = p;
                p.previous = tail;
                p.next = null;
                tail = p;
            }
            p.newNode = this;
            counter++;
        }

        // This
        void removeFromNode(Cluster<E> newCluster) {
            Node<E> node = newCluster.head;
            while(node != null) {
                Node<E> atNode = node.next;
                node.previous = node.next = null;
                this.addToNode(node);
                node = atNode;
            }
            newCluster.head = null;
            newCluster.tail = null;
            newCluster.counter = 0;
        }
    }

    private final LinkedList<Cluster<E>> newList = new LinkedList<>();

    //This helps to crete a singleton cluster containing x; and then later returns its node.
    public Node<E> makeCluster(E x) {
        Node<E> node = new Node<>(x);
        Cluster<E> lc = new Cluster<>();
        lc.addToNode(node);
        newList.add(lc);
        return node;
    }

    //Returns the leader (head) of the cluster containing fn.
    public Node<E> find(Node<E> fn) {
        if(fn == null) {
            return null;
        }
        if(fn.newNode == null) {
            return null;
        }
        return fn.newNode.head;
    }

    //This helps to merge clusters of n and m by moving the smaller into the larger.
    public void union(Node<E> n, Node<E> m) {
        if(n == null) {
            return;
        }
        if(m == null) {
            return;
        }

        Cluster<E> one = n.newNode;
        Cluster<E> two = m.newNode;

        if(one == null) {
            return;
        }

        if(two == null) {
            return;
        }

        if(one == two) {
            return;
        }

        Cluster<E> firstClus;
        Cluster<E> secondClus;

        if(one.counter >= two.counter) {
            firstClus = one;
            secondClus = two;
        } else {
            firstClus = two;
            secondClus = one;
        }

        firstClus.removeFromNode(secondClus);

        for(int i = 0; i < newList.size(); i++) {
            if(newList.get(i) == secondClus) {
                newList.remove(i);
                break;
            }
        }
    }

    // Returns the element stores at node p
    public E element(Node<E> p) {
        if(p == null) {
            return null;
        } else {
            return p.elem;
        }
    }

    //Number of clusters
    public int numberOfClusters() {
        return newList.size();
    }

    // Size of the cluster containing p
    public int clusterSize(Node<E> p) {
        if(p == null) {
            return 0;
        }

        if(p.newNode == null) {
            return 0;
        }

        return p.newNode.counter;
    }

    //All nodes in p's cluster
    public java.util.List<Node<E>> clusterPositions(Node<E> p) {
        LinkedList<Node<E>> newLL = new LinkedList<>();

        if(p == null) {
            return newLL;
        }
        if(p.newNode == null) {
            return newLL;
        }

        Node<E> atPos = p.newNode.head;

        while(atPos != null) {
            newLL.add(atPos);
            atPos = atPos.next;
        }
        return newLL;
    }

    //Sizes of all clusters, sorted in decreasing order
    public java.util.List<Integer> clusterSizes() {
        LinkedList<Integer> newInt = new LinkedList<>();
        for(int i = 0; i < newList.size(); i++) {
            newInt.add(newList.get(i).counter);
        }
        Collections.sort(newInt, Collections.reverseOrder());
        return newInt;
    }
}
