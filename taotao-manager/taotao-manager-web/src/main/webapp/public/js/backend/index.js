// Import the page's CSS. Webpack will know what to do with it.

// Import libraries we need.
var Web3 = require('web3');


var voteabi = [{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"proposals","outputs":[{"name":"voteNum","type":"uint256"},{"name":"voteCount","type":"uint256"},{"name":"document","type":"address"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"doc","type":"address"}],"name":"getCount","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":true,"inputs":[{"name":"proposed","type":"address"}],"name":"isApproved","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"approve","type":"address"}],"name":"vote","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"_addressProposal","type":"address"}],"name":"addDocument","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"voters","outputs":[{"name":"weight","type":"uint256"},{"name":"voted","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"voter","type":"address"},{"name":"docAddress","type":"address"}],"name":"giveRightToVote","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"doc","type":"address"}],"name":"getSum","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"inputs":[],"type":"constructor","payable":true},{"anonymous":false,"inputs":[{"indexed":false,"name":"_voter","type":"address"},{"indexed":false,"name":"doc","type":"address"}],"name":"SomeoneHasVoted","type":"event"}];

var accounts;
var account;

var user1 = "0x3da1ed43a912c26254cd7883ebbf5b930d22bb42";

// 采取 jquery 方式  建立点击事件
$('#vote').click(function () {
    vote.newVote();
});

function getCounter() {
    // 找到对应的address  eg.  "0x74ec46ab7b40b8bb23f69586cb6e7ad8c678fc71"
    addr1 = "0x74ec46ab7b40b8bb23f69586cb6e7ad8c678fc71";
    var myvoteContract = web3.eth.contract(voteabi);
    // var contract_instance = web3.eth.contract(myvoteContract).at(addr1);
    var contract_instance = myvoteContract.at(addr1);
    document.getElementById("myCounter").innerText=contract_instance.getCount.call();
}
function increaseCounter() {

}
function watchEvents() {

}
function stopWatching() {

}
function getMessage() {

}

function getSum() {
    addr1 = "0x74ec46ab7b40b8bb23f69586cb6e7ad8c678fc71";
    var cont = web3.eth.contract(voteabi).at(addr1);
    console.log(cont);
    document.getElementById("getSum").innerText=cont.getSum.call();


}



function getCount() {
    // 找到对应的address  eg.  "0x74ec46ab7b40b8bb23f69586cb6e7ad8c678fc71"
    addr1 = "74ec46ab7b40b8bb23f69586cb6e7ad8c678fc71";
    // var con = web3.eth.contract(voteabi, addr1);
    var con = web3.eth.contract(voteabi).at(addr1);
    document.getElementById("getCount").innerText=con.getCount.call(user1);

}


function listContract() {
    var list = new Array();

    var str = getContractAddresses(user1);
    list = str.split(",");

    for(i=0;i<list.length;i++){
        // $("ul").append("<li id='tb_"+i+"' class='normaltab' >"+ list[i]+"</li>")
        addOneRecord(i,list[i]);
    }
}

// 发起人 向 boss  发起投票请求

// 列出 有权投票的所有人：

function sendMess() {
    var contractAddress = document.getElementById("contractAddress")

    var receiver = new Array();
    var addr1 = document.getElementById("address1");
    if(addr1.value!==''){  // 检查地址是否合法
        receiver.push(addr1.value);
    }
    var ch=document.getElementsByName("address");
    for(j=0;j<ch.length;j++){
        if(ch[j].checked){
            receiver.push(ch[j].value);
        }
    };
    for(i=0;i<receiver.length;i++){
        // 发送合约地址
        sendMessage(receiver[i],contractAddress.value);
    }
}



