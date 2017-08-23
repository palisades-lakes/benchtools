package palisades.lakes.bench.java.spaces.linear.r2;

import clojure.lang.AFn;
import clojure.lang.IFn;
import palisades.lakes.bench.java.spaces.linear.LinearFunction;
import palisades.lakes.bench.java.spaces.linear.Vector;

//----------------------------------------------------------------
/** (Immutable) linear transformation  on <b>R</b><sup>2</sup> 
 * represented as a 2x2 matrix with <code>double</code> 
 * coordinates.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public final class D22 extends AFn implements LinearFunction {

  private final double _m00;
  public final double get00 () { return _m00; }
  private final double _m01;
  public final double get01 () { return _m01; }
  private final double _m10;
  public final double get10 () { return _m10; }
  private final double _m11;
  public final double get11 () { return _m11; }

  //--------------------------------------------------------------
  // Methods
  //--------------------------------------------------------------

  public final D2 invoke (final B2 v) {
    final byte v0 = v.get0();
    final byte v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final S2 v) {
    final short v0 = v.get0();
    final short v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final I2 v) {
    final int v0 = v.get0();
    final int v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final L2 v) {
    // TODO: overflow?
    final long v0 = v.get0();
    final long v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final F2 v) {
    final float v0 = v.get0();
    final float v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final D2 v) {
    final double v0 = v.get0();
    final double v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  //--------------------------------------------------------------
  // Vector
  //--------------------------------------------------------------

  @Override
  public final Vector invoke (final Vector v) {
    if (v instanceof B2) { return invoke((B2) v); }
    if (v instanceof D2) { return invoke((D2) v); }
    if (v instanceof F2) { return invoke((F2) v); }
    if (v instanceof I2) { return invoke((I2) v); }
    if (v instanceof L2) { return invoke((L2) v); }
    if (v instanceof S2) { return invoke((S2) v); }
    throw new IllegalArgumentException(
      "can't invoke " + this + " on " + v); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private D22 (final double m00,
               final double m01,
               final double m10,
               final double m11) {
    _m00 = m00; 
    _m01 = m01;
    _m10 = m10; 
    _m11 = m11;  }

  public static final D22 make (final double m00,
                                final double m01,
                                final double m10,
                                final double m11) {

    return new D22(m00,m01,m10,m11); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   * @throws an exception if the generated value is not within
   * the valid range.
   */
  
  public static final D22 generate (final IFn.D g) {
    final double m00 = g.invokePrim();
    final double m01 = g.invokePrim();
    final double m10 = g.invokePrim();
    final double m11 = g.invokePrim();
    return make(m00, m01, m10, m11); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
