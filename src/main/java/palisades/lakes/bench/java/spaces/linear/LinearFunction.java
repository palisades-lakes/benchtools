package palisades.lakes.bench.java.spaces.linear;

import clojure.lang.IFn;
import clojure.lang.ISeq;

//----------------------------------------------------------------
/** Interface for linear functions on linear (aka vector) spaces.
 * A function is linear if it distributes over linear 
 * combinations: 
 * <i>f(a*x + b*v1) = a*f(v0) + b*f(v1)</i> 
 * where <i>a</i> and <i>b</i> are numbers and <i>x</i> and 
 * <i>y</i> are <code>Vector</code>.
 * <p>
 * This is a bad design. To do it right, we need generic functions
 * (aka multimethods) equivalent to fully dynamic method lookup.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-22
 */

public interface LinearFunction extends IFn {

  Vector invoke (Vector v);
  
  /** Basic operation for affine functions. */
  Vector axpy (Vector x, Vector y);
  
  //--------------------------------------------------------------
  // IFn interface
  //--------------------------------------------------------------
  
  @Override
  default Object invoke (final Object v) { 
    return invoke((Vector) v); }
  
  @Override
  default Object applyTo (final ISeq s) { 
    assert 1 == s.count();
    return invoke((Vector) s.first()); }
  
  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
