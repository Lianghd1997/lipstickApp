var fs = require('fs')

var readdir = function (callback) {
    fs.readdir('./public/uploads/',function (err,files) {
        if (err){
            return console.log('读取上传文件失败')
        }
        callback(null,files[files.length-2])
    })
}

module.exports = readdir