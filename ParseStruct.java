//
//@author Florian Magin
//@category 
//@keybinding
//@menupath
//@toolbar

import ghidra.app.script.GhidraScript;
import ghidra.app.util.cparser.C.CParser;
import ghidra.program.model.data.DataTypeManager;


public class ParseStruct extends GhidraScript {

    @Override
    protected void run() throws Exception {
        DataTypeManager main_gdt = currentProgram.getDataTypeManager();
        var parser = new CParser(main_gdt);
        var text = askString("C Struct body","");
        println("Got text: " + text);
        parser.parse(text);
        var type = parser.getLastDataType();
        println("Parsed to data type:\n" + type.toString());
        main_gdt.addDataType(type, null);
    }
}
