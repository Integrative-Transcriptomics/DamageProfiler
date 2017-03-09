package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by neukamm on 02.09.2016.
 */
public class Popup{

    public Popup(String resultdir, JFrame frame){
        JFrame pop = new JFrame() ;
        pop.setSize(100, 100);
        JTextPane textPane = new JTextPane();
        textPane.setText("DamageProfiler has finished. \n Results can be found in " + resultdir);
        pop.add(textPane);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pop.dispose();
                frame.dispose();
            }
        });

        pop.add(ok, BorderLayout.NORTH);
        pop.pack();
        pop.setVisible(true);

    }


}
