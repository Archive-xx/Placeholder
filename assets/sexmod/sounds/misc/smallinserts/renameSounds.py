from pydub import AudioSegment
import os

path = os.getcwd()

lenght = len(path)-1
i = lenght
soundName = ""

# getting the sound name out of the directory
while i > -1:

	if(path[i] != '\\'):
		soundName += path[i]
	else:
		break
	i-=1

# reverting it and putting it into lowercase
soundName = soundName[::-1].lower()

i = 0

# actually renaming it
for file in os.listdir():

	# converting it into .ogg and renaming it	
	if(file.endswith('.mp3')):

		AudioSegment.from_mp3(open(file, "rb")).export(soundName+str(i)+'.ogg', format='ogg')
		print("turned "+ file + " into " + soundName+str(i)+'.ogg')
		os.remove(file)

	else:
		# this is not a file i want to rename
		print("skipped " + file)
		continue
	i+=1

