package airsimjacamo;

import java.util.Map;

import airsimjacamo.airsim4java.Airsim;
import jep.JepException;

public class SensorModel {

    Map<String, Object> DistanceSensorDataFRWRD;
    Map<String, Object> DistanceSensorDataFRWRDR10;
    Map<String, Object> DistanceSensorDataFRWRDL10;
    Map<String, Object> DistanceSensorDataFRWRDR30;
    Map<String, Object> DistanceSensorDataFRWRDL30;
    Map<String, Object> DistanceSensorDataFRWRDR45;
    Map<String, Object> DistanceSensorDataFRWRDL45;
    Map<String, Object> DistanceSensorDataRVRS;
    Map<String, Object> DistanceSensorDataRVRSR10;
    Map<String, Object> DistanceSensorDataRVRSL10;
    Map<String, Object> DistanceSensorDataRVRSR30;
    Map<String, Object> DistanceSensorDataRVRSL30;
    Map<String, Object> DistanceSensorDataRVRSR45;
    Map<String, Object> DistanceSensorDataRVRSL45;

    void updateSensors(String vehicle_name) throws Exception {
        DistanceSensorDataFRWRD = checkDirection("forward", vehicle_name);
        DistanceSensorDataFRWRDR10 = checkDirection("forwardright10", vehicle_name);
        DistanceSensorDataFRWRDL10 = checkDirection("forwardleft10", vehicle_name);
        DistanceSensorDataFRWRDR30 = checkDirection("forwardright30", vehicle_name);
        DistanceSensorDataFRWRDL30 = checkDirection("forwardleft30", vehicle_name);
        DistanceSensorDataFRWRDR45 = checkDirection("forwardright45", vehicle_name);
        DistanceSensorDataFRWRDL45 = checkDirection("forwardleft45", vehicle_name);
        DistanceSensorDataRVRS = checkDirection("reverse", vehicle_name);
        DistanceSensorDataRVRSR10 = checkDirection("reverseright10", vehicle_name);
        DistanceSensorDataRVRSL10 = checkDirection("reverseleft10", vehicle_name);
        DistanceSensorDataRVRSR30 = checkDirection("reverseright30", vehicle_name);
        DistanceSensorDataRVRSL30 = checkDirection("reverseleft30", vehicle_name);
        DistanceSensorDataRVRSR45 = checkDirection("reverseright45", vehicle_name);
        DistanceSensorDataRVRSL45 = checkDirection("reverseleft45", vehicle_name);
    }

    public Map<String, Object> checkDirection(String sensor, String vehicle_name) throws JepException {
        return Airsim.getDistanceSensorData(sensor, vehicle_name);
    } 

}
