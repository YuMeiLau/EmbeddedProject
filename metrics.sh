#ifndef _METRICS_SH_
#define _METRICS_SH

#include "drone.sh"
#include "inc/vec.h"

/* Counters/Metrics used to evaluate algorithm */

#define RECORD_METRICS

typedef struct metric_logger {
unsigned long _COLLISIONS;
unsigned long _PACKETS_SENT;
unsigned long _PACKETS_DROPPED;
unsigned long _WB_OUTGOING_DATA_DROPPED;
vec _DRONE_POSITIONS[MAX_NO_DRONES];
vec _TARGET;
};

#endif
