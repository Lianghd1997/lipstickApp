var fs = require('fs');

var getkouhong = function (RGB,callback) {
    function RGB_compare(RGB){
        //获取lipstick数据库;
        var data = fs.readFileSync("./public/lipstick_RGB.json");
        // console.log(data.toString());
        var lipstick = JSON.parse(data);
        // console.log(lipstick);
        var kouhong = [];
        var aim_lipstick = []; //分别输出口红品牌、系列名、颜色名、颜色码
        var aim_RGB = [];  //数据库颜色码转化

        // var colorHex = "#E21B2B";
        //
        // //十六进制颜色码转RGB
        // HextoRGB = function(colorHex){
        //     var RGB = [2];
        //     colorHex = colorHex.substr(1);
        //     var pos = 0;
        //     var n = 0;
        //     if(colorHex.length % 2!==0){
        //         console.log("something wrong");
        //     }
        //     else{
        //         for(m=0;m<colorHex.length/2;m++){
        //             colorHex = colorHex.toLowerCase();
        //             var s = colorHex.substr(pos,2);
        //             var v = parseInt(s,16);
        //             pos += 2;
        //             RGB[n] = v;
        //             n +=1;
        //         }
        //     }
        //     return RGB
        // };
        // RGB = HextoRGB(colorHex);
        // console.log(typeof (RGB));


        //口红品牌统计
        var num_brands = lipstick["brands"].length;
        var brands = [num_brands-1];
        for(i=0; i<lipstick["brands"].length; i++){
            brands[i]=lipstick["brands"][i]["name"];
        }
        // console.log("匹配的口红品牌有:",brands);
        // console.log(lipstick["brands"][0]);
        // console.log(lipstick["brands"][0]["series"][0]);
        // console.log(lipstick["brands"][0]["series"][0]["lipsticks"][0]);
        // console.log(lipstick["brands"][0]["series"][0]["lipsticks"][0]["color"]);

        for(i=0;i<num_brands;i++){
            for(j=0;j<lipstick["brands"][i]["series"].length;j++){
                for(k=0;k<lipstick["brands"][i]["series"][j]["lipsticks"].length;k++){
                    aim_RGB = lipstick["brands"][i]["series"][j]["lipsticks"][k]["RGB"];
                    // aim_RGB = HextoRGB(aim_RGB = lipstick["brands"][i]["series"][j]["lipsticks"][k]["color"]);

                    // 误差比较
                    if(Math.abs(RGB[0]-aim_RGB[0])<10 && Math.abs(RGB[1]-aim_RGB[1])<10 && Math.abs(RGB[2]-aim_RGB[2])<10) {
                        aim_lipstick[0] = lipstick["brands"][i]["name"];
                        aim_lipstick[1] = lipstick["brands"][i]["series"][j]["name"];
                        aim_lipstick[2] = lipstick["brands"][i]["series"][j]["lipsticks"][k]["id"];
                        aim_lipstick[3] = lipstick["brands"][i]["series"][j]["lipsticks"][k]["color"];
                        aim_lipstick[4] = lipstick["brands"][i]["series"][j]["lipsticks"][k]["name"];
                        kouhong.push(aim_lipstick)
                    }
                }
            }
        }
        console.log(kouhong);
        if (kouhong !=null){
            callback(null,kouhong)
        }else {
            callback(null,kouhong)
        }
    }
    RGB_compare(RGB);
};
module.exports = getkouhong;