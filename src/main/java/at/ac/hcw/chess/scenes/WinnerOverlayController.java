package at.ac.hcw.chess.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WinnerOverlayController {

    @FXML private StackPane winnerOverlay;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button restartBtn;
    @FXML private Button backToMenuBtn;

    private Runnable onRestart;
    private Runnable onBackToMenu;

    @FXML
    private void initialize() {
        // Overlay blockt automatisch Klicks im Hintergrund, sobald sichtbar
        winnerOverlay.setPickOnBounds(true);
    }

    public void showWinner(String winnerName) {
        titleLabel.setText("CHECKMATE");
        subtitleLabel.setText(winnerName + " wins!");
        winnerOverlay.setVisible(true);
        winnerOverlay.setManaged(true);
    }


    public void hide() {
        winnerOverlay.setVisible(false);
        winnerOverlay.setManaged(false);
    }

    public void setOnRestart(Runnable onRestart) {
        this.onRestart = onRestart;
        restartBtn.setOnAction(e -> {
            if (this.onRestart != null) this.onRestart.run();
        });
    }

    public void setOnBackToMenu(Runnable onBackToMenu) {
        this.onBackToMenu = onBackToMenu;
        backToMenuBtn.setOnAction(e -> {
            if (this.onBackToMenu != null) this.onBackToMenu.run();
        });
    }
}
