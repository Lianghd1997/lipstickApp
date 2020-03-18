//人脸识别接口

var https = require('https');
var qs = require('querystring');
var fs = require('fs');
var Access_Token = require('./Access_Token');

// var bitmap = fs.readFileSync('E:\\Node.js\\FirstTest\\public\\images/pic2.jpg')
// var base64str = new Buffer(bitmap).toString('base64')
// var contents = qs.stringify({
//     image:base64str,
//     image_type:'BASE64',
//     face_field:'landmark'
// })



var BaiduAI = function (contents,callback) {
    Access_Token(function (err,access_token) {
        var options = {
            host:'aip.baidubce.com',
            path:'/rest/2.0/face/v3/detect?access_token="'+access_token+'"',
            method:'POST',
            headers:{
                'Content-Type': 'application/json'
            }
        };
        var req = https.request(options,function (res) {
            res.setEncoding('utf8');
            var result = '';
            res.on('data', function (chunk) {
                result += chunk
            });
            res.on('end',function () {
                callback(null,result)
            })
        });
        req.write(contents);
        req.end()
    })




};

// BaiduAI(contents,function (err,data) {
//     console.log(JSON.parse(data).result.face_list[0].landmark72[59])
// })


module.exports = BaiduAI;