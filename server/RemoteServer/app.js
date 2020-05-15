var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var cmd = require('child_process');
var fs = require('fs');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

global.cx = -1;
global.cy = -1;

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

app.post("/mouse_move",function(req, res, next) {
  if(cx != -1 && cy != -1){
    var angle = req.body.angle;
    var radio = req.body.radio;
    var cmd_string = "python pyscript/mouse_move.py "+angle+" "+radio+" "+cx +" "+cy;
    cmd.exec(cmd_string);
  }
  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);
});

app.get("/check_server",function(req,res,next){
  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);
});

app.post("/heart_beats",function(req,res,next){
  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);
});

app.post("/mouse_down",function(req, res, next){
  var fd = fs.openSync("pyscript/location.txt",'a+');
  var str = fs.readFileSync("pyscript/location.txt");
  if(str == ""){
    cmd.execSync("python pyscript/update_location.py");
    str = fs.readFileSync("pyscript/location.txt");
  }
  fs.closeSync(fd);
  var locations = str.toString().split(" ");
  global.cx = locations[0];
  global.cy = locations[1];
  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);
});

app.post("/mouse_btn_touch",function(req, res, next){
  var type = req.body.type;
  var state = req.body.state;
  cmd.execSync("python pyscript/mouse_touch.py "+type+" "+state);

  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);
});

app.post("/key_click",function(req, res, next){
  var id = req.body.id;
  cmd.execSync("python pyscript/key_click.py "+id);
  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);
});


app.post("/mouse_up",function(req, res, next){
  cmd.exec("python pyscript/update_location.py");
  global.cx = -1;
  global.cy = -1;
  var result = {
    errno:200,
    result:'ok'
  }
  res.send(result);

});

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
