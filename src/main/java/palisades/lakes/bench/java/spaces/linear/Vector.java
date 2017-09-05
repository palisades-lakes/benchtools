package palisades.lakes.bench.java.spaces.linear;

//----------------------------------------------------------------
/** Interface for elements of real linear (aka vector) spaces.
 * A space is <i>linear</i> if it's closed under linear
 * combinations: <i>a*x + b*y</i> where <i>a</i>
 * and <i>b</i> are numbers and <i>x</i> and 
 * <i>y</i> are <code>Vector</code>.
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-09-05
 */

public interface Vector {

  /** Here as a convenience. Doesn't belong in a vector interface,
   * because same vector can be in many different normed spaces.
   */
  double l1Norm ();
  
  /** Almost certainly would want scalars to be primitive,
   * not Object references. And implementation might
   * depend on coordinate type of Vectors, so potentially
   * 6x6x6x6=1296 methods, more if high precision 
   * (BigInteger, Rational) reference types are included.
   * <p>
   * Here as a convenience. Doesn't belong in a vector interface,
   * because same vector can be in many different linear spaces,
   * with differing interpretations of linear combination.
   */
  Vector linearCombination (final Number a0,
                            final Number a1,
                            final Vector v1);
  
  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
