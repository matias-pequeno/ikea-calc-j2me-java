package util;

/**************************
 * Text class
 * 
 * Manages all "text" related functionality, including loading of text
 * 
 *
 *
 */

import app.*;
import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
// MatuX Library
import com.mxme.common.*;

public class Text 
{
    ///#startText  // Do not remove, used by TextEncoder
	public static final int str_NULL_STRING = 0;	 // 
	public static final int str_CANTPAREDESHELP_MINI = 1;	 //Ingrese la cantidad de paredes y[...]
	public static final int str_MEASURE_WALL_HELP_MINI = 2;	 //Ingrese las medidas de la pared.[...]
	public static final int str_MEASURE_WINDOW_HELP_MINI = 3;	 //Ingrese las medidas de la ventan[...]
	public static final int str_MEASURE_DOOR_HELP_MINI = 4;	 //Ingrese las medidas de la puerta[...]
	public static final int str_BIG_MEASURE_HELP = 5;	 //bbla blab lab lalba blabl abl ab[...]
	public static final int str_ABOVE_MAX_WALLS = 6;	 //Sólo se permiten entre 1 y 10 pa[...]
	public static final int str_WRITE_SOMETHING = 7;	 //Este campo no debe estar vacío.
	public static final int str_BACK_TO_PAINT_MENU = 8;	 //
	public static final int str_CALCULATOR_TYPE_PINTURA = 9;	 //Pintura
	public static final int str_CALCULATOR_TYPE_CERAMICOS = 10;	 //Pisos Cerámicos
	public static final int str_CALCULATOR_TYPE_LAMINADOS = 11;	 //Pisos Laminados
	public static final int str_CALCULATOR_TYPE_PARED = 12;	 //Pared
	public static final int str_CALCULATOR_TYPE_VENTANA = 13;	 //Ventana
	public static final int str_CALCULATOR_TYPE_PUERTA = 14;	 //Puerta
	public static final int str_CALCULATE = 15;	 //Calcular!
	public static final int str_USER = 16;	 //Usuario
	public static final int str_PASSWORD = 17;	 //Contraseña
	public static final int str_LOGIN_ERROR = 18;	 //Error al conectarse!
	public static final int str_ERROR = 19;	 //Error, vuelva a intentar.
	public static final int str_CONNECTING = 20;	 //Ingresando...
	public static final int str_PASSWORD_ERROR = 21;	 //Usuario o contraseña incorrecta!
	public static final int str_LOGIN_IN = 22;	 //Comprobando contraseña
	public static final int str_PLEASE_WAIT = 23;	 //Por favor, espere...
	public static final int str_EASTER_EGG = 24;	 //Easter Egg!
	public static final int str_PRESS_KEY_TO_CONTINUE = 25;	 //Presione para continuar
	public static final int str_EXIT = 26;	 //Salir
	public static final int str_BIDS = 27;	 //SUBASTAS
	public static final int str_NOVEDADES = 28;	 //NOVEDADES
	public static final int str_INBOX = 29;	 //INBOX
	public static final int str_MYPROFILE = 30;	 //MI PERFIL
	public static final int str_CODELOAD = 31;	 //CLAVES
	public static final int str_SUBASTA = 32;	 //SUBASTA!
	public static final int str_EN_CONSTRUCCION = 33;	 //En construcción!
	public static final int str_LOAD = 34;	 //CARGAR!
	public static final int str_ENSUBASTA = 35;	 //Actualmente en subasta
	public static final int str_ENSUBASTA_COMPACT = 36;	 //En subasta
	public static final int str_USED = 37;	 //Ya usados
	public static final int str_TOTAL = 38;	 //Totales
	public static final int str_AVAILABLE = 39;	 //Disponibles
	public static final int str_CODEUSED = 40;	 //Esta clave ya fue usada, por fav[...]
	public static final int str_CODEINCORRECT = 41;	 //La clave es incorrecta, por favo[...]
	public static final int str_CODEOK = 42;	 //Tu clave fue ingresada! %d punto[...]
	public static final int str_CODEMAXTEN = 43;	 //Superaste el límite de las 10 cl[...]
	public static final int str_CODEUSED_COMPACT = 44;	 //Clave usada, intentá con otra!
	public static final int str_CODEINCORRECT_COMPACT = 45;	 //Clave incorrecta, intentá con ot[...]
	public static final int str_CODEOK_COMPACT = 46;	 //%d puntos se acreditaron en tu c[...]
	public static final int str_CODEMAXTEN_COMPACT = 47;	 //Superaste el límite de las 10 cl[...]
	public static final int str_PASSERROR = 48;	 //Usuario y/o contraseña inválidos[...]
	public static final int str_PASSERROR_COMPACT = 49;	 //Usuario y/o contraseña inválidos
	public static final int str_TERMS = 50;	 //Términos y Condiciones
	public static final int str_NEWS1 = 51;	 //Juank ganó un iPod Nano...
	public static final int str_NEWS2 = 52;	 //El grupo LoVaho ganó un...
	public static final int str_NEWS3 = 53;	 //La camioneta sigue vige...
	public static final int str_NEWS4 = 54;	 //Obtené más puntos carga...
	public static final int str_NEWS5 = 55;	 //Se sumaron 5 premios en...
	public static final int str_NEWS6 = 56;	 //Te quedaste sin puntos?...
	public static final int str_INBOX1 = 57;	 //Juank quiere ser miembr...
	public static final int str_INBOX2 = 58;	 //JuanK: Yo creo que en 2...
	public static final int str_INBOX3 = 59;	 //Superaron tu oferta por...
	public static final int str_INBOX4 = 60;	 //PedroG acepto ser parte...
	public static final int str_INBOX5 = 61;	 //Felicitaciones tu grupo...
	public static final int str_INBOX6 = 62;	 //GuidoCM: Muerto! Que te...
	public static final int str_INBOX7 = 63;	 //En el bosque vive un vi...
	public static final int str_INBOX8 = 64;	 //Yo vivia, yo vivia muy ...
	public static final int str_INBOX9 = 65;	 //Caminaba, caminaba sin ...
	public static final int str_INBOX10 = 66;	 //Pero un día, vino el ho...
	public static final int str_LANG1 = 67;	 //English
	public static final int str_LANG2 = 68;	 //Español
	public static final int str_LANG3 = 69;	 //
	public static final int str_LANG4 = 70;	 //
	public static final int str_LANG5 = 71;	 //
	public static final int str_YES = 72;	 //Si
	public static final int str_NO = 73;	 //No
	public static final int str_OK = 74;	 //OK
	public static final int str_BACK = 75;	 //Atras
	public static final int str_HELP_TEXT = 76;	 //Este es un texto largo. Un texto[...]
	public static final int str_TERMS_TEXT = 77;	 //Consulta los términos y condicio[...]
	public static final int str_DEMO_BID = 78;	 //Caja de fichas originales del Ca[...]

