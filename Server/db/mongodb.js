var mongoose = require('mongoose');
var Schema = mongoose.Schema;

mongoose.connect('mongodb://localhost/lipstick');

var pathSchema = new Schema({
    path:{
        type:String,
        required:true
    }
})

var Path =mongoose.model('Path',pathSchema);

var savepath = function (path,callback) {
    var pic = new Path({path});
    pic.save(function (err,ret) {
        if (err){
            return console.log('存入数据库失败');
            callback(err)
        }
        console.log('存入数据库成功');
        callback(null)
    })
};

module.exports = savepath;