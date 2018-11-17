import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris {
    // use the Brain to calculate the bestMove and store it on bestMove ivar
    private DefaultBrain db;
    private DefaultBrain.Move bestMove;
    // if the brainButton is selected, use bestMove to autoplay game
    private JCheckBox brainButton;
    private boolean brainMode;

    // animate Falling
    private JCheckBox fallButton;
    private boolean fallMode;

    // adversary
    private JSlider adversary;
    private JLabel statusLabel;

    private int xCount;

    public JBrainTetris(int pixels) {
        super(pixels);
        db = new DefaultBrain();
        brainMode = false;
        xCount = 0;
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();

        // brain control
        panel.add(new JLabel("Brain:"));
        brainButton = new JCheckBox("Brain active");
        panel.add(brainButton);

        // Animate Falling control
        fallButton = new JCheckBox("Animate Falling", true);
        panel.add(fallButton);

        // make a little panel
        JComponent little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        little.add(adversary);
        statusLabel = new JLabel(" ");
        panel.add(statusLabel);

        panel.add(little);
        return panel;
    }

    @Override
    public void tick(int verb) {
        brainMode = brainButton.isSelected();
        // is brainMode is not selected, use the common tick(verb)
        if (!brainMode || verb != DOWN) {
            super.tick(verb);
            return;
        }
        // else use brain and bestMove
        board.undo();
        updateBestMove();
        fallMode = fallButton.isSelected();
        if (bestMove == null) {
            stopGame();
        }
        if (bestMove.piece.equals(currentPiece)) {
            currentPiece = currentPiece.fastRotation();
        }
        if (bestMove.x < currentX) {
            super.tick(LEFT);
        }
        else if (bestMove.x > currentX) {
            super.tick(RIGHT);
        }
        else if (fallMode){
            super.tick(DROP);
            // the defaultBrain has problem, so I have to add this DOWN command
            super.tick(DOWN);
        }
        super.tick(DOWN);
    }

    private void updateBestMove() {
        // if no new piece has been added
        // do nothing
        if (count <= xCount) {
            return;
        }
        // a new piece has been added
        assert(count == xCount + 1);
        assert(currentPiece != null);
        bestMove = db.bestMove(board, currentPiece, HEIGHT, bestMove);
        xCount += 1;
    }

    @Override
    public Piece pickNextPiece() {
        int advNum = adversary.getValue();
        int randNum = random.nextInt(99) + 1;
        if (randNum >= advNum) {
            statusLabel.setText("ok");
            return super.pickNextPiece();
        }
        else {
            statusLabel.setText("*ok*");
            return pickWorstPiece();
        }
    }

    private Piece pickWorstPiece() {
        Piece worstPiece = null;
        int pieceScore = 0;
        DefaultBrain.Move move = new DefaultBrain.Move();
        for (Piece p: pieces) {
            move = db.bestMove(board, p, HEIGHT, move);
            if (move.score > pieceScore) {
                worstPiece = move.piece;
            }
        }
        return worstPiece;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

}
