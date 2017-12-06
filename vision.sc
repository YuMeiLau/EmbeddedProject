#include "drone.sh"
#include "inc/vec.h"
#include "metrics.sh"
import "c_vec_queue";


behavior ImageCruncher(vec rel_position, long ID, struct metric_logger metric)
{
	void main(void)
	{
		waitfor(TIME_STEP);
		rel_position[_X] = metric._DRONE_POSITIONS[ID][_X] - metric._TARGET[_X];
		rel_position[_Y] = metric._DRONE_POSITIONS[ID][_Y] - metric._TARGET[_Y];
		rel_position[_Z] = metric._DRONE_POSITIONS[ID][_Z] - metric._TARGET[_Z];

	}
};

behavior ToNic(i_vec_sender vec_to_nic, vec rel_position)
{
	void main(void)
	{
			waitfor(FRAME_RATE);
			vec_to_nic.send(rel_position);

	}
};


behavior ToFormation(i_vec_sender vec_to_formation, vec rel_position)
{
	void main(void)
	{
			waitfor(FRAME_RATE);
			vec_to_formation.send(rel_position);
	}
};

interface V_Init{ void init(long); };

behavior Vision(i_vec_sender vec_to_nic, i_vec_sender vec_to_formation, struct metric_logger metric) implements V_Init
{
	long ID;
	vec rel_position;
	ImageCruncher rel_pos_algorithm(rel_position, ID, metric);
	ToNic nic_sender(vec_to_nic, rel_position); 
	ToFormation formation_sender(vec_to_formation, rel_position); 

	void init(long id){
		ID = id;
	}

        void main(void)
        {
		fsm{
			rel_pos_algorithm: goto nic_sender;
			nic_sender: goto formation_sender;
			formation_sender: goto rel_pos_algorithm;
		}
        }
};
