import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatClientGUI {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private JFrame frame;
    private JTextArea messageArea;
    private JTextField messageField;
    private JTextField searchField;
    private JList<String> userList;
    private DefaultListModel<String> listModel;
    private JButton sendButton;
    private JButton quitButton;
    private JButton sendFileButton;
    private JButton downloadFileButton;
    private JButton createGroupButton;
    private JButton addToGroupButton;
    private JButton sendGroupMessageButton;
    private JButton showRulesButton;
    private JButton searchButton;
    private JButton kickFromGroupButton;
    private JButton recordVoiceButton;

    public ChatClientGUI(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());

        frame = new JFrame("Chat Client");
        messageArea = new JTextArea(20, 10);
        messageField = new JTextField(20);
        searchField = new JTextField(15);
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);

        sendButton = createButtonWithIconAndText("send.png", "Send", 30, 30);
        quitButton = createButtonWithIconAndText("quit.png", "Quit", 30, 30);
        sendFileButton = createButtonWithIconAndText("send_file.png", "Send File", 30, 30);
        downloadFileButton = createButtonWithIconAndText("download_file.png", "Download File", 30, 30);
        createGroupButton = createButtonWithIconAndText("create_group.png", "Create Group", 30, 30);
        addToGroupButton = createButtonWithIconAndText("add_to_group.png", "Add to Group", 30, 30);
        sendGroupMessageButton = createButtonWithIconAndText("send_group_message.png", "Send Group Message", 30, 30);
        showRulesButton = createButtonWithIconAndText("rules.png", "Show Rules", 30, 30);
        searchButton = createButtonWithIconAndText("search.png", "Search", 23, 23); // Search button
        kickFromGroupButton = createButtonWithIconAndText("kick_from_group.png", "Kick from Group", 30, 30);
        // In the constructor of ChatClientGUI
        recordVoiceButton = createButtonWithIconAndText("record_voice.png", "Record Voice", 30, 30);
        Font font = new Font("Arial", Font.ITALIC, 14);

        messageArea.setFont(font);
        messageField.setFont(font);
        searchField.setFont(font);
        userList.setFont(font);
        sendButton.setFont(font);
        quitButton.setFont(font);
        sendFileButton.setFont(font);
        downloadFileButton.setFont(font);
        createGroupButton.setFont(font);
        addToGroupButton.setFont(font);
        sendGroupMessageButton.setFont(font);
        showRulesButton.setFont(font);
        searchButton.setFont(font);
        kickFromGroupButton.setFont(font);

        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        JScrollPane userScrollPane = new JScrollPane(userList);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        messagePanel.add(downloadFileButton, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, messageScrollPane, userScrollPane);
        splitPane.setDividerLocation(300);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.LINE_AXIS));
        groupPanel.add(createGroupButton);
        groupPanel.add(addToGroupButton);
        groupPanel.add(sendGroupMessageButton);
        groupPanel.add(kickFromGroupButton);
        groupPanel.add(showRulesButton);        

        bottomPanel.add(messagePanel, BorderLayout.CENTER);
        bottomPanel.add(quitButton, BorderLayout.EAST);
        bottomPanel.add(sendFileButton, BorderLayout.WEST);
        bottomPanel.add(recordVoiceButton, BorderLayout.NORTH);
        bottomPanel.add(groupPanel, BorderLayout.SOUTH);
       // bottomPanel.add(showRulesButton, BorderLayout.WEST);

        frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        recordVoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recordVoiceMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("Quit");
                    socket.close();
                    frame.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });

        downloadFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                downloadFile();
            }
        });

        createGroupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createGroup();
            }
        });

        addToGroupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToGroup();
            }
        });

        sendGroupMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendGroupMessage();
            }
        });

        showRulesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRules();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchMessages();
            }
        });

        kickFromGroupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kickFromGroup();
            }
        });
        
        

        new Thread(new ReadThread()).start();

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        String clientName = JOptionPane.showInputDialog(frame, "Enter your name:");
        dos.writeUTF(clientName);
    }

    private void recordVoiceMessage() {
        // Record the voice message using an external library or custom implementation
        // Save the recorded voice message to a file
        // For example, using JavaSound API or another library to record the voice
    
        String fileName = "voice_message.mp3"; // Example file name
        File file = new File(fileName);
        try {
            dos.writeUTF("VOICE " + file.getName() + " " + file.length());
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void receiveVoiceMessage(String fileInfo) throws IOException {
        String[] parts = fileInfo.split(" ");
        String fileName = parts[1];
        long fileSize = Long.parseLong(parts[2]);
    
        FileOutputStream fos = new FileOutputStream("client_" + fileName);
        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalRead = 0;
    
        while (totalRead < fileSize && (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalRead))) != -1) {
            totalRead += bytesRead;
            fos.write(buffer, 0, bytesRead);
        }
    
        fos.close();
        messageArea.append("Voice message received: " + fileName + "\n");
        playVoiceMessage(fileName);
    }
    
    private void playVoiceMessage(String fileName) {
        // Play the received voice message using an external library or custom implementation
        // For example, using JavaSound API or another library to play the audio
    }

    private void sendMessage() {
        try {
            String msg = messageField.getText();
            dos.writeUTF(msg);
            messageField.setText("");
            if (msg.equalsIgnoreCase("Quit")) {
                socket.close();
                frame.dispose();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                dos.writeUTF("FILE " + file.getName() + " " + file.length());
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendVoiceMessage(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Voice message file does not exist.");
            return;
        }

        dos.writeUTF("VOICE " + file.getName() + " " + file.length());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, bytesRead);
        }

        fis.close();
    }

    private void downloadFile() {
        String fileName = JOptionPane.showInputDialog(frame, "Enter the file name to download:");
        if (fileName != null && !fileName.isEmpty()) {
            try {
                dos.writeUTF("DOWNLOAD " + fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void createGroup() {
        String groupName = JOptionPane.showInputDialog(frame, "Enter the group name:");
        if (groupName != null && !groupName.isEmpty()) {
            try {
                dos.writeUTF("GROUP CREATE " + groupName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void addToGroup() {
        String groupName = JOptionPane.showInputDialog(frame, "Enter the group name:");
        String participantName = JOptionPane.showInputDialog(frame, "Enter the participant name:");
        if (groupName != null && !groupName.isEmpty() && participantName != null && !participantName.isEmpty()) {
            try {
                dos.writeUTF("GROUP ADD " + groupName + " " + participantName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void sendGroupMessage() {
        String groupName = JOptionPane.showInputDialog(frame, "Enter the group name:");
        String message = JOptionPane.showInputDialog(frame, "Enter the message:");
        if (groupName != null && !groupName.isEmpty() && message != null && !message.isEmpty()) {
            try {
                dos.writeUTF("GMSG " + groupName + " " + message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    } 

    private void showRules() {
        String rules = "Rules for using the Chat App:\n\n" +
            "1. Enter your name when prompted.\n" +
            "2. To send a message to everyone, simply type your message and press 'Send'.\n" +
            "3. To send a private message, type '@username message'.\n" +
            "4. To send a file, click 'Send File' and select the file.\n" +
            "5. To download a file, click 'Download File' and enter the file name.\n" +
            "6. To create a group, click 'Create Group' and enter the group name.\n" +
            "7. To add a participant to a group, click 'Add to Group', enter the group name and participant name.\n" +
            "8. To send a group message, click 'Send Group Message', enter the group name and message.\n" +
            "9. To quit the chat, click 'Quit'.\n";

        JOptionPane.showMessageDialog(frame, rules, "Chat App Rules", JOptionPane.INFORMATION_MESSAGE);
    }    

    private void searchMessages() {
        String searchQuery = searchField.getText().toLowerCase();
        if (!searchQuery.isEmpty()) {
            String[] lines = messageArea.getText().split("\n");
            StringBuilder searchResults = new StringBuilder();
            for (String line : lines) {
                if (line.toLowerCase().contains(searchQuery)) {
                    searchResults.append(line).append("\n");
                }
            }
            if (searchResults.length() > 0) {
                JOptionPane.showMessageDialog(frame, searchResults.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No matching messages found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a search query.", "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void kickFromGroup() {
        String groupName = JOptionPane.showInputDialog(frame, "Enter the group name:");
        String participantName = JOptionPane.showInputDialog(frame, "Enter the participant name:");
        if (groupName != null && !groupName.isEmpty() && participantName != null && !participantName.isEmpty()) {
            try {
                dos.writeUTF("GROUP KICK " + groupName + " " + participantName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    private class ReadThread implements Runnable {
        public void run() {
            try {
                while (true) {
                    String msg = dis.readUTF();
                    if (msg.startsWith("VOICE")) {
                        String[] parts = msg.split(" ");
                        String fileName = parts[1];
                        long fileSize = Long.parseLong(parts[2]);
                        receiveVoiceMessage(fileName, fileSize);
                    }
                    if (msg.startsWith("FILE")) {
                        receiveFile(msg);
                    } else {
                        if (msg.contains("has joined the chat")) {
                            String userName = msg.split(" ")[0];
                            listModel.addElement(userName);
                        } else if (msg.contains("has left the chat")) {
                            String userName = msg.split(" ")[0];
                            listModel.removeElement(userName);
                        }
                        messageArea.append(msg + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void receiveFile(String fileInfo) throws IOException {
            String[] parts = fileInfo.split(" ");
            String fileName = parts[1];
            long fileSize = Long.parseLong(parts[2]);

            FileOutputStream fos = new FileOutputStream("client_" + fileName);
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalRead = 0;

            while (totalRead < fileSize && (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalRead))) != -1) {
                totalRead += bytesRead;
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            messageArea.append("File received: " + fileName + "\n");
        }
    }

    private JButton createButtonWithIconAndText(String iconName, String text, int iconWidth, int iconHeight) {
        ImageIcon icon = new ImageIcon(new ImageIcon(iconName).getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));
        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setMargin(new Insets(5, 5, 5, 5)); // Add padding around the text
        button.setFont(new Font("Arial", Font.PLAIN, 12)); // Adjust the font size as needed
        return button;
    }

    private void receiveVoiceMessage(String fileName, long fileSize) throws IOException {
        FileOutputStream fos = new FileOutputStream("client_" + fileName);
        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalRead = 0;

        while (totalRead < fileSize && (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalRead))) != -1) {
            totalRead += bytesRead;
            fos.write(buffer, 0, bytesRead);
        }

        fos.close();
        System.out.println("Voice message received: " + fileName);
    }
    

    public static void main(String[] args) {
        try {
            new ChatClientGUI("localhost", 1234);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
