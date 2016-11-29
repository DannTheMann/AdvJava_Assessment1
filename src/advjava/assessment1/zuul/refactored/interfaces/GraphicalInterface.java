package advjava.assessment1.zuul.refactored.interfaces;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import advjava.assessment1.zuul.refactored.Game;
import advjava.assessment1.zuul.refactored.Main;
import advjava.assessment1.zuul.refactored.cmds.Command;
import advjava.assessment1.zuul.refactored.cmds.CommandExecution;
import advjava.assessment1.zuul.refactored.item.Item;
import advjava.assessment1.zuul.refactored.utils.Out;
import advjava.assessment1.zuul.refactored.utils.Resource;
import advjava.assessment1.zuul.refactored.utils.ResourceManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GraphicalInterface extends Application implements UserInterface {

	private static Game game;
	private static FontManager fontManager;

	private static List<Button> commandButtons;
	private static Stage stage;
	private static Scene scene;
	private static BorderPane root;
	private static HBox commands;
	private static Node inventory;
	private static TilePane characters;
	private static TilePane exits;

	/* Constants for nodes used in gridpanes (indentations) */
	private static final int NODE_VERTICAL_INSET = 10;
	private static final int NODE_HORIZONTAL_INSET = 10;

	/* Constants for spacing offsets between nodes in gridpanes */
	private static final int NODE_LEFT_OFFSET = 10;
	private static final int NODE_TOP_OFFSET = 10;
	private static final int NODE_RIGHT_OFFSET = 10;
	private static final int NODE_BOTTOM_OFFSET = 10;

	private static final int SIDEBAR_IMAGE_WIDTH = 50;
	private static final int SIDEBAR_IMAGE_HEIGHT = 50;
	private static final int MAX_WIDTH_CHAR = 8;
	
	public static String getExternalCSS() {
		return new File(Main.XML_CONFIGURATION_FILES + File.separator + Main.game.getProperty("css")).toURI().toString();
	}

	@Override
	public void print(Object obj) {
		System.out.print(obj);
	}

	@Override
	public void println(Object obj) {
		System.out.println(obj);
	}

	@Override
	public void println() {
		System.out.println();
	}

	@Override
	public void printErr(Object obj) {
		System.err.print(obj);
	}

	@Override
	public void printlnErr(Object obj) {
		System.err.println(obj);
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayLocale(Object obj) {

	}

	@Override
	public void displaylnLocale(Object obj) {

	}

	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void play(Game zuulGame) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		game = Main.game;
		fontManager = new FontManager(game.getProperty("defaultFont"));
		stage = primaryStage;

		// Set title of the game from what is within our .properties file
		stage.setTitle(game.getProperty("title"));

		// Define the minimum resolution of the root container of the stage
		stage.setMinWidth(720);
		stage.setMinHeight(480);

		// Define the maximum resolution of the root container of the stage
		stage.setMaxWidth(1920);
		stage.setMaxHeight(1080);

		// Create resource manager, needed to handle resources such as images
		ResourceManager.newResourceManager();
		
		commands = getCommandHBox();

		setBackgroundImage("outside");

		/* Create root pane, a border pane */
		root = new BorderPane();
		// root.setStyle("");
		root.setStyle("-fx-background-image: url(outside.jpg); -fx-background-size: cover;");
		root.setBottom(commands);
		
		inventory = getSidePanel(game.getPlayer().getInventory());
		// 0.25); -fx-effect: dropshadow(gaussian, green, 50, 0, 0, 0);");

		characters = new TilePane();
		characters.setAlignment(Pos.BASELINE_CENTER);
		characters.setPrefWidth(200);
		characters.setHgap(NODE_HORIZONTAL_INSET);
		characters.setVgap(NODE_VERTICAL_INSET);

		// Insets, in order of
		// top, right, bottom, left
		characters.setPadding(new Insets(NODE_TOP_OFFSET, NODE_RIGHT_OFFSET, NODE_BOTTOM_OFFSET, NODE_LEFT_OFFSET));
		characters.setPrefRows(4);

		// Load all items...
		// game.getPlayer().getInventory().stream().forEach(i->inventory.getChildren().add(getDisplayItem(i)));

		scene = new Scene(root, 1280, 720);
		
		// Setup styling
		scene.getStylesheets().add(getExternalCSS());

		stage.setScene(scene);

		stage.show();

	}

	private Node getDisplayItem(Resource resource) {

		Out.out.logln("Loading: " + resource.getName() + "...");
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(NODE_TOP_OFFSET, NODE_LEFT_OFFSET, NODE_BOTTOM_OFFSET, NODE_RIGHT_OFFSET));
		grid.setHgap(NODE_RIGHT_OFFSET);
		grid.setVgap(NODE_BOTTOM_OFFSET);
		grid.setAlignment(Pos.BASELINE_CENTER);
		
		Text text = new Text(resource.getName().length() > MAX_WIDTH_CHAR ? resource.getName().substring(0, MAX_WIDTH_CHAR-2) + "..." : resource.getName());
		text.setTextAlignment(TextAlignment.CENTER);
		ImageView iv = new ImageView(resource.getImage());
		iv.setPreserveRatio(true);
		iv.setFitHeight(SIDEBAR_IMAGE_HEIGHT);
		iv.setFitWidth(SIDEBAR_IMAGE_WIDTH);
		
		grid.add(text, 0, 0);		
		grid.add(iv, 0, 1);
		
		if (resource instanceof Item) {
			
			String css = "sidebar-button";
			
			Item item = (Item) resource;
			text.setText(text.getText() + System.lineSeparator() + "Weight: " + item.getWeight());
			text.setFont(fontManager.getFont("SansSerif"));
			
			// Create drop button
			Button button = newCommandButton("drop " + resource.getName().toLowerCase(), game.getCommandManager().getCommand("Drop"), css);
			button.setPrefSize(50, 20);
			grid.add(button, 1, 0);
			
			//Create give button
			button = newCommandButton("give " + resource.getName().toLowerCase(), game.getCommandManager().getCommand("Give"), css);
			button.setPrefSize(50, 20);
			
			grid.add(button, 1, 1);
		}
		
		if(resource.getDescription() != null){
			Tooltip tp = new Tooltip(resource.getName() + System.lineSeparator() + System.lineSeparator() + resource.getDescription());
			tp.setContentDisplay(ContentDisplay.BOTTOM);
			tp.setFont(fontManager.getFont("Yu Gothic"));
			tp.setOpacity(.85);
			modifyTooltipTimer(tp, 25);
			Tooltip.install(grid, tp);
		}
		
		return grid;
	}

	private Button newCommandButton(String parameters, Command command, String css) {
		this.parameters = parameters;
		Button button = new Button(command.getName());
		button.getStyleClass().add(css);
		button.setOnAction(e -> {
			executeCommand(command, e);
			this.parameters = "";
		});
		return button;
	}

	private void setBackgroundImage(String resource) {
		ImageView iv = new ImageView(ResourceManager.getResourceManager().getImage(resource));
		iv.fitWidthProperty().bind(stage.widthProperty());
		iv.fitHeightProperty().bind(stage.heightProperty());
		// ...
	}

	private Node getSidePanel(Collection<Resource> stream) {
		
		ScrollPane sp = new ScrollPane();
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		TilePane pane = new TilePane();
		pane.setAlignment(Pos.BASELINE_CENTER);
		pane.setPrefWidth(300);
		pane.setHgap(NODE_HORIZONTAL_INSET);
		pane.setVgap(NODE_VERTICAL_INSET);

		// Insets, in order of
		// top, right, bottom, left
		pane.setPadding(new Insets(NODE_TOP_OFFSET, NODE_RIGHT_OFFSET, NODE_BOTTOM_OFFSET, 0));
		pane.setPrefRows(4);
		Out.out.logln("Adding player inventory... [" + stream.size() + "]");
		stream.forEach(i -> pane.getChildren().add(getDisplayItem(i)));
		
		sp.setContent(pane);
		
		return sp;
	}

	private HBox getCommandHBox() {
		HBox hbox = new HBox();
		hbox.setPrefHeight(50);
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");

		commandButtons = new ArrayList<>(game.getCommandManager().commands().size());

		Button buttonCurrent = null;

		for (Command command : game.getCommandManager().commands()) {

			if (!command.interfaceAcceptable(this)) {
				continue;
			}

			buttonCurrent = newCommandButton("", command, "command-button");
			buttonCurrent.setPrefSize(100, 20);
			hbox.getChildren().add(buttonCurrent);

		}

		return hbox;
	}

	private String parameters = "hello darkness my old friend";

	private void executeCommand(Command cmd, ActionEvent event) {

		Out.out.logln(event.getEventType().getName() + " > [" + cmd.getName() + "] >> " + parameters);

		CommandExecution ce = new CommandExecution(parameters);

		cmd.action(game, ce);

	}

	@Override
	public void showInventory() {

		if (root.getLeft() != null) {
			root.setLeft(null);
			return;
		}

		disableOtherWindows();

		root.setLeft(inventory);

		// sliding transition... from left ...

	}

	@Override
	public void showCharacters() {
		if (root.getRight() != null) {
			root.setRight(null);
			return;
		}

		disableOtherWindows();

		root.setRight(characters);

		// sliding transition... from left ...
	}

	@Override
	public void showRoom() {

	}

	@Override
	public void showExits() {

	}

	private void disableOtherWindows() {
		root.setLeft(null);
		root.setRight(null);
		root.setTop(null);
	}

	public String getCurrentParameters() {
		return parameters;
	}
	
	public static void modifyTooltipTimer(Tooltip tooltip, int delay) {
	    try {
	        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
	        fieldBehavior.setAccessible(true);
	        Object objBehavior = fieldBehavior.get(tooltip);

	        Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
	        fieldTimer.setAccessible(true);
	        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

	        objTimer.getKeyFrames().clear();
	        objTimer.getKeyFrames().add(new KeyFrame(new Duration(delay)));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
