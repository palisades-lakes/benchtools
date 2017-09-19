package palisades.lakes.bench.java.spaces.linear;

import clojure.lang.IFn;

/** Static methods for summing numbers.
 * Doesn't really belong here.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-09-18
 * @version 2017-09-18
 */

@SuppressWarnings("unchecked")
public final class Sum extends Object {

  /** Inaccurate naive sum for benchmarking. */
  public static final double naive (final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) { s += x[i]; }
    return s; }

  /** Inaccurate naive sum for benchmarking. */
  public static final double primitive (final IFn.DD f,
                                        final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) { s += f.invokePrim(x[i]); }
    return s; }

  /** Inaccurate naive sum for benchmarking. */
  @SuppressWarnings("boxing")
  public static final double boxing (final IFn f,
                                     final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) { s += (double) f.invoke(x[i]); }
    return s; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Sum () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  ///--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
