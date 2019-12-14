import argparse
import os
parser = argparse.ArgumentParser(description='Dirty script to parse headers into Ghidra Data Type Archives')
parser.add_argument('header_file',
                    help='The header file to parse')
parser.add_argument('--output', '-o',
                    help='Path of the output file (must not exist yet!)')
#parser.add_argument('--overwrite', '-f', help="Overwrite the output file if it already exists", action='store_true')




import typing
if typing.TYPE_CHECKING:
    import ghidra
else:
    import ghidra_bridge
    try:
        _bridge
        print("_bridge exists, probably running under ipyghidra")
    except NameError:
        print("Connecting to bridge")
        _bridge = ghidra_bridge.GhidraBridge(namespace=globals())
    ghidra = _bridge.bridge.remote_import("ghidra")
    java = _bridge.bridge.remote_import("java")



args = parser.parse_args()

import pycparser
import logging



print("Creating parser")
if args.output:
    full_path = os.path.abspath(args.output)
    dtm = ghidra.program.model.data.FileDataTypeManager.createFileArchive(java.io.File(full_path))
    cparser = ghidra.app.util.cparser.C.CParser(dtm, True, None)
else:
    cparser = ghidra.app.util.cparser.C.CParser()

print("Finished parsing")

with open(args.header_file) as f:
    lines = f.readlines()
    cparser.parse("".join(lines))

if args.output:
    print("Saving to %s" % full_path)
    dtm.save()
    dtm.close()
else:
    types = b.bridge.remote_eval("list(cparser.getDataTypeManager().getAllDataTypes())", cparser=cparser)
    for t in types:
        print(repr(t))


