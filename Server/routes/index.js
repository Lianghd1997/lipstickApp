var express = require('express');
var router = express.Router();
var fs = require('fs');
var baidupic = require('../BAIDUAI/baidupic');
var photo = require('../picture/getcolor');
var getkouhong = require('../picture/RGB_compare');
var multer = require('multer');
var updatefile = require('./fs操作');
var updatefile2 = require('./twofs');
var facematch = require('../BAIDUAI/baiduface');
var addface = require('../BAIDUAI/baidufacedb');
var searchface = require('../BAIDUAI/BDsearchface');
var updatedb = require('../db/mongodb');

var storage = multer.diskStorage({
  //设置上传后文件路径，uploads文件夹需要手动创建！！！
  destination: function (req, file, cb) {
    cb(null, './public/uploads')
  },
  //给上传文件重命名，获取添加后缀名
  filename: function (req, file, cb) {
    var fileFormat = (file.originalname).split(".");
    cb(null, file.fieldname + '-' + Date.now() + "." + fileFormat[fileFormat.length - 1]);
  }
});
var upload = multer({
  storage: storage
});



router.get('/picture',function (req,res) {
  updatefile(function (err,filename) {
    if (err){
      console.log('上传文件为空或失败');
      return res.send({
        code:1,
        brand:"",
        name:"",
        id:"",
        colorname:""
      })
    }
    var upfilename = filename;
    updatedb('./public/uploads'+filename,function (err) {
      if (err){
        return console.log('上传到数据库失败')
      }
      console.log('上传到数据库成功')
    });
        baidupic(upfilename,function (err,data) {
          if (data.error_code !== 0){
            console.log('没有检测到人脸');
            return res.send({
              code:2,
              brand:"",
              name:"",
              id:"",
              colorname:""
            })
          }
          var location1 = data.result.face_list[0].landmark72[60];
          var location2 = data.result.face_list[0].landmark72[70];
          var location3 = data.result.face_list[0].landmark72[68];
          photo(upfilename,location2,function (err,RGB) {
            console.log(RGB);
            getkouhong(RGB,function (err,data) {
              if (data.length === 0){
                console.log('没有找到这个颜色的口红');
                return res.send({
                  code:3,
                  brand:"",
                  name:"",
                  id:"",
                  colorname:""
                })
              }
              res.send({
                code:0,
                brand:data[0][0],
                name:data[0][1],
                id:data[0][2],
                colorname:data[0][4]
              })
            })
          })
        })
  })

});

router.get('/upload',function (req,res) {
  res.render('update.html')
});

router.post('/upload', upload.single('avatar'), function(req, res, next) {
  var file = req.file;
  console.log(file);
  if (!file) {
    res.send('error');
  } else {
    res.redirect('/picture')
  }
});

router.post('/upload2', upload.single('avatar'), function(req, res, next) {
  var file = req.file;
  console.log(file);
  if (!file) {
    return res.send('error');
  }
  res.send('上传图片成功')
});



// router.post('/upload',upload.single('avatar'),function (req,res,next) {
//   var msg = {
//     body : req.body,
//     file : req.file
//   }
//   var output = fs.createWriteStream(File_Path+req.file.originalname)
//   var input = fs.createReadStream(req.file.path)
//   input.pipe(output)
//   res.json(msg)
// })

router.get('/files',function (req,res) {
  fs.readdir('public/uploads',function (err, files) {
    res.json(files)
  })
});

