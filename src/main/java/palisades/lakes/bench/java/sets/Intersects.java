package palisades.lakes.bench.java.sets;

import java.util.Collections;

/** Static intersection test.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-05-29
 * @version 2017-08-27
 */

@SuppressWarnings("unchecked")
public final class Intersects extends Object {

  //--------------------------------------------------------------
  // intersection tests
  //--------------------------------------------------------------

  public final static boolean intersects (final IntegerInterval s0,
                                          final IntegerInterval s1) {
    if (s0.max <= s1.min) { return false; }
    if (s1.max <= s0.min) { return false; }
    return true; }

  public final static boolean intersects (final IntegerInterval s0,
                                          final DoubleInterval s1) {
    if (s0.max <= s1.min) { return false; }
    if (s1.max <= s0.min) { return false; }
    return true; }

  public final static boolean intersects (final IntegerInterval s0,
                                          final java.util.Set s1) {
    return s0.intersects(s1); }

  //--------------------------------------------------------------

  public final static boolean intersects (final DoubleInterval s0,
                                          final IntegerInterval s1) {
    if (s0.max <= s1.min) { return false; }
    if (s1.max <= s0.min) { return false; }
    return true; }

  public final static boolean intersects (final DoubleInterval s0,
                                          final DoubleInterval s1) {
    if (s0.max <= s1.min) { return false; }
    if (s1.max <= s0.min) { return false; }
    return true; }

  public final static boolean intersects (final DoubleInterval s0,
                                          final java.util.Set s1) {
    return s0.intersects(s1); }

  //--------------------------------------------------------------

  public final static boolean intersects (final java.util.Set s0,
                                          final IntegerInterval s1) {
    return s1.intersects(s0); }

  public final static boolean intersects (final java.util.Set s0,
                                          final DoubleInterval s1) {
    return s1.intersects(s0); }

  public final static boolean intersects (final java.util.Set s0,
                                          final java.util.Set s1) {
    return (! Collections.disjoint(s0,s1)); }

  //--------------------------------------------------------------
  // lookup
  //--------------------------------------------------------------

  public static final boolean intersects (final Object s0,
                                          final Object s1) {

    if (s0 instanceof IntegerInterval) {
      final IntegerInterval ii = (IntegerInterval) s0;
      if (s1 instanceof IntegerInterval) {
        return ii.intersects((IntegerInterval) s1); }
      if (s1 instanceof DoubleInterval) {
        return ii.intersects((DoubleInterval) s1); }
      if (s1 instanceof java.util.Set) {
        return ii.intersects((java.util.Set) s1); }
      return ii.intersects(s1); }

    if (s0 instanceof DoubleInterval) {
      final DoubleInterval di = (DoubleInterval) s0;
      if (s1 instanceof DoubleInterval) {
        return di.intersects((DoubleInterval) s1); }
      if (s1 instanceof IntegerInterval) {
        return ((IntegerInterval) s1).intersects(di); }
      if (s1 instanceof java.util.Set) {
        return di.intersects((java.util.Set) s1); }
      return di.intersects(s1); }

    if (s0 instanceof java.util.Set) {
      final java.util.Set s = (java.util.Set) s0;
      if (s1 instanceof DoubleInterval) {
        return ((DoubleInterval) s1).intersects(s); }
      if (s1 instanceof IntegerInterval) {
        return ((IntegerInterval) s1).intersects(s); }
      if (s1 instanceof java.util.Set) {
        return (! Collections.disjoint(s,(java.util.Set) s1)); } }

    throw new UnsupportedOperationException(
      "Can't test for interesection of " +
        s0.getClass().getSimpleName() +
        " and " +
        s1.getClass().getSimpleName()); }

  //--------------------------------------------------------------
  // summaries
  //--------------------------------------------------------------

  public static final int 
  countStatic (final IntegerInterval[] s0,
               final IntegerInterval[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (intersects(s0[i],s1[i])) { k++; } }
    return k; }

  public static final int 
  countVirtual (final IntegerInterval[] s0,
                final IntegerInterval[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].intersects(s1[i])) { k++; } }
    return k; }

  public static final int 
  countInterface (final Set[] s0,
                  final IntegerInterval[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].intersects(s1[i])) { k++; } }
    return k; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Intersects () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  ///--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
