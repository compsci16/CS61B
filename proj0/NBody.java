public class NBody {
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int N = in.readInt();
        Planet[] planets = new Planet[N];
        in.readDouble();
        for (int i = 0; i < N; i++) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String img = in.readString();
            Planet p = new Planet(xxPos, yyPos, xxVel, yyVel, mass, img);
            planets[i] = p;
        }
        return planets;
    }

    public static void main(String[] args) {

        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String fileName = args[2];
        double R = readRadius(fileName);
        Planet[] planets = readPlanets(fileName);
        StdDraw.setScale(-R, R);

        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        for (Planet planet : planets) {
            planet.draw();
        }
        StdDraw.enableDoubleBuffering();
        for (double t = 0; t <= T; t += dt) {
            int N = planets.length;
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for (int i = 0; i < N; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < N; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.clear();
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet planet : planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", R);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", planets[i].xxPos, planets[i].yyPos,
                    planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
