package com.mxme.lcdui;

/**************************
 *  Spline class
 * 
 * Plots a spline given an array of points.
 * 
 *  Notes:

 *     
 */
import javax.microedition.lcdui.Graphics;

public class Spline
{
    private static final long virtualFloatPrecision = 10000;
    
    static public class Polygon
    {
        public int npoints = 0;
        public int[] xpoints = new int[255];
        public int[] ypoints = new int[255];
    
        public void addPoint(int x, int y)
        {
            xpoints[npoints] = x;
            ypoints[npoints] = y;
            ++npoints;
        }
        
        public void clear()
        {
            for( int i = 0; i < npoints; ++i )
            {
                xpoints[i] = 0; ypoints[i] = 0;
            }
            npoints = 0;
        }
        
        public void paint(Graphics g)
        {
            for( int i = 0; i < npoints-1; ++i )
                g.drawLine(xpoints[i], ypoints[i], xpoints[i+1], ypoints[i+1]);
        }
    }
    
    static public class Cubic 
    {
        long a, b, c, d; /* a + b*u + c*u^2 +d*u^3 */

        public Cubic(long a, long b, long c, long d)
        {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        // evaluate cubic
        public long eval(long u) 
        {
            return ((((((((d * u) / virtualFloatPrecision) + c) * u) / virtualFloatPrecision) + b) * u) / virtualFloatPrecision) + a;
        }
    }
    
    public Cubic[] X, Y;
    public Polygon spline = new Polygon();
    static final int STEPS = 12;
    
    /***
     * calculates the natural cubic spline that interpolates
     *   y[0], y[1], ... y[n]
     *   The first segment is returned as
     *   C[0].a + C[0].b*u + C[0].c*u^2 + C[0].d*u^3 0 <= u < 1
     *   the other segments are in C[1], C[2], ...  C[n-1] 
     */
    Cubic[] calcNaturalCubic(int n, int[] x) 
    {
        long[] gamma = new long[n + 1];
        long[] delta = new long[n + 1];
        long[] D = new long[n + 1];
        int i;
        
        /* We solve the equation
            [2 1       ] [D[0]]   [3(x[1] - x[0])  ]
            |1 4 1     | |D[1]|   |3(x[2] - x[0])  |
            |  1 4 1   | | .  | = |      .         |
            |    ..... | | .  |   |      .         |
            |     1 4 1| | .  |   |3(x[n] - x[n-2])|
            [       1 2] [D[n]]   [3(x[n] - x[n-1])]

            by using row operations to convert the matrix to upper triangular
            and then back sustitution.  The D[i] are the derivatives at the knots.
        */
        // adjust float precision
        for( i = 0; i <= n; ++i )
            x[i] *= virtualFloatPrecision;

        gamma[0] = (1 * virtualFloatPrecision) / 2;
        for( i = 1; i < n; i++ ) 
            gamma[i] = (1 * (virtualFloatPrecision * virtualFloatPrecision)) / ((4 * virtualFloatPrecision) - gamma[i-1]);
        gamma[n] = (1 * (virtualFloatPrecision * virtualFloatPrecision)) / ((2 * virtualFloatPrecision) - gamma[n-1]);

        delta[0] = (3 * (x[1] - x[0]) * gamma[0]) / virtualFloatPrecision;
        for( i = 1; i < n; i++ ) 
            delta[i] = ((3 * (x[i+1] - x[i-1]) - delta[i-1]) * gamma[i]) / virtualFloatPrecision;
        delta[n] = ((3 * (x[n] - x[n-1]) - delta[n-1]) * gamma[n]) / virtualFloatPrecision;

        D[n] = delta[n];
        for( i = n - 1; i >= 0; i-- )
            D[i] = delta[i] - ((gamma[i] * D[i+1]) / virtualFloatPrecision);

        /* now compute the coefficients of the cubics */
        Cubic[] C = new Cubic[n];
        for( i = 0; i < n; i++ )
            C[i] = new Cubic(x[i], D[i], 3 * (x[i+1] - x[i]) - 2 * D[i] - D[i+1], 2 * (x[i] - x[i+1]) + D[i] + D[i+1]);
        
        return C;
    }

    public void computePoints(Polygon pts)
    {
        spline.clear();
        X = calcNaturalCubic(pts.npoints-1, pts.xpoints);
        Y = calcNaturalCubic(pts.npoints-1, pts.ypoints);
    }
    
    public boolean isReady()
    {
        return X != null;
    }
    
    public int getMaxSteps()
    {
        return isReady() ? X.length * (STEPS+1) : 0;
    }
    
    public int[] plotSplineStep(int s)
    {
        if( s == 0 )
        {
            return new int[] { (int)(X[0].eval(0) / virtualFloatPrecision), (int)(Y[0].eval(0) / virtualFloatPrecision) };
        }
        else
        {
            int i = s / (STEPS+1);
            s -= (STEPS+1) * i;
            long u = ((s * (virtualFloatPrecision * virtualFloatPrecision)) / (STEPS * virtualFloatPrecision));
            return new int[] { (int)(X[i].eval(u) / virtualFloatPrecision), (int)(Y[i].eval(u) / virtualFloatPrecision) };
        }
    }
    
    public void plotSpline()
    {
        spline.addPoint((int)(X[0].eval(0) / virtualFloatPrecision), (int)(Y[0].eval(0) / virtualFloatPrecision));
        for( int i = 0; i < X.length; i++ )
        {
            for( int j = 1; j <= STEPS; j++ )
            {
                long u = ((j * (virtualFloatPrecision * virtualFloatPrecision)) / (STEPS * virtualFloatPrecision));
                spline.addPoint((int)(X[i].eval(u) / virtualFloatPrecision), (int)(Y[i].eval(u) / virtualFloatPrecision));
            }
        }
    }
    
    /* draw a cubic spline */
    public void paint(Graphics g)
    {
        //spline.paint(g);
        
        int last = getMaxSteps()-1;
        if (last > 0)
        {
            int []from = plotSplineStep(last);
            int []to;
            for (int i=last-1; i>=0; i--)
            {
                to = plotSplineStep(i);
                g.drawLine(from[0], from[1], to[0], to[1]);
                from = to;
            }
        }
    }
    
}




