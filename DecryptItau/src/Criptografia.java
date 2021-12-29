import java.io.BufferedReader;
    import java.io.FileInputStream;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.security.InvalidAlgorithmParameterException;
    import java.security.InvalidKeyException;
    import java.security.KeyFactory;
    import java.security.NoSuchAlgorithmException;
    import java.security.interfaces.RSAPrivateKey;
    import java.security.spec.PKCS8EncodedKeySpec;
    import java.util.Base64;
    import java.util.Scanner;
    
    import javax.crypto.BadPaddingException;
    import javax.crypto.Cipher;
    import javax.crypto.IllegalBlockSizeException;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;
    
    public class Criptografia {
    
        private static String extrairChaveRsaPem(String tipoChave, String arquivoChavesRsa) {
    
            try {
                InputStream is = new FileInputStream(arquivoChavesRsa);
                @SuppressWarnings("resource")
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                boolean inKey = false;
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (!inKey) {
                        if (line.startsWith("-----BEGIN ") && line.endsWith(" " + tipoChave + " KEY-----")) {
                            inKey = true;
                        }
                    } else {
                        if (line.startsWith("-----END ") && line.endsWith(" " + tipoChave + " KEY-----")) {
                            inKey = false;
                            break;
                        }
                        sb.append(line);
                    }
                }
                return sb.toString();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
    
            return null;
        }
    
        public static String decriptografiaAes(SecretKey key, String cipherText) {
    
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(new byte[16]);
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
                byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
    
                return new String(plainText);
            }
            catch(NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch(BadPaddingException e) {
                e.printStackTrace();
            }
            catch(IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            catch(InvalidKeyException e) {
                e.printStackTrace();
            }
            catch(InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            catch(NoSuchPaddingException e) {
                e.printStackTrace();
            }
    
            return null;
        }
    
        private static byte[] decriptografiaRsa(String caminhoChavePrivada, String dadosCifrados) {
    
            try {
                String chavePrivada = extrairChaveRsaPem("PRIVATE", caminhoChavePrivada);
    
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(chavePrivada.toString()));
                KeyFactory kf = KeyFactory.getInstance("RSA");
                RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
    
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
    
                return cipher.doFinal(Base64.getDecoder().decode(dadosCifrados));
    
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            return null;
        }
    
        public static void main(String[] args)
        {
            SecretKey chaveSessao = null;
            Scanner scanner = new Scanner(System.in);
    
            try {
                System.out.println("\n=========================================");
                System.out.println("Informe as informações recebidas no e-mail");
                System.out.println("=========================================");
    
                System.out.println("\nClientId: ");
                System.out.flush();
                String clientIdCifrado = scanner.nextLine().trim();
    
                System.out.println("\nToken Temporário: ");
                System.out.flush();
                String tokenCifrado = scanner.nextLine().trim();
    
                System.out.println("\nChave Sessão: ");
                System.out.flush();
                String chaveSessaoCifrada = scanner.nextLine().trim();
    
                System.out.println("\nCaminho chave privada: ");
                System.out.flush();
                String caminhoChavePrivada = scanner.nextLine().trim();
    
                System.out.flush();
                scanner.close();
    
                System.out.println("\n=====================================");
                System.out.println("    Processo de Decriptografia         ");
                System.out.println("=====================================");
    
                // Decifra a chave de sessao AES com a chave RSA privada
                byte[] chaveSessaoDecifrada = decriptografiaRsa(caminhoChavePrivada, chaveSessaoCifrada);
                chaveSessao = new SecretKeySpec(chaveSessaoDecifrada, 0, chaveSessaoDecifrada.length, "AES");
                // Decriptografa a credencial atraves da chave de sessao AES
                String clientIdDecifrada = decriptografiaAes(chaveSessao, clientIdCifrado);
                System.out.println("\nClient id decifrado com a chave de sessao AES:\n[ " + new String(clientIdDecifrada) + " ]");
                String tokenDecifrado = decriptografiaAes(chaveSessao, tokenCifrado);
                System.out.println("\nToken decifrado com a chave de sessao AES:\n[ " + new String(tokenDecifrado) + " ]");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }