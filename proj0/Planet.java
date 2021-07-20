public class Planet {
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    public double calcDistance(Planet p) {
        double dx = xxPos - p.xxPos;
        double dy = yyPos - p.yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p) {
        double dist = calcDistance(p);
        return (G * mass * p.mass) / (dist * dist);
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - xxPos;
        return calcForceExertedBy(p) * dx / calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - yyPos;
        return calcForceExertedBy(p) * dy / calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double netFx = 0;
        for (Planet p : planets) {
            if (!this.equals(p))
                netFx += calcForceExertedByX(p);
        }
        return netFx;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double netFy = 0;
        for (Planet p : planets) {
            if (!this.equals(p))
                netFy += calcForceExertedByY(p);
        }
        return netFy;
    }

    public void update(double dt, double Fx, double Fy) {
        double ax, ay;
        ax = Fx / mass;
        ay = Fy / mass;
        xxVel += dt * ax;
        yyVel += dt * ay;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }

    public void draw() {
        String file = "images/" + imgFileName;
        StdDraw.picture(xxPos, yyPos, file);
    }

}
