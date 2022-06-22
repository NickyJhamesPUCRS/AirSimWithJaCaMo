// Agent sample_agent in project airSim_JaCaMo

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : move(X) <- infinitemove(X).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