router.get('/picture1',function (req,res) {
  updatefile(function (err,filename) {
    if (err){
      console.log('上传文件为空或失败');
      return res.send('上传文件为空或失败')
    }
    var upfilename = filename;
    updatedb('./public/uploads'+filename,function (err) {
      if (err){
        return console.log('上传到数据库失败')
      }
      console.log('上传到数据库成功')
    });
    baidupic(upfilename,function (err,data) {
      if (data.error_code !== 0){
        console.log('没有检测到人脸');
        return res.send('没有检测到人脸')
      }
      var location1 = data.result.face_list[0].landmark72[60];
      var location2 = data.result.face_list[0].landmark72[70];
      var location3 = data.result.face_list[0].landmark72[68];
      photo(upfilename,location2,function (err,RGB) {
        console.log(RGB);
        getkouhong(RGB,function (err,data) {
          if (data.length === 0){
            console.log('没有找到这个颜色的口红');
            return res.send('没有找到这个颜色的口红')
          }
          res.render('index.html',{
            brand:data[0][0],
            name:data[0][1],
            id:data[0][2],
            colorname:data[0][4]
          })
        })
      })
    })
  })

});

router.get('/upload1',function (req,res) {
  res.render('update.html')
});

router.post('/upload1', upload.single('avatar'), function(req, res, next) {
  var file = req.file;
  console.log(file);
  if (!file) {
    res.send('error');
  } else {
    res.redirect('/picture1')
  }
});

router.get('/match',function (req,res) {
    updatefile(function (err,filename) {
        if(err){
            console.log('上传第一张图片为空或失败');
            return res.send('上传文件为空或失败')
        }
        var upfilename1 = filename;
        updatedb('./public/uploads'+filename,function (err) {
        if (err){
          return console.log('上传到数据库失败')
        }
          console.log('上传到数据库成功')
        });
        updatefile2(function (err,filename2) {
            if (err){
                console.log('上传第二张图片为空或失败');
                return res.send('上传文件为空或失败')
            }
            var upfilename2 = filename2;
            updatedb('./public/uploads'+filename2,function (err) {
              if (err){
                return console.log('上传到数据库失败')
              }
              console.log('上传到数据库成功')
           });
            facematch(upfilename1,upfilename2,function (err, data) {
              if (err) {
                return res.send('人脸对比失败')
              }
              res.render('matchresult.html',{
                  score:data.result.score
              })
            })
        })
    })
});

router.get('/newf',function (req,res) {
  res.render('newface.html')
});

router.post('/newface',function (req,res) {
  updatefile(function (err,filename) {
    if (err){
      console.log('上传图片为空或失败');
      return res.send('上传文件为空或失败')
    }
    var upfilename = filename;
    updatedb('./public/uploads'+filename,function (err) {
      if (err){
        return console.log('上传到数据库失败')
      }
      console.log('上传到数据库成功')
    });
    addface(upfilename,req.body.groupid,req.body.userid,req.body.userinfo,function (err,data) {
      if(data.error_code !== 0){
        return res.send('上传到库失败，或图片已经存在')
      }
      res.send('上传成功')
    })
  })
});

router.get('/search',function (req,res) {
  res.render('searchface.html')
});

router.post('/searchface',function (req,res) {
  updatefile(function (err,filename) {
    if (err){
      console.log('上传图片为空或失败');
      return res.send('上传文件为空或失败')
    }
    var upfilename = filename;
    updatedb('./public/uploads'+filename,function (err) {
      if (err){
        return console.log('上传到数据库失败')
      }
      console.log('上传到数据库成功')
    });
    searchface(upfilename,req.body.groupid,function (err,data) {
        console.log(data);
      if (data.error_code === 222207){
        return res.send('没有找到该人的信息')
      }else if (data.error_code === 0) {
        return res.render('searchresult.html', {
          groupid: data.result.face_list[0].user_list[0].group_id,
          userid: data.result.face_list[0].user_list[0].user_id,
          userinfo: data.result.face_list[0].user_list[0].user_info,
          score: data.result.face_list[0].user_list[0].score
        })
      }else {
        return res.send('识别人脸失败')
      }

    })

  })
});

// router.post('/upload',upload.single('avatar'),function (req,res,next) {
//   var msg = {
//     body : req.body,
//     file : req.file
//   }
//   var output = fs.createWriteStream(File_Path+req.file.originalname)
//   var input = fs.createReadStream(req.file.path)
//   input.pipe(output)
//   res.json(msg)
// })

router.get('/',function (req,res) {
  res.render('pictures.html')
});

module.exports = router;