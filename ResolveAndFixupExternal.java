//
//@author Florian Magin
//@category 
//@keybinding
//@menupath
//@toolbar

import java.util.ArrayList;
import java.util.List;

import ghidra.app.script.GhidraScript;
import ghidra.app.util.opinion.ElfLoader;
import ghidra.program.model.listing.Library;
import ghidra.program.model.listing.Program;
import ghidra.program.model.symbol.ExternalManager;
import ghidra.util.Msg;

public class ResolveAndFixupExternal extends GhidraScript {

	@Override
	protected void run() throws Exception {
		if (!ElfLoader.ELF_NAME.equals(currentProgram.getExecutableFormat())) {
			Msg.showError(this, null, "ResolveAndFixupExternal",
				"Current program is not an ELF program!  (" + currentProgram.getExecutableFormat() + ")");
			return;
		}
		
		Msg.showInfo(this, null, "ResolveAndFixupExternal", getLibsWithoutExternalPath(currentProgram));
	}
	
	
	
	public List<Library> getLibsWithoutExternalPath(Program program){
		List<Library> result = new ArrayList<>();
		ExternalManager externalManager = program.getExternalManager();
		return null;
		
	}
}
