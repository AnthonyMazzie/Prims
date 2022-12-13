import java.util.*;

public class Main {

    /* MinHeap represents a k-ary heap data structure of nodeElement type objects Note: the "k" represents the quantity of children each node has */
    static class MinHeap {

        /* Initialize an ArrayList of Edge type objects */
        public ArrayList<Edge> heap;

        /* The number of children each node has, "k" */
        public int numberOfChildrenForEachNode;

        /* DEFAULT CONSTRUCTOR, INITIATES A MIN HEAP WITH DEFAULT VALUE K = 2 */
        public MinHeap() {
            numberOfChildrenForEachNode = 2;
            heap = new ArrayList<>();
        }

        /* Check if heap is empty */
        public boolean isEmpty() {
            return heap.size() == 0;
        }

        /* Get index parent of i */
        private int parent(int i) {
            return (i - 1) / numberOfChildrenForEachNode;
        }

        /* Get index of k'th child of i */
        private int kthChild(int i, int k) {
            return numberOfChildrenForEachNode * i + k;
        }

        /* Adds an edge element to the heap */
        public void add(Node src, Node dst, int weight) {

            /* Always nodes add to the end of the array list and heapify up if applicable */
            heap.add(heap.size(), new Edge(src, dst, weight));

            /* If there exists more than one node in the heap, heapify up */
            if (heap.size() > 1) {
                heapifyUp((heap.size() - 1));
            }
        }

        /* Returns minimum value child index */
        private int minChild(int ind) {
            int minChildIndex = kthChild(ind, 1);
            int k = 2;
            int kthChild = kthChild(ind, k);
            while ((k <= numberOfChildrenForEachNode) && (kthChild < heap.size())) {
                if (heap.get(kthChild).weight < heap.get(minChildIndex).weight) {
                    minChildIndex = kthChild;
                }
                kthChild = kthChild(ind, k++);
            }
            return minChildIndex;
        }

        /* Search the heap for this edge, return true if heap already contains */
        public boolean containsEdge(Node src, Node dst, int weight) {
            for (Edge edge : heap) {
                if (edge.source == src && edge.destination == dst && edge.weight == weight) {
                    return true;
                }
            }
            return false;
        }

        /* Heapify the Min Heap up, ensures the minimum weight Edge is at the root of the heap */
        public void heapifyUp(int childInd) {

            Edge tmp = heap.get(childInd);

            while (childInd > 0) {
                /* If current weight is less than parent weight, move up */
                if (tmp.weight < heap.get(parent(childInd)).weight) {
                    Collections.swap(heap, childInd, parent(childInd));
                }
                /* Iterate index up */
                childInd = parent(childInd);
            }
        }

        /* Heapify the Min Heap down, ensures the minimum weight Edge is at the root of the heap */
        private void heapifyDown(int ind) {

            int childIndex;
            Edge tempElement = heap.get(ind);
            while (kthChild(ind, 1) < heap.size()) {
                childIndex = minChild(ind);
                if (heap.get(childIndex).weight < tempElement.weight) {
                    Collections.swap(heap, ind, childIndex);
                } else {
                    break;
                }
                ind = childIndex;
            }
            heap.set(ind, tempElement);
        }

        /* Poll for edge. Return and remove the minimum weight element at the groot of the heap. */
        public Edge poll() {
            if (isEmpty()) {
                throw new NoSuchElementException("Underflow Exception");
            }
            int rootOfHeap = 0;
            Edge returnThisEdge = heap.get(rootOfHeap);
            heap.remove(rootOfHeap);
            if (heap.size() >= 1) {
                heapifyDown(rootOfHeap);
            }
            return returnThisEdge;
        }
    }

    /* Node class represents a Node in a graph */
    public static class Node {

        /* Node identifier */
        private final int nodeID;

        /* Node visited? */
        private boolean visited;

        /* Node constructor */
        public Node(int nodeID) {
            this.nodeID = nodeID;
            this.visited = false;
        }

        /* Set this Node as visited */
        public void setVisited(boolean visited) {
            this.visited = visited;
        }
    }

    /* Edge class represents an Edge in a graph */
    public static class Edge {

        /* Source node */
        private final Node source;

        /* Destination node */
        private final Node destination;

        /* Edge weight */
        private final int weight;

        /* Edge constructor */
        public Edge(Node source, Node destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    /* Prim's Algorithm, returns Minimum Spanning Tree  */
    static void primsAlgorithm(List<Edge> edgeList) {

        /* Initialize a new MinHeap of Edge type objects */
        MinHeap mH = new MinHeap();

        /* Initialize a new ArrayList to store visited node ID's */
        ArrayList<Integer> visitedNodes = new ArrayList<>();

        /* Loop edges and find first adjacent node with minimum weight from source node. Note, source node is visited by default. */
        for (Edge edge : edgeList) {
            if (edge.source.visited && !edge.destination.visited) {
                /* Only add nodes to visited array if they do not already exist in array */
                if (!visitedNodes.contains(edge.source.nodeID)) {
//                    System.out.println("Visiting: " + edge.source.nodeID);
                    visitedNodes.add(edge.source.nodeID);
                }
                mH.add(edge.source, edge.destination, edge.weight);
            }
        }

        /* Loop until MinHeap is empty */
        while (!mH.isEmpty()) {

            /* Retrieve and remove first (minimum weight) edge from the MinHeap */
            Edge currentEdge = mH.poll();

            /* Add destination of new minimum weight node to array, but only if not already in array */
            if (!visitedNodes.contains(currentEdge.destination.nodeID)) {
//                System.out.println("Visiting: " + currentEdge.destination.nodeID);
                visitedNodes.add(currentEdge.destination.nodeID);
            }

            /* Set destination of this current edge as visited */
            currentEdge.destination.visited = true;

            /* Loop edges, for all visited nodes, if destination is unvisited, add to min heap for consideration by Prim's algorithm logic at top of this while loop */
            for (Edge edge : edgeList) {
                if (edge.source.visited && !edge.destination.visited) {
                    if (!mH.containsEdge(edge.source, edge.destination, edge.weight)) {
                        mH.add(edge.source, edge.destination, edge.weight);
                    }
                }
            }
        }

        /* Print the Minimum Spanning Tree produced by Prim's Algorithm in integer array format */
        System.out.println("Prim's MST (in order of node visited): " + visitedNodes);
    } /* END - primsAlgorithm */

    public static void main(String[] args) {

        /* Initialize Nodes */
        Node n0 = new Node(0);
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);

        /* Choose source node and set to visited */
        n0.setVisited(true);

        /* Initialize edgeList and add Edges */
        List<Edge> edgeList = new ArrayList<>();
        edgeList.add(new Edge(n0, n1, 3));
        edgeList.add(new Edge(n0, n2, 4));
        edgeList.add(new Edge(n0, n3, 4));
        edgeList.add(new Edge(n1, n2, 5));
        edgeList.add(new Edge(n1, n4, 4));
        edgeList.add(new Edge(n2, n3, 6));
        edgeList.add(new Edge(n2, n4, 2));
        edgeList.add(new Edge(n2, n5, 7));
        edgeList.add(new Edge(n3, n5, 8));
        edgeList.add(new Edge(n4, n5, 9));
        edgeList.add(new Edge(n5, n6, 10));

        /* Call Prim's Algorithm on this list of edges */
        primsAlgorithm(edgeList);
    }
}