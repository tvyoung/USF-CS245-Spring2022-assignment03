package assignment3;

//@name Vicki Young
//@date/version 2022.05.04
//CS245 Assignment3: Graph

import java.text.DecimalFormat;
import java.util.*;

public class Graph {

    /* inner class Edge. Do not modify this class. */
    class Edge {
        public final String s;
        public final String t;
        public final Double weight;

        public Edge(String s, String t, Double w) {
            this.s = s;
            this.t = t;
            this.weight = w;
        }

        public String toString() {
            return s+"->"+t+":"+weight;
        }
    }
    
    /* instance variable for Graph class */
    private final HashMap<String, List<Edge>> adjacencyList;

    /**
     * Graph constructor that initializes instance variables and builds the adjacencyList
     * Resource used to iterate through HashMap: https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
     * @param counts = HashMap<String, Double> "Base Asset, Quote Asset" and total trade counts data
     */
    public Graph(HashMap<String, Double> counts) {
        //initialize adjacencyList
        adjacencyList = new HashMap<>();

        //iterate through HashMap entries (resource used: https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap)
        for (Map.Entry<String, Double> entry : counts.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();

            //split given hashmap entry's key into the source and target
            String[] transaction = key.split(", ");
            String source = transaction[0];
            String target = transaction[1];
            //create edge from source, target, and value
            Edge edge = new Edge(source, target, value);

            //if source exists in adjacencyList, add edge to source's list of edges
            if (adjacencyList.containsKey(source)) {
                adjacencyList.get(source).add(edge);
            //else source does not exist in adjacencyList, create source and new array list with edge added to it
            } else {
                List<Edge> edgesList = new ArrayList<>();
                edgesList.add(edge);
                adjacencyList.put(source, edgesList);
            }
        }
        //FOR TESTING: print out adjacencyList
        /*for (Map.Entry<String, List<Edge>> entry : adjacencyList.entrySet()) {
            System.out.println("source: " + entry.getKey() + " | edges: " + entry.getValue());
        }*/
    }

    /** 
     * findFlows takes the source cryptocurrency and finds a shortest path from the source 
     * to all reachable cryptocurrencies, and calculate the net trades count over the shortest path.
     * Uses BFS algorithm with maxCount variable to store current max transaction cost in given path.
     * @param source = the source cryptocurrency name.
     * @return cryptocurrencies HashMap of (other cryptocurrency, the net trades count) pairs.
     */
    public Map<String, Double> findFlows(String source) {
        Map<String, Double> cryptocurrencies = new HashMap<>();
        //BFS algorithm; uses queue to find cryptocurrencies by layers
        Queue<String> q = new ArrayDeque<>();
        q.add(source);
        while (!q.isEmpty()) {
            //dequeues front cryptocurrency (vertex) from queue
            String v = q.poll();
            //if current vertex is not the source, get its listed max transaction count
            double maxCount;
            if (v.equals(source)) {
                maxCount = -1.0;
            } else {
                maxCount = cryptocurrencies.get(v);
            }
            //if cryptocurrency (vertex) is listed in adjacencyList (meaning it has outgoing edges)
            if (adjacencyList.containsKey(v)) {
                //for every edge with target not yet in cryptocurrencies HashMap, add to queue and find max transaction count
                for (Edge edge : adjacencyList.get(v)) {
                    //NOTE: ignore edges whose target is the source, or whose weight = 0.0
                    if (!source.equals(edge.t) && !cryptocurrencies.containsKey(edge.t) && edge.weight != 0.0) {
                        q.add(edge.t);
                        //if current edge weight is less than maxCost, add edge weight as target transaction count to cryptocurrencies
                        if (maxCount == -1.0 || maxCount > edge.weight) {
                            cryptocurrencies.put(edge.t, edge.weight);
                        //else add maxCost as target transaction count to cryptocurrencies
                        } else {
                            cryptocurrencies.put(edge.t, maxCount);
                        }
                    }
                }
            }
        }
        //return cryptocurrencies
        return cryptocurrencies;
    }

    /** 
     * sortFlows takes the (cryptocurrency, net trades count) pairs and sort them 
     * in the descending order of the net trades count. Returns the sorted list as 
     * List of Strings, where each String is "cryptocurrency: net trades count" format.
     * Used resource: https://stackoverflow.com/questions/16098046/how-do-i-print-a-double-value-without-scientific-notation-using-java
     * For formatting double values without scientific notation.
     * @param flows = (cryptocurrency, net trades count) pairs
     * @return List of cryptocurrency: net trades count Strings.
     */
    public List<String> sortFlows(Map<String, Double> flows) {
        List<String> descendingCount = new LinkedList<>();
        //while flows is not empty, loop through entries and find key of cryptocurrency with greatest transaction count
        while (!flows.isEmpty()) {
            String maxKey = "";
            //for each entry in flows:
            for (Map.Entry<String, Double> entry : flows.entrySet()) {
                //make first entry in flows the maxKey
                if (maxKey.equals("")) {
                    maxKey = entry.getKey();
                }
                //compare transaction counts of each entry, setting the maxKey to cryptocurrency of corresponding greatest
                else if (entry.getValue() > flows.get(maxKey)) {
                    maxKey = entry.getKey();
                }
            }
            //add maxKey and its transaction count to descendingCount
            //resource used for double formatting without scientific notation: https://stackoverflow.com/questions/16098046/how-do-i-print-a-double-value-without-scientific-notation-using-java
            double maxCount = flows.remove(maxKey);
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0);
            descendingCount.add(maxKey + ": " + df.format(maxCount));
        }
        //FOR TESTING: print total number of cryptocurrencies
        //System.out.println(descendingCount.size());
        return descendingCount;
    }

}
