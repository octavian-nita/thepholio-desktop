package org.thepholio.desktop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX Welcome");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //grid.setGridLinesVisible(true);

        Text sceneTitle = new Text("Welcome");
        sceneTitle.setId("welcome");
        sceneTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 24));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label usernameL = new Label("User Name:");
        grid.add(usernameL, 0, 1);

        TextField usernameF = new TextField();
        grid.add(usernameF, 1, 1);

        Label passwordL = new Label("Password:");
        grid.add(passwordL, 0, 2);

        PasswordField passwordF = new PasswordField();
        grid.add(passwordF, 1, 2);

        Button signInB = new Button("Sign in");
        HBox signInHB = new HBox(10);
        signInHB.setAlignment(Pos.BOTTOM_RIGHT);
        signInHB.getChildren().add(signInB);
        grid.add(signInHB, 1, 4);

        Text targetT = new Text();
        targetT.setId("target");
        grid.add(targetT, 1, 6);

        signInB.setOnAction(e -> {
            targetT.setFill(Color.FIREBRICK);
            targetT.setText(">> Sign in button pressed.");
        });

        Scene scene = new Scene(grid, 300, 275);
        scene.getStylesheets().add(getClass().getResource("/org/thepholio/desktop/theme.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
