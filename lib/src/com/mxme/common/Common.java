package com.mxme.common;

/**************************
 * Common class
 * 
 * Common utilities
 * 
 *
 *
 */
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Display;
import java.util.Random;

// MatuX Library
import com.mxme.lcdui.IPainter;

public class Common 
{
    // ------------------------------------------ SYSTEM METHODS --------------------------
    /**
     * Safe call to GC
     * 
     */
    public static final void callGC()
    {
    	System.gc();
    	try {
			Thread.sleep(MXMEConstants.GC_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }


    // ----------------------------------------- FILE UTILITIES --------------------------
    /***
     *  Checks if a file exists
     * Must provide path beginnig with "/" and an extension.
     * Ex: "/mydir/myimage.png"
     * 
     * @param boolean, whether the file exists or not
     */
    public static final boolean fileExists(String filename)
    {
        return new StringBuffer().getClass().getResourceAsStream(filename) != null;
    }
    
    // ----------------------------- LOADING IMAGES UTILS -------------------------------
    /**
	 * Initializes a new image
	 * @param image Path to image
	 * @return The image
	 */
	public static Image initializeImage(String image)
    {
		Image img = null;
		try {
			img = Image.createImage(image);
		} catch (Exception e) { }

		return img;
	}

    /**
     *  Loads and returns the loaded image.
     *
     * @param filename : filename without '/' or extension
     */
    public static final Image loadImageEx(String filename, boolean mirrored)
    {
        // attach '/' prefix and .PNG extension
        filename = "/" + filename + ".png";

        Image new_image = initializeImage(filename);
        
        // isn't it better just to do a call to Sprite.setTransform?!?
        /*** @todo why Painter/Sprite classes doesn't use MIDP2 Sprite?? ***/
        if( mirrored )
            new_image = (Image)painter.createFlippedImage((Object)new_image, 0, 0, new_image.getWidth(), new_image.getHeight(), MXMEConstants.FLIP_HORIZONTAL);
        
        return new_image;
    }
    
    public static Image loadImage(String filename)
    {
        return loadImageEx(filename, false);
    }

    // ----------------------------------------- PAINT UTILITIES --------------------------
    public static final String PAINTER_CLASS_NAME = "com.mxme.common.midp20.Painter";
    public static IPainter painter;
    
    /**
     * Paints the hole screen with a specified color
     * @param g: Graphics reference
     * @param color: color to be painted
     */
    public static final void clearScreen(Graphics g, Canvas c, int color)
    {
        g.setColor(color);
        g.setClip(0, 0, c.getWidth(), c.getHeight());
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
    }
   
    public static final boolean supportsAlphaBlending(Display d)
    {
        return d.numAlphaLevels() > 2;
    }

    public static final void drawAlphaRect(Graphics g, byte alpha, int color, int border_color, int x, int y, int w, int h)
    {
        // create alpha rgb image
        int size = w * h;
        int[] argb = new int[size];
        for( int o = 0; o < size; ++o )
            argb[o] = color|(alpha<<24);
        // display alpha'd image
        Image alpha_back = Image.createRGBImage(argb, w, h, true);
        g.drawImage(alpha_back, x, y, 0);
        // draw border
        g.setColor(border_color);
        g.drawRect(x, y, w, h);
    }

    // ------------------------------------ MATH UTILS ------------------------------------
    /**
     * Returns the square root of the given integer. Do not use values larger
     * than 2.000.000
     * 
     */
    public static final int sqrt(int a)
    {
        int last_temp = 0;
        int temp = a / 3;
        for( int i = 10; i-- > 0;)
        {
            if( last_temp == temp )
                break;
            last_temp = temp;
            temp = (temp + a / temp) >> 1;
        }
        return temp;
    }

    /**
     * Returns a list of all divisors for a given number
     *
     * @param number
     * @param max: Max amount of divisors to be returned
     *
     * @return
     */
    public static int[] getDivisors(int number, int max)
    {
    	int[] res = null;
    	if( number == 0 )
        {
    		res = new int[1];
    		res[0] = 0;
    	}
        else
        {
    		number = Math.abs(number);
    		max = Math.abs(max);
    		int[] aux = new int[max];
    		int count = 0;
    		for (int i = 2; i <= max; i++)
    			if (number % i == 0)
    				aux[count++] = i;

            if (count != 0)
            {
    			res = new int[count];
    			System.arraycopy(aux, 0, res, 0, count);
    		}
    		aux = null;
    	}

    	return res;
    }

    /**
     * Returns the maximum value in an array
     *
     * @param array
     *
     * @return int
     */
    public static final int getMax(int[] array)
    {
    	int max = Integer.MIN_VALUE;
        
    	if( array != null && array.length > 0 )
        {
    		max = array[0];
    		for( int i = 1; i < array.length; i++ )
            {
    			if( array[i] > max )
    				max = array[i];
    		}
    	}

    	return max;
    }

    /**
     * Returns the minimum value in an array
     *
     * @param array
     *
     * @return int
     */
    public static final int getMin(int[] array)
    {
    	int min = Integer.MAX_VALUE;

        if( array != null && array.length > 0 )
        {
    		min = array[0];
    		for( int i = 1; i < array.length; i++ )
            {
    			if( array[i] < min )
    				min = array[i];
    		}
    	}
        
    	return min;
    }

    /**
     * Sums all the items in a given array and returns the total
     *
     * @param array
     * @return
     */
    public static final long sumOfAll(long[] array)
    {
    	long res = 0L;

    	if( array != null )
    		for( int i = 0; i < array.length; i++ )
    			res += array[i];

    	return res;
    }
    
    /**
     * QuHu (Quick & Hugly random number generator)
     * 
     * initRand(): Initializes random seed
     * 
     * @param long seed, random seed
     */
    private static long quick_seed;
    public static final void initRand(long seed)
    {
        quick_seed = seed;
    }
    /**
     * Returns a random number -from- -to-.
     * 
     * @param int from, to: top and bottom numbers to return, both inclusive.
     * @return random number from var 'from' to var 'to'
     */
    private static final int QUICK_A = 1664525;
    private static final int QUICK_B = 1013904223;
    public static final int rand(int from, int to)
    {
        if( (to + 1) - from == 0 ) return from;
        
        quick_seed = (QUICK_A * quick_seed) + QUICK_B;
        long ans = (quick_seed << 16) / Integer.MAX_VALUE;
		
	if( ans < 0 ) ans = -ans;
        ans %= (to + 1) - from;
        
        return (int)(from + ans);
    }
    
    /**
     * Devuelve un vector con la misma direccion al primero pero escalado de tal
     * forma que su norma sea NORMAL
     * <p>
     * Returns a vector with the same direction but scaled to Normal (multiplied
     * by 1024 to preserve precision).
     * 
     * @param param : array de dos int
     * @param NORMAL : integer
     * @return int[2] = {x<<10,y<<10} normalized.
     * @return A REFERENCE TO A UNIQUE INITIALIZED ARRAY. SO BE CAREFULL
     * @see IT IS MULTIPLIED BY 1024
     * @see DO NOT USE VALUES where (x*x+y*y) are BIGGER THAN 2.000.000
     *      ...returns wrong number
     */
    private static final int[] _normalized_vector = new int[2];
    public static final int[] vectorScale(final int x, final int y, final int NORMAL)
    {
        final int normal = sqrt(x * x + y * y);
        if( normal != 0 )
        {
            int cte = (NORMAL << 10) / normal;
            _normalized_vector[0] = (x * cte);
            _normalized_vector[1] = (y * cte);
        }
        else
        {
            _normalized_vector[0] = 0;
            _normalized_vector[1] = 0;
        }
        return _normalized_vector;
    }
    public static final int[] _orto_vector = new int[2];

    /** @return orthogonal vector with same normal */
    public static final int[] vectorOrtho(int x, int y)
    {
        _orto_vector[0] = Math.abs(y);
        _orto_vector[1] = -x * (y > 0 ? 1 : -1);
        return _orto_vector;
    }

    /***
     * 
     * @param TIME_TO_ZERO: some strange value that creates the quadratic
     * @param TIME_INTERVAL
     */
    private static int height;
    private static int rate_div;

    public static final void quadraticSet(final int TIME_INTERVAL, final int HEIGHT)
    {
        height = HEIGHT;
        rate_div = TIME_INTERVAL;
    }

    /***
     * Describes a quadratic function (y = x^2)
     * 
     * @param time counter
     * @return an always positive quadratic function.
     */
    public static final int quadratic(final int time)
    {
        return height - ((height << 2) * time / rate_div) * time / rate_div;
    }


    // ------------------------------------ STRING UTILS ------------------------------------
    /**
     *  Replaces a string with another string. Returns the final processed string
     *
     */
    public static String replace(String _text, String _searchStr, String _replacementStr)
    {
        // String buffer to store str
        StringBuffer sb = new StringBuffer();

        // Search for search
        int pos = _text.indexOf(_searchStr);

        // Iterate to add string
        while (pos != -1)
        {
            sb.append(_text.substring(0, pos)).append(_replacementStr);

            _text = _text.substring(pos + _searchStr.length());
            pos = _text.indexOf(_searchStr);
        }

        // Create string
        sb.append(_text);

        return sb.toString();
    }


    // ----------------------------------- DEPRECATED --------------------------------------
    private static Random m_random = new Random();
    private static StringBuffer m_auxStringBuffer = new StringBuffer();
    private static StringBuffer m_auxStringBuffer2 = new StringBuffer();

    /**
     * Returns a random number inside the range min, max
     *
     * Deprecated function, available for backwards compatibility
     *
     * @deprecated
     *
     * @param min
     * @param max
     *
     * @return
     */
    public static final int getRandomNumber(int min, int max)
    {
        int r = Math.abs(m_random.nextInt() % ((max - min) + 1));
        return min + r;
    }

    /**
     * Deprecated function, available for backwards compatibility
     *
     * @deprecated
     *
     * @param keyCode
     * @param gameAction
     * 
     * @return
     *
     */
    public static final boolean isLeftSoftkeyPressed(int keyCode, int gameAction)
    {
    	return (gameAction == MXMEConstants.LEFT_SOFTKEY_ACTION_1
                && keyCode == MXMEConstants.LEFT_SOFTKEY_KEY_1)
                || ((gameAction == MXMEConstants.LEFT_SOFTKEY_ACTION_2_1
                        || gameAction == MXMEConstants.LEFT_SOFTKEY_ACTION_2_2)
                        && keyCode == MXMEConstants.LEFT_SOFTKEY_KEY_2)
                        || ((gameAction
                                == MXMEConstants.LEFT_SOFTKEY_ACTION_3_1
                                || gameAction ==
                                	MXMEConstants.LEFT_SOFTKEY_ACTION_3_2)
                                && keyCode
                                == MXMEConstants.LEFT_SOFTKEY_KEY_3)
                                || (gameAction
                                        == MXMEConstants.LEFT_SOFTKEY_ACTION_4
                                        && keyCode
                                        == MXMEConstants.LEFT_SOFTKEY_KEY_4)
                                        || (gameAction
                                                == MXMEConstants.LEFT_SOFTKEY_ACTION_5
                                                && keyCode
                                                == MXMEConstants.LEFT_SOFTKEY_KEY_5)
                                                || (gameAction
                                                == MXMEConstants.LEFT_SOFTKEY_ACTION_6
                                                && keyCode
                                                == MXMEConstants.LEFT_SOFTKEY_KEY_6)
                                                || (keyCode
                                                == MXMEConstants.LEFT_SOFTKEY_KEY_7);
    }

    /**
     * Deprecated function, available for backwards compatibility
     *
     * @deprecated
     *
     * @param keyCode
     * @param gameAction
     * 
     * @return
     */
    public static final boolean isRightSoftkeyPressed(int keyCode, int gameAction)
    {
    	return (gameAction == MXMEConstants.RIGHT_SOFTKEY_ACTION_1
                && keyCode == MXMEConstants.RIGHT_SOFTKEY_KEY_1)
                || ((gameAction == MXMEConstants.RIGHT_SOFTKEY_ACTION_2_1
                        || gameAction == MXMEConstants.RIGHT_SOFTKEY_ACTION_2_2)
                        && keyCode == MXMEConstants.RIGHT_SOFTKEY_KEY_2)
                        || ((gameAction
                                == MXMEConstants.RIGHT_SOFTKEY_ACTION_3_1
                                || gameAction ==
                                	MXMEConstants.RIGHT_SOFTKEY_ACTION_3_2)
                                && keyCode
                                == MXMEConstants.RIGHT_SOFTKEY_KEY_3)
                                || (gameAction
                                        == MXMEConstants.RIGHT_SOFTKEY_ACTION_4
                                        && keyCode
                                        == MXMEConstants.RIGHT_SOFTKEY_KEY_4)
                                        || (gameAction
                                                == MXMEConstants.RIGHT_SOFTKEY_ACTION_5
                                                && keyCode
                                                == MXMEConstants.RIGHT_SOFTKEY_KEY_5)
                                                || (gameAction
                                                == MXMEConstants.RIGHT_SOFTKEY_ACTION_6
                                                && keyCode
                                                == MXMEConstants.RIGHT_SOFTKEY_KEY_6)
                                                || (keyCode
                                                == MXMEConstants.RIGHT_SOFTKEY_KEY_7);
    }

}
