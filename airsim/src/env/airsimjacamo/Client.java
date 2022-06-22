package airsimjacamo;



import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import airsimjacamo.CarModel.Action;
import airsimjacamo.airsim4java.Airsim;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;

import tools.Utils.Display;


public class Client extends Artifact{  

    private Display display;
    CarModel car_model = new CarModel();
    SensorModel sensor_model = new SensorModel();
    private String agentName;
    boolean client_initialized;
    
    void init(String name) throws Exception {   
        // creates an observable property called numMsg
        this.defineObsProperty("numMsg",0);        
        display = new Display(name);
        display.setVisible(true);        
    }

    void initializeClient() throws Exception{
        if (!client_initialized) {
            Airsim.initializeClient();
            agentName = this.getOpUserName();
            car_model.vehicle_name = agentName;
            
            Airsim.enableApiControl(agentName);
            client_initialized = true;
        }
    }

    @OPERATION void forward(Integer delay) throws Exception {move(Action.FRWRD, delay);}    
    @OPERATION void forwardright(Integer delay) throws Exception {move(Action.FRWRDR, delay);}
    @OPERATION void forwardleft(Integer delay) throws Exception {move(Action.FRWRDL, delay);}
    @OPERATION void reverse(Integer delay) throws Exception {move(Action.RVRS, delay);}
    @OPERATION void reverseright(Integer delay) throws Exception {move(Action.RVRSR, delay);}
    @OPERATION void reverseleft(Integer delay) throws Exception {move(Action.RVRSL, delay);}

    @OPERATION void infinitemove(Integer delay) throws Exception {
        Action curr_action = Action.FRWRD; 
        while (true) {
            move(curr_action, delay);  
  
            // Check available directions
            List<String> available_directions = car_model.getAvailableDirections();
            
            // Sort directions to prioritize going forward : FRWRD, FRWRDL, FRWRDR, RVRS, RVRSL, RVRSR
            Collections.sort(available_directions);
            if (available_directions.size()>0){
                printMsg("Available Directions: "+available_directions);
                
                // Get first available action
                curr_action = Action.valueOf(available_directions.get(0));
            }                 
        }        
    }

    void move(Action direction, Integer delay) throws Exception {        
        try {
            initializeClient();

            sensor_model.updateSensors(agentName);
            car_model.moveDirection(direction, sensor_model, delay);

            // printMsg("Distance: "+sensor_model.DistanceSensorDataFRWRD.get("distance").toString()+", Time Stamp: "+sensor_model.DistanceSensorDataFRWRD.get("time_stamp").toString());
            // printMsg("Speed "+car_model.car_state.get("speed").toString()+", Gear "+car_model.car_state.get("gear").toString());

            sensor_model.updateSensors(agentName);
        } catch (Exception e) {
            e.printStackTrace();
            printMsg(e.getMessage());
        }
        
    }

    private void printMsg(String msg){
        ObsProperty prop = this.getObsProperty("numMsg");
        prop.updateValue(prop.intValue()+1);
        display.addText("Message at "+System.currentTimeMillis()+" from "+agentName+": "+msg);        

        display.updateNumMsgField(prop.intValue());
    }
    
}
