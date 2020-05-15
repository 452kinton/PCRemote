import autopy
import sys

length  = len(sys.argv);

if length==3:
    type = sys.argv[1]
    state = sys.argv[2]
    if type=='0':
        if state =='1':
            autopy.mouse.toggle(autopy.mouse.Button.LEFT,True)
        else :
            autopy.mouse.toggle(autopy.mouse.Button.LEFT,False)
    elif type=='1':
        if state =='1':
            autopy.mouse.toggle(autopy.mouse.Button.RIGHT,True)
        else :
            autopy.mouse.toggle(autopy.mouse.Button.RIGHT,False)
else :
     print('参数不对！！')