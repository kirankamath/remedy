package org.remedy.editor;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class EditorMain extends JFrame {

	public EditorMain() {
		setSize(1024, 768);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Remedy editor");
		Container mainPane = getContentPane();
		GridBagLayout layout = new GridBagLayout();
		mainPane.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel remedyListPanel = createRemedyListPanel();
		mainPane.add(remedyListPanel, c);

		c.gridx++;
		JPanel categoryPanel = createCategoryPanel();
		mainPane.add(categoryPanel, c);

		c.gridx = 0; c.gridy = 1;
		JPanel currentCategoryPanel = createCurrentCategoryPanel();
		mainPane.add(currentCategoryPanel, c);

		c.gridx = 1; c.gridy = 1;
		JPanel symptomPanel = createSymptomPanel();
		mainPane.add(symptomPanel, c);
	}

	private JPanel createRemedyListPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(7, 7, 3, 3);
		panel.add(new JLabel("Remedy"), c);

		c.gridx = 1;
		c.insets.top = 7; c.insets.left = 3; c.insets.bottom = 3; c.insets.right = 3;
		JTextField remedyName = new JTextField(20);
		panel.add(remedyName, c);

		JButton addButton = new JButton("Add");
		c.gridx++;
		c.insets.top = 7; c.insets.left = 3; c.insets.bottom = 3; c.insets.right = 7;
		panel.add(addButton, c);

		JList<String> remedyList = new JList<>();
		JScrollPane scrollPane = new JScrollPane(remedyList);
		scrollPane.setBorder(new BevelBorder(NORMAL));

		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 3; c.gridheight = 3;
		c.insets.top = 3; c.insets.left = 7; c.insets.bottom = 7; c.insets.right = 7;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		JButton removeButton = new JButton("Remove");
		c.gridx = 0; c.gridy = 4;
		c.gridwidth = 1; c.gridheight = 1;
		c.insets.top = 3; c.insets.left = 7; c.insets.right = 7; c.insets.bottom = 7;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(removeButton, c);

		panel.setBorder(new TitledBorder("List of remedies"));
		return panel;
	}

	private JPanel createCategoryPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(7, 7, 3, 3);

		JTextField categoryName = new JTextField(20);
		c.insets.left = 3;
		c.insets.right = 7;
		panel.add(categoryName, c);

		JButton addButton = new JButton("Add");
		c.gridx++;
		c.insets.top = 7; c.insets.left = 3; c.insets.bottom = 3; c.insets.right = 7;
		panel.add(addButton, c);

		JList<String> categoryList = new JList<>();
		JScrollPane scrollPane = new JScrollPane(categoryList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridy = 1; c.gridx = 0;
		c.gridheight = 3; c.gridwidth = 3;
		c.insets.top = 3; c.insets.left = 7; c.insets.bottom = 7; c.insets.right = 7;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		JButton removeButton = new JButton("Remove");
		c.gridx = 0; c.gridy = 4;
		c.gridwidth = 1; c.gridheight = 1;
		c.insets.top = 3; c.insets.left = 7; c.insets.right = 3; c.insets.bottom = 7;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(removeButton, c);

		JButton addToCurrent = new JButton("Add to this remedy");
		c.gridx = 1; c.gridy = 4;
		c.insets.top = 3; c.insets.left = 3; c.insets.right = 7; c.insets.bottom = 7;
		panel.add(addToCurrent, c);

		panel.setBorder(new TitledBorder("Categories"));
		return panel;
	}

	private JPanel createCurrentCategoryPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(7, 7, 3, 7);

		JList<String> categoryList = new JList<>();
		JScrollPane scrollPane = new JScrollPane(categoryList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridheight = 3; c.gridwidth = 1;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		JButton removeButton = new JButton("Remove");
		c.gridx = 0; c.gridy = 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.insets.top = 3; c.insets.left = 7; c.insets.right = 3; c.insets.bottom = 7;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(removeButton, c);

		panel.setBorder(new TitledBorder("Current categories"));
		return panel;
	}

	private JPanel createSymptomPanel() {
		JPanel panel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(7, 7, 3, 7);

		JList<String> categoryList = new JList<>();
		JScrollPane scrollPane = new JScrollPane(categoryList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridheight = 3; c.gridwidth = 1;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		JButton removeButton = new JButton("Remove");
		c.gridx = 0; c.gridy = 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.insets.top = 3; c.insets.left = 7; c.insets.right = 3; c.insets.bottom = 7;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(removeButton, c);

		panel.setBorder(new TitledBorder("Symptoms"));
		return panel;
	}

	public static void main(String[] args) {
		EditorMain editorMain = new EditorMain();
		editorMain.setVisible(true);
	}
}