window.vote = {
  // 页面加载 调用， 设置合约提供者， 获取当前的余额。。显示 可选择的审阅者
  start: function() {
    var self = this;
    //  从数据库 获取 address  此处用初始地址代替：
    // 从geth 获取账户   这个可以从当前用户  mongo 中获取，并解锁  ？暂时如此
    //   web3.eth.defaultAccount = web3.eth.coinbase;

    web3.personal.unlockAccount(user1,"qq062525");
    web3.eth.getAccounts(function(err, accs) {
      if (err != null) {
        alert("There was an error fetching your accounts.");
        return;
      }
      if (accs.length == 0) {
        alert("Couldn't get any accounts! Make sure your Ethereum client is configured correctly.");
        return;
      }
      accounts = accs;
      account = accounts[0];
      // self.refreshVote();
    });
  },


  newVote: function () {
      this.setStatus('"Initiating transaction... (please wait)")');
      var transaction = document.getElementById('transaction');
      var myvoteContract = web3.eth.contract([{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"proposals","outputs":[{"name":"voteNum","type":"uint256"},{"name":"voteCount","type":"uint256"},{"name":"document","type":"address"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"doc","type":"address"}],"name":"getCount","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":true,"inputs":[{"name":"proposed","type":"address"}],"name":"isApproved","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"approve","type":"address"}],"name":"vote","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"_addressProposal","type":"address"}],"name":"addDocument","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"voters","outputs":[{"name":"weight","type":"uint256"},{"name":"voted","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"voter","type":"address"},{"name":"docAddress","type":"address"}],"name":"giveRightToVote","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"doc","type":"address"}],"name":"getSum","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"inputs":[],"type":"constructor","payable":true},{"anonymous":false,"inputs":[{"indexed":false,"name":"_voter","type":"address"},{"indexed":false,"name":"doc","type":"address"}],"name":"SomeoneHasVoted","type":"event"}]);

      var ballot = myvoteContract.new(
          {
              from: user1,
              data: '0x60606040525b33600060006101000a81548173ffffffffffffffffffffffffffffffffffffffff02191690830217905550600160026000506000600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050600001600050819055505b6107cb8061009c6000396000f36060604052361561008a576000357c0100000000000000000000000000000000000000000000000000000000900480633341b4451461008f5780634f0cd27b146100e4578063673448dd146101155780636dd7d8ea146101485780637c37ab0614610165578063a3ec138d14610182578063c05e5776146101bc578063ecb86574146101e25761008a565b610002565b34610002576100aa6004808035906020019091905050610213565b604051808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff168152602001935050505060405180910390f35b34610002576100ff6004808035906020019091905050610266565b6040518082815260200191505060405180910390f35b346100025761013060048080359060200190919050506102aa565b60405180821515815260200191505060405180910390f35b34610002576101636004808035906020019091905050610347565b005b346100025761018060048080359060200190919050506104f3565b005b346100025761019d6004808035906020019091905050610629565b6040518083815260200182151581526020019250505060405180910390f35b34610002576101e06004808035906020019091908035906020019091905050610660565b005b34610002576101fd6004808035906020019091905050610787565b6040518082815260200191505060405180910390f35b60016000506020528060005260406000206000915090508060000160005054908060010160005054908060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905083565b6000600160005060008373ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000506001016000505490506102a5565b919050565b6000600060009050600160005060008473ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050600001600050546002600160005060008673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000506001016000505402111561033257600190508050610339565b6000905080505b809150610341565b50919050565b6000600260005060003373ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005090508060010160009054906101000a900460ff168061041557508173ffffffffffffffffffffffffffffffffffffffff16600160005060008473ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614155b1561041f576104ef565b60018160010160006101000a81548160ff021916908302179055508060000160005054600160005060008473ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000506001016000828282505401925050819055507fe55fa2d1f4c7dbbeb4326a48dc41866cd56b62988faa2f16d57aac695614ec5a3383604051808373ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a15b5050565b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156105535761000256610625565b80600160005060008373ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908302179055506000600160005060008373ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050600101600050819055506000600160005060008373ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050600001600050819055505b5b50565b60026000506020528060005260406000206000915090508060000160005054908060010160009054906101000a900460ff16905082565b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415806106f95750600260005060008373ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060010160009054906101000a900460ff165b1561070357610783565b6001600260005060008473ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060000160005081905550600160005060008273ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000506000016000818150548092919060010191905055505b5050565b6000600160005060008373ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000506000016000505490506107c6565b91905056',
              gas: '4700000'
          }, function (e, contract){
              if (typeof contract.address !== 'undefined') {
                  var info = 'Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash;
                  var status = document.getElementById("status");
                  status.innerHTML = info;

                  document.getElementById("getCounter").style.visibility="visible";
                  document.getElementById("increaseCounter").style.visibility="visible";
                  document.getElementById("watchEvents").style.visibility="visible";
                  document.getElementById("stopWatching").style.visibility="visible";
                  document.getElementById("getMessage").style.visibility="visible";

              }
          });
      console.log(ballot);
  },

  setStatus: function(message) {
    var status = document.getElementById("status");
    status.innerHTML = message;
  },

    // 通过jiaoyihash 查询是否被挖出    callback 交易被确认后  执行的回调函数

    callWhenMined: function(txHash,callback){
        web3.eth.getTransactionReceipt(txHash,function(error,rept){
        if(error){
            console.error(error);
        }else{
            if(rept == null){
                setTimeout(function(){
                    callWhenMined(txHash,callback);
                },500);
            }else{
                callback();
            }
        }
    })
},

  //  刷新 获得的投票数
  refreshVote: function() {
    var self = this;

    var ballot;
      Ballot.deployed().then(function(instance) {
      meta = instance;
      return meta.p.call(account, {from: account});
    }).then(function(value) {
      var balance_element = document.getElementById("balance");
      balance_element.innerHTML = value.valueOf();
    }).catch(function(e) {
      console.log(e);
      self.setStatus("Error getting balance; see log.");
    });
  },


  whisper: function () {
      var shh = web3.shh;
      var appName = "My silly app!";
      var myName = "Gav Would";
      var myIdentity = shh.newIdentity();

      shh.post({
          "from": myIdentity,
          "topics": [ web3.fromAscii(appName) ],
          "payload": [ web3.fromAscii(myName), web3.fromAscii("What is your name?") ],
          "ttl": 100,
          "priority": 1000
      });


  }


};

window.addEventListener('load', function() {
  // Checking if Web3 has been injected by the browser (Mist/MetaMask)
  if (typeof web3 !== 'undefined') {
    console.warn("Using web3 detected from external source. If you find that your accounts don't appear or you have 0 MetaCoin, ensure you've configured that source properly. If using MetaMask, see the following link. Feel free to delete this warning. :) http://truffleframework.com/tutorials/truffle-and-metamask")
    // Use Mist/MetaMask's provider
    window.web3 = new Web3(web3.currentProvider);
  } else {
    console.warn("No web3 detected. Falling back to http://localhost:8545. You should remove this fallback when you deploy live, as it's inherently insecure. Consider switching to Metamask for development. More info here: http://truffleframework.com/tutorials/truffle-and-metamask");
    // fallback - use your fallback strategy (local node / hosted node + in-dapp id mgmt / fail)
    window.web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));
      // 合约api
  }

  // App.whisper();
  vote.start();
    initFileInput("input-1", "/file");

});
