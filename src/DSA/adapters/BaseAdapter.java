package DSA.adapters;

import DSA.SortingAlgorithm;
public abstract class BaseAdapter implements SortingAlgorithm {
    protected long comparisons;
    protected long swaps;
    protected long time;

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getExecutionTime() { return time; }
}
//