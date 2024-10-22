<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>Ajax 실시간 익명 채팅 사이트</title>
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		var lastID= 0;
		function submitFunction() {
		    var chatName = $('#chatName').val();
		    var chatContent = $('#chatContent').val();

		    $.ajax({
		        type: "POST",
		        url: "./chatSubmitServlet",
		        data: {
		            chatName: encodeURIComponent(chatName) ,
		            chatContent: encodeURIComponent(chatContent)
		        },
		        success: function (result) {
		            if (result == 1) {
		                autoClosingAlert('#successMessage', 2000);
		                // 새로운 메시지가 전송되면 채팅 리스트 갱신
		                chatListFunction('ten');
		            } else if (result == 0) {
		                autoClosingAlert('#dangerMessage', 2000);
		            } else {
		                autoClosingAlert('#warningMessage', 2000);
		            }
		        }
		    });

		    $('#chatContent').val('');
		}

		
		function autoClosingAlert(selector, delay) {
			var alert = $(selector).alert();
			alert.show();
			window.setTimeout(function() {
				alert.hide()},delay);
		}
		
		function chatListFunction(type) {
			console.log("type : "+ type);
		    $.ajax({
		        type: "POST",
		        url: "./chatListServlet",
		        data: {
		            listType: type		        },
		        success: function (data) {
		            if (data == "") return;
		            console.log("Response from server: ", data);
		            
		            var parsed = JSON.parse(data);
		            var result = parsed.result;

		            // 새로운 채팅을 추가
		            for (var i = 0; i < result.length; i++) {
		                addChat(result[i][0].value, result[i][1].value, result[i][2].value);
		            }
		            
		            // 마지막 chatID 갱신
		            if (parsed.last) {
		                lastID = Number(parsed.last);
		                console.log("Updated lastID: " + lastID);
		            }
		        },
		        error: function (xhr, status, error) {
		            console.log("Error: " + error);
		        }
		    });
		}
		
		
		function addChat(chatName, chatContent, chatTime) {
		    $('#chatList').append('<div class="row">' +
		        '<div class="col-lg-12">' +
		            '<div class="media">' +
		                '<a class="pull-left" href="#">' +
		                    '<img class="media-object img-circle" src="images/icon.png" alt="" style="width: 48px; height: 48px;">' +
		                '</a>' +
		                '<div class="media-body">' +
		                    '<h4 class="media-heading">' +
		                        chatName +
		                        '<span class="small pull-right">' +
		                            chatTime +
		                        '</span>' +
		                    '</h4>' +
		                    '<p>' +
		                        chatContent +
		                    '</p>' +
		                '</div>' +
		            '</div>' +
		        '</div>' +
		        '<hr>');
		    $("#chatList").scrollTop($("#chatList")[0].scrollHeight);
		}

		
		function getInfiniteChat(){
 			setInterval(function () {
				chatListFunction(lastID);
			},1000); 
		}
		
		
	</script>
</head>
<body>

<div class="container">
	<div class="container bootstrap snippet">
		<div class="row">
			<div class="col-xs-12">
				<div class="portlet portlet-default">
					<div class="portlet-heading">
						<div class="portlet-title">
							<h4><i class="fa fa-circle text-green"></i>실시간 채팅방</h4>
						</div>
						<div class="clearfix"></div>
					</div>
					<div id="chat" class="panel-collapse callapse in">
						<div id="chatList" class="portlet-body chat-widget" style="overflow-y: auto; width: auto; height: 600px;">
						
						</div>
						<div class="portlet-footer">
							<div class="row">
								<div class="form-group col-xs-4">
									<input style="height: 40px;" type="text" id="chatName" class="form-control"placeholder="이름" maxlength="20">
								</div>
							</div>
							<div class="row" style="height: 90px">
								<div class="form-group col-xs-10">
									<textarea style="height: 80px;" id="chatContent" class="form-control" placeholder="메세지를 입력하세요" maxlength="100"></textarea>
								</div>
								<div class="form-group col-xs-2">
									<button type="button" class="btn btn-default pull-right" onclick="submitFunction();">전송</button>
									<div class="clearfix"></div>
								</div>
<!-- 								<div class="form-group col-xs-2">
									<button type="button" class="btn btn-default pull-right" onclick="getInfiniteChat();">갱신</button>
									<div class="clearfix"></div>
								</div> -->
							</div>
						</div>
					</div>
				</div>
			</div>		
		</div>
	</div>
	<div class="alert alert-success" id="successMessage" style="display: none;">
		<strong>메세지 전송에 성공했습니다</strong>
	</div>
	<div class="alert alert-danger" id="dangerMessage" style="display: none;">
		<strong>이름과 내용을 모두 입력해 주세요</strong>
	</div>
	<div class="alert alert-warning" id="warningMessage" style="display: none;">
		<strong>디비 오류가 발생했습니다.</strong>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function () {
		chatListFunction('ten');
	});
</script>
</body>
</html>