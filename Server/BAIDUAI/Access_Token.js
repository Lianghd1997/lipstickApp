var https = require('https');
var qs = require('querystring')


var GetToken = function (callback) {
    var param = qs.stringify({
        'grant_type': 'client_credentials',
        'client_id': 'KinAbVY0K2phIC3dwmErFFpg',
        'client_secret': 'CXynty6jc9wIH2hXSBRLr5ZvQ4pA3Cia'
    });
    https.get(
        {
            hostname: 'aip.baidubce.com',
            path: '/oauth/2.0/token?' + param,
            agent: false
        },
        function (res) {
            res.setEncoding('utf8');
            res.on('data',function (data) {
                access_token = JSON.parse(data).access_token
                callback(null,access_token)
            })
        }
    );
};

module.exports = GetToken;