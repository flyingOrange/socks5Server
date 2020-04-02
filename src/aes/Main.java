package aes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Ettore Ciprian <cipettaro@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AES aes = new AES();
        System.out.println("***************************************************************");
        System.out.println("*  JCRYTPO - An AES Java implementation with CBC/CFB modes    *");
        System.out.println("*                                                             *");
        System.out.println("*       Author: Ettore Ciprian - cipettaro@gmail.com          *");
        System.out.println("***************************************************************");

        switch (args[0]) {

            case "--generate-key":

                if (args.length < 2) {
                    System.err.println("Please provide a path where to save the key");
                    System.exit(1);
                } else {
                    if (args.length < 3) {
                        aes.printKeytoFile(args[1]);
                    } else if (!args[2].matches("(128|192|256)")) {
                        System.err.println("Please provide a valid key size (128, 192 or 256)");
                        System.exit(1);
                    } else {
                        aes.setKeySize(Integer.parseInt(args[2]));
                        aes.printKeytoFile(args[1]);
                    }

                }

                break;
            case "--encrypt":
                if (args.length < 3) {
                    System.err.println("Please provide correct arguments for encrypt operation: INPUT_PATH OUTPUT_PATH [KEY] [MODE]");
                    System.exit(1);
                } else if (!Paths.get(args[1]).toFile().canRead() || args[2] == null) {
                    System.err.println("Please provide valid paths! Program terminating..");
                    System.exit(1);
                } else {
                    String in = "";
                    try {
                        in = readFile(args[1], Charset.defaultCharset());
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (args[3] == null) {
                        System.err.println("Please provide valid key path! Program terminating..");
                        System.exit(1);
                    } else {
                        String key = "";
                        try {
                            key = readFile(args[3], Charset.defaultCharset());
                        } catch (IOException ex) {
                            System.out.println("Please provide a valid key path! Program terminating..");
                            System.exit(1);
                        }
                        byte[] out = null;
                        try {
                            switch (args[4]) {
                                case "CBC": {
                                    out = aes.encrypt(in, key, "CBC");
                                    break;
                                }
                                case "CFB": {
                                    out = aes.encrypt(in, key, "CFB");
                                    break;
                                }
                                default:
                                    System.err.println("Please provide a valid encryption mode (CBC|CFB)! Program terminating..");
                                    System.exit(1);
                            }
                            Path outputPath = Paths.get(args[2]);
                            if (outputPath.toFile().exists()) {
                                outputPath.toFile().delete();
                            }
                            FileOutputStream outStream = new FileOutputStream(outputPath.toFile(), true);
                            outStream.write(out);
                            outStream.close();
                        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchPaddingException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                break;

            case "--decrypt":
                if (args.length < 3) {
                    System.err.println("Please provide correct arguments for decrypt operation: INPUT_PATH OUTPUT_PATH [KEY] [MODE]");
                    System.exit(1);
                } else if (!Paths.get(args[1]).toFile().canRead() || args[2] == null) {
                    System.err.println("Please provide valid paths! Program terminating..");
                    System.exit(1);
                } else {
                    if (args[3] == null) {
                        System.err.println("Please provide valid key path! Program terminating..");
                        System.exit(1);
                    } else {
                        String out = "";
                        String key = "";
                        try {
                            key = readFile(args[3], Charset.defaultCharset());
                        } catch (IOException ex) {
                            System.out.println("Please provide a valid key path! Program terminating..");
                            System.exit(1);
                        }
                        try {
                            byte[] in = null;
                           
                            switch (args[4]) {
                                case "CBC": {
                                    in = readFile(args[1], args[4], null);
                                    out = aes.decrypt(in, key, "CBC", null);
                                    break;
                                }
                                case "CFB": {
                                     byte[] iv = new byte[16];
                                    in = readFile(args[1], "CFB", iv);
                                    out = aes.decrypt(in, key, "CFB", iv);
                                    break;
                                }
                                default:
                                    System.err.println("Please provide a valid decryption mode (CBC|CFB)! Program terminating..");
                                    System.exit(1);
                            }
                            Path outputPath = Paths.get(args[2]);
                            if (!outputPath.toFile().exists()) {
                                Files.createFile(outputPath);
                            } else {
                                outputPath.toFile().delete();
                            }
                            try (FileOutputStream outStream = new FileOutputStream(outputPath.toFile(), true)) {
                                outStream.write(out.getBytes());
                            }
                        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;

            default:
                System.err.println("Please provide a valid command! Program terminating..");
                System.exit(1);

        }
        System.exit(0);
    }

    /**
     * Read file to a single String
     *
     * @param path
     * @param encoding
     * @return String
     * @throws IOException
     */
    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * *
     * Read a cipher file
     *
     * @param path
     * @return
     * @throws IOException
     */
    private static byte[] readFile(String path, String mode, byte[] iv) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        if ("CFB".equals(mode)) {
            int y = 0;
            for (int i = encoded.length - 16; y < 16; i++) {
                iv[y] = encoded[i];
                y++;
            }
           
            byte[] encoded_shrinked = new byte[encoded.length-16];
            System.arraycopy(encoded, 0, encoded_shrinked, 0, encoded.length-16);
            
            return encoded_shrinked;
        }
        return encoded;
    }
}
