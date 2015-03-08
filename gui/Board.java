package comp1140.ass2.gui;


import comp1140.ass2.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;


public class Board extends Application {

    private static int BOARD_HEIGHT = 768;
    private static int BOARD_WIDTH = 1024;
    private static int MARGIN = 10;
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;
    protected final int NumberOfCards = 52;
    protected int humanPlayerHandSize = 7;
    protected int godPlayerHandSize = 7;
    //set default the number of deck for easy programming
    public static int deckSize = 0;
    private static final int RUN_1_X_ORGIN = CARD_WIDTH+ 5*MARGIN;
    private static final int RUN_1_Y_ORGIN = 5*MARGIN+ + CARD_HEIGHT;
    private static final int RUN_1_MAX_X = RUN_1_X_ORGIN + 7*CARD_WIDTH;
    private static final int RUN_1_MAX_Y = RUN_1_Y_ORGIN + CARD_HEIGHT;

    private static final int RUN_2_X_ORGIN = CARD_WIDTH+ 5*MARGIN;
    private static final int RUN_2_Y_ORGIN = RUN_1_MAX_Y + 7*MARGIN;
    private static final int RUN_2_MAX_Y = RUN_2_Y_ORGIN + CARD_HEIGHT;

    protected ArrayList<Card> myHandH;
    protected ArrayList<Card> myHandG;
    protected ArrayList<Card> myDeck;
    protected ArrayList<Card> myPile;

    protected Rectangle[] humanPlayerRun = new Rectangle[4];
    protected ArrayList<Card> humanRun = new ArrayList<Card>(Arrays.<Card>asList(null, null, null, null));
    protected ArrayList<Card> godRun = new ArrayList<Card>(Arrays.<Card> asList(null, null, null, null));
    protected Rectangle[] godPlayerRun = new Rectangle[4];

    private Rectangle deck  = new Rectangle();

    protected DraggableCards[] humanPlayerHand = new DraggableCards[humanPlayerHandSize];
    protected DraggableCards[] godPlayerHand = new DraggableCards[godPlayerHandSize];

    protected DraggableCards[] pile = new DraggableCards[NumberOfCards];
    protected DraggableCards[] deckCard = new DraggableCards[NumberOfCards];

    private Group root = new Group();
    private Scene scene;
    Text AIindication = new Text();
    Text calculatingHuman = new Text();
    Text calculatingGod = new Text();
    // timer
    Timer timer;
    Timer delayAI;

    //boolean value to indicate if the human player is playing legally
    private boolean isDrawfromPile = false;
    private int drawFromPile = 0;
    private int drawFromDeck = 0;
    private int drawFromHand = 0;
    private int addToHand = 0;
    private int addToPile = 0;

    //to count whether the game is over
    //lack of progress
    private int discardCount = 0;
    private int currentDeckSizeAfterHumanPlay = 0;//exhaustion
    private int currentDeckSizeBeforeHumanPlay = 0 ;//exhaustion
    private int currentPileSize = 0;//exhaustion
    private ArrayList<Card> previousGodRun = godRun;
    private ArrayList<Card> afterGodRun = godRun;

    //value to store current score and text of human Player to show in the billboard
    private int humanPlayerCurrentScoreSpade = 0;
    private int humanPlayerCurrentScoreClub = 0;
    private int humanPlayerCurrentScoreDiamond = 0;
    private int humanPlayerCurrentScoreHeart = 0;

    private int humanPlayerCurrentTimesSpade = 0;
    private int humanPlayerCurrentTimesClub = 0;
    private int humanPlayerCurrentTimesDiamond = 0;
    private int humanPlayerCurrentTimesHeart = 0;

    private Text humanPlayerClubScore = new Text();
    private Text humanPlayerDiamondScore = new Text();
    private Text humanPlayerHeartScore = new Text();
    private Text humanPlayerSpadeScore = new Text();
    private Text humanPlayerScore = new Text();

    //value to store current score and text of god Player to show in the billboard
    private int godPlayerCurrentScoreSpade = 0;
    private int godPlayerCurrentScoreClub = 0;
    private int godPlayerCurrentScoreDiamond = 0;
    private int godPlayerCurrentScoreHeart = 0;

    private int godPlayerCurrentTimesSpade = 0;
    private int godPlayerCurrentTimesClub = 0;
    private int godPlayerCurrentTimesDiamond = 0;
    private int godPlayerCurrentTimesHeart = 0;

    private Text godPlayerClubScore = new Text();
    private Text godPlayerDiamondScore = new Text();
    private Text godPlayerHeartScore = new Text();
    private Text godPlayerSpadeScore = new Text();
    private Text godPlayerScore = new Text();

