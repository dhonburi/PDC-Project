
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class Controller implements Game {

    private final Model model;
    private final View view;

    private String latestInput = "";
    private boolean inputReady = false;

    private boolean gameInProgress;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        model.addListener(view);
        model.updateStats();

        setUpKeyboardListeners();
        setUpOnscreenKeyboardListeners();
        setUpButtonListeners();
    }

    private void setUpKeyboardListeners() {
        view.registerKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    view.showCard("GAME");
                }
                if (view.typingEnabled && gameInProgress) {
                    char c = e.getKeyChar();
                    if (Character.isLetter(c)) {
                        c = Character.toUpperCase(c);
                        model.addLetter(c);
                        view.updateTileText();
                    } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        model.removeLastLetter();
                        view.updateTileText();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        latestInput = model.getCurrentWord();
                        inputReady = true;
                        synchronized (Controller.this) {
                            Controller.this.notify();
                        }
                    }
                }
            }
        });
    }

    private void setUpOnscreenKeyboardListeners() {
        for (char c = 'A'; c <= 'Z'; c++) {
            final char keyChar = c;
            view.registerKeyButtonListener(c, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (view.typingEnabled && gameInProgress) {
                        model.addLetter(keyChar);
                        view.updateTileText();
                    }
                }
            });
        }

        view.registerKeyButtonListener('-', new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.typingEnabled && gameInProgress) {
                    model.removeLastLetter();
                    view.updateTileText();
                }
            }
        });

        view.registerKeyButtonListener('+', new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.typingEnabled && gameInProgress) {
                    latestInput = model.getCurrentWord();
                    inputReady = true;
                    synchronized (Controller.this) {
                        Controller.this.notify();
                    }
                }
            }
        });
    }

    private void setUpButtonListeners() {
        view.registerTutorialButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showCard("TUTORIAL");
            }
        });

        view.registerCloseButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showCard("GAME");
            }
        });

        view.registerStatsButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readStats();
            }
        });

        view.registerReplayButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameInProgress) {
                    startGame();
                    view.showCard("GAME");
                }
            }
        });
    }

    public String getUserInput() {
        inputReady = false;
        try {
            synchronized (this) {
                while (!inputReady) {
                    this.wait();
                }
            }
        } catch (InterruptedException e) {
        }
        return latestInput;
    }

    @Override
    public void startGame() {
        gameInProgress = true;

        GameLoop game = new GameLoop(model, view, this);
        Thread thread = new Thread(game);

        thread.start();

    }

    public void gameOver() {
        gameInProgress = false;

        try {
            Thread.sleep(2100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        readStats();
        view.showReplay();
    }

    @Override
    public void readStats() {
        model.updateStats();
        view.updateStats();
        view.showCard("STATS");
    }
}
