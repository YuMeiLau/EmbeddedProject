#include "drone.sh"
#include <time.h>
#include "inc/vec.h"
#include "inc/vec.c"
#include "inc/log.h"
import "c_vec_queue";
import "c_ivec_queue";

#define _INERTIA 0.8
#define _PLOCAL .6
#define _PGLOBAL .4
#define _P 3
#define _S 200
#define TERMINATION_POINT 500
#define EXPECTED_DISTANCE 3000

void vec_new(vec* out_vec, long x, long y, long z)
{
  (*out_vec)[_X] = x;
  (*out_vec)[_Y] = y;
  (*out_vec)[_Z] = z;	
}

void vec_equals(vec* out_vec, vec original)
{
  (*out_vec)[_X] = original[_X];
  (*out_vec)[_Y] = original[_Y];
  (*out_vec)[_Z] = original[_Z];
}

behavior Self_Pos(i_vec_receiver self_vec, vec positions[MAX_NO_DRONES], in long ID)
{
		vec data;
		void main(void)
		{
				while(true)
				{
						self_vec.receive(&data);
						vec_equals(&(positions[ID]), data);		
				}
		}
};

behavior Other_Pos(i_ivec_receiver other_ivec, vec positions[MAX_NO_DRONES], in long ID)
{
		ivec data;
		vec tmp;
		int id;
		void main(void)
		{
				while(true)
				{
						other_ivec.receive(&data);
						id = data[_ID];
						if(id != ID)
						{
							vec_new(&tmp, data[_X], data[_Y], data[_Z]);
							vec_equals(&(positions[id]), tmp);
						}
				}
		}
};

behavior Self_Velocity(i_vec_receiver in_v, vec v)
{
		vec data;
		vec tmp;
		void main(void)
		{
				while(true)
				{
						in_v.receive(&data);
				                vec_new(&tmp, data[_X], data[_Y], data[_Z]);
                                                vec_equals(&v, tmp);
				}
		}
};

behavior Receive(i_vec_receiver self_vec, i_ivec_receiver other_ivec, i_vec_receiver in_v, vec positions[MAX_NO_DRONES], vec v, in long ID)
{
		Other_Pos other_pos(other_ivec, positions, ID);
		Self_Pos self_pos(self_vec, positions, ID);
		Self_Velocity self_velocity(in_v, v);

		void main(void)
		{
				par
				{
						other_pos;
						self_pos;
						self_velocity;
				}
		}
};

behavior Path_Planning(i_vec_sender out_a, vec positions[MAX_NO_DRONES], vec v, in long ID)
{		
		vec i_current; 		/* current relative distance to target for drone i */
		vec v_current;		/* current estimated velocity from controller */
		vec h;				/* minimum direction vector returned by PSO function */
		vec current_pos[MAX_NO_DRONES]; /* the updated array storing the relative distance for every drone */
		

		double cost(vec new_xi)
		{
				int j;
				long double d_ij;
				long double a_ij;
				long double a_exp;
				long double term_target, term_relative, result;
				long double tmp;
				vec target;
				vec v_newit;
				vec v_newij;
				vec v_ij;
								
				vec_new(&target, 0, 0, FORMATION_HEIGHT);
				vec_minus(&v_newit, target, new_xi);
				term_target = vec_mag(v_newit); /*- vec_mag(target));*/
				term_target = (term_target < 0) ? (term_target*-1) : term_target;
				term_target = _P * term_target;
				term_relative = 0;

				for(j = 0; j < MAX_NO_DRONES; j++) /* find neighbourhood */
				{
						vec_minus(&v_ij, i_current, current_pos[j]);
						d_ij = (double) vec_mag(v_ij);
						if(d_ij < SAFE_DISTANCE && j != ID)
						{
								a_exp = (SAFE_DISTANCE - d_ij) / _S;
								a_ij = 1 + exp(a_exp);
								vec_minus(&v_newij, current_pos[j], new_xi);
								tmp = (vec_mag(v_newij) - EXPECTED_DISTANCE);
								tmp = (tmp < 0) ? (tmp * -1) : tmp;
								term_relative += (tmp * a_ij);
						}
				}
				if (new_xi[_Z] < SAFE_DISTANCE) {
					a_ij = 1 + exp((double)((SAFE_DISTANCE - new_xi[_Z]) / _S));
					term_relative += a_ij * FORMATION_HEIGHT;
				}
				result = term_target + term_relative;
				return result;
		}

		void pso()
		{
				long double cost_local, cost_global, cost_new;
				int i, j, seed;
				double rl, rg;
				vec global, local;
				vec vi;
				vec new_p;
				vec tmp_pos_delta;
				vec_new(&h, 0, 0, 0);
				vec_new(&global, 0, 0, FORMATION_HEIGHT);
				vec_equals(&local, i_current); /* local: current location */
				cost_local = 99999999999999;
				cost_global = cost(global);
				for(i = 0; i < TERMINATION_POINT; i++)
				{
						for(j = 0; j < 3; j++)
						{
								rl = (double)rand() / (double)RAND_MAX;
								rg = (double)rand() / (double)RAND_MAX;
								vi[j] = _INERTIA * v_current[j] + /*(DR_MAX_VEL/TIME_STEP_HZ) **/ 
								        (_PLOCAL * rl * (local[j] - i_current[j]) + 
								        _PGLOBAL * rg * (global[j] - i_current[j]));
						}
					        vec_div(&tmp_pos_delta, vi, (TIME_STEP_HZ/1));	
						vec_add(&new_p, i_current, tmp_pos_delta);

						cost_new = cost(new_p);
						if(cost_new < cost_local)
						{
								vec_equals(&local, new_p);
								cost_local = cost_new;
								vec_equals(&h, tmp_pos_delta);
								if(cost_local < cost_global)
								{	
									vec_equals(&global, local);
									cost_global = cost_local;
								}
						}
				}
		}

		void main(void)
		{	
				/* Realistically, all drones wont stay be able to 
				 * stay completely synced. Stall for random waiting 
				 * period to simulate the algorithm getting de-synced */
				double rl;	
				srand(time(0));
				rl = (double)rand() / (double)RAND_MAX;
				waitfor(TIME_STEP*rl);
				while(true)
				{
						/* Time Step? */
						waitfor(TIME_STEP);
						LOG("Formation: Running PSO Algorithm");
						memcpy(current_pos, positions, MAX_NO_DRONES*3*sizeof(long));
						memcpy(v_current, v, 3*sizeof(long));
						vec_equals(&i_current, current_pos[ID]);
					
						/* PSO when to run? */	
						pso();
						LOG("Formation: PSO complete\n");
						out_a.send(h);
				}
		}
};

interface Form_Init{ void init(long); };

behavior Formation(i_vec_receiver self_vec, i_ivec_receiver other_ivec, i_vec_sender out_a, i_vec_receiver in_v) implements Form_Init
{
		long ID;
		vec positions[MAX_NO_DRONES];
		vec v;                  /* estimated velocity from controller */
		Receive receive(self_vec, other_ivec, in_v, positions, v, ID);
		Path_Planning path_planning(out_a, positions, v, ID);

	void init(long id){
		ID = id;
	}

        void main(void)
        {
    			par 
    			{
    					receive;
    					path_planning;
    			}
        }
};
