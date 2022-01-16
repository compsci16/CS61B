package lab11.graphs;

import java.util.Deque;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound;
    private final Maze maze;
    private int[] pathTo;

    public MazeCycles(Maze m) {
        super(m);
        this.maze = m;
        pathTo = new int[maze.V()];
    }

    @Override
    public void solve() {
        dfs(0);
    }

    private void dfs(int v) {
        marked[v] = true;
        announce();
        for (int w : maze.adj(v)) {
            if (cycleFound) {
                return;
            }
            if (!marked[w]) {
                pathTo[w] = v;
                dfs(w);
            } else if (marked[w] && w != pathTo[v]) {
                cycleFound = true;
                edgeTo[w] = v;
                announce();
                int current = v;
                while (current != w){
                    edgeTo[current] = pathTo[current];
                    current = pathTo[current];
                    announce();
                }
                return;
            }

        }
    }


    private void paintEdges(int cycleSource, Deque<Integer> stack) {
        while (stack.getLast() != cycleSource) {
            edgeTo[stack.removeLast()] = stack.getLast();
            announce();
        }
    }


    // Helper methods go here
}

