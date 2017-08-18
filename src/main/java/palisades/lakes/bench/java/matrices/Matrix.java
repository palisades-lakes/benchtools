package palisades.lakes.bench.java.matrices;

/** @author palisades dot lakes at gmail dot com
 * @since 2017-08-19
 * @version 2017-08-19
 */

public interface Matrix {
  int nrows ();
  int ncols ();
  double get (int i, int j);
  Matrix add (Matrix m); }
