package components.waitingline;

/**
 * {@code WaitingLineKernel} enhanced with secondary methods.
 *
 * @param <T>
 *            type of {@code WaitingLine} entries
 */
public interface WaitingLine<T> extends WaitingLineKernel<T> {
    /**
     * Reports the front of {@code this}.
     *
     * @return the front entry of {@code this}
     * @aliases reference returned by {@code front}
     * @requires this /= <>
     * @ensures <front> is prefix of this
     */
    T front();

    /**
     * Concatenates ("appends") {@code q} to the end of {@code this}.
     *
     * @param q
     *            the {@code WaitingLine} to be appended to the end of
     *            {@code this}
     * @updates this
     * @clears q
     * @ensures this = #this * #q
     */
    void append(WaitingLine<T> q);

    /**
     * Reports the value at a given position in {@code this}.
     *
     * @param pos
     *            the index of which the value will be returned
     * @return the value of entry at {@code pos}
     * @requires 0 <= pos < |this|
     * @ensures <pre>
    * <positionVal> is valid entry of this
    * </pre>
     */
    T positionVal(int pos);

    /**
     * Removes a given entry that is known to be in {@code this}.
     *
     * @param x
     *            the entry to be removed.
     * @return the entry removed
     * @updates this
     * @requires #this contains {@code x}
     * @ensures <pre>
    * <remove> is contained in #this and
    * this contains all entries besides the removed entry in the same order.
    * </pre>
     */
    T remove(T x);
}