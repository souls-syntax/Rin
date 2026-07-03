package com.soulssyntax.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst
{
    public static void main(String[] args) throws IOException
    {
        if(args.length != 1)
        {
            System.err.println("usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputdir = args[0];

        defineAst(outputdir, "Expr", Arrays.asList(
                      "Binary : Expr left, Token operator, Expr right",
                      "Grouping : Expr expression",
                      "Literal : Object value",
                      "Unary : Token operator, Expr right"
                      ));
    }
    private static void defineAst(
        String outputdir, String basename, List<String> types)
        throws IOException
    {
        String path = outputdir + "/" + basename + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.soulssyntax.rin;");
        writer.println();
        writer.println();
        writer.println("abstract class " + basename + " {");

        defineVisitor(writer, basename, types);

        for (String type: types)
        {
            String classname = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, basename, classname, fields);
        }

        writer.println();
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(
        PrintWriter writer, String baseName, List<String> types
        )
    {
        writer.println("    interface Visitor<R> {");

        for (String type : types)
        {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }

    private static void defineType (
        PrintWriter writer, String basename,
        String classname, String fieldlist)
    {
        writer.println("    static class " + classname + " extends " + basename + " {");

        // constructor
        writer.println("    " + classname + "(" + fieldlist + ") {");

        String[] fields = fieldlist.split(", ");
        for(String field : fields)
        {
            String name = field.split(" ")[1];
            writer.println("    this." + name + " = "+ name + ";");
        }
        writer.println("    }");


        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("        return visitor.visit" + classname + basename + "(this);");
        writer.println("    }");
        writer.println();

        writer.println();
        for(String field : fields)
        {
            writer.println("    final " + field + ";");
        }

        writer.println("    }");
    }
}
