package palisades.lakes.bench.java.spaces.linear;

import palisades.lakes.bench.java.spaces.linear.r2.D2;
import palisades.lakes.bench.java.spaces.linear.r2.D22;

/** Static axpy, numbers (<b>R</b?<sup>1</sup>) 
 * as well as vectors.
 * 
 * TODO: use Math.fma() for float cases once JDK 9 is available.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-22
 * @version 2017-08-26
 */

@SuppressWarnings("unchecked")
public final class Axpy extends Object {

  //--------------------------------------------------------------
  // TODO: code generation
  // TODO: articulate rules for 
  //       input precision -> output precision
  // TODO: just default to Java arithmetic promotions?
  // TODO: use fused-multiply-add for floats when available
  //--------------------------------------------------------------
  // byte, ...
  //--------------------------------------------------------------
  public static int axpy (byte a,byte x,byte y) { return a*x + y; }
  public static int axpy (byte a,byte x,short y) { return a*x + y; }
  public static long axpy (byte a,byte x,int y) { return a*x + (long) y; }
  public static long axpy (byte a,byte x,long y) { return a*x + y; }
  public static float axpy (byte a,byte x,float y) { return a*x + y; }
  public static double axpy (byte a,byte x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static int axpy (byte a,short x,byte y) { return a*x + y; }
  public static int axpy (byte a,short x,short y) { return a*x + y; }
  public static long axpy (byte a,short x,int y) { return a*x + (long) y; }
  public static long axpy (byte a,short x,long y) { return a*x + y; }
  public static float axpy (byte a,short x,float y) { return a*x + y; }
  public static double axpy (byte a,short x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (byte a,int x,byte y) { return a*((long) x) + y; }
  public static long axpy (byte a,int x,short y) { return a*((long) x) + y; }
  public static long axpy (byte a,int x,int y) { return a*((long) x) + y; }
  public static long axpy (byte a,int x,long y) { return a*((long) x) + y; }
  public static float axpy (byte a,int x,float y) { return a*x + y; }
  public static double axpy (byte a,int x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (byte a,long x,byte y) { return a*x + y; }
  public static long axpy (byte a,long x,short y) { return a*x + y; }
  public static long axpy (byte a,long x,int y) { return a*x + y; }
  public static long axpy (byte a,long x,long y) { return a*x + y; }
  public static float axpy (byte a,long x,float y) { return a*x + y; }
  public static double axpy (byte a,long x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (byte a,float x,byte y) { return a*x + y; }
  public static float axpy (byte a,float x,short y) { return a*x + y; }
  public static float axpy (byte a,float x,int y) { return a*x + y; }
  public static float axpy (byte a,float x,long y) { return a*x + y; }
  public static float axpy (byte a,float x,float y) { return a*x + y; }
  public static double axpy (byte a,float x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (byte a,double x,byte y) { return a*x + y; }
  public static double axpy (byte a,double x,short y) { return a*x + y; }
  public static double axpy (byte a,double x,int y) { return a*x + y; }
  public static double axpy (byte a,double x,long y) { return a*x + y; }
  public static double axpy (byte a,double x,float y) { return a*x + y; }
  public static double axpy (byte a,double x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  // short, ...
  //--------------------------------------------------------------
  public static int axpy (short a,byte x,byte y) { return a*x + y; }
  public static int axpy (short a,byte x,short y) { return a*x + y; }
  public static long axpy (short a,byte x,int y) { return a*x + (long) y; }
  public static long axpy (short a,byte x,long y) { return a*x + y; }
  public static float axpy (short a,byte x,float y) { return a*x + y; }
  public static double axpy (short a,byte x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (short a,short x,byte y) { return a*x + y; }
  public static long axpy (short a,short x,short y) { return a*x + y; }
  public static long axpy (short a,short x,int y) { return a*x + (long) y; }
  public static long axpy (short a,short x,long y) { return a*x + y; }
  public static float axpy (short a,short x,float y) { return a*x + y; }
  public static double axpy (short a,short x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (short a,int x,byte y) { return a*((long) x) + y; }
  public static long axpy (short a,int x,short y) { return a*((long) x) + y; }
  public static long axpy (short a,int x,int y) { return a*((long) x) + y; }
  public static long axpy (short a,int x,long y) { return a*((long) x) + y; }
  public static float axpy (short a,int x,float y) { return a*x + y; }
  public static double axpy (short a,int x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (short a,long x,byte y) { return a*x + y; }
  public static long axpy (short a,long x,short y) { return a*x + y; }
  public static long axpy (short a,long x,int y) { return a*x + y; }
  public static long axpy (short a,long x,long y) { return a*x + y; }
  public static float axpy (short a,long x,float y) { return a*x + y; }
  public static double axpy (short a,long x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (short a,float x,byte y) { return a*x + y; }
  public static float axpy (short a,float x,short y) { return a*x + y; }
  public static float axpy (short a,float x,int y) { return a*x + y; }
  public static float axpy (short a,float x,long y) { return a*x + y; }
  public static float axpy (short a,float x,float y) { return a*x + y; }
  public static double axpy (short a,float x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (short a,double x,byte y) { return a*x + y; }
  public static double axpy (short a,double x,short y) { return a*x + y; }
  public static double axpy (short a,double x,int y) { return a*x + y; }
  public static double axpy (short a,double x,long y) { return a*x + y; }
  public static double axpy (short a,double x,float y) { return a*x + y; }
  public static double axpy (short a,double x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  // int, ...
  //--------------------------------------------------------------
  public static long axpy (int a,byte x,byte y) { return a*x + y; }
  public static long axpy (int a,byte x,short y) { return a*x + y; }
  public static long axpy (int a,byte x,int y) { return a*x + (long) y; }
  public static long axpy (int a,byte x,long y) { return a*x + y; }
  public static float axpy (int a,byte x,float y) { return a*x + y; }
  public static double axpy (int a,byte x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (int a,short x,byte y) { return a*x + y; }
  public static long axpy (int a,short x,short y) { return a*x + y; }
  public static long axpy (int a,short x,int y) { return a*x + (long) y; }
  public static long axpy (int a,short x,long y) { return a*x + y; }
  public static float axpy (int a,short x,float y) { return a*x + y; }
  public static double axpy (int a,short x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (int a,int x,byte y) { return a*((long) x) + y; }
  public static long axpy (int a,int x,short y) { return a*((long) x) + y; }
  public static long axpy (int a,int x,int y) { return a*((long) x) + y; }
  public static long axpy (int a,int x,long y) { return a*((long) x) + y; }
  public static float axpy (int a,int x,float y) { return a*x + y; }
  public static double axpy (int a,int x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static long axpy (int a,long x,byte y) { return a*x + y; }
  public static long axpy (int a,long x,short y) { return a*x + y; }
  public static long axpy (int a,long x,int y) { return a*x + y; }
  public static long axpy (int a,long x,long y) { return a*x + y; }
  public static float axpy (int a,long x,float y) { return a*x + y; }
  public static double axpy (int a,long x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (int a,float x,byte y) { return a*x + y; }
  public static float axpy (int a,float x,short y) { return a*x + y; }
  public static float axpy (int a,float x,int y) { return a*x + y; }
  public static float axpy (int a,float x,long y) { return a*x + y; }
  public static float axpy (int a,float x,float y) { return a*x + y; }
  public static double axpy (int a,float x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (int a,double x,byte y) { return a*x + y; }
  public static double axpy (int a,double x,short y) { return a*x + y; }
  public static double axpy (int a,double x,int y) { return a*x + y; }
  public static double axpy (int a,double x,long y) { return a*x + y; }
  public static double axpy (int a,double x,float y) { return a*x + y; }
  public static double axpy (int a,double x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  // float, ...
  //--------------------------------------------------------------
  public static float axpy (float a,byte x,byte y) { return a*x + y; }
  public static float axpy (float a,byte x,short y) { return a*x + y; }
  public static float axpy (float a,byte x,int y) { return a*x + y; }
  public static float axpy (float a,byte x,long y) { return a*x + y; }
  public static float axpy (float a,byte x,float y) { return a*x + y; }
  public static double axpy (float a,byte x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (float a,short x,byte y) { return a*x + y; }
  public static float axpy (float a,short x,short y) { return a*x + y; }
  public static float axpy (float a,short x,int y) { return a*x + y; }
  public static float axpy (float a,short x,long y) { return a*x + y; }
  public static float axpy (float a,short x,float y) { return a*x + y; }
  public static double axpy (float a,short x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (float a,int x,byte y) { return a*x + y; }
  public static float axpy (float a,int x,short y) { return a*x + y; }
  public static float axpy (float a,int x,int y) { return a*x + y; }
  public static float axpy (float a,int x,long y) { return a*x + y; }
  public static float axpy (float a,int x,float y) { return a*x + y; }
  public static double axpy (float a,int x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (float a,long x,byte y) { return a*x + y; }
  public static float axpy (float a,long x,short y) { return a*x + y; }
  public static float axpy (float a,long x,int y) { return a*x + y; }
  public static float axpy (float a,long x,long y) { return a*x + y; }
  public static float axpy (float a,long x,float y) { return a*x + y; }
  public static double axpy (float a,long x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static float axpy (float a,float x,byte y) { return a*x + y; }
  public static float axpy (float a,float x,short y) { return a*x + y; }
  public static float axpy (float a,float x,int y) { return a*x + y; }
  public static float axpy (float a,float x,long y) { return a*x + y; }
  public static float axpy (float a,float x,float y) { return a*x + y; }
  public static double axpy (float a,float x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (float a,double x,byte y) { return a*x + y; }
  public static double axpy (float a,double x,short y) { return a*x + y; }
  public static double axpy (float a,double x,int y) { return a*x + y; }
  public static double axpy (float a,double x,long y) { return a*x + y; }
  public static double axpy (float a,double x,float y) { return a*x + y; }
  public static double axpy (float a,double x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  // double, ...
  //--------------------------------------------------------------
  public static double axpy (double a,byte x,byte y) { return a*x + y; }
  public static double axpy (double a,byte x,short y) { return a*x + y; }
  public static double axpy (double a,byte x,int y) { return a*x + y; }
  public static double axpy (double a,byte x,long y) { return a*x + y; }
  public static double axpy (double a,byte x,float y) { return a*x + y; }
  public static double axpy (double a,byte x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (double a,short x,byte y) { return a*x + y; }
  public static double axpy (double a,short x,short y) { return a*x + y; }
  public static double axpy (double a,short x,int y) { return a*x + y; }
  public static double axpy (double a,short x,long y) { return a*x + y; }
  public static double axpy (double a,short x,float y) { return a*x + y; }
  public static double axpy (double a,short x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (double a,int x,byte y) { return a*x + y; }
  public static double axpy (double a,int x,short y) { return a*x + y; }
  public static double axpy (double a,int x,int y) { return a*x + y; }
  public static double axpy (double a,int x,long y) { return a*x + y; }
  public static double axpy (double a,int x,float y) { return a*x + y; }
  public static double axpy (double a,int x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (double a,long x,byte y) { return a*x + y; }
  public static double axpy (double a,long x,short y) { return a*x + y; }
  public static double axpy (double a,long x,int y) { return a*x + y; }
  public static double axpy (double a,long x,long y) { return a*x + y; }
  public static double axpy (double a,long x,float y) { return a*x + y; }
  public static double axpy (double a,long x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (double a,float x,byte y) { return a*x + y; }
  public static double axpy (double a,float x,short y) { return a*x + y; }
  public static double axpy (double a,float x,int y) { return a*x + y; }
  public static double axpy (double a,float x,long y) { return a*x + y; }
  public static double axpy (double a,float x,float y) { return a*x + y; }
  public static double axpy (double a,float x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  public static double axpy (double a,double x,byte y) { return a*x + y; }
  public static double axpy (double a,double x,short y) { return a*x + y; }
  public static double axpy (double a,double x,int y) { return a*x + y; }
  public static double axpy (double a,double x,long y) { return a*x + y; }
  public static double axpy (double a,double x,float y) { return a*x + y; }
  public static double axpy (double a,double x,double y) { return a*x + y; }
  //--------------------------------------------------------------
  // linear spaces
  //--------------------------------------------------------------

  public final static Vector axpy (final LinearFunction a,
                                   final Vector x,
                                   final Vector y) {
    return a.axpy(x,y); }

  public final static Vector axpy (final Object a,
                                   final Object x,
                                   final Object y) {
    if (a instanceof LinearFunction) {
      final LinearFunction lfa = (LinearFunction) a;
      if (x instanceof Vector) {
        final Vector vx = (Vector) x;
        if (y instanceof Vector) {
          return lfa.axpy(vx,(Vector) y); } } }

    throw new IllegalArgumentException(
      "no axpy methods for:" 
        + a.getClass().getSimpleName()
        + ", " 
        + x.getClass().getSimpleName() 
        + ", "
        + x.getClass().getSimpleName()); }

  //--------------------------------------------------------------
  // summaries
  //--------------------------------------------------------------

  public static final double 
  sumL1Norms (final D22[] a,
              final D2[] x,
              final D2[] y) {
    // FIXME: this is not an accurate sum!
    final int n = a.length;
    assert n == x.length;
    assert n == y.length;
    double s = 0.0;
    for (int i=0;i<n;i++) { s += a[i].axpy(x[i],y[i]).l1Norm(); }
    return s; }

  public static final double 
  sumL1Norms (final LinearFunction[] a,
              final Vector[] x,
              final Vector[] y) {
    // FIXME: this is not an accurate sum!
    final int n = a.length;
    assert n == x.length;
    assert n == y.length;
    double s = 0.0;
    for (int i=0;i<n;i++) { s += a[i].axpy(x[i],y[i]).l1Norm(); }
    return s; }

  public static final double 
  sumL1Norms (final Object[] a,
              final Object[] x,
              final Object[] y) {
    // FIXME: this is not an accurate sum!
    final int n = a.length;
    assert n == x.length;
    assert n == y.length;
    double s = 0.0;
    for (int i=0;i<n;i++) { s += axpy(a[i],x[i],y[i]).l1Norm(); }
    return s; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private Axpy () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  ///--------------------------------------------------------------
} // end class
//--------------------------------------------------------------
