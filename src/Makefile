JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class

%.class : %.java
	$(JAVAC) -d ../bin -cp ../lib/kryo2-2.02-all.jar:../bin $<

test:
	java -Xmx8000M -Xms2000M -cp ../lib/kryo2-2.02-all.jar:../bin  Run /mnt/cup/data/

