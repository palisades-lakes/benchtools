package palisades.lakes.java.spaces.linear.r2;

import clojure.lang.IFn;
import palisades.lakes.java.spaces.linear.Vector;

//----------------------------------------------------------------
/** (Immutable) vector in <b>R</b><sup>2</sup> represented 
 * by <code>double</code> <code>x</code> <code>y</code>
 * coordinates.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public final class D2 implements Vector {

  private final double _x;
  public final double getX () { return _x; }
  private final double _y;
  public final double getY () { return _y; }

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

  private D2 (final double x, final double y) {
    _x = x; _y = y; }

  public static final D2 make (final double x, final double y) {

    return new D2(x,y); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   */
  
  public static final D2 generate (final IFn.D g) {
    final double x = g.invokePrim();
    final double y = g.invokePrim();
    return make(x,y); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
