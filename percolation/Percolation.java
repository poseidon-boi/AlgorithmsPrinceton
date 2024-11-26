public class Percolation {
    private boolean[] isOpened;
    private int openSites = 0;
    private int sizeOfGrid;
    private WeightedQuickUnionUF grid, noBackwashGrid;

    /**
     * @param n The size of the percolation grid
     */
    public Percolation(int n) {

        if (n <= 0)  // Throws exception
            throw new IllegalArgumentException("Invalid size of grid");

        // Creating the percolation object
        sizeOfGrid = n;
        grid = new WeightedQuickUnionUF(n * n + 2);  // Creates grid
        noBackwashGrid = new WeightedQuickUnionUF(n * n + 1);  // Creates no backwash grid
        isOpened = new boolean[n * n + 2];
        isOpened[0] = true;  // Opens virtual top
        isOpened[n * n + 1] = true;  // Opens virtual bottom
    }

    /**
     * Throws required IllegalArgumentException if the given cell is invalid
     *
     * @param row    The row number of the cell (starts from 1)
     * @param column The column number of the cell (starts from 1)
     */
    private void exceptionThrower(int row, int column) {

        if (row < 1)
            throw new IllegalArgumentException("Row too low");
        if (column < 1)
            throw new IllegalArgumentException("Column too low");
        if (row > sizeOfGrid)
            throw new IllegalArgumentException("Row too high");
        if (column > sizeOfGrid)
            throw new IllegalArgumentException("Column too high");
    }

    /**
     * Opens the given cell, throws IllegalArgumentException if invalid
     *
     * @param row    The row number of the cell (starts from 1)
     * @param column The column number of the cell (starts from 1)
     */
    public void open(int row, int column) {

        exceptionThrower(row, column);

        if (isOpen(row, column))
            return;

        int currentElement = (row - 1) * sizeOfGrid + column;
        isOpened[currentElement] = true;
        openSites++;

        // Connects to the cell to the left
        if (currentElement % sizeOfGrid != 1) {
            if (isOpened[currentElement - 1]) {
                grid.union(currentElement, currentElement - 1);
                noBackwashGrid.union(currentElement, currentElement - 1);
            }
        }

        // Connects to the cell to the right
        if (currentElement % sizeOfGrid != 0) {
            if (isOpened[currentElement + 1]) {
                grid.union(currentElement, currentElement + 1);
                noBackwashGrid.union(currentElement, currentElement + 1);
            }
        }

        // Connects to the cell above
        if (currentElement > sizeOfGrid) {
            if (isOpened[currentElement - sizeOfGrid]) {
                grid.union(currentElement, currentElement - sizeOfGrid);
                noBackwashGrid.union(currentElement, currentElement - sizeOfGrid);
            }
        }
        else {
            grid.union(currentElement, 0);
            noBackwashGrid.union(currentElement, 0);
        }

        // Connects to the cell below
        if (currentElement <= sizeOfGrid * (sizeOfGrid - 1)) {
            if (isOpened[currentElement + sizeOfGrid]) {
                grid.union(currentElement, currentElement + sizeOfGrid);
                noBackwashGrid.union(currentElement, currentElement + sizeOfGrid);
            }
        }
        else {
            grid.union(currentElement, sizeOfGrid * sizeOfGrid + 1);
        }
    }

    /**
     * Checks whether the given cell is open, throws IllegalArgumentException
     * if invalid
     *
     * @param row    The row number of the cell (starts from 1)
     * @param column The column number of the cell (starts from 1)
     * @return true if open, false if closed
     */
    public boolean isOpen(int row, int column) {

        exceptionThrower(row, column);

        return isOpened[(row - 1) * sizeOfGrid + column];
    }

    /**
     * Checks whether the given cell is full, throws IllegalArgumentException
     * if invalid
     *
     * @param row    The row number of the cell (starts from 1)
     * @param column The column number of the cell (starts from 1)
     * @return true if open, false otherwise
     */
    public boolean isFull(int row, int column) {

        // Throws exceptions if invalid cell
        exceptionThrower(row, column);

        // Returns false if site is closed
        if (!(isOpened[(row - 1) * sizeOfGrid + column]))
            return false;

        // Checks whether the cell is connected to the virtual top
        return (noBackwashGrid.find((row - 1) * sizeOfGrid + column) == noBackwashGrid.find(0));
    }

    /**
     * @return The number of open sites in the percolation grid
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Checks whether the system percolates
     *
     * @return true if percolates, false if not
     */
    public boolean percolates() {
        return (grid.find(sizeOfGrid * sizeOfGrid + 1) == grid.find(0));
    }
}
