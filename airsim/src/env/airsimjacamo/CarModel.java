package airsimjacamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jep.JepException;

import airsimjacamo.airsim4java.Airsim;

public class CarModel {
     
    Map<String, Object> car_controls;
    Map<String, Object> car_state;
    Map<String, Object> direction_state = new HashMap<String, Object>();
    String vehicle_name;
    Double threshold = 8.0;
    
    public enum Action {
        FRWRD, FRWRDR, FRWRDL, RVRS, RVRSR, RVRSL
    } 

    private void getCarControls() throws JepException{        
        this.car_controls = Airsim.getCarControls(vehicle_name);
    }

    private Map<String, Object> getCarState()  throws JepException{
        this.car_state = Airsim.getCarState(vehicle_name);
        return this.car_state;
    }

    private void  setCarControls (Float throttle, Float steering, Float brake, 
        boolean handbrake, boolean is_manual_gear, Integer manual_gear, 
        boolean gear_immediate, Integer miliseconds) throws JepException{            
            getCarControls();
            car_controls.put("throttle", throttle);
            car_controls.put("steering", steering);
            car_controls.put("brake", brake);
            car_controls.put("handbrake", handbrake);
            car_controls.put("is_manual_gear", is_manual_gear);
            car_controls.put("manual_gear", manual_gear);
            car_controls.put("gear_immediate", gear_immediate);
            car_controls.put("vehicle_name", vehicle_name);
            Airsim.setCarControls(car_controls);
            
            // Drive a bit
            try{
                Thread.sleep(miliseconds);
            }catch(InterruptedException ex){
            
            }

            //Reset state
            car_controls.put("throttle", 0f);
            car_controls.put("steering", 0f);
            car_controls.put("brake", 0f);
            car_controls.put("handbrake", false);
            car_controls.put("is_manual_gear", false);
            car_controls.put("manual_gear", 0);
            car_controls.put("gear_immediate", true);
            car_controls.put("vehicle_name", vehicle_name);
            Airsim.setCarControls(car_controls);
         }

    private boolean compareThresh (double distance, double thresh) {
        return (distance > thresh);
    }
    
    void directionAvailability (SensorModel sensor) {
        double forward_distance = (double) sensor.DistanceSensorDataFRWRD.get("distance");
        double forward_right10_distance = (double) sensor.DistanceSensorDataFRWRDR10.get("distance");
        double forward_left10_distance = (double) sensor.DistanceSensorDataFRWRDL10.get("distance");
        double forward_right30_distance = (double) sensor.DistanceSensorDataFRWRDR30.get("distance");
        double forward_left30_distance = (double) sensor.DistanceSensorDataFRWRDL30.get("distance");
        double forward_right45_distance = (double) sensor.DistanceSensorDataFRWRDR45.get("distance");
        double forward_left45_distance = (double) sensor.DistanceSensorDataFRWRDL45.get("distance");

        double reverse_distance = (double) sensor.DistanceSensorDataRVRS.get("distance");
        double reverse_right10_distance = (double) sensor.DistanceSensorDataRVRSR10.get("distance");
        double reverse_left10_distance = (double) sensor.DistanceSensorDataRVRSL10.get("distance");
        double reverse_right30_distance = (double) sensor.DistanceSensorDataRVRSR30.get("distance");
        double reverse_left30_distance = (double) sensor.DistanceSensorDataRVRSL30.get("distance");
        double reverse_right45_distance = (double) sensor.DistanceSensorDataRVRSR45.get("distance");
        double reverse_left45_distance = (double) sensor.DistanceSensorDataRVRSL45.get("distance");

        double lateral_thresh = (threshold/2)*Math.sqrt(3);

        direction_state.put("FRWRD",  (compareThresh(forward_distance, threshold) 
            & compareThresh(forward_right10_distance, threshold) 
            & compareThresh(forward_left10_distance, threshold)));

        direction_state.put("FRWRDR",  (compareThresh(forward_right45_distance, lateral_thresh)
            & compareThresh(forward_right10_distance, threshold))
            & compareThresh(forward_right30_distance, lateral_thresh));

        direction_state.put("FRWRDL",  (compareThresh(forward_left45_distance, lateral_thresh)
            & compareThresh(forward_left10_distance, threshold)
            & compareThresh(forward_left30_distance, lateral_thresh)));

        direction_state.put("RVRS",  (compareThresh(reverse_distance, threshold)
            & compareThresh(reverse_right10_distance, threshold)
            & compareThresh(reverse_left10_distance, threshold)));

        direction_state.put("RVRSR",  (compareThresh(reverse_right45_distance, lateral_thresh)
            & compareThresh(reverse_right10_distance, threshold)
            & compareThresh(reverse_right30_distance,lateral_thresh)));
            
        direction_state.put("RVRSL",  (compareThresh(reverse_left45_distance, lateral_thresh)
            & compareThresh(reverse_left10_distance, threshold)
            & compareThresh(reverse_left30_distance, lateral_thresh)));
    }

    List<String> getAvailableDirections () {
        List<String> available_directions = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : direction_state.entrySet()) {
            if (entry.getValue().equals(true)) {
                available_directions.add(entry.getKey());
            }
          }
        return available_directions;
    }

    public void moveDirection(Action action, SensorModel sensor, Integer delay) throws JepException {
        Float throttle = 0.0f;
        Float steering = 0.0f;
        Float brake = 0.0f;
        boolean handbrake = false;
        boolean is_manual_gear = false;
        Integer manual_gear = 0;
        boolean gear_immediate = true;

        directionAvailability(sensor);
        if ( (boolean) direction_state.get(action.toString())) {
            switch (action) {
                case FRWRD:
                    throttle = 0.5f;
                    break;
                case FRWRDR:
                    throttle = 0.5f;
                    steering = 1f;
                    break;
                case FRWRDL:
                    throttle = 0.5f;
                    steering = -1f;                
                    break;
                case RVRS:
                    throttle = -0.5f;
                    is_manual_gear = true;
                    manual_gear = -1;
                    break;
                case RVRSR:
                    throttle = -0.5f;
                    is_manual_gear = true;
                    manual_gear = -1;
                    steering = 1f;
                    break;
                case RVRSL:
                    throttle = -0.5f;
                    is_manual_gear = true;
                    manual_gear = -1;
                    steering = -1f;
                    break;
            }
     
            setCarControls(throttle, steering, brake, handbrake, is_manual_gear, 
        manual_gear, gear_immediate, delay);
            directionAvailability(sensor);
        } else {
            // resets everithing except break which is turned on (1)
            setCarControls(0.0f, 0.0f, 1f, false, false, 
            0, true, delay);
        }   
        getCarState();
    } 
    
}
