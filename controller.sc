#include "drone.sh"
#include "inc/vec.h"
#include "inc/vec.c"
import "c_ivec_array";
import "c_vec_queue";

interface Init{ void init(long id); };

behavior Controller(i_vec_receiver in_a, i_mon_send out_v) implements Init
{
	long id;

	void init(long ID){
		id = ID;
	}

        void main(void)
        {
	 ivec mon_pos_delta;
	 vec direction_vector, pos_delta, dir_correction_vector, est_pos_delta;
	 vec est_velocity, new_velocity, ave_velocity; 
         long max_vel_delta, gravity_vec, dest_dist, max_velocity;
	 max_vel_delta = DR_MAX_ACC / TIME_STEP_HZ;
	 gravity_vec = 98000 / TIME_STEP_HZ;
         
	 while(1){
		/* Update velocity everytime Formation returns a new destination direction */
		in_a.receive(&direction_vector);

		/* Adjust destination based on current velocity (dont overshoot) */
		/* Next position based on burrent estimated velocity */
		vec_mult(&est_pos_delta, est_velocity, (1/TIME_STEP_HZ));
		/* Position delta to reach desired point */
		vec_minus(&est_pos_delta, direction_vector, est_pos_delta);
		vec_mult(&dir_correction_vector, est_pos_delta, TIME_STEP_HZ * 2);
		dest_dist = vec_mag(dir_correction_vector); 

		
		/* Vertical accel < horizontal accel. below estimates effect of gravity 
                   as a function of the angle of acceleration */
		max_vel_delta = (dir_correction_vector[_Z] < 0) ? max_vel_delta :
			max_vel_delta - (gravity_vec * (dir_correction_vector[_Z]/dest_dist));
		if (max_vel_delta < dest_dist){
			vec_norm(&dir_correction_vector);
			vec_mult(&dir_correction_vector, dir_correction_vector, max_vel_delta);
		} 

		/* Determine new velocity after applying acceleration for TIME_STEP
		   max velocity also affected by gravity as a function of angle of travel */
		max_velocity = (dir_correction_vector[_Z] < 0) ? DR_MAX_VEL :
			(DR_MAX_VEL - (gravity_vec * (dir_correction_vector[_Z]/dest_dist)));
		vec_add(&new_velocity, est_velocity, dir_correction_vector);
		dest_dist = vec_mag(new_velocity);
		if (max_velocity < dest_dist){
			vec_norm(&new_velocity);
			vec_mult(&new_velocity, new_velocity, max_velocity);
		}

		/* acceleration remains constant throughout TIME_STEP.
		   Position delta equal to average velocity * time   */
		vec_div(&ave_velocity, dir_correction_vector, 2); 
		vec_add(&ave_velocity, ave_velocity, est_velocity);
		vec_mult(&pos_delta, ave_velocity, (1/TIME_STEP_HZ));	
		
		est_velocity = new_velocity;
		
		mon_pos_delta[_X] = pos_delta[_X];
		mon_pos_delta[_Y] = pos_delta[_Y];
		mon_pos_delta[_Z] = pos_delta[_Z];
		mon_pos_delta[_ID] = id;
		out_v.send(&mon_pos_delta);
	 }
        }
};
