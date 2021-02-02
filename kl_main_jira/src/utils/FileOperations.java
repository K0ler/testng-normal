package utils;

import main.Main;

import java.io.*;

public class FileOperations extends Main {
    private static String rootProjectPath = (new File(".").getAbsoluteFile()).toString();
    private static String resultsPath = rootProjectPath + "\\result\\attachments\\";

    public static void SaveToCSV(String FileName, Object[] line, boolean append) throws IOException {
//      BufferedWriter writer = new BufferedWriter(new FileWriter(resultsPath + FileName + ".csv", append));
        Writer fstream = new OutputStreamWriter(
                new FileOutputStream(resultsPath + FileName + ".csv", append),
                "CP1250"
        );
        BufferedWriter writer = new BufferedWriter(fstream);


        System.out.println("Grabbing ["+resultsPath + FileName + ".csv]");
        for (Object el : line) {
            writer.write(el.toString() + ";");
        }
        writer.write("\n");
        writer.close();
    }

    public static void deleteResultFile(String fileName) {
        deleteFile(resultsPath + fileName + ".csv");
    }

    private static void deleteFile(String filePath){
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("File [" + filePath + "] deleted from directory");
        } else System.out.println("File [" + filePath + "] doesn't exist in the directory");
    }
}
