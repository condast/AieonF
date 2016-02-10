package org.aieonf.concept.util;

import java.awt.*;

public class SplashScreen extends Frame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7284686161429012130L;
	
	public static final String S_STARTING_APPLICATION =
		"The application is starting.\n Please wait a moment";
	
	static Image   splashImage;
	static boolean imageLoaded = false;
	
	private boolean applicationLoaded;
	private String loaderText;
	private int progress;
	private String infoText;
	
	/**
	 * Create the splash screen
	 */
	public SplashScreen()
	{
		this.applicationLoaded = false;		
		this.progress = 0;
		this.loaderText = S_STARTING_APPLICATION;
	}
	
	void initFrame()
	{
		this.setLayout( new BorderLayout() );
		//this.progressBar.add( this.progressBar );
	}
	
	/**
	 * Get the application loaded bit
	 */
	public boolean getApplicationLoaded()
	{
		return this.applicationLoaded;
	}
	
	/**
	 * Set the application loaded bit
	 * @param applicationLoaded
	 */
	public void setApplicationLoaded( boolean applicationLoaded )
	{
		this.applicationLoaded = applicationLoaded;
	}

	/**
	 * Get the loader text
	 * @return
	 */
	public String getLoaderText()
	{
		return this.loaderText;
	}

	/**
	 * Get the progress
	 * @return
	 */
	public int getProgress()
	{
		return this.progress;
	}

	/**
	 * Set the loader text
	 * @param applicationLoaded
	 */
	public void setLoaderText( int progress, String loaderText )
	{
		this.loaderText = loaderText;
		this.progress = progress;
	}

	/**
	 * Get the info text
	 * @return
	 */
	public String getInfoText()
	{
		return this.infoText;
	}
	
	/**
	 * Set the info text
	 * @param applicationLoaded
	 */
	public void setInfoText( String infoText )
	{
		this.infoText = infoText;
	}
	
	/**
	 *  Positions the window at the centre of the screen, taking into account
	 *  the specified width and height
	 */
	private void positionAtCenter(int width, int height)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width-width)/2,
				(screenSize.height-height)/2,
				width,
				height);
	}	

	/**
	 * Draw  
	*/
	@Override
	public void paint(Graphics g){
		if(imageLoaded){
			g.drawImage(splashImage,0,0,null);
			Font font = new Font( "Verdana", Font.PLAIN, 35 );
			g.setFont( font );
			g.setColor( Color.white );
			g.drawString( this.getTitle(), 20, 60 );
			font = new Font( "Verdana", Font.PLAIN, 12 );
			g.setFont( font );
			g.drawString( this.infoText, 20, 90 );
			font = new Font( "Verdana", Font.PLAIN, 12 );
			g.setFont( font );
			g.drawString( "Loading application " + this.progress + "%", 50, 180 );
			g.drawString( "- " + this.loaderText, 50, 200 );
		}
	}

	@Override
	public void update(Graphics g){
		paint(g);
	}

	static class AsyncImageLoader implements Runnable 
	{
		private String  imageFileName;
		private Thread  loaderThread;
		private boolean stop;
		private SplashScreen  parentFrame;
		
		/**
		 * Load the image
		 * @param parent
		 * @param fileName
		 */
		public AsyncImageLoader( SplashScreen parent, String fileName)
		{
			this.parentFrame = parent;
			this.stop = true;
			this.imageFileName = fileName;
			this.loaderThread  = new Thread(this);
			this.loaderThread.setDaemon( true );
			this.loaderThread.start();
		}
		
		/**
		 * Stop the thread;
		 */
		public void stop()
		{
			this.stop = true;
			this.loaderThread.interrupt();
		}
		
		@Override
		public void run() 
		{
			parentFrame.initFrame();
			splashImage = Toolkit.getDefaultToolkit().createImage(imageFileName);
		            
		  // wait for image to be loaded
		  MediaTracker tracker = new MediaTracker(parentFrame);
		  tracker.addImage(splashImage,0);
		  try {
		  	tracker.waitForID(0);
		  }
		  catch(InterruptedException e){
		    e.printStackTrace();
		  }
		                    
		  // check to ensure the image loaded okay. It would be nice to give
		  // a more specific error message here, but the Image load/observe
		  // API doesn't give us further details.
		  if(tracker.isErrorID(0)){
		  	System.err.println("splashloader: error loading image \"" +
		  			imageFileName +
		  		"\"");

		  	// 	this isn't a fatal error - the target class should be able
		  	// to load.
		  	return;
		  }
		
		  // resize frame to match size of image, and keep frame at centre of screen
		  parentFrame.positionAtCenter(splashImage.getWidth(null), 
		  		splashImage.getHeight(null));
		  	
		  //signal a redraw, so the image can be displayed
		  imageLoaded = true;
		  int counter = 0;
		  this.stop = false;
		  parentFrame.repaint();
		  while( stop == false ){	
		  	counter++;
				parentFrame.repaint( 10, 150, this.parentFrame.getWidth(), 80 );
				if(( this.parentFrame.getApplicationLoaded() && ( counter > 90 )))
					break;
		  	try{
					Thread.sleep( 100 );
		  	}
		  	catch( InterruptedException ex ){
		  		this.stop = true;
		  	}
		  }
		  this.parentFrame.setVisible( false );
		}
	}	
	
  public static final void load( SplashScreen f, String fileName)
  {
    // create a splash window, in which we'll display an image
    f.setUndecorated(true);
    f.positionAtCenter(1,1);
    f.setVisible( true );
    
    // start loading the image, asynchronously      
    new AsyncImageLoader(f, fileName );
  }
}