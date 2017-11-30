#include <stddef.h>	// get size_t for malloc declaration
#include "drone.sh"
#include "inc/vec.h"

extern void perror(const char*);

extern void *memcpy(void*, const void*, size_t);

extern void abort(void);
extern void *malloc(size_t);
extern void free(void*);


interface i_mon_receive
{
    void receive(void *d, int idx);
};

interface i_mon_send
{
    void send(const void *d);
};

channel c_mon_array(in const unsigned long size)
	implements i_mon_send, i_mon_receive
{
    int i, index;
    long **buffer = 0;

    void setup(void)
    {
	if (!buffer)
	{
	    if (!(buffer = (long**) malloc(size * sizeof(long*))))
	    {
		perror("c_mon_array");
		abort();
	    }
            for (i = 0; i < size; i++){
		buffer[i] = (long*) malloc(3 * sizeof(long));
            }
	}
    }

    void cleanup(void)
    {
        for (i = 0; i < size; i++){
            free(buffer[i]);
        } 
	free(buffer);
	buffer = 0;
    }

    void receive(void *d, int idx)
    {
	if (idx < size)
	{
	    memcpy(d, &buffer[idx][0], 3*sizeof(int));
	}
	else
	{
	    perror("c_queue receiver");
	    abort();
	}
    }

    void send(const void *d)
    {
	ivec *p;
	p = (ivec*)d;
	index = (*p)[_ID];
	if (index < size)
	{
	   memcpy(&buffer[index][0], d, 3 * sizeof(int));
	}
	else
	{
	    perror("c_queue sender");
	    abort();
	}
    }
};


// EOF c_queue.sc
