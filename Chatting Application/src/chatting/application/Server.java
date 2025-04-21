package chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Server implements ActionListener {

    JTextField text;
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    boolean darkMode = false;

    JLabel status;
    JLabel nameLabel;
    javax.swing.Timer typingTimer;
    boolean typing = false;

    Server() {
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        // Back button
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        // Profile picture
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/tommy.jpeg"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        // Video icon
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(i8));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        // Phone icon
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        JLabel phone = new JLabel(new ImageIcon(i11));
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        // More (⋮) icon
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        JLabel morevert = new JLabel(new ImageIcon(i14));
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);

        // Menu for ⋮
        JPopupMenu menu = new JPopupMenu();
        JMenuItem toggleTheme = new JMenuItem("Toggle Theme");
        JMenuItem clearChat = new JMenuItem("Clear Chat");
        JMenuItem exitChat = new JMenuItem("Exit");

        menu.add(toggleTheme);
        menu.add(clearChat);
        menu.add(exitChat);

        morevert.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                menu.show(morevert, e.getX(), e.getY());
            }
        });

        toggleTheme.addActionListener(e -> toggleTheme());
        clearChat.addActionListener(e -> {
            a1.removeAll();
            vertical.removeAll();
            a1.revalidate();
            a1.repaint();
        });
        exitChat.addActionListener(e -> System.exit(0));

        JLabel name = new JLabel("Tommy");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        status = new JLabel("Offline");
        status.setBounds(110, 35, 150, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setLayout(new BorderLayout());
        f.add(a1);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        // Typing detection
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                status.setText("typing...");
                typing = true;

                if (typingTimer != null) {
                    typingTimer.stop();
                }

                typingTimer = new javax.swing.Timer(2000, new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (typing) {
                            status.setText("Active Now");
                            typing = false;
                        }
                    }
                });
                typingTimer.setRepeats(false);
                typingTimer.start();
            }
        });

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        f.setSize(450, 700);
        f.setUndecorated(true);
        f.setLocation(200, 50);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();
            if (out.trim().equals("")) return;

            JPanel p2 = formatLabel(out);
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            a1.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);
            text.setText("");
            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel(sdf.format(cal.getTime()));
        panel.add(time);
        return panel;
    }

    public void toggleTheme() {
        if (darkMode) {
            f.getContentPane().setBackground(Color.WHITE);
            a1.setBackground(Color.WHITE);
            text.setBackground(Color.WHITE);
            text.setForeground(Color.BLACK);
            darkMode = false;
        } else {
            f.getContentPane().setBackground(Color.DARK_GRAY);
            a1.setBackground(Color.DARK_GRAY);
            text.setBackground(new Color(40, 40, 40));
            text.setForeground(Color.WHITE);
            darkMode = true;
        }
        f.repaint();
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();
                server.status.setText("Online");

                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true) {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));
                    a1.add(vertical, BorderLayout.PAGE_START);
                    f.validate();
                }
            }
        } catch (Exception e) {
            server.status.setText("Offline");
            e.printStackTrace();
        }
    }
}
