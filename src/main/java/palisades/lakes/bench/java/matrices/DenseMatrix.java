package palisades.lakes.bench.java.matrices;

import java.util.Arrays;

import clojure.lang.IFn;

/** @author palisades dot lakes at gmail dot com
 * @since 2017-08-10
 * @version 2017-08-10
 */
public final class DenseMatrix implements Matrix {
  
  private final int _nrows;
  private final int _ncols;
  private final double[][] _elts;

  public final DenseMatrix add (final DiagonalMatrix m) {
    System.out.println("call DenseMatrix.add(DiagonalMatrix)");
    assert nrows() == m.nrows();
    assert ncols() == m.ncols();
    final double[][] e = new double[nrows()][ncols()];
    for (int i=0;i<nrows();i++) {
      for (int j=0;j<ncols();j++) {
        if (i == j) { e[i][j] = get(i,j) + m.get(i,j); } 
        else { e[i][j] = get(i,j); } } }
    return DenseMatrix.make(e); } 

  public final DenseMatrix add (final DenseMatrix m) {
    System.out.println("call DenseMatrix.add(DenseMatrix)");
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
    return _elts[i][j]; }

  @Override
  public final Matrix add (final Matrix m) {
    System.out.println("call DenseMatrix.add(Matrix)");
    assert nrows() == m.nrows();
    assert ncols() == m.ncols();
    final double[][] e = new double[nrows()][ncols()];
    for (int i=0;i<nrows();i++) {
      for (int j=0;j<ncols();j++) {
        e[i][j] = get(i,j) + m.get(i,j); } }
    return new DenseMatrix(_nrows,_ncols,e); } 

  //--------------------------------------------------------------
  
  private DenseMatrix (final int nr,
                       final int nc,
                       final double[][] e) {
    assert nr == e.length;
    for (int i=0;i<nrows();i++) { assert nc == e[i].length; }
    _nrows = nr;
    _ncols = nc;
    _elts = e; }
  
  public static final DenseMatrix make (final double[][] e) {
    final int nr = e.length;
    final int nc = e[0].length;
    final double[][] ee = new double[nr][];
    for (int i=0;i<nr;i++) {
      assert nc == e[i].length;
      ee[i] = Arrays.copyOf(e[i],nc); }
    return new DenseMatrix(nr,nc,ee); }
  
  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   */
  
  public static final DenseMatrix generate (final IFn.D g,
                                            final int nr,
                                            final int nc) {

    final double[][] e = new double[nr][nc];
    for (int i=0;i<nr;i++) {
      for (int j=0;j<nc;j++) { e[i][j] = g.invokePrim(); } }
    return make(e); }

  //--------------------------------------------------------------

}
