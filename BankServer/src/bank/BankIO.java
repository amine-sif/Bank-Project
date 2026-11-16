package bank;


import java.io.*;

public class BankIO {
    public static final String BANK_FILE_NAME = "bank.data";

    /**
     * Saves a Bank object in the file whose name is in the BANK_FILE_NAME attribute.
     *
     * @param bank the bank
     * @return true if the save succeeds
     */
    public static boolean save(Bank bank) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BANK_FILE_NAME))) {
            oos.writeObject(bank);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Retrieves a Bank object from the file whose name is in the BANK_FILE_NAME attribute.
     *
     * @return the bank object read from the file. Returns null if there is a reading problem
     */
    public static Bank load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BANK_FILE_NAME))) {
            return (Bank)ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
