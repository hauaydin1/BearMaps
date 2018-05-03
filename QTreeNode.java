/**
 * Created by Aydin on 4/13/2016.
 */
public class QTreeNode {
    double ullat;
    double ullon;
    double lrlat;
    double lrlon;
    String file;
    int depth;
    QTreeNode child1;
    QTreeNode child2;
    QTreeNode child3;
    QTreeNode child4;

    public QTreeNode(double ullon, double ullat, double lrlon, double lrlat, String file, int depth) {
        this.ullat = ullat;
        this.ullon = ullon;
        this.lrlat = lrlat;
        this.lrlon = lrlon;
        this.file = file;
        this.depth = depth;
    }
}

