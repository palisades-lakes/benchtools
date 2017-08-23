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
 * @version 2017-08-22
 */

public interface Vector {

  /** Assuming the space has a canonical basis, return the
   * <code>i</code>-th coordinate in that basis.
   * <p>
   * It's questionable whether this really belongs in an interface
   * implemented by all 'vectors'. A given 'vector' object can
   * be considered an element of many spaces, and the natural
   * meaning of 'coordinate' might be different for each.
   * <p>
   * Especially problematic that this has to return a 
   * <code>double</code>, so it's not exactly the value of the
   * coordinate in most cases.
   */
  double coordinate (final int i);
  
  double l1Norm ();
  
  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
