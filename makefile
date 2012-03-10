#Computer Networks @Columbia University
#Spring 2012
#Programming Assignment 1

#Global vars
IDENTITY = RootFolder
SUBMISSION = /home/lc2817/submission
ZIP = $(IDENTITY).zip
CC = javac		
EXE = java
#First step
STEP_1 = step_1
SRC_STEP1 =$(shell find ./$(IDENTITY)/$(STEP_1)/src -name "*java")
COMP_STEP1 =$(patsubst %.java,%.class,$(SRC_STEP1))
SRC_PATH_STEP1 =$(IDENTITY)/$(STEP_1)/src
EXE_STEP_1 = Main
#Step 2 client
STEP_21 = step_2/client
EXE_STEP_21 = Main
SRC_STEP21 =$(shell find ./$(IDENTITY)/$(STEP_21)/src -name "*java")
COMP_STEP21 =$(patsubst %.java,%.class,$(SRC_STEP21))
SRC_PATH_STEP21 =$(IDENTITY)/$(STEP_21)/src
#Step 2 client_unreliable
STEP_22 = step_2/client_unreliable
EXE_STEP_22 = Main
SRC_STEP22 =$(shell find ./$(IDENTITY)/$(STEP_22)/src -name "*java")
COMP_STEP22 =$(patsubst %.java,%.class,$(SRC_STEP22))
SRC_PATH_STEP22 =$(IDENTITY)/$(STEP_22)/src
#Step 3 client
STEP_31 = step_3/client
EXE_STEP_31 = Main
SRC_STEP31 =$(shell find ./$(IDENTITY)/$(STEP_31)/src -name "*java")
COMP_STEP31 =$(patsubst %.java,%.class,$(SRC_STEP31))
SRC_PATH_STEP31 =$(IDENTITY)/$(STEP_31)/src
#Step 3 proxy
STEP_32 = step_3/proxy
EXE_STEP_32 = Main
SRC_STEP32 =$(shell find ./$(IDENTITY)/$(STEP_32)/src -name "*java")
COMP_STEP32 =$(patsubst %.java,%.class,$(SRC_STEP32))
SRC_PATH_STEP32 =$(IDENTITY)/$(STEP_32)/src
#Step 4 client
STEP_41 = step_4/client
EXE_STEP_41 = Main
SRC_STEP41 =$(shell find ./$(IDENTITY)/$(STEP_41)/src -name "*java")
COMP_STEP41 =$(patsubst %.java,%.class,$(SRC_STEP41))
SRC_PATH_STEP41 =$(IDENTITY)/$(STEP_41)/src
#Step 4 proxy
STEP_42 = step_4/proxy
EXE_STEP_42 = Main
SRC_STEP42 =$(shell find ./$(IDENTITY)/$(STEP_42)/src -name "*java")
COMP_STEP42 =$(patsubst %.java,%.class,$(SRC_STEP42))
SRC_PATH_STEP42 =$(IDENTITY)/$(STEP_42)/src

all:
	echo ${SRC_STEP1}
gui: 
	@java -jar GUI.jar
step_1:
	@$(CC) $(SRC_STEP1) -classpath $(SRC_PATH_STEP1)
	@$(EXE) -classpath $(SRC_PATH_STEP1) $(EXE_STEP_1) 
step_21: 
	@$(CC) $(SRC_STEP21) -classpath $(SRC_PATH_STEP21)
	@$(EXE) -classpath $(SRC_PATH_STEP21) $(EXE_STEP_21) 
step_22: 
	@$(CC) $(SRC_STEP22) -classpath $(SRC_PATH_STEP22)
	@$(EXE) -classpath $(SRC_PATH_STEP22) $(EXE_STEP_22) 
step_31: 
	
	@$(CC) $(SRC_STEP31) -classpath $(SRC_PATH_STEP31)
	@$(EXE) -classpath $(SRC_PATH_STEP31) $(EXE_STEP_31) $(HOST) $(PORT) 
step_32: 
	@$(CC) $(SRC_STEP32) -classpath $(SRC_PATH_STEP32)
	@$(EXE) -classpath $(SRC_PATH_STEP32) $(EXE_STEP_32) $(PORT)
step_41: 
	@$(CC) $(SRC_STEP41) -classpath $(SRC_PATH_STEP41)
	@$(EXE) -classpath $(SRC_PATH_STEP41) $(EXE_STEP_41) $(HOST) $(PORT) $(ADDI)
step_42:
	@$(CC) $(SRC_STEP42) -classpath $(SRC_PATH_STEP42)
	@$(EXE) -classpath $(SRC_PATH_STEP42) $(EXE_STEP_42) $(PORT)

submit:
	mkdir -p $(IDENTITY)/$(IDENTITY)
	mv  $(IDENTITY)/step* $(IDENTITY)/$(IDENTITY)
	cp makefile ./$(IDENTITY)
	zip -r $(ZIP) $(IDENTITY)
	$(SUBMISSION) $(ZIP)
	@make clean
	mv $(IDENTITY)/$(IDENTITY)/* $(IDENTITY)/
	rm -rf $(IDENTITY)/$(IDENTITY)
	rm $(IDENTITY)/makefile

submit_ee:
	mkdir -p $(IDENTITY)/$(IDENTITY)
	mv  $(IDENTITY)/step* $(IDENTITY)/$(IDENTITY)
	cp makefile ./$(IDENTITY)
	zip -r $(ZIP) $(IDENTITY)
	$(SUBMISSION) $(ZIP)
	#@make clean
	mv $(IDENTITY)/$(IDENTITY)/* $(IDENTITY)/
	rm -rf $(IDENTITY)/$(IDENTITY)
	rm $(IDENTITY)/makefile


.PHONY: clean
clean:
	@rm -f $(ZIP)
	@rm -f $(COMP_STEP1)
