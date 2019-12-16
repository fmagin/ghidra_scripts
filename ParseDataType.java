// Parse Datatype
//@author Florian Magin
//@category Data Types
//@keybinding
//@menupath Data Types.Parse C Snippet
//@toolbar cache.png

import docking.DialogComponentProvider;
import ghidra.app.script.GhidraScript;
import ghidra.app.util.cparser.C.CParser;
import ghidra.app.util.cparser.C.ParseException;
import ghidra.program.model.data.DataType;
import ghidra.program.model.data.DataTypeManager;

import javax.swing.*;






public class ParseDataType extends GhidraScript {

    @Override
    protected void run() throws Exception {
        var dialog = new ParseStructDialog();
        state.getTool().showDialog(dialog);
    }


    class ParseStructDialog extends DialogComponentProvider {
        private final DataTypeManager main_gdt;
        private JTextArea textInput;
        JTextArea typeOutput;

        private DataType currentType;

        JButton parseButton;

        JSplitPane splitter;
        private ParseStructDialog() {
            super("Parse Data Type", false, true, true, true);
            setPreferredSize(500, 400);


            // GUI SETUP
            this.addCancelButton();
            this.parseButton = new JButton("Parse");
            this.parseButton.addActionListener((e) -> { this.parseType();});
            this.parseButton.setToolTipText("Parse the type and preview the result");

            this.addButton(parseButton);

            this.addApplyButton();
            this.setApplyToolTip("Add the last parsed type to the current data types");
            this.setApplyEnabled(false);

            textInput = new JTextArea(12, 50);
            textInput.setWrapStyleWord(true);
            textInput.setLineWrap(true);

            typeOutput = new JTextArea(12, 50);
            typeOutput.setWrapStyleWord(true);
            typeOutput.setLineWrap(true);
            typeOutput.setEditable(false);

            splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    textInput, typeOutput);

            addWorkPanel(splitter);

            // Parser Setup
            main_gdt = currentProgram.getDataTypeManager();
        }

        private void parseType() {
            var text = this.textInput.getText();
            var parser = new CParser(main_gdt);
            DataType type;
            try {
                type = parser.parse(text);
            } catch (ParseException e) {
                typeOutput.setText(e.toString());
                this.setApplyEnabled(false);
                return;
            }
            currentType = type;
            typeOutput.setText(currentType.toString());
            this.setApplyEnabled(true);
        }

        @Override
        protected void applyCallback() {
            int transaction_id = main_gdt.startTransaction("Parsed");
            main_gdt.addDataType(currentType, null);
            main_gdt.endTransaction(transaction_id, true);
            this.close();
        }
    }
}
