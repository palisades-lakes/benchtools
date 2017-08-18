package palisades.lakes.bench.java.matrices;

import java.util.Arrays;

import clojure.lang.IFn;

/** @author palisades dot lakes at gmail dot com
 * @since 2017-08-10
 * @version 2017-08-10
 */
public final class DiagonalMatrix implements Matrix {

  private final int _nrows;
  private final int _ncols;
  private final double[] _elts;

  //--------------------------------------------------------------

  public final DiagonalMatrix add (final DiagonalMatrix m) {
    System.out.println("call DiagonalMatrix.add(DiagonalMatrix)");
    assert nrows() == m.nrows();
    assert ncols() == m.ncols();
    final int n = Math.min(nrows(),ncols());
    final double[] e = new double[n];
    for (int i=0;i<n;i++) { e[i] = _elts[i] + m._elts[i]; } 
    return new DiagonalMatrix(_nrows,_ncols,e); } 

  public final DenseMatrix add (final DenseMatrix m) {
    System.out.println("call DiagonalMatrix.add(DenseMatrix)");
    assert nrows() == m.nrows();
    assert ncols() == m.ncols();
    final double[][] e = new double[nrows()][ncols()];
    for (int i=0;i<nrows();i++) {
      for (int j=0;j<ncols();j++) {
        e[i][j] = get(i,j) + m.get(i,j); } }
    return DenseMatrix.make(e); } 

  //--------------------------------------------------------------

  @Override
  public final int nrows () { return _nrows;  }

  @Override
  public final int ncols () { return _ncols; }

  @Override
  public final double get (final int i, 
                           final int j) {
    if (i != j) { return 0.0; }
    return _elts[i]; }

  @Override
  public final Matrix add (final Matrix m) {
    System.out.println("call DiagonalMatrix.add(Matrix)");
    assert nrows() == m.nrows();
    assert ncols() == m.ncols();
    final double[][] e = new double[nrows()][ncols()];
    for (int i=0;i<nrows();i++) {
      for (int j=0;j<ncols();j++) {
        e[i][j] = get(i,j) + m.get(i,j); } }
    return DenseMatrix.make(e); } 

  //--------------------------------------------------------------

  private DiagonalMatrix (final int nr,
                          final int nc,
                          final double[] e) {
    _nrows = nr;
    _ncols = nc;
    _elts = e; }

  public static final DiagonalMatrix make (final int nr,
                                           final int nc,
                                           final double[] e) {
    final int n = Math.min(nr,nc);
    assert n == e.length;
    return new DiagonalMatrix(nr,nc,Arrays.copyOf(e,n)); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   */

  public static final DiagonalMatrix generate (final IFn.D g,
                                               final int nr,
                                               final int nc) {
    final int n = Math.min(nr,nc);
    final double[] e = new double[n];
    for (int i=0;i<n;i++) { e[i] = g.invokePrim(); } 
    return make(nr,nc,e); }

  //--------------------------------------------------------------
}
