package com.yxbear.sg.coder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

public class G666 {

    public static void main(String[] args) throws IOException {
        File[] fs = new File("D:\\workspace\\tyy\\tyy-sg\\tyy-sg\\tyy-sg-comm\\src\\main\\java\\com\\tyy\\sg\\domain\\model\\gi").listFiles(f -> f.getName().endsWith(".java"));
        for (File file : fs) {
            System.err.println("=======================================================");
            System.out.println(file.getName());
            List<String> allLines = Files.readAllLines(file.toPath());
            int start = 0;
            String aa = "";
            for (String str : allLines) {
                String st = str.trim();
                if (st.startsWith("/**")) {
                    start = 1;
                    aa = "//" + st.substring(3, st.length() - 3).trim();
                    // System.out.println(aa);
                } else if (start == 1) {
                    st = st.replace("private ", "").trim();
                    st = st.substring(0, st.length() - 1);
                    String[] split = st.split(" ");
                    System.out.println(split[1] + ": " + split[0].toLowerCase() + " " + aa);
                    start = 0;
                }
            }

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
