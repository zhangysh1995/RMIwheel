/**
 * Created by KellyZhang on 2017/4/16.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame{
    private static Register register = new Register();
    public JTextArea JTAinfo;

    public MainFrame(String name) {
        this.setTitle(name);
        this.setLayout(new GridLayout(3, 1));

        // add input display
        JLabel JLhost = new JLabel("Host: ");
        JTextField JTFhost = new JTextField();
        JPanel JPhost = new JPanel();
        JPhost.setLayout(new BorderLayout());
        JPhost.add(JLhost, BorderLayout.WEST);
        JPhost.add(JTFhost, BorderLayout.CENTER);

        // start button
        JButton JBstart = new JButton("Start");
        JBstart.addActionListener(new JBstartListener());
        JPhost.add(JBstart, BorderLayout.EAST);

        JLabel JLport = new JLabel("Port: ");
        JTextField JTFport = new JTextField();
        JPanel JPport = new JPanel();
        JPport.setLayout(new BorderLayout());
        JPport.add(JLport, BorderLayout.WEST);
        JPport.add(JTFport);


        // add info display
        JTAinfo = new JTextArea();
        JTAinfo.setEditable(false);
        JScrollPane JSPinfo = new JScrollPane(JTAinfo);
        JSPinfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JSPinfo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        this.add(JPhost);
        this.add(JPport);
        this.add(JSPinfo);
    }

    private class JBstartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            JTAinfo.setText(JTAinfo.getText() + "\nThis operating....");
            register.run();
        }
    }

    // run panel test
    public static void main (String [] args) {
        MainFrame frame = new MainFrame("Bulk Register");

        frame.setSize(300,400);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
