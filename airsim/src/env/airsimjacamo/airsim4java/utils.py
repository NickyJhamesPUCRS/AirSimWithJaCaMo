import json
from airsim.types import *

def class2json(classe):
    return json.dumps(classe.__dict__)

  
def class2dict(instance, built_dict={}):
    if not hasattr(instance, "__dict__"):
        return instance
    new_subdic = vars(instance)
    for key, value in new_subdic.items():
        new_subdic[key] = class2dict(value)
    return new_subdic


def setCarControls(throttle = 0, steering = 0, brake = 0,
        handbrake = False, is_manual_gear = False, manual_gear = 0, gear_immediate = True, vehicle_name = ""):        
    car_controls = CarControls(throttle, steering, brake, handbrake, is_manual_gear, 
                               manual_gear, gear_immediate)
    client.setCarControls(car_controls, vehicle_name)
    
