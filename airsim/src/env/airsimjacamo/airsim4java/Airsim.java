package airsimjacamo.airsim4java;

import java.util.Map;

import jep.Jep;
import jep.JepConfig;
import jep.JepException;


import tools.Utils;

public class Airsim {
    private static final String[] sharedModules = {"airsim", "numpy"};
    static JepConfig config = new JepConfig().setInteractive(true).addSharedModules(sharedModules);
    
    static Jep jep;
    

    public static void initializeClient() throws JepException{
        Airsim.jep = new Jep(config);
        jep.setClassLoader(
                Thread.currentThread().getContextClassLoader());
        loadUtils();
        carClient();                 
    }    

    public static void closeClient() throws JepException{
        jep.close();
    }

    public static void loadUtils() throws JepException {
        jep.runScript("src/env/airsimjacamo/airsim4java/utils.py");     
    }

    //==========Car functions
    public static void carClient() throws JepException {
        jep.eval("import airsim");
        jep.eval("client = airsim.CarClient(ip = '192.168.0.19', port = 41451)");    
    }

    public static void enableApiControl(String vehicle_name) throws JepException {
        jep.eval("client.enableApiControl(True, '"+vehicle_name+"')");           
    }

    public static Map<String, Object> getCarState(String vehicle_name) throws JepException{      
        jep.eval("car_state = client.getCarState('"+vehicle_name+"')");        
        Object car_state = jep.getValue("class2dict(car_state)");        
        return Utils.obj2map(car_state);
    }

    public static Map<String, Object> getCarControls(String vehicle_name) throws JepException{        
        jep.eval("car_controls = client.getCarControls('"+vehicle_name+"')");  
        Object car_controls = jep.getValue("class2dict(car_controls)");
        return Utils.obj2map(car_controls);
    }

    public static void setCarControls(Map<String, Object> car_controls) throws JepException{                
        jep.invoke("setCarControls", car_controls);      
    }

    //==========Sensor functions
    public static Map<String, Object> getDistanceSensorData(String sensor_name, String vehicle_name) throws JepException{                
        jep.eval("distance_data = client.getDistanceSensorData('"+sensor_name+"', '"+vehicle_name+"')");  
        Object distance_data = jep.getValue("class2dict(distance_data)");
        return Utils.obj2map(distance_data);
    }
    
}
