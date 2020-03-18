//获取图片数据
var getPixels = require("get-pixels");

var fs = require('fs');
var RGB = [2];
var colorHex = 0;

// ;
// getPixels("E:\\Nodejs_project\\lipstick\\color_dir\\color1.jpg",function (err,pixels) {
//     if(err){
//         console.log("worry image path");
//         return
//     }
//     console.log("图片数据:",pixels);
// });
    // data描述颜色的RGBA值
    // shape表示数组维度和各个维度的大小,即图片长宽，第三个维度表示RGBA值
    // stride是数组各个维度的步长
    // offset表示起始值

photo = function (filename,location,callback) {
    var path = "./public/uploads/"+filename;
    getPixels(path,function (err, pixels) {
        if (err) {
            console.log("something wrong");
            callback(err);
            return
        }

        length = pixels.shape[0];
        width = pixels.shape[1];
        // console.log("图片大小为:",length,"*", width);

        //获取每个像素点RGB值求平均
        var R = 0;
        var G = 0;
        var B = 0;
        var i = Math.round(location.x);
        var j = Math.round(location.y);
        console.log(i);
        console.log(j);
        R += (pixels.get(i, j, 0));
        G += (pixels.get(i, j, 1));
        B += (pixels.get(i, j, 2));
        RGB[0] = parseInt(R);
        RGB[1] = parseInt(G);
        RGB[2] = parseInt(B);
        // console.log(RGB)

        // for (i = 0; i <= length; i++) {
        //     for (j = 0; j <= width; j++) {
        //         if (!isNaN(pixels.get(i, j, 0))) {
        //             R += (pixels.get(i, j, 0));
        //             G += (pixels.get(i, j, 1));
        //             B += (pixels.get(i, j, 2));
        //         }
        //     }
        // }

        // RGB[0] = parseInt(R / length / width);
        // RGB[1] = parseInt(G / length / width);
        // RGB[2] = parseInt(B / length / width);
        // console.log("图片RGB值为:", RGB);

        //转换为十六进制颜色码

        // var hex_RGB = [2];
        // for (k = 0; k < 3; k++) {
        //     if (RGB[k] < 10) {
        //         hex_RGB[k] = "0" + RGB[k].toString(16);
        //     } else {
        //         hex_RGB[k] = RGB[k].toString(16);
        //     }
        // }
        // colorHex = "#" + hex_RGB[0] + hex_RGB[1] + hex_RGB[2];
        // colorHex = colorHex.toUpperCase();
        // console.log(RGB)
        //console.log("图片十六进制颜色码为:", colorHex);

        callback(null,RGB)
    })
};
// photo(location,function (err,RGB) {
//     console.log(RGB);
//     // console.log(colorHex);
// });
module.exports = photo;
//module.exports导出
// module.exports = getcolor;