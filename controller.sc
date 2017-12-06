#include "drone.sh"
#include "inc/vec.h"
#include "inc/log.h"
#include "metrics.sh"
import "c_ivec_array";
import "c_vec_queue";

interface Cont_Init { void init(long); };

behavior Controller(i_vec_receiver in_a, i_mon_send out_v, i_vec_sender est_v, struct metric_logger metric) implements Cont_Init
{
	long id;
	vec est_velocity;

	void init(long ID){
		est_velocity[_X] = 0;
		est_velocity[_Y] = 0;
		est_velocity[_Z] = 0;
		id = ID;
	}

        void main(void)
        {
	 ivec mon_pos_delta;
	 vec direction_vector, pos_delta, dir_correction_vector, est_pos_delta;
	 vec new_velocity, ave_velocity; 
         long max_vel_delta, tmp_max_vel_delta, gravity_vec, dest_dist, max_velocity, norm_divisor, grav_angle;
	 max_vel_delta = DR_MAX_ACC / TIME_STEP_HZ;
	 gravity_vec = 9800 / TIME_STEP_HZ;
         
	 while(1){
		/* Update velocity everytime Formation returns a new destination direction */
		in_a.receive(&direction_vector);
		//LOGL("Drone Controller %ld received heading\n", id);
		//printf("CONTROLLER: Drone:%ld [%ld][%ld][%ld]  Direction [%ld][%ld][%ld]\n", id, metric._DRONE_POSITIONS[id][_X], metric._DRONE_POSITIONS[id][_Y], metric._DRONE_POSITIONS[id][_Z], direction_vector[_X], direction_vector[_Y], direction_vector[_Z]);

		/* Adjust destination based on current velocity (dont overshoot) */
		/* Next position based on burrent estimated velocity */
		vec_div(&est_pos_delta, est_velocity, TIME_STEP_HZ);
		/* Position delta to reach desired point */
		vec_minus(&est_pos_delta, direction_vector, est_pos_delta);
		vec_mult(&dir_correction_vector, est_pos_delta, TIME_STEP_HZ * 2);

		
		/* Vertical accel < horizontal accel. below estimates effect of gravity 
                   as a function of the angle of acceleration */
		dest_dist = vec_mag(dir_correction_vector); 
		grav_angle = (dest_dist == 0) ? 0 : (dir_correction_vector[_Z]/dest_dist);
		tmp_max_vel_delta = (dir_correction_vector[_Z] < 0) ? max_vel_delta :
			max_vel_delta - (gravity_vec * grav_angle);
		if (tmp_max_vel_delta < dest_dist){
			norm_divisor = dest_dist / tmp_max_vel_delta;
			vec_div(&dir_correction_vector, dir_correction_vector, norm_divisor);
		} 

		/* Determine new velocity after applying acceleration for TIME_STEP
		   max velocity also affected by gravity as a function of angle of travel */
		dest_dist = vec_mag(new_velocity);
		grav_angle = (dest_dist == 0) ? 0 : (new_velocity[_Z]/dest_dist);
		max_velocity = (dir_correction_vector[_Z] < 0) ? DR_MAX_VEL :
			(DR_MAX_VEL - (gravity_vec * grav_angle));
		vec_add(&new_velocity, est_velocity, dir_correction_vector);
		if (max_velocity < dest_dist){
			norm_divisor = dest_dist/max_velocity;
			vec_div(&new_velocity, new_velocity, norm_divisor);
		}

		/* acceleration remains constant throughout TIME_STEP.
		   Position delta equal to average velocity * time   */
		vec_add(&ave_velocity, new_velocity, est_velocity); 
		vec_div(&ave_velocity, ave_velocity, 2);
		vec_div(&pos_delta, ave_velocity, TIME_STEP_HZ);	
		
		est_velocity = new_velocity;
		//printf("CONTROLLER: NEW_VEL [%ld][%ld][%ld]\n", new_velocity[_X], new_velocity[_Y], new_velocity[_Z]);
		
		mon_pos_delta[_X] = pos_delta[_X];
		mon_pos_delta[_Y] = pos_delta[_Y];
		mon_pos_delta[_Z] = pos_delta[_Z];
		mon_pos_delta[_ID] = id;
		out_v.send(&mon_pos_delta);
		est_v.send(est_velocity);
	 }
        }
};
