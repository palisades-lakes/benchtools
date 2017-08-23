package palisades.lakes.bench.java.spaces.linear.r2;

import clojure.lang.IFn;
import palisades.lakes.bench.java.spaces.linear.Vector;

//----------------------------------------------------------------
/** (Immutable) vector in <b>R</b><sup>2</sup> represented 
 * by <code>float</code> <code>x</code> <code>y</code>
 * coordinates.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public final class F2 implements Vector {

  private final float _v0;
  public final float get0 () { return _v0; }
  private final float _v1;
  public final float get1 () { return _v1; }

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

  private F2 (final float v0, final float v1) {
    _v0 = v0; _v1 = v1; }

  public static final F2 make (final float v0, final float v1) {

    return new F2(v0,v1); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   * @throws an exception if the generated value is not within
   * the valid range.
   */
  
  public static final F2 generate (final IFn.D g) {
    final double v0 = g.invokePrim();
    assert Double.isNaN(v0) || 
    ((Float.MIN_VALUE <= v0) && (v0 <= Float.MAX_VALUE));

    final double v1 = g.invokePrim();
    assert Double.isNaN(v1) || 
    ((Float.MIN_VALUE <= v1) && (v1 <= Float.MAX_VALUE));

    return make((float) v0, (float) v1); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
