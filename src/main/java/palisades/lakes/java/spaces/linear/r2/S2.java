package palisades.lakes.java.spaces.linear.r2;

import clojure.lang.IFn;
import palisades.lakes.java.spaces.linear.Vector;

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

  private final short _x;
  public final short getX () { return _x; }
  private final short _y;
  public final short getY () { return _y; }

  //--------------------------------------------------------------
  // Vector
  //--------------------------------------------------------------

  @Override
  public final double coordinate (final int i) {
    switch (i) {
    case 0 : return _x;
    case 1 : return _y;
    default : 
      throw new IllegalArgumentException(
        "No " + i + "th coordinate in " + this); } }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private S2 (final short x, final short y) {
    _x = x; _y = y; }

  public static final S2 make (final short x, final short y) {

    return new S2(x,y); }

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
    final long x = g.invokePrim();
    assert (Short.MIN_VALUE <= x) && (x <= Short.MAX_VALUE);

    final long y = g.invokePrim();
    assert (Short.MIN_VALUE <= y) && (y <= Short.MAX_VALUE);

    return make((short) x, (short) y); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
