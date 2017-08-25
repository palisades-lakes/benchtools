package palisades.lakes.bench.java.sets;

/** Primitive return test.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-23
 * @version 2017-08-23
 */

@SuppressWarnings("unchecked")
public final class Diameter extends Object {

  //--------------------------------------------------------------
  
  public final static double diameter (final java.util.Set s) {
    if (1 >= s.size()) { return 0.0; }
    double smin = Double.POSITIVE_INFINITY;
    double smax = Double.NEGATIVE_INFINITY;
    for (final Object x : s) {
      final double xx = ((Number) x).doubleValue();
      if (Double.isNaN(xx)) { return Double.NaN; }
      if (xx < smin) { smin = xx; }
      if (xx > smax) { smax = xx; } }
    return smax - smin; }

   //--------------------------------------------------------------
  // lookup
  //--------------------------------------------------------------

  public static final double diameter (final Object s) {

    if (s instanceof Set) { 
      return ((Set) s).diameter(); }
    
    if (s instanceof java.util.Set) { 
      return diameter((java.util.Set) s); }
  
  throw new IllegalArgumentException(
    "no diameter method for " + s.getClass()); }

  //--------------------------------------------------------------
  // summaries
  //--------------------------------------------------------------

  public static final double 
  maxStatic (final IntegerInterval[] s) {
    double dmax = Double.NEGATIVE_INFINITY;
    final int n = s.length;
    for (int i=0;i<n;i++) { 
      final double d = diameter(s[i]); 
      if (dmax < d) { dmax = d; } }
    return dmax; }

  public static final double 
  maxVirtual (final IntegerInterval[] s) {
    double dmax = Double.NEGATIVE_INFINITY;
    final int n = s.length;
    for (int i=0;i<n;i++) { 
      final double d = s[i].diameter(); 
      if (dmax < d) { dmax = d; } }
    return dmax; }

  public static final double 
  maxInterface (final Set[] s) {
    double dmax = Double.NEGATIVE_INFINITY;
    final int n = s.length;
    for (int i=0;i<n;i++) { 
      final double d = s[i].diameter(); 
      if (dmax < d) { dmax = d; } }
    return dmax; }

  public static final double 
  maxLookup (final Object[] s) {
    double dmax = Double.NEGATIVE_INFINITY;
    final int n = s.length;
    for (int i=0;i<n;i++) { 
      final double d = diameter(s[i]); 
      if (dmax < d) { dmax = d; } }
    return dmax; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Diameter () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  ///--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
