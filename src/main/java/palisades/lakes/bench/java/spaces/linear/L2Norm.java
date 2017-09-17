package palisades.lakes.bench.java.spaces.linear;

import clojure.lang.IFn;

/** Static l2norm related methods.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-09-16
 * @version 2017-09-17
 */

@SuppressWarnings("unchecked")
public final class L2Norm extends Object {

  /** Inaccurate naive sum for benchmarking. */
  public static final double naive (final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) {
      final double xi = x[i];
      s += xi*xi; }
    return s; }

  private static final double square (final double x) { 
    return x*x; }

  /** Inaccurate naive sum for benchmarking. */
  public static final double invokestatic (final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) {
      s += square(x[i]); }
    return s; }

  /** Inaccurate naive sum for benchmarking. */
  public static final double primitive (final IFn.DD f,
                                        final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) {
      final double fxi = f.invokePrim(x[i]);
      s += fxi*fxi; }
    return s; }

  /** Inaccurate naive sum for benchmarking. */
  public static final double boxing (final IFn f,
                                     final double[] x) {
    double s = 0.0;
    for (int i=0;i<x.length;i++) {
      @SuppressWarnings("boxing")
      final double fxi = (double) f.invoke(x[i]);
      s += fxi*fxi; }
    return s; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private L2Norm () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  ///--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
