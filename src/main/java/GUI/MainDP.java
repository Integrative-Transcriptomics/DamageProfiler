package GUI;

import IO.Communicator;
import calculations.StartCalculations;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by neukamm on 01.09.2016.
 */
public class MainDP {


    private JButton selectInputFileButton;
    private JButton selectReferenceButton;
    private JButton selectOutputButton;
    private JCheckBox rescaleCheckBox;
    private JCheckBox useAllMappedReadsCheckBox;
    private JButton rescaleAdvancedButton;
    private JPanel masterpanel;
    private JButton runButton;
    private JTextField threshold;
    private JTextField length;
    private JButton plottingOptionsButton;
    private JTextField specie_input;
    private JLabel specie_label;

    private static JFrame frame;


    private Communicator communicator;

    public static void main(String[] args) throws Exception {

    }

    public MainDP(Communicator c, StartCalculations starter, String version) throws Exception {

        frame = new JFrame("DamageProfiler v" + version);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setContentPane(masterpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(d.width / 6, d.height / 4));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        checkBoxes();
        runButton.setEnabled(false);


        communicator = c;
        checkBoxes();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setValues(communicator);

        /**
         * Listener Section
         */

        selectInputFileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);    //To change body of overridden methods use File | Settings | File Templates.
                selectInputFileButton.setToolTipText("Specify SAM/BAM input file. Zipped SAM file is possible as well.");
            }
        });
        selectReferenceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);    //To change body of overridden methods use File | Settings | File Templates.
                selectReferenceButton.setToolTipText("Specify reference file in fastA format.");
            }
        });
        selectOutputButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);    //To change body of overridden methods use File | Settings | File Templates.
                selectOutputButton.setToolTipText("Specify output directory where all results will be stored.");
            }
        });
        runButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);    //To change body of overridden methods use File | Settings | File Templates.
                runButton.setToolTipText("Start DamageProfiler. Enabled, if input file, reference and result directory are specified.");
            }
        });


        /**
         *   Action Listener Section
         */

        rescaleAdvancedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                RescaleAdvanced ra = new RescaleAdvanced(communicator);
                //setWindowPosition(ra);
                ra.setSize(600, 400);
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
                ra.setLocation(x, y);
                ra.setResizable(false);
                ra.setVisible(true);

            }
        });

        selectInputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                BamFileChooser fqfc = new BamFileChooser(communicator);
                //Check if some input was truly selected

                if (checkIfInputWasSelected()) {
                    runButton.setEnabled(true);
                } else {
                    runButton.setEnabled(false);
                }
            }
        });

        selectReferenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ReferenceFileChooser rfc = new ReferenceFileChooser(communicator);
                //Check if some input was truly selected

                if (checkIfInputWasSelected()) {
                    runButton.setEnabled(true);
                } else {
                    runButton.setEnabled(false);
                }
            }
        });

        selectOutputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                OutputDirChooser rfc = new OutputDirChooser(communicator);
                //Check if some input was truly selected

                if (checkIfInputWasSelected()) {
                    runButton.setEnabled(true);
                } else {
                    runButton.setEnabled(false);
                }
            }
        });

        plottingOptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                PlottingOptions plottingOptions = new PlottingOptions();
                //setWindowPosition(ra);
                plottingOptions.setSize(600, 400);
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
                plottingOptions.setLocation(x, y);
                plottingOptions.setResizable(false);
                plottingOptions.setVisible(true);

            }
        });


        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    frame.dispose();
                    // set all user options
                    communicator.setLength(Integer.parseInt(length.getText()));
                    communicator.setThreshold(Integer.parseInt(threshold.getText()));
                    communicator.setSpecie_name(specie_input.getText());

                    starter.start(communicator);


                } catch (Exception ex) {
                    throw new IllegalArgumentException("Input settings are wrong. Please see help.");// TODO error handling
                }
            }


        });


    }


    /**
     * Method that can check/uncheck (given a boolean value) the appropriate checkboxes in the main GUI of EAGER.
     * Can be used to ease the usage of the overall GUI to quickly deselect/select the parameters.
     */

    private void checkBoxes() {
        boolean b = false;

        rescaleCheckBox.setSelected(b);
        useAllMappedReadsCheckBox.setSelected(b);


    }

    private boolean checkIfInputWasSelected() {
        boolean tmp = false;
        if (communicator.getInput() != null && communicator.getReference() != null && communicator.getOutfolder() != null) {
            if (communicator.getInput().length() != 0) {
                tmp = true;
            }
        }
        return tmp;
    }


    private void setValues(Communicator communicator) {

        this.threshold.setText(String.valueOf(communicator.getThreshold()));
        this.length.setText(String.valueOf(communicator.getLength()));

    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        masterpanel = new JPanel();
        masterpanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        masterpanel.setBorder(BorderFactory.createTitledBorder("Main Settings DamageProfiler"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        masterpanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectInputFileButton = new JButton();
        selectInputFileButton.setText("Select input file");
        panel1.add(selectInputFileButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rescaleCheckBox = new JCheckBox();
        rescaleCheckBox.setText("rescale");
        panel1.add(rescaleCheckBox, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        useAllMappedReadsCheckBox = new JCheckBox();
        useAllMappedReadsCheckBox.setText("use all mapped reads");
        panel1.add(useAllMappedReadsCheckBox, new GridConstraints(5, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Threshold");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Length");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        threshold = new JTextField();
        panel1.add(threshold, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        length = new JTextField();
        panel1.add(length, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectReferenceButton = new JButton();
        selectReferenceButton.setText("Select reference");
        panel1.add(selectReferenceButton, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rescaleAdvancedButton = new JButton();
        rescaleAdvancedButton.setText("Advanced");
        rescaleAdvancedButton.setToolTipText("Default parameters for rescaling can be changed here.");
        panel1.add(rescaleAdvancedButton, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectOutputButton = new JButton();
        selectOutputButton.setText("Select output ");
        panel1.add(selectOutputButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(76, 27), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(76, 27), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(76, 27), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(76, 27), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        plottingOptionsButton = new JButton();
        plottingOptionsButton.setText("Plotting options");
        panel1.add(plottingOptionsButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        specie_label = new JLabel();
        specie_label.setText("Specie");
        panel6.add(specie_label, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        specie_input = new JTextField();
        panel1.add(specie_input, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        runButton = new JButton();
        runButton.setText("Run");
        panel1.add(runButton, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 32), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel7.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return masterpanel;
    }
}
