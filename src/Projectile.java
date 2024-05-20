public class Projectile {
    private double velocity;
    private double angle;
    private double dragCoefficient;
    private double fluidDensity;
    private double surfaceArea;
    private double mass;
    private double x_position;
    private double y_position;
    private double time;
    private double maxHeight;
    private double minVelocity;
    private double apexVelocity;

    public Projectile() {
        velocity = 0;
        angle = 0;
        x_position = 0;
        y_position = 0;
        time = 0;
        maxHeight = 0;
        dragCoefficient = 0.38;
        fluidDensity = 1.225;
        surfaceArea = 0.000126;
        mass = 0.001;
        minVelocity = 0;
        apexVelocity = 0;
    }

    public Projectile(double velocity, double angle, double height, double dragCoefficient, double fluidDensity, double surfaceArea, double mass) {
        this.velocity = velocity;
        this.angle = angle;
        x_position = 0;
        y_position = height;
        time = 0;
        maxHeight = 0;
        this.dragCoefficient = dragCoefficient;
        this.fluidDensity = fluidDensity;
        this.surfaceArea = surfaceArea;
        this.mass = mass;
        minVelocity = 0;
        apexVelocity = 0;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAngle() {
        return angle;
    }

    public double getX_position() {
        return x_position;
    }

    public double getY_position() {
        return y_position;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getDragCoefficient() {
        return dragCoefficient;
    }

    public double getFluidDensity() {
        return fluidDensity;
    }

    public double getMass() {
        return mass;
    }

    public double getSurfaceArea() {
        return surfaceArea;
    }

    public double getTime() {
        return time;
    }

    public double getMinVelocity() {
        return minVelocity;
    }

    public double getApexVelocity() {
        return apexVelocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
        if (velocity < minVelocity) {
            minVelocity = velocity;
        }
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setX_position(double x_position) {
        this.x_position = x_position;

    }

    public void setY_position(double y_position) {
        this.y_position = y_position;
        if (y_position > maxHeight) {
            maxHeight = y_position;
            minVelocity = velocity;
            apexVelocity = velocity;
        }
    }

    public void setDragCoefficient (double dragCoefficient) {this.dragCoefficient = dragCoefficient;}

    public void setFluidDensity(double fluidDensity) {
        this.fluidDensity = fluidDensity;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setSurfaceArea(double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public void move(double dt) {
        setX_position(getX_position() + dt * getVelocity() * Math.cos(getAngle()*Math.PI/180) + 0.5
                * accelerationX(getAngle(), getVelocity()) * Math.pow(dt, 2));
        setY_position(getY_position() + dt * getVelocity() * Math.sin(getAngle()*Math.PI/180) + 0.5
                * accelerationY(getAngle(), getVelocity()) * Math.pow(dt, 2));
        setVelocity(getVelocity() + acceleration(getAngle(), getVelocity()) * dt);
        setAngle(Math.atan((2 * accelerationY(getAngle(), getVelocity()) * Math.sqrt
                ((accelerationX(getAngle(), getVelocity()) * (dt * getVelocity() * Math.cos(getAngle() *
                        Math.PI / 180) + (accelerationX(getAngle(), getVelocity()) * Math.pow(dt, 2)) / 2)) / 2 +
                        (Math.pow(Math.cos(getAngle() * Math.PI / 180), 2) * Math.pow(getVelocity(), 2)) / 4) +
                (Math.sin(getAngle() * Math.PI / 180) * accelerationX(getAngle(), getVelocity()) -
                        Math.cos(getAngle() * Math.PI / 180) * accelerationY(getAngle(), getVelocity())) *
                        getVelocity()) / (2 * accelerationX(getAngle(), getVelocity()) *
                Math.sqrt((accelerationX(getAngle(), getVelocity()) * (dt * getVelocity() *
                        Math.cos(getAngle() * Math.PI / 180) + (accelerationX(getAngle(), getVelocity()) *
                        Math.pow(dt, 2)) / 2)) / 2 + (Math.pow(Math.cos(getAngle() * Math.PI / 180), 2) *
                        Math.pow(getVelocity(), 2)) / 4))) * 180 / Math.PI);
    }

    public void launch(double dt) {
        do {
            setX_position(getX_position() + dt * getVelocity() * Math.cos(getAngle()*Math.PI/180) + 0.5
                    * accelerationX(getAngle(), getVelocity()) * Math.pow(dt, 2));
            setY_position(getY_position() + dt * getVelocity() * Math.sin(getAngle()*Math.PI/180) + 0.5
                    * accelerationY(getAngle(), getVelocity()) * Math.pow(dt, 2));
            setVelocity(getVelocity() + acceleration(getAngle(), getVelocity()) * dt);
            setAngle(Math.atan((2 * accelerationY(getAngle(), getVelocity()) * Math.sqrt
                    ((accelerationX(getAngle(), getVelocity()) * (dt * getVelocity() * Math.cos(getAngle() *
                            Math.PI / 180) + (accelerationX(getAngle(), getVelocity()) * Math.pow(dt, 2)) / 2)) / 2 +
                            (Math.pow(Math.cos(getAngle() * Math.PI / 180), 2) * Math.pow(getVelocity(), 2)) / 4) +
                    (Math.sin(getAngle() * Math.PI / 180) * accelerationX(getAngle(), getVelocity()) -
                            Math.cos(getAngle() * Math.PI / 180) * accelerationY(getAngle(), getVelocity())) *
                            getVelocity()) / (2 * accelerationX(getAngle(), getVelocity()) *
                    Math.sqrt((accelerationX(getAngle(), getVelocity()) * (dt * getVelocity() *
                            Math.cos(getAngle() * Math.PI / 180) + (accelerationX(getAngle(), getVelocity()) *
                            Math.pow(dt, 2)) / 2)) / 2 + (Math.pow(Math.cos(getAngle() * Math.PI / 180), 2) *
                            Math.pow(getVelocity(), 2)) / 4))) * 180 / Math.PI);
            time += dt;
        } while (getY_position() > 0);
    }

    public double acceleration(double angle, double velocity) {
        return -9.81 * (Math.sin(angle*Math.PI/180)) - (dragCoefficient*surfaceArea*Math.pow(velocity, 2)*fluidDensity/2)/mass;
    }

    public double accelerationX(double angle, double velocity) {
        return - dragCoefficient*surfaceArea*Math.pow(velocity, 2)*fluidDensity/2/mass*Math.cos(angle*Math.PI/180);
    }

    public double accelerationY(double angle, double velocity) {
        return -9.81 - dragCoefficient*surfaceArea*Math.pow(velocity, 2)*fluidDensity/2/mass*Math.sin(angle*Math.PI/180);
    }
}
