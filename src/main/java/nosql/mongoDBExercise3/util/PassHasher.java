package nosql.mongoDBExercise3.util;

import org.mindrot.jbcrypt.BCrypt;
//https://www.mindrot.org/projects/jBCrypt/

public class PassHasher {
    
    //ONE WAY HASHING
    
    /*storing the password in a plain-text field is a horrible idea.
    We will be storing the representation of a password in the database. By representation we want to hash the password using a salt (which should be different for every user) and a secure 1-way algorithm and store that, throwing away the original password.
    Then, when you want to verify a password, you hash the input value (using the same hashing algorithm and salt) and compare it to the hashed value in the database.*/
    
    //JBCRYPT
    
    /*The bcrypt function is the default password hash algorithm for BSD and other systems including some Linux distributions such as SUSE Linux.[2] The prefix "$2a$" or "$2b$" (or "$2y$") in a hash string in a shadow password file indicates that hash string is a bcrypt hash in modular crypt format.[3] The rest of the hash string includes the cost parameter, a 128-bit salt (base-64 encoded as 22 characters), and 184 bits of the resulting hash value (base-64 encoded as 31 characters).[4] The cost parameter specifies a key expansion iteration count as a power of two, which is an input to the crypt algorithm.

For example, the shadow password record $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy specifies a cost parameter of 10, indicating 210 key expansion rounds. The salt is N9qo8uLOickgx2ZMRZoMye and the resulting hash is IjZAgcfl7p92ldGxad68LJZdL17lhWy. Per standard practice, the user's password itself is not stored.*/
    
    
    /*Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.*/
    private static int workload = 12;
    
    public static String hashPassword(String password_plaintext) {
        //generating salt
        String salt = BCrypt.gensalt(workload);
        //hashing the password
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        
        return(hashed_password);
    }
    
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;
        
        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        
        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);
        
        return(password_verified);
    }
    
    public static void main(String[] args) {
        String plaintext1 = "serin";
        System.out.println("hashing :" + plaintext1);
        String hashed1 = hashPassword(plaintext1);
        System.out.println(hashed1);
    
        System.out.println(checkPassword(plaintext1, hashed1));
    
        String hashed2 = hashPassword(plaintext1);
        System.out.println(hashed2);
    }
}
