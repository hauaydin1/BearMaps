/**
 * Created by Aydin on 4/13/2016.
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Node implements Comparable<Node> {
    long id;
    Point position;
    Set<Node> set;
    double priority;

    public Node(long id, Point position, double priority) {
        this.id = id;
        this.position = position;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        return position != null ? position.equals(node.position) : node.position == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", position=" + position +
                '}';
    }

    public int compareTo(Node other) {
        if (this.priority < other.priority) {
            return -1;
        }
        else if (this.priority > other.priority) {
            return  1;
        }
        else {
            return 0;
        }
    }
}
