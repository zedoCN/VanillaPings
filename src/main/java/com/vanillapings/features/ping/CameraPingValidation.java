package com.vanillapings.features.ping;

/** Pure policy, independent of Minecraft so hostile payloads can be regression-tested. */
public final class CameraPingValidation {
    private CameraPingValidation() {}
    public static boolean isValid(double x, double y, double z, double dx, double dy, double dz,
                                  double px, double py, double pz, double range) {
        for (double value : new double[]{x,y,z,dx,dy,dz,px,py,pz,range})
            if (!Double.isFinite(value)) return false;
        if (range == 0 || Math.abs(x) > 30_000_000 || Math.abs(z) > 30_000_000 || Math.abs(y) > 20_000_000) return false;
        double limit = Math.min(range < 0 ? 256 : range, 256);
        double length = dx*dx + dy*dy + dz*dz;
        return length > 0.99 && length < 1.01 &&
                (x-px)*(x-px)+(y-py)*(y-py)+(z-pz)*(z-pz) <= limit*limit;
    }
}
