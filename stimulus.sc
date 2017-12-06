#include "drone.sh"
#include "inc/vec.h"
#include "stdio.h"
#include "stdlib.h"
#include "inc/log.h"
#include "inc/vec.h"
#include "metrics.sh"

behavior Stimulus(struct metric_logger metric)
{
	void main(void)
	{
		long target_mag;
		bool increasing = true;
		bool yinc = true;
		metric._TARGET[_X] = 0;
		metric._TARGET[_Y] = 1000;
		metric._TARGET[_Z] = 0;
		while(1){
			waitfor(TIME_STEP);
			if (increasing) {
				metric._TARGET[_X] += TARGET_SPEED;
				if (metric._TARGET[_X] > TARGET_BOUNDS)
					increasing = false;
			} else {
				metric._TARGET[_X] -= TARGET_SPEED;
				if (metric._TARGET[_X] < (-1 * TARGET_BOUNDS))
					increasing = true;
			}
			if (yinc) {
				metric._TARGET[_Y] += TARGET_SPEED;
				if (metric._TARGET[_Y] > TARGET_BOUNDS)
					yinc = false;
			} else {
				metric._TARGET[_Y] -= TARGET_SPEED;
				if (metric._TARGET[_Y] < (-1 * TARGET_BOUNDS))
					yinc = true;
			}
		}
	}
};
