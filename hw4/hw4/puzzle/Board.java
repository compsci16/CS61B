package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board implements WorldState {
    private final int[][] tiles;
    private final int N;
    private Integer hash;
    private Integer manhattan;
    private Integer hamming;

    public Board(int[][] tiles) {
        this.tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        this.N = tiles.length;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) throw new IndexOutOfBoundsException();
        return tiles[i][j];
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        if (hamming == null) {
            int ham = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (tiles[i][j] == 0) continue;
                    if (getGoalVal(i, j) != tiles[i][j])
                        ham++;
                }
            }
            hamming = ham;
        }
        return hamming;
    }

    private int getGoalVal(int i, int j) {
        if (i == N - 1 && j == N - 1) return 0;
        return i * N + j + 1;
    }


    public int manhattan() {
        if (manhattan == null) {
            int man = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int C = tiles[i][j];
                    if (C == 0) continue;
                    int row = (C - 1) / N;
                    int col = (C - 1) - N * row;

                    man += Math.abs(row - i) + Math.abs(col - j);
                }
            }
            manhattan = man;
        }
        return manhattan;
    }


    public int estimatedDistanceToGoal() {
        return hamming();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        if (hash == null) {
            hash = Arrays.deepHashCode(tiles);
        }
        return hash;
    }

    /**
     * Returns the string representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
