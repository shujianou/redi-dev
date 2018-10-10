var API_PATH = 'http://' + window.location.host + '/v';
console.log(window.location.host);
var MONITOR_PATH = 'http://' + window.location.host;

//localStorage封装
var store = {
    set: function (key, val, exp) {
        localStorage.setItem(key, JSON.stringify({val: val, exp: exp, time: new Date().getTime()}));
    },

    get: function (key) {

        var info = JSON.parse(localStorage.getItem(key));
        if (!info) {
            return null;
        }

        if (new Date().getTime() - info.time > info.exp) {
            localStorage.removeItem(key);
            return null;
        }

        return info.val
    },
    remove: function (key) {
        localStorage.removeItem(key);
    }
}
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
$(function () {
    // Waves初始化
    Waves.displayEffect();
    // 数据表格动态高度
    $(window).resize(function () {
        $('#table').bootstrapTable('resetView', {
            height: getHeight()
        });
    });
    // 设置input特效
    $(document).on('focus', 'input[type="text"]', function () {
        $(this).parent().find('label').addClass('active');
    }).on('blur', 'input[type="text"]', function () {
        if ($(this).val() == '') {
            $(this).parent().find('label').removeClass('active');
        }
    });
});

// 动态高度
function getHeight() {
    return $(window).height() - 20;
}

// 数据表格展开内容
function detailFormatter(index, row) {
    var html = [];
    $.each(row, function (key, value) {
        html.push('<p><b>' + key + ':</b> ' + value + '</p>');
    });
    return html.join('');
}

// 初始化input特效
function initMaterialInput() {
    $('form input[type="text"]').each(function () {
        if ($(this).val() != '') {
            $(this).parent().find('label').addClass('active');
        }
    });
}

$.ajaxSetup({
    xhrFields: {
        withCredentials: true   //允许携带cookie
    }
})
/**
 * 权限异常拦截
 */
$(document).ajaxComplete(function (evt, req, settings) {
    if (req && req.responseJSON) {
        var json = req.responseJSON;
        if (json.code === 403 && json.info === 'perm error' && !json.success) {
            window.location.href = location.protocol + '//' + location.hostname;
            return;
        }
        if (json.code === 404 && !json.success) {
            window.location.href = location.protocol + '//' + location.hostname + '/404.html';
        }
    }
});

/**
 * 捕获全局异常
 */
$(document).ajaxError(function (e, xhr, optionx, exc) {
    if (xhr.responseJSON) {
        var data = xhr.responseJSON;
        if (data.code && data.code != 200) {
            $.message({
                message: data.code + ':' + data.msg,
                type: 'error'
            })
        } else {
            var status = data.status ? data.status : 400;
            var msg;

            if (data.message) {
                msg = data.message;
            } else {
                msg = '系统未知错误,请联系网络管理员';
            }
            $.message({
                message: status + ':' + msg,
                type: 'error'
            })
        }
    } else if (xhr.status == 0) {

        //location.href = MONITOR_PATH+'/login.html';
    } else {
        $.message({
            message: xhr.status,
            type: 'error'
        })
    }
});

$(function () {

    $.check = {

        //验证邮箱的合法性
        email: function (value) {
            var exp = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
            var reg = value.match(exp);
            if (reg == null) {
                return false;
            }
            return true;
        },

        //验证IP地址的合法性
        ip: function (value) {
            var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            var reg = value.match(exp);
            if (reg == null) {
                return false;
            }
            return true;
        }
    }

    /*权限工具*/
    $.security = {

        //获取当前登录用户信息(从cookies取)
        getUser: function () {
            var user = store.get('user');
            if (user) {
                return JSON.parse(user);
            }
            return null;
        },
        //刷新用户信息
        refreshUser: function () {
            $.ajax({
                url: API_PATH + '/security/info',
                type: 'post',
                async: false,
                success: function (res) {
                    var user = res.data.user;
                    var resources = res.data.resources;
                    store.set('user', user, 60 * 60 * 1000);
                    store.set('resources', resources, 60 * 60 * 1000);
                    /*       $.cookie('user', JSON.stringify(user));
                           $.cookie('resources', JSON.stringify(resources));*/
                    $('#aUser').html('<span class="tpl-header-list-user-nick">' + user.userName + '</span><span class="tpl-header-list-user-ico"> <img src="' + user.avatarUrl + '"></span>');
                }
            })
        },
        logut: function () {
            $.ajax({
                url: API_PATH + '/security/logout',
                type: 'post',
                success: function (res) {
                    store.remove('user');
                    store.remove('resources');
                    window.location.href = 'login.html';
                }
            })
        }
    };


    $.model = {
        //渲染左侧菜单资源(暂时只支持三级菜单)
        loadResources: function () {
            var resources = store.get('resources');
            var user = store.get('user');


            if (!user || !resources) {
                window.location.href = 'login.html?t=' + new Date().getTime();
            }
            $('.sp-info').html(user.userName + ',您好!<i class="zmdi zmdi-caret-down"></i>');
            $('.sp-pic img').attr('src', user.avatarUrl);
            var menuStr = menu(resources);
            $('.resources-menu').html(menuStr);

            function menu(res) {
                var root = '<ul class="main-menu">';
                for (var i = 0; i < res.length; i++) {

                    if (res[i].items && res[i].items.length > 0) {
                        root += '<li class="sub-menu">';
                        root += '<a class="waves-effect" href="javascript:Tab.addTab(\'' + res[i].name + '\', \'' + MONITOR_PATH + res[i].url + '\');"><i class="' + res[i].icon + '"></i> ' + res[i].name + '</a>';
                        root += menu(res[i].items);
                    } else {
                        root += '<li>';
                        root += '<a class="waves-effect" href="javascript:Tab.addTab(\'' + res[i].name + '\', \'' + MONITOR_PATH + res[i].url + '\');"><i class="' + res[i].icon + '"></i> ' + res[i].name + '</a>';
                    }
                    root += '</li>';
                }
                root += '</ul>';
                return root;
            }

            function menu_1(res) {
                var menu_1 = '<ul class="tpl-left-nav-sub-menu">';
                for (var i = 0; i < res.length; i++) {
                    menu_1 += '<li>';

                    if (res[i].items) {
                        menu_1 += '<a a-href="' + res[i].url + '" class="res-jump tpl-left-nav-link-list" res-name="' + res[i].name + '"><i class="' + res[i].icon + '"></i><span>' + res[i].name + '</span><i class="am-icon-angle-right tpl-left-nav-more-ico am-fr am-margin-right"></i></a>';
                        menu_1 += subMenu(res[i].items);
                    } else {
                        menu_1 += '<a a-href="' + res[i].url + '" class="res-jump" res-name="' + res[i].name + '"><i class="' + res[i].icon + '"></i><span>' + res[i].name + '</span></a>';
                    }
                    menu_1 += '</li>';
                }
                menu_1 += '</ul>';
                return menu_1;
            }

            function subMenu(res) {
                var subMenu = '<ul class="tpl-left-nav-sub-menu"><li>';
                //var subMenu='';
                for (var i = 0; i < res.length; i++) {
                    subMenu += '<a a-href="' + res[i].url + '" class="res-jump right-move tpl-left-nav-link-list" res-name="' + res[i].name + '"><i class="' + res[i].icon + '"></i><span>' + res[i].name + '</span></a>';
                }
                subMenu += '</li></ul>';
                return subMenu;
            }

        }
    };
})