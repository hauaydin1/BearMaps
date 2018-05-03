/**
 * Created by Aydin on 4/13/2016.
 */
import sun.awt.image.ImageWatched;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

public class QuadTree {
    QTreeNode root;

    public QuadTree() {
        root = new QTreeNode(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT, "", 0);
        quadTreeInit(root);

    }

    public void quadTreeInit(QTreeNode root) {
        if (root.depth >= 7) {
            return;
        } else {
            root.child1 = makeChild1(root);
            root.child2 = makeChild2(root);
            root.child3 = makeChild3(root);
            root.child4 = makeChild4(root);
            quadTreeInit(root.child1);
            quadTreeInit(root.child2);
            quadTreeInit(root.child3);
            quadTreeInit(root.child4);
        }
    }

    public double centerX(double ullon, double lrlon) {
        return (lrlon - ullon) / 2 + ullon;
    }

    public double centerY(double ullat, double lrlat) {
        return (ullat - lrlat) / 2 + lrlat;
    }

    public QTreeNode makeChild1(QTreeNode root) {
        double childLRLON = centerX(root.ullon, root.lrlon); // this is your x
        double childLRLAT = centerY(root.ullat, root.lrlat); /// this is iyour y
            String childName = root.file + 1;
            QTreeNode child1 = new QTreeNode(root.ullon, root.ullat, childLRLON, childLRLAT, childName, root.depth + 1);
            return child1;
    }

    public QTreeNode makeChild2(QTreeNode root) {
        double childLRLON = centerX(root.ullon, root.lrlon);
        double childULLAT = centerY(root.ullat, root.lrlat);
        String childName = root.file + 2;
        QTreeNode child2 = new QTreeNode(childLRLON, root.ullat, root.lrlon, childULLAT, childName, root.depth + 1);
        return child2;
    }

    public QTreeNode makeChild3(QTreeNode root) {
        double childULLON = centerY(root.ullat, root.lrlat);
        double childLRLAT = centerX(root.ullon, root.lrlon);
        String childName = root.file + 3;
        QTreeNode child3 = new QTreeNode(root.ullon, childULLON, childLRLAT, root.lrlat, childName, root.depth + 1);
        return child3;
    }

    public QTreeNode makeChild4(QTreeNode root) {
        double childULLAT = centerX(root.ullon, root.lrlon);
        double childULLON = centerY(root.ullat, root.lrlat);
        String childName = root.file + 4;
        QTreeNode child4 = new QTreeNode(childULLAT, childULLON, root.lrlon, root.lrlat, childName, root.depth + 1);
        return child4;
    }

    public boolean intersection(QTreeNode node, double ullon, double ullat, double lrlon, double lrlat) {
        return !(lrlat > node.ullat || ullat < node.lrlat || lrlon < node.ullon || ullon > node.lrlon);
    }

    public boolean metResolutionReq(double lrlon, double ullon, double queryDPP) {
        double dpp = (lrlon - ullon) / 256;
        return (dpp <= queryDPP);
    }

    public boolean hasChildren(QTreeNode root) {
        return root.child1 == null;
    }

    public void treeTraversal(QTreeNode root, double ullon, double ullat, double lrlon, double lrlat, double dpp, LinkedList<QTreeNode> traversed) {
        if (intersection(root, ullon, ullat, lrlon, lrlat)) {
            if (metResolutionReq(root.lrlon, root.ullon, dpp) || root.depth >= 7) {
                traversed.addLast(root);
                return;
            } else {
                treeTraversal(root.child1, ullon, ullat, lrlon, lrlat, dpp, traversed);
                treeTraversal(root.child2, ullon, ullat, lrlon, lrlat, dpp, traversed);
                treeTraversal(root.child3, ullon, ullat, lrlon, lrlat, dpp, traversed);
                treeTraversal(root.child4, ullon, ullat, lrlon, lrlat, dpp, traversed);
            }
        }
    }


    public LinkedList<QTreeNode> sortedList(LinkedList<QTreeNode> traversed) {
        Collections.sort(traversed, new Comparator<QTreeNode>() {
            @Override
            public int compare(QTreeNode o1, QTreeNode o2) {
                if (o1.ullat < o2.ullat) {
                    return 1;
                }
                if (o1.ullat > o2.ullat) {
                    return -1;
                }
                if (o1.ullon > o2.ullon) {
                    return 1;
                }
                if (o1.ullon < o2.ullon) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        return traversed;
    }
}
