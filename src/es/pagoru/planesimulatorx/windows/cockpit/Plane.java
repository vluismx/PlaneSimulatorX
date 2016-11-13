package es.pagoru.planesimulatorx.windows.cockpit;

import es.pagoru.planesimulatorx.utils.Vector3Di;

/**
 * Created by pablo on 25/10/16.
 */
public class Plane {

    private static final int MAX_THROTTLE = 570;

    public enum FlightControlThrottlePosition {
        REVERSE_1,
        OFF,
        POWER_1,
        POWER_2;
        
        public String getFileName() {
            return "cockpit_throttle_" + ordinal();
        }
    }

    public enum FlightControlPositionsUpDown {
        UP,
        NORMAL,
        DOWN
    }

    public enum FlightControlPositionsLeftRight {
        RIGHT_90(1),
        RIGHT_45(2),
        NORMAL(3),
        LEFT_45(4),
        LEFT_90(5);

        int id;

        FlightControlPositionsLeftRight(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }

        public String getFileName(){
            return "cockpit_control_" + getId();
        }
    }

    private String plate;

    private String brand;
    private String model;
    private String owner;
    private int capacity; 

    private boolean undercarriage;

    private boolean engine;
    private boolean engineTurningOn;
    private int enginesOn;
    private int kerosene;
    
    private boolean breaks;

    private Vector3Di position;
    
    private int throttle;
    private float yaw;
    private float pitch;

    private FlightControlPositionsLeftRight flightControlPositionsLeftRight;
    private FlightControlPositionsUpDown flightControlPositionsUpDown;
    private FlightControlThrottlePosition flightControlThrottlePosition;

    public Plane(String model, String brand, String plate){
        this.plate = plate;
        this.model = model;
        this.brand = brand;
        this.engine = false;
        this.undercarriage = true;
        this.position = new Vector3Di(0, 0, 0);
        this.throttle = 0;
        this.enginesOn = 0;
        this.engineTurningOn = false;
        breaks = true;

        this.flightControlPositionsLeftRight = FlightControlPositionsLeftRight.NORMAL;
        this.flightControlPositionsUpDown = FlightControlPositionsUpDown.NORMAL;
        this.flightControlThrottlePosition = FlightControlThrottlePosition.OFF;
    }
    
