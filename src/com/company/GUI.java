package com.company;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private JPanel pnlMain;
    private JButton btnSaveLog;
    private JList lstInQueue;
    private JTextField txtName;
    private JComboBox cmbType;
    private JButton btnEnqueue;
    private JSpinner spnTime;
    private JTable tblLog;
    private JTextPane txtLog;
    private JLabel lblTime;
    private JPanel pnlProgressBar;
    private JSpinner spinner1;
    private JButton btnReset;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        GUI myGUI = new GUI();
        myGUI.addProgressBar("ABC");
        myGUI.addProgressBar("CDEEEEEE");
        myGUI.addProgressBar("aaa");
        myGUI.addProgressBar("aaa");
        myGUI.addProgressBar("aaa");
        myGUI.addProgressBar("aaa");
        myGUI.addProgressBar("aaa");


    }

    public GUI() {
        JFrame form = new JFrame("Network Simulation");
        form.setResizable(false);

        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(pnlMain);
        form.pack();
        form.setVisible(true);

        pnlProgressBar.setLayout(new BoxLayout(pnlProgressBar, BoxLayout.Y_AXIS));
    }

    private ProgressManager addProgressBar(String name) {
        return new ProgressManager(pnlProgressBar, name);
    }

    static class ProgressManager {
        private final JPanel panelArea;
        private final JPanel panel;
        private final JProgressBar progressBar;
        private final JLabel label;

        ProgressManager(JPanel panelArea, String Title) {
            this.panelArea = panelArea;
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.X_AXIS));


            this.progressBar = new JProgressBar(0, 100);
            this.label = new JLabel(Title + ": ");
            progressBar.setValue(50);

            this.panel.add(this.label);
            this.panel.add(this.progressBar);

            Dimension progressDimension = new Dimension(new Dimension(this.panelArea.getWidth(), 25));
            Dimension panelDimension = new Dimension(new Dimension(this.panelArea.getWidth() - 10, 30));
            Dimension labelDimension = new Dimension(100, 25);

            this.label.setMinimumSize(labelDimension);
            this.label.setMaximumSize(labelDimension);
            this.label.setPreferredSize(labelDimension);

            this.progressBar.setMaximumSize(progressDimension);
            this.progressBar.setPreferredSize(progressDimension);

            this.panel.setMaximumSize(panelDimension);
            this.panel.setPreferredSize(panelDimension);

            panelArea.add(this.panel);
            panelArea.revalidate();
        }

        void destroy() {
            panelArea.remove(this.panel);
            this.panel.removeAll();
            panelArea.revalidate();
        }
    }
}
