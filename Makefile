# -----------------------------------
# Makefile 
# ----------------------------------- 

# --- Macros

SCC = scc
CMP = diff -s
RM  = rm -f

SCFILES = c_ivec_array.sc c_vec_queue.sc c_ivec_queue.sc\
          controller.sc drone.sc formation.sc monitor.sc nic.sc system.sc\
          vision.sc wireless.sc

SIRFILES = c_ivec_array.sir c_vec_queue.sir c_ivec_queue.sir\
           controller.sir drone.sir formation.sir monitor.sir nic.sir system.sir\
           vision.sir wireless.sir

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

test:	$(EXEFILE)
	./$(EXEFILE)
