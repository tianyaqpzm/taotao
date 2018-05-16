/**
 * Created by pei on 2017/6/22.
 */

var Web3 = require('web3');


$(function () {



});
$("#createAddress").click(function () {
    console.log("createAddress");
    $("#register").toggle();
});

$("#register1").click(function(){
    var username = $("#username").val();
    var password = $("#password").val();
    var password1 = $("#password1").val();
    if(password !== password1){
        $("#password").css("border","1px solid red");
        $("#password1").css("border","1px solid red");
    }else if(password === password1){
        var keyObject = Register.register(password);
        var data = {"name":username,"password":password,"address":keyObject.address};
        $.ajax({
            url: "http://localhost:8080/backend/address/register",
            contentType:'application/json;charset=UTF-8',
            type: 'post',
            data: data,
            success: function(data,status){
                if(status == 'success'){
                    alert("恭喜你，注册新地址成功");
                }
            },
            error: function(data,err){
                alert("Sorry，注册新地址失败");
            }
        });
    }
});

window.Register = {
    register: function(password) {
        //      方式3
// optional private key and initialization vector sizes in bytes
// (if params is not passed to create, keythereum.constants is used by default)
//         var params = { keyBytes: 32, ivBytes: 16 };  应该是在浏览器中 localstore 中 加密
// synchronous
        var dk = keythereum.create();

// Note: if options is unspecified, the values in keythereum.constants are used.
        var options = {
            kdf: "pbkdf2",
            cipher: "aes-128-ctr",
            kdfparams: {
                c: 262144,
                dklen: 32,
                prf: "hmac-sha256"
            }
        };
// synchronous
        var keyObject = keythereum.dump(password, dk.privateKey, dk.salt, dk.iv, options);

        // keythereum.exportToFile(keyObject,'/Users/pei/program/DApp/chain/keystore');
        console.log(keyObject);
        console.log(keyObject.address);
        return keyObject;
    }
};

window.addEventListener('load',function () {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof web3 !== 'undefined') {
        console.warn("Using web3 detected from external source. If you find that your accounts don't appear or you have 0 MetaCoin, ensure you've configured that source properly. If using MetaMask, see the following link. Feel free to delete this warning. :) http://truffleframework.com/tutorials/truffle-and-metamask")
        // Use Mist/MetaMask's provider
        window.web3 = new Web3(web3.currentProvider);
    } else {
        console.warn("No web3 detected. Falling back to http://localhost:8545. You should remove this fallback when you deploy live, as it's inherently insecure. Consider switching to Metamask for development. More info here: http://truffleframework.com/tutorials/truffle-and-metamask");
        // fallback - use your fallback strategy (local node / hosted node + in-dapp id mgmt / fail)
        window.web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));
    }
})