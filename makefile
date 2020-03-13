all:
	./gradlew run
	emacs src/main/resources/img.ppm
clean:
	rm -r build/
