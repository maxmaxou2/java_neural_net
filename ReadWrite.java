import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

public class ReadWrite {

    public static void serializeObject(Object obj, String path) {
        // Serialization
        try (FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserializeObject(String path) {
        Object ans = null;
        try (FileInputStream fileIn = new FileInputStream(path);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            
            ans = in.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static void delete(String path) {
        new File(path).delete();
    }

    public static void write(String path, String object) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
        new File(path).mkdirs();
        BufferedWriter bufWriter = new BufferedWriter(writer);
        bufWriter.write(object);
        bufWriter.close();
    }

    public static void write(String path, byte[] data) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();
    }

    public static String readText(String path) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
        BufferedReader bufReader = new BufferedReader(reader);
        String ans = "";

        String nextLine = bufReader.readLine();
        while (nextLine != null) {
            ans += nextLine + "\n";
            nextLine = bufReader.readLine();
        }
        bufReader.close();
        return ans.trim();
    }

    public static byte[] readData(String path) throws IOException {
        return Files.readAllBytes(new File(path).toPath());
    }

    public static boolean exists(String path) {
        File tmpDir = new File(path);
        return tmpDir.exists();
    }
}
