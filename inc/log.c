#include "log.h"

/* my macro kung-fu is weak... */

void LOG(const char* msg){
	if (DISPLAY_LOGS){
		printf(msg);
	}
}

void LOGL(const char* msg, long var){
	if (DISPLAY_LOGS){
		printf(msg, var);
	}
}

void LOG_VERBOSE(const char* msg){
	if (DISPLAY_LOGS && DISPLAY_VERBOSE_LOGS){
		printf(msg);
	}
}

void LOGL_VERBOSE(const char* msg, long var){
	if (DISPLAY_LOGS && DISPLAY_VERBOSE_LOGS){
		printf(msg, var);
	}
}
