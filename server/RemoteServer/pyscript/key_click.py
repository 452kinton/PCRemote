import autopy
import sys

length  = len(sys.argv);

if length==2:
    id = sys.argv[1]
    if id=='0':
        autopy.key.toggle(autopy.key.Code.UP_ARROW,True)
        autopy.key.toggle(autopy.key.Code.UP_ARROW,False)
    elif id=='1':
        autopy.key.toggle(autopy.key.Code.DOWN_ARROW,True)
        autopy.key.toggle(autopy.key.Code.DOWN_ARROW,False)
    elif id=='2':
        autopy.key.toggle(autopy.key.Code.LEFT_ARROW,True)
        autopy.key.toggle(autopy.key.Code.LEFT_ARROW,False)
    elif id=='3':
        autopy.key.toggle(autopy.key.Code.RIGHT_ARROW,True)
        autopy.key.toggle(autopy.key.Code.RIGHT_ARROW,False)
    elif id=='4':
        autopy.key.toggle(autopy.key.Code.ESCAPE,True)
        autopy.key.toggle(autopy.key.Code.ESCAPE,False)
    elif id=='5':
        autopy.key.toggle(autopy.key.Code.SPACE,True)
        autopy.key.toggle(autopy.key.Code.SPACE,False)
else :
     print('参数不对！！')