    private int totalSumGod = 0;
    private int totalSumHuman = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //GUI for welcome and set the number of deck
        primaryStage.setTitle("Renier");
        Group root = new Group();
        Image background = new Image("/resources/images/loadingImage.png");
        ImagePattern backgroundPattern = new ImagePattern(background);
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT, backgroundPattern);

        //userinput for cards number of cards
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(BOARD_WIDTH/4, 50, 50,BOARD_HEIGHT/2));
        //stylish the welcome title
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);

        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(254, 235, 66, 0.3));
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);

        blend.setBottomInput(ds);

        DropShadow ds1 = new DropShadow();
        ds1.setColor(Color.web("#f13a00"));
        ds1.setRadius(20);
        ds1.setSpread(0.2);

        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);

        InnerShadow is = new InnerShadow();
        is.setColor(Color.web("#feeb42"));
        is.setRadius(9);
        is.setChoke(0.8);
        blend2.setBottomInput(is);

        InnerShadow is1 = new InnerShadow();
        is1.setColor(Color.web("#f13a00"));
        is1.setRadius(5);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);

        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        blend.setTopInput(blend1);

        Text welcometitle = new Text("Welcome\n      to\n  Renier");
        welcometitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 80));
        welcometitle.setEffect(blend);
        grid.add(welcometitle, 0, 0, 2, 1);
        //end of stylish the welcome title

        Label userName = new Label("Set deck(Enter 0-"+(NumberOfCards - humanPlayerHandSize - godPlayerHandSize) +"''):");
        userName.setTextFill(Color.BLACK);
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPrefWidth(8);
        grid.add(userTextField, 1, 1);

        Button btn = new Button("Start");
        HBox hbBtn = new HBox(7);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        btn.setOnAction(e -> {
            actiontarget.setFill(Color.FIREBRICK);
            deckSize = Integer.parseInt(userTextField.getText());
            currentDeckSizeBeforeHumanPlay = deckSize;
            currentDeckSizeAfterHumanPlay = deckSize;
            if(deckSize > NumberOfCards - humanPlayerHandSize - godPlayerHandSize || deckSize < 0){
                Stage dialogStage = new Stage();
                Button warning = new Button();
                warning.setText("Close");
                warning.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.exit();
                    }
                });
                dialogStage.setTitle("Error");
                dialogStage.setWidth(300);
                dialogStage.setHeight(150);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setScene(new Scene(VBoxBuilder.create().
                        children(new Text("You have the number of table WRONG!"), warning).
                        alignment(Pos.CENTER).padding(new Insets(20)).build()));
                dialogStage.show();
            }else{
                primaryStage.setScene(logInScene());
            }
        });
        //end of userinput for cards number of cards

        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Scene logInScene() {
        // GUI for formal game
        Image background = new Image("/resources/images/background.png");
        ImagePattern backgroundPattern = new ImagePattern(background);
        scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT, backgroundPattern);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q) { Platform.exit(); event.consume();} // quit the puzzle
        });

        //set button to indicate human player has finished
        Button btn = new Button();
        btn.setText("Next Turn");
        btn.setOnAction(e -> {
            if (!isValidMove()) {//exit if the move is invaild
                Image background1 = new Image("/resources/images/sorry.png");
                ImagePattern backgroundPattern1 = new ImagePattern(background1);
                Rectangle warn = new Rectangle(1024, 768, backgroundPattern1);
                root.getChildren().add(warn);
                Stage dialogStage = new Stage();
                Button warning = new Button();
                warning.setText("Close");
                warning.setOnAction(event -> Platform.exit());
                dialogStage.setTitle("Error");
                dialogStage.setWidth(200);
                dialogStage.setHeight(150);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setScene(new Scene(VBoxBuilder.create().
                        children(new Text("Sorry, your move is ISVALID!\n" + "Please close window and restart!"), warning).
                        alignment(Pos.CENTER).padding(new Insets(5)).build()));
                dialogStage.show();
                //reset to default
                isDrawfromPile = false;
                drawFromPile = 0;
                drawFromDeck = 0;
                drawFromHand = 0;
                addToHand = 0;
                addToPile = 0;
                //end of reset
            } else {
                humanPlayerClubScore.setOpacity(0);
                humanPlayerDiamondScore.setOpacity(0);
                humanPlayerHeartScore.setOpacity(0);
                humanPlayerSpadeScore.setOpacity(0);
                humanPlayerScore.setOpacity(0);

                godPlayerClubScore.setOpacity(0);
                godPlayerDiamondScore.setOpacity(0);
                godPlayerHeartScore.setOpacity(0);
                godPlayerSpadeScore.setOpacity(0);
                godPlayerScore.setOpacity(0);

                //check the game is over
                if (isGameOver()) {
                    // God player wins
                    if (totalSumGod > totalSumHuman) {
                        scene.setCursor(Cursor.NONE);
                        Button lose = new Button();
                        lose.setText("Close and try to win");
                        lose.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Platform.exit();
                            }
                        });
                        Stage youLoseStage = new Stage();
                        youLoseStage.setTitle("Loser");
                        youLoseStage.setWidth(280);
                        youLoseStage.setHeight(280);
                        youLoseStage.initModality(Modality.WINDOW_MODAL);
                        youLoseStage.setScene(new Scene(VBoxBuilder.create().
                                children(new Text(""), lose).
                                alignment(Pos.CENTER).padding(new Insets(5)).build()));
                        Group root = new Group();
                        root.getChildren().add(lose);
                        lose.setLayoutX(70);
                        lose.setLayoutY(200);
                        Image back = new Image("/resources/images/iwin.png");
                        ImagePattern backPattern = new ImagePattern(back);
                        Scene sceneLose = new Scene(root, 280, 280, backPattern);
                        youLoseStage.setScene(sceneLose);
                        youLoseStage.show();
                    } else if(totalSumGod < totalSumHuman) {//human player wins! hooray!
                        scene.setCursor(Cursor.NONE);
                        Button congrats = new Button();
                        congrats.setText("Close and win again");
                        congrats.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Platform.exit();
                            }
                        });
                        Stage youWinStage = new Stage();
                        youWinStage.setTitle("Winner");
                        youWinStage.setWidth(400);
                        youWinStage.setHeight(400);
                        youWinStage.initModality(Modality.WINDOW_MODAL);
                        youWinStage.setScene(new Scene(VBoxBuilder.create().
                                children(new Text(""), congrats).
                                alignment(Pos.CENTER).padding(new Insets(5)).build()));
                        Group root = new Group();
                        root.getChildren().add(congrats);
                        congrats.setLayoutX(250);
                        congrats.setLayoutY(300);
                        Image back = new Image("/resources/images/youwin.png");
                        ImagePattern backPattern = new ImagePattern(back);
                        Scene sceneWin = new Scene(root, 400, 400, backPattern);
                        youWinStage.setScene(sceneWin);
                        youWinStage.show();
                    }else if(totalSumGod == totalSumHuman) {
                        scene.setCursor(Cursor.NONE);
                        Button tie = new Button();
                        tie.setText("Close and play again");
                        tie.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Platform.exit();
                            }
                        });
                        Stage youWinStage = new Stage();
                        youWinStage.setTitle("Winner");
                        youWinStage.setWidth(640);
                        youWinStage.setHeight(480);
                        youWinStage.initModality(Modality.WINDOW_MODAL);
                        youWinStage.setScene(new Scene(VBoxBuilder.create().
                                children(new Text(""), tie).
                                alignment(Pos.CENTER).padding(new Insets(5)).build()));
                        Group root = new Group();
                        root.getChildren().add(tie);
                        tie.setLayoutX(250);
                        tie.setLayoutY(400);
                        Image back = new Image("/resources/images/tie.png");
                        ImagePattern backPattern = new ImagePattern(back);
                        Scene sceneWin = new Scene(root, 640, 480, backPattern);
                        youWinStage.setScene(sceneWin);
                        youWinStage.show();
                    }
                } else {
                    //end of checking
                    //set word in waiting AI response
                    DropShadow ds = new DropShadow();
                    ds.setOffsetY(3.0f);
                    ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
                    AIindication.setFont(Font.font(null, FontWeight.BOLD, 40));
                    AIindication.setFill(Color.DARKSALMON);
                    AIindication.setEffect(ds);
                    AIindication.setCache(true);
                    AIindication.setLayoutX(BOARD_WIDTH / 4 - 3 * MARGIN);
                    AIindication.setLayoutY(BOARD_HEIGHT / 2 - MARGIN);
                    AIindication.setText("The computer is thinking ...");

                    calculatingHuman.setFont(Font.font(null, FontWeight.BOLD, 15));
                    calculatingHuman.setFill(Color.DARKSALMON);
                    calculatingHuman.setEffect(ds);
                    calculatingHuman.setCache(true);
                    calculatingHuman.setLayoutX(getXForSquare_1(4) + MARGIN + 4.7 * CARD_WIDTH);
                    calculatingHuman.setLayoutY(getYForSquare_1(4) + 5 * MARGIN);
                    calculatingHuman.setText("Calculating...");

                    calculatingGod.setFont(Font.font(null, FontWeight.BOLD, 15));
                    calculatingGod.setFill(Color.DARKSALMON);
                    calculatingGod.setEffect(ds);
                    calculatingGod.setCache(true);
                    calculatingGod.setLayoutX(getXForSquare_1(4) + MARGIN + 4.7 * CARD_WIDTH);
                    calculatingGod.setLayoutY(getYForSquare_1(4) + 1.5 * CARD_HEIGHT + 5 * MARGIN);
                    calculatingGod.setText("Calculating...");
                    //end of setting

                    //disable mouse
                    disableMouse();
                    //reset to default
                    isDrawfromPile = false;
                    drawFromPile = 0;
                    drawFromDeck = 0;
                    drawFromHand = 0;
                    addToHand = 0;
                    addToPile = 0;
                    //end of reset

                    //timer, 2 seconds AI dealing with the card state
                    delayAI = new Timer();
                    delayAI.schedule(new delayAImoveCard(), 3000);
                    //timer, 5 seconds enable mouse
                    timer = new Timer();
                    timer.schedule(new enableMouse(), 3000);
                }
            }
        });
        btn.setLayoutX(BOARD_WIDTH - CARD_WIDTH);
        btn.setLayoutY(BOARD_HEIGHT - CARD_WIDTH);
        root.getChildren().add(calculatingGod);
        root.getChildren().add(calculatingHuman);
        root.getChildren().add(AIindication);
        root.getChildren().add(btn);

        // computing
        // generating cards
        Table cardGenerator = new Table(true);
        myHandH = cardGenerator.getMyHandH();
        myHandG = cardGenerator.getMyHandG();
        myDeck = cardGenerator.getMyDeck();
        myPile = cardGenerator.getPile();
        //end of generation

        //set the table
        reportHumanPlayerCurrentScore();
        reportGodPlayerCurrentScore();
        makeTargetHumanPlayerRun(root);
        makeTargetGodPlayerRun(root);
        makeHumanPlayerHand(root);
        makeGodPlayerHand(root);
        makePile(root);
        if(deckSize != 0){//show deck only the userinput is non-zero
            makeCards_Deck(root);
            flippedtoShowDeck(root);
            makeText(root);}

        //block mouse in god player's run area
        Rectangle blockGodRunArea = new Rectangle();
        blockGodRunArea.setWidth(7*CARD_WIDTH);
        blockGodRunArea.setHeight(CARD_HEIGHT);
        blockGodRunArea.setLayoutX(4 * MARGIN);
        blockGodRunArea.setLayoutY(RUN_2_MAX_Y + 5 * MARGIN);
        blockGodRunArea.setFill(Color.TRANSPARENT);
        blockGodRunArea.setCursor(Cursor.NONE);
        root.getChildren().add(blockGodRunArea);
        //end of blocking
        //end of set-up

        return scene;
    }

    //calculate sum
    private int sum(int times, int score) {
        int s = 0;
        if(score != 0) {
            s = (int) Math.pow(2, times) * (score - 20);
        }
        return s;
    }

    //disable mouse
    public void disableMouse () {
        scene.setCursor(Cursor.NONE);
        AIindication.setOpacity(1);
        calculatingGod.setOpacity(1);
        calculatingHuman.setOpacity(1);
    }

    // timer, delay AI for 2 seconds
    private class delayAImoveCard extends TimerTask {
        public void run() {
            Platform.runLater(() -> {
                attemptToMove_god();
                calculateGodRunScore();
            });

        }

    }

    // timer, enable mouse after 5 seconds
    private class enableMouse extends TimerTask {
        public void run() {
            Platform.runLater(() -> {
                AIindication.setOpacity(0);
                calculatingGod.setOpacity(0);
                calculatingHuman.setOpacity(0);
                reportHumanPlayerCurrentScore();
                reportGodPlayerCurrentScore();
            });
            // revive mouse
            scene.setCursor(Cursor.DEFAULT);//enable mouse after 5 seconds
            timer.cancel(); // Terminate the timer thread
        }
    }

    //call this method to activate board
    public static void start() {
        Application.launch();
    }

    //generalising the position for each card
    //decide which grid of run to go
    public static int getGridPosition(Card c, ArrayList<Card> list) {
        if (list == null) {
            return -1;
        }else if (c == null){
            return -1;
        }else if (list.contains(c)) {
            return list.indexOf(c);
        } else {
            return -1;
        }
    }
    //set position for card in different list
    private void setPosition (DraggableCards card) {
        int p= humanRun.indexOf(card.card);
        if (p != -1){
            card.setLayoutX(getXForSquare_1(p));
            card.setLayoutY(getYForSquare_1(p));
        }else {
            p = getGridPosition(card.card, myPile);
            if (p != -1){
                switch (card.card.getMySuit()) {
                    case "DIAMONDS":
                        card.setLayoutX(RUN_1_MAX_X - 7 * MARGIN);
                        card.setLayoutY(7 * MARGIN + RUN_1_MAX_Y);
                        break;
                    case "SPADES":
                        card.setLayoutX(RUN_1_MAX_X - 7 * MARGIN);
                        card.setLayoutY(5 * MARGIN + RUN_1_MAX_Y - 2 * CARD_HEIGHT);
                        break;
                    case "CLUBS":
                        card.setLayoutX(RUN_1_MAX_X - 7 * MARGIN);
                        card.setLayoutY(MARGIN + RUN_2_MAX_Y);
                        break;
                    case "HEARTS":
                        card.setLayoutX(RUN_1_MAX_X - 7 * MARGIN);
                        card.setLayoutY(6 * MARGIN + RUN_1_MAX_Y - CARD_HEIGHT);
                        break;}
            }else  {
                p = getGridPosition(card.card, myHandH);
                if (p != -1){
                    card.setLayoutX(4 * MARGIN + p * CARD_WIDTH);
                    card.setLayoutY(MARGIN);
                }else {
                    p = getGridPosition(card.card, myDeck);
                    if (p != -1){
                        card.setLayoutX(RUN_1_MAX_X + 0.5 * CARD_WIDTH);
                        card.setLayoutY(RUN_1_MAX_Y + MARGIN * 9);
                    }else {
                        p = getGridPosition(card.card, godRun);
                        if ( p != -1){
                            switch (card.card.getMySuit()) {
                                case "DIAMONDS":
                                    card.setLayoutX(getXForSquare_2(0));
                                    card.setLayoutY(getYForSquare_2(0));
                                    break;
                                case "SPADES":
                                    card.setLayoutX(getXForSquare_2(2));
                                    card.setLayoutY(getYForSquare_2(2));
                                    break;
                                case "CLUBS":
                                    card.setLayoutX(getXForSquare_2(3));
                                    card.setLayoutY(getYForSquare_2(3));
                                    break;
                                case "HEARTS":
                                    card.setLayoutX(getXForSquare_2(1));
                                    card.setLayoutY(getYForSquare_2(1));
                                    break;}
                        }else {
                            p = getGridPosition (card.card, myHandG);
                            if (p != -1){
                                card.setLayoutX(4 * MARGIN + p * CARD_WIDTH);
                                card.setLayoutY(RUN_2_MAX_Y + 5 * MARGIN);
                            }else{
                            }
                        }}}}}}
    //end of generalisation

    // grid for store run of human player
    private void makeTargetHumanPlayerRun(Group root) {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                int square = i + (j);
                Rectangle r = new Rectangle();
                r.setWidth(CARD_WIDTH - 2);
                r.setHeight(CARD_HEIGHT - 2);
                // ****************************
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.LIGHTGRAY);
                r.setStrokeWidth(2.5);
                r.setLayoutX(getXForSquare_1(square));
                r.setLayoutY(getYForSquare_1(square));
                // ***************************
                root.getChildren().add(r);
                humanPlayerRun[square] = r;}}}
    private double getXForSquare_1(int s) {return RUN_1_X_ORGIN + 1.2*((s % 4) * CARD_WIDTH);}
    private int getYForSquare_1(int s) {
        return RUN_1_Y_ORGIN ;
    }//end of human player's run
    //end of human player's run

    // grid for store run of godplayer
    private void makeTargetGodPlayerRun(Group root) {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                int square = i + (j);
                Rectangle r = new Rectangle();
                r.setWidth(CARD_WIDTH - 2);
                r.setHeight(CARD_HEIGHT - 2);
                // ****************************
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.LIGHTGRAY);
                r.setStrokeWidth(2.5);
                r.setLayoutX(getXForSquare_2(square));
                r.setLayoutY(getYForSquare_2(square));
                // ****************************
                root.getChildren().add(r);
                godPlayerRun[square] = r;}}}
    private double getXForSquare_2(int s) {
        return RUN_2_X_ORGIN + 1.2*((s % 4) * CARD_WIDTH);
    }
    private int getYForSquare_2(int s) {
        return RUN_2_Y_ORGIN ;
    }
    private void setGodPosition (DraggableCards card) {   //the function to set the God cards. -- kevin
        int p = getGridPosition(card.card, godRun);
        if (p != -1){
            card.setLayoutX(getXForSquare_2(p));
            card.setLayoutY(getYForSquare_2(p));
        }else {
            p = getGridPosition(card.card, myHandG);
            if (p != -1){
                card.setLayoutX(4 * MARGIN + p * CARD_WIDTH);
                card.setLayoutY(RUN_2_MAX_Y + 5 * MARGIN);
            }else {
            }
        }
    }
    private Card getMinCard(ArrayList<Card> list) {   // a little intelligence  --kevin
        Card card = list.get(0);
        for (int i = 0; i < list.size()-1; i++){
            if (list.get(i).getMyNumber() > list.get(i+1).getMyNumber()){
                card = list.get(i+1);
            }
        }
        return card;
    }

    public void updateGodCardState () {  // update the position of God cards
        Card card = getMinCard(myHandG);
        myHandG.remove(card);
        if (card.getMySuit() == "DIAMONDS"){
            godRun.set(0, card);
        }else if (card.getMySuit() == "HEARTS"){
            godRun.set(1, card);
        }else if (card.getMySuit() == "SPADES"){
            godRun.set(2, card);
        }else if (card.getMySuit() == "CLUBS"){
            godRun.set(3, card);
        }
        myHandG.add(myPile.get(myPile.size()-1));
        myPile.remove(myPile.get(myPile.size()-1));
    }
    public void attemptToMove_god(){  // special attempt to move methods
        updateGodCardState ();
        for (DraggableCards card1 : godPlayerHand){
            setGodPosition(card1);
        }
        for (DraggableCards card1 : pile){
            if (card1 != null){
                setGodPosition(card1);
            }
        }
    }
    //show god player current score
    private void reportGodPlayerCurrentScore() {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        //total sum
        godPlayerScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 1.5 * CARD_HEIGHT + 12 * MARGIN,  "Total sum:   " + totalSumGod);
        godPlayerScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        godPlayerScore.setEffect(ds);
        godPlayerScore.setCache(true);
        godPlayerScore.setFill(Color.rgb(232, 28, 72));

        godPlayerClubScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 1.5 * CARD_HEIGHT + 4 * MARGIN, "Club: "+ godPlayerCurrentScoreClub + "  "+(int)Math.pow(2, godPlayerCurrentTimesClub)+" times");
        godPlayerClubScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        godPlayerClubScore.setEffect(ds);
        godPlayerClubScore.setCache(true);

        godPlayerDiamondScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 1.5 * CARD_HEIGHT + 6 * MARGIN, "Diamond: "+ godPlayerCurrentScoreDiamond + "  "+(int)Math.pow(2, godPlayerCurrentTimesDiamond)+" times");
        godPlayerDiamondScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        godPlayerDiamondScore.setEffect(ds);
        godPlayerDiamondScore.setCache(true);

        godPlayerHeartScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 1.5 * CARD_HEIGHT + 8 * MARGIN, "Heart: "+ godPlayerCurrentScoreHeart + "  "+(int)Math.pow(2, godPlayerCurrentTimesHeart)+" times");
        godPlayerHeartScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        godPlayerHeartScore.setEffect(ds);
        godPlayerHeartScore.setCache(true);

        godPlayerSpadeScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 1.5 * CARD_HEIGHT + 10 * MARGIN, "Spade: "+ godPlayerCurrentScoreSpade + "  "+(int)Math.pow(2, godPlayerCurrentTimesSpade)+" times");
        godPlayerSpadeScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        godPlayerSpadeScore.setEffect(ds);
        godPlayerSpadeScore.setCache(true);

        if(godPlayerCurrentScoreClub < 20){
            godPlayerClubScore.setFill(Color.LIGHTGRAY);
        }else if(godPlayerCurrentScoreClub >= 20){
            godPlayerClubScore.setFill(Color.rgb(232, 28, 72));
        }
        if(godPlayerCurrentScoreSpade < 20){
            godPlayerSpadeScore.setFill(Color.LIGHTGRAY);
        }else if(godPlayerCurrentScoreSpade >= 20){
            godPlayerSpadeScore.setFill(Color.rgb(232, 28, 72));
        }
        if(godPlayerCurrentScoreDiamond < 20){
            godPlayerDiamondScore.setFill(Color.LIGHTGRAY);
        }else if(godPlayerCurrentScoreClub >= 20){
            godPlayerDiamondScore.setFill(Color.rgb(232, 28, 72));
        }
        if(godPlayerCurrentScoreHeart < 20){
            godPlayerHeartScore.setFill(Color.LIGHTGRAY);
        }else if(godPlayerCurrentScoreSpade >= 20){
            godPlayerHeartScore.setFill(Color.rgb(232, 28, 72));
        }
        root.getChildren().add(godPlayerSpadeScore);
        root.getChildren().add(godPlayerHeartScore);
        root.getChildren().add(godPlayerClubScore);
        root.getChildren().add(godPlayerDiamondScore);
        root.getChildren().add(godPlayerScore);
    }
    //end of god player's run

    //initialise Human player's hand
    private void makeHumanPlayerHand(Group root) {
        for (int i = 0; i < myHandH.size(); i++) {
            humanPlayerHand[i] = new DraggableCards(myHandH.get(i), this);
            humanPlayerHand[i].setFitHeight(CARD_HEIGHT);
            humanPlayerHand[i].setFitWidth(CARD_WIDTH);
            setPosition(humanPlayerHand[i]);
            root.getChildren().add(humanPlayerHand[i]);}}
    //end of human player's hand

    //initialise God player's hand
    private void makeGodPlayerHand(Group root) {
        for (int i = 0; i < godPlayerHandSize; i++) {
            godPlayerHand[i] = new DraggableCards(myHandG.get(i), this);
            godPlayerHand[i].setFitHeight(CARD_HEIGHT);
            godPlayerHand[i].setFitWidth(CARD_WIDTH);
            setGodPosition(godPlayerHand[i]);
            root.getChildren().add(godPlayerHand[i]);}}
    //end of initialisation

    // building piles
    private void makePile (Group root){
        for (int i = 0; i < myPile.size(); i++){
            pile[i] = new DraggableCards(myPile.get(i), this);
            pile[i].setFitHeight(CARD_HEIGHT);
            pile[i].setFitWidth(CARD_WIDTH);
            setPosition(pile[i]);
            root.getChildren().add(pile[i]);
        }
    }
    //end of pile building

    //Deck
    //create de descriptive text for deck
    private void makeText(Group root) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        Text t1 = new Text(RUN_1_MAX_X + 0.7*CARD_WIDTH, RUN_1_MAX_Y + MARGIN * 7.7, "Deck");
        t1.setFont(Font.font(null, FontWeight.BOLD, 20));
        t1.setEffect(ds);
        t1.setCache(true);
        root.getChildren().add(t1);

        Text billboard = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + MARGIN, "Your score: ");
        billboard.setFont(Font.font(null, FontWeight.BOLD, 20));
        billboard.setEffect(ds);
        billboard.setCache(true);
        billboard.setFill(Color.WHEAT);
        root.getChildren().add(billboard);

        Text bill = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 1.5 * CARD_HEIGHT + MARGIN, "Computer score: ");
        bill.setFont(Font.font(null, FontWeight.BOLD, 20));
        bill.setEffect(ds);
        bill.setCache(true);
        bill.setFill(Color.WHEAT);
        root.getChildren().add(bill);
    }
    //create invisible card back indicating where to click and show deck
    private void flippedtoShowDeck(Group root) {
        Image image = new Image("/resources/images/card_back.png");
        ImagePattern imagePattern = new ImagePattern(image);
        deck.setFill(imagePattern);
        deck.setWidth(CARD_WIDTH);
        deck.setHeight(CARD_HEIGHT);
        deck.setLayoutX(RUN_1_MAX_X + 0.5*CARD_WIDTH);
        deck.setLayoutY(RUN_1_MAX_Y - CARD_HEIGHT + MARGIN * 5);
        deck.setOnMouseClicked(event -> {
            if(!isDrawfromPile){//disable flip function, flip only player is not draw from pile
                makeCards_Deck(root);
            }
        });
        root.getChildren().add(deck);
    }
    private int j= -1 ;
    private void makeCards_Deck(Group root) {
        if(j == -1){
            //to show the first card
        }
        else if(j < deckSize) {
            deckCard[j] = new DraggableCards(myDeck.get(j), this);
            deckCard[j].setFitHeight(CARD_HEIGHT);
            deckCard[j].setFitWidth(CARD_WIDTH);
            setPosition(deckCard[j]);
            root.getChildren().add(deckCard[j]);
            if(j == deckSize-1){
                deck.setOpacity(0);
            }
        }
        j++;
    }
    //end of deck building
    //end of the card building
    //calculate the current score
    private void reportHumanPlayerCurrentScore() {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        //total sum
        humanPlayerScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 12 * MARGIN,  "Total sum:   " + totalSumHuman);
        humanPlayerScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        humanPlayerScore.setEffect(ds);
        humanPlayerScore.setCache(true);
        humanPlayerScore.setFill(Color.rgb(232, 28, 72));

        humanPlayerClubScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 4 * MARGIN, "Club: "+ humanPlayerCurrentScoreClub + "  "+(int)Math.pow(2, humanPlayerCurrentTimesClub)+" times");
        humanPlayerClubScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        humanPlayerClubScore.setEffect(ds);
        humanPlayerClubScore.setCache(true);

        humanPlayerDiamondScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 6 * MARGIN, "Diamond: "+ humanPlayerCurrentScoreDiamond + "  "+(int)Math.pow(2, humanPlayerCurrentTimesDiamond)+" times");
        humanPlayerDiamondScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        humanPlayerDiamondScore.setEffect(ds);
        humanPlayerDiamondScore.setCache(true);

        humanPlayerHeartScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 8 * MARGIN, "Heart: "+ humanPlayerCurrentScoreHeart + "  "+(int)Math.pow(2, humanPlayerCurrentTimesHeart)+" times");
        humanPlayerHeartScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        humanPlayerHeartScore.setEffect(ds);
        humanPlayerHeartScore.setCache(true);

        humanPlayerSpadeScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 10 * MARGIN, "Spade: "+ humanPlayerCurrentScoreSpade + "  "+(int)Math.pow(2, humanPlayerCurrentTimesSpade)+" times");
        humanPlayerSpadeScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        humanPlayerSpadeScore.setEffect(ds);
        humanPlayerSpadeScore.setCache(true);

        if(humanPlayerCurrentTimesSpade == 1){
            humanPlayerSpadeScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 10 * MARGIN,  "Spade: "+ humanPlayerCurrentScoreSpade + "   1 times");
        }else{
            humanPlayerSpadeScore = new Text(getXForSquare_1(4) + MARGIN + 4.6 * CARD_WIDTH, getYForSquare_1(4) + 10 * MARGIN,  "Spade: "+ humanPlayerCurrentScoreSpade + "  "+(int)Math.pow(2, humanPlayerCurrentTimesSpade)+" times");
        }
        humanPlayerSpadeScore.setFont(Font.font(null, FontWeight.BOLD, 15));
        humanPlayerSpadeScore.setEffect(ds);
        humanPlayerSpadeScore.setCache(true);
        if(humanPlayerCurrentScoreClub < 20){
            humanPlayerClubScore.setFill(Color.LIGHTGRAY);
        }else if(humanPlayerCurrentScoreClub >= 20){
            humanPlayerClubScore.setFill(Color.rgb(232, 28, 72));
        }
        if(humanPlayerCurrentScoreSpade < 20){
            humanPlayerSpadeScore.setFill(Color.LIGHTGRAY);
        }else if(humanPlayerCurrentScoreSpade >= 20){
            humanPlayerSpadeScore.setFill(Color.rgb(232, 28, 72));
        }
        if(humanPlayerCurrentScoreDiamond < 20){
            humanPlayerDiamondScore.setFill(Color.LIGHTGRAY);
        }else if(humanPlayerCurrentScoreClub >= 20){
            humanPlayerDiamondScore.setFill(Color.rgb(232, 28, 72));
        }
        if(humanPlayerCurrentScoreHeart < 20){
            humanPlayerHeartScore.setFill(Color.LIGHTGRAY);
        }else if(humanPlayerCurrentScoreSpade >= 20){
            humanPlayerHeartScore.setFill(Color.rgb(232, 28, 72));
        }
        root.getChildren().add(humanPlayerSpadeScore);
        root.getChildren().add(humanPlayerHeartScore);
        root.getChildren().add(humanPlayerClubScore);
        root.getChildren().add(humanPlayerDiamondScore);
        root.getChildren().add(humanPlayerScore);
    }
    //check the end of game
    private boolean isGameOver() {
        boolean flag = false;
        Card firstclub = null;
        Card firstdiamond = null;
        Card firstspade = null;
        Card firstheart = null;
        Card runclub = null;
        Card rundiamond = null;
        Card runspade = null;
        Card runheart = null;
        Card handclub = null;
        Card handdiamond = null;
        Card handspade = null;
        Card handheart = null;
        for(int i = 0; i<myPile.size();i++){
            if(myPile.get(i) != null) {
                switch (myPile.get(i).getMySuit()) {
                    case "DIAMONDS":
                        firstdiamond = myPile.get(i);
                        break;
                    case "SPADES":
                        firstspade = myPile.get(i);
                        break;
                    case "CLUBS":
                        firstclub = myPile.get(i);
                        break;
                    case "HEARTS":
                        firstheart = myPile.get(i);
                }
            }
        }
        for(int i = 0; i<4;i++){
            if(humanRun.get(i) != null) {
                switch (humanRun.get(i).getMySuit()) {
                    case "DIAMONDS":
                        rundiamond = humanRun.get(i);
                        break;
                    case "SPADES":
                        runspade = humanRun.get(i);
                        break;
                    case "CLUBS":
                        runclub = humanRun.get(i);
                        break;
                    case "HEARTS":
                        runheart = humanRun.get(i);
                }
            }
        }
        for(int i = 0; i<7;i++){
            if(myHandH.get(i) != null ) {
                switch (myHandH.get(i).getMySuit()) {
                    case "DIAMONDS":
                        handdiamond = myHandH.get(i);
                        break;
                    case "SPADES":
                        handspade = myHandH.get(i);
                        break;
                    case "CLUBS":
                        handclub = myHandH.get(i);
                        break;
                    case "HEARTS":
                        handheart = myHandH.get(i);
                }
            }
        }
        //exhaustion
        if(currentDeckSizeAfterHumanPlay == 0 && currentPileSize == 0)
            flag = true;
        boolean isForcetoDiscard = true;
        if(firstclub != null && runclub != null && handclub != null){
            isForcetoDiscard = isForcetoDiscard && firstclub.getMyNumber() < runclub.getMyNumber()  && handclub.getMyNumber() < runclub.getMyNumber();
        }
        if(firstdiamond != null && rundiamond != null && handdiamond != null) {
            isForcetoDiscard = isForcetoDiscard && firstdiamond.getMyNumber() < rundiamond.getMyNumber() && handdiamond.getMyNumber() < rundiamond.getMyNumber();
        }
        if(firstheart != null && runheart != null && handheart != null){
            isForcetoDiscard = isForcetoDiscard && firstheart.getMyNumber() < runheart.getMyNumber() && handheart.getMyNumber() < runheart.getMyNumber();
        }
        if(firstspade != null && runspade != null && handheart != null){
            isForcetoDiscard = isForcetoDiscard &&  firstspade.getMyNumber() < runspade.getMyNumber() && handspade.getMyNumber() < runspade.getMyNumber();
        }
        //lack of progress
        if(currentDeckSizeBeforeHumanPlay == 0 && isForcetoDiscard && discardCount == 3){
            flag = true;
        }
        return flag;
    }

    //to store the previous card and compare
    private int previousSpade = 0;
    private int previousDiamond = 0;
    private int previousClub = 0;
    private int previousHeart = 0;

    //calculate the human run score after each movement
    public void calculateHumanRunScore() {
        currentDeckSizeAfterHumanPlay = myDeck.size();
        currentPileSize = myPile.size();
        //calculate the score
        for(int i = 0; i < 4; i++) {
            if (humanRun.get(i) != null) {
                switch (humanRun.get(i).getMySuit()) {
                    case "DIAMONDS":
                        if (previousDiamond != humanRun.get(i).getMyNumber()) {
                            if (humanRun.get(i).getMyNumber() > 10) {
                                humanPlayerCurrentTimesDiamond += 1;
                            } else {
                                humanPlayerCurrentScoreDiamond += humanRun.get(i).getMyNumber();
                            }
                            previousDiamond = humanRun.get(i).getMyNumber();
                            break;
                        }
                    case "SPADES":
                        if (previousSpade != humanRun.get(i).getMyNumber()) {
                            if (humanRun.get(i).getMyNumber() > 10) {
                                humanPlayerCurrentTimesSpade += 1;
                            } else {
                                humanPlayerCurrentScoreSpade += humanRun.get(i).getMyNumber();
                            }
                            previousSpade = humanRun.get(i).getMyNumber();
                        }
                        break;
                    case "CLUBS":
                        if (previousClub != humanRun.get(i).getMyNumber()) {
                            if (humanRun.get(i).getMyNumber() > 10) {
                                humanPlayerCurrentTimesClub += 1;
                            } else {
                                humanPlayerCurrentScoreClub += humanRun.get(i).getMyNumber();
                            }
                            previousClub = humanRun.get(i).getMyNumber();
                        }
                        break;
                    case "HEARTS":
                        if (previousHeart != humanRun.get(i).getMyNumber()) {
                            if (humanRun.get(i).getMyNumber() > 10) {
                                humanPlayerCurrentTimesHeart += 1;
                            } else {
                                humanPlayerCurrentScoreHeart += humanRun.get(i).getMyNumber();
                            }
                            previousHeart = humanRun.get(i).getMyNumber();
                        }
                        break;
                }
            }
        }
        totalSumHuman = sum(humanPlayerCurrentTimesClub, humanPlayerCurrentScoreClub) + sum(humanPlayerCurrentTimesDiamond, humanPlayerCurrentScoreDiamond)
                + sum(humanPlayerCurrentTimesHeart, humanPlayerCurrentScoreHeart) + sum(humanPlayerCurrentTimesSpade, humanPlayerCurrentScoreSpade);
    }

    private int beforeSpade = 0;
    private int beforeDiamond = 0;
    private int beforeClub = 0;
    private int beforeHeart = 0;
    //calculate the god run score after each movement
    public void calculateGodRunScore() {
        currentPileSize = myPile.size();
        //calculate the score
        for(int i = 0; i < 4; i++) {
            if (godRun.get(i) != null) {
                switch (godRun.get(i).getMySuit()) {
                    case "DIAMONDS":
                        if (beforeDiamond != godRun.get(i).getMyNumber()) {
                            if (godRun.get(i).getMyNumber() > 10) {
                                godPlayerCurrentTimesDiamond += 1;
                            } else {
                                godPlayerCurrentScoreDiamond += godRun.get(i).getMyNumber();
                            }
                            beforeDiamond = godRun.get(i).getMyNumber();
                            break;
                        }
                    case "SPADES":
                        if (beforeSpade != godRun.get(i).getMyNumber()) {
                            if (godRun.get(i).getMyNumber() > 10) {
                                godPlayerCurrentTimesSpade += 1;
                            } else {
                                godPlayerCurrentScoreSpade += godRun.get(i).getMyNumber();
                            }
                            beforeSpade = godRun.get(i).getMyNumber();
                        }
                        break;
                    case "CLUBS":
                        if (beforeClub != godRun.get(i).getMyNumber()) {
                            if (godRun.get(i).getMyNumber() > 10) {
                                godPlayerCurrentTimesClub += 1;
                            } else {
                                godPlayerCurrentScoreClub += godRun.get(i).getMyNumber();
                            }
                            beforeClub = godRun.get(i).getMyNumber();
                        }
                        break;
                    case "HEARTS":
                        if (beforeHeart != godRun.get(i).getMyNumber()) {
                            if (godRun.get(i).getMyNumber() > 10) {
                                godPlayerCurrentTimesHeart += 1;
                            } else {
                                godPlayerCurrentScoreHeart += godRun.get(i).getMyNumber();
                            }
                            beforeHeart = godRun.get(i).getMyNumber();
                        }
                        break;
                }
            }
        }
        totalSumGod = sum(godPlayerCurrentTimesClub, godPlayerCurrentScoreClub) + sum(godPlayerCurrentTimesDiamond, godPlayerCurrentScoreDiamond)
                + sum(godPlayerCurrentTimesHeart, godPlayerCurrentScoreHeart) + sum(godPlayerCurrentTimesSpade, godPlayerCurrentScoreSpade);
    }


    //judge the validity of human player's movement
    //think before you do, there is invalid to undo what you have done
    public boolean isValidMove() {
        boolean flag = false;
        if(drawFromHand == 1 && drawFromPile == 1 && addToHand == 1 && addToPile == 0 && drawFromDeck == 0){
            flag = true;
        }
        if(drawFromHand == 1 && drawFromPile == 0 && addToHand == 1 && addToPile == 0 && drawFromDeck == 1){
            currentDeckSizeBeforeHumanPlay = currentDeckSizeAfterHumanPlay;
            flag = true;
        }
        if(drawFromHand == 1 && drawFromPile == 0 && addToHand == 1 && addToPile == 1 && drawFromDeck == 2 ){
            currentDeckSizeBeforeHumanPlay = currentDeckSizeAfterHumanPlay;
            flag = true;
        }
        if(drawFromHand == 0  && drawFromPile == 1 && addToHand == 0 && addToPile == 0 && drawFromDeck == 0 ) {
            flag = true;
        }
        if(drawFromHand == 0  && drawFromPile == 0 && addToHand == 0 && addToPile == 0 && drawFromDeck == 1 ) {
            currentDeckSizeBeforeHumanPlay = currentDeckSizeAfterHumanPlay;
            flag = true;
        }
        if(drawFromHand == 0  && drawFromPile == 0 && addToHand == 0 && addToPile == 0 && drawFromDeck == 0 ) {
            discardCount += 1;
            flag = true;
        }
        calculateHumanRunScore();
        return flag;
    }

    // Highlight effect for grid of run
    void highlightNearestSquare(DraggableCards card) {
        int nearest = nearestSquareRun(card);
        for (int i = 0; i < humanPlayerRun.length; i++) {
            humanPlayerRun[i].setOpacity((i == nearest) ? 0.5 : 1);
        }
    }

    //calculate the movement of player
    //update run, pile and hand, discard previous card after updating the run
    public void updateCardState(Card c, int target, ArrayList<Card> list, ArrayList<Card> add_list) {
        // remove card
        int currentPile = myPile.size();
        int currentHand = myHandH.size();
        int currentDeck = myDeck.size();
        ArrayList<Card> currentRun = humanRun;
        if (list.contains(c)) {
            list.remove(c);
        }else if (add_list.contains(c)){
            add_list.remove(c);
        }else {
            if (humanRun.indexOf(c) == -1) {
            } else {
                humanRun.set(humanRun.indexOf(c), null);
            }
        }

        // add card
        if (target == -1) {
            list.add(c);
        } else if (target == -2) {
            add_list.add(c);
        } else {
            if (humanRun.get(target) == null) {
                // make the same suit in same place
                boolean flag = true;
                for (int i = 0; i < humanRun.size(); i++) {
                    if (humanRun.get(i) != null && humanRun.get(i).getMySuit() == c.getMySuit()) {
                        flag = false;
                    }
                }// end of the check suit
                if (flag) {
                    humanRun.set(target, c);
                } else {
                    list.add(c);// if place start multiple runs of same suit
                }
            } else if (humanRun.get(target).getMySuit() == c.getMySuit()) {
                if (humanRun.get(target).getMyNumber() > 10) {// place face card
                    humanRun.set(target, c);
                } else {
                    if (humanRun.get(target).getMyNumber() < c.getMyNumber() && c.getMyNumber() <= 10) {
                        humanRun.set(target, c);
                    } else {
                        list.add(c);
                    }
                }
            } else {
                list.add(c);
            }
        }
        if(currentPile > myPile.size()){
            drawFromPile += 1;
            isDrawfromPile = true;
        }
        if(currentPile < myPile.size()){
            addToPile += 1;
        }
        if(currentHand > myHandH.size()){
            drawFromHand += 1;}
        if(currentHand < myHandH.size()){
            addToHand += 1;}
        if(currentDeck != myDeck.size()){
            drawFromDeck += 1;
        }
    }

    //deal the draggable movement to different cards
    public void attemptToMove_run(DraggableCards card) {
        int target = nearestSquareRun(card);
        updateCardState(card.card, target, myHandH, myPile);
        for (DraggableCards card1 : humanPlayerHand) {
            if (card1 != null) {
                setPosition(card1);
            }
        }
    }

    public void attemptToMove_pile(DraggableCards card) {
        int target = nearestSquarePile(card);
        updateCardState(card.card, target, myPile, myHandH);
        for (DraggableCards card1 : pile) {
            if (card1  != null){
                setPosition(card1);
            }
        }
    }

    public void attemptToMove_deck(DraggableCards card) {
        int target = nearestSquarePile(card);
        updateCardState(card.card, target, myDeck, myHandH);
        for (DraggableCards card1 : deckCard) {
            if(card1 != null)
                setPosition(card1);
        }
    }

    //check grid for in pile to place
    public int nearestSquarePile(DraggableCards card) {
        double centerx = card.getLayoutX() + (CARD_WIDTH / 2);
        double centery = card.getLayoutY() + (CARD_HEIGHT / 2);
        if (centerx >= 4 * MARGIN && centerx <= 12 * MARGIN + 6 * CARD_WIDTH 	&& centery >= MARGIN && centery <= MARGIN + CARD_HEIGHT) {
            return -2;
        } else if (centerx <= RUN_1_X_ORGIN || centerx >= RUN_1_MAX_X - 2.5 * CARD_WIDTH || centery <= RUN_1_Y_ORGIN || centery >= RUN_1_MAX_Y) {
            return -1;
        } else {
            int row = (int) (centerx - RUN_1_X_ORGIN - 0.5 * CARD_WIDTH)	/ CARD_WIDTH;
            return row;
        }
    }

    //check grid for in run to place
    public int nearestSquareRun(DraggableCards card) {
        double centerx = card.getLayoutX() + (CARD_WIDTH / 2);
        double centery = card.getLayoutY() + (CARD_HEIGHT / 2);
        if (centerx >= RUN_1_MAX_X - 7 * MARGIN	&& centerx <= RUN_1_MAX_X - 7 * MARGIN + CARD_WIDTH && centery >= 5 * MARGIN + RUN_1_MAX_Y - 2 * CARD_HEIGHT && centery <= 7 * MARGIN + RUN_1_MAX_Y + 2 * CARD_HEIGHT) {
            return -2;
        } else if (centerx <= RUN_1_X_ORGIN || centerx >= RUN_1_MAX_X - 2.5 * CARD_WIDTH || centery <= RUN_1_Y_ORGIN || centery >= RUN_1_MAX_Y) {
            return -1;
        } else {
            int row = (int) (centerx - RUN_1_X_ORGIN - 0.5 * CARD_WIDTH)	/ CARD_WIDTH;
            return row;
        }
    }
}


