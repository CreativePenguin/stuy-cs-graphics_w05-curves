all:
	./gradlew run
	emacs face.ppm
clean:
	rm -r build/
