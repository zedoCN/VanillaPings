import com.vanillapings.features.ping.CameraPingValidation;

public class CameraPingValidationTest {
    static void check(boolean expected, double x, double y, double z, double dx, double dy, double dz, double range) {
        if (CameraPingValidation.isValid(x,y,z,dx,dy,dz,0,64,0,range) != expected)
            throw new AssertionError("Unexpected validation result");
    }
    public static void main(String[] args) {
        check(true, 0,65.62,0,0,0,1,500);
        check(true, 120,80,-40,1,0,0,500);
        check(false, 257,64,0,1,0,0,500);
        check(false, 30,64,0,1,0,0,20);
        check(false, 0,64,0,0,0,0,500);
        check(false, 0,64,0,2,0,0,500);
        check(false, Double.NaN,64,0,1,0,0,500);
        check(false, 0,64,0,Double.POSITIVE_INFINITY,0,0,500);
        check(false, 0,64,0,1,0,0,0);
        check(false, 0,64,0,1,0,0,Double.POSITIVE_INFINITY);
        check(true, 256,64,0,1,0,0,-1);
        System.out.println("11 camera payload policy checks passed");
    }
}
