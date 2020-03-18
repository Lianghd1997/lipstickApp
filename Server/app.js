var express = require('express');
var router = require('./routes/index');
var bodyparser = require('body-parser');
//开启服务器
var app = express();

//对用户开放public文件夹和node_modules文件夹
app.use('/public/',express.static('./public'));
app.use('/node_modules/',express.static('./node_modules'));
//导入express的网页渲染
app.engine('html',require('express-art-template'));
//使express服务器端能够接受到post请求
app.use(bodyparser.urlencoded({ extended:false }));
app.use(bodyparser.json());

app.use(router);

app.listen(3000,function () {
    console.log('running at port 3000...')
});
