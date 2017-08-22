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

  private final double _v0;
  public final double get0 () { return _v0; }
  private final double _v1;
  public final double get1 () { return _v1; }

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

  private D2 (final double v0, final double v1) {
    _v0 = v0; _v1 = v1; }

  public static final D2 make (final double v0, final double v1) {

    return new D2(v0,v1); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   */
  
  public static final D2 generate (final IFn.D g) {
    final double v0 = g.invokePrim();
    final double v1 = g.invokePrim();
    return make(v0,v1); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
