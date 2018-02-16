package structure;

import java.util.ArrayList;
import java.util.List;

class GraphNode {
  String id;
  List<GraphNode> connectedTo = new ArrayList<>();

  GraphNode(String id) {
    this.id = id;
  }

  public void addEdge(GraphNode target) {
    connectedTo.add(target);
  }

  public String toString() {
    return id;
  }
}
