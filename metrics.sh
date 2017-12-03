#ifndef _METRICS_SH_
#define _METRICS_SH

#include "drone.sh"

/* Counters/Metrics used to evaluate algorithm */

#define RECORD_METRICS
extern unsigned long _COLLISIONS;
extern unsigned long _PACKETS_SENT;
extern unsigned long _PACKETS_DROPPED;
extern unsigned long _WB_OUTGOING_DATA_DROPPED;
extern vec _DRONE_POSITIONS[MAX_NO_DRONES];
#endif
