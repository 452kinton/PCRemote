import autopy

try:
    f = open('pyscript/location.txt','w+')
    x,y = autopy.mouse.location()
    f.write(str(x)+" "+str(y))
    f.close()
    pass
except expression as identifier:
    print('error')
    pass
