package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Solver {
    Deque<WorldState> solution;
    private int minMoves;
    private final Map<WorldState, Integer> estimatedDistanceMap;

    private class SearchNode implements Comparable<SearchNode> {
        private final WorldState world;
        private final int moves;
        private final SearchNode previous;
        private int cost;

        public SearchNode(int moves, WorldState world, SearchNode previousMove) {
            this.world = world;
            this.moves = moves;
            this.previous = previousMove;
            computeCost();
        }

        private void computeCost() {
            Integer value = estimatedDistanceMap.get(world);
            if (value == null) {
                value = world.estimatedDistanceToGoal();
                estimatedDistanceMap.put(world, value);
            }
            cost = this.moves + value;
        }

        private int cost() {
            return cost;
        }


        @Override
        public int compareTo(SearchNode o) {
            return this.cost() - o.cost();
        }
    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> nodes = new MinPQ<>();
        estimatedDistanceMap = new HashMap<>();
        solution = new ArrayDeque<>();
        nodes.insert(new SearchNode(0, initial, null));
        solve(nodes);
    }

    private void solve(MinPQ<SearchNode> nodes) {
        while (!nodes.isEmpty()) {
            SearchNode min = nodes.delMin();
            if (min.world.isGoal()) {
                minMoves = min.moves;
                SearchNode current = min;
                while (current != null) {
                    solution.addFirst(current.world);
                    current = current.previous;
                }
                return;
            } else {
                for (WorldState neighbour : min.world.neighbors()) {
                    if (min.previous == null || !min.previous.world.equals(neighbour)) {
                        nodes.insert(new SearchNode(min.moves + 1, neighbour, min));
                    }

                }
            }
        }
    }

    public int moves() {
        return minMoves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}