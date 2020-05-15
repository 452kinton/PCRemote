import autopy
import sys

length  = len(sys.argv);

if length==2:
    type = sys.argv[1]
    if type=='0':
        autopy.mouse.click()
    elif type=='1':
        autopy.mouse.click(autopy.mouse.Button.RIGHT)
else :
     print('参数不对！！')