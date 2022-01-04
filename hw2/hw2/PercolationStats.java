package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    // perform T independent experiments on an N-by-N grid
    private final double[] x;
    private final int T;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        x = new double[T];
        this.T = T;
        Percolation percolation;
        for (int i = 0; i < T; i++) {
            percolation = pf.make(N);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            x[i] = 1.0 * percolation.numberOfOpenSites() / (N*N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(x);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(x);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }
}
