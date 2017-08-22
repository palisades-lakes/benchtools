package palisades.lakes.java.spaces.linear.r2;

import clojure.lang.IFn;
import palisades.lakes.java.spaces.linear.Vector;

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

  private final float _x;
  public final float getX () { return _x; }
  private final float _y;
  public final float getY () { return _y; }

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

  private F2 (final float x, final float y) {
    _x = x; _y = y; }

  public static final F2 make (final float x, final float y) {

    return new F2(x,y); }

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
    final double x = g.invokePrim();
    assert Double.isNaN(x) || 
    ((Float.MIN_VALUE <= x) && (x <= Float.MAX_VALUE));

    final double y = g.invokePrim();
    assert Double.isNaN(y) || 
    ((Float.MIN_VALUE <= y) && (y <= Float.MAX_VALUE));

    return make((float) x, (float) y); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
