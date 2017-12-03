#include "metrics.sh"
/* Counters/Metrics used to evaluate algorithm */

unsigned long _COLLISIONS = 0;
unsigned long _PACKETS_SENT = 0;
unsigned long _PACKETS_DROPPED = 0;
unsigned long _WB_OUTGOING_DATA_DROPPED = 0;

long _DRONE_POSITIONS[100][3];
