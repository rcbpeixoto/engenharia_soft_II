package br.fbv.cryptosvault.model.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.content.Context;

/**
 * Project: Cryptos Vault Class: FileEncrypter REVISION HISTORY Date          Developer       							  Comment ----------    -------------------------------------------- ---------------------------------------------- 9/11/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>     Code Revision ----------    -------------------------------------------- ----------------------------------------------
 */
public class FileEncrypter {
	
	//FileEncrypter instance for singleton
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static FileEncrypter 	  instance;
	
	//Private hashtable for fileNames storage and display
    private Hashtable<String, String> fileNames;
    
    //Activity context for information display
    private Context                   context;
    
    //Encryption cypher 
    private Cipher                    encCipher;
    
    //Decryption cypher
    private Cipher                    decCipher;
    
    //Buffer
    private byte[]                    buffer             = new byte[1024];
    
    //Salt
    private static final byte[]       SALT            = {  (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
           												   (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03  };
    //Iteration Counts
    private static final int          ITERATION_COUNT = 19;

    public static FileEncrypter getInstance(String pass, Context context) {
        if (instance == null) {
            instance = new FileEncrypter(pass, context);
        }
        return instance;
    }

    public static FileEncrypter getInstance() throws IllegalArgumentException {
        if (instance == null) {
            throw new IllegalArgumentException("First you need to initialize properly");
        }
        return instance;
    }

    private FileEncrypter(String pass, Context context) {
    	
        this.context = context;
        KeySpec ks = new PBEKeySpec(pass.toCharArray(), SALT, ITERATION_COUNT);
        SecretKey key = null;
        try {
            key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(ks);
        } catch (InvalidKeySpecException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        // Initialization Feed.
        byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A };
        AlgorithmParameterSpec aps = new PBEParameterSpec(iv, 19);
        try {
            encCipher = Cipher.getInstance(key.getAlgorithm());
            decCipher = Cipher.getInstance(key.getAlgorithm());

            // Cipher objects initialization.
            encCipher.init(Cipher.ENCRYPT_MODE, key, aps);
            decCipher.init(Cipher.DECRYPT_MODE, key, aps);
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    public void encrypt(InputStream in, OutputStream out) {
        try {
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(out, encCipher);
            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buffer)) >= 0) {
                out.write(buffer, 0, numRead);
            }
            out.close();
        } catch (java.io.IOException e) {
        }
    }

    public void decrypt(InputStream in, OutputStream out) {
        try {
            // Bytes read from in will be decrypted
            in = new CipherInputStream(in, decCipher);
            // Read in the decrypted bytes and write the cleartext to out
            int numRead = 0;
            while ((numRead = in.read(buffer)) >= 0) {
                out.write(buffer, 0, numRead);
            }
            out.close();

        } catch (java.io.IOException e) {
        }
    }

    public String getEncryptFileName(String name) {

        if (fileNames == null) {
            fileNames = new Hashtable<String, String>();
            fileNames = loadNames();
        }

        name = validateName(name);
        String newName = generateName();

        while (fileNames.containsKey(newName)) {
            newName = generateName();
        }

        fileNames.put(newName, name);
        saveNamesOnPrivateMode(fileNames);

        return newName;
    }

    public void removeReference(String name) {
        if (fileNames != null) {
            fileNames.remove(name);
            saveNamesOnPrivateMode(fileNames);
        }
    }

    // Esse metodo eh responsavel por validar o nome dos arquivos
    private String validateName(String name) {

        Iterator<String> e = fileNames.keySet().iterator();
        String string;

        String nameAdapted;
        String nameFirstPart, nameSecondPart, aux;
        int value = 1;

        // Varreremos todos os arquivos ja adicionados ao cofre
        while (e.hasNext()) {
            string = (String) e.next();

            // Se existir um arquivo com o mesmo nome do que esta sendo
            // adicionado
            if (name.equalsIgnoreCase(fileNames.get(string))) {

                // verifica se o arquivo tem extensao
                if (name.contains(".")) {

                    // Se tiver extensao recuperamos a parte "antes do ponto"
                    nameFirstPart = name.substring(0, name.lastIndexOf('.'));
                    nameSecondPart = name.substring(name.lastIndexOf('.'), name.length());

                    // Verificamos se o arquivo ja possui o numero no formato
                    // nome(x).
                    if (nameFirstPart.contains("(")) {

                        // se ja tiver vamos recuperar o numero e fazer um ++
                        // para que seja criado um novo arquivo.
                        try {
                            aux = nameFirstPart.substring(nameFirstPart.lastIndexOf('(') + 1,
                                    nameFirstPart.lastIndexOf(')'));
                            value = Integer.parseInt(aux);
                            value++;
                        } catch (NumberFormatException nfe) {

                            // se algum erro acontecer. sera gerado um numero
                            // aleatorio para colocar dentro dos parenteses
                            Random r = new Random();
                            nameFirstPart += "(" + r.nextInt() + ")";
                        }
                        nameFirstPart = nameFirstPart.substring(0, nameFirstPart.lastIndexOf('('));
                        nameAdapted = nameFirstPart + "(" + value + ")" + nameSecondPart;

                    } else {
                        // ao finao sempre juntamos primeira parte do nome +
                        // (numero) + extensao
                        nameAdapted = nameFirstPart + "(1)" + nameSecondPart;
                    }
                } else {
                    // se o arquivo nao possuir extensao adicionamos direto o
                    // numero
                    nameAdapted = name + "(1)";
                }

                // ao fim chamamos recursivamente esse metodo para garantir que
                // seja criado sempre o proximo numero.
                name = validateName(nameAdapted);
            }

        }

        return name;
    }

    public String getDecryptFileName(String name) {
        String realName;

        if (fileNames == null) {
            fileNames = new Hashtable<String, String>();
            fileNames = loadNames();
        }

        realName = fileNames.get(name);

        if (realName == null) {
            realName = "";
        }

        return realName;
    }

    private String generateName() {
        String newName = "";
        Random r = new Random();
        newName = "tempfile" + (System.currentTimeMillis() + r.nextLong());
        return newName;
    }

    private void saveNamesOnPrivateMode(Hashtable<String, String> fileNames) {
        try {
            FileOutputStream fos = this.context.openFileOutput("hashnames.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(fileNames);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Hashtable<String, String> loadNames() {

        Hashtable<String, String> names = new Hashtable<String, String>();
        try {
            FileInputStream fis = this.context.openFileInput("hashnames.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            names = (Hashtable<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return names;
    }
}
