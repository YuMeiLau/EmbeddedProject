#include "drone.sh"
#include "inc/vec.h"
#include "inc/vec.c"
import "c_ivec_array";
import "c_vec_queue";

behavior Controller(i_vec_receiver in_a, i_mon_send out_v)
{
        void main(void)
        {
	vec direction_vector;
	vec dir_correction_vector;
	vec est_velocity;
	long max_dist, dest_dist;

		while(1){
			in_a.receive(&direction_vector);
			vec_minus(&dir_correction_vector, direction_vector, est_velocity);
			dest_dist = vec_mag(dir_correction_vector);
			
						
		}
        }
};
