package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    private Timer tmTime;
    private DefaultTableModel modelTable;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Network.myGUI = new GUI();


    }

    public GUI() {
        JFrame form = new JFrame("Network Simulation");
        form.setResizable(false);

        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(pnlMain);
        form.pack();
        form.setVisible(true);

        pnlProgressBar.setLayout(new BoxLayout(pnlProgressBar, BoxLayout.Y_AXIS));

        tmTime = new Timer(50, e -> lblTime.setText(Network.getTimeStampFormatted()));
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
    }

    private void btnSimManager_clicked() {
        if (btnSimManager.getText().equals("Start Simulation")) {
            try {
                startSimulation((Integer) spnCapacity.getValue());
            } catch (Exception ex) {
                showMessageDialog(null, ex.getMessage());
                ex.printStackTrace();
            }
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

    public void startSimulation(int connections) throws Exception {
        Network.startSimulation(connections);
        btnSimManager.setText("End Simulation");
        txtLog.setText("");

        this.pnlProgressBar.removeAll();
        this.pnlProgressBar.revalidate();

        modelTable.getDataVector().removeAllElements();
        modelTable.fireTableDataChanged();
    }

    public void endSimulation() {
        btnSimManager.setText("Start Simulation");
        Network.clearData();
        updateQueue(Network.getDeviceQueue());
    }

    public void updateQueue(Vector<Device> curr) {
        if (curr != null) {
            lstInQueue.setListData(curr);
        } else {
            DefaultListModel listModel = (DefaultListModel) lstInQueue.getModel();
            listModel.removeAllElements();
        }
    }

    public void log(String[] data) {
        modelTable.addRow(data);
    }


    ProgressManager addProgressBar(String name) {
        return new ProgressManager(pnlProgressBar, name);
    }


    public static class ProgressManager implements Device.ProgressKeeper {
        private final JPanel panelArea;
        private final JPanel panel;
        private final JProgressBar progressBar;
        private final JLabel label;

        private ProgressManager(JPanel panelArea, String Title) {
            this.panelArea = panelArea;
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.X_AXIS));


            this.progressBar = new JProgressBar(0, 100);
            this.label = new JLabel(Title + ": ");

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

        public void setValue(int v) {
            this.progressBar.setValue(v);
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
