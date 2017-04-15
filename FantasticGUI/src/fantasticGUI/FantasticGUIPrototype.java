/*
 * Name: Mahmoud Enani
 * Date: 04/02/2017
 * Language: Java 
 * Version: 8
 * Includes: background.jpg, placeholder1.png, pointer.png, Cursor.wav, Intro.wav, catalog.txt
 * Class: COP2552.0M1
 * Assignment: Project 3
 */

/*
 * This program is a prototype for a GUI for a catalog of Fantastic Beasts from the Harry Potter Universe 
 */

package fantasticGUI;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Stage;
import javafx.util.Duration;



public class FantasticGUIPrototype extends Application{
	
	Media cursorFile = new Media(new File("bin/sound/Cursor.wav").toURI().toString());
	Media introFile = new Media(new File("bin/sound/Intro.wav").toURI().toString());
	
	MediaPlayer cursor = new MediaPlayer(cursorFile);
	MediaPlayer intro = new MediaPlayer(introFile);
	
	static PathTransition ptBrowse = new PathTransition();
	static PathTransition ptEntry = new PathTransition();
	
	static FantasticBeastsCatalog myBeasts; 
	
	static boolean homeScreen = true; // used to determine enter button function
	static boolean newSearch = true; // used to determine if txtSearch properties need to change
	static boolean newFood = true;
	static boolean newBeast = true;
	
	static{
		try {
			myBeasts = new FantasticBeastsCatalog();
		}
		catch (IOException e) {
			System.out.print("Error creating catalog");
		}
	}
	
	private int optionSelected = 1; // 1 = Browse; 2 = New Entry (used to determine user menu selection when user hits the enter key)  
	private GridPane pane = new GridPane(); // main pane 
	
