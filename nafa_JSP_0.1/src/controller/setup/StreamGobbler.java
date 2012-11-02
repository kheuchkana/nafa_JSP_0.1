package controller.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Read a stream in a separate thread.
 *
 * @author S. Gamel
 */
public class StreamGobbler extends Thread
{
    /** The stream to read from. */
    private InputStream inputStream;
    
    /** Output grabber */
    private StringBuffer result;
    
    /** Constructor.
     * 
     * @param aStream The stream to read from.
     */
    StreamGobbler(final InputStream aStream)
    {
        inputStream = aStream;
        result = new StringBuffer();
    }
    
    /** Get the final output log.
     */
    public String getResult()
    {
        return result.toString();
    }
    
    /** Read until the end of stream.
     */
    public void run()
    {
        try
        {
            final InputStreamReader reader = new InputStreamReader(inputStream);
            final BufferedReader buffReader = new BufferedReader(reader);
            
            String line=null;
            
            line = buffReader.readLine();
            while ( null != line )
            {
                result.append(line);
                result.append("\n");
                
                line = buffReader.readLine();
            }
            
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }
    }
}