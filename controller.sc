#include "drone.sh"
#include "inc/vec.h"
#include "inc/vec.c"
import "c_ivec_array";
import "c_vec_queue";

typedef long vec[3];
typedef long ivec[4];
behavior Controller(i_vec_receiver in_a, i_mon_send out_v)
{
/*	void vector_minus(vec )
	{        
  		(*out)[_x] = a[_X] - b[_X];    
		(*out)[_Y] = a[_X] - b[_Y];    
		(*out)[_Z] = a[_X] - b[_Z];                     
	}*/

        void main(void)
        {
	vec direction_vector;
	vec dir_correction_vector;
	vec est_velocity;

		while(1){
			in_a.receive(&direction_vector);
			vec_minus(&dir_correction_vector, direction_vector, est_velocity);
						
		}
        }
};
