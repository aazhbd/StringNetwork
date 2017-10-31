JFLAGS = -g
JC = javac
JVM= java 
FILE=
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES = \
        MultithreadedClient.java 

MAIN = MultithreadedClient

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN) ${send} ${rec}
clean:
	$(RM) *.class
