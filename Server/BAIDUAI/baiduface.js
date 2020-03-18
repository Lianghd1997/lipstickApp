var BaiduAi = require('./baiduAI1')
var qs = require('querystring')
var fs = require('fs')

var BaiduFace = function(filename1,filename2,callback){
    var bitmap1 = fs.readFileSync('./public/uploads/'+filename1)
    var bitmap2 = fs.readFileSync('./public/uploads/'+filename2)
    var base64str1 = new Buffer(bitmap1).toString('base64')
    var base64str2 = new Buffer(bitmap2).toString('base64')
    var contents = JSON.stringify(
        [{
            image:base64str1,
            image_type:'BASE64'
        },
        {
            image:base64str2,
            image_type:'BASE64'
        }]
    )
    BaiduAi(contents,function (err,data) {
        // if (JSON.parse(data).error_code !== 0){
        //     return  callback(err)
        // }
        // console.log(data)
        // var PicData = JSON.parse(data).result.face_list[0].landmark72
        // callback(null,PicData)
        // if (JSON.parse(data).error_code !== 0){
        //     return callback(err)
        // }
        var PicData = JSON.parse(data)
        //.result.face_list[0].landmark72
        callback(null,PicData)
    })
}

module.exports = BaiduFace