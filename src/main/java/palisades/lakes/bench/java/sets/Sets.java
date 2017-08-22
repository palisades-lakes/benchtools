package palisades.lakes.bench.java.sets;

/** 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-05-22
 * @version 2017-08-21
 */
public final class Sets {

  //--------------------------------------------------------------
  // benchmarks
  //--------------------------------------------------------------

  public static final int 
  countIntersections (final IntegerInterval[] s0,
                      final IntegerInterval[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].intersects(s1[i])) { k++; } }
    return k; }

  public static final int 
  countIntersections (final Set[] s0,
                      final IntegerInterval[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].intersects(s1[i])) { k++; } }
    return k; }

  //--------------------------------------------------------------

  public static final int 
  countContained (final IntegerInterval[] s0,
                  final int[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].contains(s1[i])) { k++; } }
    return k; }

  public static final int 
  countContained (final Set[] s0,
                  final int[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].contains(s1[i])) { k++; } }
    return k; }

  public static final int 
  countContained (final IntegerInterval[] s0,
                  final Integer[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].contains(s1[i])) { k++; } }
    return k; }

  public static final int 
  countContained (final Set[] s0,
                  final Integer[] s1) {
    int k = 0;
    final int n = s0.length;
    assert n == s1.length;
    for (int i=0;i<n;i++) { 
      if (s0[i].contains(s1[i])) { k++; } }
    return k; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
