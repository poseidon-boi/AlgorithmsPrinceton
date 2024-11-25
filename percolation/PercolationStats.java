public class PercolationStats {
    private static final double CON95 = 1.96;
    private double[] p;

    /**
     * @param n      The size of the percolation grid
     * @param trials The number of trials to run
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Invalid arguments");
        p = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int siteToOpen = (int) (Math.random() * n * n);
                perc.open(siteToOpen / n + 1, siteToOpen % n + 1);
            }
            p[i] = ((double) perc.numberOfOpenSites()) / (n * n);
        }
    }

    /**
     * @return The mean of the ratio of open sites across all trials
     */
    public double mean() {
        if (p == null)
            return Double.NaN;
        if (p.length == 0)
            return Double.NaN;
        double mean = 0;
        for (double num : p)
            mean += num / p.length;
        return mean;
    }

    /**
     * @return The standard deviation of the ratio of open sites across all
     * trials
     */
    public double stddev() {
        if (p == null)
            return Double.NaN;
        if (p.length == 0)
            return Double.NaN;
        double mean = mean();
        double stddev = 0;
        for (double num : p)
            stddev += (mean - num) * (mean - num) / (p.length - 1);
        return stddev;
    }

    /**
     * @return The lower end of the 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - CON95 * stddev() / Math.sqrt(p.length);
    }

    /**
     * @return The upper end of the 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + CON95 * stddev() / Math.sqrt(p.length);
    }

    public static void main(String[] args) {
        int args0 = Integer.parseInt(args[0]), args1 = Integer.parseInt(args[1]);
        PercolationStats obj = new PercolationStats(args0, args1);
        System.out.println("mean\t = " + obj.mean());
        System.out.println("stddev\t = " + obj.stddev());
        System.out.printf("95%% confidence interval\t = [%f, %f]\n", obj.confidenceLo(),
                          obj.confidenceHi());
    }
}
