
import argparse
import os

from pycparser.c_ast import Typedef, TypeDecl, ExprList
from pycparserext.ext_c_parser import AttributeSpecifier, FuncDeclExt

parser = argparse.ArgumentParser(description='Clean up headers for Ghidra')
parser.add_argument('header_file',
                    help='The header file to parse')
parser.add_argument('--output', '-o',
                    help='Path of the output file')
#parser.add_argument('--overwrite', '-f', help="Overwrite the output file if it already exists", action='store_true')


args = parser.parse_args()

from pycparserext.ext_c_generator import GnuCGenerator
import pycparser


class StripGnuCGenerator(GnuCGenerator):
    def visit_AttributeSpecifier(self, n: AttributeSpecifier):
        return ""

    # def visit_Typedef(self, n: Typedef):
    #     regular_result = super().visit_Typedef(n)
    #     if n.name == "va_list":
    #         print(f"Returning '' instead of {regular_result}")
    #         return ""
    #     type_str = self._generate_type(n.type)
    #     if '__builtin_va_list' in type_str:
    #         s = ''
    #         if n.storage: s += ' '.join(n.storage) + ' '
    #         s += 'va_list'
    #         s += f' {n.name}'
    #         print(f"Returning '{s}' instead of '{regular_result}' for {str(n)}")
    #         return s
    #     return regular_result

    def visit_Decl(self, n, no_type=False):
        orig = super().visit_Decl(n, no_type)
        #if "__atribute__" in
        if isinstance(n.type, FuncDeclExt):
            if n.type.attributes and isinstance(n.type.attributes, ExprList):
                x = 1
                pass
        return orig
    pass


from pycparserext.ext_c_parser import GnuCParser
gnu_parser = GnuCParser()

src = open(args.header_file).read()

ast = gnu_parser.parse(src)


cleaned_source = StripGnuCGenerator().visit(ast)

plain_parser = pycparser.CParser()

#clean_ast = plain_parser.parse(cleaned_source)

if args.output:
    f = open(args.output, "w")
    f.write(cleaned_source)
else:
    print(cleaned_source)

x = 1

# from pycparser import c_generator
# generator = c_generator.CGenerator()
#
# print(generator.visit(ast))