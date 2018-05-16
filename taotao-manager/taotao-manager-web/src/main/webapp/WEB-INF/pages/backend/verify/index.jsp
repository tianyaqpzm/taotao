<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <jsp:include page="../../common/bootstrap.jsp" flush="false" />
    <link rel="stylesheet" href="../../../../public/css/steps.css">

</head>
<body style="margin-top: 50px;">
<jsp:include page="../common/navbar.jsp" flush="false" />
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2" id="sidebar" style="padding: 0;">
            <jsp:include page="../common/sidebar.jsp" flush="false" />
        </div>
        <div class="col-sm-9 col-md-10">
            <h3 class="page-header header">完整性验证<small>Welcome </small></h3>
            <form id="msform" >
                <!-- progressbar -->
                <ul id="progressbar" style="background: lightslategray">
                    <li class="active">Merkle Root</li>
                    <li >Unlock Address</li>
                    <li>Register</li>
                    <li>Commit</li>
                    <li>Cast</li>
                    <li>Tally</li>
                </ul>
                <!-- The Steps1 -->
                <fieldset id="merkleRoot">
                    <h2> Computing Merkle Root </h2>
                    <br>
                    <div id="dropdown1">
                        <input id="uploadTrigger" name="next" class="action-button" value="Upload" type="button" onclick='openFile()'>
                        <input id="done" class="hidden next" type="button">
                    </div>
                </fieldset>
                <fieldset id="toChain">
                    <div id="dropdown">
                        <p>Address:</p>
                        <select id='addrs' class="action-list">
                            <option value='0'>None</option>
                        </select>
                        <br>
                        <br>
                        <p>None of your Ethereum accounts are eligible to vote...</p>
                    </div>
                </fieldset>
                <fieldset id="registerfs">
                    <div>
                        <p id="registerwait">Please wait.... Registration will begin soon.</p>
                        <div hidden id="registerready">
                            <h2 id="question3"></h2>
                            <br>
                            <p>You have <span id="balance"></span> ether.</p>
                            <br>
                            <p id="asktoregister"> Registration deposit is <span id="deposit"></span> ether.
                                <br>
                            <p> Would you like to register for this vote?</p>
                            <input id="registerbutton" class="action-button" type="button" value="Register" onclick="register();">
                            <p hidden id = "submitvotingkey">Waiting for Ethereum to confirm your voting key.</p>
                            <br>
                            <p id="registerby"></p>
                            <br>
                            <p id="registerrefundby"></p>
                            <br>
                            <p id = "registrationprogress"></p>
                        </div>
                        <div hidden id="resetbutton" style="text-align: center">
                            <p>The Election Authority should have finished registration before <span id="regclock"></span></p>
                            <br>
                            <p>Do you want to cancel the Election and get your deposit back?</p>
                            <br>
                            <div id="reset1">
                                <input class="action-button" type="button" value="Yes" onclick="resetElection();"></button>
                            </div>
                            <div hidden id="reset1-msg">
                                <p>Waiting for Ethereum to cancel the election.</p>
                            </div>
                        </div>
                    </div>

                </fieldset>

                <fieldset id="commitfs">
                    <h2 id="question2"></h2>
                    <br>
                    <div id="vote">
                        <button onclick="vote(1)" class="action-button">Yes</button>
                        <button onclick="vote(0)" class="action-button">No</button>
                    </div>
                    <br>
                    <p id="no_vote_waiting"></p>

                    <div hidden id="resetbutton3" style="text-align: center">
                        <p>All commitments should have been cast before <span id="regclock3"></span></p>
                        <br>
                        <p>Do you want to cancel the Election and get your deposit back?</p>
                        <br>
                        <div id="reset3">
                            <input class="action-button" type="button" value="Yes" onclick="resetElection();"></button>
                        </div>
                        <div hidden id="reset3-msg">
                            <p>Waiting for Ethereum to cancel the election.</p>
                        </div>
                    </div>
                    <br>
                    <p id="commitby"></p>
                </fieldset>

                <fieldset id="votefs">
                    <h2 id="question"></h2>
                    <br>
                    <div id="do_vote">
                        <button onclick="vote(1)" class="action-button">Yes</button>
                        <button onclick="vote(0)" class="action-button">No</button>
                    </div>
                    <br>
                    <p id="vote_waiting"></p>

                    <div hidden id="resetbutton4" style="text-align: center">
                        <p>All votes should have been cast before <span id="regclock4"></span></p>
                        <br>
                        <p>Do you want to cancel the Election and get your deposit back?</p>
                        <br>
                        <div id="reset4">
                            <input class="action-button" type="button" value="Yes" onclick="resetElection();"></button>
                        </div>
                        <div hidden id="reset4-msg">
                            <p>Waiting for Ethereum to cancel the election.</p>
                        </div>
                    </div>
                    <br>
                    <p id="voteby"></p>

                </fieldset>

                <fieldset id="tallyfs">
                    <h2 id="question4"></h2>
                    <br>
                    <div id="result">
                    </div>
                    <br>
                    <hr>
                    <br>
                    <div hidden id="refund-valid">
                        <p>Claim your deposit of <span id="refund"></span> ether before <span id="refundclock"></span></p>
                        <input id="claimrefundbutton" type="button" value="Refund" onclick="claimrefund();" class="action-button" />
                        <p hidden id="waitingforrefund">Waiting for Ethereum to confirm your refund</p>
                    </div>
                    <div hidden id="refund-notvalid">
                        <p>Refund already claimed, or not available. </p>
                    </div>
                </fieldset>
            </form>

        </div>

        </div>
</div>
<script type="text/javascript">

    // borrowed from http://stackoverflow.com/questions/14895287/how-to-print-object-array-in-javascript
    function print_r(printthis, returnoutput) {
        var output = '';

        if($.isArray(printthis) || typeof(printthis) == 'object') {
            for(var i in printthis) {
                output += i + ' : ' + print_r(printthis[i], true) + '\n';
            }
        }else {
            output += printthis;
        }
        if(returnoutput && returnoutput == true) {
            return output;
        }else {
            append(output);
        }
    }


    var openFile = function() {

        // 发送ajax 请求，计算merkle root  成功返回true
        $.ajax({
            type: "GET",
            <%--url: "${pageContext.request.contextPath}/backend/pretreatment/cutfile",--%>
            url: "/backend/verify/merkle",
//            data: {"fileurl":$("#fileurl").val()},
            dataType: "json",
            success: function(data){
//                $('#resText').empty();   //清空resText里面的所有内容
//                var html = '';
//                $.each(data, function(commentIndex, comment){
//                    html += '<div class="comment"><h6>' + comment['username']
//                        + ':</h6><p class="para"' + comment['content']
//                        + '</p></div>';
//                });
//                $('#resText').html(html);
                if(data !== " "){
                    alert("suceeess");
                    $("#merkleRoot").hide();
                    $("#toChain").show();
                    $("#progressbar li:eq(1)").addClass("active")
                }
            }
        });


        fileUploaded = true;
    };

//    setInterval("currentState()", 1000);

</script>
<script src="../../../../public/js/jquery.easing.min.js" type="text/javascript"></script>


</body>
</html>
