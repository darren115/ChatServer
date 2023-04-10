package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class UserInterface {

	private Server server;

//	private JTextArea textArea;
	private JTextPane textArea;
	private JTextField textField;
	private JList<String> list;
	
	private DefaultListModel<String> listModel1;
	
	private LocalTime time = LocalTime.now();
	
	public UserInterface(Server server) {
		
		JFrame frame = new JFrame("Chat Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);

		JPanel panel = new JPanel();
		JPanel mainChat = new JPanel();
		mainChat.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
//		mainChat.setSize(1000, 1000);
//		mainChat.setBackground(Color.red);
//		panel.setLayout(new FlowLayout());

		frame.setTitle("Chat Server ");

//		textArea = new JTextArea(15, 25);
		textArea = new JTextPane();
		textArea.setEditable(false);
		
//		textArea.setLineWrap(true);
//		textArea.setWrapStyleWord(true);
		
		JScrollPane chatScroll = new JScrollPane(textArea);
		chatScroll.setPreferredSize(new Dimension(270, 300));
		chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		textField = new JTextField(20);
		textField.addActionListener(e -> {
			String field = textField.getText();
			List<String> selectedUsers = list.getSelectedValuesList();
			if (field.length() > 0) {
				try {
				if(selectedUsers.size()==0) {
					server.broadcastAll("Server: " + textField.getText());
				}
				else {
					for(String user: selectedUsers) {
						System.out.println(user);
						server.messageClient(user, "Server: " + textField.getText());
					}
				}
				} catch(BadLocationException err) {
					err.printStackTrace();
				}
				
				textField.setText("");

			}

		});

		listModel1 = new DefaultListModel<>();
		list = new JList<>(listModel1);
		listModel1.addElement("test");
		listModel1.removeElement("test");
		
//		list.addListSelectionListener(e->{
//			System.out.println(list.getSelectedValuesList());
//		});
		
		
		JScrollPane usersScrollList = new JScrollPane(list);
		usersScrollList.setPreferredSize(new Dimension(100, 145));
		usersScrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		usersScrollList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JLabel usersLabel = new JLabel();
		usersLabel.setText("Connected Users");
		usersLabel.setToolTipText("Select users to private message");
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		mainChat.add(chatScroll, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		mainChat.add(textField, c);
		panel.setLayout(new BorderLayout());
		panel.add(usersLabel, BorderLayout.NORTH);
		panel.add(usersScrollList);

		frame.add(mainChat, BorderLayout.LINE_START);
		frame.add(panel, BorderLayout.LINE_END);
//		frame.pack();
		frame.setVisible(true);

	}

	public void appendMessage(String message) throws BadLocationException {
		
		time = LocalTime.now();
		String current = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
//		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
//	      StyleConstants.setItalic(attributeSet, true);
//	      textArea.setCharacterAttributes(attributeSet, true);
//	      textArea.setText("Learn with Text and "); 
//	      Font font = new Font("Verdana", Font.BOLD, 22);
//	      textArea.setFont(font);
	      StyledDocument doc = textArea.getStyledDocument();
	      Style style = textArea.addStyle("", null);
	      StyleConstants.setForeground(style, Color.GREEN);
	      doc.insertString(doc.getLength(), current + ": ", style);
	      StyleConstants.setForeground(style, Color.BLACK);
	      doc.insertString(doc.getLength(), message + "\n", style);
		

	}

	
	public void appendList(String item) {
		listModel1.addElement(item);
	}
	
	public void removeList(String item) {
		listModel1.removeElement(item);
	}

}
