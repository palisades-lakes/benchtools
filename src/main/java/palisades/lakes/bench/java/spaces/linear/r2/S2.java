package palisades.lakes.bench.java.spaces.linear.r2;

import clojure.lang.IFn;
import palisades.lakes.bench.java.spaces.linear.Vector;

//----------------------------------------------------------------
/** (Immutable) vector in <b>R</b><sup>2</sup> represented 
 * by <code>short</code> <code>x</code> <code>y</code>
 * coordinates.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public final class S2 implements Vector {

  private final short _v0;
  public final short get0 () { return _v0; }
  private final short _v1;
  public final short get1 () { return _v1; }

  //--------------------------------------------------------------
  // Vector
  //--------------------------------------------------------------

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

  private S2 (final short v0, final short v1) {
    _v0 = v0; _v1 = v1; }

  public static final S2 make (final short v0, final short v1) {

    return new S2(v0,v1); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   * @throws an exception if the generated value is not within
   * the valid range.
   */
  
  public static final S2 generate (final IFn.L g) {
    final long v0 = g.invokePrim();
    assert (Short.MIN_VALUE <= v0) && (v0 <= Short.MAX_VALUE);

    final long v1 = g.invokePrim();
    assert (Short.MIN_VALUE <= v1) && (v1 <= Short.MAX_VALUE);

    return make((short) v0, (short) v1); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
