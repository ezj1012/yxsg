package com.yxbear.sg.coder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

public class G666 {

    public static void main(String[] args) throws IOException {
        File[] fs = new File("D:\\projects\\yxbear.top\\yxsg\\sg\\src\\test\\java\\com\\yxbear\\sg\\coder").listFiles(f -> f.getName().endsWith(".java"));
        for (File file : fs) {
            System.err.println("=======================================================");
            List<String> allLines = Files.readAllLines(file.toPath());
            allLines.forEach(str -> {

                str = str.trim();
                if (str.startsWith(".model")) {
                    System.err.println("=================================");
                    System.out.println(str);
                }
                if (str.startsWith(".attr")) {
                    try {
                        printAttr(str);
                    } catch (Exception e) {
                        System.err.println(str);
                    }

                }
            });
        }

    }

    public static void printAttr(String str) {
        String type = str.substring(5, str.indexOf("(\"")).toLowerCase();
        int idx = str.indexOf("\"");
        int idx2 = str.indexOf("\"", idx + 1);
        String name = str.substring(idx + 1, idx2);
        String dd = null;
        str = str.substring(idx2 + 1);
        idx = str.indexOf("\"");
        idx2 = str.indexOf("\"", idx + 1);
        if (idx > 0) {
            dd = str.substring(idx + 1, idx2);
        }

        System.out.println(name + ": " + type + " " + (dd != null ? "// " + dd : ""));
    }

}
