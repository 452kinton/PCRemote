import autopy
import math
import sys

width,height = autopy.screen.size()
print("x,y:"+str(width)+","+str(height))
max_lenght = width/2
if width>height:
    max_lenght = height/2
length = len(sys.argv)
if length==5:
    radio = float(sys.argv[2])
    angle = float(sys.argv[1])*math.pi/180
    currentx = float(sys.argv[3])
    currentY = float(sys.argv[4])
    disx = (max_lenght*radio/2)*math.cos(angle)
    disY = (max_lenght*radio/2)*math.sin(angle)

    # f = open('location.txt')
    # location_str = f.read()
    # f.close()
    # currentx,currentY = currentx,currentY = autopy.mouse.location()
    # if len(location_str)>0:
    #     locations = location_str.split(' ')
    #     currentx  = float(locations[0])
    #     currentY =  float(locations[1])
    
    #print("cx,cy:"+str(currentx)+","+str(currentY))
    if (currentx+disx)>width: 
        currentx = width-1
    elif  (currentx+disx)<0:
        currentx =1
    else:
        currentx+= disx
    
    if (currentY+disY)>height: 
        currentY = height-1
    elif  (currentx+disY)<0:
        currentY =1
    else:
        currentY+= disY

    #print("x,y:"+str(currentx)+","+str(currentY))
    autopy.mouse.move(currentx,currentY)
else :
    print('参数不对！！')