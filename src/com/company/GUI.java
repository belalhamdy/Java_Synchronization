package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private DefaultTableModel dtm;

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

        dtm = new DefaultTableModel(null, new String[]{"Name", "Type", "Arrive Time", "Start Time", "Leave Time"});
        tblLog.setModel(dtm);

        ((SpinnerNumberModel) this.spnTime.getModel()).setMinimum(0);
        this.spnCapacity.getModel().setValue(1);
        ((SpinnerNumberModel) this.spnCapacity.getModel()).setMinimum(1);

        cmbType.setModel(new DefaultComboBoxModel<>(Device.Type.toArray()));

        btnEnqueue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                if (name.equals("")) {
                    showMessageDialog(null, "Name can't be empty");
                    return;
                }
                if (name.length()>25){
                    showMessageDialog(null, "Name is too large");
                    txtName.setText("");
                    return;
                }
                int type = cmbType.getSelectedIndex() + 1;
                int time = (Integer) spnTime.getValue();
                Network.addDevice(name, type, time);
                updateQueue(Network.getDeviceQueue());
            }
        });
        btnSimManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(btnSimManager.getText().equals("Start Simulation")){
                    try {
                        startSimulation((Integer) spnCapacity.getValue());
                    } catch (Exception ex) {
                        showMessageDialog(null, ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                else
                {
                    endSimulation();
                }
            }
        });
    }

    public void startSimulation(int connections) throws Exception {
        Network.startSimulation(connections);
        btnSimManager.setText("End Simulation");
        txtLog.setText("");
        this.pnlProgressBar.removeAll();
        this.pnlProgressBar.revalidate();
        dtm = new DefaultTableModel(null, new String[]{"Name", "Type", "Arrive Time", "Start Time", "Leave Time"});
        tblLog.setModel(dtm);
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
        dtm.addRow(data);
    }


    ProgressManager addProgressBar(String name) {
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

        void setValue(int v) {
            this.progressBar.setValue(v);
        }

        void destroy() {
            panelArea.remove(this.panel);
            this.panel.removeAll();
            panelArea.revalidate();
        }
    }
}
