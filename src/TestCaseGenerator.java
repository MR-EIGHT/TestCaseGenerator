import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class TestCaseGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Number of Test Cases: ");
        int numberOfTestCases = scanner.nextInt();

        System.out.println("Enter Number of Test Lines In Each File: ");
        int numberOfTestLines = scanner.nextInt();

        StringBuilder builder = new StringBuilder();


        for (int i = 1; i <= numberOfTestCases; i++) {
            for (int j = 1; j <= numberOfTestLines; j++) {

                scanner = new Scanner(System.in);
                System.out.println("Enter The " + j + " th Line of The Input " + i + ":");
                builder.append(scanner.nextLine()).append("\n");
            }
            saveToFile("input" + i + ".txt", builder, true);

            builder = new StringBuilder();
            System.out.println("\n");
            for (int j = 1; j <= numberOfTestLines; j++) {
                scanner = new Scanner(System.in);
                System.out.println("Enter The " + j + " th Line of The Output " + i + ":");

                builder.append(scanner.nextLine()).append("\n");


            }
            saveToFile("output" + i + ".txt", builder, false);
            builder = new StringBuilder();
            System.out.println("\n");
        }

        ZipFolderSeven zs = new ZipFolderSeven();
        zs.zippingInSeven();
        System.out.println("Zip file testcases generated successfully!");
        deleteTemp(new File(".\\temp"));
    }

    private static void saveToFile(String name, StringBuilder builder, Boolean input) {

        try {
            File f = new File(".\\temp");
            f.mkdir();
            String a = "";
            if (input) a += "in";
            else a += "out";


            f = new File(".\\temp\\" + a);

            f.mkdir();
            FileWriter fw = new FileWriter(".\\temp\\" + a + "\\" + name);
            fw.write(builder.toString());
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("The test is written to file named: " + name);
    }


    private static void deleteTemp(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteTemp(file);
            }
        }
        directoryToBeDeleted.delete();
    }


    public static class ZipFolderSeven {
        static final String FOLDER = ".\\temp";

        private void zippingInSeven() {
            try (FileOutputStream fos = new FileOutputStream("testcases".concat(".zip"));
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                Path sourcePath = Paths.get(FOLDER);
                Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                        if (!sourcePath.equals(dir)) {
                            zos.putNextEntry(new ZipEntry(sourcePath.relativize(dir).toString() + "/"));
                            zos.closeEntry();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                        zos.putNextEntry(new ZipEntry(sourcePath.relativize(file).toString()));
                        Files.copy(file, zos);
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}



