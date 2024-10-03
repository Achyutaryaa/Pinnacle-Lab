import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.security.SecureRandom;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageEncryption {

    public static void main(String[] args) throws Exception {

        // path and name of the original image.
        String imagePath = "/home/aaryaa/Desktop/Aarya.jpg";

        // name of the encrypted image
        String encryptedImagePath = "encrypted_image.aes";

        // name of the decrypted image. You can also include desirable folder's path to store.
        String decryptedImagePath = "decrypted_image.jpg";

        SecretKey secretKey = generateKey();
        IvParameterSpec iv = generateIv();

        // Encrypt the image
        byte[] encryptedData = encryptImage(imagePath, secretKey, iv);
        Files.write(new File(encryptedImagePath).toPath(), encryptedData);
        System.out.println("Image encrypted successfully!");

        // Decrypt the image
        byte[] decryptedData = decryptImage(encryptedData, secretKey, iv);
        saveDecryptedImage(decryptedData, decryptedImagePath);
        System.out.println("Image decrypted successfully!");
    }

    // Generate a random AES key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    // Generate a random initialization vector (IV)
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Encrypt the image
    public static byte[] encryptImage(String imagePath, SecretKey key, IvParameterSpec iv) throws Exception {
        BufferedImage image = ImageIO.read(new File(imagePath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(imageBytes);
    }

    // Decrypt the image
    public static byte[] decryptImage(byte[] encryptedData, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }

    // Save decrypted image bytes to file
    public static void saveDecryptedImage(byte[] imageBytes, String outputPath) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bais);
        ImageIO.write(image, "jpg", new File(outputPath));
    }
}
