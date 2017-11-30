#include <c_typed_queue.sh>	/* make the template available */

typedef long vec[3];

DEFINE_I_TYPED_TRANCEIVER(vec, vec)
DEFINE_I_TYPED_SENDER(vec, vec)
DEFINE_I_TYPED_RECEIVER(vec, vec)
DEFINE_C_TYPED_QUEUE(vec, vec)

