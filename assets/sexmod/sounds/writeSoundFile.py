import os
import json

data = {}


for dirpath, dirnames, filenames in os.walk("."):
    for filename in [f for f in filenames if f.endswith(".ogg")]:

        objectname = os.path.join(dirpath, filename)[2:-4].replace("\\", ".")
        path = "sexmod:" + os.path.join(dirpath, filename)[2:-4].replace("\\", "/")   

        nameEnd = -1
        nameStart = -1

        while objectname[nameEnd].isdigit():
            nameEnd-=1

        while not objectname[nameStart] == ".":
            nameStart-=1
        
        subtitle = objectname[nameStart+1:nameEnd+1]

        data[objectname] = {"category": "entity", "subtitle": subtitle, "sounds": [{"name": path, "stream": True}]}
        


# saving the data var into the json
with open('sounds.json', 'w') as outfile:
    json.dump(data, outfile)