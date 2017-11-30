#include <c_typed_queue.sh>	/* make the template available */

typedef long ivec[4];

DEFINE_I_TYPED_TRANCEIVER(ivec, ivec)
DEFINE_I_TYPED_SENDER(ivec, ivec)
DEFINE_I_TYPED_RECEIVER(ivec, ivec)
DEFINE_C_TYPED_QUEUE(ivec, ivec)