	public void start(Stage primaryStage) {
			
		// pane(s)
		final ScrollPane sp = new ScrollPane();
			
		// scene(s)
		final Scene scene = new Scene(pane);
		
		// alert(s)
		// final Alert alert = new Alert(AlertType.CONFIRMATION); // TO-DO: ask user if they would like to enter additional food
			
		// boxes
		final VBox bxMenu = new VBox(5); // box to store menu options
		final VBox imageContainer = new VBox(5); // box to store image 
		final VBox bxMessage = new VBox(20); // box to store search field
		final VBox bxEntryName = new VBox(5); // box to store search field
		final VBox bxEntryFood = new VBox(5); // box to store search field
		final HBox bxSearch = new HBox(5); // box to store search field
		
		// button(s)
		final Button btBrowse = new Button("Browse");
		final Button btEntry = new Button("New Entry");
		final Button btSubmitEntry = new Button("Submit");
		final Button btSearch = new Button("Search");
		final Button btReset = new Button("Reset");
		final Button btMainMenu = new Button("Main Menu");
		
		// group(s)
		Group root = new Group();
		
		// path(s)
		Path browsePath = new Path();
			
		// image(s)
		Image image = new Image("image/background.jpg", 1024, 691.5, false, false); 
		Image pointer = new Image ("image/pointer.png");
		Image selectedImage = new Image ("image/placeholder1.png"); // image of selected beast (temporary set to placeholder image) 
		
		final ImageView displayImage = new ImageView(selectedImage);
		final ImageView pointerImage = new ImageView(pointer); 	
		final ImageView bgImage = new ImageView(image);
			
		
		browsePath.getElements().add(new MoveTo(63, 20));
		browsePath.getElements().add(new LineTo(63, 30));
		
		Path entryPath = new Path();
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
		
		// labels
		Label lblResults = new Label("RESULTS:");
		Label lblSearch = new Label("SEARCH:");
		Label lblEntryName = new Label("NAME:");
		Label lblFoodName = new Label("FOOD:");
		Label lblMessageHeader = new Label ("MESSAGE:");
		Label lblMessage = new Label("You've found a new beast?! Great! Please enter the information you have on the beast below:");
		
		// input(s)
		final TextField txtSearch = new TextField();
		final TextField txtEntryName = new TextField(); 	
		final TextField txtFoodName = new TextField(); 	
		
		// input attributes
		txtSearch.setPrefColumnCount(20);
		txtSearch.setPrefHeight(40);
		txtSearch.setStyle("-fx-font-size: 15px;" + 
				"-fx-text-fill: #a9a9a9;");
		txtSearch.setText("Enter the name of a creature...");
		
		txtEntryName.setPrefColumnCount(20);
		txtEntryName.setPrefHeight(40);
		txtEntryName.setStyle("-fx-font-size: 15px;" + 
				"-fx-text-fill: #a9a9a9;");
		txtEntryName.setText("Enter the name of the creature...");
		
		txtFoodName.setPrefColumnCount(20);
		txtFoodName.setPrefHeight(40);
		txtFoodName.setStyle("-fx-font-size: 15px;" + 
				"-fx-text-fill: #a9a9a9;");
		txtFoodName.setText("Enter name of the food it eats...");
		
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
		
		lblResults.setStyle("-fx-font-weight: bold; "
				+ "-fx-font-size: 16px;");
		
		lblMessageHeader.setStyle("-fx-font-weight: bold; "
				+ "-fx-font-size: 16px;");
		
		lblMessageHeader.setWrapText(true);
		lblMessage.setWrapText(true);
		lblMessage.setStyle("-fx-font-size: 16px;");
		
		// box attributes
		bxSearch.setPadding(new Insets(15,0,15,15));
		bxEntryName.setPadding(new Insets(15,0,15,15));
		bxEntryFood.setPadding(new Insets(15,0,15,15));
		bxMenu.setPadding(new Insets(15,0,15,15));
		bxMessage.setPadding(new Insets(15,0,15,15));
		
		bxMessage.setMaxHeight(150);
		bxMessage.setMaxWidth(510);
		bxMessage.setStyle("-fx-background-color: #ffffff;" +
				"-fx-opacity: 0.95;" +
				"-fx-background-radius: 5,5,5,5;" +
				"-fx-background-radius: 5,5,5,5;" +
				"-fx-border-radius: 5,5,5,5;" +
				"-fx-font-smoothing-type: lcd;");
		
		bxMenu.getChildren().add(lblResults);
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
		btBrowse.setMaxWidth(125);
		btBrowse.setStyle("-fx-background-color: transparent; " +
				"-fx-border-color: #ffffff; " +
				"-fx-border-radius: 10 10 10 10;" +
				"-fx-font-size: 18px;" +
				"-fx-text-fill: #eff1f3;");

		
		btEntry.setMaxHeight(50);
		btEntry.setMaxWidth(125);
		btEntry.setStyle("-fx-background-color: transparent; " +
				"-fx-border-color: #ffffff;" +
				"-fx-border-radius: 10 10 10 10;" +
				"-fx-font-size: 16px;" +
				"-fx-text-fill: #eff1f3;");
		
		btSubmitEntry.setMaxHeight(50);
		btSubmitEntry.setMaxWidth(125);
		btSubmitEntry.setStyle("-fx-background-color: #9FA4A9; " +
				"-fx-border-radius: 10 10 10 10;" +
				"-fx-background-radius: 10 10 10 10;" +
				"-fx-font-size: 18px;" + 
				"-fx-text-fill: #ffffff;");
		
		btSearch.setMaxHeight(50);
		btSearch.setMaxWidth(100);
		btSearch.setStyle("-fx-background-color: #9FA4A9; " +
				"-fx-border-radius: 10 10 10 10;" +
				"-fx-background-radius: 10 10 10 10;" +
				"-fx-font-size: 18px;" + 
				"-fx-text-fill: #ffffff;");
		
		btReset.setMaxHeight(50);
		btReset.setMaxWidth(100);
		btReset.setStyle("-fx-background-color: #E6AF2E; " +
				"-fx-border-radius: 10 10 10 10;" +
				"-fx-background-radius: 10 10 10 10;" +
				"-fx-font-size: 18px;" + 
				"-fx-text-fill: #ffffff;");
		
		btMainMenu.setMaxHeight(50);
		btMainMenu.setMaxWidth(125);
		btMainMenu.setStyle("-fx-background-color: transparent; " +
				"-fx-border-color: #ffffff; " +
				"-fx-border-radius: 10 10 10 10;" +
				"-fx-font-size: 18px;" +
				"-fx-text-fill: #eff1f3;");
		
		// group attributes
		sp.setPannable(true);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setCache(false);

		sp.setPrefSize(400, 200);
		sp.setContent(bxMenu);
		
		// path attributes
		root.getChildren().add(sp);
		
		root.setStyle("-fx-background-color: #9FA4A9; "
				+ "-fx-opacity: 0.90;"
				+ "-fx-background-radius: 5,5,5,5;"
				+ "-fx-border-radius: 5,5,5,5;");

		// TO-DO: Option for user to enter more food
		// alert attributes
		// alert.setTitle("Additional Food?");
		// alert.setHeaderText("Would you like to enter more than one food item?");
		// alert.setContentText("Please choose an option:");
		//
		// ButtonType btYes = new ButtonType("Yes");
		// ButtonType btNo = new ButtonType("No");
		//		
		// alert.getButtonTypes().setAll(btYes,btNo);

		// image attributes
		imageContainer.setStyle("-fx-background-color: #ffffff;" +
				"-fx-opacity: 0.95;" +
				"-fx-background-radius: 5,5,5,5;");
		

	    imageContainer.getChildren().add(displayImage);
	    imageContainer.setAlignment(Pos.CENTER);
	    
	    imageContainer.setMaxHeight(200);
	    imageContainer.setMaxWidth(200);
		
	    // pane attributes	
		setPane(bgImage, pointerImage, btBrowse, btEntry);
		
		// scene attributes
		primaryStage.setTitle("FantasticGUI (Prototype)");
		primaryStage.setScene(scene);
		primaryStage.setMaxHeight(691.5);
		primaryStage.setMaxWidth(1024);
		primaryStage.setMinHeight(691.5);
		primaryStage.setMinWidth(1024);
		
		FadeTransition ft = new FadeTransition(Duration.millis(1500),bgImage);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();	
		
		primaryStage.show();
		intro.play();
		
		
			
		// event handling
		
		//change cursor to hand on hover over button
		btBrowse.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(490,0,0,310));
			cursor.stop();
			cursor.play();
			ptBrowse.play();
			ptEntry.stop();
		});
		
		btEntry.setOnMouseEntered(me -> {
			scene.setCursor(Cursor.HAND);
			GridPane.setMargin(pointerImage, new Insets(490,0,0,490));
			cursor.stop();
			cursor.play();
			ptBrowse.stop();
			ptEntry.play();
			
		});
		
		btSearch.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		btReset.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		btMainMenu.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		btSubmitEntry.setOnMouseEntered(me -> scene.setCursor(Cursor.HAND));
		
		
		//change cursor to point on exit hover over button
		btBrowse.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));		
		btEntry.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btSearch.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));		
		btReset.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btMainMenu.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));
		btSubmitEntry.setOnMouseExited(me -> scene.setCursor(Cursor.DEFAULT));

		
		
		// browse button event
		btBrowse.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				homeScreen = false;
				
				clearPane(pointerImage, btBrowse, btEntry);
				
				pane.getChildren().addAll(btMainMenu, bxSearch, btSearch, txtSearch, btReset, imageContainer, root);
				GridPane.setMargin(btMainMenu, new Insets(-600,0,0,20));
				GridPane.setMargin(bxSearch, new Insets(-200,0,0,200));
				GridPane.setMargin(txtSearch, new Insets(-200,440,0,310));
				GridPane.setMargin(btSearch, new Insets(-200,0,0,610));
				GridPane.setMargin(btReset, new Insets(-200,0,0,715));
				GridPane.setMargin(root, new Insets(100,0,0,200));
				GridPane.setMargin(imageContainer, new Insets(100,0,0,610));

			}
		});
		
		// new entry button event
		btEntry.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				
				homeScreen = false;
				
				clearPane(pointerImage, btBrowse, btEntry);
				
				TypewriterAnimation(lblMessage, lblMessage.getText());
				
				pane.getChildren().addAll(btMainMenu, btSubmitEntry, bxMessage, bxEntryName, bxEntryFood, txtEntryName, txtFoodName);
				GridPane.setMargin(btMainMenu, new Insets(-600,0,0,20));
				GridPane.setMargin(bxMessage, new Insets(-200,0,0,275));
				GridPane.setMargin(txtEntryName, new Insets(50,355,0,390));
				GridPane.setMargin(txtFoodName, new Insets(175,355,0,390));
				GridPane.setMargin(bxEntryName, new Insets(50,0,0,275));
				GridPane.setMargin(bxEntryFood, new Insets(175,0,0,275));
				GridPane.setMargin(btSubmitEntry, new Insets(300,0,0,660));
				
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
				setPane(bgImage, pointerImage, btBrowse, btEntry);
				
				ptEntry.stop();

				
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
				
				// TO-DO: give user option of adding more than one food
				// Optional<ButtonType> result = alert.showAndWait();
				
				try {
					String results = myBeasts.enterData(txtEntryName.getText(), txtFoodName.getText());
					lblMessage.setText(results);
					txtEntryName.setStyle("-fx-font-size: 15px;" + 
							"-fx-text-fill: #a9a9a9;");
					txtEntryName.setText("Enter the name of the creature...");
					txtFoodName.setStyle("-fx-font-size: 15px;" + 
							"-fx-text-fill: #a9a9a9;");
					txtFoodName.setText("Enter name of the food it eats...");
				
				} catch (IOException e1) {
					System.out.println("Not Found");
				}
			}
			
		});
		
		// search button event
		btSearch.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				
				bxMenu.getChildren().clear();
				bxMenu.getChildren().add(lblResults);
				
				try {
					String[] results = myBeasts.retrieveData(txtSearch.getText().trim().replaceAll("\\s+",""));
					Label[] options = new Label[results.length];

					// create and add labels to menu box
					int i = 0;
					
					// display name and food on separate lines
					if (results.length == 1){
						String[] tempResults = new String [2];
						for (String result: results[0].split(";")) {
							tempResults[i] = result;
							i++;
						}
						results = tempResults;
						options = new Label[results.length];
					}
									
					i = 0;
					for (Label option: options){
						option = new Label (results[i]);
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
				bxMenu.getChildren().add(lblResults);
				txtSearch.setStyle("-fx-font-size: 15px;" + 
						"-fx-text-fill: #a9a9a9;");
				txtSearch.setText("Enter the name of a creature...");

				
				newSearch = true;
			}
		});
				
		
		// moves the pointer between menu options
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.RIGHT){
					GridPane.setMargin(pointerImage, new Insets(490,0,0,490));
					
					if (optionSelected != 2){
						cursor.stop();
						cursor.play();
						
						ptBrowse.stop();
						ptEntry.play();
					}
					
					optionSelected = 2;
				}
				else if (event.getCode() == KeyCode.LEFT){
					GridPane.setMargin(pointerImage, new Insets(490,0,0,310));
					
					
					if (optionSelected != 1){
						cursor.stop();
						cursor.play();
						
						ptBrowse.play();
						ptEntry.stop();
						
					}
					
					optionSelected = 1;
					
				}
				else if (homeScreen == true && event.getCode() == KeyCode.ENTER){
					if (optionSelected == 1) {
						btBrowse.fire();
					}
					else if (optionSelected == 2) {
						btEntry.fire();
					}
				}
				else if (homeScreen == false && event.getCode() == KeyCode.ENTER){
					btSearch.fire();
				}
			}
		});
					
	} 
	
	public void setPane(ImageView bgImage, ImageView pointerImage, Button btBrowse, Button btEntry){
			
		pane.getChildren().add(bgImage);
		pane.getChildren().add(pointerImage);
		pane.getChildren().add(btBrowse);
		pane.getChildren().add(btEntry);
		GridPane.setMargin(pointerImage, new Insets(490,0,0,310));
		GridPane.setMargin(btBrowse, new Insets(490,0,0,360));
		GridPane.setMargin(btEntry, new Insets(490,0,0, 540));

		ptBrowse.play();

		
	}
	
	public void clearPane(ImageView pointerImage, Button btBrowse, Button btEntry){
		pane.getChildren().remove(pointerImage);
		pane.getChildren().remove(btBrowse);
		pane.getChildren().remove(btEntry);
	}
	
	public void TypewriterAnimation(Label lbl, String message){
		final Animation text = new Transition() {
			
			{
				setCycleDuration(Duration.millis(4500));
			}

			protected void interpolate(double frac) {
				final int length = message.length();
				final int n = Math.round(length * (float) frac);
				lbl.setText(message.substring(0, n));	
			}
		};
		text.play();
		
	}

	public static void main(String[] args) { 
		Application.launch(args); 
	
	} 
}
