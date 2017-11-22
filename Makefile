# -----------------------------------
# Makefile for Susan Edge Detection example
# ----------------------------------- 

# --- Macros

SCC = scc
CMP = diff -s
RM  = rm -f

SCFILES = monitor.sc wireless.sc drone.sc system.sc\
		c_ivec_array.sc c_vec_queue.sc c_ivec_queue.sc

SIRFILES = monitor.sir wireless.sir drone.sir system.sir\
                c_ivec_array.sir c_vec_queue.sir c_ivec_queue.sir

SCCOPTS = -vv -ww -g -d #-par

EXEFILE = system


#--- Rules

.SUFFIXES: .sc .sir

.sc.sir:
	$(SCC) $* -sc2sir $(SCCOPTS)

#--- Build process

all: $(EXEFILE) 

$(EXEFILE): $(SIRFILES)
	$(SCC) $(EXEFILE) -sc2out $(SCCOPTS)
	$(RM) *.sir

clean:
	$(RM) *~ *.si *.o *.h *.cc out*.pgm
	$(RM) $(SIRFILES) 
	$(RM) $(EXEFILE) $(EXEFILE_PAR)

