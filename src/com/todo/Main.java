import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {
    private static ArrayList<String> taskList = new ArrayList<>();
    private static DefaultListModel<String> listModel = new DefaultListModel<>();

    private JTextField taskField;
    private JButton addButton, removeButton, completeButton;
    private JList<String> taskJList;

    public Main() {
        // Create the frame for the application
        JFrame frame = new JFrame("To-Do List App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Create the text field for entering tasks
        taskField = new JTextField();

        JPanel textFieldPanel = new JPanel(new BorderLayout());
        textFieldPanel.add(taskField, BorderLayout.CENTER);
        textFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(textFieldPanel, BorderLayout.NORTH);

        // Set a document filter on the text field to limit the number of characters
        ((AbstractDocument) taskField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private int maxCharacters = 200;

            @Override
            public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength + str.length();

                if (newLength <= maxCharacters) {
                    super.insertString(fb, offs, str, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + str.length();

                if (newLength <= maxCharacters) {
                    super.replace(fb, offs, length, str, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        // Set some basic styling for the task input text field
        taskField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        taskField.setBackground(new Color(255, 255, 255));
        taskField.setForeground(new Color(0, 0, 0));
        Font font = new Font("VERDENA", Font.BOLD, 14);
        taskField.setFont(font);

        // Create the JList to display tasks
        taskJList = new JList<>(listModel);

        // Add the JList inside a JScrollPane for scrollable list
        JScrollPane scrollPane = new JScrollPane(taskJList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Customize the rendering of list items
        taskJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(new Color(255, 255, 255));
                if (!isSelected) {
                    if (value instanceof String && ((String) value).contains("rgb(78, 245, 66)")) {
                        c.setForeground(new Color(78, 245, 66));
                    } else {
                        c.setForeground(new Color(0, 0, 0));
                    }
                }
                return c;
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        frame.add(centerPanel, BorderLayout.CENTER);

        // Create buttons for adding, removing, and marking tasks as complete
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add Task");
        completeButton = new JButton("Mark as Completed");
        removeButton = new JButton("Remove Task");

        JButton[] buttons = {addButton, removeButton, completeButton};

        // Customize buttons appearance
        for (JButton btn : buttons) {
            btn.setBackground(new Color(27, 112, 204));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Verdana", Font.BOLD, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            bottomPanel.add(btn);
        }

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);

        // ActionListener for Add Task button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskField.getText();
                if (!task.isEmpty()) {
                    taskList.add(task);
                    listModel.addElement("<html><font color='black'>" + task + "</font></html>");
                    taskField.setText("");
                }
            }
        });

        // ActionListener for Remove Task button
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    taskList.remove(selectedIndex);
                    listModel.remove(selectedIndex);
                }
            }
        });

        // ActionListener for Mark as Completed button
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedText = taskList.get(selectedIndex);
                    if (!selectedText.contains(" (Completed)")) {
                        String completedTask = taskList.get(selectedIndex) + " (Completed)";
                        taskList.set(selectedIndex, completedTask);
                        listModel.set(selectedIndex, "<html><font color='rgb(31, 232, 31)'>" + completedTask + "</font></html>");
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}