    public Plane(String model, String brand, String plate, String owner, int capacity, int kerosene, Vector3Di position){
        this(model, brand, plate);
        this.owner = owner;
        this.capacity = capacity;
        this.kerosene = kerosene;
        this.position = position;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isBreaks() {
        return breaks;
    }

    public void toggleBreaks(){
        setBreaks(!isBreaks());
    }
    
    private void setBreaks(boolean breaks) {
        this.breaks = breaks;
    }

    public int getEnginesOn() {
        return enginesOn;
    }

    public String getPlate() {
        return plate.substring(0, plate.length() > 6 ? 6 : plate.length());
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isUndercarriage() {
        return undercarriage;
    }

    public boolean isEngine() {
        return engine;
    }

    public int getThrottle() {
        return throttle;
    }

    public int getKerosene() {
        return kerosene;
    }

    public Vector3Di getPosition(){
        return position;
    }

    public float getYaw(){
        return yaw;
    }

    public float getPitch(){
        return pitch;
    }

    public void setUndercarriage(boolean undercarriage) {
        this.undercarriage = undercarriage;
    }

    /*
    Capturar error conforme no se puede subir porque esta tocando el suelo.
     */
    public boolean toggleUndercarriage(){
        if(isUndercarriage()){
            if(position.getY() == 0){
                return false;
            }
        }
        setUndercarriage(!isUndercarriage());
        return true;
    }

    private void setEngine(boolean engine) {
        this.engine = engine;
    }

    public void toggleEngine(){
        if(!engineTurningOn){
            if(!this.engine){
                engineTurningOn = true;
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                            enginesOn = 1;
                            Thread.sleep(1000);
                            enginesOn = 2;
                            Thread.sleep(1000);
                            enginesOn = 3;
                            Thread.sleep(1000);
                            enginesOn = 4;
                            engineTurningOn = false;
                        } catch(Exception e){}
                        setEngine(true);
                    }
                }.start();
                return;
            }
            setEngine(false);
            enginesOn = 0;
        }
    }

    public void addYaw(float yaw){
        if (isBreaks()) {
            return;
        }
        this.yaw += yaw;
        if(this.yaw < 0){
            this.yaw += 360;
        }
        this.yaw %= 360;
    }

    private void setPitch(float pitch){
        if (isBreaks()) {
            return;
        }
        this.pitch = pitch;
    }

    public void addPitch(float pitch){
        if(getPitch() < 45){
            setPitch(getPitch() + pitch);
        }
    }

    public void subtractPitch(float pitch){
        if(getPosition().getY() > 0 || getPitch() > 0){
            if(getPitch() > -45){
                setPitch(getPitch() - pitch);
            }
        }
    }

    private void setThrottle(int throttle){
        this.throttle = throttle;
    }

    public void addThrottle(int throttle){
        if(isEngine()){
            if(throttle > 1){
                if(getThrottle() < MAX_THROTTLE){
                    setThrottle(getThrottle() + (throttle - 1));
                }
            } else if(throttle < 1){
                if(getThrottle() > 1){
                    setThrottle(getThrottle() - 3);
                }
                if(getThrottle() > -5 && getThrottle() < 5){
                    setThrottle(0);
                }
            }
            return;
        }
        if(getThrottle() > 0){
            setThrottle(getThrottle() - 1);
        }
        if(getThrottle() > -5 && getThrottle() < 5){
            setThrottle(0);
        }
    }

    public FlightControlThrottlePosition getFlightControlThrottlePosition() {
        return flightControlThrottlePosition;
    }

    public void setFlightControlThrottlePosition(FlightControlThrottlePosition flightControlThrottlePosition) {
        this.flightControlThrottlePosition = flightControlThrottlePosition;
    }

    public void moveFlightControlThrottlePosition(boolean up){
        int id = getFlightControlThrottlePosition().ordinal();
        if(up){
            if(id > 0){
                setFlightControlThrottlePosition(FlightControlThrottlePosition.values()[id - 1]);
            }
            return;
        }
        if(id < FlightControlThrottlePosition.values().length - 1){
            setFlightControlThrottlePosition(FlightControlThrottlePosition.values()[id + 1]);
        }
    }


    public FlightControlPositionsUpDown getFlightControlPositionsUpDown() {
        return flightControlPositionsUpDown;
    }

    public void setFlightControlPositionsUpDown(FlightControlPositionsUpDown flightControlPositionsUpDown) {
        this.flightControlPositionsUpDown = flightControlPositionsUpDown;
    }

    public void moveFlightControlPositionsUpDown(boolean up){
        int id = getFlightControlPositionsUpDown().ordinal();
        if(up){
            if(id > 0){
                setFlightControlPositionsUpDown(FlightControlPositionsUpDown.values()[id - 1]);
            }
            return;
        }
        if(id < FlightControlPositionsUpDown.values().length - 1){
            setFlightControlPositionsUpDown(FlightControlPositionsUpDown.values()[id + 1]);
        }
    }

/*    public static void main(String[] args){
        Plane cockpit = new Plane("asd", "adsa", "asd");
        cockpit.addPitch(45);
        cockpit.addPitch(-67);

        cockpit.moveFlightControlPositionsUpDown(true);
        cockpit.moveFlightControlPositionsUpDown(true);

        System.out.println(cockpit.getFlightControlPositionsUpDown());
    }*/

    public FlightControlPositionsLeftRight getFlightControlPositionsLeftRight() {
        return flightControlPositionsLeftRight;
    }

    private void setFlightControlPositionsLeftRight(FlightControlPositionsLeftRight controlsPosition) {
        this.flightControlPositionsLeftRight = controlsPosition;
    }

    public void moveFlightControlPositionLeftRight(boolean right){
        int id = getFlightControlPositionsLeftRight().getId();
        if(right){
            if(id > 1){
                setFlightControlPositionsLeftRight(FlightControlPositionsLeftRight.values()[id - 2]);
            }
            return;
        }
        if(id < FlightControlPositionsLeftRight.values().length){
            setFlightControlPositionsLeftRight(FlightControlPositionsLeftRight.values()[id]);
        }
    }
}
