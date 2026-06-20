package com.soulssyntax.utils;

import java.io.ioexception;
import java.io.printwriter;
import java.util.arrays;
import java.util.list;


public class generateast
{
    public static void main(string[] args) throws ioexception
    {
        if(args.length != 1)
        {
            system.err.println("usage: generate_ast <output directory>");
            system.exit(64);
        }
        string outputdir = args[0];

        defineast(outputdir, "expr", arrays.aslist(
                      "binary : expr left, token operator, expr right",
                      "grouping : expr expression",
                      "literal : object value",
                      "unary : token operator, expr right"
                      ));
    }
    private static void defineast(
        string outputdir, string basename, list<string> types)
        throws ioexception
    {
        string path = outputdir + "/" + basename + ".java";
        printwriter writer = new printwriter(path, "utf-8");

        writer.println("package com.soulssyntax.rin;");
        writer.println();
        writer.println();
        writer.println("abstract class " + basename + " {");

        for (string type: types)
        {
            string classname = type.split(":")[0].trim();
            string fields = type.split(":")[1].trim();
            definetype(writer, basename, classname, fields);
        }

        writer.println("}");
        writer.close();
    }


    private static void definetype (
        printwriter writer, string basename,
        string classname, string fieldlist)
    {
        writer.println("    static class " + classname + " extends " + basename + " {");

        // constructor
        writer.println("    " + classname + "(" + fieldlist + ") {");

        string[] fields = fieldlist.split(", ");
        for(string field : fields)
        {
            string name = field.split(" ")[1];
            writer.println("    this." + name + " = "+ name + ";");
        }
        writer.println("    }");

        writer.println();
        for(string field : fields)
        {
            writer.println("    final " + field + ";");
        }

        writer.println("    }");
    }
}
