package org.remedy.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
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

	private JTextField remedyName;
	private JList<String> remedyList;
	private DefaultListModel<String> remedyListModel;

	private JList<String> categoryList;
	private DefaultListModel<String> categoryListModel;
	private JTextField categoryName;

	private JList<String> symptomList;
	private DefaultListModel<String> symptomListModel;
	private JTextField symptomName;

	private JList<String> currentSymptomList;
	private DefaultListModel<String> currentSymptomListModel;

	private JPanel createRemedyListPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);
		panel.add(new JLabel("Remedy"), c);

		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		remedyName = new JTextField(20);
		panel.add(remedyName, c);

		JButton addRemedyButton = new JButton("Add");
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		panel.add(addRemedyButton, c);

		remedyListModel = new DefaultListModel<>();
		remedyList = new JList<>(remedyListModel);
		JScrollPane scrollPane = new JScrollPane(remedyList);
		scrollPane.setBorder(new BevelBorder(NORMAL));

		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 3; c.gridheight = 3;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		addRemedyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String remedy = remedyName.getText();
				remedyListModel.addElement(remedy);
				remedyName.setText("");
			}
		});

		JButton removeButton = new JButton("Remove");
		c.gridx = 0; c.gridy = 4;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		panel.add(removeButton, c);

		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedItem = remedyList.getSelectedIndex();
				remedyListModel.remove(selectedItem);
			}
		});

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
		c.insets = new Insets(3, 3, 3, 3);

		categoryName = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(categoryName, c);

		JButton addButton = new JButton("Add");
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		panel.add(addButton, c);

		categoryListModel = new DefaultListModel<>();
		categoryList = new JList<>(categoryListModel);
		JScrollPane scrollPane = new JScrollPane(categoryList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridy++; c.gridx = 0;
		c.gridheight = 3; c.gridwidth = 3;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String category = categoryName.getText();
				categoryListModel.addElement(category);
				categoryName.setText("");
			}
		});

		c.gridx = 0; c.gridy += 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		JButton removeButton = new JButton("Remove");
		panel.add(removeButton, c);
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = categoryList.getSelectedIndex();
				if (selectedIndex != -1) {
					categoryListModel.remove(selectedIndex);
				}
			}
		});

		panel.setBorder(new TitledBorder("Categories"));
		return panel;
	}

	private JPanel createCurrentSymptomPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);

		JTextField chosenRemedyName = new JTextField(20);
		chosenRemedyName.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(chosenRemedyName, c);

		currentSymptomListModel = new DefaultListModel<>();
		currentSymptomList = new JList<>(currentSymptomListModel);
		JScrollPane scrollPane = new JScrollPane(currentSymptomList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridx = 0; c.gridy++;
		c.gridheight = 3; c.gridwidth = 2;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		JPanel buttonPanel = new JPanel();
		c.gridx = 0; c.gridy += 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton saveButton = new JButton("Save");
		buttonPanel.add(saveButton);
		JButton removeButton = new JButton("Remove");
		buttonPanel.add(removeButton);

		panel.add(buttonPanel, c);
		TitledBorder border = new TitledBorder(
				new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK), "Current symptoms");
		border.setTitleColor(Color.BLACK);
		border.setTitleJustification(TitledBorder.CENTER);
		panel.setBorder(border);
		return panel;
	}

	private JPanel createSymptomListPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);

		symptomName = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(symptomName, c);

		JButton addButton = new JButton("Add");
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		panel.add(addButton, c);

		symptomListModel = new DefaultListModel<>();
		symptomList = new JList<>(symptomListModel);
		JScrollPane scrollPane = new JScrollPane(symptomList);
		scrollPane.setBorder(new BevelBorder(NORMAL));
		c.gridy++; c.gridx = 0;
		c.gridheight = 3; c.gridwidth = 3;
		c.weighty = 1.0; c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				symptomListModel.addElement(symptomName.getText());
				symptomName.setText("");
			}
		});

		c.gridx = 0; c.gridy += 3;
		c.gridwidth = 1; c.gridheight = 1;
		c.weighty = 0.0; c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		JButton addToCurrent = new JButton("Add to current symptoms");
		panel.add(addToCurrent, c);

		addToCurrent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = symptomList.getSelectedIndex();
				if (selectedIndex != -1) {
					currentSymptomListModel.addElement(symptomListModel.get(selectedIndex));
				}
			}
		});

		c.gridx++;
		JButton removeButton = new JButton("Remove");
		panel.add(removeButton, c);

		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = symptomList.getSelectedIndex();
				if (selectedIndex != -1) {
					symptomListModel.remove(selectedIndex);
				}
			}
		});

		panel.setBorder(new TitledBorder("Symptom list"));
		return panel;
	}

	public EditorMain() {
		setSize(1024, 768);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Remedy editor");
		Container mainPane = getContentPane();
		GridBagLayout layout = new GridBagLayout();
		mainPane.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.weighty = 0.25;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;

		JPanel remedyListPanel = createRemedyListPanel();
		mainPane.add(remedyListPanel, c);

		c.gridx++;
		JPanel categoryPanel = createCategoryPanel();
		mainPane.add(categoryPanel, c);

		c.gridx++;
		JPanel symptomListPanel = createSymptomListPanel();
		mainPane.add(symptomListPanel, c);

		c.gridx = 0; c.gridy++;
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridwidth = 3;
		c.insets.top = 20;
		c.fill = GridBagConstraints.BOTH;
		JPanel currentSymptomPanel = createCurrentSymptomPanel();
		mainPane.add(currentSymptomPanel, c);
	}

	public static void main(String[] args) {
		EditorMain editorMain = new EditorMain();
		editorMain.setVisible(true);
	}
}
