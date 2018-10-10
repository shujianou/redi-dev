/*var API_PATH='http://irany:8081';
var HOTEL_PATH='/hotel';*/
$(function() {
	// Waves初始化
	Waves.displayEffect();
	// 输入框获取焦点后出现下划线
	$('.form-control').focus(function() {
		$(this).parent().addClass('fg-toggled');
	}).blur(function() {
		$(this).parent().removeClass('fg-toggled');
	});
});
Checkbix.init();
$(function() {
	// 点击登录按钮
	$('#login-bt').click(function() {
		login();
	});
	// 回车事件
	$('#username, #password').keypress(function (event) {
		if (13 == event.keyCode) {
			login();
		}
	});

    // 登录
    function login() {
        $.ajax({
            url: API_PATH + '/security/login',
            type: 'POST',

            data: {
                account: $('#username').val(),
                password: $('#password').val(),
                rememberMe: $('#rememberMe').is(':checked')
            },
            beforeSend: function() {

            },
            success: function(json){
                
                if (json.code == 200) {
                    //设置cookie
                    document.cookie = "JSESSIONID="+json.data.token+";path=/;domain="+API_PATH.split('://')[1].split(':')[0];
                    $.security.refreshUser();
                    location.href = 'index.html';
                } else {
                   	if (json.code!=200){
                        $.message({
                            type:'error',
                            message:json.msg
                        })
					}
                }
            },
            error: function(error){
                console.log(error);
            }
        });
    }

    /**
     * 获取资源菜单
     */
    function resourceNode() {

       /* $.ajax({
            url:API_PATH+'/resources/menuNodeList',
            type:'post',
            success:function (res) {
                if (res.code=200){
                    $.cookie('resources',res.data,{
                        expires:7
                    });
                }
            }
        })*/
    }
});
