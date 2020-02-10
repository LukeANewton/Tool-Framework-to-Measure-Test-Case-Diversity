package utilities;

/**
 * A tuple of values
 */
public class Tuple<L,R> {

    private L left;
    private R right;
    
    /**
     * Creates a new tuple
     *
     * @param left  The left value for this tuple
     * @param right The right value for this tuple
     */
    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
    
    @Override
    public String toString() {
        return "[" + left + ", " + right + "]";
    }
}
