import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class App extends JFrame {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem saveRaport, exitApp;
    private JScrollPane scrollRaportArea;
    private JTextArea raportArea;
    private JPanel bottomPanel;
    private JButton generateButton;
    private JProgressBar progressBar;


    App() {
        setTitle("INF.04 - Generator Raportów");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        menu = new JMenu("Plik");

        saveRaport = new JMenuItem("Zapisz Raport");

        saveRaport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        saveRaport.addActionListener(e -> saveReport());

        exitApp = new JMenuItem("Wyjście");

        exitApp.addActionListener(e -> {System.exit(0);});

        menu.add(saveRaport);
        menu.addSeparator();
        menu.add(exitApp);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        raportArea = new JTextArea();

        scrollRaportArea = new JScrollPane(raportArea);

        add(scrollRaportArea, BorderLayout.CENTER);

        generateButton = new JButton("Generuj Raport w Tle");

        generateButton.addActionListener(e -> generateReport());

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        bottomPanel = new JPanel(new FlowLayout());

        bottomPanel.add(generateButton);
        bottomPanel.add(progressBar);

        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generateReport() {

        generateButton.setEnabled(false);

        raportArea.setText("");

        progressBar.setValue(0);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {

                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(300);
                    String reportLine = "Wiersz  nr " + i + "\n";

                    publish(i);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {

                int latestValue = chunks.get(chunks.size() - 1);

                progressBar.setValue(latestValue * 10);

                raportArea.append("Wiersz nr " + latestValue + "\n");
            }

            @Override
            protected void done() {
                generateButton.setEnabled(true);
                progressBar.setValue(100);
                JOptionPane.showMessageDialog(App.this, "Raport został wygenerowany.");
            }
        };

        worker.execute();
    }

    private void saveReport() {
        if (raportArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Raport jest pusty!!!", "Ostrzeżenie", JOptionPane.WARNING_MESSAGE);

            return;
        }

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Zapisz raport");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(raportArea.getText());

                JOptionPane.showMessageDialog(this, "Raport został zapisany.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "BŁĄD!!!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}

