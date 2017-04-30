/*
 * Name: Mahmoud Enani
 * Date: 04/27/2017
 * Language: Java 
 * Version: 8
 * Includes: background.jpg, browseBg.png, placeholder1.png, placeholder1.jpg, pointerSmall.png, dragon.jpg, troll.jpg, write.mp3, Cursor.wav, Intro.wav, catalog.txt
 * Class: COP2552.0M1
 * Assignment: Final Project
 */

/*
 * This program is a GUI for a catalog of Fantastic Beasts from the Harry Potter Universe 
 */

package fantasticGUI;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.swing.GroupLayout.Alignment;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FantasticGUI extends Application{
	
	Media cursorFile = new Media(new File("bin/sound/Cursor.wav").toURI().toString());
	Media introFile = new Media(new File("bin/sound/Intro.wav").toURI().toString());
	Media writeFile = new Media(new File("bin/sound/write.mp3").toURI().toString());
	
	MediaPlayer cursor = new MediaPlayer(cursorFile);
	MediaPlayer intro = new MediaPlayer(introFile);
	MediaPlayer write = new MediaPlayer(writeFile);
	
	static PathTransition ptBrowse = new PathTransition();
	static PathTransition ptEntry = new PathTransition();	
	static PathTransition ptMainScreen = new PathTransition();
	static PathTransition ptNewt = new PathTransition();
	
	static FantasticBeastsCatalog myBeasts; 
	
	static Image image;
	static Image imageShadow;
	static Image selectedImage;
	static Image newt;
	
	static ImageView bgImage = new ImageView(image);
	static ImageView bgImageShadow = new ImageView(image); 
	static ImageView displayImage = new ImageView(selectedImage); 
	static ImageView displayNewt = new ImageView(newt);
	
	static boolean homeScreen = true; // used to determine enter button function
	static boolean newSearch = true; // used to determine if txtSearch properties need to change
	static boolean newFood = true;
	static boolean newBeast = true;
	static boolean editMode = false;
	static boolean browseScreen = false;
	
	static String[] results;
	static String[] name;
	static String[] food;
	static String oldImagePath;
	static String imagePath = "image/placeholder.jpg" ;
	
	static{
		try {
			myBeasts = new FantasticBeastsCatalog();
		}
		catch (IOException e) {
			System.out.print("Error creating catalog");
		}
	}
	
	private int optionSelected = 1; // (used to determine user menu selection when user hits the enter key)  
	private GridPane pane = new GridPane(); // main pane 
	
	public void start(Stage primaryStage) {
				
		// pane(s)
		final ScrollPane sp = new ScrollPane();
			
		// scene(s)
		final Scene scene = new Scene(pane);
		
		// alert(s)
		// final Alert alert = new Alert(AlertType.CONFIRMATION); 
		final Alert success = new Alert(AlertType.INFORMATION);
		
		// boxes
		final VBox bxMenu = new VBox(5); // box to store menu options
		final VBox imageContainer = new VBox(5); // box to store image 
		final VBox bxMessage = new VBox(20); // box to store search field
		final VBox bxEntryName = new VBox(5); // box to store search field
		final VBox bxEntryFood = new VBox(5); // box to store search field
		final HBox bxSearch = new HBox(5); // box to store search field
		
		// button(s)
		final Button btBrowse = new Button("BROWSE");
		final Button btBack = new Button("Back");
		final Button btEntry = new Button("NEW ENTRY");
		final Button btSubmitEntry = new Button("Submit");
		final Button btSearch = new Button("Search");
		final Button btAddImage = new Button("Add Image");
		final Button btReset = new Button("Reset");
		final Button btEdit = new Button("Edit");
		final Button btMainMenu = new Button("Main Menu");
		
		// group(s)
		Group root = new Group();
		
		// path(s)
		Path browsePath = new Path();
		Path entryPath = new Path();
		Path mainScreenPath = new Path();
		Path newtPath = new Path();
		
		// image(s)
		Image pointer = new Image ("image/pointerSmall.png");
		selectedImage = new Image ("image/placeholder1.png", 150, 150, false, false); // image of selected beast (temporary set to placeholder image) 
		image = new Image("image/background.jpg", 1024, 691.5, false, false); 
		imageShadow = new Image("image/shadow.png", 1024, 691.5, false, false); 
		newt = new Image("image/newtBrowse.png");
		
		final ImageView pointerImage = new ImageView(pointer); 	
		bgImage = new ImageView(image);		
		bgImageShadow = new ImageView(imageShadow);
		displayNewt = new ImageView(newt);
		
		bgImageShadow.setStyle("-fx-opacity: 0.5;" );
		
		// path attributes
		browsePath.getElements().add(new MoveTo(63, 20));
		browsePath.getElements().add(new LineTo(63, 30));
	
		entryPath.getElements().add(new MoveTo(63, 20));
		entryPath.getElements().add(new LineTo(63, 30));
		
		ptBrowse.setDuration(Duration.millis(500));
		ptBrowse.setPath(browsePath);
		ptBrowse.setNode(btBrowse);
		ptBrowse.setCycleCount(Timeline.INDEFINITE);
		ptBrowse.setAutoReverse(true);
		
		ptEntry.setDuration(Duration.millis(500));
		ptEntry.setPath(entryPath);
		ptEntry.setNode(btEntry);
		ptEntry.setCycleCount(Timeline.INDEFINITE);
		ptEntry.setAutoReverse(true);
		
		
		mainScreenPath.getElements().add(new MoveTo(510, 345));
		mainScreenPath.getElements().add(new LineTo(515, 345));

		newtPath.getElements().add(new MoveTo(105, 110));
		newtPath.getElements().add(new LineTo(105, 115));		
		
		ptNewt.setDuration(Duration.millis(1000));
		ptNewt.setPath(newtPath);
		ptNewt.setNode(displayNewt);
		ptNewt.setCycleCount(Timeline.INDEFINITE);
		ptNewt.setAutoReverse(true);
		
		ptMainScreen.setDuration(Duration.millis(1000));
		ptMainScreen.setPath(mainScreenPath);
		ptMainScreen.setNode(bgImageShadow);
		ptMainScreen.setCycleCount(Timeline.INDEFINITE);
		ptMainScreen.setAutoReverse(true);
		
		
		
		// labels
		Label lblSearch = new Label("SEARCH:");
		Label lblEntryName = new Label("NAME:");
		Label lblFoodName = new Label("FOOD:");
		Label lblMessageHeader = new Label ("Newt:");
		Label lblMessage = new Label();
		
		// input(s)
		final TextField txtSearch = new TextField();
		final TextField txtEntryName = new TextField(); 	
		final TextField txtFoodName = new TextField(); 
		final TextField txtEditName = new TextField(); 	
		final TextField txtEditFood = new TextField(); 	
		
		// input attributes
		txtSearch.setPrefColumnCount(20);
		txtSearch.setPrefHeight(40);
		txtSearch.setMaxWidth(216);
		txtSearch.setFocusTraversable(false);
		txtSearch.setStyle("-fx-font-size: 15px;" + 
				"-fx-text-fill: #a9a9a9;");
		txtSearch.setText("Enter the name of a creature...");
		
		txtEntryName.setPrefColumnCount(20);
		txtEntryName.setPrefHeight(40);
		txtEntryName.setMaxWidth(216);
		txtEntryName.setFocusTraversable(false);
		txtEntryName.setStyle("-fx-font-size: 15px;" + 
				"-fx-text-fill: #a9a9a9;");
		txtEntryName.setText("Creature's name...");
		
		txtFoodName.setPrefColumnCount(20);
		txtFoodName.setPrefHeight(40);
		txtFoodName.setMaxWidth(216);
		txtFoodName.setFocusTraversable(false);
		txtFoodName.setStyle("-fx-font-size: 15px;" + 
				"-fx-text-fill: #a9a9a9;");
		txtFoodName.setText("Food it eats...");
		
		txtEditName.setPrefColumnCount(20);
		txtEditName.setPrefHeight(40);
		txtEditName.setMaxWidth(216);
		txtEditName.setFocusTraversable(false);
		txtEditName.setStyle("-fx-font-size: 15px;");
		
		txtEditFood.setPrefColumnCount(20);
		txtEditFood.setPrefHeight(40);
		txtEditFood.setMaxWidth(216);
		txtEditFood.setFocusTraversable(false);
		txtEditFood.setStyle("-fx-font-size: 15px;");
		
		// label attributes
		lblSearch.setStyle("-fx-font-weight: bold; "
				+ "-fx-font-size: 16px;"
				+ "-fx-text-fill: #ffffff;");
		
		lblEntryName.setStyle("-fx-font-weight: bold; "
				+ "-fx-font-size: 16px;"
				+ "-fx-text-fill: #ffffff;");
		
		lblFoodName.setStyle("-fx-font-weight: bold; "
				+ "-fx-font-size: 16px;"
				+ "-fx-text-fill: #ffffff;");
		
		lblMessageHeader.setStyle("-fx-font-weight: bold; "
				+ "-fx-font-size: 16px;");
		
		lblMessageHeader.setWrapText(true);
		lblMessage.setWrapText(true);
		lblMessage.setMaxWidth(300);
		lblMessage.setStyle("-fx-font-size: 22px;" +
				 "-fx-font-family: 'Brush Script MT';" + 
				 "-fx-text-fill: #0F4C5C;");
		
		// box attributes
		bxSearch.setPadding(new Insets(15,0,15,15));
		bxEntryName.setPadding(new Insets(15,0,15,15));
		bxEntryFood.setPadding(new Insets(15,0,15,15));
		bxMenu.setPadding(new Insets(15,0,15,15));
		bxMessage.setPadding(new Insets(15,0,15,15));
		
		bxMessage.setMaxHeight(210);
		bxMessage.setMaxWidth(550);
		bxMessage.setStyle("-fx-background-color: #ffffff;" +
				"-fx-opacity: 0.85;" +
				"-fx-background-radius: 5,5,5,5;" +
				"-fx-background-radius: 5,5,5,5;" +
				"-fx-border-radius: 5,5,5,5;" +
				"-fx-font-smoothing-type: lcd;");
		
//		bxMenu.getChildren().add(lblResults);
		bxMessage.getChildren().addAll(lblMessageHeader, lblMessage);
		
		bxMenu.setMaxHeight(200);
		bxMenu.setMaxWidth(400);
		bxMenu.setStyle("-fx-background-color: #ffffff;" +
				"-fx-opacity: 0.90;" +
				"-fx-background-radius: 5,5,5,5;" +
				"-fx-background-radius: 5,5,5,5;" +
				"-fx-border-radius: 5,5,5,5;" +
				"-fx-font-smoothing-type: lcd;");
		
		// remove on focus highlighting (will cause text to be blurry otherwise)
		sp.setStyle("-fx-focus-color: transparent;" +
				"-fx-faint-focus-color:transparent;"+
				"-fx-background-insets: 0, 1");
		
		bxSearch.setMaxHeight(30);
		bxSearch.setMaxWidth(400);
		bxSearch.setStyle("-fx-background-color: #30515f;" +
				"-fx-opacity: 0.90;" +
				"-fx-background-radius: 5,5,5,5;");
		
		
		bxSearch.getChildren().add(lblSearch);
		
		bxEntryName.setPrefHeight(50);
		bxEntryName.setMaxHeight(50);
		bxEntryName.setMaxWidth(510);
		bxEntryName.setStyle("-fx-background-color: #30515f;" +
				"-fx-opacity: 0.90;" +
				"-fx-background-radius: 5,5,5,5;");
		
		bxEntryName.getChildren().add(lblEntryName);
		
		bxEntryFood.setPrefHeight(50);
		bxEntryFood.setMaxHeight(50);
		bxEntryFood.setMaxWidth(510);
		bxEntryFood.setStyle("-fx-background-color: #30515f;" +
				"-fx-opacity: 0.90;" +
				"-fx-background-radius: 5,5,5,5;");
		
	
		bxEntryFood.getChildren().add(lblFoodName);

		// button attributes
		btBrowse.setMaxHeight(50);
		btBrowse.setMaxWidth(150);
		btBrowse.setAlignment(Pos.BASELINE_CENTER);
		btBrowse.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 18px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btBack.setMaxHeight(50);
		btBack.setMaxWidth(150);
		btBack.setAlignment(Pos.BASELINE_RIGHT);
		btBack.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 18px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btEntry.setMaxHeight(50);
		btEntry.setMaxWidth(150);
		btEntry.setAlignment(Pos.BASELINE_CENTER);
		btEntry.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 18px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btSubmitEntry.setMaxHeight(50);
		btSubmitEntry.setMaxWidth(150);
		btSubmitEntry.setAlignment(Pos.BASELINE_RIGHT);
		btSubmitEntry.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 20px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btSearch.setMaxHeight(50);
		btSearch.setMaxWidth(150);
		btSearch.setAlignment(Pos.BASELINE_RIGHT);
		btSearch.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 20px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btAddImage.setMaxHeight(50);
		btAddImage.setMaxWidth(150);
		btAddImage.setAlignment(Pos.BASELINE_RIGHT);
		btAddImage.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 20px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btReset.setMaxHeight(50);
		btReset.setMaxWidth(150);
		btReset.setAlignment(Pos.BASELINE_RIGHT);
		btReset.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 20px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		btEdit.setMaxHeight(50);
		btEdit.setMaxWidth(150);
		btEdit.setAlignment(Pos.BASELINE_RIGHT);
		btEdit.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 20px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #8c8585;");
		
		btMainMenu.setMaxHeight(50);
		btMainMenu.setMaxWidth(150);
		btMainMenu.setAlignment(Pos.BASELINE_RIGHT);
		btMainMenu.setStyle("-fx-background-color: transparent; " +
				"-fx-font-size: 20px;" +
				"-fx-font-family: 'Palatino Linotype';" + 
				"-fx-text-fill: #eff1f3;");
		
		// group attributes
		sp.setPannable(true);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setCache(false);

		sp.setPrefSize(375, 250);
		sp.setContent(bxMenu);
		
		// path attributes
		root.getChildren().add(sp);
		
		root.setBlendMode(BlendMode.MULTIPLY);
		
		
		root.setStyle("-fx-background-color: #9FA4A9; "
				+ "-fx-background-radius: 5,5,5,5;"
				+ "-fx-box-border: transparent;"
				+ "-fx-border-radius: 5,5,5,5;");

		// alert attributes
		success.setTitle("Image Added");

		// image attributes
		imageContainer.setStyle("-fx-background-color: #ffffff;" +
				"-fx-background-radius: 5,5,5,5;");
		
		imageContainer.setBlendMode(BlendMode.MULTIPLY);
		

	    imageContainer.getChildren().add(displayImage);
	    imageContainer.setAlignment(Pos.CENTER);
	    
	    imageContainer.setMaxHeight(200);
	    imageContainer.setMaxWidth(200);
		
	    // pane attributes	
		setPane(bgImage, bgImageShadow, pointerImage, btBrowse, btEntry);
		
		// scene attributes
		primaryStage.setTitle("FantasticGUI (Prototype)");
		primaryStage.setScene(scene);
		primaryStage.setMaxHeight(691.5);
		primaryStage.setMaxWidth(1024);
		primaryStage.setMinHeight(691.5);
		primaryStage.setMinWidth(1024);
		
	    	
		FadeTransition ftShadow = new FadeTransition(Duration.millis(10),bgImageShadow);
		ftShadow.setFromValue(0.0);
		ftShadow.setToValue(1.0);
		ftShadow.play();	
		
		
		FadeTransition btTransition = new FadeTransition(Duration.millis(700), pointerImage);
		btTransition.setFromValue(0.10);
		btTransition.setToValue(1.0);
		btTransition.setCycleCount(Timeline.INDEFINITE);
		btTransition.setAutoReverse(true);
		btTransition.play();	
				
				
		primaryStage.show();
		intro.play();
		ptMainScreen.play();
		ptNewt.play();
				
		// event handling
		
		//change cursor to hand on hover over button
		btBrowse.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(490,0,0,410));
			cursor.stop();
			cursor.play();

		});
		
		btEntry.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(550,0,0,410));
			cursor.stop();
			cursor.play();
		});
		
		
		btSearch.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(-355,0,0,135));
			cursor.stop();
			cursor.play();
		});
		
		
		btReset.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(-285,0,0,145));
			cursor.stop();
			cursor.play();
		});
		
		btEdit.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(-215,0,0,156));
			cursor.stop();
			cursor.play();
		});

		btMainMenu.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(-145,0,0,88));
			cursor.stop();
			cursor.play();
		});
		

		btBack.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		btAddImage.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		btEdit.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		btSubmitEntry.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		
		//change cursor to point on exit hover over button
		btBrowse.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btAddImage.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btBack.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));		
		btEntry.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btSearch.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));		
		btReset.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btEdit.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btMainMenu.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btSubmitEntry.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));

		
		// add image button event - gives user option to upload image and appends path to creature in catalog 
		btAddImage.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				FileChooser fileChooser = new FileChooser();
	            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png or *.jpg)", "*.png", "*.jpg");
	            
	            fileChooser.getExtensionFilters().add(extFilter);
				fileChooser.setTitle("Upload Image");
				File userImage = fileChooser.showOpenDialog(primaryStage);		
				
				String tempName;
	
				if (userImage != null){
					try {
						java.nio.file.Files.copy( 
						           userImage.toPath(), 
						           new java.io.File("bin/image/" + userImage.getName()).toPath(),
						           java.nio.file.StandardCopyOption.REPLACE_EXISTING,
						           java.nio.file.StandardCopyOption.COPY_ATTRIBUTES,
						           java.nio.file.LinkOption.NOFOLLOW_LINKS );
						imagePath = "image/" + userImage.getName();
												
						if(editMode == true){
							tempName = txtEditName.getText();
						}
						else{
							if (txtEntryName.getText().equals(" Creature's name...")){
								tempName = "(Unsaved Entry)";
							}
							else{
								tempName = txtEntryName.getText();
							}
						}
						
						success.setHeaderText(tempName + " successfully updated!");
						success.setContentText("You have successfully added '" + userImage.getName() + "' to " + tempName + 
								". You can change the image by clicking the 'Add Image button again.");
						success.showAndWait();
					} catch (IOException e1) {
						System.out.println("No Image Found!");
					}
				}

			}
		});
		
		
		// browse button event - launches browse page
		btBrowse.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				homeScreen = false;
				browseScreen = true;
				
				btReset.fire();
				
				ftShadow.stop();
				
				pane.getChildren().clear();
				
				image = new Image("image/browseBg.png", 1024, 691.5, false, false);	
				bgImage = new ImageView(image);
				
				imageContainer.getChildren().remove(displayImage);
				
				selectedImage = new Image("image/placeholder.jpg", 214, 214, false, false);
				displayImage = new ImageView(selectedImage);
				
				imageContainer.getChildren().add(displayImage);

				pane.getChildren().addAll(bgImage, pointerImage,  bxMessage, displayNewt, btMainMenu, btEdit, btReset, btSearch,  txtSearch, imageContainer, root);
				GridPane.setMargin(displayNewt, new Insets(-430,0,0,675));
				GridPane.setMargin(bxMessage, new Insets(-365,0,0,340));
				GridPane.setMargin(btMainMenu, new Insets(-145,0,0,90));
				GridPane.setMargin(txtSearch, new Insets(-450,0,0,15));
				GridPane.setMargin(btSearch, new Insets(-355,0,0,90));
				GridPane.setMargin(btReset, new Insets(-285,0,0,90));
				GridPane.setMargin(btEdit, new Insets(-215,0,0,90));
				GridPane.setMargin(pointerImage, new Insets(-355,0,0,135));
				GridPane.setMargin(root, new Insets(275,0,0,285));
				GridPane.setMargin(imageContainer, new Insets(170,0,0,730));
				
				TypewriterAnimation(lblMessage, "Welcome to the Fantastic Catalog, fellow Magizoologist!", 1500);
				
			    PauseTransition pause = new PauseTransition(
			            Duration.seconds(2)
			        );
			    
			    pause.setOnFinished(event -> {
			    	
			    	TypewriterAnimation(lblMessage, "Please use the search option on the left to peruse the catalog.", 1500);
			    });
				
			    pause.play();

			}
		});
		
		// back button event - navigates back to browse page
		btBack.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				pane.getChildren().clear();
				write.stop();
				write.pause();
				
				editMode = false;
				browseScreen = true;
				btBrowse.fire();
				
			}
		});	
		
		// new entry button event
		btEntry.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				homeScreen = false;
				editMode = false;
				browseScreen = false;
								
				clearPane(pointerImage, bgImage, btBrowse, btEntry);
				
				TypewriterAnimation(lblMessage, "You've found a new beast?!", 1500);
				
			    PauseTransition pause = new PauseTransition(
			            Duration.seconds(2)
			        );
			    
			    pause.setOnFinished(event -> {
			    	
			    	TypewriterAnimation(lblMessage, "Great!", 500);
			    });
				
			    pause.play();
			    
			    pause = new PauseTransition(
			            Duration.seconds(4)
			        );
			    
			    pause.setOnFinished(event -> {
			    	TypewriterAnimation(lblMessage, "Please enter the information you have gathered (to enter multiple food types, separate each with a comma):", 1500);
			    });
			    
			    pause.play();
				
				image = new Image("image/browseBg.png", 1024, 691.5, false, false); 				
				bgImage = new ImageView(image);
				
				imageContainer.getChildren().remove(displayImage);
				
				selectedImage = new Image("image/placeholder.jpg", 214, 214, false, false);
				displayImage = new ImageView(selectedImage);
				
				imageContainer.getChildren().add(displayImage);
				
				pane.getChildren().addAll(bgImage, bxMessage, displayNewt, btAddImage, btMainMenu, btSubmitEntry, txtEntryName, txtFoodName, imageContainer,  root);
				GridPane.setMargin(displayNewt, new Insets(-430,0,0,675));
				GridPane.setMargin(btMainMenu, new Insets(-40,0,0,90));
				GridPane.setMargin(bxMessage, new Insets(-365,0,0,340));
				GridPane.setMargin(txtEntryName, new Insets(-450,0,0,15));
				GridPane.setMargin(txtFoodName, new Insets(-350,355,0,15));
				GridPane.setMargin(btAddImage, new Insets(-250,0,0,90));
				GridPane.setMargin(root, new Insets(275,0,0,285));
				GridPane.setMargin(imageContainer, new Insets(170,0,0,730));
				GridPane.setMargin(btSubmitEntry, new Insets(-180,0,0,90));
				
			}
		});
		
		btEdit.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
						
				if (editMode == true){
					homeScreen = false;
					browseScreen = false;
								
					name = results[0].trim().split(":");  // store name results of entry user would like to edit 
					food = results[1].trim().split(":");  // store food results of entry user would like to edit
					oldImagePath = results[2].trim();
					
					imagePath = oldImagePath; // in case user decides not to choose a new image
					
					
					btReset.fire(); // reset the browse screen
					
					pane.getChildren().clear();
					
					TypewriterAnimation(lblMessage, "So you've found additional information on this creature?", 1500);
					
				    PauseTransition pause = new PauseTransition(
				            Duration.seconds(2)
				        );
				    
				    pause.setOnFinished(event -> {
				    	
				    	TypewriterAnimation(lblMessage, "What do yo mean there is a mistake in my catalog?!", 500);
				    });
					
				    pause.play();
				    
				    pause = new PauseTransition(
				            Duration.seconds(4)
				        );
				    
				    pause.setOnFinished(event -> {
				    	TypewriterAnimation(lblMessage, "Very well, please make the changes:", 1500);
				    });
				    
				    pause.play();
					
					image = new Image("image/browseBg.png", 1024, 691.5, false, false);
					bgImage = new ImageView(image);
									
					txtEditName.setText(name[1]);
					txtEditFood.setText(food[1]);
					
					pane.getChildren().addAll(bgImage, btAddImage,  bxMessage, displayNewt, btMainMenu, btBack, btSubmitEntry, txtEditName, imageContainer, txtEditFood, root);
					GridPane.setMargin(displayNewt, new Insets(-430,0,0,675));
					GridPane.setMargin(btMainMenu, new Insets(-40,0,0,90));
					GridPane.setMargin(btBack, new Insets(-110,0,0,90));
					GridPane.setMargin(bxMessage, new Insets(-365,0,0,340));
					GridPane.setMargin(txtEditName, new Insets(-450,0,0,15));
					GridPane.setMargin(txtEditFood, new Insets(-350,355,0,15));
					GridPane.setMargin(btSubmitEntry, new Insets(-180,0,0,90));
					GridPane.setMargin(imageContainer, new Insets(170,0,0,730));
					GridPane.setMargin(btAddImage, new Insets(-250,0,0,90));
					GridPane.setMargin(root, new Insets(275,0,0,285));
				}
				else {
					TypewriterAnimation(lblMessage, "You'll have to first locate an entry before you can edit it!", 1500);
				}
				
			}
		});
		
		// main menu button event
		btMainMenu.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				homeScreen = true;
				newSearch = true;
				newFood = true;
				newBeast = true;
				optionSelected = 1;
				
				pane.getChildren().clear();
				
				image = new Image("image/background.jpg", 1024, 691.5, false, false); 
				
				bgImage = new ImageView(image);
				
				setPane(bgImage, bgImageShadow, pointerImage, btBrowse, btEntry);
				
				ftShadow.play();
				ptMainScreen.playFromStart();;

				
			}
		});
		
		// search field on focus event
		txtSearch.focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (newSearch == true){
					txtSearch.setText("");
					txtSearch.setStyle("-fx-text-fill: #000000;" + 
					"-fx-font-size: 15px;");
					newSearch = false;
				}
			}
		});
		
		txtEntryName.focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (newBeast == true){
					txtEntryName.setText("");
					txtEntryName.setStyle("-fx-text-fill: #000000;" + 
					"-fx-font-size: 15px;");
					newBeast = false;
				}
			}
		});
		
		txtFoodName.focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (newFood == true){
					txtFoodName.setText("");
					txtFoodName.setStyle("-fx-text-fill: #000000;" + 
					"-fx-font-size: 15px;");
					newFood = false;
				}
			}
		});
		
		// submit entry button event
		btSubmitEntry.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				
				bxMenu.getChildren().clear();
								
				if (editMode == true){
					try {
						myBeasts.editData(name[1].trim(), food[1].trim(), oldImagePath, txtEditName.getText().trim(), txtEditFood.getText().trim(), imagePath);
					} catch (IOException e) {
						System.out.println("File not found!");
					}
					TypewriterAnimation(lblMessage, "Thank you for your help!", 1500);
					
					
					String tempName = name[1].trim();
					String tempFood = food[1].trim();
					
					Label name = new Label ("Name: " + tempName);
					Label food = new Label ("Food: " + tempFood);
					name.setStyle("-fx-font-size: 15px;" +
									"-fx-font-family: 'Palatino Linotype';");
					food.setStyle("-fx-font-size: 15px;" +
							"-fx-font-family: 'Palatino Linotype';");
					
					VBox.setMargin(name, new Insets(5,5,5,5));
					VBox.setMargin(food, new Insets(5,5,5,5));
					bxMenu.getChildren().addAll(name, food);
					
				}
				else if (editMode == false){
					try {
						String results = myBeasts.enterData(txtEntryName.getText(), txtFoodName.getText(), imagePath);
						
						TypewriterAnimation(lblMessage, results, 1500);	
						
						String tempName = txtEntryName.getText().trim();
						String tempFood = txtFoodName.getText().trim();
						
						Label name = new Label ("Name: " + tempName);
						Label food = new Label ("Food: " + tempFood);
						name.setStyle("-fx-font-size: 15px;" +
										"-fx-font-family: 'Palatino Linotype';");
						food.setStyle("-fx-font-size: 15px;" +
								"-fx-font-family: 'Palatino Linotype';");
						
						VBox.setMargin(name, new Insets(5,5,5,5));
						VBox.setMargin(food, new Insets(5,5,5,5));
						bxMenu.getChildren().addAll(name, food);				
						
						txtEntryName.setStyle("-fx-font-size: 15px;" + 
								"-fx-text-fill: #a9a9a9;");
						txtEntryName.setText("Creature's name...");
						txtFoodName.setStyle("-fx-font-size: 15px;" + 
								"-fx-text-fill: #a9a9a9;");
						txtFoodName.setText("Food it eats...");
						newBeast = true;
						newFood = true;
					
					} catch (IOException e1) {
						System.out.println("Not Found");
					}
			
				}
				
				imageContainer.getChildren().remove(displayImage);
				
				selectedImage = new Image(imagePath, 214, 214, false, false);
				displayImage = new ImageView(selectedImage);
				
				imageContainer.getChildren().add(displayImage);
			}
			
		});
		
		// search button event
		btSearch.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				
				bxMenu.getChildren().clear();
				
				try {
					results = myBeasts.retrieveData(txtSearch.getText().trim().replaceAll("\\s+",""));
					Label[] options = new Label[results.length];
										
					// create and add labels to menu box
					int i = 0;
					
					// display name and food on separate lines
					if (results.length == 1 && !results[0].equals("No results found!")){
						String[] tempResults = new String [3];
						for (String result: results[0].split(";")) {
							tempResults[i] = result;
							i++;
						}
						results = tempResults;
						options = new Label[results.length - 1]; // -1 to remove image location information from being displayed
									
						btEdit.setStyle("-fx-background-color: transparent; " +
								"-fx-font-size: 20px;" +
								"-fx-font-family: 'Palatino Linotype';" + 
								"-fx-text-fill: #eff1f3;");
						
						editMode = true;
						
						imageContainer.getChildren().remove(displayImage);
						
						selectedImage = new Image (results[2], 150, 150, false, false);
						displayImage = new ImageView(selectedImage);
						
						imageContainer.getChildren().add(displayImage);
						
						String[] tempName = results[0].split(":"); 
						TypewriterAnimation(lblMessage, "Ah yes, the " + tempName[1].trim() + ". Truly a magnificent creature. Wouldn't you agree?" , 1500);
						
					}
					else if (results.length > 1) {
						TypewriterAnimation(lblMessage, "It appears that we have multiple entries that match your query.", 1500);
						
					    PauseTransition pause = new PauseTransition(
					            Duration.seconds(2)
					        );
					    
					    pause.setOnFinished(next -> {
					    	
					    	TypewriterAnimation(lblMessage, "Please type in the name of one of the options into the search box to find out more about that creature", 1500);
					    });
					    
					    pause.play();
	
					}
					
					else if(results[0].equals("No results found!")){
						TypewriterAnimation(lblMessage, "Unfortunately we don't have any entries that match your query.", 1500);
					}
					
					i = 0;
					for (Label option: options){
						option = new Label (results[i]);
						option.setStyle("-fx-font-size: 15px;" +
										"-fx-font-family: 'Palatino Linotype';");
						VBox.setMargin(option, new Insets(5,5,5,5));
						bxMenu.getChildren().add(option);
						i++;
					}
					
				
				} catch (IOException e1) {
					System.out.println("Not Found");
				}
			}
			
		});
		
		// reset button event
		btReset.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				bxMenu.getChildren().clear();
				
				imageContainer.getChildren().remove(displayImage);
				selectedImage = new Image("image/placeholder1.png", 150, 150, false, false);
				displayImage = new ImageView(selectedImage);
				imageContainer.getChildren().add(displayImage);
				
				txtSearch.setStyle("-fx-font-size: 15px;" + 
						"-fx-text-fill: #a9a9a9;");
				txtSearch.setText("Enter the name of a creature...");
				
				btEdit.setStyle("-fx-background-color: transparent; " +
						"-fx-font-size: 20px;" +
						"-fx-font-family: 'Palatino Linotype';" + 
						"-fx-text-fill: #8c8585;");
				
				newSearch = true;
			}
		});
				
		
		// moves the pointer between menu options
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent event){
				
				Bounds bound = pointerImage.localToScene(pointerImage.getBoundsInLocal());

				
				if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.DOWN){
					
					if (optionSelected != 2 || optionSelected != 3){
						cursor.stop();
						cursor.play();
					}
					
					if (homeScreen == true){
						GridPane.setMargin(pointerImage, new Insets(550,0,0,410));
											
						optionSelected = 2;
					}
					else if (browseScreen == true){
						
						bound = pointerImage.localToScene(pointerImage.getBoundsInLocal());
						
						if (bound.getMinX() == 135.0){
							GridPane.setMargin(pointerImage, new Insets(-285,0,0,145));
							optionSelected = 2;
						}
						else if (bound.getMinX() == 145.0){
							GridPane.setMargin(pointerImage, new Insets(-215,0,0,156));
							optionSelected = 3;
						}
						else if (bound.getMinX() == 156.0){
							GridPane.setMargin(pointerImage, new Insets(-145,0,0,88));
							optionSelected = 4;
						}
					}				
					
				}
				else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.UP){
										
					if (optionSelected != 1){
						cursor.stop();
						cursor.play();
					}
					
					if (homeScreen == true){
						GridPane.setMargin(pointerImage, new Insets(490,0,0,410));	
						optionSelected = 1;
					}
					else if (browseScreen == true) {
						
						bound = pointerImage.localToScene(pointerImage.getBoundsInLocal());
						
						if (bound.getMinX() == 145.0){
							GridPane.setMargin(pointerImage, new Insets(-355,0,0,135));
							optionSelected = 1;
						}
						else if (bound.getMinX() == 156.0){
							GridPane.setMargin(pointerImage, new Insets(-285,0,0,145));
							optionSelected = 2;
						}
						else if(bound.getMinX() == 88.0){
							GridPane.setMargin(pointerImage, new Insets(-215,0,0,156));
							optionSelected = 3;
						}
					}			
					
				}
				else if (homeScreen == true && event.getCode() == KeyCode.ENTER){
					if (optionSelected == 1) {
						btBrowse.fire();
					}
					else if (optionSelected == 2) {
						btEntry.fire();
					}
				}
				else if (homeScreen == false && event.getCode() == KeyCode.ENTER && browseScreen == true){
					if (optionSelected == 1) {
						btSearch.fire();
					}
					else if (optionSelected == 2) {
						btReset.fire();
					}
					else if (optionSelected == 3) {
						btEdit.fire();
					}
					else if (optionSelected == 4) {
						btMainMenu.fire();
					}
				}
			}
		});
					
	} 
	
	public void setPane(ImageView bgImage, ImageView bgImageShadow, ImageView pointerImage, Button btBrowse, Button btEntry){
			
		pane.getChildren().add(bgImage);
		pane.getChildren().add(bgImageShadow);
		pane.getChildren().add(pointerImage);
		pane.getChildren().add(btBrowse);
		pane.getChildren().add(btEntry);
		GridPane.setMargin(pointerImage, new Insets(490,0,0,410));
		GridPane.setMargin(btBrowse, new Insets(490,0,0,440));
		GridPane.setMargin(btEntry, new Insets(550,0,0, 440));
	
	}
	
	// clears homescreen elements in preparation for new screen
	public void clearPane(ImageView pointerImage, ImageView bgImage, Button btBrowse, Button btEntry){
		pane.getChildren().remove(pointerImage);
		pane.getChildren().remove(bgImage);
		pane.getChildren().remove(btBrowse);
		pane.getChildren().remove(btEntry);
	}
	
	// creates a typewriter like animation on text
	public void TypewriterAnimation(Label lbl, String message, int duration){
		final Animation text = new Transition() {
			
			{
				setCycleDuration(Duration.millis(duration));
			}

			protected void interpolate(double frac) {
				final int length = message.length();
				final int n = Math.round(length * (float) frac);
				lbl.setText(message.substring(0, n));	
			}
		};
		text.play();
		write.stop();
		write.play();
		
	}

	public static void main(String[] args) { 
		Application.launch(args); 
	
	} 
}
