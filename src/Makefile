JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class

%.class : %.java
	$(JAVAC) -d ../bin -cp ../bin $<

test:
	java -cp ../bin Run /mnt/cup/data/
