package components.waitingline;

import components.standard.Standard;

/**
 * First-in-first-out (FIFO) queue kernel component with primary methods. (Note:
 * by package-wide convention, all references are non-null.)
 *
 * @param <T>
 *            type of {@code WaitingLineKernel} entries
 * @mathmodel type WaitingLineKernel is modeled by string of T, entries must be
 *            unique.
 * @initially <pre>
* default:
* ensures
* this = <>
* </pre>
 * @iterator ~this.seen * ~this.unseen = this
 */
public interface WaitingLineKernel<T>
        extends Standard<WaitingLine<T>>, Iterable<T> {
    /**
     * Adds {@code x} to the end of {@code this}.
     *
     * @param x
     *            the entry to be added
     * @aliases reference {@code x}
     * @updates this
     * @requires #this does not contain x
     * @ensures this = #this * <x>
     */
    void enqueue(T x);

    /**
     * Removes and returns the entry at the front of {@code this}.
     *
     * @return the entry removed
     * @updates this
     * @requires this /= <>
     * @ensures #this = <dequeue> * this
     */
    T dequeue();

    /**
     * Reports length of {@code this}.
     *
     * @return the length of {@code this}
     * @ensures length = |this|
     */
    int length();

    /**
     * Reports the position of {@code x} in {@code this}, and -1 if value is not
     * in this.
     *
     * @param x
     *            the entry of which the position will be returned.
     * @return the position of entry {@code x} in {@code this}
     * @requires 0 <= pos < |this|
     * @ensures <position> corresponds to {@code x}, or is -1 for value not in
     *          this.
     */
    int position(T x);
}