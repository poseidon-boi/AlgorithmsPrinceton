public class WeightedQuickUnionUF {

    private int[] id;
    private int[] size;

    public WeightedQuickUnionUF(int n) {
        id = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        while (id[i] != i) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    public void union(int p, int q) {
        if (p < 0 || q < 0 || p >= id.length || q >= id.length)
            throw new IllegalArgumentException("Invalid p or q");
        int i = find(p), j = find(q);
        if (size[i] < size[j]) {
            id[i] = j;
            size[j] += size[i];
        }
        else {
            id[j] = i;
            size[i] += size[j];
        }
    }
}
