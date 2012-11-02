package controller.setup;


import java.io.IOException;

/** Manage the fork of the command and process its outputs.
 *
 * Rely on the StreamGobbler to capture process outputs.
 *
 * @author S. Gamel
 */
public class CommandRunner extends Thread
{
    /** Internaly created process. */
    private Process process;
    
    /** A thread to capture standard output. */
    private StreamGobbler outputGobbler;
    
    /** A thread to capture standard error. */
    private StreamGobbler errorGobbler;
    
    /** A String representing the command  */
    private String command;
    
    /** A String to retrieve command output  */
    private String resultat;
    
    /** A String to retrieve command error  */
    private String error;

    /** An Integer to retrieve command process return code  */
    private int returnCode;
    
    
    
    public CommandRunner () {
    	
    }
    
    /**
     * 
     * @param aCommand 
     *  The command line specification.
     * @param aWorkDirectory 
     *  The directory to set as execution directory.
     */
    public void CommandRunnerMethod()
    {
        process = null;

        final Runtime runTime = Runtime.getRuntime();

        try
        {
            process = runTime.exec(getCommand(), null, null);
            
            // any error message?
            outputGobbler = new StreamGobbler(process.getErrorStream());
            
            // any output?
            errorGobbler = new StreamGobbler(process.getInputStream());
            
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
            
            //Wait for process to complete
            waitForCompletion();
        }
        catch ( final IOException e )
        {
            System.err.println("ERROR: "+e);
        }
        catch ( final SecurityException e )
        {
            System.err.println("ERROR: "+e);
        }
        catch ( final NullPointerException e )
        {
            System.err.println("ERROR: "+e);
        }
        catch ( final IllegalArgumentException e )
        {
            System.err.println("ERROR: "+e);
        }
    }

    /** Wait for the process to complete.
     */
    public void waitForCompletion()
    {
        if ( null != process )
        {
            try
            {
                final int rc = process.waitFor();
                setReturnCode(rc);
                setError(errorGobbler.getResult());
                setResultat(outputGobbler.getResult());
            }
            catch ( final InterruptedException e )
            {
                System.err.println("ERROR: "+e);
            }
        }
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
    
}
