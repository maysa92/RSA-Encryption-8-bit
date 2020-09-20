import java.util.*; 
import java.util.Random;
import java.math.BigInteger;

//p and q -> 8 bits long
//encrypt 1 letter at a time
public class RSA {
    long modular(BigInteger a, long b){  
        BigInteger bbig = BigInteger.valueOf(b);
        BigInteger resultult = a.remainder(bbig);
        long result = resultult.longValue();
        return result;
        }

    long gcd(long a, long b){
        long getgcd = 1;
        for(int i = 1; i <= a && i <= b; i++){
            if(a%i == 0 && b%i == 0)
                getgcd = i;
        }
        return getgcd;
    }

    long Phifunction(long n){
    int count = 0;
    int divisor = 0;
    int nint = (int)n;
    for (int i = 2; i < nint; i++) {
                if (nint % i == 0)
                divisor = i;
        }
        System.out.println("divisor: "+divisor);
        count = (divisor - 1) * (nint/divisor - 1);
    return(count);
    }


	boolean MillerTest(long n) {
        long tmp = n-1;
        if (n == 0 || n == 1){
            return false;
        }else if (n == 2){
            return true;
        }else if (n % 2 == 0){
            return false;
        }
        while(tmp %2 == 0){
        tmp /= 2;
        }
        int x = 12;
        Random rand = new Random();
        for (int i = 0; i < x; i++){
            long r = Math.abs(rand.nextLong());            
            long a = r % (n-1) + 1;
            long mod = PowerModular(a, tmp, n);
            while (tmp != n - 1 && mod != 1 && mod != n - 1){
                mod = (mod*mod) %n;
                tmp *= 2;
            }
            if (mod != n - 1 && tmp % 2 == 0)
                return false;
        }
        return true;        
    }
    
    // Calculate (a to power of b) % c 
    long PowerModular(long a, long b, long c){
        long result = 1;
        for (int i = 0; i < b; i++)
        {
            result *= a;
            result %= c; 
        }
        return result % c;
    }

    //generate an 8-bit prime
    int Primegeneration(int max, int min){
        Random rand = new Random();
        boolean isprime = false;
        int randomNum = 0;
        int randomresultult = 0;
        while(isprime == false){
        randomNum = rand.nextInt((max - min) + 1) + min;
        isprime = MillerTest(randomNum);
        }
        randomresultult = randomNum;
        return randomresultult;
    }

    int n;
    int e;
    int d;
    char plaintext;
    //encryption
	long encryption(int p, int q){
        n = p * q;
        long phiofn = Phifunction((long)n);
        System.out.println("n = "+ n + "," + "Phi of n = "+phiofn);
        e = Primegeneration(15, 2);
        while (phiofn % e == 0){
            e = Primegeneration(15, 2);
        }
        System.out.println("e = "+ e);
        for(int i = 0; i< phiofn*phiofn; i++){
            if((i * e)% phiofn == 1){
                d = i;
                break;
            }
        }
        System.out.println("d = "+ d);
        
        Scanner userplaintext = new Scanner(System.in);  
        System.out.print("Enter the plain text ( 1 letter ): ");
        this.plaintext = userplaintext.next().charAt(0);

        //convert plaintext to ascii
        int ascii = (int) plaintext;
        BigInteger y = squareandmultiply(ascii, e);
        long ylong = modular(y, n);
        System.out.println("Encrypted: "+ylong);
        return ylong;
    }

    BigInteger squareandmultiply(long base, int exp){
        String bin = Integer.toBinaryString(exp);
        int[] exparray = Arrays.stream(bin.split("")).mapToInt(Integer::parseInt).toArray();
        BigInteger[] step = new BigInteger[exparray.length + 1];
        step[1] = BigInteger.valueOf(base);
        BigInteger nbig = BigInteger.valueOf(n);
        step[1] = step[1].remainder(nbig);
        for (int i = 1; i < exparray.length; i++){
            step[i+1] = step[i].multiply(step[i]);
            step[i+1] = step[i+1].remainder(nbig);
            if(exparray[i] == 1){
                step[i+1] = step[i+1].multiply(step[1]);
                step[i+1] = step[i+1].remainder(nbig);
            }
        }
        //System.out.println("y = "+step[exparray.length]);
        return step[exparray.length];
    } 

    //decryption
    char decryption(long y){
        BigInteger resultbig = squareandmultiply(y, d);
        int tmp = resultbig.intValue();
        char x =(char)tmp;
        System.out.println("Decrypted: "+x);
        return x;
    }

    public static void main(String[] args) {
        RSA test = new RSA();
        BigInteger a = new BigInteger("25584");
        long b = 50;
        long c = 36;
        long d = 23;
        System.out.println("Testing Modular 25584 mod 50 = "+test.modular(a, b));
        System.out.println("Testing GCD gcd(50,36) = "+test.gcd(c, b));
        System.out.println("Testing Phi of 36 = "+test.Phifunction(c));
        System.out.println("Testing MillerTest 36 = "+test.MillerTest(c));
        System.out.println("Testing MillerTest 23 = "+test.MillerTest(d));
        int prime = test.Primegeneration(256, 128);
        System.out.println("Randomly generated p: " +prime);
        int prime2 = test.Primegeneration(256, 128);
        System.out.println("Randomly generated q: " +prime2);
        long startTime = System.nanoTime();
        long y = test.encryption(prime, prime2);
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Execution time for encryption(millis): "
                + elapsedTime/1000000);
        startTime = System.nanoTime();
        test.decryption(y);
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Execution time for decryption(millis): "
                + elapsedTime/1000000);
    }
}
