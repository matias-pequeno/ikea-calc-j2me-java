package util;

/**************************
 * AppConfig class
 * 
 * Manages all Record Data used by the application.
 * 
 *  This class is intended to store application configuration.
 *
 */
import java.io.*;
import javax.microedition.rms.*;

import app.Profile;

public class AppConfig 
{
    private final static String RECORD_CLASSID = "lasubastada";
    public static final int
            VALUE_USERNAME_LENGTH = 0,
            VALUE_USERNAME = 1,
            VALUE_COUNT = 2;

    public static final void load_config()
    {
        RecordStore r = null;
        RecordEnumeration records = null;
        try
        {
            r = RecordStore.openRecordStore(RECORD_CLASSID, true);
            records = r.enumerateRecords(null, null, false);
            DataInputStream data_in = new DataInputStream(new ByteArrayInputStream(records.nextRecord()));
            
            int len = data_in.readByte();
            byte[] b = new byte[len];
            data_in.readFully(b, 0, len);
            Profile.userName = new String(b);
        }
        catch( Exception e )
        {
            // create the recordstore
            save_config(true);
        }

        try
        {
            if( records != null )
                records.destroy();
            if( r != null )
                r.closeRecordStore();
        }
        catch( Exception e ) { /* Unable to close recordstore? */ }
    }

    public static final void save_config(boolean add)
    {
        RecordStore r = null;
        try
        {
            r = RecordStore.openRecordStore(RECORD_CLASSID, true);
            ByteArrayOutputStream bytes_out = new ByteArrayOutputStream();
            DataOutputStream data_out = new DataOutputStream(bytes_out);

            data_out.writeByte(Profile.userName.length());
            data_out.write(Profile.userName.getBytes());

            byte[] record = bytes_out.toByteArray();
            if( add )
                r.addRecord(record, 0, record.length);
            else
                r.setRecord(1, record, 0, record.length);
        }
        catch( Exception e ) { }

        try
        {
            if( r != null )
                r.closeRecordStore();
        }
        catch( Exception e ) { /* Unable to close recordstore? */ }
    }

}
