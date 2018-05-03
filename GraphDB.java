import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 * @author Alan Yao
 */
public class GraphDB {
    /**
     * Example constructor shows how to create and start an XML parser.
     * @param db_path Path to the XML file to be parsed.
     */
    Map<Long, HashSet<Long>> params;
    Map<Long, Node> params2;
    Map<Long, HashSet<Node>> params3;
    LinkedList<Long> removedList;
    LinkedList<Long> path;
    Node rightNode;

    public GraphDB(String db_path) {
        params3 = new HashMap<>();
        params2 = new HashMap<>();
        params = new HashMap<>();
        removedList = new LinkedList<>();
        try {
            File inputFile = new File(db_path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            MapDBHandler maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
            System.out.println(params.size());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        cleanHelper(params);
        clean();
    }

    public LinkedList<Long> findShortestPath(Node start, double endLon, double endLat) {
        HashSet<Node> visited  = new HashSet<>();
        HashMap <Long, Double> distance = new HashMap<>();
        HashMap <Long, Node> previous = new HashMap<>();
        Queue<Node> fringe = new PriorityQueue<>();
        path = new LinkedList<>();
        Node pointer;
        fringe.add(start);
        distance.put(start.id, 0.0);
        while (!fringe.isEmpty()) {
            pointer = fringe.remove();
            if (visited.contains(pointer)) {
                continue;
            }
            visited.add(pointer);
            if (pointer.position.lon == endLon && pointer.position.lat == endLat) {
                System.out.println("actually got here");
                path = pathReconstruction(pointer, start, previous);
                return path;
            }
            if (params.get(pointer.id) == null) {
                continue;
            }
            for (long c1 : params.get(pointer.id)) {
                Node c2 = params2.get(c1);
                double edge = euclideanDistance(pointer, c2);
                if (!(distance.containsKey(c2.id))||(distance.get(c2.id) > (distance.get(pointer.id) + edge))) {
                    //System.out.println(distance.get(pointer.id) + edge);
                    distance.put(c2.id, (distance.get(pointer.id) + edge));
                    //System.out.println();
                    c2.priority = distance.get(c2.id);
                    fringe.add(c2);
                    previous.put(c2.id, pointer);
                }
            }

        }
        //path = pathReconstruction(previous);
        System.out.println("got here instead ):");
        return null;
    }
//    public LinkedList<Board> pathReconstruction(SearchNode goal, Board initial) {
//        LinkedList<Board> result = new LinkedList<>();
//        SearchNode currentNode = goal;
//        while (!currentNode.board.equals(initial)) {
//            result.add(currentNode.board);
//            currentNode = currentNode.prevNode;
//        }
//        return result;
//    }

    public LinkedList<Long> pathReconstruction(Node goal, Node start, HashMap<Long, Node> prev) {
        LinkedList<Long> list = new LinkedList<>();
        Node current = goal;
        long startID = start.id;
        while (!current.equals(start)) {
            list.addFirst(current.id);
            current = prev.get(current.id);
        }
        list.addFirst(startID);
        return list;
    }
    public double euclideanDistance(Node n1, Node n2) {
        double x = n1.position.lon;
        double x1 = n2.position.lon;
        double y = n1.position.lat;
        double y1 = n2.position.lat;
        return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    public LinkedList<Long> cleanHelper(Map<Long, HashSet<Long>> toRemove) {
        for (Long key : params.keySet()) {
            if (params.get(key).contains(null)) {
                removedList.addLast(key);
            }
            if (params.get(key).isEmpty()) {
                long removed = key;
                removedList.addLast(removed);
            }
        }
        return removedList;
    }

    private void clean() {
        for (int i = 0; i < removedList.size(); i++) {
            params.remove(removedList.get(i));
        }
        System.out.println(params.size());
    }

}
