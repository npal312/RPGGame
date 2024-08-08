package ai;

public class Node {

    Node parent;
    public int col;
    public int row;
    int gCost; //distance between starting and current node
    int hCost; //distance from current node to goal node
    int fCost; //sum of h and g costs
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col, int row){
        this.col = col;
        this.row = row;
    }

}
