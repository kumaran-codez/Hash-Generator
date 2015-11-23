import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
* This program will recursively search all folders (starting at the given
* base) for .pom/.jar/.xml files and generate the md5 and sha1 hash file for
* each. All file names are printed to a file.
*
*/
public class MyGenerateHashes {
 
                // Regular expression for *.jar|*.pom|*.xml files
                private static final String FILE_PATTERN = "([^\\s]+(\\.(?i)(jar|pom|xml|tgz|zip))$)";
 
                private final Pattern filePattern;
                private ArrayList<String> fileNamesList;
 
                /**
                * Creates the constructor for GenerateHashes.
                * Constructor
                */
                public MyGenerateHashes() {
                                filePattern = Pattern.compile(FILE_PATTERN);
                                fileNamesList = new ArrayList<String>();
                }
 
               
                /**
                * Main method
                 * @param args
                */
                public static void main(String[] args) {
                                String filePath = "\\C:\\Users\\kramanuj\\Desktop\\Planning-Team";
                               
                                MyGenerateHashes genHashes = new MyGenerateHashes();
                                genHashes.searchDirectory(filePath);
                                genHashes.printAllFileNames();
 
                }
               
                /**
                * Start search - is it a directory? If yes, perform recursive search.
                * @param filePath
                */
                public void searchDirectory (String filePath) {
                                File directory = new File(filePath);                           
                                if(directory.isDirectory()) {
                                                search(directory);
                                } else {
                                                System.out.println("Not a directory!");
                                }
                }
               
                /**
                * Directory search for files using recursion.
                * @param file
                */
                public void search(File file) {
                                if(file.canRead()) {
                                                for(File tmp : file.listFiles()) {
                                                                if(tmp.isDirectory()) {
                                                                                search(tmp);
                                                                } else {
                                                                                // check if file name matches the given file pattern (e.g. pom, jar, xml)
                                                                                // if so, generate the hashes.
                                                                                if(this.checkFileName(tmp)) {
                                                                                                this.prepareHashes(tmp);
                                                                                                fileNamesList.add(tmp.getName());
                                                                                }
                                                                                // add ALL filenames to the list that will be inserted into a text file
                                                                                //fileNamesList.add(tmp.getName());                                  
                                                                }
                                                }
                                }
                }
               
                private boolean checkFileName(File file) {
                                Matcher matcher = filePattern.matcher(file.getName());
                                boolean isMatch = matcher.matches();
                                return isMatch;                
                }
 
 
                private void printAllFileNames() {
                                System.out.println("Printing filenames to file.");
                                File fileNamesFile = new File("C:/file_names.txt");         
                                try {
                                                BufferedWriter bw = new BufferedWriter(new FileWriter(fileNamesFile));
                                                for(String fileName : fileNamesList) {
                                                                bw.write(fileName + "\n");
                                                }
                                                bw.close();
 
                                } catch(IOException e) {
                                                e.printStackTrace();
                                }
                }
 
                public void prepareHashes(File file) {
                                try {
                                                MessageDigest sha1 = MessageDigest.getInstance("sha1");
                                                MessageDigest md5 = MessageDigest.getInstance("md5");
                                                byte[] data = new byte[(int)file.length()];
                                                FileInputStream fis = new FileInputStream(file);
                                                fis.read(data);
                                                fis.close();
                                                String sha1Hash = this.generateHash(sha1, data);
                                                String md5Hash = this.generateHash(md5, data);
                                                this.createHashFile(file, sha1Hash, "sha1");
                                                this.createHashFile(file, md5Hash, "md5");
                                } catch (NoSuchAlgorithmException e) {
                                                e.printStackTrace();
                                } catch (IOException e) {
                                                e.printStackTrace();
                                }
                }
               
                private String generateHash(MessageDigest md, byte[] data) {
                                String hash = "";
                                md.update(data);
 
                                byte[] digest = md.digest();                       
                               
                                for (int i = 0; i < digest.length; i++)
                                {
                                                String hex = Integer.toHexString(digest[i]);
                                                if (hex.length() == 1) hex = "0" + hex;
                                                hex = hex.substring(hex.length() - 2);
                                                hash += hex;
                                }
                                return hash;
                }
               
                public void createHashFile(File file, String hash, String type) {
                                File hashFile = new File(file.getAbsolutePath() + "." + type);
                                try {
                                                BufferedWriter bw = new BufferedWriter(new FileWriter(hashFile));
                                                bw.write(hash);
                                                bw.close();
                                } catch(IOException e) {
                                                e.printStackTrace();
                                }
 
                               
                }
 
}
 