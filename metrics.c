#include "metrics.sh"
#include "drone.sh"

/* Counters/Metrics used to evaluate algorithm */

unsigned long _COLLISIONS = 0;
unsigned long _PACKETS_SENT = 0;
unsigned long _PACKETS_DROPPED = 0;
unsigned long _WB_OUTGOING_DATA_DROPPED = 0;

vec _DRONE_POSITIONS[MAX_NO_DRONES];
