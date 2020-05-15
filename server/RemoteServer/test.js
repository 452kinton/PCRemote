var fs = require('fs');
var cmd = require('child_process');

var fd = fs.openSync("pyscript/location.txt",'a+');
var str = fs.readFileSync("pyscript/location.txt");
console.log(str.toString());
if(str == ""){
  cmd.execSync("python pyscript/update_location.py");
  str = fs.readFileSync("pyscript/location.txt");
}
console.log(str.toString());
fs.closeSync(fd);
var locations = str.toString().split(" ");
global.cx = locations[0];
global.cy = locations[1];

