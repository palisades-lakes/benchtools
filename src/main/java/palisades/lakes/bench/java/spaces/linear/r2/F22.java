package palisades.lakes.bench.java.spaces.linear.r2;

import clojure.lang.AFn;
import clojure.lang.IFn;
import palisades.lakes.bench.java.spaces.linear.LinearFunction;
import palisades.lakes.bench.java.spaces.linear.Vector;

//----------------------------------------------------------------
/** (Immutable) linear transformation on <b>R</b><sup>2</sup> 
 * represented as a 2x2 matrix with <code>float</code> 
 * coordinates.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public final class F22 extends AFn implements LinearFunction {

  private final float _m00;
  public final float get00 () { return _m00; }
  private final float _m01;
  public final float get01 () { return _m01; }
  private final float _m10;
  public final float get10 () { return _m10; }
  private final float _m11;
  public final float get11 () { return _m11; }

  //--------------------------------------------------------------
  //  7 invoke methods
  //--------------------------------------------------------------

  public final F2 invoke (final B2 v) {
    final byte v0 = v.get0();
    final byte v1 = v.get1();
    return F2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final F2 invoke (final S2 v) {
    final short v0 = v.get0();
    final short v1 = v.get1();
    return F2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final F2 invoke (final I2 v) {
    final int v0 = v.get0();
    final int v1 = v.get1();
    return F2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final L2 v) {
    // TODO: overflow?
    final long v0 = v.get0();
    final long v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final F2 invoke (final F2 v) {
    final float v0 = v.get0();
    final float v1 = v.get1();
    return F2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  public final D2 invoke (final D2 v) {
    final double v0 = v.get0();
    final double v1 = v.get1();
    return D2.make(_m00*v0 + _m01*v1, _m10*v0 + _m11*v1); }
  
  //--------------------------------------------------------------
  // 37 axpy methods
  //--------------------------------------------------------------

  public final F2 axpy (final B2 x, final B2 y) {
    final byte x0 = x.get0();
    final byte x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final B2 x, final S2 y) {
    final int x0 = x.get0();
    final int x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final B2 x, final I2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final B2 x, final L2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final B2 x, final F2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final B2 x, final D2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  //--------------------------------------------------------------

  public final F2 axpy (final S2 x, final B2 y) {
    final short x0 = x.get0();
    final short x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final S2 x, final S2 y) {
    final int x0 = x.get0();
    final int x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final S2 x, final I2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final S2 x, final L2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final S2 x, final F2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final S2 x, final D2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  //--------------------------------------------------------------

  public final F2 axpy (final I2 x, final B2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final I2 x, final S2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final I2 x, final I2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final I2 x, final L2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final I2 x, final F2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final I2 x, final D2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  //--------------------------------------------------------------

  public final F2 axpy (final L2 x, final B2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final L2 x, final S2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final L2 x, final I2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final L2 x, final L2 y) {
    final long x0 = x.get0();
    final long x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final L2 x, final F2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final L2 x, final D2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  //--------------------------------------------------------------

  public final F2 axpy (final F2 x, final B2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final F2 x, final S2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final F2 x, final I2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final F2 x, final L2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final F2 axpy (final F2 x, final F2 y) {
    final float x0 = x.get0();
    final float x1 = x.get1();
    return 
      F2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final F2 x, final D2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  //--------------------------------------------------------------

  public final D2 axpy (final D2 x, final B2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final D2 x, final S2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final D2 x, final I2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final D2 x, final L2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final D2 x, final F2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  public final D2 axpy (final D2 x, final D2 y) {
    final double x0 = x.get0();
    final double x1 = x.get1();
    return 
      D2.make(
        _m00*x0 + _m01*x1 + y.get0(), 
        _m10*x0 + _m11*x1 + y.get1()); }

  //--------------------------------------------------------------
  // LinearFunction interface
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
  // order of tests?
  
  @Override
  public final Vector axpy (final Vector x, 
                            final Vector y) {
    if (x instanceof B2) {
      if (y instanceof B2) { return axpy((B2) x, (B2) y); }
      if (y instanceof D2) { return axpy((B2) x, (D2) y); }
      if (y instanceof F2) { return axpy((B2) x, (F2) y); }
      if (y instanceof I2) { return axpy((B2) x, (I2) y); }
      if (y instanceof L2) { return axpy((B2) x, (L2) y); }
      if (y instanceof S2) { return axpy((B2) x, (S2) y); } }
    if (x instanceof D2) { 
      if (y instanceof B2) { return axpy((D2) x, (B2) y); }
      if (y instanceof D2) { return axpy((D2) x, (D2) y); }
      if (y instanceof F2) { return axpy((D2) x, (F2) y); }
      if (y instanceof I2) { return axpy((D2) x, (I2) y); }
      if (y instanceof L2) { return axpy((D2) x, (L2) y); }
      if (y instanceof S2) { return axpy((D2) x, (S2) y); } }
    if (x instanceof F2) { 
      if (y instanceof B2) { return axpy((F2) x, (B2) y); }
      if (y instanceof D2) { return axpy((F2) x, (D2) y); }
      if (y instanceof F2) { return axpy((F2) x, (F2) y); }
      if (y instanceof I2) { return axpy((F2) x, (I2) y); }
      if (y instanceof L2) { return axpy((F2) x, (L2) y); }
      if (y instanceof S2) { return axpy((F2) x, (S2) y); } }
    if (x instanceof I2) { 
      if (y instanceof B2) { return axpy((I2) x, (B2) y); }
      if (y instanceof D2) { return axpy((I2) x, (D2) y); }
      if (y instanceof F2) { return axpy((I2) x, (F2) y); }
      if (y instanceof I2) { return axpy((I2) x, (I2) y); }
      if (y instanceof L2) { return axpy((I2) x, (L2) y); }
      if (y instanceof S2) { return axpy((I2) x, (S2) y); } }
    if (x instanceof L2) { 
      if (y instanceof B2) { return axpy((L2) x, (B2) y); }
      if (y instanceof D2) { return axpy((L2) x, (D2) y); }
      if (y instanceof F2) { return axpy((L2) x, (F2) y); }
      if (y instanceof I2) { return axpy((L2) x, (I2) y); }
      if (y instanceof L2) { return axpy((L2) x, (L2) y); }
      if (y instanceof S2) { return axpy((L2) x, (S2) y); } }
    if (x instanceof S2) { 
      if (y instanceof B2) { return axpy((S2) x, (B2) y); }
      if (y instanceof D2) { return axpy((S2) x, (D2) y); }
      if (y instanceof F2) { return axpy((S2) x, (F2) y); }
      if (y instanceof I2) { return axpy((S2) x, (I2) y); }
      if (y instanceof L2) { return axpy((S2) x, (L2) y); }
      if (y instanceof S2) { return axpy((S2) x, (S2) y); } }
    throw new IllegalArgumentException(
      "can't axpy " + this + " , " + x + " , " + y); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private F22 (final float m00,
               final float m01,
               final float m10,
               final float m11) {
    _m00 = m00; 
    _m01 = m01;
    _m10 = m10; 
    _m11 = m11;  }

  public static final F22 make (final float m00,
                                final float m01,
                                final float m10,
                                final float m11) {

    return new F22(m00,m01,m10,m11); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   * @throws an exception if the generated value is not within
   * the valid range.
   */
  
  public static final F22 generate (final IFn.D g) {
    final double m00 = g.invokePrim();
    assert (Math.nextUp(Float.NEGATIVE_INFINITY) <= m00) && (m00 <= Float.MAX_VALUE);
    final double m01 = g.invokePrim();
    assert (Math.nextUp(Float.NEGATIVE_INFINITY) <= m01) && (m01 <= Float.MAX_VALUE);
    final double m10 = g.invokePrim();
    assert (Math.nextUp(Float.NEGATIVE_INFINITY) <= m10) && (m10<= Float.MAX_VALUE);
    final double m11 = g.invokePrim();
    assert (Math.nextUp(Float.NEGATIVE_INFINITY) <= m11) && (m11 <= Float.MAX_VALUE);
    return make((float) m00, (float) m01, (float) m10, (float) m11); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
