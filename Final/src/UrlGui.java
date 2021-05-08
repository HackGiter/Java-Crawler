import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UrlGui extends JFrame implements ActionListener {

    private JTextArea display = new JTextArea();
    private JButton runs = new JButton("Run"),
            settings = new JButton("Settings"),
            reads = new JButton("Logs"),
            files = new JButton("Browser");
    private JLabel dones = new JLabel("Text");
    private JPanel commands = new JPanel(),
            browser = new JPanel();

    public UrlGui(String Title) {
        super(Title);

        runs.addActionListener(this::actionPerformed);
        settings.addActionListener(this::actionPerformed);
        reads.addActionListener(this::actionPerformed);
        files.addActionListener(this::actionPerformed);

        commands.setLayout(new GridLayout(1, 4, 1, 1));
        commands.add(reads);
        commands.add(settings);
        commands.add(runs);
        commands.add(files);

        display.setLineWrap(true);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add("North", commands);
        this.getContentPane().add(new JScrollPane(display));


        setSize(500, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == files) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(browser);
        }
    }

    public static void main(String[] args) {
        UrlGui urlGui = new UrlGui("Spider");
    }
}
