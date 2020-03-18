var fs = require("fs");

// 连接本地mongodb
var MongoClient = require("mongodb").MongoClient;
var url = "mongodb://localhost:27017/";
MongoClient.connect(url, { useNewUrlParser: true,useUnifiedTopology: true }, function(err, db) {
    if (err){
        throw err;
    }
    console.log("连接成功");
    var dbase = db.db("lipstick");
    dbase.createCollection('lipstick', function (err, res) {
        if (err) throw err;
        console.log("创建集合!");

        // 插入文件
        var fileName = "G:/js代码/FirstTest/public/lipstick_RGB.json";
        console.log("读取json文件："+fileName);
        var fileContent = fs.readFileSync(fileName);
        if(fileContent){
            console.log("fileContent .len="+fileContent.length);

            // 写入数据库
            var objfile = JSON.parse(fileContent);
            dbase.collection("lipstick").insertOne(objfile,function (err,res) {
                if (err){
                    throw err;
                }
                console.log("objfile文件写入成功");
                db.close();
            })
        }
    })
});