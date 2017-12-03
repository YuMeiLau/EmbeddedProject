#include "drone.sh"

#include "inc/vec.h"
#include "inc/vec.c"
#include "inc/log.h"
import "c_vec_queue";
import "c_ivec_queue";

#define _INERTIA 0.4
#define _PLOCAL 0.5
#define _PGLOBAL 0.5
#define _P 3
#define _S 0.4
#define TERMINATION_POINT 100
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
						vec_equals(&positions[ID], data);		
				}
		}
};

behavior Other_Pos(i_ivec_receiver other_ivec, vec positions[MAX_NO_DRONES])
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
						vec_new(&tmp, data[_X], data[_Y], data[_Z]);
						vec_equals(&positions[id], tmp);
				}
		}
};

behavior Receive(i_vec_receiver self_vec, i_ivec_receiver other_ivec, vec positions[MAX_NO_DRONES], in long ID)
{
		Other_Pos other_pos(other_ivec, positions);
		Self_Pos self_pos(self_vec, positions, ID);

		void main(void)
		{
				par
				{
						other_pos;
						self_pos;
				}
		}
};

behavior Path_Planning(i_vec_sender out_a, vec positions[MAX_NO_DRONES], in long ID)
{		
		vec i_current; 		/* current relative distance to target for drone i */
		vec h;				/* minimum direction vector returned by PSO function */
		vec current_pos[MAX_NO_DRONES]; /* the updated array storing the relative distance for every drone */
		

		double cost(vec hi)
		{
				int j;
				long d_ij;
				double a_ij;
				double term_target, term_relative, result;
				vec new_xi;
				vec target;
				vec v_newit;
				vec v_newij;
				vec v_ij;
				
				vec_add(&new_xi, i_current, hi);
				vec_new(&target, 0, 0, FORMATION_HEIGHT);
				vec_minus(&v_newit, target, new_xi);
				term_target = _P * (vec_mag(v_newit) - vec_mag(target));

				for(j = 0; j < MAX_NO_DRONES; j++) /* find neighbourhood */
				{
						vec_minus(&v_ij, i_current, current_pos[j]);
						d_ij = vec_mag(v_ij);
						if(d_ij < SAFE_DISTANCE && j != ID)
						{
								a_ij = 1 + exp((double)(SAFE_DISTANCE - d_ij) / _S);
								vec_minus(&v_newij, current_pos[j], new_xi);
								term_relative += a_ij * (vec_mag(v_newij) - EXPECTED_DISTANCE);
						}
				}

				result = term_target + term_relative;
				return result;
		}

		void pso()
		{
				int i, j;
				double rl, rg;
				vec global, local;
				vec vi;
				vec v;
				vec_new(&global, 0, 0, FORMATION_HEIGHT);
				vec_equals(&local, i_current);
				for(i = 0; i < TERMINATION_POINT; i++)
				{
						for(j = 0; j < 3; j++)
						{
								srand(i);
								rl = (double)rand() / (double)RAND_MAX;
								rg = (double)rand() / (double)RAND_MAX;
								vi[j] = _INERTIA * v[j] + _PLOCAL * rl * (local[j] - i_current[j]) + _PGLOBAL * rg * (global[j] - i_current[j]);
						}

						if(cost(vi) < cost(local))
						{
								vec_equals(&local, vi);
								if(cost(local) < cost(global))
									vec_equals(&global, local);
						}
				}
				vec_equals(&h, vi);
		}

		void main(void)
		{
				while(true)
				{
						/* Time Step? */
						waitfor(TIME_STEP);
						LOG("Formation: Running PSO Algorithm");
						memcpy(current_pos, positions, MAX_NO_DRONES*sizeof(vec));
						i_current = current_pos[ID];

						/* PSO when to run? */	
						pso();
						LOG("Formation: PSO complete");
						out_a.send(h);
				}
		}
};

interface Form_Init{ void init(long); };

behavior Formation(i_vec_receiver self_vec, i_ivec_receiver other_ivec, i_vec_sender out_a) implements Form_Init
{
		long ID;
		vec positions[MAX_NO_DRONES];
		Receive receive(self_vec, other_ivec, positions, ID);
		Path_Planning path_planning(out_a, positions, ID);

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
