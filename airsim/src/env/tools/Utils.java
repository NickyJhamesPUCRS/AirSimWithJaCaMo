package tools;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Utils {
    public static Map<String, Object> obj2map(Object obj){
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(obj, Map.class);
        return map;
    }

    public static class Display extends JFrame {

        private JTextArea text;
        private JLabel numMsg;
        private static int n = 0;

        public Display(String name) {
            setTitle(".:: "+name+" console ::.");

            JPanel panel = new JPanel();
            setContentPane(panel);
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

            numMsg = new JLabel("0");
            text = new JTextArea(15,80);

            panel.add(text);
            panel.add(Box.createVerticalStrut(5));
            panel.add(numMsg);
            pack();
            setLocation(n*40, n*80);
            setVisible(true);

            n++;
        }

        public void addText(final String s){
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    text.insert(s+"\n",0);
                    // text.append();
                }
            });
        }

        public void updateNumMsgField(final int value){
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    numMsg.setText(""+value);
                }
            });
        }
    }
}
