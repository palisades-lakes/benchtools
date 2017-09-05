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
 * @version 2017-09-05
 */

public final class S2 implements Vector {

  private final short _v0;
  public final short get0 () { return _v0; }
  private final short _v1;
  public final short get1 () { return _v1; }

  //--------------------------------------------------------------
  // methods
  //--------------------------------------------------------------
  // Likely to be cases where reducing to doubles won't be good
  // enough. It it was, wouldn't bother with different primitive
  // coordinate types in the first place.
  // Possibly more realistic: BigInteger and rational coordinates.

  private final Vector linearCombination (final Number a0,
                                          final Number a1,
                                          final B2 v1) {
    final double a00 = a0.doubleValue();
    final double a11 = a1.doubleValue();
    return 
      D2.make(
        a00*get0() + a11*v1.get0(), 
        a00*get1() + a11*v1.get1()); }

  private final Vector linearCombination (final Number a0,
                                          final Number a1,
                                          final D2 v1) {
    final double a00 = a0.doubleValue();
    final double a11 = a1.doubleValue();
    return 
      D2.make(
        a00*get0() + a11*v1.get0(), 
        a00*get1() + a11*v1.get1()); }

  private final Vector linearCombination (final Number a0,
                                          final Number a1,
                                          final F2 v1) {
    final double a00 = a0.doubleValue();
    final double a11 = a1.doubleValue();
    return 
      D2.make(
        a00*get0() + a11*v1.get0(), 
        a00*get1() + a11*v1.get1()); }

  private final Vector linearCombination (final Number a0,
                                          final Number a1,
                                          final I2 v1) {
    final double a00 = a0.doubleValue();
    final double a11 = a1.doubleValue();
    return 
      D2.make(
        a00*get0() + a11*v1.get0(), 
        a00*get1() + a11*v1.get1()); }

  private final Vector linearCombination (final Number a0,
                                          final Number a1,
                                          final L2 v1) {
    final double a00 = a0.doubleValue();
    final double a11 = a1.doubleValue();
    return 
      D2.make(
        a00*get0() + a11*v1.get0(), 
        a00*get1() + a11*v1.get1()); }

  private final Vector linearCombination (final Number a0,
                                          final Number a1,
                                          final S2 v1) {
    final double a00 = a0.doubleValue();
    final double a11 = a1.doubleValue();
    return 
      D2.make(
        a00*get0() + a11*v1.get0(), 
        a00*get1() + a11*v1.get1()); }

  //--------------------------------------------------------------
  // Vector interface
  //--------------------------------------------------------------

  @Override
  public final Vector linearCombination (final Number a0,
                                         final Number a1,
                                         final Vector v1) {
    if (v1 instanceof B2) {
      return linearCombination(a0, a1, (B2) v1); }
    if (v1 instanceof D2) {
      return linearCombination(a0, a1, (D2) v1); }
    if (v1 instanceof F2) {
      return linearCombination(a0, a1, (F2) v1); }
    if (v1 instanceof I2) {
      return linearCombination(a0, a1, (I2) v1); }
    if (v1 instanceof L2) {
      return linearCombination(a0, a1, (L2) v1); }
    if (v1 instanceof S2) {
      return linearCombination(a0, a1, (S2) v1); }

    throw new UnsupportedOperationException(
      "no method for a linear combination of ("
        + a0.getClass().getSimpleName() + " * "
        + getClass().getSimpleName() + ") + ("
        + a1.getClass().getSimpleName() + " * "
        + v1.getClass().getSimpleName() + ")");  }

  @Override
  public final double l1Norm () {
    return Math.abs(_v0) + Math.abs(_v1); }

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
