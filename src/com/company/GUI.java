package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import static javax.swing.JOptionPane.showMessageDialog;

public class GUI {

    private JPanel pnlMain;
    private JButton btnSaveLog;
    private JList<Device> lstInQueue;
    private JTextField txtName;
    private JComboBox<String> cmbType;
    private JButton btnEnqueue;
    private JSpinner spnTime;
    private JTable tblLog;
    private JTextPane txtLog;
    private JLabel lblTime;
    private JPanel pnlProgressBar;
    private JSpinner spnCapacity;
    private JButton btnSimManager;

    private DefaultTableModel modelTable;

    public GUI() {
        JFrame form = new JFrame("Network Simulation");
        form.setResizable(false);

        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(pnlMain);
        form.pack();
        form.setVisible(true);

        pnlProgressBar.setLayout(new BoxLayout(pnlProgressBar, BoxLayout.Y_AXIS));

        Timer tmTime = new Timer(50, e -> lblTime.setText(Network.getTimeStampFormatted()));
        tmTime.start();

        modelTable = new DefaultTableModel(null, new String[]{"Name", "Type", "Arrive Time", "Start Time", "Leave Time"});
        tblLog.setModel(modelTable);

        SpinnerNumberModel modelSpinner;
        //Set up spnCapacity
        modelSpinner = (SpinnerNumberModel) spnCapacity.getModel();
        modelSpinner.setMinimum(1);
        modelSpinner.setValue(1);
        //Set up spnTime
        modelSpinner = (SpinnerNumberModel) spnTime.getModel();
        modelSpinner.setMinimum(0);
        modelSpinner.setStepSize(100);

        cmbType.setModel(new DefaultComboBoxModel<>(Device.Type.toArray()));

        //Action Listeners for buttons.
        btnEnqueue.addActionListener(e -> btnEnqueue_clicked());
        btnSimManager.addActionListener(e -> btnSimManager_clicked());
        btnSaveLog.addActionListener(e -> btnSaveLog_clicked());
    }

    private void btnSaveLog_clicked() {
        JFileChooser chooser = new JFileChooser();
        int response = chooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(chooser.getSelectedFile()), StandardCharsets.UTF_8));
                out.write(txtLog.getText());
                out.close();
                showMessageDialog(null, "Log saved successfully!");
            } catch (Exception ex) {
                showMessageDialog(null, ex.getLocalizedMessage());
            }
        }
    }

    private void btnSimManager_clicked() {
        if (btnSimManager.getText().equals("Start Simulation")) {
            startSimulation();
        } else {
            endSimulation();
        }
    }

    private void btnEnqueue_clicked() {
        String name = txtName.getText();
        if (name.equals("")) {
            showMessageDialog(null, "Name can't be empty");
            return;
        }
        if (name.length() > 25) {
            showMessageDialog(null, "Name is too large");
            txtName.selectAll();
            return;
        }
        int type = cmbType.getSelectedIndex() + 1;
        int time = (int) spnTime.getValue();
        Network.addDevice(name, type, time);
        updateQueue(Network.getDeviceQueue());
    }

    private void startSimulation() {
        int connections = (int) spnCapacity.getValue();
        Network.startSimulation(connections);

        spnCapacity.setEnabled(false);
        btnSimManager.setText("End Simulation");
        txtLog.setText("");

        modelTable.getDataVector().removeAllElements();
        modelTable.fireTableDataChanged();
    }

    private void endSimulation() {
        Network.endSimulation();

        this.pnlProgressBar.removeAll();

        spnCapacity.setEnabled(true);
        btnSimManager.setText("Start Simulation");
        updateQueue(Network.getDeviceQueue());

        this.pnlProgressBar.revalidate();

    }

    public void updateQueue(Vector<Device> curr) {
        if (curr != null) {
            lstInQueue.setListData(curr);
        } else {
            DefaultListModel listModel = (DefaultListModel) lstInQueue.getModel();
            listModel.removeAllElements();
        }
    }

    public synchronized void logInConsole(String data) {
        System.out.println(data);
        txtLog.setText(txtLog.getText() + data + '\n');
    }

    public synchronized void logInTable(String[] data) {
        modelTable.addRow(data);
    }


    ProgressManager addProgressBar(String name) {
        return new ProgressManager(pnlProgressBar, name);
    }


    public static class ProgressManager implements Device.ProgressKeeper {
        private final JProgressBar progressBar;

        private ProgressManager(JPanel panelArea, String Title) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));


            this.progressBar = new JProgressBar(0, 100);
            JLabel label = new JLabel(Title + ": ");

            panel.add(label);
            panel.add(this.progressBar);

            Dimension progressDimension = new Dimension(new Dimension(panelArea.getWidth(), 25));
            Dimension panelDimension = new Dimension(new Dimension(panelArea.getWidth() - 10, 30));
            Dimension labelDimension = new Dimension(100, 25);

            label.setMinimumSize(labelDimension);
            label.setMaximumSize(labelDimension);
            label.setPreferredSize(labelDimension);

            this.progressBar.setMaximumSize(progressDimension);
            this.progressBar.setPreferredSize(progressDimension);

            panel.setMaximumSize(panelDimension);
            panel.setPreferredSize(panelDimension);

            panelArea.add(panel);
            panelArea.revalidate();
        }

        public void setValue(int v) {
            this.progressBar.setValue(v);
            this.progressBar.revalidate();
        }

        /*
        void destroy() {
            panelArea.remove(this.panel);
            this.panel.removeAll();
            panelArea.revalidate();
        }
        */
    }
}
