package org.firstinspires.ftc.teamcode.common.util;
import static java.lang.Math.*;
public class AsymProfile extends MotionProfile {
    public static class AsymConstraints {
        public final double vm;
        public final double ai;
        public final double af;
        public AsymConstraints(double vm, double ai, double af) {
            this.vm = vm;
            this.ai = ai;
            this.af = af;
        }
        public AsymConstraints scaleX(double f) {
            return new AsymConstraints(vm * f, ai * f, af * f);
        }
        public AsymConstraints scaleT(double f) {
            return new AsymConstraints(vm * f, ai * f * f, af * f * f);
        }
    }
    public static final double EPS = 1e-6;
    private AsymConstraints c;
    private MotionState f;
    private double t1;
    private double t2;
    private int sgn;
    public AsymProfile(AsymConstraints c, double ti, MotionState i, MotionState f) {
        this.c = c;
        this.ti = ti;
        this.i = i;
        this.f = f;
        sgn = (f.x < i.x) ? -1 : 1;
        double v2 = Math.sqrt((2 * c.ai * c.af * abs(f.x - i.x) + c.af * i.v * i.v + c.ai * f.v * f.v) / (c.ai + c.af));
        if (sgn == 1 && v2 + EPS < max(i.v, f.v) || sgn == -1 && -v2 - EPS > min(i.v, f.v)) {
            throw new IllegalArgumentException("Impossible profile " + min(abs(v2 - max(i.v, f.v)), abs(v2 + min(i.v, f.v))));
        }
        if (v2 > c.vm) {
            tf = ti + (2 * abs(f.x - i.x) + (sgn * c.vm - i.v) * (sgn * c.vm - i.v) / c.ai +
                    (sgn * c.vm - f.v) * (sgn * c.vm - f.v) / c.af) / (2 * c.vm);
            t1 = ti + abs(sgn * c.vm - i.v) / c.ai;
            t2 = tf - abs(sgn * c.vm - f.v) / c.af;
        } else {
            tf = ti + abs(sgn * v2 - i.v) / c.ai + abs(sgn * v2 - f.v) / c.af;
            t1 = ti + abs(sgn * v2 - i.v) / c.ai;
            t2 = t1;
        }
    }
    @Override
    public MotionState state(double t) {
        if (t < ti) {
            return new MotionState(i.x + i.v * (t - ti), i.v, 0);
        } else if (t < t1) {
            return new MotionState(i.x + i.v * (t - ti) + sgn * c.ai * (t - ti) * (t - ti) / 2, i.v + sgn * c.ai * (t - ti), sgn * c.ai);
        } else if (t < t2) {
            return new MotionState(i.x + i.v * (t1 - ti) + sgn * c.ai * (t1 - ti) * (t1 - ti) / 2 + sgn * c.vm * (t - t1), sgn * c.vm,0);
        } else if (t < tf) {
            return new MotionState(f.x + f.v * (t - tf) - sgn * c.af * (t - tf) * (t - tf) / 2, f.v - sgn * c.af * (t - tf), -sgn * c.af);
        }
        return new MotionState(f.x + f.v * (t - tf), f.v, 0);
    }
    public static AsymProfile extendAsym(MotionProfile p, AsymConstraints c, double ti, MotionState f) {
        return new AsymProfile(c, ti, p.state(ti), f);
    }
    public static AsymProfile extendAsym(MotionProfile p, AsymConstraints c, MotionState f) {
        return extendAsym(p, c, p.tf, f);
    }
    public double t1() {
        return t1;
    }
    public double t2() {
        return t2;
    }
    public AsymConstraints c() {
        return c;
    }
}