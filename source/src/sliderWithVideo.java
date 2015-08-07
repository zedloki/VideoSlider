//This Software is made by Zed Yang

package Slider;
import javafx.application.*;
//import javafx.beans.value.*;
//import javafx.collections.*;
import javafx.event.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.*;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;
 
/** Example of playing all audio files in a given directory. */
public class sliderWithVideo extends Application {
 //configuration set up
  private final String CONFIG_FILE_NAME = "config.txt";	
  Hashtable<String, String> configParams;

  //global variables
  private static String File_DIR = null;
  public static final int FILE_EXTENSION_LEN = 3;
  public static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".png", ".jpg", ".mp4", ".m4a");
  public static final List<String> SUPPORTED_IMG_EXTENSIONS = Arrays.asList(".png", ".jpg");
  public static final List<String> SUPPORTED_VID_EXTENSIONS = Arrays.asList(".mp4", ".m4a");
  
  List<String> list = new ArrayList<>();
  
  long DELAY_time = 4; //Delay time in seconds
  List<Image> images = new ArrayList<>();
  List<MediaPlayer> players = new ArrayList<>();
  int playerIndex = 0;
  
  ImageView imageView = new ImageView();
  int count = 0;
  Group root = new Group();
  Scene scene = new Scene(root);
  
  long delay = DELAY_time*1000;
  StackPane layout = new StackPane();
  boolean toggle = true;
  
  boolean test = false;
  
  public static void main(String[] args) throws Exception { launch(args);}
  
  public void start(final Stage stage) throws Exception {
		readConfigParams();
		setConfigParams();
		getMasterList();
		stage.setTitle("Bing Slider");
		decision(stage);
  }
  
  
  //Private Classes
  /** sets the currently playing label to the label of the new media player and updates the progress monitor. */
  private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
    newPlayer.seek(Duration.ZERO);
 
    String source = newPlayer.getMedia().getSource();
    source = source.substring(0, source.length() - FILE_EXTENSION_LEN);
    source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
    source = null;
  }
  
  public void playVideo(Stage stage){
	  	toggle = true;
	  	//Plays first video
	    final MediaView mediaView = new MediaView(players.get(0));
	    
	 // add a metadataTable for meta data display
	    // layout the scene.

	    mediaView.fitWidthProperty().bind(scene.widthProperty());
	    mediaView.fitHeightProperty().bind(scene.heightProperty());
	    //mediaView.autosize();
	    
	    layout.getChildren().clear();
	    layout.getChildren().setAll(mediaView);
	    layout.autosize();
	    
	    root.getChildren().clear();
	    root.getChildren().add(layout);	
	    //scene = new Scene(root);
	   
	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    scene.setCursor(Cursor.NONE);
	    stage.show();
	    
	    
	    // play each audio file in turn.
	    for (int i = 0; i < players.size(); i++) {
	      final MediaPlayer player     = players.get(i);
	      final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
	      player.setOnEndOfMedia(new Runnable() {
	        @Override public void run() {
	          player.stop();
	          if (players.indexOf(nextPlayer) == 0){
	        	  layout.getChildren().clear();
	        	  root.getChildren().clear();
	        	  players.clear();
	        	  player.dispose();
	        	  nextPlayer.dispose();
	        	  decision(stage);
	        	  return;
	          }else{     	  
	        	  mediaView.setMediaPlayer(nextPlayer);
	        	  player.dispose();
	        	  nextPlayer.play();
	          }
	        } 
	      });
	    }
	   
	    // start playing the first track.
	    mediaView.setMediaPlayer(players.get(0));
	    mediaView.getMediaPlayer().play();
	    setCurrentlyPlaying(mediaView.getMediaPlayer());
	 
	// allow the user to skip a track.
	    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
	      @Override public void handle(KeyEvent ke) {
	    	  MediaPlayer nextPlayer;
	    	  MediaPlayer curPlayer = mediaView.getMediaPlayer();
	      	  if (ke.getCode()==KeyCode.SPACE) {
	      		  if (toggle){
	      		  	mediaView.getMediaPlayer().pause();
	      		  	toggle = false;
	      		  }
	      		  else{
	      			  mediaView.getMediaPlayer().play();
	      			  toggle = true;
	      		  }
	      	  }
	      		else if(ke.getCode() == KeyCode.RIGHT){
		        curPlayer.stop();
		        if(players.indexOf(curPlayer)+1 == players.size()){
		        	  layout.getChildren().clear();
		        	  decision(stage);
		        }else{
			        nextPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
			        mediaView.setMediaPlayer(nextPlayer);
			        nextPlayer.play();
		        }
	    	  }
	    	  else if(ke.getCode() == KeyCode.LEFT){
			        curPlayer.stop();
			        if (players.indexOf(curPlayer) == 0){
			        	System.out.println(players.size()-1);
			        	nextPlayer = players.get((players.size()-1));
			        	mediaView.setMediaPlayer(nextPlayer);
			        	nextPlayer.play();
			        }else{
			        	nextPlayer = players.get((players.indexOf(curPlayer) - 1) % players.size());
			        	mediaView.setMediaPlayer(nextPlayer);
			        	nextPlayer.play();
			        }
		    	 }
	      }
	    });
	    //return null;
  }
  
  public void playImg(Stage stage){
	  	toggle = true;
	  	root.getChildren().clear();
	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    scene.setCursor(Cursor.NONE);
	    imageView.fitHeightProperty().bind(scene.heightProperty());
	    imageView.fitWidthProperty().bind(scene.widthProperty());
	    layout.getChildren().clear();
	    layout.getChildren().add(imageView);
	    root.getChildren().add(layout);
       
       stage.show();
       
       count = 0;
	    //Manages the SlideShow
	    Timer task = new Timer();
	    TimerTask slider = new TimerTask() {	
		      @Override
		      public void run() {  
		          if (count == images.size()) {	 
		        	  count = 0;
		        	  task.cancel();
		        	  Platform.runLater(new Runnable() {
		        	       public void run() {
		        	    	   root.getChildren().clear();
		        	    	   decision(stage);
		        	      }
		        	  });
		              return;
		          }else{
		        	  
		          }
		          //testing(test,images.get(count).impl_getUrl());
		          imageView.setImage(images.get(count++));
		      }
	    };		 
	    
	    task.schedule(slider, 0, delay);

	  
             
   /*    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
 	      public void handle(KeyEvent ke) {
 	    	  //System.out.println(img_done);
 	    	  if (ke.getCode() == KeyCode.SPACE){
 	    		  if(toggle){
 	    			  task.cancel();
 	    			  toggle = false;
 	    		  }else{
 	    			 //task.cancel();
 	    			// task = new Timer();
 	    			 toggle = true;
 	    			 //task.schedule(slider, 0, delay);
 	    		  }
 	    	  }
 	    	  else if(ke.getCode() == KeyCode.RIGHT){
 		            if (count >= images.size()) {		              
	        	    	root.getChildren().clear();
	        	    	decision(stage); 
 		            }else{
 		            	imageView.setImage(images.get(count++%images.size()));
 		            }
 		          
 	    	  }else if(ke.getCode() == KeyCode.LEFT){
 	    		 
 	    		  if(count == 0){
 	    			  count = images.size();
 	    			  imageView.setImage(images.get(images.size()-1));
 	    		  }
 	    		  else{
 	    		  	imageView.setImage(images.get(count--%images.size()));
 	    		  	
 	    		  }
 	   
 	    	  }
 	      }
 	    });
     */  
} 
			    	
  /** @return a MediaPlayer for the given source which will report any errors it encounters */
  private MediaPlayer createPlayer(String mediaSource) {
    final Media media = new Media(mediaSource);
    final MediaPlayer player = new MediaPlayer(media);
    player.setOnError(new Runnable() {
      @Override public void run() {
        System.out.println("Media error occurred: " + player.getError());
      }
    });
    return player;
  }
  
  private static Image createImage(String mediaSource) {
	    final Image image = new Image(mediaSource);
	
	    return image;
  }
  
  //Configuration
	private void readConfigParams()
	{
		String line;
		String[] pair = new String[2];
		configParams = new Hashtable<String, String>();

		try{
			BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_NAME));
			while( (line = br.readLine()) != null)
			{
				if(line.trim().length() > 0)
				{
					pair = line.split("=");
					configParams.put(pair[0].trim(), pair[1].trim());
				}
			}
			br.close();
		}catch(IOException ioe) { System.out.println("Configuration file "+CONFIG_FILE_NAME+" not found... "); System.exit(0);}
		
	}

	
	private void setConfigParams()
	{
		File_DIR = (String) configParams.get("File_DIR");
		if(configParams.containsKey("DELAY_time"))
			DELAY_time = Integer.parseInt(configParams.get("DELAY_time"));
		else
			DELAY_time = 3;
		
		if(configParams.containsKey("Test_Mode")){
			if (configParams.get("Test_Mode").equals("true"))
				test = true;
			else 
				test = false;
		}
		else
			test = false;
	}
	
	private void getMasterList(){//gets the file path of all items in the directory
		  List<String> params = getParameters().getRaw();
		  list.clear();
		  images.clear();
		  players.clear();
		  count = 0;
		    final File dir = (params.size() > 0)
		      ? new File(params.get(0))
		      : new File(File_DIR);
		    if (!dir.exists() || !dir.isDirectory()) {
		      System.out.println("Cannot find audio source directory: " + dir + " please supply a directory as a command line argument");
		      Platform.exit();
		      return;
		    }
		    
		    // create some image players.
		    for (String file : dir.list(new FilenameFilter() {
		      @Override public boolean accept(File dir, String name) {
		        for (String ext: SUPPORTED_EXTENSIONS) {
		          if (name.endsWith(ext)) {
		            return true;
		          }
		        }
		 
		        return false;
		      }
		    })) list.add("file:///" + (dir + "\\" + file).replace("\\", "/").replaceAll(" ", "%20"));
		    
		    if (list.isEmpty()) {
		      System.out.println("No audio found in " + dir);
			      Platform.exit();
			      return;
			    }   
	    //algorithm for allowing only unique file paths
		//params.clear();
		Set<String> hs = new HashSet<>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);
		hs.clear();
		params = null;
	    Collections.sort(list);
	}
	
	private void decision(Stage stage){//decision algorithm to determine what to play next	
		//System.out.println(playerIndex);
		
		images.clear();
		players.clear();
		
		int i;
		if(playerIndex == list.size()){ //reset playerIndex when it goes past the array size
			playerIndex = 0;
			list.clear();
			configParams.clear();
			root.getChildren().clear();
			layout.getChildren().clear();
			getMasterList();//updates the player list at the end of every iteration
		}
		
		if (isImg(list.get(playerIndex))){
			images.add(createImage(list.get(playerIndex)));
			for(i = playerIndex+1;i<list.size();i++ ){
				if(isImg(list.get(i))){
					images.add(createImage(list.get(i)));
					playerIndex=i;
				}else{
					break;
				}	
			}
			//testing(test,list.get(playerIndex));
			playerIndex = playerIndex + 1; //add 1 to player index for next iteration
			playImg(stage);
			
		}else if(isVid(list.get(playerIndex))){
			players.add(createPlayer(list.get(playerIndex)));
			for(i = playerIndex+1;i<list.size();i++ ){
				if(isVid(list.get(i))){
					players.add(createPlayer(list.get(i)));
					playerIndex=i;
				}else{
					break;
				}	
			}
			
			playerIndex = playerIndex + 1; //add 1 to player index for next iteration
			playVideo(stage);
		}else{
				
		}
		
			
	}
	public boolean isImg(String filepath){
		for(String ext: SUPPORTED_IMG_EXTENSIONS){	
			if (filepath.endsWith(ext)){	
				return true;
			}
		}
		return false;
	}
	
	public boolean isVid(String filepath){
		for(String ext: SUPPORTED_VID_EXTENSIONS){
			if (filepath.endsWith(ext)){	
				return true;
			}
		}
		return false;
	}
	
	private void testing(boolean select,String args){
		if (select == true){
			System.out.println(args);
			 Thread t = new Thread(new Runnable(){
			        public void run(){
			            JOptionPane.showMessageDialog(null, args);
			        }
			    });
			  t.start();
			
		}else{
			return;
		}
	}

}




