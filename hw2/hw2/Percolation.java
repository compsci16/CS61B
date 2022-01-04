package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // false = blocked, true = unblocked.
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufDup;
    private final int N;
    private int openSites = 0;
    private boolean percolates;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException();
        this.N = N;
        grid = new boolean[N][N];
        // N*N = top pouring hole above the grid, N*N+1 = bottom hole
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufDup = new WeightedQuickUnionUF(N * N + 2);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (grid[row][col]) return; // Already open

        grid[row][col] = true;
        openSites++;
        establishConnections(row, col);
        int index = getIndex(row, col);
        if (uf.connected(index, N * N) && ufDup.connected(index, N * N + 1)) {
            percolates = true;
        }
    }

    private void establishConnections(int row, int col) {
        // uf_grid connected to top tap
        // dupUf_grid connected to bottom tap
        if (row == 0) {
            uf.union(getIndex(row, col), N * N);
        }
        if (row == N - 1) {
            ufDup.union(getIndex(row, col), N * N + 1);
        }
        connect(row, col, row - 1, col);
        connect(row, col, row + 1, col);
        connect(row, col, row, col - 1);
        connect(row, col, row, col + 1);
    }

    private void connect(int row1, int col1, int row2, int col2) {
        if (isValid(row1, col1) && isValid(row2, col2)
                && isOpen(row1, col1) && isOpen(row2, col2)) {
            uf.union(getIndex(row1, col1), getIndex(row2, col2));
            ufDup.union(getIndex(row1, col1), getIndex(row2, col2));
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < N;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int index = getIndex(row, col);
        return uf.connected(index, N * N);
    }

    private int getIndex(int row, int col) {
        return row * N + col;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }


}
