/**
 * Created by pei on 2017/6/21.
 */
/**
 * Created by pei on 2017/6/15.
 */

// 获取  anyone  发送给自己的 信息  搜索前10块
function getMessage(receiver){

    result = "";
    endBlockNumber = web3.eth.blockNumber;
    startBlockNumber = endBlockNumber-10;
    //get latest block
    for (var i = startBlockNumber; i <= endBlockNumber; i++) {
        var block = web3.eth.getBlock(i, true);
        if (block != null && block.transactions != null) {
            block.transactions.forEach( function(e) {
                if (receiver == e.to) {

                    // var message = web3.toAscii(e.input );
                    var message = e.input;
                    result += message+",";
                    console.log("  receive input : " +  message);
                }
            })
        }
    }
    return result;
}

// 发送给 receiver
function sendMessage(receiver,message){
    if(message != null){

        web3.personal.unlockAccount(web3.eth.accounts[0],"qq062525");

        web3.eth.sendTransaction({from:web3.eth.accounts[0],to:receiver,value:web3.toWei(100,'finney'),data:web3.toHex(message)});

        console.log("send message "+message+" to "+receiver);
    }
}



// 获取当前外部账户 的最近的 合约地址
function getContractAddresses(myaccount, startBlockNumber, endBlockNumber){

    var result = "";
    if (endBlockNumber == null) {
        endBlockNumber = web3.eth.blockNumber;
        console.log("Using endBlockNumber: " + endBlockNumber);
    }
    if (startBlockNumber == null) {
        startBlockNumber = endBlockNumber - 1000;
        console.log("Using startBlockNumber: " + startBlockNumber);
    }
    console.log("Searching for transactions to/from account \"" + myaccount + "\" within blocks "  + startBlockNumber + " and " + endBlockNumber);

    for (var i = startBlockNumber; i <= endBlockNumber; i++) {
        if (i % 1000 == 0) {
            console.log("Searching block " + i);
        }
        var block = web3.eth.getBlock(i, true);
        if (block != null && block.transactions != null) {
            block.transactions.forEach( function(e) {
                if (myaccount == "*" || myaccount == e.from ) {
                    // 根据transactionID  获取交易信息
                    var tx = web3.eth.getTransactionReceipt(e.hash);
                    if (tx != null) {
                        if(tx.contractAddress != null){
                            result+= tx.contractAddress+",";
                            console.log("contract address:" + tx.contractAddress);
                        }
                    }

                }
            })
        }
    }

    return result;
}



function addOneRecord(i, address) {
    console.log("errrorrrr");
    $("#list").append("<li id='tb_"+i+"' class='normaltab' >"+ address+"" +
        "<button id='giveRightToVote' onclick='giveRightToVote()'>" +  "giveRightToVote"   +"</button> "+
        "<button id='vote' onclick='vote()'>" +  "vote"   +"</button> "+
        "<button id='getCounter' onclick='getCounter()'>" +   "getCounter"  +"</button> "+
        "<button id='getSum' onclick='getSum()'>" +   "getSum"  +"</button> "+
        "<button id='getCount' onclick='getCount()'>" +  "getCount"   +"</button> "+
        "<button id='isApproved' onclick='isApproved()'>" +   "isApproved"  +"</button> "+


        "<button id='increaseCounter' onclick='increaseCounter()'>" +  "increaseCounter"   +"</button> "+
        "<button id='watchEvents' onclick='watchEvents()'>" +   "watchEvents"  +"</button> "+
        "<button id='stopWatching' onclick='stopWatching()'>" +  "stopWatching"   +"</button> "+
        "<button id='getMessage' onclick='getMessage()'>" +   "getMessage"  +"</button> "+
        "</li>");

}

//初始化fileinput控件（第一次初始化）
function initFileInput(ctrlName, uploadUrl) {
    var control = $('#' + ctrlName);

    control.fileinput({
        language: 'zh', //设置语言
        uploadUrl: uploadUrl, //上传的地址
        // allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀
        showUpload: true, //是否显示上传按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        // uploadAsync: true,
    });
    control.on("fileuploaded",function (event, data, previesId, index) {
        $("#fileurl").val(data.response.fileurl);
        // alert(data.response.fileurl);

    })
}
