package org.remedy.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class EditorMain extends JFrame {

    private static final String REMEDY_DIR = "remedyList";
    private JTextField remedyNameText;
    private JList<String> remedyList;
    private RemedyListModel remedyListModel;

    private JList<String> categoryList;
    private CategoryListModel categoryListModel;
    private JTextField categoryName;

    private JList<String> symptomList;
    private DefaultListModel<String> symptomListModel;
    private JTextField symptomName;

    private JList<String> currentSymptomList;
    private DefaultListModel<String> currentSymptomListModel;

    private JTextField chosenRemedyName;
    private JButton addRemedyButton;
    private JButton removeRemedyButton;
    private JButton addCategoryButton;
    private JButton removeCategoryButton;
    private JButton removeSymptomButton;
    private JButton addSymptomButton;
    private JButton addToCurrentSymptomsButton;
    private JButton removeCurrentSymptomButton;

    private Map<String, Remedy> remedyMap = new HashMap<>();
    private Map<String, Set<String>> categoryMap = new HashMap<>();

    private JPanel createRemedyListPanel() {
        addRemedyButton = new JButton("Add");
        removeRemedyButton = new JButton("Remove");
        remedyNameText = new JTextField(20);

        remedyNameText.getDocument().addDocumentListener(
                new DocumentListener() {

                    private void documentChanged() {
                        if (remedyNameText.getText().length() == 0) {
                            addRemedyButton.setEnabled(false);
                        } else {
                            addRemedyButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        documentChanged();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        documentChanged();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        documentChanged();
                    }
                });

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3, 3, 3, 3);
        panel.add(new JLabel("Remedy"), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel.add(remedyNameText, c);

        c.gridx++;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        panel.add(addRemedyButton, c);

        remedyListModel = new RemedyListModel(remedyMap);
        remedyList = new JList<>(remedyListModel);
        ListSelectionModel selectionModel = remedyList.getSelectionModel();
        JScrollPane scrollPane = new JScrollPane(remedyList);
        scrollPane.setBorder(new BevelBorder(NORMAL));

        selectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = remedyList.getSelectedIndex();
                if (selectedIndex == -1) {
                    // Clear all other buttons/text.
                    chosenRemedyName.setText("");
                    removeRemedyButton.setEnabled(false);
                } else {
                    String remedyName = remedyListModel.get(selectedIndex);
                    chosenRemedyName.setText(remedyName);
                    removeRemedyButton.setEnabled(true);
                    updateCurrentSymptoms();
                }
                updateButtonState();
            }
        });

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        addRemedyButton.setEnabled(false);
        addRemedyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remedyName = remedyNameText.getText();
                Remedy remedy = remedyMap.get(remedyName);
                if (remedy == null) {
                    remedy = new Remedy(remedyName);
                    remedyMap.put(remedyName, remedy);
                    remedyListModel.update();
                }

                remedyNameText.setText("");
                remedyList.setSelectedIndex(remedyListModel.getSize() - 1);
            }
        });

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(removeRemedyButton, c);

        removeRemedyButton.setEnabled(false);
        removeRemedyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedItem = remedyList.getSelectedIndex();
                if (selectedItem != -1) {
                    String remedyName = remedyListModel.get(selectedItem);
                    remedyMap.remove(remedyName);
                    remedyListModel.update();
                }
            }
        });

        panel.setBorder(new TitledBorder("List of remedies"));
        return panel;
    }

    private void updateButtonState() {
        int selectedCategoryIndex = categoryList.getSelectedIndex();
        if (selectedCategoryIndex != -1) {
            removeCategoryButton.setEnabled(true);
            symptomName.setEnabled(true);
            if (symptomName.getText().length() > 0) {
                addSymptomButton.setEnabled(true);
            } else {
                addSymptomButton.setEnabled(false);
            }
        } else {
            removeCategoryButton.setEnabled(false);
            symptomName.setEnabled(false);
            addSymptomButton.setEnabled(false);
        }

        int selectedSymptomIndex = symptomList.getSelectedIndex();
        if (selectedSymptomIndex != -1 && selectedCategoryIndex != -1) {
            removeSymptomButton.setEnabled(true);
            int selectedRemedyIndex = remedyList.getSelectedIndex();
            if (selectedRemedyIndex != -1) {
                addToCurrentSymptomsButton.setEnabled(true);
            } else {
                addToCurrentSymptomsButton.setEnabled(false);
            }
        } else {
            removeSymptomButton.setEnabled(false);
            addToCurrentSymptomsButton.setEnabled(false);
        }
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

        addCategoryButton = new JButton("Add");
        addCategoryButton.setEnabled(false);
        removeCategoryButton = new JButton("Remove");
        removeCategoryButton.setEnabled(false);

        categoryName = new JTextField(20);
        categoryName.getDocument().addDocumentListener(new DocumentListener() {

            private void documentChanged() {
                if (categoryName.getText().length() == 0) {
                    addCategoryButton.setEnabled(false);
                } else {
                    addCategoryButton.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentChanged();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel.add(categoryName, c);

        c.gridx++;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        panel.add(addCategoryButton, c);

        categoryListModel = new CategoryListModel(categoryMap);
        categoryList = new JList<>(categoryListModel);
        ListSelectionModel selectionModel = categoryList.getSelectionModel();
        JScrollPane scrollPane = new JScrollPane(categoryList);
        scrollPane.setBorder(new BevelBorder(NORMAL));
        c.gridy++;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 3;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonState();
                int selectedCategoryIndex = categoryList.getSelectedIndex();
                if (selectedCategoryIndex != -1) {
                    // Clear the current list of symptoms and update with the
                    // new list.
                    symptomListModel.clear();
                    Iterable<String> symptoms = categoryListModel
                            .getSymptoms(selectedCategoryIndex);
                    for (String symptom : symptoms) {
                        symptomListModel.addElement(symptom);
                    }
                }
            }
        });

        addCategoryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String category = categoryName.getText();
                categoryListModel.addCategory(category);
                categoryName.setText("");
                categoryList.setSelectedIndex(categoryListModel.getSize() - 1);
            }
        });

        c.gridx = 0;
        c.gridy += 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;

        panel.add(removeCategoryButton, c);
        removeCategoryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = categoryList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String category = categoryListModel.get(selectedIndex);
                    categoryListModel.removeCategory(category);
                }
            }
        });

        panel.setBorder(new TitledBorder("Categories"));
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

        addSymptomButton = new JButton("Add");
        addSymptomButton.setEnabled(false);
        removeSymptomButton = new JButton("Remove");
        removeSymptomButton.setEnabled(false);
        addToCurrentSymptomsButton = new JButton("Add to current symptoms");
        addToCurrentSymptomsButton.setEnabled(false);
        symptomName = new JTextField(20);
        symptomName.setEnabled(false);
        symptomName.getDocument().addDocumentListener(new DocumentListener() {

            private void documentChanged() {
                if (symptomName.getText().length() == 0) {
                    addSymptomButton.setEnabled(false);
                } else {
                    addSymptomButton.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentChanged();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel.add(symptomName, c);

        c.gridx++;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        panel.add(addSymptomButton, c);

        symptomListModel = new DefaultListModel<>();
        symptomList = new JList<>(symptomListModel);
        ListSelectionModel selectionModel = symptomList.getSelectionModel();
        JScrollPane scrollPane = new JScrollPane(symptomList);
        scrollPane.setBorder(new BevelBorder(NORMAL));
        c.gridy++;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 3;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        selectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonState();
            }
        });

        addSymptomButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String symptomDescription = symptomName.getText();
                symptomListModel.addElement(symptomDescription);
                symptomName.setText("");
                symptomList.setSelectedIndex(symptomListModel.getSize() - 1);

                int selectedCategoryIndex = categoryList.getSelectedIndex();
                assert selectedCategoryIndex != -1;

                String remedyName = chosenRemedyName.getText();
                if (remedyName.length() > 0) {
                    // Add the entry to the current remedy and update.
                    Remedy remedy = remedyMap.get(remedyName);
                    remedy.addSymptom(new Symptom(categoryListModel
                            .get(selectedCategoryIndex), symptomDescription));
                    updateCurrentSymptoms();
                }
            }
        });

        c.gridx = 0;
        c.gridy += 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;

        panel.add(addToCurrentSymptomsButton, c);

        addToCurrentSymptomsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedCategoryIndex = categoryList.getSelectedIndex();
                if (selectedCategoryIndex == -1) {
                    System.out.println("No category selected");
                    return;
                }
                int selectedSymptomIndex = symptomList.getSelectedIndex();
                if (selectedSymptomIndex == -1) {
                    System.out.println("No symptom selected");
                    return;
                }
                String category = categoryListModel.get(selectedCategoryIndex);
                String symptomName = symptomListModel.get(selectedSymptomIndex);
                Symptom symptom = new Symptom(category, symptomName);
                Remedy remedy = remedyMap.get(chosenRemedyName.getText());
                remedy.addSymptom(symptom);
                updateCurrentSymptoms();
            }
        });

        c.gridx++;

        panel.add(removeSymptomButton, c);

        removeSymptomButton.addActionListener(new ActionListener() {

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

    private void updateCurrentSymptoms() {
        String remedyName = chosenRemedyName.getText();
        currentSymptomListModel.clear();
        if (remedyName.length() == 0) {
            return;
        }
        Remedy remedy = remedyMap.get(remedyName);
        List<String> symptoms = new ArrayList<>();
        for (Symptom s : remedy.getSymptoms()) {
            symptoms.add(s.getCategory().toUpperCase(Locale.ENGLISH) + " -- "
                    + s.getDescription());
        }
        Collections.sort(symptoms);

        for (String s : symptoms) {
            currentSymptomListModel.addElement(s);
        }

    }

    private JPanel createCurrentSymptomPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3, 3, 3, 3);

        chosenRemedyName = new JTextField(20);
        chosenRemedyName.setEditable(false);
        removeCurrentSymptomButton = new JButton("Remove");
        removeCurrentSymptomButton.setEnabled(false);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel.add(chosenRemedyName, c);

        currentSymptomListModel = new DefaultListModel<>();
        currentSymptomList = new JList<>(currentSymptomListModel);
        ListSelectionModel selectionModel = currentSymptomList
                .getSelectionModel();
        JScrollPane scrollPane = new JScrollPane(currentSymptomList);
        scrollPane.setBorder(new BevelBorder(NORMAL));
        c.gridx = 0;
        c.gridy++;
        c.gridheight = 3;
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        selectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = currentSymptomList.getSelectedIndex();
                if (selectedIndex == -1) {
                    removeCurrentSymptomButton.setEnabled(false);
                } else {
                    removeCurrentSymptomButton.setEnabled(true);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        c.gridx = 0;
        c.gridy += 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton saveButton = new JButton("Save");
        buttonPanel.add(saveButton);
        buttonPanel.add(removeCurrentSymptomButton);

        removeCurrentSymptomButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int selectedIndex = currentSymptomList.getSelectedIndex();
                if (selectedIndex == -1) {
                    return;
                }
                currentSymptomListModel.remove(selectedIndex);
            }
        });

        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Save the currently selected remedy to the file.
                String remedyName = chosenRemedyName.getText();
                File file = new File(REMEDY_DIR + "/" + remedyName);
                try {
                    BufferedWriter output = new BufferedWriter(new FileWriter(file));
                    Remedy remedy = remedyMap.get(remedyName);
                    assert remedy != null;
                    List<String> symptoms = new ArrayList<>();
                    for (Symptom symptom : remedy.getSymptoms()) {
                        symptoms.add(symptom.toString());
                    }
                    Collections.sort(symptoms);

                    for (String s : symptoms) {
                        output.write(s + "\n");
                    }
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        panel.add(buttonPanel, c);
        TitledBorder border = new TitledBorder(new BevelBorder(
                BevelBorder.LOWERED, Color.BLACK, Color.BLACK),
                "Current symptoms");
        border.setTitleColor(Color.BLACK);
        border.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(border);
        return panel;
    }

    public EditorMain() throws IOException {
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

        c.gridx = 0;
        c.gridy++;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 3;
        c.insets.top = 20;
        c.fill = GridBagConstraints.BOTH;
        JPanel currentSymptomPanel = createCurrentSymptomPanel();
        mainPane.add(currentSymptomPanel, c);

        loadRemedyMap();
    }

    private void loadRemedyMap() throws IOException {
        File dir = new File(REMEDY_DIR);
        for (File remedyFile : dir.listFiles()) {
            String remedyName = remedyFile.getName();
            Remedy remedy = new Remedy(remedyName);
            BufferedReader input = new BufferedReader(new FileReader(remedyFile));
            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.trim().length() == 0) {
                    continue;
                }

                String[] chunks = line.split("#");
                String category = chunks[0].toLowerCase(Locale.ENGLISH);
                Symptom symptom = new Symptom(category, chunks[1]);
                remedy.addSymptom(symptom);
                Set<String> symptoms = categoryMap.get(category);
                if (symptoms == null) {
                    symptoms = new HashSet<>();
                    categoryMap.put(category, symptoms);
                }
                symptoms.add(chunks[1]);
            }
            input.close();

            remedyMap.put(remedyName, remedy);
        }
    }

    public static void main(String[] args) throws IOException {
        EditorMain editorMain = new EditorMain();
        editorMain.setVisible(true);
    }
}
