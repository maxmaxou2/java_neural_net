//MOSTLY GENERATED USING GPT3

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResizer {

    public static void resize(String sourceImagePath, String destImagePath, int targetWidth, int targetHeight) throws IOException {
        File file = new File(sourceImagePath);
        File destFolder = new File(destImagePath);

        if (!destFolder.exists()) {
            return;
        }

        if (file.isFile() && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png"))) {
            String formatName = file.getName().toLowerCase().endsWith(".jpg") ? "jpg" : "png";
            BufferedImage originalImage = ImageIO.read(file);
            Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedResizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = bufferedResizedImage.createGraphics();
            g2d.drawImage(resizedImage, 0, 0, null);
            g2d.dispose();

            File outputFile = new File(destFolder, file.getName());
            ImageIO.write(bufferedResizedImage, formatName, outputFile);
        }
    }

    public static void resizeAll(String sourceFolderPath, String destFolderPath, int targetWidth, int targetHeight) throws IOException {
        File sourceFolder = new File(sourceFolderPath);
        File destFolder = new File(destFolderPath);

        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }

        File[] files = sourceFolder.listFiles();

        for (File file : files) {
            if (file.isFile() && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png"))) {
                String formatName = file.getName().toLowerCase().endsWith(".jpg") ? "jpg" : "png";
                BufferedImage originalImage = ImageIO.read(file);
                Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                BufferedImage bufferedResizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

                Graphics2D g2d = bufferedResizedImage.createGraphics();
                g2d.drawImage(resizedImage, 0, 0, null);
                g2d.dispose();

                File outputFile = new File(destFolder, file.getName());
                ImageIO.write(bufferedResizedImage, formatName, outputFile);
            }
        }
    }
}
