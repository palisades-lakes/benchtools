package palisades.lakes.bench.java.spaces.linear.r2;

import clojure.lang.IFn;
import palisades.lakes.bench.java.spaces.linear.Vector;

//----------------------------------------------------------------
/** (Immutable) vector in <b>R</b><sup>2</sup> represented 
 * by <code>int</code> <code>x</code> <code>y</code>
 * coordinates.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public final class I2 implements Vector {

  private final int _v0;
  public final int get0 () { return _v0; }
  private final int _v1;
  public final int get1 () { return _v1; }

  //--------------------------------------------------------------
  // Vector interface
  //--------------------------------------------------------------

  @Override
  public final double l1Norm () {
    return Math.abs(_v0) + Math.abs(_v1); }
  
  @Override
  public final double coordinate (final int i) {
    switch (i) {
    case 0 : return _v0;
    case 1 : return _v1;
    default : 
      throw new IllegalArgumentException(
        "No " + i + "th coordinate in " + this); } }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private I2 (final int v0, final int v1) {
    _v0 = v0; _v1 = v1; }

  public static final I2 make (final int v0, final int v1) {

    return new I2(v0,v1); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   * @throws an exception if the generated value is not within
   * the valid range.
   */
  
  public static final I2 generate (final IFn.L g) {
    final long v0 = g.invokePrim();
    assert (Integer.MIN_VALUE <= v0) && (v0 <= Integer.MAX_VALUE);

    final long v1 = g.invokePrim();
    assert (Integer.MIN_VALUE <= v1) && (v1 <= Integer.MAX_VALUE);

    return make((int) v0, (int) v1); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