    ///#endText  // Do not remove, used by TextEncoder

    private static int fast_count, last_id;
    private static String last_string;
    private static byte slow_buffer[][];
    private static String text[];
    
    /** 
     * Function to initialize the Text system.
     * 
     * @param int lang, language to be loaded from the text file.
     */
    public static final void init(int lang)
    {
        try
        {
            // Text vars
            int lang_count;
            int slow_count;
            int lang_offset = 0;
            int slow_offset[];

            int i;
            int c;
            int pos;
            byte bs[];
            int bs_len;
            StringBuffer buffer = new StringBuffer();
            InputStream is = buffer.getClass().getResourceAsStream("/t");
            DataInputStream dis = new DataInputStream(is);
            lang_count = dis.readUnsignedByte();

            for( i = 0; i < lang_count; i++ )
            {
                c = dis.readUnsignedShort();
                if( i == lang )
                    lang_offset = c;
            }

            fast_count = dis.readUnsignedByte();
            int fast_lensize = dis.readUnsignedByte();
            slow_count = dis.readUnsignedByte() - 1;
            slow_buffer = new byte[slow_count + 1][];
            slow_offset = new int[slow_count + 1];
            text = new String[fast_count];
            last_id = -1;

            pos = lang_count * 2 + 4;
            dis.skipBytes(lang_offset - pos);
            for( c = 0; c < fast_count; c++ )
            {
                if( fast_lensize == 1 )
                    bs_len = dis.readUnsignedByte();
                else
                    bs_len = dis.readUnsignedShort();
                bs = new byte[bs_len];
                dis.readFully(bs);
                //#if DEV_LG == "true"
                //# LANG[c] = new String(bs, "ISO8859_1");
                //#else
                    text[c] = new String(bs, "ISO-8859-1");
                //#endif
                pos += bs_len + fast_lensize;
            }
            for( c = 0; c < slow_count; c++ )
            {
                slow_offset[c] = dis.readUnsignedShort();
                pos += 2;
            }
            
            for( c = 0; c < slow_count; c++ )
            {
                bs_len = dis.readUnsignedShort();
                slow_buffer[c] = new byte[bs_len];
                dis.readFully(slow_buffer[c]);
            }
        }
        catch( Exception e )
        {
            //#if DEBUG == "true"
//#                 e.printStackTrace();
//#                 System.out.println("lang_init: "+e.getMessage());
            //#endif
        }
    }

    /**
     * This functions returns a text from the LANGZ file. it has 2 modes, fast mode and slow mode. 
     *      Fast Mode: gets a string from an array in memory. 
     *      Slow Mode: gets a string from a text file, and save it into a temporal string cache to prevent a leak when drawing briefings, tips, help, etc.
     *
     * @param int id: String index to get
     * @return the text specified by id
     * 
     */
    public static final String get(int id)
    {
        if( id >= fast_count )
        {
            if( last_id == id )
                return last_string;

            try
            {
                //#if DEV_LG == "true"
                //# last_string = new String(slow_buffer[id - fast_count], "ISO8859_1");
                //#else
                    last_string = new String(slow_buffer[id - fast_count], "ISO-8859-1");
                //#endif
                return last_string;
            }
            catch( Exception e )
            {
                //#if DEBUG == "true"
//#                     e.printStackTrace();
//#                     System.out.println(e.getMessage());
                //#endif
                return "txterror";
            }
        }
        return text[id];
    }

}
